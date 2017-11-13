package com.android.engineeringmode;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.Settings.System;
import android.widget.TextView;

import com.android.engineeringmode.autotest.AutoTestItemActivity;

import java.util.Calendar;

public class KeepSrceenOn extends AutoTestItemActivity {
    private int mPreviousSleepTime = 86400000;
    private boolean mbIsKeepScreenOn = false;
    private TextView mtvKeepScreenOn = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903127);
        setTitle(2131296825);
        this.mtvKeepScreenOn = (TextView) findViewById(2131493239);
        this.mbIsKeepScreenOn = getSharedPreferences("key_is_keep_screen_on", 0).getBoolean("key_is_keep_screen_on", false);
        Calendar now = Calendar.getInstance();
        String time = now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13);
        String content;
        if (this.mbIsKeepScreenOn) {
            this.mPreviousSleepTime = getSharedPreferences("key_previous_sleep_time", 0).getInt("key_previous_sleep_time", 15000);
            setScreenSleepTime(this.mPreviousSleepTime);
            content = time + "--KeepSrceenOn--" + "keep_srceen_cancel";
            this.mtvKeepScreenOn.setText(2131296827);
        } else {
            this.mPreviousSleepTime = getScreenSleepTime();
            if (86400000 == this.mPreviousSleepTime) {
                this.mPreviousSleepTime = 15000;
            }
            setScreenSleepTime(86400000);
            content = time + "--LCDContrast--" + "keep_srceen_on";
            this.mtvKeepScreenOn.setText(2131296826);
        }
        setAutoExit(1000);
    }

    protected void onDestroy() {
        Editor passwdfile;
        boolean z = false;
        if (!this.mbIsKeepScreenOn) {
            passwdfile = getSharedPreferences("key_previous_sleep_time", 0).edit();
            if (passwdfile != null) {
                Log.d("KeepSrceenOn", "onDestroy(), save sleep time, mPreviousSleepTime = " + this.mPreviousSleepTime);
                passwdfile.putInt("key_previous_sleep_time", this.mPreviousSleepTime);
                passwdfile.commit();
            }
        }
        passwdfile = getSharedPreferences("key_is_keep_screen_on", 0).edit();
        if (passwdfile != null) {
            String str = "key_is_keep_screen_on";
            if (!this.mbIsKeepScreenOn) {
                z = true;
            }
            passwdfile.putBoolean(str, z);
            passwdfile.commit();
        }
        super.onDestroy();
    }

    private int getScreenSleepTime() {
        try {
            return System.getInt(getContentResolver(), "screen_off_timeout");
        } catch (Exception e) {
            return 15000;
        }
    }

    private void setScreenSleepTime(int nSleepTime) {
        Log.i("KeepSrceenOn", "in setScreenSleepTime(), nSleepTime = " + nSleepTime);
        System.putInt(getContentResolver(), "screen_off_timeout", nSleepTime);
    }
}
