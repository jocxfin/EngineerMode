package com.android.engineeringmode.gyroscopetest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.engineeringmode.autotest.AutoTestItemActivity;

public class GyroscopeSelfTest extends AutoTestItemActivity {
    private int mDiffDataX;
    private int mDiffDataY;
    private int mDiffDataZ;
    private TextView mDiffX;
    private TextView mDiffY;
    private TextView mDiffZ;
    private Button mExit;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903112);
        setTitle(2131297103);
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
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mGyroSensor = this.mSensorManager.getDefaultSensor(4);
        this.mSensorEventListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                GyroscopeSelfTest gyroscopeSelfTest;
                if (GyroscopeSelfTest.this.mIndex < 300) {
                    int x = (int) Math.round(((double) event.values[0]) * 100.0d);
                    int y = (int) Math.round(((double) event.values[1]) * 100.0d);
                    int z = (int) Math.round(((double) event.values[2]) * 100.0d);
                    if (Math.abs(x) > Math.abs(GyroscopeSelfTest.this.mDiffDataX)) {
                        GyroscopeSelfTest.this.mDiffDataX = x;
                    }
                    if (Math.abs(y) > Math.abs(GyroscopeSelfTest.this.mDiffDataY)) {
                        GyroscopeSelfTest.this.mDiffDataY = y;
                    }
                    if (Math.abs(z) > Math.abs(GyroscopeSelfTest.this.mDiffDataZ)) {
                        GyroscopeSelfTest.this.mDiffDataZ = z;
                    }
                    gyroscopeSelfTest = GyroscopeSelfTest.this;
                    gyroscopeSelfTest.mSumX = gyroscopeSelfTest.mSumX + x;
                    gyroscopeSelfTest = GyroscopeSelfTest.this;
                    gyroscopeSelfTest.mSumY = gyroscopeSelfTest.mSumY + y;
                    gyroscopeSelfTest = GyroscopeSelfTest.this;
                    gyroscopeSelfTest.mSumZ = gyroscopeSelfTest.mSumZ + z;
                    gyroscopeSelfTest = GyroscopeSelfTest.this;
                    gyroscopeSelfTest.mSelfDataX = gyroscopeSelfTest.mSelfDataX + Math.abs(x);
                    gyroscopeSelfTest = GyroscopeSelfTest.this;
                    gyroscopeSelfTest.mSelfDataY = gyroscopeSelfTest.mSelfDataY + Math.abs(y);
                    gyroscopeSelfTest = GyroscopeSelfTest.this;
                    gyroscopeSelfTest.mSelfDataZ = gyroscopeSelfTest.mSelfDataZ + Math.abs(z);
                } else if (GyroscopeSelfTest.this.mIndex == 300) {
                    GyroscopeSelfTest.this.mSelfDataX = GyroscopeSelfTest.this.mSelfDataX / 300;
                    GyroscopeSelfTest.this.mSelfDataY = GyroscopeSelfTest.this.mSelfDataY / 300;
                    GyroscopeSelfTest.this.mSelfDataZ = GyroscopeSelfTest.this.mSelfDataZ / 300;
                    GyroscopeSelfTest.this.mSelfDataX = GyroscopeSelfTest.this.mSumX != 0 ? GyroscopeSelfTest.this.mSelfDataX * (GyroscopeSelfTest.this.mSumX / Math.abs(GyroscopeSelfTest.this.mSumX)) : GyroscopeSelfTest.this.mSelfDataX;
                    GyroscopeSelfTest.this.mSelfDataY = GyroscopeSelfTest.this.mSumY != 0 ? GyroscopeSelfTest.this.mSelfDataY * (GyroscopeSelfTest.this.mSumY / Math.abs(GyroscopeSelfTest.this.mSumY)) : GyroscopeSelfTest.this.mSelfDataY;
                    GyroscopeSelfTest.this.mSelfDataZ = GyroscopeSelfTest.this.mSumZ != 0 ? GyroscopeSelfTest.this.mSelfDataZ * (GyroscopeSelfTest.this.mSumZ / Math.abs(GyroscopeSelfTest.this.mSumZ)) : GyroscopeSelfTest.this.mSelfDataZ;
                    GyroscopeSelfTest.this.mSelfX.setText(String.valueOf(GyroscopeSelfTest.this.mSelfDataX));
                    GyroscopeSelfTest.this.mSelfY.setText(String.valueOf(GyroscopeSelfTest.this.mSelfDataY));
                    GyroscopeSelfTest.this.mSelfZ.setText(String.valueOf(GyroscopeSelfTest.this.mSelfDataZ));
                    GyroscopeSelfTest.this.mDiffX.setText(String.valueOf(GyroscopeSelfTest.this.mDiffDataX));
                    GyroscopeSelfTest.this.mDiffY.setText(String.valueOf(GyroscopeSelfTest.this.mDiffDataY));
                    GyroscopeSelfTest.this.mDiffZ.setText(String.valueOf(GyroscopeSelfTest.this.mDiffDataZ));
                    GyroscopeSelfTest.this.computeNormal();
                }
                gyroscopeSelfTest = GyroscopeSelfTest.this;
                gyroscopeSelfTest.mIndex = gyroscopeSelfTest.mIndex + 1;
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        this.mExit = (Button) findViewById(2131493142);
        if (checkIsAutoAging() || checkIsAutoTest()) {
            this.mExit.setVisibility(0);
        } else {
            this.mExit.setVisibility(8);
        }
        this.mExit.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                GyroscopeSelfTest.this.endActivity();
            }
        });
    }

    public void computeNormal() {
        this.mNormalDataX = this.mSelfDataX - this.mDiffDataX;
        this.mNormalDataY = this.mSelfDataY - this.mDiffDataY;
        this.mNormalDataZ = this.mSelfDataZ - this.mDiffDataZ;
        if (Math.abs(this.mNormalDataX) > 200 || Math.abs(this.mNormalDataY) > 200 || Math.abs(this.mNormalDataZ) > 200) {
            this.mResult.setText("Fail");
            this.mResult.setTextColor(-65536);
        } else {
            this.mResult.setText("Pass");
            this.mResult.setTextColor(-16711936);
        }
        this.mNormalX.setText(String.valueOf(this.mNormalDataX));
        this.mNormalY.setText(String.valueOf(this.mNormalDataY));
        this.mNormalZ.setText(String.valueOf(this.mNormalDataZ));
    }

    protected void onPause() {
        super.onPause();
        this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mGyroSensor);
    }

    protected void onResume() {
        super.onResume();
        this.mSensorManager.registerListener(this.mSensorEventListener, this.mGyroSensor, 0);
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
        this.mNormalX.setText("");
        this.mNormalY.setText("");
        this.mNormalZ.setText("");
        this.mSelfX.setText("");
        this.mSelfY.setText("");
        this.mSelfZ.setText("");
        this.mDiffX.setText("");
        this.mDiffY.setText("");
        this.mDiffZ.setText("");
        this.mResult.setText(2131297116);
        this.mResult.setTextColor(-16777216);
    }
}
