package com.android.engineeringmode.autoaging;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.oem.util.Feature;

public class AutoAgingMainListActivity extends PreferenceActivity {
    private final String TAG = "AutoAgingMainListActivity";
    private AlertDialog dialog = null;
    private Handler mHander = new Handler() {
        public void handleMessage(Message msg) {
            ((PowerManager) AutoAgingMainListActivity.this.getSystemService("power")).reboot("aging");
        }
    };

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(2130968583);
        for (RunningServiceInfo info : ((ActivityManager) getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            Log.i("AutoAgingMainListActivity", "servisepackagename==" + info.service.getClassName());
            if (info.service.getClassName().equals("com.oppo.qemonitor.MonitorServices")) {
                Log.i("AutoAgingMainListActivity", "it means this is in aging test don't reboot, you can finish this activity");
                finish();
                return;
            }
        }
        if (!(Feature.isNoAgingSupported(this) || SystemProperties.get("ro.bootmode", "").equals("ftm_aging"))) {
            Builder builder = new Builder(this);
            builder.setMessage(2131296940);
            builder.setTitle(2131296939);
            builder.setPositiveButton(17039370, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("AutoAgingMainListActivity", "this is in aging test , reboot aging");
                    SystemProperties.set("persist.oem.dump", "1");
                    ((PowerManager) AutoAgingMainListActivity.this.getSystemService("power")).reboot("aging");
                }
            });
            builder.setNegativeButton(17039360, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AutoAgingMainListActivity.this.finish();
                }
            });
            this.dialog = builder.create();
            this.dialog.getWindow().setType(2003);
            this.dialog.setCancelable(false);
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.show();
        }
    }
}
