package com.android.engineeringmode.manualtest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public abstract class OrientationEventListener {
    private boolean mEnabled;
    private int mOrientation;
    private int mRate;
    private Sensor mSensor;
    private SensorEventListener mSensorEventListener;
    private SensorManager mSensorManager;

    class SensorEventListenerImpl implements SensorEventListener {
        SensorEventListenerImpl() {
        }

        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            float X = -values[0];
            float Y = -values[1];
            float Z = -values[2];
            if (4.0f * ((X * X) + (Y * Y)) >= Z * Z) {
                int orientation = 90 - Math.round(((float) Math.atan2((double) (-Y), (double) X)) * 57.29578f);
                while (orientation >= 360) {
                    orientation -= 360;
                }
                while (orientation < 0) {
                    orientation += 360;
                }
                if (orientation != OrientationEventListener.this.mOrientation) {
                    OrientationEventListener.this.mOrientation = orientation;
                    OrientationEventListener.this.onOrientationChanged(orientation, event.values);
                }
                return;
            }
            OrientationEventListener.this.mOrientation = -1;
            OrientationEventListener.this.onOrientationChanged(-1, event.values);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    public abstract void onOrientationChanged(int i, float[] fArr);

    public OrientationEventListener(Context context) {
        this(context, 3);
    }

    public OrientationEventListener(Context context, int rate) {
        this.mOrientation = -1;
        this.mEnabled = false;
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        this.mRate = rate;
        this.mSensor = this.mSensorManager.getDefaultSensor(1);
        if (this.mSensor != null) {
            this.mSensorEventListener = new SensorEventListenerImpl();
        }
    }

    public void enable() {
        if (this.mSensor == null) {
            Log.w("OrientationEventListener", "Cannot detect sensors. Not enabled");
            return;
        }
        if (!this.mEnabled) {
            this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensor, this.mRate);
            this.mEnabled = true;
        }
    }

    public void disable() {
        if (this.mSensor == null) {
            Log.w("OrientationEventListener", "Cannot detect sensors. Invalid disable");
            return;
        }
        if (this.mEnabled) {
            this.mSensorManager.unregisterListener(this.mSensorEventListener);
            this.mEnabled = false;
        }
    }
}
