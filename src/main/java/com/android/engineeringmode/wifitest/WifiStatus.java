package com.android.engineeringmode.wifitest;

import android.content.Context;
import android.net.NetworkInfo.DetailedState;
import android.text.TextUtils;

import com.android.engineeringmode.Log;
import com.android.engineeringmode.functions.Light;

public class WifiStatus {
    private static final /* synthetic */ int[] -android-net-NetworkInfo$DetailedStateSwitchesValues =null;

    private static /* synthetic */ int[] -getandroid-net-

    NetworkInfo$DetailedStateSwitchesValues() {
        if (-android - net - NetworkInfo$DetailedStateSwitchesValues != null) {
            return -android - net - NetworkInfo$DetailedStateSwitchesValues;
        }
        int[] iArr = new int[DetailedState.values().length];
        try {
            iArr[DetailedState.AUTHENTICATING.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[DetailedState.BLOCKED.ordinal()] = 9;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[DetailedState.CAPTIVE_PORTAL_CHECK.ordinal()] = 10;
        } catch (NoSuchFieldError e3) {
        }
        try {
            iArr[DetailedState.CONNECTED.ordinal()] = 2;
        } catch (NoSuchFieldError e4) {
        }
        try {
            iArr[DetailedState.CONNECTING.ordinal()] = 3;
        } catch (NoSuchFieldError e5) {
        }
        try {
            iArr[DetailedState.DISCONNECTED.ordinal()] = 4;
        } catch (NoSuchFieldError e6) {
        }
        try {
            iArr[DetailedState.DISCONNECTING.ordinal()] = 5;
        } catch (NoSuchFieldError e7) {
        }
        try {
            iArr[DetailedState.FAILED.ordinal()] = 6;
        } catch (NoSuchFieldError e8) {
        }
        try {
            iArr[DetailedState.IDLE.ordinal()] = 11;
        } catch (NoSuchFieldError e9) {
        }
        try {
            iArr[DetailedState.OBTAINING_IPADDR.ordinal()] = 7;
        } catch (NoSuchFieldError e10) {
        }
        try {
            iArr[DetailedState.SCANNING.ordinal()] = 8;
        } catch (NoSuchFieldError e11) {
        }
        try {
            iArr[DetailedState.SUSPENDED.ordinal()] = 12;
        } catch (NoSuchFieldError e12) {
        }
        try {
            iArr[DetailedState.VERIFYING_POOR_LINK.ordinal()] = 13;
        } catch (NoSuchFieldError e13) {
        }
        -android - net - NetworkInfo$DetailedStateSwitchesValues = iArr;
        return iArr;
    }

    public static String getStatus(Context context, String ssid, DetailedState detailedState) {
        if (TextUtils.isEmpty(ssid) || !isLiveConnection(detailedState)) {
            return getPrintable(context, detailedState);
        }
        return getPrintableFragment(context, detailedState, ssid);
    }

    public static boolean isLiveConnection(DetailedState detailedState) {
        if (detailedState == DetailedState.DISCONNECTED || detailedState == DetailedState.FAILED || detailedState == DetailedState.IDLE || detailedState == DetailedState.SCANNING) {
            return false;
        }
        return true;
    }

    public static boolean isConnectting(DetailedState detailedState) {
        if (detailedState == DetailedState.CONNECTING || detailedState == DetailedState.OBTAINING_IPADDR) {
            return true;
        }
        return false;
    }

    public static boolean isConnected(DetailedState detailedState) {
        return detailedState == DetailedState.CONNECTED;
    }

    public static String getPrintable(Context context, DetailedState detailedState) {
        switch (-getandroid - net - NetworkInfo$DetailedStateSwitchesValues()[detailedState.ordinal()]) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                Log.i("WifiStatus", "AUTHENTICATING");
                return context.getString(2131296442);
            case Light.CHARGE_RED_LIGHT /*2*/:
                Log.i("WifiStatus", "CONNECTED");
                return context.getString(2131296444);
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                Log.i("WifiStatus", "CONNECTING");
                return context.getString(2131296441);
            case 4:
                Log.i("WifiStatus", "DISCONNECTED");
                return context.getString(2131296446);
            case 5:
                Log.i("WifiStatus", "DISCONNECTING");
                return context.getString(2131296445);
            case Light.MAIN_KEY_NORMAL /*6*/:
                Log.i("WifiStatus", "FAILED");
                return context.getString(2131296447);
            case 7:
                Log.i("WifiStatus", "OBTAINING_IPADDR");
                return context.getString(2131296443);
            case 8:
                Log.i("WifiStatus", "SCANNING");
                return context.getString(2131296440);
            default:
                return null;
        }
    }

    public static String getPrintableFragment(Context context, DetailedState detailedState, String apName) {
        String fragment = null;
        switch (-getandroid - net - NetworkInfo$DetailedStateSwitchesValues()[detailedState.ordinal()]) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                fragment = context.getString(2131296434);
                Log.i("WifiStatus", "getPrintableFragment, AUTHENTICATING");
                break;
            case Light.CHARGE_RED_LIGHT /*2*/:
                fragment = context.getString(2131296436);
                Log.i("WifiStatus", "getPrintableFragment, CONNECTED");
                break;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                fragment = context.getString(2131296433);
                Log.i("WifiStatus", "getPrintableFragment, CONNECTING");
                break;
            case 4:
                fragment = context.getString(2131296438);
                Log.i("WifiStatus", "getPrintableFragment, DISCONNECTED");
                break;
            case 5:
                fragment = context.getString(2131296437);
                Log.i("WifiStatus", "getPrintableFragment, DISCONNECTING");
                break;
            case Light.MAIN_KEY_NORMAL /*6*/:
                fragment = context.getString(2131296439);
                Log.i("WifiStatus", "getPrintableFragment, FAILED");
                break;
            case 7:
                fragment = context.getString(2131296435);
                Log.i("WifiStatus", "getPrintableFragment, OBTAINING_IPADDR");
                break;
            case 8:
                fragment = context.getString(2131296432);
                Log.i("WifiStatus", "SCANNING");
                break;
        }
        return String.format(fragment, new Object[]{apName});
    }
}
