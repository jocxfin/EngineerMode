package com.oem.util;

import android.annotation.SuppressLint;
import android.os.Build;

public class Utils {
    @SuppressLint({"NewApi"})
    public static boolean isOppoFind7() {
        if ("OPPO".equals(Build.MANUFACTURER)) {
            return "X9007".equals(Build.MODEL);
        }
        return false;
    }

    public static boolean isOppoFind7WCDMA() {
        if ("OPPO".equals(Build.MANUFACTURER)) {
            return "X9000".equals(Build.MODEL);
        }
        return false;
    }

    @SuppressLint({"NewApi"})
    public static boolean isOnePlus() {
        return true;
    }

    @SuppressLint({"NewApi"})
    public static boolean isOppoFind7s() {
        if ("OPPO".equals(Build.MANUFACTURER)) {
            return "X9077".equals(Build.MODEL);
        }
        return false;
    }

    @SuppressLint({"NewApi"})
    public static boolean isOppoFind7sWCDMA() {
        if ("OPPO".equals(Build.MANUFACTURER)) {
            return "X9070".equals(Build.MODEL);
        }
        return false;
    }

    @SuppressLint({"NewApi"})
    public static boolean isOppoN5207() {
        if ("OPPO".equals(Build.MANUFACTURER)) {
            return "N5207".equals(Build.MODEL);
        }
        return false;
    }

    @SuppressLint({"NewApi"})
    public static boolean isOppoFind7Exp() {
        if ("OPPO".equals(Build.MANUFACTURER)) {
            return "X9006".equals(Build.MODEL);
        }
        return false;
    }

    @SuppressLint({"NewApi"})
    public static boolean isOppoFind7sExp() {
        if ("OPPO".equals(Build.MANUFACTURER)) {
            return "X9076".equals(Build.MODEL);
        }
        return false;
    }

    @SuppressLint({"NewApi"})
    public static boolean isOppoN5206() {
        if ("OPPO".equals(Build.MANUFACTURER)) {
            return "N5206".equals(Build.MODEL);
        }
        return false;
    }
}
