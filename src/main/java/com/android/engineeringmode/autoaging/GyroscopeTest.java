package com.android.engineeringmode.autoaging;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class GyroscopeTest extends BaseTest {
    private int mDiffDataX;
    private int mDiffDataY;
    private int mDiffDataZ;
    private TextView mDiffX;
    private TextView mDiffY;
    private TextView mDiffZ;
    private Sensor mGyroSensor;
    private int mIndex;
    private int mNormalDataX;
    private int mNormalDataY;
    private int mNormalDataZ;
    private TextView mNormalX;
    private TextView mNormalY;
    private TextView mNormalZ;
    private TextView mResult;
    private int mSelfDataX;
    private int mSelfDataY;
    private int mSelfDataZ;
    private TextView mSelfX;
    private TextView mSelfY;
    private TextView mSelfZ;
    private SensorEventListener mSensorEventListener;
    private SensorManager mSensorManager;
    private int mSumX;
    private int mSumY;
    private int mSumZ;
    private int mTestCountFail = 0;
    private int mTestCountTotal = 0;
    private TextView mTextViewResulFail;
    private TextView mTextViewResultTotal;

    protected void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setTitle(2131297104);
        setContentView(2130903109);
        this.mNormalX = (TextView) findViewById(2131493149);
        this.mNormalY = (TextView) findViewById(2131493150);
        this.mNormalZ = (TextView) findViewById(2131493151);
        this.mSelfX = (TextView) findViewById(2131493152);
        this.mSelfY = (TextView) findViewById(2131493153);
        this.mSelfZ = (TextView) findViewById(2131493154);
        this.mDiffX = (TextView) findViewById(2131493155);
        this.mDiffY = (TextView) findViewById(2131493156);
        this.mDiffZ = (TextView) findViewById(2131493157);
        this.mResult = (TextView) findViewById(2131492997);
        this.mTextViewResultTotal = (TextView) findViewById(2131493158);
        this.mTextViewResulFail = (TextView) findViewById(2131493159);
        this.mTextViewResultTotal.setText("test count total : " + this.mTestCountTotal);
        this.mTextViewResulFail.setText("fail count: " + this.mTestCountFail);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mGyroSensor = this.mSensorManager.getDefaultSensor(4);
        this.mSensorEventListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                GyroscopeTest gyroscopeTest;
                if (GyroscopeTest.this.mIndex < 300) {
                    int x = (int) Math.round(((double) event.values[0]) * 100.0d);
                    int y = (int) Math.round(((double) event.values[1]) * 100.0d);
                    int z = (int) Math.round(((double) event.values[2]) * 100.0d);
                    if (Math.abs(x) > Math.abs(GyroscopeTest.this.mDiffDataX)) {
                        GyroscopeTest.this.mDiffDataX = x;
                    }
                    if (Math.abs(y) > Math.abs(GyroscopeTest.this.mDiffDataY)) {
                        GyroscopeTest.this.mDiffDataY = y;
                    }
                    if (Math.abs(z) > Math.abs(GyroscopeTest.this.mDiffDataZ)) {
                        GyroscopeTest.this.mDiffDataZ = z;
                    }
                    gyroscopeTest = GyroscopeTest.this;
                    gyroscopeTest.mSumX = gyroscopeTest.mSumX + x;
                    gyroscopeTest = GyroscopeTest.this;
                    gyroscopeTest.mSumY = gyroscopeTest.mSumY + y;
                    gyroscopeTest = GyroscopeTest.this;
                    gyroscopeTest.mSumZ = gyroscopeTest.mSumZ + z;
                    gyroscopeTest = GyroscopeTest.this;
                    gyroscopeTest.mSelfDataX = gyroscopeTest.mSelfDataX + Math.abs(x);
                    gyroscopeTest = GyroscopeTest.this;
                    gyroscopeTest.mSelfDataY = gyroscopeTest.mSelfDataY + Math.abs(y);
                    gyroscopeTest = GyroscopeTest.this;
                    gyroscopeTest.mSelfDataZ = gyroscopeTest.mSelfDataZ + Math.abs(z);
                } else if (GyroscopeTest.this.mIndex == 300) {
                    GyroscopeTest.this.mSelfDataX = GyroscopeTest.this.mSelfDataX / 300;
                    GyroscopeTest.this.mSelfDataY = GyroscopeTest.this.mSelfDataY / 300;
                    GyroscopeTest.this.mSelfDataZ = GyroscopeTest.this.mSelfDataZ / 300;
                    GyroscopeTest.this.mSelfDataX = GyroscopeTest.this.mSumX != 0 ? GyroscopeTest.this.mSelfDataX * (GyroscopeTest.this.mSumX / Math.abs(GyroscopeTest.this.mSumX)) : GyroscopeTest.this.mSelfDataX;
                    GyroscopeTest.this.mSelfDataY = GyroscopeTest.this.mSumY != 0 ? GyroscopeTest.this.mSelfDataY * (GyroscopeTest.this.mSumY / Math.abs(GyroscopeTest.this.mSumY)) : GyroscopeTest.this.mSelfDataY;
                    GyroscopeTest.this.mSelfDataZ = GyroscopeTest.this.mSumZ != 0 ? GyroscopeTest.this.mSelfDataZ * (GyroscopeTest.this.mSumZ / Math.abs(GyroscopeTest.this.mSumZ)) : GyroscopeTest.this.mSelfDataZ;
                    GyroscopeTest.this.mSelfX.setText(String.valueOf(GyroscopeTest.this.mSelfDataX));
                    GyroscopeTest.this.mSelfY.setText(String.valueOf(GyroscopeTest.this.mSelfDataY));
                    GyroscopeTest.this.mSelfZ.setText(String.valueOf(GyroscopeTest.this.mSelfDataZ));
                    GyroscopeTest.this.mDiffX.setText(String.valueOf(GyroscopeTest.this.mDiffDataX));
                    GyroscopeTest.this.mDiffY.setText(String.valueOf(GyroscopeTest.this.mDiffDataY));
                    GyroscopeTest.this.mDiffZ.setText(String.valueOf(GyroscopeTest.this.mDiffDataZ));
                    GyroscopeTest.this.computeNormal();
                    GyroscopeTest.this.mIndex = 0;
                    GyroscopeTest.this.clearData();
                }
                gyroscopeTest = GyroscopeTest.this;
                gyroscopeTest.mIndex = gyroscopeTest.mIndex + 1;
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        this.mSensorManager.registerListener(this.mSensorEventListener, this.mGyroSensor, 0);
    }

    protected void endTest() {
        this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mGyroSensor);
    }

    protected void runTest() {
    }

    public void computeNormal() {
        System.out.println("mSelfDataX:" + this.mSelfDataX + ", mDiffDataX:" + this.mDiffDataX);
        this.mTestCountTotal++;
        this.mTextViewResultTotal.setText("test count total : " + this.mTestCountTotal);
        this.mNormalDataX = this.mSelfDataX - this.mDiffDataX;
        this.mNormalDataY = this.mSelfDataY - this.mDiffDataY;
        this.mNormalDataZ = this.mSelfDataZ - this.mDiffDataZ;
        if (Math.abs(this.mNormalDataX) > 200 || Math.abs(this.mNormalDataY) > 200 || Math.abs(this.mNormalDataZ) > 200) {
            this.mResult.setText("Fail");
            this.mResult.setTextColor(-65536);
            this.mTestCountFail++;
            this.mTextViewResulFail.setText("fail count: " + this.mTestCountFail);
        } else {
            this.mResult.setText("Pass");
            this.mResult.setTextColor(-16776961);
            this.mTextViewResulFail.setText("fail count: " + this.mTestCountFail);
        }
        this.mNormalX.setText(String.valueOf(this.mNormalDataX));
        this.mNormalY.setText(String.valueOf(this.mNormalDataY));
        this.mNormalZ.setText(String.valueOf(this.mNormalDataZ));
    }

    private void clearData() {
        this.mIndex = 0;
        this.mSumX = 0;
        this.mSumY = 0;
        this.mSumZ = 0;
        this.mSelfDataX = 0;
        this.mSelfDataY = 0;
        this.mSelfDataZ = 0;
        this.mDiffDataX = 0;
        this.mDiffDataY = 0;
        this.mDiffDataZ = 0;
        this.mResult.setText(2131297116);
        this.mResult.setTextColor(-16777216);
    }
}
