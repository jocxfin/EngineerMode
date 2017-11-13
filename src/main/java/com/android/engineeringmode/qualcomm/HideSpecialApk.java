package com.android.engineeringmode.qualcomm;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.Toast;

public class HideSpecialApk extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FileTransactionUtil.isPersistFileExist()) {
            SystemProperties.set("persist.sys.pms.scanoppo-app", "false");
            Toast.makeText(this, "hide special apk", 0).show();
            reboot();
        }
        finish();
    }

    private void reboot() {
        new Thread(new Runnable() {
            public void run() {
                Log.i("", "reboot device...");
                ((PowerManager) HideSpecialApk.this.getSystemService("power")).reboot(null);
            }
        }).start();
    }
}
