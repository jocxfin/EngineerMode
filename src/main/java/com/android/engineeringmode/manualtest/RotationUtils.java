package com.android.engineeringmode.manualtest;

public class RotationUtils {
    public static native void close_motor();

    public static native int motor_rotation_set_para(int i, int i2, int i3, int i4, int i5);

    public static native int motor_rotation_test(int i, int i2, int i3, int i4);

    public static native int open_motor();

    public static native void set_fill_flash_brightness(int i);

    public native short[] calibrate_hall();

    public native void close_hall();

    public native int get_calibrate_adc_data();

    public native int get_pswitch_value();

    public native int[] hall_test();

    public native int open_hall();

    public native int set_hall_calibrate_data(short s, short s2, short s3);

    static {
        System.loadLibrary("rotation_jni");
    }
}
