package com.xpping.windows10.utils;

import android.util.Log;


/**
 * log管理	@author bananas
 */
public final class Windows10Loger {
    private static boolean IS_DEBUG = true;
    private static boolean DEBUG_LOG = false;
    private static boolean SHOW_ACTIVITY_STATE = true;
    private final static String TAG = "test";
    private final static String URL_TAG = "other";

    public static void openDebutLog(boolean enable) {
        IS_DEBUG = enable;
        DEBUG_LOG = enable;
    }

    public static void openActivityState(boolean enable) {
        SHOW_ACTIVITY_STATE = enable;
    }

    public static void debug(String msg) {
        if (IS_DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void debugUrl(String msg) {
        if (IS_DEBUG) {
            Log.d(URL_TAG, msg);
        }
    }

    public static void log(String packName, String state) {
        debugLog(packName, state);
    }

    public static void debug(String msg, Throwable tr) {
        if (IS_DEBUG) {
            Log.i(TAG, msg, tr);
        }
    }

    public static void state(String packName, String state) {
        if (SHOW_ACTIVITY_STATE) {
            Log.d("activity_state", packName + state);
        }
    }

    public static void debugLog(String packName, String state) {
        if (DEBUG_LOG) {
            Log.d(TAG, packName + state);
        }
    }

    public static void exception(Exception e) {
        if (DEBUG_LOG) {
            e.printStackTrace();
        }
    }

    public static void debug(String msg, Object... format) {
        debug(String.format(msg, format));
    }
}
