package com.android.engineeringmode.manualtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.engineeringmode.KeepScreenOnActivity;
import com.android.engineeringmode.functions.Light;

public class ProximityFunTest extends KeepScreenOnActivity {
    private volatile boolean exit;
    private int grayColor = -1;
    private int greenColor = -862916976;
    private LightSensorUtils lsUtils;
    private TextView mDistance;
    private int mFileD;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int[] adc = ProximityFunTest.this.lsUtils.getADC(ProximityFunTest.this.mFileD);
                ProximityFunTest.this.mProx.setText("" + adc[1]);
                if (adc[2] == 2) {
                    ProximityFunTest.this.mLayout.setBackgroundColor(ProximityFunTest.this.greenColor);
                    ProximityFunTest.this.mDistance.setText("near");
                } else if (adc[2] == 15) {
                    ProximityFunTest.this.mLayout.setBackgroundColor(ProximityFunTest.this.grayColor);
                    ProximityFunTest.this.mDistance.setText("far");
                } else {
                    ProximityFunTest.this.mDistance.setText("unknown");
                }
                if (!ProximityFunTest.this.exit) {
                    sendEmptyMessageDelayed(1, 100);
                }
            }
        }
    };
    private LinearLayout mLayout;
    private TextView mProx;
    private int proxHigh;
    private int proxLow;
    private int proxMean;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903188);
        this.mProx = (TextView) findViewById(2131493493);
        this.mDistance = (TextView) findViewById(2131493494);
        this.mLayout = (LinearLayout) findViewById(2131493185);
        this.lsUtils = new LightSensorUtils(this);
        byte[] proxData = new byte[32];
        this.proxMean = (((proxData[0] & -16777216) | (proxData[1] & 16711680)) | (proxData[2] & 65280)) | (proxData[3] & Light.MAIN_KEY_MAX);
        this.proxHigh = (((proxData[4] & -16777216) | (proxData[5] & 16711680)) | (proxData[6] & 65280)) | (proxData[7] & Light.MAIN_KEY_MAX);
        this.proxLow = (((proxData[8] & -16777216) | (proxData[9] & 16711680)) | (proxData[10] & 65280)) | (proxData[11] & Light.MAIN_KEY_MAX);
        this.grayColor = getResources().getColor(2131165187);
    }

    protected void onResume() {
        super.onResume();
        this.mFileD = this.lsUtils.openLightSensor();
        this.exit = false;
        this.mHandler.sendEmptyMessage(1);
    }

    protected void onPause() {
        super.onPause();
        this.lsUtils.closeLightSensor();
        this.exit = true;
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
