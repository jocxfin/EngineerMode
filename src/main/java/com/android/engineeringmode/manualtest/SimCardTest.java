package com.android.engineeringmode.manualtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.engineeringmode.manualtest.modeltest.ModelTest3ItemActivity;

public class SimCardTest extends ModelTest3ItemActivity {
    private IntentFilter intentFilter;
    private boolean isInModelTest = false;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int i = 2131296279;
            int i2 = -16711936;
            boolean sim1_state_result = false;
            boolean sim2_state_result = true;
            if (SimCardTest.this.sim1_state_tv != null) {
                int i3;
                int sim1state = SimCardTest.this.tm.getSimState(0);
                TextView - get1 = SimCardTest.this.sim1_state_tv;
                if (sim1state <= 1 || sim1state >= 6) {
                    i3 = 2131296280;
                } else {
                    i3 = 2131296279;
                }
                -get1.setText(i3);
                -get1 = SimCardTest.this.sim1_state_tv;
                if (sim1state <= 1 || sim1state >= 6) {
                    i3 = -65536;
                } else {
                    i3 = -16711936;
                }
                -get1.setTextColor(i3);
                if (sim1state > 1 && sim1state < 6) {
                    sim1_state_result = true;
                }
            }
            if (SimCardTest.this.sim2_state_tv != null) {
                sim2_state_result = false;
                int sim2state = SimCardTest.this.tm.getSimState(1);
                TextView - get2 = SimCardTest.this.sim2_state_tv;
                if (sim2state <= 1 || sim2state >= 6) {
                    i = 2131296280;
                }
                -get2.setText(i);
                -get2 = SimCardTest.this.sim2_state_tv;
                if (sim2state <= 1 || sim2state >= 6) {
                    i2 = -65536;
                }
                -get2.setTextColor(i2);
                if (sim2state > 1 && sim2state < 6) {
                    sim2_state_result = true;
                }
            }
            if (SimCardTest.this.isInModelTest && sim1_state_result && sim2_state_result) {
                SimCardTest.this.onTestPassed();
            }
        }
    };
    private TextView sim1_state_tv = null;
    private LinearLayout sim2_state_layout = null;
    private TextView sim2_state_tv = null;
    private TelephonyManager tm;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903200);
        this.tm = (TelephonyManager) getSystemService("phone");
        boolean isMultiSimSupported = TelephonyManager.getDefault().isMultiSimEnabled();
        this.sim1_state_tv = (TextView) findViewById(2131493537);
        if (isMultiSimSupported) {
            this.sim2_state_layout = (LinearLayout) findViewById(2131493538);
            this.sim2_state_layout.setVisibility(0);
            this.sim2_state_tv = (TextView) findViewById(2131493539);
        }
        this.intentFilter = new IntentFilter("android.intent.action.SIM_STATE_CHANGED");
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
    }

    protected void onResume() {
        super.onResume();
        registerReceiver(this.mBroadcastReceiver, this.intentFilter);
    }

    protected void onStop() {
        super.onStop();
        unregisterReceiver(this.mBroadcastReceiver);
    }
}
