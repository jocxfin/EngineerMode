package com.android.engineeringmode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.util.Log;

public class NvFactoryReceiver extends BroadcastReceiver {
    private static String isengineeringmode_file = "/mnt/sdcard/isengineeringmode.dat";

    public void onReceive(Context context, Intent intent) {
        Log.d("NvFactoryReceiver", "songxh onReceive action = " + intent.getAction());
        if (!"true".equals(SystemProperties.get("ro.factorymode", "false"))) {
            Log.d("NvFactoryReceiver", "set factorymode true by EngineeringMode file setting\n");
            if ("false".equals(SystemProperties.get("ro.factorymode"))) {
                SystemProperties.set("sys.usb.config", "diag,serial_tty,rmnet_smd,adb");
            }
            context.sendBroadcast(new Intent("android.intent.action.LOCKSREEN_ENGINEERINGMODE"));
        }
    }
}
