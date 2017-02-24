package me.chon.hedge.utils;

import android.util.Log;

/**
 * Created by chon on 2017/2/24.
 * What? How? Why?
 */

public class LogUtils {
    private static final String TAG = "chon_den";
    private static final boolean DEBUG = true;

    public static void e(String message) {
        e(TAG,message);
    }

    public static void e(String tag,String message) {
        if (DEBUG) {
            Log.e(tag,message);
        }
    }

}
