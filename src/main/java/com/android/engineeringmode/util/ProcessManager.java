package com.android.engineeringmode.util;

import android.app.ActivityManagerNative;
import android.os.RemoteException;

public class ProcessManager {
    private ProcessManager() {
    }

    public static void killApplicationProcess(String packageName, int uid) {
        try {
            ActivityManagerNative.getDefault().killApplicationProcess(packageName, uid);
        } catch (RemoteException e) {
        }
    }
}
