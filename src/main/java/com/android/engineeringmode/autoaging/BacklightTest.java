package com.android.engineeringmode.autoaging;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;

public class BacklightTest extends BaseTest {
    private BacklightHandler mHandler = new BacklightHandler();
    private int mOldBrightness;
    private int mOldBrightnessMode;
    private TextView mTextInfo = null;

    private class BacklightHandler extends Handler {
        private Mode mMode;

        private BacklightHandler() {
            this.mMode = Mode.Blink;
        }

        public void setMode(Mode mode) {
            this.mMode = mode;
        }

        public void start() {
            sendEmptyMessage(0);
        }

        public void stop() {
            removeAllMessage();
        }

        private void removeAllMessage() {
            removeMessages(0);
            removeMessages(1);
        }

        public void handleMessage(Message msg) {
            removeAllMessage();
            if (msg.what == 0) {
                BacklightTest.this.turnBacklight(true);
                BacklightTest.this.mTextInfo.setText(2131296823);
                if (this.mMode != Mode.Always) {
                    sendEmptyMessageDelayed(1, 1000);
                }
            } else if (1 == msg.what) {
                BacklightTest.this.turnBacklight(false);
                BacklightTest.this.mTextInfo.setText(2131296824);
                if (this.mMode != Mode.Always) {
                    sendEmptyMessageDelayed(0, 1000);
                }
            }
        }
    }

    private enum Mode {
        Blink,
        Always
    }

    protected void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setContentView(2130903202);
        setTitle(2131296822);
        this.mTextInfo = (TextView) findViewById(2131493540);
        this.mHandler.setMode(isToggled() ? Mode.Always : Mode.Blink);
        getInitBrightness();
    }

    protected void runTest() {
        this.mHandler.start();
    }

    protected void endTest() {
        this.mHandler.stop();
        setBrightness(this.mOldBrightness);
        System.putInt(getContentResolver(), "screen_brightness_mode", this.mOldBrightnessMode);
    }

    private void getInitBrightness() {
        try {
            this.mOldBrightness = System.getInt(getContentResolver(), "screen_brightness");
            this.mOldBrightnessMode = System.getInt(getContentResolver(), "screen_brightness_mode");
            if (this.mOldBrightnessMode == 1) {
                System.putInt(getContentResolver(), "screen_brightness_mode", 0);
            }
        } catch (SettingNotFoundException e) {
            this.mOldBrightness = Light.MAIN_KEY_MAX;
        }
    }

    private void turnBacklight(boolean turnon) {
        setBrightness(turnon ? Light.MAIN_KEY_MAX : 0);
    }

    private boolean setBrightness(int nBrightness) {
        PowerManager pwManager = (PowerManager) getSystemService("power");
        if (pwManager != null) {
            pwManager.setBacklightBrightness(nBrightness);
        }
        return true;
    }
}
