package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class TouchKeyTest extends Activity implements OnClickListener {
    private Handler mHander = new Handler() {
        public void handleMessage(Message msg) {
            if (1 == msg.what) {
                TouchKeyTest.this.mSignalStrength.setTextSize(25.0f);
                TouchKeyTest.this.mSignalStrength.setText(TouchKeyTest.this.readStringFromFile("/proc/s1302/strength"));
            }
        }
    };
    private Button mQuitButton;
    private TextView mSignalStrength;
    private Button mStartButton;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903211);
        this.mSignalStrength = (TextView) findViewById(2131493566);
        this.mStartButton = (Button) findViewById(2131493019);
        this.mQuitButton = (Button) findViewById(2131493018);
        this.mStartButton.setOnClickListener(this);
        this.mQuitButton.setOnClickListener(this);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131493018:
                if (writeFile("/proc/s1302/reset", "3")) {
                    finish();
                    return;
                } else {
                    Log.e("SmartAntennaSwitchTest", "Fail to write 3 to file /proc/s1302/reset");
                    return;
                }
            case 2131493019:
                if (writeFile("/proc/s1302/reset", "4")) {
                    if (this.mTimer == null) {
                        this.mTimer = new Timer();
                    }
                    if (this.mTimerTask == null) {
                        this.mTimerTask = new TimerTask() {
                            public void run() {
                                if (TouchKeyTest.this.isFileExist("/proc/s1302/strength")) {
                                    TouchKeyTest.this.mHander.sendEmptyMessage(1);
                                }
                            }
                        };
                    }
                    if (!(this.mTimer == null || this.mTimerTask == null)) {
                        this.mTimer.schedule(this.mTimerTask, 0, 500);
                    }
                    this.mStartButton.setEnabled(false);
                    return;
                }
                Log.e("SmartAntennaSwitchTest", "Fail to write 4 to file /proc/s1302/reset");
                return;
            default:
                return;
        }
    }

    private boolean isFileExist(String fileName) {
        return new File(fileName).exists();
    }

    private String readStringFromFile(String fileName) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        String tempString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                tempString = reader.readLine() + "; " + reader.readLine();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("SmartAntennaSwitchTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("SmartAntennaSwitchTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("SmartAntennaSwitchTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    return tempString;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("SmartAntennaSwitchTest", "readFileByLines io close exception :" + e122.getMessage());
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
        } catch (IOException e3) {
            e = e3;
            Log.e("SmartAntennaSwitchTest", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return tempString;
        }
        return tempString;
    }

    private boolean writeFile(String fileAbsolutePath, String content) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileAbsolutePath);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e("SmartAntennaSwitchTest", "CommandWriteImportantLog writeFile function exception:" + e.getMessage());
            return false;
        } catch (IOException e2) {
            Log.e("SmartAntennaSwitchTest", "CommandWriteImportantLog writeFile function exception:" + e2.getMessage());
            return false;
        }
    }

    protected void onPause() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
        if (this.mTimerTask != null) {
            this.mTimerTask.cancel();
            this.mTimerTask = null;
        }
        this.mHander.removeMessages(1);
        this.mHander = null;
        if (!writeFile("/proc/s1302/reset", "3")) {
            Log.e("SmartAntennaSwitchTest", "Fail to write 3 to file /proc/s1302/reset");
        }
        finish();
        super.onPause();
    }
}
