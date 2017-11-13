package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

public class FingerPrintAutoTest extends Activity {
    Boolean isPassed = Boolean.valueOf(false);
    TextView tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903095);
        this.tv = (TextView) findViewById(2131492997);
    }

    public void onResume() {
        super.onResume();
        if (readFileByLines("/sys/devices/f9968000.spi/spi_master/spi12/spi12.0/diag/selftest") == 1) {
            this.tv.setTextColor(-16711936);
            this.tv.setText("PASS");
            return;
        }
        this.tv.setTextColor(-65536);
        this.tv.setText("FAIL");
    }

    protected void onStop() {
        super.onStop();
        Calendar now = Calendar.getInstance();
        String time = now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13);
        String content;
        if (this.isPassed.booleanValue()) {
            content = time + "--FingerPrintAutoTest--" + "PASS";
        } else {
            content = time + "--FingerPrintAutoTest--" + "FAIL";
        }
    }

    public static int readFileByLines(String fileName) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        int result = 0;
        String str = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                str = reader.readLine();
                Log.d("FingerPrintAutoTest", "readFileByLines tempString:" + str);
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("FingerPrintAutoTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("FingerPrintAutoTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("FingerPrintAutoTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    try {
                        result = Integer.valueOf(str).intValue();
                    } catch (NumberFormatException e3) {
                        Log.e("FingerPrintAutoTest", "readFileByLines NumberFormatException:" + e3.getMessage());
                    }
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("FingerPrintAutoTest", "readFileByLines io close exception :" + e122.getMessage());
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
            Log.e("FingerPrintAutoTest", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            result = Integer.valueOf(str).intValue();
            return result;
        }
        if (!(str == null || "".equals(str))) {
            result = Integer.valueOf(str).intValue();
        }
        return result;
    }
}
