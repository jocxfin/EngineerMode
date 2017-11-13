package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class StepCounterTest extends Activity implements SensorEventListener {
    private int initNum = 0;
    private SensorManager mSensorManager;
    private TextView passTextView;
    private Sensor sensor;
    private TextView stepCounterText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903205);
        this.stepCounterText = (TextView) findViewById(2131493543);
        this.passTextView = (TextView) findViewById(2131493544);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.sensor = this.mSensorManager.getDefaultSensor(19);
        if (this.sensor != null) {
            this.mSensorManager.registerListener(this, this.sensor, 0);
        } else {
            this.stepCounterText.setText(2131297525);
        }
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 19) {
            this.stepCounterText.setText(String.format("%f", new Object[]{Float.valueOf(event.values[0])}));
            this.initNum++;
            if (this.initNum == 5) {
                this.passTextView.setText("PASS");
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    protected void onDestroy() {
        if (this.sensor != null) {
            this.mSensorManager.unregisterListener(this);
        }
        super.onDestroy();
    }
}
