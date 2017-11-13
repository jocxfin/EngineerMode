package com.android.engineeringmode.qualcomm;

import android.app.Activity;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.ServiceState;
import android.util.Log;

import com.android.engineeringmode.autoaging.AirplaneModeEnabler;
import com.android.engineeringmode.autoaging.AirplaneModeEnabler.onServiceStateChangedListener;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

public class InitTelcelNV extends Activity implements onServiceStateChangedListener {
    private AirplaneModeEnabler mAirplaneModeEnabler = null;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    AsyncResult ar = msg.obj;
                    if (ar.exception == null) {
                        Log.d("InitTelcelNV", "Init Telcel NV request succeeded, now set Radio Power off!");
                        InitTelcelNV.this.mAirplaneModeEnabler = new AirplaneModeEnabler(InitTelcelNV.this);
                        InitTelcelNV.this.mAirplaneModeEnabler.setServiceStateChangedListener(InitTelcelNV.this);
                        InitTelcelNV.this.mAirplaneModeEnabler.start();
                        InitTelcelNV.this.mAirplaneModeEnabler.setAirplaneModeOn(true);
                        return;
                    }
                    Log.e("InitTelcelNV", "Init Telcel NV request failed! Exception:" + ar.exception);
                    InitTelcelNV.this.doFinish();
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
        initNV();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }

    public void onServiceStateChanged(ServiceState serviceState) {
        Log.d("InitTelcelNV", "Service State = " + serviceState.getState());
        if (3 == serviceState.getState()) {
            Log.d("InitTelcelNV", "Now set radio power on, set lockinit to false!");
            this.mAirplaneModeEnabler.setAirplaneModeOn(false);
            SystemProperties.set("persist.sys.oppo.lockinit", "false");
            this.mAirplaneModeEnabler.pause();
            doFinish();
        }
    }

    private void initNV() {
        this.mPhone.setFactoryModeModemGPIO(0, 3, this.mHandler.obtainMessage(2000));
        Log.d("InitTelcelNV", "Init Telcel NV request has been sent!");
    }

    private void doFinish() {
        Log.e("InitTelcelNV", "Here in finish!");
        finish();
    }
}
