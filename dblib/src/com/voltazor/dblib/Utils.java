package com.voltazor.dblib;

import android.os.Build;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Dmitriy Dovbnya
 * Date: 18.03.13
 * Time: 16:08
 * To change this template use File | Settings | File Templates.
 */
public enum Utils {
    i;

    private static final String TAG = Utils.class.getSimpleName();

    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public void log_d(String TAG, String message) {
        if (Const.DEBUG) {
            Log.d(TAG, message);
        }
    }

    public void log_e(String TAG, Exception e) {
        String msg = null;
        if (e != null) {
            msg = e.getMessage();
            e.printStackTrace();
        }
        Log.e(TAG, msg != null ? msg : "Exception!");
    }

    public void log_e(String TAG, String message, Exception e) {
        Log.e(TAG, message);
        if (e != null) {
            e.printStackTrace();
        }
    }

    public void log_e(String TAG, String message, Error e) {
        Log.e(TAG, message);
        if (e != null) {
            e.printStackTrace();
        }
    }

    public void log_e(String TAG, Error e) {
        String msg = null;
        if (e != null) {
            msg = e.getMessage();
            e.printStackTrace();
        }
        Log.e(TAG, msg != null ? msg : "Error!");
    }

    public void log_e(String TAG, String message) {
        Log.e(TAG, message);
    }
}
