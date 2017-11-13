package com.android.engineeringmode;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.Log;
import android.widget.TextView;

import com.android.engineeringmode.autotest.AutoTestItemActivity;
import com.android.engineeringmode.functions.Light;

import java.util.Calendar;

public class TurnOnOffBackLight extends AutoTestItemActivity {
    private int mCurrentTime = 0;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Log.d("TurnOnOffBackLight", "handler msg TRUN_ON");
                TurnOnOffBackLight.this.turnbacklight(true);
                TurnOnOffBackLight.this.mtvShowInfo.setText(2131296823);
                TurnOnOffBackLight.this.removeAllMessage();
                sendEmptyMessageDelayed(1, 1000);
            } else if (1 == msg.what) {
                Log.d("TurnOnOffBackLight", "handler msg TRUN_OFF");
                TurnOnOffBackLight.this.removeAllMessage();
                TurnOnOffBackLight.this.turnbacklight(false);
                TurnOnOffBackLight.this.mtvShowInfo.setText(2131296824);
                TurnOnOffBackLight turnOnOffBackLight = TurnOnOffBackLight.this;
                turnOnOffBackLight.mCurrentTime = turnOnOffBackLight.mCurrentTime + 1;
                if (TurnOnOffBackLight.this.mCurrentTime <= 3) {
                    TurnOnOffBackLight.this.mHandler.sendEmptyMessageDelayed(0, 1000);
                } else if (TurnOnOffBackLight.this.checkIsAutoAging() || TurnOnOffBackLight.this.checkIsAutoTest()) {
                    TurnOnOffBackLight.this.endActivity();
                }
            }
        }
    };
    private int mOldBrightness;
    private int mOldBrightnessMode;
    private boolean mbIsBacklightOn = false;
    private TextView mtvShowInfo = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903216);
        setTitle(2131296822);
        this.mtvShowInfo = (TextView) findViewById(2131493572);
        setRequestedOrientation(1);
    }

    void turnbacklight(boolean turnon) {
        setBrightness(turnon ? Light.MAIN_KEY_MAX : 0);
    }

    protected void onResume() {
        super.onResume();
        getInitBrightness();
        this.mHandler.sendEmptyMessage(0);
    }

    protected void onStop() {
        setBrightness(this.mOldBrightness);
        System.putInt(getContentResolver(), "screen_brightness_mode", this.mOldBrightnessMode);
        this.mbIsBacklightOn = false;
        Calendar now = Calendar.getInstance();
        String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--TurnOnOffBackLight--" + "has entered it";
        removeAllMessage();
        super.onStop();
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

    private boolean setBrightness(int nBrightness) {
        PowerManager pwManager = (PowerManager) getSystemService("power");
        if (pwManager != null) {
            pwManager.setBacklightBrightness(nBrightness);
        }
        return true;
    }

    private void removeAllMessage() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(0);
            this.mHandler.removeMessages(1);
        }
    }
}
