package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MotorAgingTest extends Activity {
    private final String TAG = "MotorAgingTest";
    private Handler mHander = new Handler() {
        public void handleMessage(Message msg) {
            MotorAgingTest.this.mResonanceFrequency.setText(MotorAgingTest.this.getString(2131297593) + MotorAgingTest.this.readFileByLines("/sys/class/timed_output/vibrator/rf_hz") + "Hz");
        }
    };
    private TextView mResonanceFrequency;
    private Button mStartVibrate;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private Vibrator mVibrate = null;
    private long mVirbateTime = 0;
    private EditText mVirbateTimeEdit;
    private long mVirbateTimeInterval = 0;
    private EditText mVirbateTimeIntervalEdit;
    private long[] sVibratePattern;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903159);
        initResource();
    }

    public void onPause() {
        this.mVibrate.cancel();
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
        if (this.mTimerTask != null) {
            this.mTimerTask.cancel();
            this.mTimerTask = null;
        }
        finish();
        super.onPause();
    }

    private void initResource() {
        this.mResonanceFrequency = (TextView) findViewById(2131493388);
        this.mVirbateTimeEdit = (EditText) findViewById(2131493384);
        this.mVirbateTimeIntervalEdit = (EditText) findViewById(2131493387);
        this.mStartVibrate = (Button) findViewById(2131493389);
        this.mVibrate = (Vibrator) getSystemService("vibrator");
        this.mStartVibrate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d("MotorAgingTest", "Start Vibrate button clicked");
                try {
                    MotorAgingTest.this.mVirbateTime = Long.parseLong(MotorAgingTest.this.mVirbateTimeEdit.getText().toString());
                    MotorAgingTest.this.mVirbateTimeInterval = Long.parseLong(MotorAgingTest.this.mVirbateTimeIntervalEdit.getText().toString());
                    MotorAgingTest.this.sVibratePattern = new long[]{MotorAgingTest.this.mVirbateTimeInterval / 2, MotorAgingTest.this.mVirbateTime, MotorAgingTest.this.mVirbateTimeInterval / 2};
                    MotorAgingTest.this.mVibrate.vibrate(MotorAgingTest.this.sVibratePattern, 0);
                    MotorAgingTest.this.mTimer = new Timer();
                    MotorAgingTest.this.mTimerTask = new TimerTask() {
                        public void run() {
                            MotorAgingTest.this.mHander.sendEmptyMessage(1);
                        }
                    };
                    MotorAgingTest.this.mTimer.schedule(MotorAgingTest.this.mTimerTask, 0, 500);
                } catch (NumberFormatException e) {
                    Toast.makeText(MotorAgingTest.this, "invalid input value", 0).show();
                }
            }
        });
    }

    private int readFileByLines(String fileName) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        int result = 0;
        String tempString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                tempString = reader.readLine();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("MotorAgingTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("MotorAgingTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("MotorAgingTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    try {
                        result = Integer.valueOf(tempString).intValue();
                    } catch (NumberFormatException e3) {
                        Log.e("MotorAgingTest", "readFileByLines NumberFormatException:" + e3.getMessage());
                    }
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("MotorAgingTest", "readFileByLines io close exception :" + e122.getMessage());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = reader;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e = e4;
            Log.e("MotorAgingTest", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            result = Integer.valueOf(tempString).intValue();
            return result;
        }
        if (!(tempString == null || "".equals(tempString))) {
            result = Integer.valueOf(tempString).intValue();
        }
        return result;
    }
}
