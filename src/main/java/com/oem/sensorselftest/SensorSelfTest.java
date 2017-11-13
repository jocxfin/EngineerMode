package com.oem.sensorselftest;

import android.app.ListActivity;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.engineeringmode.functions.Light;
import com.android.internal.view.RotationPolicy;
import com.oem.util.Feature;
import com.qualcomm.qti.sensors.core.sensortest.SensorID;
import com.qualcomm.qti.sensors.core.sensortest.SensorID.SensorType;
import com.qualcomm.qti.sensors.core.sensortest.SensorTest;
import com.qualcomm.qti.sensors.core.sensortest.SensorTest.TestType;

import java.util.Iterator;
import java.util.List;

public class SensorSelfTest extends ListActivity {
    private int brightnessMode;
    private Sensor mAccelSensor;
    private boolean mAccelSensorEnabled = false;
    private final SensorEventListener mAccelSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            Log.e("SensorSelfTest", "gsensor data x: " + x + " y: " + y + " z: " + event.values[2]);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    Bundle b = msg.getData();
                    SensorSelfTest.this.retrySelfCalibration(b.getString("name"), b.getBoolean("iswakeup"));
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    if (SensorSelfTest.this.mAccelSensorEnabled) {
                        SensorSelfTest.this.mSensorManager.unregisterListener(SensorSelfTest.this.mAccelSensorListener);
                        SensorSelfTest.this.mAccelSensorEnabled = false;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private int mRetryCount = 0;
    SensorManager mSensorManager;
    private int rotation = 0;
    private List<SensorStatus> sensorList;

    private class SensorListClickListener implements OnItemClickListener {
        private SensorListClickListener() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            SensorStatus testSensor = (SensorStatus) ((SensorListAdapter) SensorSelfTest.this.getListAdapter()).getItem(position);
            if (SensorSelfTest.this.mAccelSensorEnabled) {
                Log.d("SensorSelfTest", "gsensor is enalbed ,please wait for seconds");
                return;
            }
            Log.d("SensorSelfTest", "onItemClick " + position);
            SensorListItemView sensorView = (SensorListItemView) view;
            if (sensorView.getSensorStatus().getStatus().equals("Test Started")) {
                Log.d("SensorSelfTest", "onItemClick " + position + " already running...");
            } else {
                sensorView.setStatus("Test Started", -256);
                new SensorTestTask().execute(new Object[]{testSensor.getSensor(), sensorView});
            }
        }
    }

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
            } else {
                SensorSelfTest sensorSelfTest = SensorSelfTest.this;
                int-get3 = sensorSelfTest.mRetryCount;
                sensorSelfTest.mRetryCount = -get3 + 1;
                if (-get3 > 20) {
                    Log.e("SensorSelfTest", "retry times match MAX_RETRY_CALIBRATION_COUNT");
                    this.view.setStatus("Test FAILED. Error: " + SensorSelfTest.getTestError(testResult.intValue()), -65536);
                    return;
                }
                SensorStatus testSensor = this.view.getSensorStatus();
                String testSensorName = SensorUtilityFunctions.getSensorTypeString(testSensor.getSensor()) + ": " + testSensor.getSensor().getName();
                Message msg = SensorSelfTest.this.mHandler.obtainMessage(1);
                Bundle b = new Bundle();
                b.putString("name", testSensorName);
                b.putBoolean("iswakeup", testSensor.getSensor().isWakeUpSensor());
                msg.setData(b);
                msg.sendToTarget();
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        this.rotation = System.getInt(getContentResolver(), "accelerometer_rotation", 0);
        this.brightnessMode = System.getInt(getContentResolver(), "screen_brightness_mode", 0);
        Log.d("SensorSelfTest", "onCreate rotation = " + this.rotation);
        Log.d("SensorSelfTest", "onCreate mCount = " + this.sensorList.size());
        this.mRetryCount = 0;
        if (1 == this.rotation) {
            this.mAccelSensor = this.mSensorManager.getDefaultSensor(1);
            this.mSensorManager.registerListener(this.mAccelSensorListener, this.mAccelSensor, 3);
            this.mAccelSensorEnabled = true;
        }
        setListAdapter(new SensorListAdapter(this, this.sensorList));
        getListView().setOnItemClickListener(new SensorListClickListener());
    }

    public void onResume() {
        super.onResume();
        Log.d("SensorSelfTest", "onResume");
        if (this.rotation == 1) {
            RotationPolicy.setRotationLockForAccessibility(this, true);
            System.putInt(getContentResolver(), "accelerometer_rotation", 0);
        }
        if (this.brightnessMode != 0) {
            System.putInt(getContentResolver(), "screen_brightness_mode", 0);
        }
        this.mHandler.sendEmptyMessageDelayed(2, 1000);
    }

    private void retrySelfCalibration(String sensorName, boolean isWakeupSensor) {
        ListView lv = getListView();
        SensorListAdapter listAdapter = (SensorListAdapter) lv.getAdapter();
        Log.d("SensorSelfTest", "retrySelfCalibration:" + sensorName + " isWakeupSensor: " + isWakeupSensor);
        for (int i = 0; i < this.sensorList.size(); i++) {
            SensorStatus testSensor = (SensorStatus) listAdapter.getItem(i);
            SensorListItemView sensorView = (SensorListItemView) lv.getChildAt(i);
            if (sensorView.getSensorName().equals(sensorName)) {
                sensorView.setStatus("Test Started", -256);
                new SensorTestTask().execute(new Object[]{testSensor.getSensor(), sensorView});
            }
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

    public void onPause() {
        super.onPause();
        Log.d("SensorSelfTest", "onPause");
        this.mHandler.removeMessages(1);
        if (this.rotation == 1) {
            RotationPolicy.setRotationLockForAccessibility(this, false);
            System.putInt(getContentResolver(), "accelerometer_rotation", 1);
        }
        if (this.brightnessMode != 0) {
            System.putInt(getContentResolver(), "screen_brightness_mode", this.brightnessMode);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("SensorSelfTest", "onDestroy");
        this.mRetryCount = 0;
        if (this.mAccelSensorEnabled) {
            this.mSensorManager.unregisterListener(this.mAccelSensorListener);
            this.mAccelSensorEnabled = false;
        }
    }
}
