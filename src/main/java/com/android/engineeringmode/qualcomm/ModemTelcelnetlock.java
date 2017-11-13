package com.android.engineeringmode.qualcomm;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

public class ModemTelcelnetlock extends Activity {
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            AsyncResult ar;
            Intent intent;
            switch (msg.what) {
                case 1002:
                    ar = msg.obj;
                    if (ar.exception == null) {
                        Log.d("ModemTelcelnetlock", "Clear ModemNetlock request succeeded!");
                        intent = new Intent();
                        intent.putExtra("result", "");
                        ModemTelcelnetlock.this.setResult(0, intent);
                        ModemTelcelnetlock.this.doFinish();
                        return;
                    }
                    Log.e("ModemTelcelnetlock", "Clear ModemNetlock request failed! Exception:" + ar.exception);
                    intent = new Intent();
                    intent.putExtra("result", "Exception:" + ar.exception);
                    ModemTelcelnetlock.this.setResult(1, intent);
                    ModemTelcelnetlock.this.doFinish();
                    return;
                case 1003:
                    ar = (AsyncResult) msg.obj;
                    if (ar.exception == null) {
                        Log.d("ModemTelcelnetlock", "Recover ModemNetlock request succeeded!");
                        intent = new Intent();
                        intent.putExtra("result", "");
                        ModemTelcelnetlock.this.setResult(0, intent);
                        ModemTelcelnetlock.this.doFinish();
                        return;
                    }
                    Log.e("ModemTelcelnetlock", "Recover ModemNetlock request failed! Exception:" + ar.exception);
                    intent = new Intent();
                    intent.putExtra("result", "Exception:" + ar.exception);
                    ModemTelcelnetlock.this.setResult(1, intent);
                    ModemTelcelnetlock.this.doFinish();
                    return;
                case 1004:
                    ar = (AsyncResult) msg.obj;
                    if (ar.exception == null) {
                        Log.d("ModemTelcelnetlock", "Forever ModemNetlock request succeeded!");
                        intent = new Intent();
                        intent.putExtra("result", "");
                        ModemTelcelnetlock.this.setResult(0, intent);
                        ModemTelcelnetlock.this.doFinish();
                        return;
                    }
                    Log.e("ModemTelcelnetlock", "Forever ModemNetlock request failed! Exception:" + ar.exception);
                    intent = new Intent();
                    intent.putExtra("result", "Exception:" + ar.exception);
                    ModemTelcelnetlock.this.setResult(1, intent);
                    ModemTelcelnetlock.this.doFinish();
                    return;
                default:
                    return;
            }
        }
    };
    private Phone mPhone = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.mPhone == null) {
            this.mPhone = PhoneFactory.getDefaultPhone();
        }
        changeModemNetlock();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }

    private void changeModemNetlock() {
        int request = getIntent().getIntExtra("request", -1);
        if (request == 0) {
            clearModemNetlock();
        } else if (1 == request) {
            recoverModemNetlock();
        } else if (2 == request) {
            foreverModemNetlock();
        } else {
            Log.e("ModemTelcelnetlock", "request is invalid!");
        }
    }

    private void clearModemNetlock() {
        this.mPhone.setFactoryModeModemGPIO(1, 0, this.mHandler.obtainMessage(1002));
        Log.d("ModemTelcelnetlock", "Clear ModemNetlock request has been sent!");
    }

    private void recoverModemNetlock() {
        this.mPhone.setFactoryModeModemGPIO(0, 1, this.mHandler.obtainMessage(1003));
        Log.d("ModemTelcelnetlock", "Recover ModemNetlock request has been sent!");
    }

    private void foreverModemNetlock() {
        this.mPhone.setFactoryModeModemGPIO(0, 2, this.mHandler.obtainMessage(1004));
        Log.d("ModemTelcelnetlock", "Forever ModemNetlock request has been sent!");
    }

    private void doFinish() {
        Log.e("ModemTelcelnetlock", "Here in finish!");
        finish();
    }
}
