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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MotorEffectTest extends Activity {
    private final String TAG = "MotorEffectTest";
    private Handler mHander = new Handler() {
        public void handleMessage(Message msg) {
            MotorEffectTest.this.mVibrateVol.setText(MotorEffectTest.this.getString(2131297596) + MotorEffectTest.this.readFileByLines("/sys/class/timed_output/vibrator/vmax"));
        }
    };
    private Button mStartMiddleVibrate;
    private Button mStartStrongVibrate;
    private Button mStartVibrate;
    private Button mStartWeakVibrate;
    private Button mStopVibrate;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private Vibrator mVibrate = null;
    private TextView mVibrateVol;
    private EditText mVirbateVolEdit;
    private long mVirbateVoltage = 0;
    private long mWfRep = 0;
    private EditText mWfRepEdit;
    private long mWfS0 = 0;
    private EditText mWfS0Edit;
    private long mWfS1 = 0;
    private EditText mWfS1Edit;
    private long mWfS2 = 0;
    private EditText mWfS2Edit;
    private long mWfS3 = 0;
    private EditText mWfS3Edit;
    private long mWfS4 = 0;
    private EditText mWfS4Edit;
    private long mWfS5 = 0;
    private EditText mWfS5Edit;
    private long mWfS6 = 0;
    private EditText mWfS6Edit;
    private long mWfS7 = 0;
    private EditText mWfS7Edit;
    private long mWfSRep = 0;
    private EditText mWfSRepEdit;
    private long mWfUpdate = 0;
    private EditText mWfUpdateEdit;
    private long[] sVibratePattern = new long[]{500, 1000, 500};
    private long[] sVibratePatternMiddle = new long[]{-2, 500, 1000, 500};
    private long[] sVibratePatternStrong = new long[]{-3, 500, 1000, 500};
    private long[] sVibratePatternWeak = new long[]{-1, 500, 1000, 500};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903160);
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
        this.mVibrateVol = (TextView) findViewById(2131493425);
        this.mVirbateVolEdit = (EditText) findViewById(2131493424);
        this.mWfS0Edit = (EditText) findViewById(2131493392);
        this.mWfS1Edit = (EditText) findViewById(2131493395);
        this.mWfS2Edit = (EditText) findViewById(2131493398);
        this.mWfS3Edit = (EditText) findViewById(2131493401);
        this.mWfS4Edit = (EditText) findViewById(2131493404);
        this.mWfS5Edit = (EditText) findViewById(2131493407);
        this.mWfS6Edit = (EditText) findViewById(2131493410);
        this.mWfS7Edit = (EditText) findViewById(2131493413);
        this.mWfUpdateEdit = (EditText) findViewById(2131493416);
        this.mWfRepEdit = (EditText) findViewById(2131493419);
        this.mWfSRepEdit = (EditText) findViewById(2131493422);
        this.mStartVibrate = (Button) findViewById(2131493389);
        this.mStopVibrate = (Button) findViewById(2131493429);
        this.mStartWeakVibrate = (Button) findViewById(2131493426);
        this.mStartMiddleVibrate = (Button) findViewById(2131493427);
        this.mStartStrongVibrate = (Button) findViewById(2131493428);
        this.mStopVibrate.setEnabled(false);
        this.mVibrate = (Vibrator) getSystemService("vibrator");
        this.mStartVibrate.setOnClickListener(new OnClickListener() {
            public void onClick(android.view.View r1) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.android.engineeringmode.manualtest.MotorEffectTest.2.onClick(android.view.View):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
	at jadx.core.dex.nodes.MethodNode.initTryCatches(MethodNode.java:317)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:105)
	... 6 more
*/
                /*
                r0 = this;
                r7 = 0;
                r0 = "MotorEffectTest";
                r1 = "Start Vibrate button clicked";
                android.util.Log.d(r0, r1);
                r0 = r0.mStopVibrate;
                r1 = 1;
                r0 = r0.mStartVibrate;
                r0 = r0.mStartWeakVibrate;
                r0 = r0.mStartMiddleVibrate;
                r0 = r0.mStartStrongVibrate;
                r1 = r1.mVirbateVolEdit;
                r1 = r1.toString();
                r2 = java.lang.Long.parseLong(r1);
                r0.mVirbateVoltage = r2;
                r1 = r1.mWfS0Edit;
                r1 = r1.toString();
                r2 = java.lang.Long.parseLong(r1);
                r0.mWfS0 = r2;
                r1 = r1.mWfS1Edit;
                r1 = r1.toString();
                r2 = java.lang.Long.parseLong(r1);
                r0.mWfS1 = r2;
                r1 = r1.mWfS2Edit;
                r1 = r1.toString();
                r2 = java.lang.Long.parseLong(r1);
                r0.mWfS2 = r2;
                r1 = r1.mWfS3Edit;
                r1 = r1.toString();
                r2 = java.lang.Long.parseLong(r1);
                r0.mWfS3 = r2;
                r1 = r1.mWfS4Edit;
                r1 = r1.toString();
                r2 = java.lang.Long.parseLong(r1);
                r0.mWfS4 = r2;
                r1 = r1.mWfS5Edit;
                r1 = r1.toString();
                r2 = java.lang.Long.parseLong(r1);
                r0.mWfS5 = r2;
                r1 = r1.mWfS6Edit;
                r1 = r1.toString();
                r2 = java.lang.Long.parseLong(r1);
                r0.mWfS6 = r2;
                r1 = r1.mWfS7Edit;
                r1 = r1.toString();
                r2 = java.lang.Long.parseLong(r1);
                r0.mWfS7 = r2;
                r1 = r1.mWfUpdateEdit;
                r1 = r1.toString();
                r2 = java.lang.Long.parseLong(r1);
                r0.mWfUpdate = r2;
                r1 = r1.mWfRepEdit;
                r1 = r1.toString();
                r2 = java.lang.Long.parseLong(r1);
                r0.mWfRep = r2;
                r1 = r1.mWfSRepEdit;
                r1 = r1.toString();
                r2 = java.lang.Long.parseLong(r1);
                r0.mWfSRep = r2;
                r1 = "/sys/class/timed_output/vibrator/vmax";
                r2 = new java.lang.StringBuilder;
                r2.<init>();
                r4 = r3.mVirbateVoltage;
                r3 = "";
                r0.WriteNodeValue(r1, r2);
                r1 = "/sys/class/timed_output/vibrator/wf_s0";
                r2 = new java.lang.StringBuilder;
                r2.<init>();
                r4 = r3.mWfS0;
                r3 = "";
                r0.WriteNodeValue(r1, r2);
                r1 = "/sys/class/timed_output/vibrator/wf_s1";
                r2 = new java.lang.StringBuilder;
                r2.<init>();
                r4 = r3.mWfS1;
                r3 = "";
                r0.WriteNodeValue(r1, r2);
                r1 = "/sys/class/timed_output/vibrator/wf_s2";
                r2 = new java.lang.StringBuilder;
                r2.<init>();
                r4 = r3.mWfS2;
                r3 = "";
                r0.WriteNodeValue(r1, r2);
                r1 = "/sys/class/timed_output/vibrator/wf_s3";
                r2 = new java.lang.StringBuilder;
                r2.<init>();
                r4 = r3.mWfS3;
                r3 = "";
                r0.WriteNodeValue(r1, r2);
                r1 = "/sys/class/timed_output/vibrator/wf_s4";
                r2 = new java.lang.StringBuilder;
                r2.<init>();
                r4 = r3.mWfS4;
                r3 = "";
                r0.WriteNodeValue(r1, r2);
                r1 = "/sys/class/timed_output/vibrator/wf_s5";
                r2 = new java.lang.StringBuilder;
                r2.<init>();
                r4 = r3.mWfS5;
                r3 = "";
                r0.WriteNodeValue(r1, r2);
                r1 = "/sys/class/timed_output/vibrator/wf_s6";
                r2 = new java.lang.StringBuilder;
                r2.<init>();
                r4 = r3.mWfS6;
                r3 = "";
                r0.WriteNodeValue(r1, r2);
                r1 = "/sys/class/timed_output/vibrator/wf_s7";
                r2 = new java.lang.StringBuilder;
                r2.<init>();
                r4 = r3.mWfS7;
                r3 = "";
                r0.WriteNodeValue(r1, r2);
                r1 = "/sys/class/timed_output/vibrator/wf_update";
                r2 = new java.lang.StringBuilder;
                r2.<init>();
                r4 = r3.mWfUpdate;
                r3 = "";
                r0.WriteNodeValue(r1, r2);
                r1 = "/sys/class/timed_output/vibrator/wf_rep";
                r2 = new java.lang.StringBuilder;
                r2.<init>();
                r4 = r3.mWfRep;
                r3 = "";
                r0.WriteNodeValue(r1, r2);
                r1 = "/sys/class/timed_output/vibrator/wf_s_rep";
                r2 = new java.lang.StringBuilder;
                r2.<init>();
                r4 = r3.mWfSRep;
                r3 = "";
                r0.WriteNodeValue(r1, r2);
                r0 = r0.mVibrate;
                r1 = r1.sVibratePattern;
                r1 = new java.util.Timer;
                r1.<init>();
                r0.mTimer = r1;
                r1 = new com.android.engineeringmode.manualtest.MotorEffectTest$2$1;
                r1.<init>();
                r0.mTimerTask = r1;
                r0 = r0.mTimer;
                r1 = r1.mTimerTask;
                r2 = 0;
                r4 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
            L_0x0272:
                r6 = move-exception;
                r1 = "invalid input value";
                r0 = android.widget.Toast.makeText(r0, r1, r7);
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.engineeringmode.manualtest.MotorEffectTest.2.onClick(android.view.View):void");
            }
        });
        this.mStopVibrate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d("MotorEffectTest", "Stop Vibrate button clicked");
                MotorEffectTest.this.mStopVibrate.setEnabled(false);
                MotorEffectTest.this.mStartVibrate.setEnabled(true);
                MotorEffectTest.this.mStartWeakVibrate.setEnabled(true);
                MotorEffectTest.this.mStartMiddleVibrate.setEnabled(true);
                MotorEffectTest.this.mStartStrongVibrate.setEnabled(true);
                MotorEffectTest.this.mVibrate.cancel();
                if (MotorEffectTest.this.mTimer != null) {
                    MotorEffectTest.this.mTimer.cancel();
                    MotorEffectTest.this.mTimer = null;
                }
                if (MotorEffectTest.this.mTimerTask != null) {
                    MotorEffectTest.this.mTimerTask.cancel();
                    MotorEffectTest.this.mTimerTask = null;
                }
            }
        });
        this.mStartWeakVibrate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d("MotorEffectTest", "Start Weak Vibrate button clicked");
                MotorEffectTest.this.mStopVibrate.setEnabled(true);
                MotorEffectTest.this.mStartVibrate.setEnabled(false);
                MotorEffectTest.this.mStartWeakVibrate.setEnabled(false);
                MotorEffectTest.this.mStartMiddleVibrate.setEnabled(false);
                MotorEffectTest.this.mStartStrongVibrate.setEnabled(false);
                MotorEffectTest.this.mVibrate.vibrate(MotorEffectTest.this.sVibratePatternWeak, 0);
                MotorEffectTest.this.mTimer = new Timer();
                MotorEffectTest.this.mTimerTask = new TimerTask() {
                    public void run() {
                        MotorEffectTest.this.mHander.sendEmptyMessage(1);
                    }
                };
                MotorEffectTest.this.mTimer.schedule(MotorEffectTest.this.mTimerTask, 0, 500);
            }
        });
        this.mStartMiddleVibrate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d("MotorEffectTest", "Start Middle Vibrate button clicked");
                MotorEffectTest.this.mStopVibrate.setEnabled(true);
                MotorEffectTest.this.mStartVibrate.setEnabled(false);
                MotorEffectTest.this.mStartWeakVibrate.setEnabled(false);
                MotorEffectTest.this.mStartMiddleVibrate.setEnabled(false);
                MotorEffectTest.this.mStartStrongVibrate.setEnabled(false);
                MotorEffectTest.this.mVibrate.vibrate(MotorEffectTest.this.sVibratePatternMiddle, 0);
                MotorEffectTest.this.mTimer = new Timer();
                MotorEffectTest.this.mTimerTask = new TimerTask() {
                    public void run() {
                        MotorEffectTest.this.mHander.sendEmptyMessage(1);
                    }
                };
                MotorEffectTest.this.mTimer.schedule(MotorEffectTest.this.mTimerTask, 0, 500);
            }
        });
        this.mStartStrongVibrate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d("MotorEffectTest", "Start Strong Vibrate button clicked");
                MotorEffectTest.this.mStopVibrate.setEnabled(true);
                MotorEffectTest.this.mStartVibrate.setEnabled(false);
                MotorEffectTest.this.mStartWeakVibrate.setEnabled(false);
                MotorEffectTest.this.mStartMiddleVibrate.setEnabled(false);
                MotorEffectTest.this.mStartStrongVibrate.setEnabled(false);
                MotorEffectTest.this.mVibrate.vibrate(MotorEffectTest.this.sVibratePatternStrong, 0);
                MotorEffectTest.this.mTimer = new Timer();
                MotorEffectTest.this.mTimerTask = new TimerTask() {
                    public void run() {
                        MotorEffectTest.this.mHander.sendEmptyMessage(1);
                    }
                };
                MotorEffectTest.this.mTimer.schedule(MotorEffectTest.this.mTimerTask, 0, 500);
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
                        Log.e("MotorEffectTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("MotorEffectTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("MotorEffectTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    try {
                        result = Integer.valueOf(tempString).intValue();
                    } catch (NumberFormatException e3) {
                        Log.e("MotorEffectTest", "readFileByLines NumberFormatException:" + e3.getMessage());
                    }
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("MotorEffectTest", "readFileByLines io close exception :" + e122.getMessage());
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
            Log.e("MotorEffectTest", "readFileByLines io exception:" + e.getMessage());
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

    private void WriteNodeValue(String nodePath, String value) {
        try {
            FileWriter writer = new FileWriter(new File(nodePath));
            writer.write(String.valueOf(value));
            writer.close();
        } catch (IOException e) {
            Log.e("MotorEffectTest", "writeNodeValue IO exception:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
