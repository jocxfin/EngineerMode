package com.android.engineeringmode.manualtest;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.manualtest.modeltest.ModelTest3ItemActivity;

public class InfraredGestureManualTest extends ModelTest3ItemActivity implements OnClickListener {
    private RelativeLayout buttonLayout;
    private boolean isInModelTest = false;
    private View mAfterTestView;
    private View mBeforeTestView;
    private TextView mBottomTextView;
    private TextView mCenterTextView;
    private Button mExitButton;
    private boolean mIsBottomSensor = false;
    private boolean mIsLeftSensor = false;
    private boolean mIsManualTestMode = true;
    private boolean mIsRightSensor = false;
    private boolean mIsTestFinished = false;
    private boolean mIsTopSensor = false;
    private TextView mLeftTextView;
    private TextView mResultTextView;
    private Button mRetryButton;
    private TextView mRightTextView;
    private Sensor mSensor = null;
    private SensorEventListener mSensorListener = new SensorEventListener() {
        @SuppressLint({"NewApi"})
        public void onSensorChanged(SensorEvent event) {
            if (!InfraredGestureManualTest.this.mIsTestFinished) {
                if (event.values[0] == 0.0f && event.values[1] == 0.0f) {
                    if (event.values[2] == 0.0f) {
                        return;
                    }
                }
                Log.e("InfraredGestureSensorTest", "sensorChanged " + event.sensor.getName() + ", x: " + event.values[0] + ", y: " + event.values[1] + ", z: " + event.values[2]);
                switch ((int) event.values[0]) {
                    case Light.MAIN_KEY_LIGHT /*1*/:
                    case 17:
                    case 33:
                        InfraredGestureManualTest.this.mIsBottomSensor = true;
                        InfraredGestureManualTest.this.mBottomTextView.setBackgroundResource(2130837505);
                        break;
                    case Light.CHARGE_RED_LIGHT /*2*/:
                    case 18:
                    case 34:
                        InfraredGestureManualTest.this.mIsTopSensor = true;
                        InfraredGestureManualTest.this.mTopTextView.setBackgroundResource(2130837532);
                        break;
                    case 4:
                    case 20:
                    case 36:
                        InfraredGestureManualTest.this.mIsLeftSensor = true;
                        InfraredGestureManualTest.this.mLeftTextView.setBackgroundResource(2130837525);
                        break;
                    case 8:
                    case 24:
                    case 40:
                        InfraredGestureManualTest.this.mIsRightSensor = true;
                        InfraredGestureManualTest.this.mRightTextView.setBackgroundResource(2130837530);
                        break;
                }
                if (InfraredGestureManualTest.this.mIsLeftSensor && InfraredGestureManualTest.this.mIsRightSensor && InfraredGestureManualTest.this.mIsTopSensor && InfraredGestureManualTest.this.mIsBottomSensor) {
                    InfraredGestureManualTest.this.mIsLeftSensor = false;
                    InfraredGestureManualTest.this.mIsRightSensor = false;
                    InfraredGestureManualTest.this.mIsTopSensor = false;
                    InfraredGestureManualTest.this.mIsBottomSensor = false;
                    InfraredGestureManualTest.this.mIsTestFinished = true;
                    InfraredGestureManualTest.this.mCenterTextView.setBackgroundResource(2130837508);
                    InfraredGestureManualTest.this.mBeforeTestView.setVisibility(8);
                    InfraredGestureManualTest.this.mAfterTestView.setVisibility(0);
                    InfraredGestureManualTest.this.mResultTextView.setText("PASS");
                    InfraredGestureManualTest.this.mResultTextView.setTextColor(-16711936);
                    if (InfraredGestureManualTest.this.isInModelTest) {
                        InfraredGestureManualTest.this.setResult(1);
                        InfraredGestureManualTest.this.finish();
                    }
                }
            }
        }

        @SuppressLint({"NewApi"})
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private SensorManager mSensorManager = null;
    private TextView mTopTextView;

    @SuppressLint({"NewApi"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mSensor = this.mSensorManager.getDefaultSensor(33171014);
        setContentView(2130903123);
        this.mBeforeTestView = findViewById(2131493223);
        this.mAfterTestView = findViewById(2131493229);
        this.mResultTextView = (TextView) findViewById(2131493230);
        this.mLeftTextView = (TextView) findViewById(2131493226);
        this.mRightTextView = (TextView) findViewById(2131493227);
        this.mTopTextView = (TextView) findViewById(2131493224);
        this.mBottomTextView = (TextView) findViewById(2131493228);
        this.mCenterTextView = (TextView) findViewById(2131493225);
        this.mRetryButton = (Button) findViewById(2131493231);
        this.mExitButton = (Button) findViewById(2131493232);
        this.mRetryButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                InfraredGestureManualTest.this.mIsLeftSensor = false;
                InfraredGestureManualTest.this.mIsRightSensor = false;
                InfraredGestureManualTest.this.mIsTopSensor = false;
                InfraredGestureManualTest.this.mIsBottomSensor = false;
                InfraredGestureManualTest.this.mIsTestFinished = false;
                InfraredGestureManualTest.this.mBeforeTestView.setVisibility(0);
                InfraredGestureManualTest.this.mAfterTestView.setVisibility(8);
                InfraredGestureManualTest.this.mResultTextView.setText("");
                InfraredGestureManualTest.this.mResultTextView.setTextColor(-16777216);
                InfraredGestureManualTest.this.mLeftTextView.setBackgroundResource(2130837524);
                InfraredGestureManualTest.this.mRightTextView.setBackgroundResource(2130837529);
                InfraredGestureManualTest.this.mTopTextView.setBackgroundResource(2130837531);
                InfraredGestureManualTest.this.mBottomTextView.setBackgroundResource(2130837504);
                InfraredGestureManualTest.this.mCenterTextView.setBackgroundResource(2130837507);
            }
        });
        this.mExitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                InfraredGestureManualTest.this.finish();
            }
        });
        if (!(getIntent() == null || getIntent().getBooleanExtra("isManualTestMode", true))) {
            this.mIsManualTestMode = false;
        }
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
        if (this.isInModelTest) {
            this.mRetryButton.setVisibility(8);
            this.mExitButton.setVisibility(8);
        } else {
            this.buttonLayout = (RelativeLayout) findViewById(2131493190);
            this.buttonLayout.setVisibility(8);
        }
        initResources();
    }

    private void initResources() {
        Button pass = (Button) findViewById(2131493015);
        pass.setOnClickListener(this);
        pass.setVisibility(8);
        ((Button) findViewById(2131493236)).setOnClickListener(this);
        ((Button) findViewById(2131493237)).setOnClickListener(this);
    }

    public void onClick(View v) {
        Log.i("InfraredGestureSensorTest", "onClick");
        switch (v.getId()) {
            case 2131493015:
                setResult(1);
                finish();
                return;
            case 2131493236:
                setResult(2);
                finish();
                return;
            case 2131493237:
                setResult(3);
                finish();
                return;
            default:
                return;
        }
    }

    @SuppressLint({"NewApi"})
    protected void onResume() {
        this.mSensorManager.registerListener(this.mSensorListener, this.mSensor, 0);
        super.onResume();
    }

    @SuppressLint({"NewApi"})
    protected void onStop() {
        this.mSensorManager.unregisterListener(this.mSensorListener);
        super.onStop();
    }
}
