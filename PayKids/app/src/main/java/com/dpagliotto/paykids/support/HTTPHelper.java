package com.dpagliotto.paykids.support;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by davidpagliotto on 16/08/17.
 */

public class HTTPHelper {

    public static String URL_BASE = "http://a2cdf181.ngrok.io/time11";

    public static String URL_TITULARES_POST = HTTPHelper.URL_BASE + "/titulares";

    public static String URL_TITULARES_EMAIL_GET = HTTPHelper.URL_BASE + "/titulares/email/%s";

    public static String URL_DEPENDENTES_EMAIL_GET = HTTPHelper.URL_BASE + "/titulares/dependentes/%s";

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null)
            networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

}
