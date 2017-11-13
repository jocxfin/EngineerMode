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
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;

import java.lang.reflect.Field;

public class ClearTelcelnetlock extends Activity {
    private static String mIMEI;
    private AlertDialog mAlertDlg;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1937:
                    Log.d("ClearTelcelnetlock", "reboot...");
                    ClearTelcelnetlock.this.reboot();
                    ClearTelcelnetlock.this.doFinish();
                    return;
                default:
                    return;
            }
        }
    };
    private EditText mPwdField;
    private View mView;
    TelephonyManager tm = null;
    private int unlocktimes = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ClearTelcelnetlock", "onCreat!");
        if (SystemProperties.get("ro.oppo.build.exp", "US").equalsIgnoreCase("MX")) {
            this.tm = (TelephonyManager) getSystemService("phone");
            mIMEI = this.tm.getDeviceId();
            this.unlocktimes = Telcelnetlock.getUnlockTimes();
            if (this.unlocktimes < 0) {
                Log.e("ClearTelcelnetlock", "get unlocktimes failed!");
                doFinish();
                return;
            } else if (this.unlocktimes >= 5) {
                Toast.makeText(this, 2131297350, 0).show();
                SystemProperties.set("PROPERTY_LOCKFOREVER", "true");
                SendLockForeverBroadcasttoUI();
                Log.e("ClearTelcelnetlock", "get unlocktimes > 5, send lockforever broadcase to ui to lock device immediately!");
                doFinish();
                return;
            } else {
                if (checkTelcelNetlock()) {
                    showTelcelNetLockClearDialog(this);
                } else {
                    showTelcelNetLockRecoverDialog(this);
                }
                return;
            }
        }
        Log.e("ClearTelcelnetlock", "Not MX build, Just exit!");
        doFinish();
    }

    protected void onResume() {
        super.onResume();
    }

    public boolean checkTelcelNetlock() {
        return Telcelnetlock.check() != 0;
    }

    protected void onStop() {
        super.onStop();
    }

    private void Clear(String password) {
        if (TextUtils.isEmpty(password)) {
            Log.e("ClearTelcelnetlock", "Clear password is empty!");
            return;
        }
        this.unlocktimes = Telcelnetlock.getUnlockTimes();
        if (this.unlocktimes < 0) {
            Log.e("ClearTelcelnetlock", "get unlocktimes failed, finish!");
            doFinish();
        } else if (this.unlocktimes >= 5) {
            Log.e("ClearTelcelnetlock", "unlock times has met: 5, send REQUEST_TELCELNETLOCK_FOREVER!");
            changeModemNetlock(2);
        } else {
            Log.d("ClearTelcelnetlock", "IMEI:" + mIMEI + ", Pwd:" + password);
            boolean ret = Telcelnetlock.match(mIMEI, password);
            Log.d("ClearTelcelnetlock", "password:" + (ret ? "matched !" : "mismatched !"));
            if (ret) {
                if (clearTelcelNetlock()) {
                    Log.d("ClearTelcelnetlock", "clear AP telcelnetlock succeeded, now clear modem telcelnetlock!");
                    changeModemNetlock(0);
                }
            } else if (Telcelnetlock.addUnlockTimes()) {
                int leftUnlocktimes = (5 - this.unlocktimes) - 1;
                if (leftUnlocktimes == 0) {
                    Log.e("ClearTelcelnetlock", "unlock times has met: 5, send REQUEST_TELCELNETLOCK_FOREVER!");
                    changeModemNetlock(2);
                } else {
                    Toast.makeText(this, String.format(getResources().getString(1 == leftUnlocktimes ? 2131297352 : 2131297351), new Object[]{Integer.valueOf(leftUnlocktimes)}), 0).show();
                }
            } else {
                Log.e("ClearTelcelnetlock", "addUnlockTimes failed!");
            }
        }
    }

    private void Recover() {
        if (recoverTelcelNetlock()) {
            Log.d("ClearTelcelnetlock", "recover AP telcelnetlock succeeded, now recover modem telcelnetlock!");
            changeModemNetlock(1);
        }
    }

    private void SendLockForeverBroadcasttoUI() {
        Log.d("ClearTelcelnetlock", "Send Lockforever broadcast!");
        sendBroadcast(new Intent("com.oppo.engineermode.TelcelLockForever"));
    }

    private void changeModemNetlock(int request) {
        Intent mIntent = new Intent(this, ModemTelcelnetlock.class);
        mIntent.putExtra("request", request);
        startActivityForResult(mIntent, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ClearTelcelnetlock", "onActivityResult requestCode:" + requestCode + " resultCode:" + resultCode);
        switch (requestCode) {
            case 0:
                switch (resultCode) {
                    case 0:
                        dismissAlertDialog();
                        Toast.makeText(this, 2131297346, 0).show();
                        this.mHandler.sendEmptyMessageDelayed(1937, 1000);
                        return;
                    case Light.MAIN_KEY_LIGHT /*1*/:
                        recoverTelcelNetlock();
                        Toast.makeText(this, 2131297347, 0).show();
                        return;
                    default:
                        Log.e("ClearTelcelnetlock", "Invalid resultCode: " + resultCode);
                        return;
                }
            case Light.MAIN_KEY_LIGHT /*1*/:
                switch (resultCode) {
                    case 0:
                        dismissAlertDialog();
                        Toast.makeText(this, 2131297348, 0).show();
                        this.mHandler.sendEmptyMessageDelayed(1937, 1000);
                        return;
                    case Light.MAIN_KEY_LIGHT /*1*/:
                        clearTelcelNetlock();
                        Toast.makeText(this, 2131297349, 0).show();
                        return;
                    default:
                        Log.e("ClearTelcelnetlock", "Invalid resultCode: " + resultCode);
                        return;
                }
            case Light.CHARGE_RED_LIGHT /*2*/:
                switch (resultCode) {
                    case 0:
                        dismissAlertDialog();
                        Toast.makeText(this, 2131297350, 0).show();
                        this.mHandler.sendEmptyMessageDelayed(1937, 1000);
                        return;
                    case Light.MAIN_KEY_LIGHT /*1*/:
                        Log.e("ClearTelcelnetlock", "Forever TelcelNetlock failed, try again!");
                        changeModemNetlock(2);
                        return;
                    default:
                        Log.e("ClearTelcelnetlock", "Invalid resultCode: " + resultCode);
                        return;
                }
            default:
                Log.e("ClearTelcelnetlock", "Invalid resultCode: " + resultCode);
                return;
        }
    }

    private boolean clearTelcelNetlock() {
        return Telcelnetlock.clear();
    }

    private boolean recoverTelcelNetlock() {
        return Telcelnetlock.recover();
    }

    private void doFinish() {
        Log.e("ClearTelcelnetlock", "Here in finish!");
        finish();
    }

    private void reboot() {
        new Thread(new Runnable() {
            public void run() {
                Log.i("ClearTelcelnetlock", "reboot device...");
                ((PowerManager) ClearTelcelnetlock.this.getSystemService("power")).reboot(null);
            }
        }).start();
    }

    private void showTelcelNetLockClearDialog(Context context) {
        this.mView = createTelcelNetLockClearView();
        this.mAlertDlg = new Builder(context).setTitle(2131297343).setView(this.mView, 0, 1, 1, 1).setCancelable(false).setPositiveButton(2131297353, new OnClickListener() {
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
                String pwd = ClearTelcelnetlock.this.mPwdField.getText().toString();
                ClearTelcelnetlock.this.mPwdField.setText(null);
                ClearTelcelnetlock.this.Clear(pwd);
            }
        }).setNegativeButton(2131297354, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ClearTelcelnetlock.this.dismissAlertDialog();
                ClearTelcelnetlock.this.doFinish();
            }
        }).create();
        this.mAlertDlg.setCancelable(false);
        this.mAlertDlg.show();
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
                ClearTelcelnetlock.this.Recover();
            }
        }).setNegativeButton(2131297354, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ClearTelcelnetlock.this.dismissAlertDialog();
                ClearTelcelnetlock.this.doFinish();
            }
        }).create();
        this.mAlertDlg.setCancelable(false);
        this.mAlertDlg.show();
    }

    private View createTelcelNetLockClearView() {
        View dialogView = getLayoutInflater().inflate(2130903208, null);
        this.mPwdField = (EditText) dialogView.findViewById(2131493563);
        this.mPwdField.requestFocus();
        this.mPwdField.setText(null);
        this.mPwdField.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 6) {
                    String key = ClearTelcelnetlock.this.mPwdField.getText().toString();
                    ClearTelcelnetlock.this.mPwdField.setText(null);
                    ClearTelcelnetlock.this.Clear(key);
                }
                return true;
            }
        });
        return dialogView;
    }

    private void dismissAlertDialog() {
        try {
            if (this.mAlertDlg != null && this.mAlertDlg.isShowing() && !isFinishing()) {
                this.mAlertDlg.dismiss();
            }
        } catch (IllegalArgumentException e) {
            Log.w("ClearTelcelnetlock", "Trying to dismiss a dialog not connected to the current UI");
        }
    }
}
