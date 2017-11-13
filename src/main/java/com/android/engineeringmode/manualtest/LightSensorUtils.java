package com.android.engineeringmode.manualtest;

import android.content.Context;

public class LightSensorUtils {
    private Context mContext;

    public native void closeLightSensor();

    public native int[] getADC(int i);

    public native int openLightSensor();

    public native int[] startAdjust();

    public native String startMsensorAutoTest();

    public LightSensorUtils(Context context) {
        this.mContext = context;
    }

    static {
        System.loadLibrary("light_jni");
    }
}
