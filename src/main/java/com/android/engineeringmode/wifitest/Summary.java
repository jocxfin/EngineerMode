package com.android.engineeringmode.wifitest;

import android.content.Context;
import android.net.NetworkInfo.DetailedState;

class Summary {
    Summary() {
    }

    static String get(Context context, String ssid, DetailedState state) {
        String[] formats = context.getResources().getStringArray(ssid == null ? 2131099673 : 2131099674);
        int index = state.ordinal();
        if (index >= formats.length || formats[index].length() == 0) {
            return null;
        }
        return String.format(formats[index], new Object[]{ssid});
    }

    static String get(Context context, DetailedState state) {
        return get(context, null, state);
    }
}
