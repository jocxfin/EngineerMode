package com.android.engineeringmode.gyroscopetest;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Timer;
import java.util.TimerTask;

public class GyroscopeCalibrateTest extends Activity implements OnClickListener {
    private float GyrX_Sens = 0.0f;
    private float GyrY_Sens = 0.0f;
    private float GyrZ_Sens = 0.0f;
    private boolean GyroSensCalculationOnGoing = false;
    private float Gyro_Sens = 0.0f;
    private float Gyro_Sens_Initial = 1.0f;
    private float Tot_Angle_rad = 0.0f;
    private Runnable doSomething = new Runnable() {
        public void run() {
            if (GyroscopeCalibrateTest.this.GyroSensCalculationOnGoing) {
                GyroscopeCalibrateTest.this.mCurrentAngle.setText("Angle [deg] = " + String.valueOf(((double) (GyroscopeCalibrateTest.this.Tot_Angle_rad * 180.0f)) / 3.141592653589793d));
            }
        }
    };
    private long gyroTimeStamp_OLD = -1;
    private int mCount = 3;
    private TextView mCurrentAngle;
    private TextView mGyroSens;
    private Sensor mGyroSensor;
    private CountDownTimer mPreTimer = new CountDownTimer(5000, 1000) {
        public void onTick(long millisUntilFinished) {
            GyroscopeCalibrateTest.this.mStart.setEnabled(false);
            int i = (int) (millisUntilFinished / 1000);
            GyroscopeCalibrateTest.this.mTimerText.setVisibility(0);
            GyroscopeCalibrateTest.this.mTimerText.setText(Html.fromHtml("Acquisition will start in <b>" + i + "</b>" + " sec"));
        }

        public void onFinish() {
            GyroscopeCalibrateTest.this.mTimerText.setVisibility(8);
            GyroscopeCalibrateTest.this.GyroSensCalculation();
        }
    };
    private Button mSave;
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            long l1 = event.timestamp;
            long l2 = l1 - GyroscopeCalibrateTest.this.gyroTimeStamp_OLD;
            if (GyroscopeCalibrateTest.this.GyroSensCalculationOnGoing) {
                GyroscopeCalibrateTest.this.Tot_Angle_rad = (float) (((double) GyroscopeCalibrateTest.this.Tot_Angle_rad) + (((double) (event.values[GyroscopeCalibrateTest.this.mCount] / GyroscopeCalibrateTest.this.Gyro_Sens_Initial)) * (((double) l2) / 1.0E9d)));
            }
            GyroscopeCalibrateTest.this.gyroTimeStamp_OLD = l1;
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private SensorManager mSensorManager;
    private Button mStart;
    private CountDownTimer mTimer = new CountDownTimer(10000, 1000) {
        public void onTick(long millisUntilFinished) {
            int i = (int) (millisUntilFinished / 1000);
            GyroscopeCalibrateTest.this.mTimerText.setVisibility(0);
            GyroscopeCalibrateTest.this.mTimerText.setText(Html.fromHtml("Calculation ends <b>" + i + "</b>" + " sec"));
        }

        public void onFinish() {
            GyroscopeCalibrateTest.this.Gyro_Sens = Math.abs((float) (6.283185307179586d / ((double) GyroscopeCalibrateTest.this.Tot_Angle_rad)));
            if (GyroscopeCalibrateTest.this.mCount == 2) {
                GyroscopeCalibrateTest.this.mGyroSens.append("Z : ");
                GyroscopeCalibrateTest.this.GyrZ_Sens = GyroscopeCalibrateTest.this.Gyro_Sens;
            } else if (GyroscopeCalibrateTest.this.mCount == 1) {
                GyroscopeCalibrateTest.this.mGyroSens.append("Y : ");
                GyroscopeCalibrateTest.this.GyrY_Sens = GyroscopeCalibrateTest.this.Gyro_Sens;
            } else {
                GyroscopeCalibrateTest.this.mGyroSens.append("X : ");
                GyroscopeCalibrateTest.this.GyrX_Sens = GyroscopeCalibrateTest.this.Gyro_Sens;
            }
            GyroscopeCalibrateTest.this.mGyroSens.append(Float.toString(GyroscopeCalibrateTest.this.Gyro_Sens));
            GyroscopeCalibrateTest.this.mGyroSens.append("\n");
            GyroscopeCalibrateTest.this.mStart.setEnabled(true);
            GyroscopeCalibrateTest.this.mTimerText.setVisibility(8);
            GyroscopeCalibrateTest.this.GyroSensCalculationOnGoing = false;
            if (GyroscopeCalibrateTest.this.mCount == 0 && GyroscopeCalibrateTest.this.GyrZ_Sens != 0.0f && GyroscopeCalibrateTest.this.GyrY_Sens != 0.0f && GyroscopeCalibrateTest.this.GyrX_Sens != 0.0f) {
                GyroscopeCalibrateTest.this.mSave.setEnabled(true);
                GyroscopeCalibrateTest.this.mStart.setEnabled(false);
            }
        }
    };
    private TextView mTimerText;
    Timer myTimerDataUpdate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mGyroSensor = this.mSensorManager.getDefaultSensor(4);
        setContentView(2130903110);
        this.mCurrentAngle = (TextView) findViewById(2131493160);
        this.mGyroSens = (TextView) findViewById(2131493161);
        this.mTimerText = (TextView) findViewById(2131493162);
        this.mStart = (Button) findViewById(2131493163);
        this.mStart.setOnClickListener(this);
        this.mSave = (Button) findViewById(2131493164);
        this.mSave.setOnClickListener(this);
        this.mSave.setEnabled(false);
    }

    protected void GyroSensCalculation() {
        this.GyroSensCalculationOnGoing = true;
        this.mTimer.start();
    }

    protected void onResume() {
        this.mSensorManager.registerListener(this.mSensorEventListener, this.mGyroSensor, 1);
        super.onResume();
        this.myTimerDataUpdate = new Timer();
        this.myTimerDataUpdate.schedule(new TimerTask() {
            public void run() {
                GyroscopeCalibrateTest.this.TimerMethod();
            }
        }, 0, 50);
    }

    protected void TimerMethod() {
        runOnUiThread(this.doSomething);
    }

    protected void onStop() {
        this.mSensorManager.unregisterListener(this.mSensorEventListener);
        super.onStop();
        resetTest();
    }

    private void resetTest() {
        this.GyroSensCalculationOnGoing = false;
        this.Tot_Angle_rad = 0.0f;
        this.gyroTimeStamp_OLD = -1;
        this.Gyro_Sens_Initial = 1.0f;
        this.Gyro_Sens = 0.0f;
        this.mCount = 3;
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == 2131493163) {
            this.mCount--;
            this.Tot_Angle_rad = 0.0f;
            this.mPreTimer.start();
        }
        if (id == 2131493164) {
            saveTestValues();
            this.mStart.setEnabled(true);
            this.mSave.setEnabled(false);
        }
    }

    private void saveTestValues() {
        try {
            OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(new File("/persist/sensors/gyro_sensitity_cal")), "utf-8");
            localOutputStreamWriter.append("GyroSens " + this.GyrX_Sens + " " + this.GyrY_Sens + " " + this.GyrZ_Sens + "\r\n");
            localOutputStreamWriter.append("\r\n");
            if (localOutputStreamWriter != null) {
                localOutputStreamWriter.flush();
                localOutputStreamWriter.close();
            }
        } catch (Exception localException) {
            localException.printStackTrace();
            Log.e("GyroscopeCalibrateTest", "write file failed!");
        }
        Log.e("GyroscopeCalibrateTest", "write file succeed!");
        resetTest();
        Toast.makeText(this, "Save values succeed!", 1).show();
    }
}
