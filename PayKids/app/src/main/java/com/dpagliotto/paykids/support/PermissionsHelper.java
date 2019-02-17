package com.dpagliotto.paykids.support;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by davidpagliotto on 03/08/17.
 */

public class PermissionsHelper {

    private static void requestPermission(Activity activity, String permission) {
        if (activity != null && permission != null) {
            if (!PermissionsHelper.checkPermission(activity, permission)) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, 1);
            }
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        if (context != null && permission != null)
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;

        return false;
    }

    public static void checkAllPermissions(Activity activity) {
        //if (!PermissionsHelper.checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION))
        //PermissionsHelper.requestPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

        if (!PermissionsHelper.checkPermission(activity, Manifest.permission.CAMERA))
            PermissionsHelper.requestPermission(activity, Manifest.permission.CAMERA);
    }

    public static boolean accessLocation(Context context) {
        if (context == null) return false;

        boolean result = PermissionsHelper.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (!result)
            result = PermissionsHelper.checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);

        return result;
    }

}
