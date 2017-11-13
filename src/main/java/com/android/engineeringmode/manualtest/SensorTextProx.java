package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class SensorTextProx extends Activity {
    private int grayColor = -1;
    private int greenColor = -16711936;
    private TextView mDistance;
    private LinearLayout mLayout;
    private TextView mMean;
    private TextView mProx;
    private SensorEventListener proxListener;
    private SensorManager sm = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903188);
        this.sm = (SensorManager) getSystemService("sensor");
        this.mProx = (TextView) findViewById(2131493493);
        this.mDistance = (TextView) findViewById(2131493494);
        this.mMean = (TextView) findViewById(2131493495);
        this.mLayout = (LinearLayout) findViewById(2131493185);
        this.grayColor = getResources().getColor(2131165187);
        Calendar now = Calendar.getInstance();
        String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--SensorTextProx--" + "has entered it";
        this.proxListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                SensorTextProx.this.mProx.setText("" + event.values[0]);
                SensorTextProx.this.mMean.setText("" + event.values[1]);
                if (event.values[0] > 0.0f) {
                    SensorTextProx.this.mDistance.setText("far");
                    SensorTextProx.this.mLayout.setBackgroundColor(SensorTextProx.this.grayColor);
                    return;
                }
                SensorTextProx.this.mDistance.setText("near");
                SensorTextProx.this.mLayout.setBackgroundColor(SensorTextProx.this.greenColor);
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    protected void onResume() {
        super.onResume();
        this.sm.registerListener(this.proxListener, this.sm.getDefaultSensor(8), 0);
    }

    protected void onPause() {
        super.onPause();
        this.sm.unregisterListener(this.proxListener);
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
