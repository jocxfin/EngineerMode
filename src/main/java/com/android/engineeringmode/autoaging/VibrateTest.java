package com.android.engineeringmode.autoaging;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.widget.TextView;

public class VibrateTest extends BaseTest {
    private Handler mHandler = new VibratorHandler();
    private Vibrator mVibrator;

    private class VibratorHandler extends Handler {
        private VibratorHandler() {
        }

        public void handleMessage(Message msg) {
            if (1 == msg.what && VibrateTest.this.mVibrator != null) {
                VibrateTest.this.mVibrator.cancel();
                VibrateTest.this.mVibrator.vibrate(2000);
                VibrateTest.this.mHandler.sendEmptyMessageDelayed(1, 3000);
            }
        }
    }

    protected void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setTitle(2131296819);
        setContentView(2130903202);
        ((TextView) findViewById(2131493540)).setText(2131296820);
        this.mVibrator = (Vibrator) getSystemService("vibrator");
    }

    protected void runTest() {
        this.mHandler.sendEmptyMessage(1);
    }

    protected void endTest() {
        this.mHandler.removeMessages(1);
        this.mVibrator.cancel();
    }
}
