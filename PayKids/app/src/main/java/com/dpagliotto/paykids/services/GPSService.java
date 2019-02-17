package com.dpagliotto.paykids.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.dpagliotto.paykids.R;
import com.dpagliotto.paykids.view.helper.Helper;

/**
 * Created by davidpagliotto on 14/08/17.
 */

public class GPSService extends Service implements LocationListener {

    private static GPSService instance;
    private boolean canGetLocation;

    private Context mContext;
    private Location location;
    private LocationManager locationManager;

    private final Integer FACTOR = 1000000;

    public static void instantiate(Context context) {
        instance = new GPSService();
        instance.mContext = context;
    }

    public static GPSService getInstance(boolean accessLocation) {
        if (instance != null && accessLocation)
            instance.getLocation();

        return instance;
    }

    @SuppressLint("MissingPermission")
    private Location getLocation() {
        if (location == null || !location.getProvider().equals(LocationManager.GPS_PROVIDER)) {

            canGetLocation = false;
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            if (locationManager != null) {
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (isGPSEnabled || isNetworkEnabled) {
                    canGetLocation = true;

                    long MIN_DISTANCE_UPD = 10;
                    long MIN_TIME_UPD = 1000;

                    try {
                        Looper.prepare();
                    } catch (Exception e) {
                        // Do Nothing
                    } finally {
                        if (isNetworkEnabled) {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPD, MIN_DISTANCE_UPD, this);
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }

                        if (isGPSEnabled) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPD, MIN_DISTANCE_UPD, this);
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }
        }

        return location;
    }

    public double getLatitude() {
        try {
            int idx = 0;
            do {
                location = instance.getLocation();
                if (idx++ > 10) break;
            } while ((location == null) || (location.hasAccuracy() && location.getAccuracy() > 25) || (location.getLatitude() == 0 && location.getLongitude() == 0));

            if (location != null)
                return location.getLatitude();
        } catch (Exception e) {}

        return 0.0;
    }

    public double getLongitude() {
        try {
            int idx = 0;
            do {
                location = instance.getLocation();
                if (idx++ > 10) break;
            } while ((location == null) || (location.hasAccuracy() && location.getAccuracy() > 25) || (location.getLatitude() == 0 && location.getLongitude() == 0));

            if (location != null)
                return location.getLongitude();
        } catch (Exception e) {}

        return 0.0;
    }

    public boolean canGetLocation() {
        location = null;
        location = getLocation();

        return (canGetLocation && locationManager.getProviders(true).size() > 0);
    }

    public boolean checkCanGetLocation(Context context) {
        if (!this.canGetLocation()) {
            //Helper.customAlertDialog(context, R.string.msg_active_gps, Helper.neutralBtnOK(null));
            return false;
        }

        return true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
