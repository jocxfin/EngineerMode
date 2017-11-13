package com.android.engineeringmode.util;

import android.content.Context;
import android.os.PowerManager;

public final class WakeLock {
    private static android.os.PowerManager.WakeLock sCpuWakeLock;
    private static Object sLock = new Object();

    public static void acquire(Context context) {
        synchronized (sLock) {
            if (sCpuWakeLock != null) {
                return;
            }
            sCpuWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(805306378, "Eng");
            sCpuWakeLock.acquire();
        }
    }

    public static void release() {
        synchronized (sLock) {
            if (sCpuWakeLock != null) {
                sCpuWakeLock.release();
                sCpuWakeLock = null;
            }
        }
    }
}
