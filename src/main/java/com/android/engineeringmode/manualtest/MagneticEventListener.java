package com.android.engineeringmode.manualtest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public abstract class MagneticEventListener {
    private boolean mEnabled;
    float[] mGData;
    private Sensor mGSensor;
    float[] mI;
    float[] mMData;
    private Sensor mMSensor;
    float[] mOriData;
    private Sensor mOriSensor;
    private int mOrientation;
    float[] mR;
    private int mRate;
    private SensorEventListener mSensorEventListener;
    private SensorManager mSensorManager;
    float[] orien;

    class SensorEventListenerImpl implements SensorEventListener {
        SensorEventListenerImpl() {
        }

        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            int type = event.sensor.getType();
            if (type == 1) {
                MagneticEventListener.this.mGData[0] = values[0];
                MagneticEventListener.this.mGData[1] = values[2];
                MagneticEventListener.this.mGData[2] = values[2];
            } else if (type == 2) {
                MagneticEventListener.this.mMData[0] = values[0];
                MagneticEventListener.this.mMData[1] = values[1];
                MagneticEventListener.this.mMData[2] = values[2];
            } else if (type == 3) {
                MagneticEventListener.this.mOriData[0] = values[0];
                MagneticEventListener.this.mOriData[1] = values[1];
                MagneticEventListener.this.mOriData[2] = values[2];
            } else {
                return;
            }
            SensorManager.getRotationMatrix(MagneticEventListener.this.mR, MagneticEventListener.this.mI, MagneticEventListener.this.mGData, MagneticEventListener.this.mMData);
            MagneticEventListener.this.orien = SensorManager.getOrientation(MagneticEventListener.this.mR, MagneticEventListener.this.orien);
            float incl = SensorManager.getInclination(MagneticEventListener.this.mI);
            int orientation = (int) (MagneticEventListener.this.orien[0] * 57.29578f);
            while (orientation >= 360) {
                orientation -= 360;
            }
            while (orientation < 0) {
                orientation += 360;
            }
            if (orientation != MagneticEventListener.this.mOrientation) {
                MagneticEventListener.this.mOrientation = orientation;
                MagneticEventListener.this.onDistanceChanged(MagneticEventListener.this.mMData, (int) MagneticEventListener.this.mOriData[0]);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    public abstract void onDistanceChanged(float[] fArr, int i);

    public MagneticEventListener(Context context) {
        this(context, 1);
    }

    public MagneticEventListener(Context context, int rate) {
        this.mEnabled = false;
        this.mOrientation = -2;
        this.mGData = new float[3];
        this.mMData = new float[3];
        this.mOriData = new float[3];
        this.mR = new float[16];
        this.mI = new float[16];
        this.orien = new float[3];
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        this.mRate = rate;
    }

    public void enable() {
        this.mGSensor = this.mSensorManager.getDefaultSensor(2);
        this.mMSensor = this.mSensorManager.getDefaultSensor(1);
        this.mOriSensor = this.mSensorManager.getDefaultSensor(3);
        if (this.mGSensor != null) {
            this.mSensorEventListener = new SensorEventListenerImpl();
        }
        if (this.mGSensor == null) {
            Log.w("DistanceEventListener", "Cannot detect sensors. Not enabled");
            return;
        }
        if (!this.mEnabled) {
            this.mSensorManager.registerListener(this.mSensorEventListener, this.mGSensor, this.mRate);
            this.mSensorManager.registerListener(this.mSensorEventListener, this.mMSensor, this.mRate);
            this.mSensorManager.registerListener(this.mSensorEventListener, this.mOriSensor, this.mRate);
            this.mEnabled = true;
        }
    }

    public void disable() {
        if (this.mGSensor == null) {
            Log.w("DistanceEventListener", "Cannot detect sensors. Invalid disable");
            return;
        }
        if (this.mEnabled) {
            this.mSensorManager.unregisterListener(this.mSensorEventListener);
            this.mEnabled = false;
        }
    }
}
