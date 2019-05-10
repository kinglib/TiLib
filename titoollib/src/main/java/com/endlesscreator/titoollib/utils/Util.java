package com.endlesscreator.titoollib.utils;

import java.net.URLEncoder;
import java.nio.charset.Charset;

public class Util {

    public static String toString(Object aObj) {
        return aObj == null ? "" : aObj.toString();
    }

    public static String urlEncoder(String aStr) {
        try {
            return URLEncoder.encode(toString(aStr), Charset.defaultCharset().displayName()).replaceAll("\\+", "%20");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aStr;
    }

    public static String encodeMobile(String aStr) {
        if(aStr != null && aStr.length() == 11) {
            return aStr.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        }
        return aStr;
    }
}
