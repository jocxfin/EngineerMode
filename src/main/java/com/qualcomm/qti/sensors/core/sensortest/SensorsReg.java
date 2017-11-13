package com.qualcomm.qti.sensors.core.sensortest;

public class SensorsReg {
    private static final String TAG = "SensorReg";

    public static native int close();

    public static native byte[] getRegistryValue(int i);

    public static native int open();

    public static native int setRegistryValue(int i, byte[] bArr, byte b);

    static {
        System.loadLibrary("sensor_reg");
    }
}
