package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HallSwitch extends Activity {
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int adc = HallSwitch.this.mRotationUtils.get_pswitch_value();
                Log.i("HallSwitch", "adc id :" + adc);
                if (adc == 1) {
                    if (HallSwitch.this.mLayout != null) {
                        HallSwitch.this.mLayout.setBackgroundColor(-16776961);
                        HallSwitch.this.mstatus.setText("BACK");
                    }
                } else if (adc == 0 && HallSwitch.this.mLayout != null) {
                    HallSwitch.this.mLayout.setBackgroundColor(-16711936);
                    HallSwitch.this.mstatus.setText("FRONT");
                }
                sendEmptyMessageDelayed(1, 200);
            }
        }
    };
    private LinearLayout mLayout;
    private RotationUtils mRotationUtils;
    private TextView mstatus;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903115);
        this.mstatus = (TextView) findViewById(2131493062);
        this.mLayout = (LinearLayout) findViewById(2131493185);
        this.mRotationUtils = new RotationUtils();
    }

    protected void onResume() {
        super.onResume();
        if (this.mHandler != null) {
            this.mHandler.sendEmptyMessage(1);
        }
    }

    protected void onPause() {
        super.onPause();
        if (this.mHandler != null) {
            this.mHandler.removeMessages(1);
        }
    }

    protected void onStop() {
        super.onPause();
        if (this.mHandler != null) {
            this.mHandler = null;
        }
        finish();
    }
}
