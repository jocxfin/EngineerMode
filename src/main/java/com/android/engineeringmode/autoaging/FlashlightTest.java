package com.android.engineeringmode.autoaging;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;

public class FlashlightTest extends BaseTest {
    private FlashlightHandler mHandler;
    private TextView mInfoView;
    private int mMode;
    private boolean mOpened = false;

    private class FlashlightHandler extends Handler {
        private long mInterval = 1000;

        public void start() {
            sendEmptyMessage(FlashlightTest.this.mMode);
        }

        public void stop() {
            removeMessages(FlashlightTest.this.mMode);
            FlashlightTest.this.close();
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    FlashlightTest.this.open();
                    return;
                default:
                    FlashlightTest.this.blink();
                    sendEmptyMessageDelayed(0, this.mInterval);
                    return;
            }
        }
    }

    public void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setTitle(2131296759);
        setContentView(2130903202);
        this.mInfoView = (TextView) findViewById(2131493540);
        this.mMode = isToggled() ? 1 : 0;
        this.mHandler = new FlashlightHandler();
    }

    protected void runTest() {
        this.mHandler.start();
    }

    protected void endTest() {
        this.mHandler.stop();
    }

    private void open() {
        if (!this.mOpened) {
            this.mOpened = true;
            this.mInfoView.setText(2131296761);
            Light.flashLamp(1);
        }
    }

    private void close() {
        Light.flashLamp(0);
    }

    private void blink() {
        Light.flashLamp(2);
        this.mInfoView.setText(2131296760);
    }
}
