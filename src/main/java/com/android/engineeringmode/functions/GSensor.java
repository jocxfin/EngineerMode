package com.android.engineeringmode.functions;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.lang.ref.WeakReference;

public class GSensor {
    private WeakReference<OnChangedListener> mChangedListener;
    private boolean mEnabled;
    private float mOrientationAngle;
    private int mRate;
    private Sensor mSensor;
    private SensorEventListener mSensorEventListener;
    private SensorManager mSensorManager;

    public interface OnChangedListener {
        void onChanged(float f, float f2, float f3, float f4);
    }

    class SensorEventListenerImpl implements SensorEventListener {
        SensorEventListenerImpl() {
        }

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if (vectorMagnitude(x, y, z) >= 1.5f) {
                float newOrientationAngle = computeNewOrientation(x, y);
                if (!verifyHorizontal(z)) {
                    newOrientationAngle = -1.0f;
                }
                if (GSensor.this.mOrientationAngle != newOrientationAngle || newOrientationAngle == -1.0f) {
                    GSensor.this.mOrientationAngle = newOrientationAngle;
                    GSensor.this.invokeChangedListener(GSensor.this.mOrientationAngle, x, y, z);
                }
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        private float vectorMagnitude(float x, float y, float z) {
            return (float) Math.sqrt((double) (((x * x) + (y * y)) + (z * z)));
        }

        private boolean verifyHorizontal(float z) {
            return z / 9.81f <= 0.9174312f;
        }

        private float computeNewOrientation(float x, float y) {
            float orientationAngle = ((float) (-Math.atan2((double) x, (double) y))) * 57.29578f;
            if (orientationAngle < 0.0f) {
                return orientationAngle + 360.0f;
            }
            return orientationAngle;
        }
    }

    public GSensor(Context context) {
        this(context, 3);
    }

    public GSensor(Context context, int rate) {
        this.mOrientationAngle = -1.0f;
        this.mEnabled = false;
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        this.mRate = rate;
        this.mSensor = this.mSensorManager.getDefaultSensor(1);
        if (this.mSensor != null) {
            this.mSensorEventListener = new SensorEventListenerImpl();
        }
    }

    public void setOnChangedListener(OnChangedListener listener) {
        WeakReference weakReference = null;
        if (listener != null) {
            weakReference = new WeakReference(listener);
        }
        this.mChangedListener = weakReference;
    }

    public OnChangedListener getOnChangedListener() {
        if (this.mChangedListener == null) {
            return null;
        }
        return (OnChangedListener) this.mChangedListener.get();
    }

    private void invokeChangedListener(float angle, float x, float y, float z) {
        OnChangedListener changedListener = getOnChangedListener();
        if (changedListener != null) {
            changedListener.onChanged(angle, x, y, z);
        }
    }

    public void enable() {
        if (this.mSensor == null) {
            Log.w("GSensor", "Cannot detect sensors. Not enabled");
            return;
        }
        if (!this.mEnabled) {
            this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensor, this.mRate);
            this.mEnabled = true;
        }
    }

    public void disable() {
        if (this.mSensor == null) {
            Log.w("GSensor", "Cannot detect sensors. Invalid disable");
            return;
        }
        if (this.mEnabled) {
            this.mSensorManager.unregisterListener(this.mSensorEventListener);
            this.mEnabled = false;
        }
    }
}
