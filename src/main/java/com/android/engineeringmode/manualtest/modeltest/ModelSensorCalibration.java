package com.android.engineeringmode.manualtest.modeltest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.System;
import android.util.Log;
import android.widget.ListView;

import com.android.engineeringmode.functions.Light;
import com.android.internal.view.RotationPolicy;
import com.oem.sensorselftest.SensorListAdapter;
import com.oem.sensorselftest.SensorListItemView;
import com.oem.sensorselftest.SensorStatus;
import com.oem.sensorselftest.SensorUtilityFunctions;
import com.oem.util.Feature;
import com.qualcomm.qti.sensors.core.sensortest.SensorID;
import com.qualcomm.qti.sensors.core.sensortest.SensorID.SensorType;
import com.qualcomm.qti.sensors.core.sensortest.SensorTest;
import com.qualcomm.qti.sensors.core.sensortest.SensorTest.TestType;

import java.util.Iterator;
import java.util.List;

public class ModelSensorCalibration extends ModelTest3ItemActivity {
    private int brightnessMode;
    private Sensor mAccelSensor;
    private boolean mAccelSensorEnabled = false;
    private final SensorEventListener mAccelSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            Log.e("ModelSensorCalibration", "gsensor data x: " + x + " y: " + y + " z: " + event.values[2]);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private int mCount = -1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    if (ModelSensorCalibration.this.mAccelSensorEnabled) {
                        ModelSensorCalibration.this.mSensorManager.unregisterListener(ModelSensorCalibration.this.mAccelSensorListener);
                        ModelSensorCalibration.this.mAccelSensorEnabled = false;
                    }
                    if (ModelSensorCalibration.this.mCount != 0 && ModelSensorCalibration.this.mPositon < ModelSensorCalibration.this.mCount) {
                        SensorStatus testSensor = (SensorStatus) ((SensorListAdapter) ModelSensorCalibration.this.mListView.getAdapter()).getItem(ModelSensorCalibration.this.mPositon);
                        ((SensorListItemView) ModelSensorCalibration.this.mListView.getChildAt(ModelSensorCalibration.this.mPositon)).setStatus("Test Started", -256);
                        new SensorTestTask().execute(new Object[]{testSensor.getSensor(), sensorView});
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private ListView mListView;
    private int mPositon = 0;
    private int mRetryCount = 0;
    SensorManager mSensorManager;
    private int rotation = 0;
    private List<SensorStatus> sensorList;

    private class SensorTestTask extends AsyncTask<Object, Void, Integer> {
        private Sensor sensor;
        private SensorListItemView view;

        private SensorTestTask() {
        }

        protected Integer doInBackground(Object... params) {
            int rv;
            this.sensor = (Sensor) params[0];
            this.view = (SensorListItemView) params[1];
            SensorType type = SensorType.getSensorType(this.sensor);
            if (type == null) {
                rv = -2;
            } else {
                rv = SensorTest.runSensorTest(new SensorID(type, 0), SensorUtilityFunctions.getDataType(this.sensor), TestType.SELFTEST, true, true);
            }
            return Integer.valueOf(rv);
        }

        protected void onPostExecute(Integer testResult) {
            if (testResult.intValue() == 0) {
                this.view.setStatus("Test PASSED", -16711936);
                if (ModelSensorCalibration.this.mPositon < ModelSensorCalibration.this.mCount) {
                    ModelSensorCalibration.this.mPositon = ModelSensorCalibration.this.mPositon + 1;
                }
                if (ModelSensorCalibration.this.mPositon == ModelSensorCalibration.this.mCount) {
                    ModelSensorCalibration.this.onTestPassed();
                    return;
                } else {
                    ModelSensorCalibration.this.mHandler.sendEmptyMessage(1);
                    return;
                }
            }
            ModelSensorCalibration modelSensorCalibration = ModelSensorCalibration.this;
            int-get6 = modelSensorCalibration.mRetryCount;
            modelSensorCalibration.mRetryCount = -get6 + 1;
            if (-get6 < 20) {
                Log.e("ModelSensorCalibration", "retry " + ModelSensorCalibration.this.mRetryCount + " calibration sesor = " + ModelSensorCalibration.this.mPositon);
                ModelSensorCalibration.this.mHandler.sendEmptyMessage(1);
                return;
            }
            this.view.setStatus("Test FAILED. Error: " + ModelSensorCalibration.getTestError(testResult.intValue()), -65536);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903155);
        this.mListView = (ListView) findViewById(2131493376);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        if (this.sensorList == null) {
            this.sensorList = SensorStatus.createSensorList(this.mSensorManager.getSensorList(-1));
        }
        Iterator<SensorStatus> itr = this.sensorList.iterator();
        while (itr.hasNext()) {
            if (!selfTestEnabled(((SensorStatus) itr.next()).getSensor())) {
                itr.remove();
            }
        }
        this.mCount = this.sensorList.size();
        this.rotation = System.getInt(getContentResolver(), "accelerometer_rotation", 0);
        this.brightnessMode = System.getInt(getContentResolver(), "screen_brightness_mode", 0);
        Log.d("ModelSensorCalibration", "onCreate rotation = " + this.rotation);
        this.mRetryCount = 0;
        if (1 == this.rotation) {
            this.mAccelSensor = this.mSensorManager.getDefaultSensor(1);
            this.mSensorManager.registerListener(this.mAccelSensorListener, this.mAccelSensor, 3);
            this.mAccelSensorEnabled = true;
        }
        if (Feature.isProxCalibrationSupported(this)) {
            findViewById(2131493375).setVisibility(0);
        }
        if (this.mCount == 0) {
            findViewById(2131493377).setVisibility(0);
        } else {
            this.mListView.setAdapter(new SensorListAdapter(this, this.sensorList));
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.rotation == 1) {
            RotationPolicy.setRotationLockForAccessibility(this, true);
            System.putInt(getContentResolver(), "accelerometer_rotation", 0);
        }
        if (this.brightnessMode != 0) {
            System.putInt(getContentResolver(), "screen_brightness_mode", 0);
        }
        Log.d("ModelSensorCalibration", "onResume");
        this.mHandler.sendEmptyMessageDelayed(1, 3000);
    }

    protected void onPause() {
        super.onPause();
        this.mHandler.removeMessages(1);
        if (this.rotation == 1) {
            RotationPolicy.setRotationLockForAccessibility(this, false);
            System.putInt(getContentResolver(), "accelerometer_rotation", 1);
        }
        if (this.brightnessMode != 0) {
            System.putInt(getContentResolver(), "screen_brightness_mode", this.brightnessMode);
        }
        Log.d("ModelSensorCalibration", "onPause rotation = " + this.rotation);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("ModelSensorCalibration", "onDestroy");
        this.mRetryCount = 0;
        if (this.mAccelSensorEnabled) {
            this.mSensorManager.unregisterListener(this.mAccelSensorListener);
            this.mAccelSensorEnabled = false;
        }
    }

    private static String getTestError(int error) {
        switch (error) {
            case -50:
                return "Settings Database items not found";
            case -22:
                return "Internal Error";
            case -21:
                return "Broken Message Pipe";
            case -16:
                return "Another test is running";
            case -15:
                return "Received 'failed' response";
            case -14:
                return "Invalid Test Parameter";
            case -13:
                return "Invalid Test";
            case -12:
                return "Device Busy";
            case -3:
                return "Test Timed-out";
            case -2:
                return "Invalid Sensor ID";
            case -1:
                return "Sensor Test Native Error";
            default:
                return "Sensor Specific error: " + String.valueOf(error);
        }
    }

    private boolean selfTestEnabled(Sensor sensor) {
        if (sensor.getVendor().equals("Google Inc.")) {
            return false;
        }
        if (sensor.getType() == 8) {
            if (!sensor.isWakeUpSensor()) {
                return false;
            }
        } else if (sensor.isWakeUpSensor()) {
            return false;
        }
        switch (sensor.getType()) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                return true;
            case Light.CHARGE_RED_LIGHT /*2*/:
                return true;
            case 4:
                return true;
            case 5:
                return Feature.isLightCalibrationSupported(this);
            case 8:
                return Feature.isProxCalibrationSupported(this);
            default:
                return false;
        }
    }
}
