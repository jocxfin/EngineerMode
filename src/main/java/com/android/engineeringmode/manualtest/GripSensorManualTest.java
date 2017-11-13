package com.android.engineeringmode.manualtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;

public class GripSensorManualTest extends Activity {
    private Sensor mSensor = null;
    @SuppressLint({"NewApi"})
    private SensorEventListener mSensorListener = new SensorEventListener() {
        @SuppressLint({"NewApi"})
        public void onSensorChanged(SensorEvent event) {
            Log.e("GripSensorManualTest", "sensorChanged " + event.sensor.getName() + ", x: " + event.values[0] + ", y: " + event.values[1] + ", z: " + event.values[2]);
            switch ((int) event.values[0]) {
                case 0:
                    GripSensorManualTest.this.mtvShowInfo.setText("LEFT");
                    return;
                case Light.MAIN_KEY_LIGHT /*1*/:
                    GripSensorManualTest.this.mtvShowInfo.setText("RIGHT");
                    return;
                default:
                    GripSensorManualTest.this.mtvShowInfo.setText("NONE");
                    return;
            }
        }

        @SuppressLint({"NewApi"})
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private SensorManager mSensorManager = null;
    private TextView mtvShowInfo = null;

    @SuppressLint({"NewApi"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mSensor = this.mSensorManager.getDefaultSensor(26);
        setContentView(2130903105);
        setTitle(2131297446);
        this.mtvShowInfo = (TextView) findViewById(2131493126);
        this.mtvShowInfo.setText("NONE");
    }

    @SuppressLint({"NewApi"})
    protected void onResume() {
        this.mSensorManager.registerListener(this.mSensorListener, this.mSensor, 0);
        super.onResume();
    }

    @SuppressLint({"NewApi"})
    protected void onStop() {
        this.mSensorManager.unregisterListener(this.mSensorListener);
        super.onStop();
    }
}
