package com.android.engineeringmode.autoaging;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.android.engineeringmode.call.CallTestService;
import com.android.engineeringmode.call.CallTestService.ExitCallBack;
import com.android.engineeringmode.call.CallTestService.LocalBinder;
import com.android.engineeringmode.functions.Light;

public class CallTest extends BaseTest {
    private static final String[] number_array = new String[]{"10000", "10010", "10086", "112"};
    private static final String[] sNumbers = new String[]{"112", "10086", "10010", "10000"};
    private LocalServiceConnection conn = null;
    private AudioManager mAudioManager = null;
    private CallTestService mCallTestService = null;
    private int mDialCount = 0;
    private String mExtraNumber = "";
    private TelephonyManager mTelephonyManager;
    private TestHandler mTestHandler;
    private TextView mTextInfo;

    private class LocalServiceConnection implements ServiceConnection {
        public void onServiceDisconnected(ComponentName name) {
            Log.i("CallTest", "onServiceConnected");
            CallTest.this.mCallTestService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("CallTest", "onServiceConnected");
            CallTest.this.mCallTestService = ((LocalBinder) service).getService();
            CallTest.this.mCallTestService.setAutoTestMode(true);
            CallTest.this.mCallTestService.setExitCallBack(CallTest.this.mTestHandler);
            CallTest.this.dialNumberEx();
        }
    }

    private class TestHandler extends Handler implements ExitCallBack {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    Log.i("CallTest", "CALLTEST_TIMEOUT_EVENT handled");
                    exit();
                    return;
                default:
                    return;
            }
        }

        public void exit() {
            CallTest.this.setCurInfo("STOP CALL TEST!");
            CallTest.this.endTest();
            CallTest.this.setResult(32);
            CallTest.this.finish();
        }
    }

    public void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setTitle(2131296932);
        setContentView(2130903057);
        this.mTextInfo = (TextView) findViewById(2131492928);
        this.mTelephonyManager = (TelephonyManager) getSystemService("phone");
        this.mAudioManager = (AudioManager) getSystemService("audio");
        this.mTestHandler = new TestHandler();
    }

    public void runTest() {
        bindService();
    }

    public void endTest() {
        if (this.mTestHandler != null && this.mTestHandler.hasMessages(1)) {
            this.mTestHandler.removeMessages(1);
        }
        if (this.mCallTestService != null) {
            this.mCallTestService.endCall();
        }
        unBind();
    }

    private String getNetxtNumber() {
        int size = number_array.length;
        if (this.mTelephonyManager.getSimState() != 5) {
            Log.i("CallTest", "getNetxtNumber sim state = " + this.mTelephonyManager.getSimState());
            return "112";
        }
        Log.i("CallTest", "getNetxtNumber mDialCount = " + this.mDialCount);
        return number_array[this.mDialCount % size];
    }

    private void setCurInfo(String text) {
        if (this.mTextInfo != null) {
            this.mTextInfo.setText(text);
        }
    }

    private void bindService() {
        this.conn = new LocalServiceConnection();
        bindService(new Intent(this, CallTestService.class), this.conn, 1);
    }

    private void unBind() {
        if (this.conn != null) {
            unbindService(this.conn);
            this.conn = null;
        }
    }

    private void dialNumberEx() {
        String current_num = getNetxtNumber();
        if (this.mCallTestService != null) {
            this.mCallTestService.dialNumber(current_num);
            setCurInfo("dialing : " + current_num);
            this.mDialCount++;
        }
    }

    protected void onResume() {
        super.onResume();
        SystemClock.sleep(500);
        TelecomManager tm = TelecomManager.from(this);
        if (tm == null || !(tm.isRinging() || tm.isInCall())) {
            Log.w("CallTest", "onResume");
            if (this.mTestHandler != null && this.mTestHandler.hasMessages(1)) {
                this.mTestHandler.removeMessages(1);
            }
            if (this.mCallTestService != null) {
                this.mCallTestService.endCall();
            }
            dialNumberEx();
            if (this.mTestHandler != null) {
                this.mTestHandler.sendEmptyMessageDelayed(1, 30000);
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.w("CallTest", "onDestroy");
        if (this.mAudioManager != null) {
            this.mAudioManager.setMode(0);
        }
    }
}
