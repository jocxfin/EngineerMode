package com.android.engineeringmode.manualtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.manualtest.modeltest.ModelTest3ItemActivity;

public class LaserFocusCalibrateTest extends ModelTest3ItemActivity implements OnClickListener {
    private final int UPDATE_TEXT_FAILED = 2;
    private final int UPDATE_TEXT_PASS = 1;
    private boolean isInModelTest = false;
    private int mCalibrateItem = 0;
    private Button mCrossTalkCalibrate;
    private int mCrossTalkValue = 0;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (LaserFocusCalibrateTest.this.mCalibrateItem == 1) {
                if (msg.what == 1) {
                    LaserFocusCalibrateTest.this.mResultOffsetCalibrate.setText("PASS");
                    LaserFocusCalibrateTest.this.mResultOffsetCalibrate.setTextColor(-16711936);
                    LaserFocusCalibrateTest.this.mResultOffsetCalibrate.setGravity(17);
                    LaserFocusCalibrateTest.this.mNoteText.setText("Note: offset value is ");
                    LaserFocusCalibrateTest.this.mNoteText.append(Integer.toString(LaserFocusCalibrateTest.this.mOffsetValue));
                    LaserFocusCalibrateTest.this.mNoteText.setTextColor(-16711936);
                    LaserFocusCalibrateTest.this.mNoteText.setGravity(17);
                } else if (msg.what == 2) {
                    LaserFocusCalibrateTest.this.mResultOffsetCalibrate.setText("FAILED");
                    LaserFocusCalibrateTest.this.mResultOffsetCalibrate.setTextColor(-65536);
                    LaserFocusCalibrateTest.this.mResultOffsetCalibrate.setGravity(17);
                    switch (LaserFocusCalibrateTest.this.mOffsetValue) {
                        case 256:
                            LaserFocusCalibrateTest.this.mNoteText.setText("Error: open /dev/vl6180_ranging failed");
                            break;
                        case 257:
                            LaserFocusCalibrateTest.this.mNoteText.setText("Error: VL6180_IOCTL_STOP failed");
                            break;
                        case 258:
                            LaserFocusCalibrateTest.this.mNoteText.setText("Error: VL6180_IOCTL_INIT failed");
                            break;
                        case 259:
                            LaserFocusCalibrateTest.this.mNoteText.setText("Error: VL6180_IOCTL_GETDATAS failed");
                            break;
                        case 260:
                            LaserFocusCalibrateTest.this.mNoteText.setText("Error: VL6180_IOCTL_OFFCALB failed");
                            break;
                        case 261:
                            LaserFocusCalibrateTest.this.mNoteText.setText("Error: the offset is out of boundary");
                            break;
                        case 262:
                            LaserFocusCalibrateTest.this.mNoteText.setText("Error: VL6180_IOCTL_SETOFFSET failed");
                            break;
                        case 263:
                            LaserFocusCalibrateTest.this.mResultOffsetCalibrate.setText("PASS");
                            LaserFocusCalibrateTest.this.mResultOffsetCalibrate.setTextColor(-16711936);
                            LaserFocusCalibrateTest.this.mResultOffsetCalibrate.setGravity(17);
                            LaserFocusCalibrateTest.this.mNoteText.setText("Note: The default offset is OK, no need to do offset calibration");
                            break;
                        case 264:
                            LaserFocusCalibrateTest.this.mNoteText.setText("Error: Error happened, such as error code is not 0, range is too large or too small, please check the distance from the chart to the device");
                            break;
                    }
                    if (LaserFocusCalibrateTest.this.mOffsetValue == 263) {
                        LaserFocusCalibrateTest.this.mNoteText.setTextColor(-16711936);
                    } else {
                        LaserFocusCalibrateTest.this.mNoteText.setTextColor(-65536);
                    }
                    LaserFocusCalibrateTest.this.mNoteText.setGravity(17);
                }
            } else if (LaserFocusCalibrateTest.this.mCalibrateItem == 2) {
                if (msg.what == 1) {
                    LaserFocusCalibrateTest.this.mResultCrossTalkCalibrate.setText("PASS");
                    LaserFocusCalibrateTest.this.mResultCrossTalkCalibrate.setTextColor(-16711936);
                    LaserFocusCalibrateTest.this.mResultCrossTalkCalibrate.setGravity(17);
                    LaserFocusCalibrateTest.this.mNoteText.setText("Note: cross talk value is ");
                    LaserFocusCalibrateTest.this.mNoteText.append(Integer.toString(LaserFocusCalibrateTest.this.mCrossTalkValue));
                    LaserFocusCalibrateTest.this.mNoteText.setTextColor(-16711936);
                    LaserFocusCalibrateTest.this.mNoteText.setGravity(17);
                } else if (msg.what == 2) {
                    LaserFocusCalibrateTest.this.mResultCrossTalkCalibrate.setText("FAILED");
                    LaserFocusCalibrateTest.this.mResultCrossTalkCalibrate.setTextColor(-65536);
                    LaserFocusCalibrateTest.this.mResultCrossTalkCalibrate.setGravity(17);
                    switch (LaserFocusCalibrateTest.this.mCrossTalkValue) {
                        case -6:
                            LaserFocusCalibrateTest.this.mNoteText.setText("Error: Error happened, such as error code is not 0 or range is too large, please check the distance from the chart to the device");
                            break;
                        case -5:
                            LaserFocusCalibrateTest.this.mNoteText.setText("Error: ioctl VL6180_IOCTL_SETXTALK failed");
                            break;
                        case -4:
                            LaserFocusCalibrateTest.this.mNoteText.setText("Error: ioctl VL6180_IOCTL_GETDATAS failed");
                            break;
                        case -3:
                            LaserFocusCalibrateTest.this.mNoteText.setText("Error: ioctl VL6180_IOCTL_XTALKCALB failed");
                            break;
                        case -2:
                            LaserFocusCalibrateTest.this.mNoteText.setText("Error: ioctl VL6180_IOCTL_STOP failed");
                            break;
                        case -1:
                            LaserFocusCalibrateTest.this.mNoteText.setText("Error: open /dev/stmvl6180_ranging failed");
                            break;
                    }
                    LaserFocusCalibrateTest.this.mNoteText.setTextColor(-65536);
                    LaserFocusCalibrateTest.this.mNoteText.setGravity(17);
                }
            } else if (LaserFocusCalibrateTest.this.mCalibrateItem != 3) {
            } else {
                if (msg.what == 1) {
                    LaserFocusCalibrateTest.this.mResultRestText.setText("SUCCESSFULLY");
                    LaserFocusCalibrateTest.this.mResultRestText.setTextColor(-16711936);
                    LaserFocusCalibrateTest.this.mResultRestText.setGravity(17);
                } else if (msg.what == 2) {
                    switch (LaserFocusCalibrateTest.this.mResetValue) {
                        case -3:
                            LaserFocusCalibrateTest.this.mResultRestText.setText("Error: VL6180_IOCTL_SETXTALK failed");
                            break;
                        case -2:
                            LaserFocusCalibrateTest.this.mResultRestText.setText("Error: VL6180_IOCTL_SETOFFSET failed");
                            break;
                        case -1:
                            LaserFocusCalibrateTest.this.mResultRestText.setText("Error: open /dev/stmvl6180_ranging failed");
                            break;
                    }
                    LaserFocusCalibrateTest.this.mResultRestText.setTextColor(-65536);
                    LaserFocusCalibrateTest.this.mResultRestText.setGravity(17);
                }
            }
        }
    };
    private boolean mIsOffsetCalibrated = false;
    private boolean mIsThreadStart = false;
    private TextView mNoteText;
    private Button mOffsetCalibrate;
    private int mOffsetValue = 0;
    private Button mReset;
    private int mResetValue = 0;
    private TextView mResultCrossTalkCalibrate;
    private TextView mResultOffsetCalibrate;
    private TextView mResultRestText;
    Runnable mRunnable = new Runnable() {
        public void run() {
            Log.i("LaserFocusCalibrateTest", "Thread start");
            LaserFocusCalibrateTest.this.mIsThreadStart = true;
            Object oRemoteService;
            Object oIOemExService;
            if (LaserFocusCalibrateTest.this.mCalibrateItem == 1) {
                LaserFocusCalibrateTest.this.mOffsetValue = Light.offsetCalibrate();
                LaserFocusCalibrateTest.this.mIsOffsetCalibrated = true;
                if (LaserFocusCalibrateTest.this.mOffsetValue < -128 || LaserFocusCalibrateTest.this.mOffsetValue > 127) {
                    LaserFocusCalibrateTest.this.mHandler.sendEmptyMessage(2);
                } else {
                    int Offset;
                    if (LaserFocusCalibrateTest.this.mOffsetValue < 0) {
                        Offset = LaserFocusCalibrateTest.this.mOffsetValue + 256;
                    } else {
                        Offset = LaserFocusCalibrateTest.this.mOffsetValue;
                    }
                    try {
                        oRemoteService = Class.forName("android.os.ServiceManager").getMethod("getService", new Class[]{String.class}).invoke(null, new Object[]{"OEMExService"});
                        oIOemExService = Class.forName("com.oem.os.IOemExService$Stub").getMethod("asInterface", new Class[]{IBinder.class}).invoke(null, new Object[]{oRemoteService});
                        oIOemExService.getClass().getMethod("setLaserSensorOffset", new Class[]{Integer.TYPE}).invoke(oIOemExService, new Object[]{Integer.valueOf(Offset)});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LaserFocusCalibrateTest.this.mHandler.sendEmptyMessage(1);
                }
            } else if (LaserFocusCalibrateTest.this.mCalibrateItem == 2) {
                LaserFocusCalibrateTest.this.mCrossTalkValue = Light.crossTalkCalibrate();
                if (LaserFocusCalibrateTest.this.mCrossTalkValue >= 0) {
                    try {
                        oRemoteService = Class.forName("android.os.ServiceManager").getMethod("getService", new Class[]{String.class}).invoke(null, new Object[]{"OEMExService"});
                        oIOemExService = Class.forName("com.oem.os.IOemExService$Stub").getMethod("asInterface", new Class[]{IBinder.class}).invoke(null, new Object[]{oRemoteService});
                        oIOemExService.getClass().getMethod("setLaserSensorCrossTalk", new Class[]{Integer.TYPE}).invoke(oIOemExService, new Object[]{Integer.valueOf(LaserFocusCalibrateTest.this.mCrossTalkValue)});
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    if (LaserFocusCalibrateTest.this.isInModelTest) {
                        LaserFocusCalibrateTest.this.onTestPassed();
                    }
                    LaserFocusCalibrateTest.this.mHandler.sendEmptyMessage(1);
                } else {
                    LaserFocusCalibrateTest.this.mHandler.sendEmptyMessage(2);
                }
            } else if (LaserFocusCalibrateTest.this.mCalibrateItem == 3) {
                LaserFocusCalibrateTest.this.mResetValue = Light.calibrateReset();
                if (LaserFocusCalibrateTest.this.mResetValue >= 0) {
                    LaserFocusCalibrateTest.this.mHandler.sendEmptyMessage(1);
                } else {
                    LaserFocusCalibrateTest.this.mHandler.sendEmptyMessage(2);
                }
            }
            LaserFocusCalibrateTest.this.mIsThreadStart = false;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903133);
        this.mOffsetCalibrate = (Button) findViewById(2131493310);
        this.mResultOffsetCalibrate = (TextView) findViewById(2131493311);
        this.mCrossTalkCalibrate = (Button) findViewById(2131493312);
        this.mResultCrossTalkCalibrate = (TextView) findViewById(2131493313);
        this.mNoteText = (TextView) findViewById(2131493316);
        this.mReset = (Button) findViewById(2131493314);
        this.mResultRestText = (TextView) findViewById(2131493315);
        this.mOffsetCalibrate.setText(2131297458);
        this.mCrossTalkCalibrate.setText(2131297459);
        this.mReset.setText(2131297460);
        this.mOffsetCalibrate.setOnClickListener(this);
        this.mCrossTalkCalibrate.setOnClickListener(this);
        this.mReset.setOnClickListener(this);
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
    }

    protected void onDestroy() {
        this.mIsThreadStart = false;
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131493310:
                if (!this.mIsThreadStart) {
                    this.mResultOffsetCalibrate.setText("Calibrating, please wait...");
                    this.mResultOffsetCalibrate.setTextColor(-1);
                    this.mResultOffsetCalibrate.setGravity(17);
                    this.mCalibrateItem = 1;
                    break;
                }
                break;
            case 2131493312:
                if (!this.mIsThreadStart) {
                    if (!this.mIsOffsetCalibrated) {
                        this.mResultCrossTalkCalibrate.setText("Please do offset calibrating firstly!");
                        this.mResultCrossTalkCalibrate.setTextColor(-65536);
                        this.mResultCrossTalkCalibrate.setGravity(17);
                        break;
                    }
                    this.mResultCrossTalkCalibrate.setText("Calibrating, please wait...");
                    this.mResultCrossTalkCalibrate.setTextColor(-1);
                    this.mResultCrossTalkCalibrate.setGravity(17);
                    this.mCalibrateItem = 2;
                    break;
                }
                break;
            case 2131493314:
                if (!this.mIsThreadStart) {
                    this.mResultRestText.setText("Resetting, please wait...");
                    this.mResultRestText.setTextColor(-1);
                    this.mResultRestText.setGravity(17);
                    this.mCalibrateItem = 3;
                    break;
                }
                break;
        }
        if (this.mIsThreadStart) {
            this.mCalibrateItem = 0;
        } else {
            new Thread(null, this.mRunnable).start();
        }
    }
}
