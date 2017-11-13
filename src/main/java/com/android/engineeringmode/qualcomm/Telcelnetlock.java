package com.android.engineeringmode.qualcomm;

public class Telcelnetlock {
    public static native boolean addUnlockTimes();

    public static native int check();

    public static native boolean clear();

    public static native int getUnlockTimes();

    public static native boolean match(String str, String str2);

    public static native boolean recover();

    static {
        System.loadLibrary("telcelnetlock");
    }
}
