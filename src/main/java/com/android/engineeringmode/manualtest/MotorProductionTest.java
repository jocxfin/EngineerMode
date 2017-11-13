package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MotorProductionTest extends Activity {
    private final String TAG = "MotorProductionTest";
    private int cnt = 0;
    private Handler mHander = new Handler() {
        public void handleMessage(Message msg) {
            MotorProductionTest.this.mResonanceFrequency.setText(MotorProductionTest.this.getString(2131297593) + MotorProductionTest.this.readFileByLines("/sys/class/timed_output/vibrator/rf_hz") + "Hz");
        }
    };
    private TextView mResonanceFrequency;
    private TextView mResult;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private Vibrator mVibrate = null;
    private final int maxHz = 240;
    private final int maxRetry = 5;
    private final int minHz = 230;
    private long[] sVibratePattern = new long[]{500, 1000, 500};
    private long[] sVibratePatternLong = new long[]{500, 5000, 500};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903161);
        initResource();
    }

    private void stopVibrate() {
        this.mVibrate.cancel();
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
        if (this.mTimerTask != null) {
            this.mTimerTask.cancel();
            this.mTimerTask = null;
        }
    }

    public void onPause() {
        stopVibrate();
        finish();
        super.onPause();
    }

    private void initResource() {
        this.mResonanceFrequency = (TextView) findViewById(2131493388);
        this.mResult = (TextView) findViewById(2131492997);
        this.mVibrate = (Vibrator) getSystemService("vibrator");
        this.mVibrate.vibrate(this.sVibratePatternLong, 0);
        this.mTimer = new Timer();
        this.mTimerTask = new TimerTask() {
            public void run() {
                MotorProductionTest.this.mHander.sendEmptyMessage(1);
            }
        };
        this.mTimer.schedule(this.mTimerTask, 1000, 500);
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
                        Log.e("MotorProductionTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("MotorProductionTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("MotorProductionTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    try {
                        result = Integer.valueOf(tempString).intValue();
                    } catch (NumberFormatException e3) {
                        Log.e("MotorProductionTest", "readFileByLines NumberFormatException:" + e3.getMessage());
                    }
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("MotorProductionTest", "readFileByLines io close exception :" + e122.getMessage());
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
            Log.e("MotorProductionTest", "readFileByLines io exception:" + e.getMessage());
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
