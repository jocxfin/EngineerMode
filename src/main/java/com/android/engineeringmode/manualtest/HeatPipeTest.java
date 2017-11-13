package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class HeatPipeTest extends Activity {
    private final String A53_CPU0_PATH = "/sys/class/thermal/thermal_zone7/temp";
    private final String A53_CPU1_PATH = "/sys/class/thermal/thermal_zone8/temp";
    private final String A53_CPU2_PATH = "/sys/class/thermal/thermal_zone9/temp";
    private final String A53_CPU3_PATH = "/sys/class/thermal/thermal_zone10/temp";
    private final String A57_CPU0_PATH = "/sys/class/thermal/thermal_zone13/temp";
    private final String A57_CPU1_PATH = "/sys/class/thermal/thermal_zone14/temp";
    private final String A57_CPU2_PATH = "/sys/class/thermal/thermal_zone15/temp";
    private final String A57_CPU3_PATH = "/sys/class/thermal/thermal_zone6/temp";
    private final String TAG = "HeatPipeTest";
    private TextView a53_cpu0;
    private TextView a53_cpu1;
    private TextView a53_cpu2;
    private TextView a53_cpu3;
    private TextView a57_cpu0;
    private TextView a57_cpu1;
    private TextView a57_cpu2;
    private TextView a57_cpu3;
    private boolean isRun = true;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                try {
                    HeatPipeTest.this.a53_cpu0.setText("A53_CPU0: " + FileUtils.readTextFile(new File("/sys/class/thermal/thermal_zone7/temp"), 0, null).trim());
                    HeatPipeTest.this.a53_cpu1.setText("A53_CPU1: " + FileUtils.readTextFile(new File("/sys/class/thermal/thermal_zone8/temp"), 0, null).trim());
                    HeatPipeTest.this.a53_cpu2.setText("A53_CPU2: " + FileUtils.readTextFile(new File("/sys/class/thermal/thermal_zone9/temp"), 0, null).trim());
                    HeatPipeTest.this.a53_cpu3.setText("A53_CPU3: " + FileUtils.readTextFile(new File("/sys/class/thermal/thermal_zone10/temp"), 0, null).trim());
                    HeatPipeTest.this.a57_cpu0.setText("A57_CPU0: " + FileUtils.readTextFile(new File("/sys/class/thermal/thermal_zone13/temp"), 0, null).trim());
                    HeatPipeTest.this.a57_cpu1.setText("A57_CPU1: " + FileUtils.readTextFile(new File("/sys/class/thermal/thermal_zone14/temp"), 0, null).trim());
                    HeatPipeTest.this.a57_cpu2.setText("A57_CPU2: " + FileUtils.readTextFile(new File("/sys/class/thermal/thermal_zone15/temp"), 0, null).trim());
                    HeatPipeTest.this.a57_cpu3.setText("A57_CPU3: " + FileUtils.readTextFile(new File("/sys/class/thermal/thermal_zone6/temp"), 0, null).trim());
                } catch (IOException e) {
                    Log.e("HeatPipeTest", "failed to read from /sys/class/thermal/thermal_zone7/temp");
                }
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903119);
        this.a53_cpu0 = (TextView) findViewById(2131493194);
        this.a53_cpu1 = (TextView) findViewById(2131493195);
        this.a53_cpu2 = (TextView) findViewById(2131493196);
        this.a53_cpu3 = (TextView) findViewById(2131493197);
        this.a57_cpu0 = (TextView) findViewById(2131493198);
        this.a57_cpu1 = (TextView) findViewById(2131493199);
        this.a57_cpu2 = (TextView) findViewById(2131493200);
        this.a57_cpu3 = (TextView) findViewById(2131493201);
        new Thread(new Runnable() {
            public void run() {
                while (HeatPipeTest.this.isRun) {
                    HeatPipeTest.this.mHandler.sendEmptyMessage(1);
                    SystemClock.sleep(1000);
                }
            }
        }).start();
        startRunning();
    }

    private void startRunning() {
        new Thread(new Runnable() {
            public void run() {
                while (HeatPipeTest.this.isRun) {
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                while (HeatPipeTest.this.isRun) {
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                while (HeatPipeTest.this.isRun) {
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                while (HeatPipeTest.this.isRun) {
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                while (HeatPipeTest.this.isRun) {
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                while (HeatPipeTest.this.isRun) {
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                while (HeatPipeTest.this.isRun) {
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                while (HeatPipeTest.this.isRun) {
                }
            }
        }).start();
    }

    protected void onDestroy() {
        this.isRun = false;
        super.onDestroy();
    }
}
