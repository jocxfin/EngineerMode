package com.android.engineeringmode;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public abstract class LumenEventListener {
    private boolean mEnabled;
    private int mRate;
    private Sensor mSensor;
    private SensorEventListener mSensorEventListener;
    private SensorManager mSensorManager;

    class SensorEventListenerImpl implements SensorEventListener {
        SensorEventListenerImpl() {
        }

        public void onSensorChanged(SensorEvent event) {
            LumenEventListener.this.onLumenChanged((int) event.values[0]);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    public abstract void onLumenChanged(int i);

    public LumenEventListener(Context context) {
        this(context, 3);
    }

    public LumenEventListener(Context context, int rate) {
        this.mEnabled = false;
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        this.mRate = rate;
        this.mSensor = this.mSensorManager.getDefaultSensor(5);
        if (this.mSensor != null) {
            this.mSensorEventListener = new SensorEventListenerImpl();
        }
    }

    public void enable() {
        if (this.mSensor == null) {
            Log.w("LumenEventListener", "Cannot detect sensors. Not enabled");
            return;
        }
        if (!this.mEnabled) {
            this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensor, this.mRate);
            this.mEnabled = true;
        }
    }

    public void disable() {
        if (this.mSensor == null) {
            Log.w("LumenEventListener", "Cannot detect sensors. Invalid disable");
            return;
        }
        if (this.mEnabled) {
            this.mSensorManager.unregisterListener(this.mSensorEventListener);
            this.mEnabled = false;
        }
    }
}
