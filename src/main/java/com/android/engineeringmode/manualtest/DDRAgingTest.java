package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemService;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DDRAgingTest extends Activity implements OnClickListener {
    private final String DTEST_SERVICE = "dtest";
    private final String RESULT_PATH = "/sdcard/qmesa.log";
    private Button mQuitButton;
    private TextView mResult;
    private Button mStartButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903080);
        this.mResult = (TextView) findViewById(2131492997);
        this.mStartButton = (Button) findViewById(2131493019);
        this.mQuitButton = (Button) findViewById(2131493018);
        this.mStartButton.setOnClickListener(this);
        this.mQuitButton.setOnClickListener(this);
        this.mQuitButton.setEnabled(false);
        getWindow().addFlags(128);
        if (new File("/sdcard/qmesa.log").exists()) {
            String result = readStringFromFile("/sdcard/qmesa.log");
            if (result.equals("PASS")) {
                this.mResult.setTextColor(-16711936);
            } else {
                this.mResult.setTextColor(-65536);
            }
            this.mResult.setText(result);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131493018:
                SystemService.stop("dtest");
                this.mStartButton.setEnabled(true);
                this.mQuitButton.setEnabled(false);
                String result = readStringFromFile("/sdcard/qmesa.log");
                if (result.equals("PASS")) {
                    this.mResult.setTextColor(-16711936);
                } else {
                    this.mResult.setTextColor(-65536);
                }
                this.mResult.setText(result);
                return;
            case 2131493019:
                SystemService.start("dtest");
                this.mStartButton.setEnabled(false);
                this.mQuitButton.setEnabled(true);
                this.mResult.setTextColor(-1);
                this.mResult.setText("Test is runing!");
                return;
            default:
                return;
        }
    }

    private String readStringFromFile(String fileName) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        String tempString = "PASS";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            while (true) {
                try {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    } else if ((line.contains("ERROR") || line.contains("Fault") || line.contains("failed")) && !line.contains("errorCheck")) {
                        if (tempString.equals("PASS")) {
                            tempString = "failed";
                        }
                        tempString = tempString + "\n" + line;
                    }
                } catch (IOException e2) {
                    e = e2;
                    bufferedReader = reader;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = reader;
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    Log.e("DDRAgingTest", "readFileByLines io close exception :" + e1.getMessage());
                }
            }
            bufferedReader = reader;
        } catch (IOException e3) {
            e = e3;
            try {
                Log.e("DDRAgingTest", "readFileByLines io exception:" + e.getMessage());
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e12) {
                        Log.e("DDRAgingTest", "readFileByLines io close exception :" + e12.getMessage());
                    }
                }
                return tempString;
            } catch (Throwable th3) {
                th = th3;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e122) {
                        Log.e("DDRAgingTest", "readFileByLines io close exception :" + e122.getMessage());
                    }
                }
                throw th;
            }
        }
        return tempString;
    }
}
