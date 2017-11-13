package com.android.engineeringmode.manualtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.android.engineeringmode.Log;
import com.android.engineeringmode.manualtest.modeltest.ModelTest3ItemActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class QcomFastChargerTest extends ModelTest3ItemActivity {
    private boolean isInModelTest = false;
    private TextView mCurrentShow;
    private Handler mHander = new Handler() {
        public void handleMessage(Message msg) {
            if (1 == msg.what) {
                int elec = QcomFastChargerTest.readFileByLines("/sys/class/power_supply/battery/current_now") / 1000;
                QcomFastChargerTest.this.mCurrentShow.setText(String.valueOf(elec) + " " + QcomFastChargerTest.this.getString(2131296722));
                if (elec < -2000) {
                    QcomFastChargerTest.this.mResultShow.setText(2131296281);
                    if (QcomFastChargerTest.this.isInModelTest) {
                        QcomFastChargerTest.this.onTestPassed();
                    }
                }
            }
        }
    };
    private TextView mResultShow;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903190);
        setTitle(2131297504);
        this.mCurrentShow = (TextView) findViewById(2131493503);
        this.mResultShow = (TextView) findViewById(2131493504);
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
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
                tempString = reader.readLine();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("QcomFastChargerTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("QcomFastChargerTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("QcomFastChargerTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    return tempString;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("QcomFastChargerTest", "readFileByLines io close exception :" + e122.getMessage());
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
            Log.e("QcomFastChargerTest", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return tempString;
        }
        return tempString;
    }

    public static int readFileByLines(String fileName) {
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
                        Log.e("QcomFastChargerTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("QcomFastChargerTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("QcomFastChargerTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    try {
                        result = Integer.valueOf(tempString).intValue();
                    } catch (NumberFormatException e3) {
                        Log.e("QcomFastChargerTest", "readFileByLines NumberFormatException:" + e3.getMessage());
                    }
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("QcomFastChargerTest", "readFileByLines io close exception :" + e122.getMessage());
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
            Log.e("QcomFastChargerTest", "readFileByLines io exception:" + e.getMessage());
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

    public void onResume() {
        if (this.mTimer == null) {
            this.mTimer = new Timer();
        }
        if (this.mTimerTask == null) {
            this.mTimerTask = new TimerTask() {
                public void run() {
                    if (!QcomFastChargerTest.this.isFileExist("/sys/class/power_supply/usb/type")) {
                        return;
                    }
                    if ((QcomFastChargerTest.this.readStringFromFile("/sys/class/power_supply/usb/type").equals("USB_HVDCP_3") || QcomFastChargerTest.this.readStringFromFile("/sys/class/power_supply/usb/type").equals("USB_HVDCP")) && QcomFastChargerTest.this.isFileExist("/sys/class/power_supply/battery/current_now")) {
                        QcomFastChargerTest.this.mHander.sendEmptyMessage(1);
                    }
                }
            };
        }
        if (!(this.mTimer == null || this.mTimerTask == null)) {
            this.mTimer.schedule(this.mTimerTask, 0, 1000);
        }
        super.onResume();
    }

    public void onPause() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
        if (this.mTimerTask != null) {
            this.mTimerTask.cancel();
            this.mTimerTask = null;
        }
        super.onPause();
    }

    public void onDestroy() {
        this.mHander.removeMessages(1);
        this.mHander = null;
        super.onDestroy();
    }
}
