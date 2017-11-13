package com.android.engineeringmode.manualtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.manualtest.modeltest.ModelTest3ItemActivity;
import com.android.engineeringmode.util.ExternFunction;

public class LaserFocusBasicTest extends ModelTest3ItemActivity implements OnClickListener {
    private final int REFRESH_RESULT = 2;
    private final int UPDATE_TEXT = 1;
    private boolean isInModelTest = false;
    private boolean mConnected = false;
    private TextView mConvTimeTextView;
    private TextView mCrossTalkTextView;
    private int mCurrentTime = 0;
    private int mErrorStatus = 0;
    private TextView mErrorStatusTextView;
    private ExternFunction mExFunction;
    private Button mFortiethButton;
    private TextView mFortiethTextView;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1 && LaserFocusBasicTest.this.mLaserRangeValue < 0) {
                switch (LaserFocusBasicTest.this.mLaserRangeValue) {
                    case -4:
                        LaserFocusBasicTest.this.mNoteTextView.setText("Error: VL6180_IOCTL_GETDATAS failed");
                        break;
                    case -3:
                        LaserFocusBasicTest.this.mNoteTextView.setText("Error: VL6180_IOCTL_INIT failed");
                        break;
                    case -2:
                        LaserFocusBasicTest.this.mNoteTextView.setText("Error: VL6180_IOCTL_STOP failed");
                        break;
                    case -1:
                        LaserFocusBasicTest.this.mNoteTextView.setText("Error: open /dev/stmvl6180_ranging failed");
                        break;
                }
                LaserFocusBasicTest.this.mNoteTextView.setTextColor(-65536);
                LaserFocusBasicTest.this.mNoteTextView.setGravity(17);
                LaserFocusBasicTest.this.mResultTextView.setText("FAIL");
                LaserFocusBasicTest.this.mResultTextView.setTextColor(-65536);
                LaserFocusBasicTest.this.mResultTextView.setGravity(17);
            }
            if (msg.what == 1 && LaserFocusBasicTest.this.mLaserRangeValue >= 0) {
                LaserFocusBasicTest.this.mResultRangeTextView.setText(Integer.toString(LaserFocusBasicTest.this.mLaserRangeValue));
                LaserFocusBasicTest.this.mResultMinTextView.setText(Integer.toString(LaserFocusBasicTest.this.mMin));
                LaserFocusBasicTest.this.mResultMaxTextView.setText(Integer.toString(LaserFocusBasicTest.this.mMax));
                LaserFocusBasicTest.this.mResultMeanTextView.setText(Integer.toString(LaserFocusBasicTest.this.mMean));
                LaserFocusBasicTest.this.mResultRtnSignalRateTextView.setText(Float.toString(LaserFocusBasicTest.this.mLaserRateValue));
                LaserFocusBasicTest.this.mResultRawRangeTextView.setText(Integer.toString(Light.getLaserRawRangeValue()));
                LaserFocusBasicTest.this.mResultRangeOffsetTextView.setText(Integer.toString(Light.getLaserRangeOffsetValue()));
                LaserFocusBasicTest.this.mResultCrossTalkTextView.setText(Float.toString(Light.getLaserCrossTalkValue()));
                LaserFocusBasicTest.this.mResultRefSignalRateTextView.setText(Float.toString(Light.getLaserRefSignalRateValue()));
                LaserFocusBasicTest.this.mResultRtnAmbRateTextView.setText(Float.toString(Light.getLaserRtnAmbRateValue()));
                LaserFocusBasicTest.this.mResultRefAmbRateTextView.setText(Float.toString(Light.getLaserRefAmbRateValue()));
                LaserFocusBasicTest.this.mResultConvTimeTextView.setText(Integer.toString(Light.getLaserConvTimeValue()));
                LaserFocusBasicTest.this.mResultRtnSignalCountTextView.setText(Integer.toString(Light.getLaserRtnSignalCountValue()));
                LaserFocusBasicTest.this.mResultRefSignalCountTextView.setText(Integer.toString(Light.getLaserRefSignalCountValue()));
                LaserFocusBasicTest.this.mResultRtnAmbCountTextView.setText(Integer.toString(Light.getLaserRtnAmbCountValue()));
                LaserFocusBasicTest.this.mResultRefAmbCountTextView.setText(Integer.toString(Light.getLaserRefAmbCountValue()));
                LaserFocusBasicTest.this.mResultRtnConvTimeTextView.setText(Integer.toString(Light.getLaserRtnConvTimeValue()));
                LaserFocusBasicTest.this.mResultRefConvTimeTextView.setText(Integer.toString(Light.getLaserRefConvTimeValue()));
                LaserFocusBasicTest.this.mErrorStatus = Light.getLaserErrorCodeValue();
                switch (LaserFocusBasicTest.this.mErrorStatus & 240) {
                    case 0:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("No error");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-16711936);
                        break;
                    case 16:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("VCSEL Continuity Test");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-16711936);
                        break;
                    case 32:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("VCSEL Watchdog Test");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-16711936);
                        break;
                    case 48:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("VCSEL Watchdog");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-16711936);
                        break;
                    case 64:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("PLL1 Lock");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-65536);
                        break;
                    case 80:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("PLL2 Lock");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-65536);
                        break;
                    case 96:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("Early Convergence Estimate");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-65536);
                        break;
                    case 112:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("Max Convergence");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-65536);
                        break;
                    case 128:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("No Target Ignore");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-65536);
                        break;
                    case 144:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("Not used");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-16711936);
                        break;
                    case 160:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("Not used");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-16711936);
                        break;
                    case 176:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("Max Signal To Noise Ratio");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-65536);
                        break;
                    case 192:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("Raw Ranging Algo Underflow");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-65536);
                        break;
                    case 208:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("Raw Ranging Algo Overflow");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-65536);
                        break;
                    case 224:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("Ranging Algo Underflow");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-65536);
                        break;
                    case 240:
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setText("Ranging Algo Overflow");
                        LaserFocusBasicTest.this.mResultErrorStatusTextView.setTextColor(-65536);
                        break;
                }
                if (!LaserFocusBasicTest.this.mIsThreadStart) {
                    LaserFocusBasicTest.this.mHandler.sendEmptyMessage(2);
                }
            } else if (msg.what != 2) {
            } else {
                if (LaserFocusBasicTest.this.mTestResult == 10) {
                    LaserFocusBasicTest.this.mResultTextView.setText("PASS");
                    LaserFocusBasicTest.this.mResultTextView.setTextColor(-16711936);
                    LaserFocusBasicTest.this.mResultTextView.setGravity(17);
                    if (LaserFocusBasicTest.this.mTestOption == 100) {
                        LaserFocusBasicTest.this.mTest100Passed = true;
                    } else if (LaserFocusBasicTest.this.mTestOption == 400) {
                        LaserFocusBasicTest.this.mTest400Passed = true;
                    }
                    if (LaserFocusBasicTest.this.mTest100Passed && LaserFocusBasicTest.this.mTest400Passed) {
                        LaserFocusBasicTest.this.mExFunction.setProductLineTestFlagExtraByte(22, (byte) 1);
                        if (LaserFocusBasicTest.this.isInModelTest) {
                            LaserFocusBasicTest.this.onTestPassed();
                            return;
                        }
                        return;
                    }
                    return;
                }
                LaserFocusBasicTest.this.mExFunction.setProductLineTestFlagExtraByte(22, (byte) 2);
                LaserFocusBasicTest.this.mResultTextView.setText("FAIL");
                LaserFocusBasicTest.this.mResultTextView.setTextColor(-65536);
                LaserFocusBasicTest.this.mResultTextView.setGravity(17);
            }
        }
    };
    private boolean mIsThreadStart = false;
    private int mLaserRangeValue = 0;
    private float mLaserRateValue = 0.0f;
    private int mMax = 0;
    private TextView mMaxTextView;
    private int mMean = 0;
    private TextView mMeanTextView;
    private int mMin = 0;
    private TextView mMinTextView;
    private Handler mNVHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                LaserFocusBasicTest.this.mConnected = true;
            }
        }
    };
    private TextView mNoteTextView;
    private TextView mRangeOffsetTextView;
    private TextView mRangeTextView;
    private TextView mRawRangeTextView;
    private TextView mRefAmbCountTextView;
    private TextView mRefAmbRateTextView;
    private TextView mRefConvTimeTextView;
    private TextView mRefSignalCountTextView;
    private TextView mRefSignalRateTextView;
    private TextView mResultConvTimeTextView;
    private TextView mResultCrossTalkTextView;
    private TextView mResultErrorStatusTextView;
    private TextView mResultMaxTextView;
    private TextView mResultMeanTextView;
    private TextView mResultMinTextView;
    private TextView mResultRangeOffsetTextView;
    private TextView mResultRangeTextView;
    private TextView mResultRawRangeTextView;
    private TextView mResultRefAmbCountTextView;
    private TextView mResultRefAmbRateTextView;
    private TextView mResultRefConvTimeTextView;
    private TextView mResultRefSignalCountTextView;
    private TextView mResultRefSignalRateTextView;
    private TextView mResultRtnAmbCountTextView;
    private TextView mResultRtnAmbRateTextView;
    private TextView mResultRtnConvTimeTextView;
    private TextView mResultRtnSignalCountTextView;
    private TextView mResultRtnSignalRateTextView;
    private TextView mResultTextView;
    private TextView mRtnAmbCountTextView;
    private TextView mRtnAmbRateTextView;
    private TextView mRtnConvTimeTextView;
    private int mRtnRate = 0;
    private TextView mRtnSignalCountTextView;
    private TextView mRtnSignalRateTextView;
    Runnable mRunnable = new Runnable() {
        public void run() {
            Log.i("LaserFocusBasicTest", "Thread start, open Laser sensor");
            LaserFocusBasicTest.this.mLaserRangeValue = Light.openLaserSensor();
            if (LaserFocusBasicTest.this.mLaserRangeValue < 0) {
                LaserFocusBasicTest.this.mHandler.sendEmptyMessage(1);
                LaserFocusBasicTest.this.mIsThreadStart = false;
            }
            while (LaserFocusBasicTest.this.mIsThreadStart) {
                LaserFocusBasicTest.this.mLaserRangeValue = Light.getLaserRangeValue();
                LaserFocusBasicTest.this.mLaserRateValue = Light.getLaserRtnSignalRateValue();
                if (LaserFocusBasicTest.this.mLaserRangeValue < 0) {
                    LaserFocusBasicTest.this.mHandler.sendEmptyMessage(1);
                    LaserFocusBasicTest.this.mIsThreadStart = false;
                    return;
                }
                LaserFocusBasicTest laserFocusBasicTest;
                if (((double) LaserFocusBasicTest.this.mLaserRangeValue) <= ((double) LaserFocusBasicTest.this.mTestOption) * 1.2d && ((double) LaserFocusBasicTest.this.mLaserRangeValue) >= ((double) LaserFocusBasicTest.this.mTestOption) * 0.8d) {
                    laserFocusBasicTest = LaserFocusBasicTest.this;
                    laserFocusBasicTest.mTestResult = laserFocusBasicTest.mTestResult + 1;
                }
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    Log.i("LaserFocusBasicTest", Log.getStackTraceString(e));
                }
                laserFocusBasicTest = LaserFocusBasicTest.this;
                laserFocusBasicTest.mCurrentTime = laserFocusBasicTest.mCurrentTime + 1;
                if (LaserFocusBasicTest.this.mCurrentTime >= 10) {
                    LaserFocusBasicTest.this.mIsThreadStart = false;
                }
                if (LaserFocusBasicTest.this.mCurrentTime == 1) {
                    LaserFocusBasicTest.this.mMin = LaserFocusBasicTest.this.mLaserRangeValue;
                    LaserFocusBasicTest.this.mMax = LaserFocusBasicTest.this.mLaserRangeValue;
                }
                if (LaserFocusBasicTest.this.mMin >= LaserFocusBasicTest.this.mLaserRangeValue) {
                    LaserFocusBasicTest.this.mMin = LaserFocusBasicTest.this.mLaserRangeValue;
                }
                if (LaserFocusBasicTest.this.mMax <= LaserFocusBasicTest.this.mLaserRangeValue) {
                    LaserFocusBasicTest.this.mMax = LaserFocusBasicTest.this.mLaserRangeValue;
                }
                laserFocusBasicTest = LaserFocusBasicTest.this;
                laserFocusBasicTest.mSum = laserFocusBasicTest.mSum + LaserFocusBasicTest.this.mLaserRangeValue;
                LaserFocusBasicTest.this.mMean = LaserFocusBasicTest.this.mSum / LaserFocusBasicTest.this.mCurrentTime;
                LaserFocusBasicTest.this.mHandler.sendEmptyMessage(1);
            }
            LaserFocusBasicTest.this.mCurrentTime = 0;
            LaserFocusBasicTest.this.mSum = 0;
            Log.i("LaserFocusBasicTest", "Thread end, close Laser sensor");
            if (Light.closeLaserSensor() != 0) {
                Log.e("LaserFocusBasicTest", "Close Laser sensor failed!");
            }
        }
    };
    private int mSum = 0;
    private Button mTenthButton;
    private TextView mTenthTextView;
    private boolean mTest100Passed = false;
    private boolean mTest400Passed = false;
    private int mTestOption = 0;
    private int mTestResult = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903132);
        this.mTenthButton = (Button) findViewById(2131493305);
        this.mFortiethButton = (Button) findViewById(2131493307);
        this.mTenthTextView = (TextView) findViewById(2131493306);
        this.mFortiethTextView = (TextView) findViewById(2131493308);
        this.mRtnSignalRateTextView = (TextView) findViewById(2131493283);
        this.mResultRtnSignalRateTextView = (TextView) findViewById(2131493284);
        this.mMinTextView = (TextView) findViewById(2131493269);
        this.mResultMinTextView = (TextView) findViewById(2131493270);
        this.mMaxTextView = (TextView) findViewById(2131493271);
        this.mResultMaxTextView = (TextView) findViewById(2131493272);
        this.mMeanTextView = (TextView) findViewById(2131493273);
        this.mResultMeanTextView = (TextView) findViewById(2131493274);
        this.mTenthButton.setOnClickListener(this);
        this.mFortiethButton.setOnClickListener(this);
        this.mRangeTextView = (TextView) findViewById(2131493267);
        this.mRawRangeTextView = (TextView) findViewById(2131493275);
        this.mErrorStatusTextView = (TextView) findViewById(2131493277);
        this.mRangeOffsetTextView = (TextView) findViewById(2131493279);
        this.mCrossTalkTextView = (TextView) findViewById(2131493281);
        this.mRefSignalRateTextView = (TextView) findViewById(2131493285);
        this.mRtnAmbRateTextView = (TextView) findViewById(2131493287);
        this.mRefAmbRateTextView = (TextView) findViewById(2131493289);
        this.mConvTimeTextView = (TextView) findViewById(2131493291);
        this.mRtnSignalCountTextView = (TextView) findViewById(2131493293);
        this.mRefSignalCountTextView = (TextView) findViewById(2131493295);
        this.mRtnAmbCountTextView = (TextView) findViewById(2131493297);
        this.mRefAmbCountTextView = (TextView) findViewById(2131493299);
        this.mRtnConvTimeTextView = (TextView) findViewById(2131493301);
        this.mRefConvTimeTextView = (TextView) findViewById(2131493303);
        this.mResultRangeTextView = (TextView) findViewById(2131493268);
        this.mResultRawRangeTextView = (TextView) findViewById(2131493276);
        this.mResultErrorStatusTextView = (TextView) findViewById(2131493278);
        this.mResultRangeOffsetTextView = (TextView) findViewById(2131493280);
        this.mResultCrossTalkTextView = (TextView) findViewById(2131493282);
        this.mResultRefSignalRateTextView = (TextView) findViewById(2131493286);
        this.mResultRtnAmbRateTextView = (TextView) findViewById(2131493288);
        this.mResultRefAmbRateTextView = (TextView) findViewById(2131493290);
        this.mResultConvTimeTextView = (TextView) findViewById(2131493292);
        this.mResultRtnSignalCountTextView = (TextView) findViewById(2131493294);
        this.mResultRefSignalCountTextView = (TextView) findViewById(2131493296);
        this.mResultRtnAmbCountTextView = (TextView) findViewById(2131493298);
        this.mResultRefAmbCountTextView = (TextView) findViewById(2131493300);
        this.mResultRtnConvTimeTextView = (TextView) findViewById(2131493302);
        this.mResultRefConvTimeTextView = (TextView) findViewById(2131493304);
        this.mNoteTextView = (TextView) findViewById(2131493309);
        this.mMinTextView.setText(2131297462);
        this.mMinTextView.setTextColor(-16711936);
        this.mMinTextView.setGravity(5);
        this.mMaxTextView.setText(2131297463);
        this.mMaxTextView.setTextColor(-16711936);
        this.mMaxTextView.setGravity(5);
        this.mMeanTextView.setText(2131297464);
        this.mMeanTextView.setTextColor(-16711936);
        this.mMeanTextView.setGravity(5);
        this.mRangeTextView.setText(2131297461);
        this.mRangeTextView.setTextColor(-16711936);
        this.mRangeTextView.setGravity(5);
        this.mRawRangeTextView.setText("Raw Range(mm): ");
        this.mRawRangeTextView.setGravity(5);
        this.mErrorStatusTextView.setText("Error Status: ");
        this.mErrorStatusTextView.setGravity(5);
        this.mRangeOffsetTextView.setText("Range Offset(mm): ");
        this.mRangeOffsetTextView.setGravity(5);
        this.mCrossTalkTextView.setText("Cross-Talk Comp Factor: ");
        this.mCrossTalkTextView.setGravity(5);
        this.mRtnSignalRateTextView.setText("Rtn Signal Rate(Mcps): ");
        this.mRtnSignalRateTextView.setTextColor(-16711936);
        this.mRtnSignalRateTextView.setGravity(5);
        this.mRefSignalRateTextView.setText("Ref Signal Rate(Mcps): ");
        this.mRefSignalRateTextView.setGravity(5);
        this.mRtnAmbRateTextView.setText("Rtn Amb Rate(Mcps): ");
        this.mRtnAmbRateTextView.setGravity(5);
        this.mRefAmbRateTextView.setText("Ref Amb Rate(Mcps): ");
        this.mRefAmbRateTextView.setGravity(5);
        this.mConvTimeTextView.setText("Conv Time(us): ");
        this.mConvTimeTextView.setGravity(5);
        this.mRtnSignalCountTextView.setText("Rtn Signal Count: ");
        this.mRtnSignalCountTextView.setGravity(5);
        this.mRefSignalCountTextView.setText("Ref Signal Count: ");
        this.mRefSignalCountTextView.setGravity(5);
        this.mRtnAmbCountTextView.setText("Rtn Amb Count: ");
        this.mRtnAmbCountTextView.setGravity(5);
        this.mRefAmbCountTextView.setText("Ref Amb Count: ");
        this.mRefAmbCountTextView.setGravity(5);
        this.mRtnConvTimeTextView.setText("Rtn Conv Time(us): ");
        this.mRtnConvTimeTextView.setGravity(5);
        this.mRefConvTimeTextView.setText("Ref Conv Time(us): ");
        this.mRefConvTimeTextView.setGravity(5);
        this.mExFunction = new ExternFunction(this);
        this.mExFunction.registerOnServiceConnected(this.mNVHandler, 1, null);
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
        if (this.isInModelTest) {
            this.mTenthButton.setEnabled(true);
            this.mFortiethButton.setEnabled(true);
        }
    }

    protected void onDestroy() {
        this.mIsThreadStart = false;
        if (this.mExFunction != null) {
            this.mExFunction.unregisterOnServiceConnected(this.mNVHandler);
            this.mExFunction.dispose();
        }
        super.onDestroy();
    }

    public void onClick(View v) {
        if (!this.mIsThreadStart) {
            switch (v.getId()) {
                case 2131493305:
                    this.mTestOption = 100;
                    this.mResultTextView = this.mTenthTextView;
                    break;
                case 2131493307:
                    this.mTestOption = 400;
                    this.mResultTextView = this.mFortiethTextView;
                    break;
            }
            this.mResultTextView.setText("");
            this.mResultTextView.setText("Testing, please wait...");
            this.mResultTextView.setTextColor(-1);
            this.mResultTextView.setGravity(17);
            this.mResultRangeTextView.setTextColor(-16711936);
            this.mResultMinTextView.setTextColor(-16711936);
            this.mResultMaxTextView.setTextColor(-16711936);
            this.mResultMeanTextView.setTextColor(-16711936);
            this.mResultRtnSignalRateTextView.setTextColor(-16711936);
            this.mResultMaxTextView.setText("");
            this.mResultMeanTextView.setText("");
            this.mResultRtnSignalRateTextView.setText("");
            this.mResultRangeTextView.setText("");
            this.mResultMinTextView.setText("");
            this.mResultRawRangeTextView.setText("");
            this.mResultRangeOffsetTextView.setText("");
            this.mResultCrossTalkTextView.setText("");
            this.mResultRefSignalRateTextView.setText("");
            this.mResultRtnAmbRateTextView.setText("");
            this.mResultRefAmbRateTextView.setText("");
            this.mResultConvTimeTextView.setText("");
            this.mResultRtnSignalCountTextView.setText("");
            this.mResultRefSignalCountTextView.setText("");
            this.mResultRtnAmbCountTextView.setText("");
            this.mResultRefAmbCountTextView.setText("");
            this.mResultRtnConvTimeTextView.setText("");
            this.mResultRefConvTimeTextView.setText("");
            this.mResultErrorStatusTextView.setText("");
            this.mNoteTextView.setText("");
            this.mLaserRangeValue = 0;
            this.mTestResult = 0;
            this.mIsThreadStart = true;
            new Thread(null, this.mRunnable).start();
        }
    }
}
