package com.android.engineeringmode;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.PendingIntent.OnFinished;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

import com.android.engineeringmode.functions.Light;

public class AllClear extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDialog(1);
    }

    protected void onDestroy() {
        super.onDestroy();
        try {
            dismissDialog(2);
        } catch (Exception e) {
        }
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                return new Builder(this).setTitle("Waring").setMessage("It will restore default settings.Do you want to go on?").setPositiveButton(17039370, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AllClear.this.resetTheFactoryData();
                    }
                }).setNegativeButton(17039360, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AllClear.this.finish();
                    }
                }).create();
            case Light.CHARGE_RED_LIGHT /*2*/:
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("Restore");
                dialog.setMessage("It's restoring deafult settings");
                return dialog;
            default:
                return super.onCreateDialog(id);
        }
    }

    private void resetTheFactoryData() {
        restoreSettings();
        showDialog(2);
    }

    private void restoreSettings() {
        try {
            PendingIntent.getBroadcast(this, 0, new Intent("android.intent.action.RESTORE_DEFAULT_SETTINGS"), 1073741824).send(0, new OnFinished() {
                public void onSendFinished(PendingIntent pendingIntent, Intent intent, int resultCode, String resultData, Bundle resultExtras) {
                    Log.d("AllClear", "send restoreDefaultSettings broadcast sucessfully, now need reboot system!!!!!!!!");
                    new Thread() {
                        public void run() {
                            ((PowerManager) AllClear.this.getSystemService("power")).reboot(null);
                        }
                    }.start();
                }
            }, null);
        } catch (CanceledException e) {
            Log.e("AllClear", "PendingIntent send error, also need to reboot system!!!!!!!!");
            new Thread() {
                public void run() {
                    ((PowerManager) AllClear.this.getSystemService("power")).reboot(null);
                }
            }.start();
        }
    }
}
