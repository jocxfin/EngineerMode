package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HallTest extends Activity {
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int[] adc = HallTest.this.mRotationUtils.hall_test();
                if (adc[3] == 1) {
                    HallTest.this.mLayout.setBackgroundColor(-16711936);
                    HallTest.this.mlow_threshold.setText("" + adc[0]);
                    HallTest.this.mhigh_threshold.setText("" + adc[1]);
                    HallTest.this.madc.setText("" + adc[2]);
                    HallTest.this.mstatus.setText("BACK");
                } else if (adc[3] == 0) {
                    HallTest.this.mLayout.setBackgroundColor(-16776961);
                    HallTest.this.mlow_threshold.setText("" + adc[0]);
                    HallTest.this.mhigh_threshold.setText("" + adc[1]);
                    HallTest.this.madc.setText("" + adc[2]);
                    HallTest.this.mstatus.setText("FRONT");
                }
                sendEmptyMessageDelayed(1, 200);
            }
        }
    };
    private LinearLayout mLayout;
    private RotationUtils mRotationUtils;
    private TextView madc;
    private TextView mhigh_threshold;
    private TextView mlow_threshold;
    private TextView mstatus;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903116);
        this.mlow_threshold = (TextView) findViewById(2131493186);
        this.mhigh_threshold = (TextView) findViewById(2131493187);
        this.madc = (TextView) findViewById(2131493188);
        this.mstatus = (TextView) findViewById(2131493062);
        this.mLayout = (LinearLayout) findViewById(2131493185);
        this.mRotationUtils = new RotationUtils();
    }

    protected void onResume() {
        super.onResume();
        this.mRotationUtils.open_hall();
        this.mHandler.sendEmptyMessage(1);
    }

    protected void onPause() {
        super.onPause();
        this.mHandler.removeMessages(1);
        this.mRotationUtils.close_hall();
    }
}
