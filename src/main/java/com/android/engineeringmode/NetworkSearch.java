package com.android.engineeringmode;

import android.app.Activity;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Chronometer;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

public class NetworkSearch extends Activity {
    private Chronometer chronometer;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    if (msg.obj.exception != null) {
                        Log.e("NetworkSearch", "TDD freq lock error");
                        return;
                    }
                    Log.e("NetworkSearch", "Set LTE mode in 5000s.");
                    NetworkSearch.this.mHandler.sendEmptyMessageDelayed(700, 5000);
                    return;
                case 500:
                    if (((AsyncResult) msg.obj).exception != null) {
                        Log.e("NetworkSearch", "set W only preferred error");
                        return;
                    }
                    Log.e("NetworkSearch", "set W only preferred done. Set W/G W preferred mode in 0.1s.");
                    NetworkSearch.this.mHandler.sendEmptyMessageDelayed(700, 100);
                    return;
                case 600:
                    if (((AsyncResult) msg.obj).exception != null) {
                        Log.e("NetworkSearch", "set LTE mode error");
                        return;
                    }
                    Log.e("NetworkSearch", "set LTE mode done");
                    NetworkSearch.this.finish();
                    return;
                case 700:
                    String rf = SystemProperties.get("ro.rf_version", "TDD_FDD_All");
                    Message inner_msg = NetworkSearch.this.mHandler.obtainMessage(600);
                    if (rf.equals("TDD_FDD_Eu")) {
                        Log.e("NetworkSearch", rf + ", set NETWORK_MODE_LTE_GSM_WCDMA mode.");
                        NetworkSearch.this.mPhone.setPreferredNetworkType(9, inner_msg);
                        return;
                    } else if (rf.equals("TDD_FDD_Am")) {
                        Log.e("NetworkSearch", rf + ", set NETWORK_MODE_LTE_CDMA_EVDO_GSM_WCDMA mode.");
                        NetworkSearch.this.mPhone.setPreferredNetworkType(10, inner_msg);
                        return;
                    } else {
                        Log.e("NetworkSearch", rf + ", set NETWORK_MODE_TD_SCDMA_LTE_CDMA_EVDO_GSM_WCDMA mode.");
                        NetworkSearch.this.mPhone.setPreferredNetworkType(22, inner_msg);
                        return;
                    }
                case 800:
                    if (((AsyncResult) msg.obj).exception != null) {
                        Log.e("NetworkSearch", "set G only preferred error");
                        return;
                    }
                    Log.e("NetworkSearch", "set G only preferred done. delay 1000s.");
                    NetworkSearch.this.mHandler.sendEmptyMessageDelayed(700, 500);
                    return;
                case 900:
                    Log.e("NetworkSearch", "send freq lock msg to modem");
                    NetworkSearch.this.mPhone.setBandMode(47, NetworkSearch.this.mHandler.obtainMessage(100));
                    return;
                case 1000:
                    Log.e("NetworkSearch", "EVENT_SET_G_ONLY_PREF_MODE");
                    NetworkSearch.this.mPhone.setPreferredNetworkType(1, NetworkSearch.this.mHandler.obtainMessage(800));
                    return;
                default:
                    return;
            }
        }
    };
    Phone mPhone;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903171);
        this.chronometer = (Chronometer) findViewById(2131493468);
        this.chronometer.start();
        Log.e("NetworkSearch", "Start time");
        if (TelephonyManager.getDefault().isMultiSimEnabled()) {
            TelephonyManager tm = CallManager.getPhoneInterface(this);
            int status0 = tm.getSimState(0);
            int status1 = tm.getSimState(1);
            if (status0 > 1 || status1 <= 1) {
                this.mPhone = PhoneFactory.getPhone(0);
            } else {
                this.mPhone = PhoneFactory.getPhone(1);
            }
        } else {
            this.mPhone = PhoneFactory.getDefaultPhone();
        }
        Log.e("NetworkSearch", "lock  fdd lte frequenc22222.");
        this.mHandler.sendEmptyMessageDelayed(1000, 1000);
    }

    protected void onStop() {
        super.onStop();
        if (this.chronometer != null) {
            this.chronometer.stop();
            this.chronometer = null;
        }
        finish();
        Log.e("NetworkSearch", "onStop");
    }

    protected void onPause() {
        super.onPause();
        if (this.chronometer != null) {
            this.chronometer.stop();
            this.chronometer = null;
        }
        finish();
        Log.e("NetworkSearch", "onPause");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.e("NetworkSearch", "onDestroy");
    }
}
