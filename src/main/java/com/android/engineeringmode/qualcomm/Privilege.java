package com.android.engineeringmode.qualcomm;

public class Privilege {
    public static native boolean escalate(String str);

    public static native boolean isEscalated();

    public static native void recover();

    static {
        System.loadLibrary("door");
    }
}
