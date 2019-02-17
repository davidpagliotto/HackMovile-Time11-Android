package com.dpagliotto.paykids.support;

public class HTTPHelper {

    public static String URL_BASE = "http://a2cdf181.ngrok.io/time11";

    public static String URL_TITULARES_POST = HTTPHelper.URL_BASE + "/titulares";

    public static String URL_TITULARES_EMAIL_GET = HTTPHelper.URL_BASE + "/titulares/email/%s";

    public static String URL_DEPENDENTES_EMAIL_GET = HTTPHelper.URL_BASE + "/titulares/dependentes/%s";

}
