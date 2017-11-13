package com.android.engineeringmode.autoaging;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.util.WakeLock;

public abstract class BaseTest extends Activity {
    private long mDuration = -1;
    private Handler mHandler = new AutoAgingHandler();
    private boolean mToggled = false;

    private class AutoAgingHandler extends Handler {
        private AutoAgingHandler() {
        }

        public void handleMessage(Message msg) {
            BaseTest.this.onHandleMessage(msg);
        }
    }

    protected abstract void endTest();

    protected abstract void runTest();

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mToggled = getIntent().getBooleanExtra("test_toggled", false);
        this.mDuration = getIntent().getLongExtra("test_duration", -1);
        onInit(savedInstanceState);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, (int) this.mDuration, 0), 200);
    }

    protected void onSaveInstanceState(Bundle outState) {
    }

    protected void onResume() {
        super.onResume();
        WakeLock.acquire(this);
    }

    protected void onPause() {
        super.onPause();
        WakeLock.release();
    }

    protected void onDestroy() {
        removeAllMessages();
        endTest();
        super.onDestroy();
    }

    protected void onInit(Bundle savedInstanceState) {
        keepScreenOn();
    }

    private void removeAllMessages() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
    }

    public void onBackPressed() {
        endTest();
        setResult(16);
        super.onBackPressed();
    }

    protected void runTest(long timeout) {
        if (timeout >= 0) {
            scheduleEndTest(timeout);
        }
        runTest();
    }

    public final void recordResult(boolean success) {
        setResult(success ? 17 : 18, null);
    }

    public final void keepScreenOn() {
        getWindow().addFlags(128);
    }

    public final long getDuration() {
        return this.mDuration;
    }

    public final boolean isToggled() {
        return this.mToggled;
    }

    private void scheduleEndTest(long delayMillis) {
        this.mHandler.sendEmptyMessageDelayed(2, delayMillis);
    }

    protected void onHandleMessage(Message msg) {
        switch (msg.what) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                long timeout = (long) msg.arg1;
                Log.e("BaseTest", "runTest(), timeout=" + timeout);
                runTest(timeout);
                return;
            case Light.CHARGE_RED_LIGHT /*2*/:
                endTest();
                finish();
                return;
            default:
                return;
        }
    }
}
