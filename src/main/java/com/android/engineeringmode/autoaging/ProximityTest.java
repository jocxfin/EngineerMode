package com.android.engineeringmode.autoaging;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.manualtest.LightSensorUtils;

public class ProximityTest extends BaseTest {
    private volatile boolean exit;
    private int grayColor = -1;
    private int greenColor = -862916976;
    private LightSensorUtils lsUtils;
    private TextView mDistance;
    private int mFileD;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int[] adc = ProximityTest.this.lsUtils.getADC(ProximityTest.this.mFileD);
                ProximityTest.this.mProx.setText("" + adc[1]);
                if (adc[2] == 2) {
                    ProximityTest.this.mLayout.setBackgroundColor(ProximityTest.this.greenColor);
                    ProximityTest.this.mDistance.setText("near");
                } else if (adc[2] == 15) {
                    ProximityTest.this.mLayout.setBackgroundColor(ProximityTest.this.grayColor);
                    ProximityTest.this.mDistance.setText("far");
                } else {
                    ProximityTest.this.mDistance.setText("unknown");
                }
                if (!ProximityTest.this.exit) {
                    sendEmptyMessageDelayed(1, 500);
                }
            }
        }
    };
    private LinearLayout mLayout;
    private TextView mProx;
    private int proxHigh;
    private int proxLow;
    private int proxMean;

    protected void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setTitle(2131296982);
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

    protected void endTest() {
        this.lsUtils.closeLightSensor();
        this.mHandler.removeMessages(1);
        this.exit = true;
    }

    protected void runTest() {
        this.mFileD = this.lsUtils.openLightSensor();
        this.exit = false;
        this.mHandler.sendEmptyMessage(1);
    }
}
