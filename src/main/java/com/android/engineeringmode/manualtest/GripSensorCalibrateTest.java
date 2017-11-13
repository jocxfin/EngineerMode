package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GripSensorCalibrateTest extends Activity {
    private TextView mResultTextView;
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int line = GripSensorCalibrateTest.readFileByLines("/sys/bus/i2c/drivers/ad7146/6-002c/ad7146_grip_raw_value");
                Log.e("GripSensorCalibrateTest", "readFileByLines: line =  " + line);
                if (line == 4) {
                    GripSensorCalibrateTest.this.mResultTextView.setTextColor(-16711936);
                    GripSensorCalibrateTest.this.mResultTextView.setText("PASS!!!");
                    return;
                }
                GripSensorCalibrateTest.this.mResultTextView.setTextColor(-65536);
                GripSensorCalibrateTest.this.mResultTextView.setText("FAIL!!!");
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903104);
        this.mResultTextView = (TextView) findViewById(2131493125);
        this.mResultTextView.setText("Plase don`t move,Testing...");
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                GripSensorCalibrateTest.this.myHandler.sendMessageDelayed(msg, 2000);
            }
        }.start();
    }

    public static int readFileByLines(String fileName) {
        IOException e;
        String[] tempStrArray;
        int i;
        int capacitor;
        Throwable th;
        BufferedReader bufferedReader = null;
        int result = 0;
        String str = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                str = reader.readLine();
                Log.d("GripSensorCalibrateTest", "readFileByLines tempString:" + str);
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("GripSensorCalibrateTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("GripSensorCalibrateTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("GripSensorCalibrateTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    tempStrArray = str.split("\\s+");
                    for (i = 0; i < tempStrArray.length; i++) {
                        capacitor = Integer.parseInt(tempStrArray[i].substring(2, tempStrArray[i].length()), 16);
                        Log.e("GripSensorCalibrateTest", "readFileByLines capacitor:" + i + " " + capacitor);
                        result++;
                    }
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("GripSensorCalibrateTest", "readFileByLines io close exception :" + e122.getMessage());
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
            Log.e("GripSensorCalibrateTest", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            tempStrArray = str.split("\\s+");
            for (i = 0; i < tempStrArray.length; i++) {
                capacitor = Integer.parseInt(tempStrArray[i].substring(2, tempStrArray[i].length()), 16);
                Log.e("GripSensorCalibrateTest", "readFileByLines capacitor:" + i + " " + capacitor);
                result++;
            }
            return result;
        }
        if (!(str == null || "".equals(str))) {
            tempStrArray = str.split("\\s+");
            for (i = 0; i < tempStrArray.length; i++) {
                capacitor = Integer.parseInt(tempStrArray[i].substring(2, tempStrArray[i].length()), 16);
                Log.e("GripSensorCalibrateTest", "readFileByLines capacitor:" + i + " " + capacitor);
                if (capacitor >= 6000 && capacitor <= 14000) {
                    result++;
                }
            }
        }
        return result;
    }
}
