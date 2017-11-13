package com.android.engineeringmode.gyroscopetest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.android.engineeringmode.autotest.AutoTestItemActivity;

import java.util.Date;
import java.util.LinkedList;

public class GyroDispalyData extends AutoTestItemActivity {
    private GyroDataAdapter mAdapter;
    private LinkedList<Data> mContainer;
    private int mCount;
    private Sensor mGyroSensor;
    private long mLastTime;
    private ListView mListView;
    private SensorEventListener mSensorEventListener;
    private SensorManager mSensorManager;
    private long mStartTime;
    private TextView mTime;

    public class Data {
        String count;
        String x;
        String y;
        String z;

        public Data(String count, String x, String y, String z) {
            this.count = count;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(128, 128);
        setContentView(2130903107);
        setTitle(2131297103);
        this.mTime = (TextView) findViewById(2131493143);
        this.mListView = (ListView) findViewById(2131493144);
        this.mContainer = new LinkedList();
        this.mAdapter = new GyroDataAdapter(this, this.mContainer);
        this.mListView.setAdapter(this.mAdapter);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mGyroSensor = this.mSensorManager.getDefaultSensor(4);
        this.mSensorEventListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                if (System.currentTimeMillis() - GyroDispalyData.this.mStartTime < 120000 && System.currentTimeMillis() - GyroDispalyData.this.mLastTime >= 500) {
                    float x = event.values[0];
                    float y = event.values[1];
                    x = (float) (((double) Math.round(((((double) x) / 3.141592653589793d) * 180.0d) * 1000.0d)) / 1000.0d);
                    y = (float) (((double) Math.round(((((double) y) / 3.141592653589793d) * 180.0d) * 1000.0d)) / 1000.0d);
                    float z = (float) (((double) Math.round(((((double) event.values[2]) / 3.141592653589793d) * 180.0d) * 1000.0d)) / 1000.0d);
                    GyroDispalyData gyroDispalyData = GyroDispalyData.this;
                    gyroDispalyData.mCount = gyroDispalyData.mCount + 1;
                    GyroDispalyData.this.mContainer.add(new Data(String.valueOf(GyroDispalyData.this.mCount), "X: " + x, "Y: " + y, "Z: " + z));
                    int temp = GyroDispalyData.this.mContainer.size() - 4;
                    ListView - get4 = GyroDispalyData.this.mListView;
                    if (temp < 0) {
                        temp = 0;
                    }
                    -get4.setSelection(temp);
                    GyroDispalyData.this.mLastTime = System.currentTimeMillis();
                    GyroDispalyData.this.mAdapter.notifyDataSetChanged();
                }
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    protected void onPause() {
        super.onPause();
        this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mGyroSensor);
    }

    protected void onResume() {
        super.onResume();
        this.mCount = 0;
        this.mContainer.clear();
        this.mAdapter.notifyDataSetChanged();
        this.mSensorManager.registerListener(this.mSensorEventListener, this.mGyroSensor, 0);
        this.mStartTime = System.currentTimeMillis();
        this.mLastTime = this.mStartTime;
        this.mTime.setText(new Date(this.mStartTime).toLocaleString());
    }
}
