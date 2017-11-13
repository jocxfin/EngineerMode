package com.android.engineeringmode.gyroscopetest;

import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.engineeringmode.autotest.AutoTestItemActivity;
import com.android.engineeringmode.functions.Light;

import java.util.Calendar;

public class GyroscopeGraph extends AutoTestItemActivity {
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    double[] data = msg.obj;
                    double x = data[0];
                    double y = data[1];
                    x = (double) (((float) Math.round(1.0E8d * x)) / 1.0E8f);
                    y = (double) (((float) Math.round(1.0E8d * y)) / 1.0E8f);
                    double z = (double) (((float) Math.round(1.0E8d * data[2])) / 1.0E8f);
                    GyroscopeGraph.this.mDataX.setText(String.valueOf(x));
                    GyroscopeGraph.this.mDataY.setText(String.valueOf(y));
                    GyroscopeGraph.this.mDataZ.setText(String.valueOf(z));
                    x = (10.0d * x) * 1.9d;
                    y = (10.0d * y) * 1.9d;
                    z = (10.0d * z) * 1.9d;
                    if (x >= 150.0d) {
                        GyroscopeGraph.this.xPassed = GyroscopeGraph.this.xPassed | 1;
                    }
                    if (x <= -150.0d) {
                        GyroscopeGraph.this.xPassed = GyroscopeGraph.this.xPassed | 2;
                    }
                    if (y >= 150.0d) {
                        GyroscopeGraph.this.yPassed = GyroscopeGraph.this.yPassed | 1;
                    }
                    if (y <= -150.0d) {
                        GyroscopeGraph.this.yPassed = GyroscopeGraph.this.yPassed | 2;
                    }
                    if (z >= 150.0d) {
                        GyroscopeGraph.this.zPassed = GyroscopeGraph.this.zPassed | 1;
                    }
                    if (z <= -150.0d) {
                        GyroscopeGraph.this.zPassed = GyroscopeGraph.this.zPassed | 2;
                    }
                    if (GyroscopeGraph.this.xPassed == 3) {
                        GyroscopeGraph.this.mTextX.setTextColor(-65536);
                    }
                    if (GyroscopeGraph.this.yPassed == 3) {
                        GyroscopeGraph.this.mTextY.setTextColor(-65536);
                    }
                    if (GyroscopeGraph.this.zPassed == 3) {
                        GyroscopeGraph.this.mTextZ.setTextColor(-65536);
                    }
                    if (GyroscopeGraph.this.xPassed == 3 && GyroscopeGraph.this.yPassed == 3 && GyroscopeGraph.this.zPassed == 3) {
                        GyroscopeGraph.this.isPassed = Boolean.valueOf(true);
                        GyroscopeGraph.this.mResult.setVisibility(0);
                    }
                    GyroscopeGraph.this.mWaveView.addPoint((int) x, (int) y, (int) z);
                    if (!hasMessages(2)) {
                        sendEmptyMessage(2);
                        return;
                    }
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    GyroscopeGraph.this.mWaveView.draw();
                    return;
                default:
                    return;
            }
        }
    };
    private Boolean isPassed = Boolean.valueOf(false);
    private ImageView mColorX;
    private ImageView mColorY;
    private ImageView mColorZ;
    private TextView mDataX;
    private TextView mDataY;
    private TextView mDataZ;
    private Button mExit;
    private Sensor mGyroSensor;
    private TextView mResult;
    private SensorEventListener mSensorEventListener;
    private SensorManager mSensorManager;
    private TextView mTextX;
    private TextView mTextY;
    private TextView mTextZ;
    private WaveView mWaveView;
    private volatile int xPassed;
    private volatile int yPassed;
    private volatile int zPassed;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903111);
        setTitle(2131297103);
        this.mColorX = (ImageView) findViewById(2131493167);
        this.mColorY = (ImageView) findViewById(2131493169);
        this.mColorZ = (ImageView) findViewById(2131493171);
        this.mColorX.setImageDrawable(new ColorDrawable(-65536));
        this.mColorY.setImageDrawable(new ColorDrawable(-16711936));
        this.mColorZ.setImageDrawable(new ColorDrawable(-16776961));
        this.mDataX = (TextView) findViewById(2131493172);
        this.mDataY = (TextView) findViewById(2131493173);
        this.mDataZ = (TextView) findViewById(2131493174);
        this.mTextX = (TextView) findViewById(2131493166);
        this.mTextY = (TextView) findViewById(2131493168);
        this.mTextZ = (TextView) findViewById(2131493170);
        this.mResult = (TextView) findViewById(2131492997);
        this.mWaveView = (WaveView) findViewById(2131493175);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mGyroSensor = this.mSensorManager.getDefaultSensor(4);
        this.mSensorEventListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                GyroscopeGraph.this.handler.sendMessage(Message.obtain(GyroscopeGraph.this.handler, 1, new double[]{(double) event.values[0], (double) event.values[1], (double) event.values[2]}));
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        this.mExit = (Button) findViewById(2131493142);
        if (checkIsAutoAging() || checkIsAutoTest()) {
            this.mExit.setVisibility(0);
        } else {
            this.mExit.setVisibility(8);
        }
        this.mExit.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                GyroscopeGraph.this.endActivity();
            }
        });
    }

    protected void onPause() {
        super.onPause();
        this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mGyroSensor);
    }

    protected void onStop() {
        super.onStop();
        Calendar now = Calendar.getInstance();
        String time = now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13);
        String content;
        if (this.isPassed.booleanValue()) {
            content = time + "--GyroscopeGraph--" + "PASS";
        } else {
            content = time + "--GyroscopeGraph--" + "FAIL";
        }
    }

    protected void onResume() {
        super.onResume();
        this.mSensorManager.registerListener(this.mSensorEventListener, this.mGyroSensor, 0);
        this.xPassed = 0;
        this.yPassed = 0;
        this.zPassed = 0;
        this.mResult.setVisibility(4);
    }
}
