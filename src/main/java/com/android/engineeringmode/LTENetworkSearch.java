package com.android.engineeringmode;

import android.app.Activity;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

public class LTENetworkSearch extends Activity {
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    if (msg.obj.exception != null) {
                        Log.e("NetworkSearch", "TDD freq lock error");
                        return;
                    }
                    Log.e("NetworkSearch", "Set LTE mode in 1500s.");
                    LTENetworkSearch.this.mHandler.sendEmptyMessageDelayed(700, 1500);
                    return;
                case 500:
                    if (((AsyncResult) msg.obj).exception != null) {
                        Log.e("NetworkSearch", "set W only preferred error");
                        return;
                    }
                    Log.e("NetworkSearch", "set W only preferred done. Set W/G W preferred mode in 0.1s.");
                    LTENetworkSearch.this.mHandler.sendEmptyMessageDelayed(700, 100);
                    return;
                case 600:
                    if (((AsyncResult) msg.obj).exception != null) {
                        Log.e("NetworkSearch", "set LTE mode error");
                        return;
                    }
                    Log.e("NetworkSearch", "set LTE mode done");
                    LTENetworkSearch.this.finish();
                    return;
                case 700:
                    Log.e("NetworkSearch", "set LTE mode.");
                    LTENetworkSearch.this.mPhone.setPreferredNetworkType(11, LTENetworkSearch.this.mHandler.obtainMessage(600));
                    return;
                case 800:
                    if (((AsyncResult) msg.obj).exception != null) {
                        Log.e("NetworkSearch", "set G only preferred error");
                        return;
                    }
                    Log.e("NetworkSearch", "set G only preferred done. delay 500s.");
                    LTENetworkSearch.this.mHandler.sendEmptyMessageDelayed(700, 500);
                    return;
                case 900:
                    Log.e("NetworkSearch", "send freq lock msg to modem");
                    LTENetworkSearch.this.mPhone.setBandMode(47, LTENetworkSearch.this.mHandler.obtainMessage(100));
                    return;
                default:
                    return;
            }
        }
    };
    Phone mPhone;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPhone = PhoneFactory.getDefaultPhone();
        Log.e("NetworkSearch", "lock  fdd lte frequence.");
        this.mPhone.setPreferredNetworkType(1, this.mHandler.obtainMessage(800));
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.e("NetworkSearch", "onDestroy");
    }
}
