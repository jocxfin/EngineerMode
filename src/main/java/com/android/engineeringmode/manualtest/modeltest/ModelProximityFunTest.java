package com.android.engineeringmode.manualtest.modeltest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.System;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ModelProximityFunTest extends ModelTest3ItemActivity {
    private int grayColor = -1;
    private int greenColor = -16711936;
    private int mActivityState = 0;
    private int mAutoBright = -1;
    private TextView mDistance;
    private LinearLayout mLayout;
    private TextView mMean;
    private TextView mProx;
    private TextView mResultTextView;
    private boolean mfar = false;
    private boolean mhigh = false;
    private boolean mlow = false;
    private boolean mnear = false;
    private SensorEventListener proxListener;
    private SensorManager sm = null;

    private class SensorTestTask extends AsyncTask<Object, Void, Integer> {
        private SensorTestTask() {
        }

        protected Integer doInBackground(Object... params) {
            Log.d("ModelProximityFunTest", "SensorTestTask doInBackground *********** 01");
            int rv = -1;
            if (1 == ModelProximityFunTest.this.mActivityState) {
                rv = 0;
                ModelProximityFunTest.this.registerListener();
            } else if (2 == ModelProximityFunTest.this.mActivityState) {
                ModelProximityFunTest.this.unregisterListener();
            }
            Log.d("ModelProximityFunTest", "SensorTestTask doInBackground ********* 02");
            return Integer.valueOf(rv);
        }

        protected void onPostExecute(Integer testResult) {
            Log.d("ModelProximityFunTest", "SensorTestTask onPostExecute enter **********");
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903188);
        this.sm = (SensorManager) getSystemService("sensor");
        this.mProx = (TextView) findViewById(2131493493);
        this.mDistance = (TextView) findViewById(2131493494);
        this.mMean = (TextView) findViewById(2131493495);
        this.mLayout = (LinearLayout) findViewById(2131493185);
        this.grayColor = getResources().getColor(2131165187);
        this.mResultTextView = (TextView) findViewById(2131492997);
        this.mAutoBright = System.getInt(getContentResolver(), "screen_brightness_mode", 1);
        if (this.mAutoBright == 1) {
            System.putInt(getContentResolver(), "screen_brightness_mode", 0);
            System.putInt(getContentResolver(), "screen_brightness", 127);
        }
        this.proxListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                int mean = (int) event.values[1];
                ModelProximityFunTest.this.mProx.setText("" + event.values[0]);
                ModelProximityFunTest.this.mMean.setText("" + mean);
                if (event.values[0] > 0.0f) {
                    ModelProximityFunTest.this.mfar = true;
                    ModelProximityFunTest.this.mDistance.setText("far");
                } else {
                    ModelProximityFunTest.this.mnear = true;
                    ModelProximityFunTest.this.mDistance.setText("near");
                }
                if (ModelProximityFunTest.this.mfar && ModelProximityFunTest.this.mnear) {
                    ModelProximityFunTest.this.mResultTextView.setText("Pass");
                    ModelProximityFunTest.this.mResultTextView.setTextColor(-16711936);
                    ModelProximityFunTest.this.onTestPassed();
                }
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    void registerListener() {
        this.sm.registerListener(this.proxListener, this.sm.getDefaultSensor(8), 1);
    }

    void unregisterListener() {
        this.sm.unregisterListener(this.proxListener);
    }

    protected void onResume() {
        super.onResume();
        this.mActivityState = 1;
        Log.d("ModelProximityFunTest", "SensorTextProx:onResume********** 01");
        new SensorTestTask().execute(new Object[0]);
        Log.d("ModelProximityFunTest", "SensorTextProx:onResume********** 02");
    }

    protected void onPause() {
        super.onPause();
        this.mActivityState = 2;
        Log.d("ModelProximityFunTest", "SensorTextProx:onPause 01");
        new SensorTestTask().execute(new Object[0]);
        Log.d("ModelProximityFunTest", "SensorTextProx:onPause 02");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d("ModelProximityFunTest", "on stop");
        if (this.mAutoBright == 1) {
            System.putInt(getContentResolver(), "screen_brightness_mode", 1);
        }
    }
}
