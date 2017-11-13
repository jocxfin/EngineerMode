package com.android.engineeringmode.manualtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;

public class InfraredGestureAgingTest extends Activity {
    private Handler mAcquireDataResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle res = msg.getData();
            if (res != null && res.getString("resultdata") != null) {
                Log.d("InfraredGestureSensorTest", "mAcquireDataResultHandler result:" + res.getString("resultdata"));
                String[] eachDat = res.getString("resultdata").split(",");
                if (eachDat.length == 4) {
                    try {
                        InfraredGestureAgingTest.this.mLeftTimes = Integer.parseInt(eachDat[0]);
                        InfraredGestureAgingTest.this.mTopTimes = Integer.parseInt(eachDat[1]);
                        InfraredGestureAgingTest.this.mRightTimes = Integer.parseInt(eachDat[2]);
                        InfraredGestureAgingTest.this.mBottomTimes = Integer.parseInt(eachDat[3]);
                        if (InfraredGestureAgingTest.this.mLeftTimes > 0) {
                            InfraredGestureAgingTest.this.mLeftTextView.setBackgroundResource(2130837525);
                        }
                        if (InfraredGestureAgingTest.this.mRightTimes > 0) {
                            InfraredGestureAgingTest.this.mRightTextView.setBackgroundResource(2130837530);
                        }
                        if (InfraredGestureAgingTest.this.mTopTimes > 0) {
                            InfraredGestureAgingTest.this.mTopTextView.setBackgroundResource(2130837532);
                        }
                        if (InfraredGestureAgingTest.this.mBottomTimes > 0) {
                            InfraredGestureAgingTest.this.mBottomTextView.setBackgroundResource(2130837505);
                        }
                    } catch (NumberFormatException e) {
                        InfraredGestureAgingTest.this.mLeftTimes = 0;
                        InfraredGestureAgingTest.this.mRightTimes = 0;
                        InfraredGestureAgingTest.this.mTopTimes = 0;
                        InfraredGestureAgingTest.this.mBottomTimes = 0;
                        Log.d("InfraredGestureSensorTest", "NumberFormatException " + e.getMessage());
                    }
                }
                if (InfraredGestureAgingTest.this.mRightTimes > 0 && InfraredGestureAgingTest.this.mLeftTimes > 0 && InfraredGestureAgingTest.this.mTopTimes > 0 && InfraredGestureAgingTest.this.mBottomTimes > 0) {
                    InfraredGestureAgingTest.this.mCenterTextView.setBackgroundResource(2130837508);
                }
                InfraredGestureAgingTest.this.mResultTextView.setTextColor(-65536);
                InfraredGestureAgingTest.this.mResultTextView.setText(InfraredGestureAgingTest.this.makeResultText());
            }
        }
    };
    private View mAfterTestView;
    private View mBeforeTestView;
    private TextView mBottomTextView;
    private int mBottomTimes = 0;
    private TextView mCenterTextView;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 4096:
                    InfraredGestureAgingTest.this.acquireDataFromSdCard(4096);
                    return;
                case 4097:
                    InfraredGestureAgingTest.this.saveDataToSdCard(String.format("%d,%d,%d,%d", new Object[]{Integer.valueOf(InfraredGestureAgingTest.this.mLeftTimes), Integer.valueOf(InfraredGestureAgingTest.this.mTopTimes), Integer.valueOf(InfraredGestureAgingTest.this.mRightTimes), Integer.valueOf(InfraredGestureAgingTest.this.mBottomTimes)}));
                    return;
                default:
                    return;
            }
        }
    };
    private TextView mLeftTextView;
    private int mLeftTimes = 0;
    private Messenger mMessenger;
    private Button mResetDataButton;
    private TextView mResultTextView;
    private TextView mRightTextView;
    private int mRightTimes = 0;
    private Handler mSaveDataResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle res = msg.getData();
            Log.d("InfraredGestureSensorTest", "mResultHandler result:" + res.getBoolean("RESULT_RENAME"));
            if (res.getBoolean("RESULT_RENAME")) {
                Toast.makeText(InfraredGestureAgingTest.this, "save data successed", 0).show();
            } else {
                Toast.makeText(InfraredGestureAgingTest.this, "save data failed", 0).show();
            }
            InfraredGestureAgingTest.this.mResetDataButton.setEnabled(true);
        }
    };
    private Sensor mSensor = null;
    private SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            if (event.values[0] == 0.0f && event.values[1] == 0.0f) {
                if (event.values[2] == 0.0f) {
                    return;
                }
            }
            Log.e("InfraredGestureSensorTest", "sensorChanged " + event.sensor.getName() + ", x: " + event.values[0] + ", y: " + event.values[1] + ", z: " + event.values[2]);
            InfraredGestureAgingTest infraredGestureAgingTest;
            switch ((int) event.values[0]) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                case 17:
                case 33:
                    infraredGestureAgingTest = InfraredGestureAgingTest.this;
                    infraredGestureAgingTest.mBottomTimes = infraredGestureAgingTest.mBottomTimes + 1;
                    InfraredGestureAgingTest.this.mBottomTextView.setBackgroundResource(2130837505);
                    break;
                case Light.CHARGE_RED_LIGHT /*2*/:
                case 18:
                case 34:
                    infraredGestureAgingTest = InfraredGestureAgingTest.this;
                    infraredGestureAgingTest.mTopTimes = infraredGestureAgingTest.mTopTimes + 1;
                    InfraredGestureAgingTest.this.mTopTextView.setBackgroundResource(2130837532);
                    break;
                case 4:
                case 20:
                case 36:
                    infraredGestureAgingTest = InfraredGestureAgingTest.this;
                    infraredGestureAgingTest.mLeftTimes = infraredGestureAgingTest.mLeftTimes + 1;
                    InfraredGestureAgingTest.this.mLeftTextView.setBackgroundResource(2130837525);
                    break;
                case 8:
                case 24:
                case 40:
                    infraredGestureAgingTest = InfraredGestureAgingTest.this;
                    infraredGestureAgingTest.mRightTimes = infraredGestureAgingTest.mRightTimes + 1;
                    InfraredGestureAgingTest.this.mRightTextView.setBackgroundResource(2130837530);
                    break;
            }
            if (InfraredGestureAgingTest.this.mRightTimes > 0 && InfraredGestureAgingTest.this.mLeftTimes > 0 && InfraredGestureAgingTest.this.mTopTimes > 0 && InfraredGestureAgingTest.this.mBottomTimes > 0) {
                InfraredGestureAgingTest.this.mCenterTextView.setBackgroundResource(2130837508);
            }
            InfraredGestureAgingTest.this.mResultTextView.setTextColor(-65536);
            InfraredGestureAgingTest.this.mResultTextView.setText(InfraredGestureAgingTest.this.makeResultText());
        }

        @SuppressLint({"NewApi"})
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private SensorManager mSensorManager = null;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            InfraredGestureAgingTest.this.mMessenger = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            InfraredGestureAgingTest.this.mMessenger = new Messenger(service);
            InfraredGestureAgingTest.this.mHandler.sendEmptyMessage(4096);
        }
    };
    private TextView mTopTextView;
    private int mTopTimes = 0;

    private void acquireDataFromSdCard(int msgType) {
        Message message = Message.obtain(null, 0);
        message.replyTo = new Messenger(this.mAcquireDataResultHandler);
        message.arg1 = 1;
        Bundle bundle = new Bundle();
        bundle.putString("KEY_RENAME_FROM", "/sdcard/infraredGestureAgingTest.txt");
        message.setData(bundle);
        try {
            this.mMessenger.send(message);
        } catch (RemoteException e) {
            Log.e("InfraredGestureSensorTest", "handleMessage RemoteException :" + e.getMessage());
        }
    }

    private void saveDataToSdCard(String data) {
        Message message = Message.obtain(null, 0);
        message.replyTo = new Messenger(this.mSaveDataResultHandler);
        message.arg1 = 0;
        Bundle bundle = new Bundle();
        bundle.putString("KEY_RENAME_FROM", "/sdcard/infraredGestureAgingTest.txt");
        Log.d("InfraredGestureSensorTest", "data:" + data);
        bundle.putString("pcb", data);
        message.setData(bundle);
        try {
            this.mMessenger.send(message);
        } catch (RemoteException e) {
            Log.e("InfraredGestureSensorTest", "handleMessage RemoteException :" + e.getMessage());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mSensor = this.mSensorManager.getDefaultSensor(33171014);
        setContentView(2130903124);
        this.mBeforeTestView = findViewById(2131493223);
        this.mAfterTestView = findViewById(2131493229);
        this.mResultTextView = (TextView) findViewById(2131493230);
        this.mLeftTextView = (TextView) findViewById(2131493226);
        this.mRightTextView = (TextView) findViewById(2131493227);
        this.mTopTextView = (TextView) findViewById(2131493224);
        this.mBottomTextView = (TextView) findViewById(2131493228);
        this.mCenterTextView = (TextView) findViewById(2131493225);
        this.mResetDataButton = (Button) findViewById(2131493233);
        this.mResetDataButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                InfraredGestureAgingTest.this.mLeftTextView.setBackgroundResource(2130837524);
                InfraredGestureAgingTest.this.mRightTextView.setBackgroundResource(2130837529);
                InfraredGestureAgingTest.this.mTopTextView.setBackgroundResource(2130837531);
                InfraredGestureAgingTest.this.mBottomTextView.setBackgroundResource(2130837504);
                InfraredGestureAgingTest.this.mCenterTextView.setBackgroundResource(2130837507);
                InfraredGestureAgingTest.this.mLeftTimes = 0;
                InfraredGestureAgingTest.this.mRightTimes = 0;
                InfraredGestureAgingTest.this.mTopTimes = 0;
                InfraredGestureAgingTest.this.mBottomTimes = 0;
                InfraredGestureAgingTest.this.mResultTextView.setText(InfraredGestureAgingTest.this.makeResultText());
            }
        });
        initService();
    }

    protected void onResume() {
        this.mSensorManager.registerListener(this.mSensorListener, this.mSensor, 0);
        super.onResume();
    }

    protected void onPause() {
        this.mSensorManager.unregisterListener(this.mSensorListener);
        this.mHandler.sendEmptyMessage(4097);
        super.onPause();
    }

    private String makeResultText() {
        return String.format(getString(2131297281), new Object[]{Integer.valueOf(this.mLeftTimes), Integer.valueOf(this.mRightTimes), Integer.valueOf(this.mTopTimes), Integer.valueOf(this.mBottomTimes)});
    }

    private void initService() {
        Log.d("InfraredGestureSensorTest", "InfraredGestureAgingTest---initService");
        Intent intent = new Intent("com.oppo.sdcard.command");
        intent.setPackage("com.oneplus.sdcardservice");
        bindService(intent, this.mServiceConnection, 1);
    }

    protected void onDestroy() {
        Log.d("InfraredGestureSensorTest", "InfraredGestureAgingTest---unbindService(mServiceConnection)");
        unbindService(this.mServiceConnection);
        super.onDestroy();
    }
}
