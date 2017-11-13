package com.android.engineeringmode.manualtest;

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

import java.util.Calendar;

public class MSensorAutoTest2 extends AutoTestItemActivity {
    private Boolean isPassed = Boolean.valueOf(false);
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
    private TextView mTitle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903112);
        setTitle(2131297005);
        this.mTitle = (TextView) findViewById(2131493176);
        this.mTitle.setText(getString(2131297005));
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
        this.mGyroSensor = this.mSensorManager.getDefaultSensor(2);
        this.mSensorEventListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                MSensorAutoTest2 mSensorAutoTest2;
                if (MSensorAutoTest2.this.mIndex < 300) {
                    int x = Math.round(event.values[0]);
                    int y = Math.round(event.values[1]);
                    int z = Math.round(event.values[2]);
                    if (Math.abs(x) > Math.abs(MSensorAutoTest2.this.mDiffDataX)) {
                        MSensorAutoTest2.this.mDiffDataX = x;
                    }
                    if (Math.abs(y) > Math.abs(MSensorAutoTest2.this.mDiffDataY)) {
                        MSensorAutoTest2.this.mDiffDataY = y;
                    }
                    if (Math.abs(z) > Math.abs(MSensorAutoTest2.this.mDiffDataZ)) {
                        MSensorAutoTest2.this.mDiffDataZ = z;
                    }
                    mSensorAutoTest2 = MSensorAutoTest2.this;
                    mSensorAutoTest2.mSumX = mSensorAutoTest2.mSumX + x;
                    mSensorAutoTest2 = MSensorAutoTest2.this;
                    mSensorAutoTest2.mSumY = mSensorAutoTest2.mSumY + y;
                    mSensorAutoTest2 = MSensorAutoTest2.this;
                    mSensorAutoTest2.mSumZ = mSensorAutoTest2.mSumZ + z;
                    mSensorAutoTest2 = MSensorAutoTest2.this;
                    mSensorAutoTest2.mSelfDataX = mSensorAutoTest2.mSelfDataX + Math.abs(x);
                    mSensorAutoTest2 = MSensorAutoTest2.this;
                    mSensorAutoTest2.mSelfDataY = mSensorAutoTest2.mSelfDataY + Math.abs(y);
                    mSensorAutoTest2 = MSensorAutoTest2.this;
                    mSensorAutoTest2.mSelfDataZ = mSensorAutoTest2.mSelfDataZ + Math.abs(z);
                } else if (MSensorAutoTest2.this.mIndex == 300) {
                    MSensorAutoTest2.this.mSelfDataX = MSensorAutoTest2.this.mSelfDataX / 300;
                    MSensorAutoTest2.this.mSelfDataY = MSensorAutoTest2.this.mSelfDataY / 300;
                    MSensorAutoTest2.this.mSelfDataZ = MSensorAutoTest2.this.mSelfDataZ / 300;
                    MSensorAutoTest2.this.mSelfDataX = MSensorAutoTest2.this.mSumX != 0 ? MSensorAutoTest2.this.mSelfDataX * (MSensorAutoTest2.this.mSumX / Math.abs(MSensorAutoTest2.this.mSumX)) : MSensorAutoTest2.this.mSelfDataX;
                    MSensorAutoTest2.this.mSelfDataY = MSensorAutoTest2.this.mSumY != 0 ? MSensorAutoTest2.this.mSelfDataY * (MSensorAutoTest2.this.mSumY / Math.abs(MSensorAutoTest2.this.mSumY)) : MSensorAutoTest2.this.mSelfDataY;
                    MSensorAutoTest2.this.mSelfDataZ = MSensorAutoTest2.this.mSumZ != 0 ? MSensorAutoTest2.this.mSelfDataZ * (MSensorAutoTest2.this.mSumZ / Math.abs(MSensorAutoTest2.this.mSumZ)) : MSensorAutoTest2.this.mSelfDataZ;
                    MSensorAutoTest2.this.mSelfX.setText(String.valueOf(MSensorAutoTest2.this.mSelfDataX));
                    MSensorAutoTest2.this.mSelfY.setText(String.valueOf(MSensorAutoTest2.this.mSelfDataY));
                    MSensorAutoTest2.this.mSelfZ.setText(String.valueOf(MSensorAutoTest2.this.mSelfDataZ));
                    MSensorAutoTest2.this.mDiffX.setText(String.valueOf(MSensorAutoTest2.this.mDiffDataX));
                    MSensorAutoTest2.this.mDiffY.setText(String.valueOf(MSensorAutoTest2.this.mDiffDataY));
                    MSensorAutoTest2.this.mDiffZ.setText(String.valueOf(MSensorAutoTest2.this.mDiffDataZ));
                    MSensorAutoTest2.this.computeNormal();
                }
                mSensorAutoTest2 = MSensorAutoTest2.this;
                mSensorAutoTest2.mIndex = mSensorAutoTest2.mIndex + 1;
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
                MSensorAutoTest2.this.endActivity();
            }
        });
    }

    public void computeNormal() {
        this.mNormalDataX = this.mSelfDataX - this.mDiffDataX;
        this.mNormalDataY = this.mSelfDataY - this.mDiffDataY;
        this.mNormalDataZ = this.mSelfDataZ - this.mDiffDataZ;
        if (Math.abs(this.mNormalDataX) > 200 || Math.abs(this.mNormalDataY) > 200 || Math.abs(this.mNormalDataZ) > 200) {
            this.mResult.setText("Fail");
            this.isPassed = Boolean.valueOf(false);
            this.mResult.setTextColor(-65536);
        } else {
            this.mResult.setText("Pass");
            this.isPassed = Boolean.valueOf(true);
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

    protected void onStop() {
        super.onStop();
        Calendar now = Calendar.getInstance();
        String time = now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13);
        String content;
        if (this.isPassed.booleanValue()) {
            content = time + "--MSensorAutoTest2--" + "PASS";
        } else {
            content = time + "--MSensorAutoTest2--" + "FAIL";
        }
    }
}
