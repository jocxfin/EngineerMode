package com.android.engineeringmode.manualtest;

import android.app.ListActivity;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;

public class MSensorAutoTest extends ListActivity {
    private static final int[][] VALUE = new int[][]{new int[]{72, 72}, new int[]{0, Light.MAIN_KEY_MAX}, new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}, new int[]{0, 0}, new int[]{1, 254}, new int[]{1, 254}, new int[]{1, 254}, new int[]{15, 15}, new int[]{1, 1}, new int[]{-4096, 4095}, new int[]{-4096, 4095}, new int[]{-4096, 4095}, new int[]{0, 0}, new int[]{1, 1}, new int[]{-100, 100}, new int[]{-100, 100}, new int[]{-1000, -100}, new int[]{0, 0}};
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String[] array = msg.obj.split(";");
            int len = array.length;
            boolean passed = true;
            if (len == MSensorAutoTest.VALUE.length) {
                int i = 0;
                while (i < len) {
                    String[] item = array[i].split(",");
                    int value = Integer.valueOf(item[1]).intValue();
                    if (value < MSensorAutoTest.VALUE[i][0] || value > MSensorAutoTest.VALUE[i][1]) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(" **failed ");
                        sb.append(value);
                        sb.append(" not in[").append(MSensorAutoTest.VALUE[i][0]).append(",").append(MSensorAutoTest.VALUE[i][1]).append("]**");
                        array[i] = new String(item[0] + sb.toString());
                        passed = false;
                    }
                    i++;
                }
            }
            if (passed) {
                MSensorAutoTest.this.setTitle("Passed");
                MSensorAutoTest.this.mResult.setText("Pass");
                MSensorAutoTest.this.mResult.setVisibility(0);
                MSensorAutoTest.this.mListView.setVisibility(8);
                return;
            }
            MSensorAutoTest.this.setTitle("Failed");
            MSensorAutoTest.this.setTitleColor(-65536);
            MSensorAutoTest.this.mListView.setAdapter(new ArrayAdapter(MSensorAutoTest.this, 2130903203, 2131493145, array));
            MSensorAutoTest.this.mResult.setVisibility(8);
            MSensorAutoTest.this.mListView.setVisibility(0);
        }
    };
    private ListView mListView;
    private final SensorListener mListener = new SensorListener() {
        public void onSensorChanged(int sensor, float[] values) {
        }

        public void onAccuracyChanged(int arg0, int arg1) {
        }
    };
    private TextView mResult;
    LightSensorUtils mUtils;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903162);
        this.mResult = (TextView) findViewById(2131492997);
        this.mUtils = new LightSensorUtils(this);
        this.mListView = getListView();
        TextView empty = (TextView) LayoutInflater.from(this).inflate(2130903203, null);
        empty.setText("Test Failed");
        this.mListView.setEmptyView(empty);
        startTest();
    }

    protected void onPause() {
        super.onPause();
        SensorManager sensorManager = (SensorManager) getSystemService("sensor");
        sensorManager.registerListener(this.mListener, 1, 3);
        sensorManager.unregisterListener(this.mListener);
    }

    private void startTest() {
        new Thread() {
            public void run() {
                String data = MSensorAutoTest.this.mUtils.startMsensorAutoTest();
                System.out.println("startMsensorAutoTest, return:" + data);
                if (data != null) {
                    Message msg = Message.obtain(MSensorAutoTest.this.mHandler);
                    msg.obj = data;
                    msg.what = 1;
                    msg.sendToTarget();
                }
            }
        }.start();
    }
}
