package com.android.engineeringmode.manualtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.widget.TextView;

import com.android.engineeringmode.KeepScreenOnActivity;

public class ProximitySensor extends KeepScreenOnActivity {
    private String cleardata;
    private volatile boolean exit;
    private String irdata;
    private LightSensorUtils lsUtils;
    private String lux;
    private TextView mCleardata;
    private TextView mDistance;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int[] adc = ProximitySensor.this.lsUtils.getADC(0);
                Log.e("ProximitySensor", "len:" + adc.length);
                ProximitySensor.this.mCleardata.setText(ProximitySensor.this.cleardata + ":" + adc[0]);
                ProximitySensor.this.mIrdata.setText(ProximitySensor.this.irdata + ":" + adc[1]);
                ProximitySensor.this.mProx.setText(ProximitySensor.this.prox + ":" + adc[2]);
                ProximitySensor.this.mLux.setText(ProximitySensor.this.lux + ":" + adc[3]);
                if (adc[4] == 0) {
                    ProximitySensor.this.mDistance.setText("near");
                } else if (adc[4] == 1) {
                    ProximitySensor.this.mDistance.setText("far");
                } else {
                    ProximitySensor.this.mDistance.setText("unknown");
                }
                if (!ProximitySensor.this.exit) {
                    sendEmptyMessageDelayed(1, 1000);
                }
            }
        }
    };
    private TextView mIrdata;
    private TextView mLux;
    private PowerManager mPowerManager;
    private TextView mProx;
    private String prox;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903189);
        this.mCleardata = (TextView) findViewById(2131493497);
        this.mIrdata = (TextView) findViewById(2131493498);
        this.mProx = (TextView) findViewById(2131493499);
        this.mLux = (TextView) findViewById(2131493500);
        this.mDistance = (TextView) findViewById(2131493501);
        this.lsUtils = new LightSensorUtils(this);
        this.cleardata = getString(2131296998);
        this.irdata = getString(2131296999);
        this.prox = getString(2131297000);
        this.lux = getString(2131297001);
        this.mPowerManager = (PowerManager) getSystemService("power");
    }

    protected void onResume() {
        super.onResume();
        this.lsUtils.openLightSensor();
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
