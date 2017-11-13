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

public class RotationMotor extends Activity implements OnClickListener {
    private static final int[][] ROTATION_MODE10_DIR_PARAMETER = new int[][]{new int[]{1, 500, 0, 1}};
    private static final int[][] ROTATION_MODE1_DELAY_PARAMETER = new int[][]{new int[]{0, 500, 1, 1}, new int[]{0, 500, 0, 0}, new int[]{0, 500, 0, 1}};
    private static final int[][] ROTATION_MODE2_DELAY_PARAMETER = new int[][]{new int[]{1, 1200, 1, 1}, new int[]{1, 1000, 0, 0}, new int[]{1, 1200, 0, 1}};
    private static final int[][] ROTATION_MODE3_DELAY_PARAMETER = new int[][]{new int[]{6, 1, 500, 100, 0, 1}, new int[]{6, 1, 500, 0, 0, 0}};
    private static final int[][] ROTATION_MODE4_DELAY_PARAMETER = new int[][]{new int[]{6, 1, 500, 100, 1, 1}, new int[]{6, 1, 500, 0, 1, 0}};
    private static final int[][] ROTATION_MODE5_DELAY_PARAMETER = new int[][]{new int[]{6, 0, 500, 215, 1, 1}, new int[]{6, 0, 500, 0, 0, 0}, new int[]{6, 0, 500, 215, 0, 1}, new int[]{6, 0, 500, 0, 0, 0}};
    private static final int[][] ROTATION_MODE6_DELAY_PARAMETER = new int[][]{new int[]{6, 1, 1200, 215, 1, 1}, new int[]{6, 1, 1000, 0, 0, 0}, new int[]{6, 1, 1200, 215, 0, 1}, new int[]{6, 1, 1000, 0, 0, 0}};
    private static final int[][] ROTATION_MODE7_DELAY_PARAMETER = new int[][]{new int[]{6, 4, 10000, 215, 1, 1}, new int[]{6, 4, 8500, 0, 0, 0}, new int[]{6, 4, 10000, 215, 0, 1}, new int[]{6, 4, 8500, 0, 0, 0}};
    private static final int[][] ROTATION_MODE8_DELAY_PARAMETER = new int[][]{new int[]{6, 5, 20000, 215, 1, 1}, new int[]{6, 5, 17000, 0, 0, 0}, new int[]{6, 5, 20000, 215, 0, 1}, new int[]{6, 5, 17000, 0, 0, 0}};
    private static final int[][] ROTATION_MODE9_DIR_PARAMETER = new int[][]{new int[]{1, 500, 1, 1}};
    private static int mCountMode3 = 0;
    private static int mCountMode4 = 0;
    private static int mCountMode5 = 0;
    private static int mCountMode6 = 0;
    private static int mCountMode7 = 0;
    private static int mCountMode8 = 0;
    private static RotationHandler mRotationUpdateUIHandler;
    private MotorTestThread mThread;
    private Button mbtnMode1;
    private Button mbtnMode10;
    private Button mbtnMode2;
    private Button mbtnMode3;
    private Button mbtnMode4;
    private Button mbtnMode5;
    private Button mbtnMode6;
    private Button mbtnMode7;
    private Button mbtnMode8;
    private Button mbtnMode9;
    private Button mbtnStop;
    private boolean motor_started = false;
    private TextView textMode3;
    private TextView textMode4;
    private TextView textMode5;
    private TextView textMode6;
    private TextView textMode7;
    private TextView textMode8;
    private TextView tvRotationStatus;

    private static class MotorTestThread extends Thread {
        private int enable_rotation;
        private int[][] mRotaitonParameterArray;
        private int mRotationMode;
        private int rotation_direction;

        private MotorTestThread() {
            this.mRotationMode = 0;
            this.rotation_direction = 0;
            this.enable_rotation = 1;
        }

        public void setRotationParameter(int rotation_mode, int[][] parameter_array) {
            this.mRotationMode = rotation_mode;
            this.mRotaitonParameterArray = parameter_array;
        }

        public synchronized void waitNewTest() throws InterruptedException {
            Log.i("Rotation", "wait ~~ wait");
            wait();
        }

        public synchronized void notifyNewTest() {
            Log.i("Rotation", "notify ~~ notify");
            notify();
        }

        public void run() {
            Object obj = new Object();
            try {
                Log.i("Rotation", "round ~~ round");
                waitNewTest();
                int i;
                if (this.mRotationMode == 1 || this.mRotationMode == 2) {
                    for (i = 0; i < 3; i++) {
                        RotationUtils.motor_rotation_set_para(6, this.mRotaitonParameterArray[i][0], 215, this.mRotaitonParameterArray[i][2], this.mRotaitonParameterArray[i][3]);
                        updateRotationStatus(this.mRotaitonParameterArray[i][2], this.mRotaitonParameterArray[i][3]);
                        Thread.sleep((long) this.mRotaitonParameterArray[i][1]);
                    }
                } else if (this.mRotationMode == 9 || this.mRotationMode == 10) {
                    for (i = 0; i < 1; i++) {
                        RotationUtils.motor_rotation_test(this.mRotaitonParameterArray[i][0], 60, this.mRotaitonParameterArray[i][2], this.mRotaitonParameterArray[i][3]);
                        updateRotationStatus(this.mRotaitonParameterArray[i][2], this.mRotaitonParameterArray[i][3]);
                        Thread.sleep((long) this.mRotaitonParameterArray[i][1]);
                    }
                } else {
                    while (!isInterrupted()) {
                        int i2 = 0;
                        if (this.mRotationMode == 3 || this.mRotationMode == 4) {
                            i2 = 2;
                        }
                        if (!(this.mRotationMode == 5 || this.mRotationMode == 6 || this.mRotationMode == 7)) {
                            if (this.mRotationMode == 8) {
                            }
                            for (i = 0; i < i2; i++) {
                                Log.i("Rotation", "loop ~~ loop");
                                RotationUtils.motor_rotation_set_para(this.mRotaitonParameterArray[i][0], this.mRotaitonParameterArray[i][1], this.mRotaitonParameterArray[i][3], this.mRotaitonParameterArray[i][4], this.mRotaitonParameterArray[i][5]);
                                updateRotationStatus(this.mRotaitonParameterArray[i][4], this.mRotaitonParameterArray[i][5]);
                                Thread.sleep((long) this.mRotaitonParameterArray[i][2]);
                            }
                            if (this.mRotationMode == 3) {
                                RotationMotor.mCountMode3 = RotationMotor.mCountMode3 + 1;
                            } else if (this.mRotationMode == 4) {
                                RotationMotor.mCountMode4 = RotationMotor.mCountMode4 + 1;
                            } else if (this.mRotationMode == 5) {
                                RotationMotor.mCountMode5 = RotationMotor.mCountMode5 + 1;
                            } else if (this.mRotationMode == 6) {
                                RotationMotor.mCountMode6 = RotationMotor.mCountMode6 + 1;
                            } else if (this.mRotationMode == 7) {
                                RotationMotor.mCountMode7 = RotationMotor.mCountMode7 + 1;
                            } else if (this.mRotationMode == 8) {
                                RotationMotor.mCountMode8 = RotationMotor.mCountMode8 + 1;
                            }
                            RotationMotor.mRotationUpdateUIHandler.sendMessage(RotationMotor.mRotationUpdateUIHandler.obtainMessage(1, 0, 0));
                        }
                        i2 = 4;
                        for (i = 0; i < i2; i++) {
                            Log.i("Rotation", "loop ~~ loop");
                            RotationUtils.motor_rotation_set_para(this.mRotaitonParameterArray[i][0], this.mRotaitonParameterArray[i][1], this.mRotaitonParameterArray[i][3], this.mRotaitonParameterArray[i][4], this.mRotaitonParameterArray[i][5]);
                            updateRotationStatus(this.mRotaitonParameterArray[i][4], this.mRotaitonParameterArray[i][5]);
                            Thread.sleep((long) this.mRotaitonParameterArray[i][2]);
                        }
                        if (this.mRotationMode == 3) {
                            RotationMotor.mCountMode3 = RotationMotor.mCountMode3 + 1;
                        } else if (this.mRotationMode == 4) {
                            RotationMotor.mCountMode4 = RotationMotor.mCountMode4 + 1;
                        } else if (this.mRotationMode == 5) {
                            RotationMotor.mCountMode5 = RotationMotor.mCountMode5 + 1;
                        } else if (this.mRotationMode == 6) {
                            RotationMotor.mCountMode6 = RotationMotor.mCountMode6 + 1;
                        } else if (this.mRotationMode == 7) {
                            RotationMotor.mCountMode7 = RotationMotor.mCountMode7 + 1;
                        } else if (this.mRotationMode == 8) {
                            RotationMotor.mCountMode8 = RotationMotor.mCountMode8 + 1;
                        }
                        RotationMotor.mRotationUpdateUIHandler.sendMessage(RotationMotor.mRotationUpdateUIHandler.obtainMessage(1, 0, 0));
                    }
                }
                RotationMotor.mRotationUpdateUIHandler.sendMessage(RotationMotor.mRotationUpdateUIHandler.obtainMessage(0, 2131297414, 0));
            } catch (InterruptedException e) {
                RotationUtils.motor_rotation_test(1, 215, 1, 0);
                RotationMotor.mRotationUpdateUIHandler.sendMessage(RotationMotor.mRotationUpdateUIHandler.obtainMessage(0, 2131297414, 0));
            }
        }

        private void updateRotationStatus(int direction, int enable) {
            if (enable == 0) {
                RotationMotor.mRotationUpdateUIHandler.sendMessage(RotationMotor.mRotationUpdateUIHandler.obtainMessage(0, 2131297414, 0));
            } else if (direction == 0 && enable == 1) {
                RotationMotor.mRotationUpdateUIHandler.sendMessage(RotationMotor.mRotationUpdateUIHandler.obtainMessage(0, 2131297413, 0));
            } else if (direction == 1 && enable == 1) {
                RotationMotor.mRotationUpdateUIHandler.sendMessage(RotationMotor.mRotationUpdateUIHandler.obtainMessage(0, 2131297412, 0));
            }
        }
    }

    private class RotationHandler extends Handler {
        private RotationHandler() {
        }

        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Log.i("Rotation", "got message, refresh status");
                RotationMotor.this.tvRotationStatus.setText(msg.arg1);
            } else if (msg.what == 1) {
                RotationMotor.this.updateSummary();
            } else if (msg.what == 100 && RotationMotor.this.mThread != null) {
                RotationMotor.this.mThread.notifyNewTest();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903195);
        getWindow().addFlags(128);
        getWindow().addFlags(4194304);
        this.mbtnMode1 = (Button) findViewById(2131493513);
        this.mbtnMode2 = (Button) findViewById(2131493514);
        this.mbtnMode3 = (Button) findViewById(2131493515);
        this.mbtnMode4 = (Button) findViewById(2131493517);
        this.mbtnMode5 = (Button) findViewById(2131493519);
        this.mbtnMode6 = (Button) findViewById(2131493521);
        this.mbtnMode7 = (Button) findViewById(2131493523);
        this.mbtnMode8 = (Button) findViewById(2131493525);
        this.mbtnMode9 = (Button) findViewById(2131493527);
        this.mbtnMode10 = (Button) findViewById(2131493528);
        this.mbtnStop = (Button) findViewById(2131493529);
        this.textMode3 = (TextView) findViewById(2131493516);
        this.textMode4 = (TextView) findViewById(2131493518);
        this.textMode5 = (TextView) findViewById(2131493520);
        this.textMode6 = (TextView) findViewById(2131493522);
        this.textMode7 = (TextView) findViewById(2131493524);
        this.textMode8 = (TextView) findViewById(2131493526);
        this.tvRotationStatus = (TextView) findViewById(2131493530);
        this.tvRotationStatus.setText(2131297414);
        this.mbtnMode1.setOnClickListener(this);
        this.mbtnMode2.setOnClickListener(this);
        this.mbtnMode3.setOnClickListener(this);
        this.mbtnMode4.setOnClickListener(this);
        this.mbtnMode5.setOnClickListener(this);
        this.mbtnMode6.setOnClickListener(this);
        this.mbtnMode7.setOnClickListener(this);
        this.mbtnMode8.setOnClickListener(this);
        this.mbtnMode9.setOnClickListener(this);
        this.mbtnMode10.setOnClickListener(this);
        this.mbtnStop.setOnClickListener(this);
        mRotationUpdateUIHandler = new RotationHandler();
    }

    protected void onResume() {
        super.onResume();
        mCountMode3 = 0;
        mCountMode4 = 0;
        mCountMode5 = 0;
        mCountMode6 = 0;
        mCountMode7 = 0;
        mCountMode8 = 0;
        updateSummary();
    }

    protected void onPause() {
        stopRotationTest();
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    private void prepareRotationTest(int mode, int[][] parameter) {
        if (!this.motor_started) {
            RotationUtils.open_motor();
        }
        stopRotationTest();
        if (this.mThread == null) {
            this.mThread = new MotorTestThread();
            this.mThread.setRotationParameter(mode, parameter);
            this.mThread.start();
            mRotationUpdateUIHandler.sendEmptyMessageDelayed(100, 200);
        }
    }

    private void stopRotationTest() {
        if (this.mThread != null) {
            this.mThread.interrupt();
            this.mThread = null;
        }
        if (this.motor_started) {
            RotationUtils.close_motor();
        }
    }

    public void onClick(View view) {
        int i;
        int i2 = -16776961;
        if (this.mbtnMode1 == view) {
            prepareRotationTest(1, ROTATION_MODE1_DELAY_PARAMETER);
        } else if (this.mbtnMode2 == view) {
            prepareRotationTest(2, ROTATION_MODE2_DELAY_PARAMETER);
        } else if (this.mbtnMode3 == view) {
            prepareRotationTest(3, ROTATION_MODE3_DELAY_PARAMETER);
        } else if (this.mbtnMode4 == view) {
            prepareRotationTest(4, ROTATION_MODE4_DELAY_PARAMETER);
        } else if (this.mbtnMode5 == view) {
            prepareRotationTest(5, ROTATION_MODE5_DELAY_PARAMETER);
        } else if (this.mbtnMode6 == view) {
            prepareRotationTest(6, ROTATION_MODE6_DELAY_PARAMETER);
        } else if (this.mbtnMode7 == view) {
            prepareRotationTest(7, ROTATION_MODE7_DELAY_PARAMETER);
        } else if (this.mbtnMode8 == view) {
            prepareRotationTest(8, ROTATION_MODE8_DELAY_PARAMETER);
        } else if (this.mbtnMode9 == view) {
            prepareRotationTest(9, ROTATION_MODE9_DIR_PARAMETER);
        } else if (this.mbtnMode10 == view) {
            prepareRotationTest(10, ROTATION_MODE10_DIR_PARAMETER);
        } else if (this.mbtnStop == view) {
            stopRotationTest();
        }
        Button button = this.mbtnMode1;
        if (this.mbtnMode1 == view) {
            i = -16776961;
        } else {
            i = -1;
        }
        button.setTextColor(i);
        button = this.mbtnMode2;
        if (this.mbtnMode2 == view) {
            i = -16776961;
        } else {
            i = -1;
        }
        button.setTextColor(i);
        button = this.mbtnMode3;
        if (this.mbtnMode3 == view) {
            i = -16776961;
        } else {
            i = -1;
        }
        button.setTextColor(i);
        button = this.mbtnMode4;
        if (this.mbtnMode4 == view) {
            i = -16776961;
        } else {
            i = -1;
        }
        button.setTextColor(i);
        button = this.mbtnMode5;
        if (this.mbtnMode5 == view) {
            i = -16776961;
        } else {
            i = -1;
        }
        button.setTextColor(i);
        button = this.mbtnMode6;
        if (this.mbtnMode6 == view) {
            i = -16776961;
        } else {
            i = -1;
        }
        button.setTextColor(i);
        button = this.mbtnMode7;
        if (this.mbtnMode7 == view) {
            i = -16776961;
        } else {
            i = -1;
        }
        button.setTextColor(i);
        button = this.mbtnMode8;
        if (this.mbtnMode8 == view) {
            i = -16776961;
        } else {
            i = -1;
        }
        button.setTextColor(i);
        button = this.mbtnMode9;
        if (this.mbtnMode9 == view) {
            i = -16776961;
        } else {
            i = -1;
        }
        button.setTextColor(i);
        button = this.mbtnMode10;
        if (this.mbtnMode10 == view) {
            i = -16776961;
        } else {
            i = -1;
        }
        button.setTextColor(i);
        Button button2 = this.mbtnStop;
        if (this.mbtnStop != view) {
            i2 = -1;
        }
        button2.setTextColor(i2);
    }

    private void updateSummary() {
        this.textMode3.setText(String.format(getString(2131297391), new Object[]{Integer.valueOf(mCountMode3)}));
        this.textMode4.setText(String.format(getString(2131297393), new Object[]{Integer.valueOf(mCountMode4)}));
        this.textMode5.setText(String.format(getString(2131297395), new Object[]{Integer.valueOf(mCountMode5)}));
        this.textMode6.setText(String.format(getString(2131297397), new Object[]{Integer.valueOf(mCountMode6)}));
        this.textMode7.setText(String.format(getString(2131297399), new Object[]{Integer.valueOf(mCountMode7)}));
        this.textMode8.setText(String.format(getString(2131297401), new Object[]{Integer.valueOf(mCountMode8)}));
    }
}
