package com.android.engineeringmode.autotest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.engineeringmode.autotest.CameraPreview.OnOpenCameraFailedListener;

public class BackCameraTest extends AutoTestItemActivity {
    private static final int[] mIntercepttKeys = new int[]{82, 3, 4};
    private volatile boolean isNear;
    private CameraPreview mPreview;
    private Sensor mSensor;
    private SensorEventListener mSensorEventListener;
    private SensorManager mSensorManager;

    class SensorEventListenerImpl implements SensorEventListener {
        SensorEventListenerImpl() {
        }

        public void onSensorChanged(SensorEvent event) {
            float X = event.values[0];
            if (X <= 2.0f) {
                Log.d("AutoTestItemActivity", "SensorEventListener--X <= 2");
                BackCameraTest.this.isNear = true;
            } else {
                Log.d("AutoTestItemActivity", "SensorEventListener--X > 2");
                BackCameraTest.this.isNear = false;
            }
            System.out.println("onSensorChanged , x = " + X);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("AutoTestItemActivity", "BackCameraTest--onCreate");
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        getWindow().setFlags(128, 128);
        setContentView(2130903072);
        this.mPreview = (CameraPreview) findViewById(2131492934);
        this.mPreview.setDevice(0);
        this.mPreview.setOnOpenCameraFailedListener(new OnOpenCameraFailedListener() {
            public void onFail() {
                Toast.makeText(BackCameraTest.this, "Can't open the Camera", 1).show();
                BackCameraTest.this.setAutoExit(1000);
            }
        });
        if (checkIsAutoAging() || checkIsAutoTest()) {
            Log.d("AutoTestItemActivity", "checkIsAutoAging() || checkIsAutoTest()");
            setAutoExit(15000);
        }
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mSensor = this.mSensorManager.getDefaultSensor(8);
        if (this.mSensor != null) {
            this.mSensorEventListener = new SensorEventListenerImpl();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    protected void onResume() {
        super.onResume();
        this.mPreview.open();
        this.isNear = false;
        if (this.mSensor != null) {
            this.mSensorManager.registerListener(this.mSensorEventListener, this.mSensor, 3);
        }
    }

    protected void onPause() {
        super.onPause();
        this.mPreview.close();
        if (this.mSensor != null) {
            this.mSensorManager.unregisterListener(this.mSensorEventListener);
        }
    }
}
