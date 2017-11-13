package com.android.engineeringmode.autoaging;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;

public class KeylightTest extends BaseTest {
    private KeylightHandler mHandler;
    private TextView mInfoView;
    private Mode mMode;

    private class KeylightHandler extends Handler {
        private static final /* synthetic */ int[] -com-android-engineeringmode-autoaging-KeylightTest$ModeSwitchesValues =null;
        private long mInterval = 1000;
        private boolean mOpened;

        private static /* synthetic */ int[] -getcom-android-engineeringmode-autoaging-

        KeylightTest$ModeSwitchesValues() {
            if (-com - android - engineeringmode - autoaging - KeylightTest$ModeSwitchesValues != null) {
                return -com - android - engineeringmode - autoaging - KeylightTest$ModeSwitchesValues;
            }
            int[] iArr = new int[Mode.values().length];
            try {
                iArr[Mode.Always.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Mode.Blink.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            -com - android - engineeringmode - autoaging - KeylightTest$ModeSwitchesValues = iArr;
            return iArr;
        }

        public KeylightHandler() {
            KeylightTest.this.close();
            this.mOpened = false;
        }

        public void start() {
            sendEmptyMessage(0);
        }

        public void stop() {
            removeMessages(0);
            KeylightTest.this.close();
            this.mOpened = false;
        }

        public void handleMessage(Message msg) {
            switch (-getcom - android - engineeringmode - autoaging - KeylightTest$ModeSwitchesValues()[KeylightTest.this.mMode.ordinal()]) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    KeylightTest.this.open();
                    this.mOpened = true;
                    return;
                default:
                    if (this.mOpened) {
                        KeylightTest.this.close();
                        this.mOpened = false;
                    } else {
                        KeylightTest.this.open();
                        this.mOpened = true;
                    }
                    sendEmptyMessageDelayed(0, this.mInterval);
                    return;
            }
        }
    }

    private enum Mode {
        Blink,
        Always
    }

    public void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setTitle(2131296772);
        setContentView(2130903202);
        this.mInfoView = (TextView) findViewById(2131493540);
        this.mMode = isToggled() ? Mode.Always : Mode.Blink;
        this.mHandler = new KeylightHandler();
    }

    protected void runTest() {
        this.mHandler.start();
    }

    protected void endTest() {
        this.mHandler.stop();
    }

    private void open() {
        Light.setElectric(1, Light.MAIN_KEY_MAX);
        this.mInfoView.setText(2131296274);
    }

    private void close() {
        Light.setElectric(1, 0);
        this.mInfoView.setText(2131296275);
    }
}
