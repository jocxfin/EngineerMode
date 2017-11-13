package com.android.engineeringmode.manualtest.modeltest;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ModelSmallBoardCheck extends ModelTest3ItemActivity {
    private boolean isInModelTest = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                ModelSmallBoardCheck.this.startJudge();
            } else if (msg.what == 3) {
                ModelSmallBoardCheck.this.tx.setTextColor(-16711936);
                ModelSmallBoardCheck.this.tx.setText("PASS!!!");
                if (ModelSmallBoardCheck.this.isInModelTest) {
                    ModelSmallBoardCheck.this.mHandler.sendEmptyMessageDelayed(4, 1000);
                }
            } else if (msg.what == 2) {
                ModelSmallBoardCheck.this.tx.setTextColor(-65536);
                ModelSmallBoardCheck.this.tx.setText("this is not " + Build.BOARD + "\nFAIL!!!");
            } else if (msg.what == 4) {
                ModelSmallBoardCheck.this.onTestPassed();
            }
        }
    };
    private TextView tx;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903156);
        setTitle(2131297515);
        this.tx = (TextView) findViewById(2131493378);
        this.tx.setText("Testing...");
        this.mHandler.sendEmptyMessageDelayed(1, 0);
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
    }

    protected void onDestroy() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(4);
        }
        super.onDestroy();
    }

    private void startJudge() {
        int main_borad_type = 0;
        int small_borad_type = 0;
        if (new File("/sys/project_info/component_info/Aboard").exists() && new File("/sys/project_info/rf_id_v1").exists()) {
            small_borad_type = readFileByLines("/sys/project_info/component_info/Aboard", true);
            main_borad_type = readFileByLines("/sys/project_info/rf_id_v1", false);
        }
        Log.d("ModelSmallBoardCheck", "small_borad_type: " + small_borad_type + "main_borad_type: " + main_borad_type);
        if ((small_borad_type == 1 && main_borad_type == 14) || ((small_borad_type == 1 && main_borad_type == 24) || ((small_borad_type == 1 && main_borad_type == 34) || ((small_borad_type == 1 && main_borad_type == 44) || ((small_borad_type == 1 && main_borad_type == 21) || ((small_borad_type == 1 && main_borad_type == 11) || ((small_borad_type == 1 && main_borad_type == 31) || ((small_borad_type == 1 && main_borad_type == 12) || ((small_borad_type == 1 && main_borad_type == 32) || (small_borad_type == 1 && main_borad_type == 22)))))))))) {
            this.mHandler.sendEmptyMessageDelayed(3, 0);
        } else {
            this.mHandler.sendEmptyMessageDelayed(2, 0);
        }
    }

    private static int readFileByLines(String fileName, boolean on) {
        IOException e;
        String[] lineSplit;
        Throwable th;
        BufferedReader bufferedReader = null;
        int result = 0;
        String str = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                str = reader.readLine();
                Log.d("ModelSmallBoardCheck", "readFileByLines tempString:" + str);
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("ModelSmallBoardCheck", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("ModelSmallBoardCheck", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("ModelSmallBoardCheck", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    if (on) {
                        try {
                            lineSplit = str.split(":");
                            if (lineSplit.length > 1) {
                                str = lineSplit[1].trim();
                            }
                        } catch (NumberFormatException e3) {
                            Log.e("ModelSmallBoardCheck", "readFileByLines NumberFormatException:" + e3.getMessage());
                        }
                    }
                    result = Integer.valueOf(str).intValue();
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("ModelSmallBoardCheck", "readFileByLines io close exception :" + e122.getMessage());
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
            Log.e("ModelSmallBoardCheck", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (on) {
                lineSplit = str.split(":");
                if (lineSplit.length > 1) {
                    str = lineSplit[1].trim();
                }
            }
            result = Integer.valueOf(str).intValue();
            return result;
        }
        if (!(str == null || "".equals(str))) {
            if (on) {
                lineSplit = str.split(":");
                if (lineSplit.length > 1) {
                    str = lineSplit[1].trim();
                }
            }
            result = Integer.valueOf(str).intValue();
        }
        return result;
    }
}
