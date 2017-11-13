package com.qualcomm.qti.sensors.core.sensortest;

import android.util.Log;

public class SensorTest {
    private static final String TAG = "SensorTest";

    public enum DataType {
        PRIMARY,
        SECONDARY
    }

    public enum TestType {
        SELFTEST,
        IRQ,
        CONNECTIVITY,
        SELFTEST_HW,
        SELFTEST_SW,
        OEMTEST
    }

    private static native int getNativeRawDataMode();

    private static native int runNativeSensorTest(int i, int i2, int i3);

    private static native int runNativeSensorTest(int i, int i2, int i3, boolean z, boolean z2);

    private static native int sendNativeDAFRequest(int i, int i2);

    private static native int setNativeRawDataMode(boolean z);

    static {
        System.loadLibrary("sensor_test");
    }

    public static synchronized int runSensorTest(SensorID sensorID, DataType dataType, TestType testType, boolean saveToRegistry, boolean applyCalNow) {
        synchronized (SensorTest.class) {
            if (sensorID == null) {
                Log.e(TAG, "SensorID must not be NULL");
                return -2;
            }
            int nativeTestResult = runNativeSensorTest(sensorID.getSensorID(), dataType.ordinal(), testType.ordinal(), saveToRegistry, applyCalNow);
            return nativeTestResult;
        }
    }

    public static synchronized int runSensorOffsetTest(int sensorid, int dataType, int testType, boolean saveToRegistry, boolean applyCalNow) {
        int nativeTestResult;
        synchronized (SensorTest.class) {
            nativeTestResult = runNativeSensorTest(sensorid, dataType, testType, saveToRegistry, applyCalNow);
        }
        return nativeTestResult;
    }

    public static int runSensorTest(SensorID sensorID, DataType dataType, TestType testType) {
        return runSensorTest(sensorID, dataType, testType, true, true);
    }

    public static synchronized void setRawDataMode(boolean enabled) throws Exception {
        synchronized (SensorTest.class) {
            int result = setNativeRawDataMode(enabled);
            if (result != 0) {
                throw new Exception("Unknown error occurred within native code: " + result);
            }
        }
    }

    public static synchronized boolean getRawDataMode() {
        boolean z = true;
        synchronized (SensorTest.class) {
            if (getNativeRawDataMode() != 1) {
                z = false;
            }
        }
        return z;
    }

    public static int sendDAFRequest(int uuid_index, int daf_req_id) {
        return sendNativeDAFRequest(uuid_index, daf_req_id);
    }
}
