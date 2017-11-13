package com.oem.sensorselftest;

import android.app.ListActivity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.engineeringmode.functions.Light;
import com.qualcomm.qti.sensors.core.sensortest.SensorID;
import com.qualcomm.qti.sensors.core.sensortest.SensorID.SensorType;
import com.qualcomm.qti.sensors.core.sensortest.SensorTest;
import com.qualcomm.qti.sensors.core.sensortest.SensorTest.TestType;

import java.util.Iterator;
import java.util.List;

public class RGBSensorSelfTest extends ListActivity {
    private boolean mAccelSensorEnabled = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    Bundle b = msg.getData();
                    RGBSensorSelfTest.this.retrySelfCalibration(b.getString("name"), b.getBoolean("iswakeup"));
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
            SensorStatus testSensor = (SensorStatus) ((SensorListAdapter) RGBSensorSelfTest.this.getListAdapter()).getItem(position);
            Log.d("RGBSensorSelfTest", "onItemClick " + position);
            SensorListItemView sensorView = (SensorListItemView) view;
            if (sensorView.getSensorStatus().getStatus().equals("Test Started")) {
                Log.d("RGBSensorSelfTest", "onItemClick " + position + " already running...");
                return;
            }
            sensorView.setStatus("Test Started", -256);
            new SensorTestTask().execute(new Object[]{testSensor.getSensor(), sensorView});
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
                RGBSensorSelfTest rGBSensorSelfTest = RGBSensorSelfTest.this;
                int-get1 = rGBSensorSelfTest.mRetryCount;
                rGBSensorSelfTest.mRetryCount = -get1 + 1;
                if (-get1 > 20) {
                    Log.e("RGBSensorSelfTest", "retry times match MAX_RETRY_CALIBRATION_COUNT");
                    this.view.setStatus("Test FAILED. Error: " + RGBSensorSelfTest.getTestError(testResult.intValue()), -65536);
                    return;
                }
                SensorStatus testSensor = this.view.getSensorStatus();
                String testSensorName = SensorUtilityFunctions.getSensorTypeString(testSensor.getSensor()) + ": " + testSensor.getSensor().getName();
                Message msg = RGBSensorSelfTest.this.mHandler.obtainMessage(1);
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
        Log.d("RGBSensorSelfTest", "onCreate mCount = " + this.sensorList.size());
        this.mRetryCount = 0;
        setListAdapter(new SensorListAdapter(this, this.sensorList));
        getListView().setOnItemClickListener(new SensorListClickListener());
    }

    public void onResume() {
        super.onResume();
        Log.d("RGBSensorSelfTest", "onResume");
    }

    private void retrySelfCalibration(String sensorName, boolean isWakeupSensor) {
        ListView lv = getListView();
        SensorListAdapter listAdapter = (SensorListAdapter) lv.getAdapter();
        Log.d("RGBSensorSelfTest", "retrySelfCalibration:" + sensorName + " isWakeupSensor: " + isWakeupSensor);
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
        switch (sensor.getType()) {
            case 5:
                return !sensor.isWakeUpSensor();
            case 33171020:
                return true;
            default:
                return false;
        }
    }

    public void onPause() {
        super.onPause();
        Log.d("RGBSensorSelfTest", "onPause");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("RGBSensorSelfTest", "onDestroy");
        this.mRetryCount = 0;
    }
}
