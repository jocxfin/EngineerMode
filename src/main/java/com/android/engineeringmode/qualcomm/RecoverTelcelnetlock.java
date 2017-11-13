package com.android.engineeringmode.qualcomm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;

import java.lang.reflect.Field;

public class RecoverTelcelnetlock extends Activity {
    private AlertDialog mAlertDlg;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1937:
                    Log.d("RecoverTelcelnetlock", "reboot...");
                    RecoverTelcelnetlock.this.reboot();
                    RecoverTelcelnetlock.this.doFinish();
                    return;
                default:
                    return;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("RecoverTelcelnetlock", "onCreat!");
        if (!SystemProperties.get("ro.oppo.build.exp", "US").equalsIgnoreCase("MX")) {
            Log.e("RecoverTelcelnetlock", "Not MX build, Just exit!");
            doFinish();
        } else if (checkTelcelNetlock()) {
            Toast.makeText(this, "Device is alread locked!", 0).show();
            doFinish();
        } else {
            showTelcelNetLockRecoverDialog(this);
        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }

    public boolean checkTelcelNetlock() {
        return Telcelnetlock.check() != 0;
    }

    private void Recover() {
        if (Telcelnetlock.recover()) {
            Log.d("RecoverTelcelnetlock", "recover AP telcelnetlock succeeded, now recover modem telcelnetlock!");
            changeModemNetlock(1);
            return;
        }
        Log.e("RecoverTelcelnetlock", "recover AP telcelnetlock failed!");
    }

    private void changeModemNetlock(int request) {
        Intent mIntent = new Intent(this, ModemTelcelnetlock.class);
        mIntent.putExtra("request", request);
        startActivityForResult(mIntent, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                switch (resultCode) {
                    case 0:
                        dismissAlertDialog();
                        Toast.makeText(this, 2131297348, 0).show();
                        this.mHandler.sendEmptyMessageDelayed(1937, 1000);
                        return;
                    case Light.MAIN_KEY_LIGHT /*1*/:
                        Toast.makeText(this, 2131297349, 0).show();
                        return;
                    default:
                        Log.e("RecoverTelcelnetlock", "Invalid resultCode!");
                        return;
                }
            default:
                Log.e("RecoverTelcelnetlock", "Invalid requestcode: " + resultCode);
                return;
        }
    }

    private void doFinish() {
        Log.e("RecoverTelcelnetlock", "Here in finish!");
        finish();
    }

    private void reboot() {
        new Thread(new Runnable() {
            public void run() {
                Log.i("RecoverTelcelnetlock", "reboot device...");
                ((PowerManager) RecoverTelcelnetlock.this.getSystemService("power")).reboot(null);
            }
        }).start();
    }

    private void showTelcelNetLockRecoverDialog(Context context) {
        this.mAlertDlg = new Builder(context).setTitle(2131297342).setMessage(2131297344).setPositiveButton(2131297353, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                    field.setAccessible(true);
                    field.set(dialog, Boolean.valueOf(false));
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                } catch (NoSuchFieldException e3) {
                    e3.printStackTrace();
                } catch (IllegalAccessException e4) {
                    e4.printStackTrace();
                }
                RecoverTelcelnetlock.this.Recover();
            }
        }).setNegativeButton(2131297354, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                RecoverTelcelnetlock.this.dismissAlertDialog();
                RecoverTelcelnetlock.this.doFinish();
            }
        }).create();
        this.mAlertDlg.setCancelable(false);
        this.mAlertDlg.show();
    }

    private void dismissAlertDialog() {
        try {
            if (this.mAlertDlg != null && this.mAlertDlg.isShowing() && !isFinishing()) {
                this.mAlertDlg.dismiss();
            }
        } catch (IllegalArgumentException e) {
            Log.w("RecoverTelcelnetlock", "Trying to dismiss a dialog not connected to the current UI");
        }
    }
}
