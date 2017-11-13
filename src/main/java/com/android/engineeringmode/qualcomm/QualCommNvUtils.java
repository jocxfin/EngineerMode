package com.android.engineeringmode.qualcomm;

public class QualCommNvUtils {
    public native int CRCcheckDynamicNV();

    public native int CRCcheckStaticNV();

    public native int CmpDynamicNV();

    public native int CmpStaticNV();

    public native int NVDump();

    public native int adjustForceBackup();

    public native int[] adjustNvBackup();

    public native int adjustNvSelf();

    public native int adjustStaticNv();

    public native int backupDynamicNv(int i);

    public native int backupNv();

    public native int backupStaticNv(int i);

    public native int[] getNvStatus();

    public native void nvForceSync();

    public native int restoreDynamicNv();

    public native int restoreNv();

    public native int restoreStaticNv();

    public native void setAdjustRestoreEnable(int i);

    public native int setDiagEnbaled(boolean z);

    public native void setStaicRestoreEnable(int i);

    static {
        System.loadLibrary("light_jni");
    }
}
