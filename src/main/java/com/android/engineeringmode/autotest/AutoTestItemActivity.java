package com.android.engineeringmode.autotest;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;

import com.android.engineeringmode.KeepScreenOnActivity;
import com.android.engineeringmode.Log;

public class AutoTestItemActivity extends KeepScreenOnActivity {
    private AudioManager mAudioManager = null;
    private ExitHandler mExitHandler = null;
    private int mSoundEffectOn = 1;
    private boolean mbCanManualExit = true;
    private boolean mbIsAutoAging = false;
    private boolean mbIsAutoTest = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intnt = getIntent();
        Log.d("AutoTestItemActivity", "AutoTestItemActivity--onCreate");
        if (intnt != null) {
            if (intnt.hasExtra("key_if_can_manual_exited")) {
                this.mbCanManualExit = intnt.getBooleanExtra("key_if_can_manual_exited", false);
                Log.d("AutoTestItemActivity", "KEY_CAN_MANUAL_EXIT=" + this.mbCanManualExit);
            }
            if (intnt.hasExtra("key_is_autotest")) {
                this.mbIsAutoTest = intnt.getBooleanExtra("key_is_autotest", false);
                Log.d("AutoTestItemActivity", "KEY_ISAUTOTEST=" + this.mbIsAutoTest);
            }
            if (intnt.hasExtra("key_is_autotest")) {
                this.mbIsAutoAging = intnt.getBooleanExtra("key_is_autoaging", false);
                Log.d("AutoTestItemActivity", "KEY_ISAUTOTEST=" + this.mbIsAutoAging);
            }
        }
        this.mExitHandler = new ExitHandler(this);
        try {
            this.mSoundEffectOn = System.getInt(getContentResolver(), "sound_effects_enabled");
            Log.i("AutoTestItemActivity", "mSoundEffectOn=" + this.mSoundEffectOn);
        } catch (SettingNotFoundException e) {
            Log.e("AutoTestItemActivity", "get SOUND_EFFECTS_ENABLED state error");
        }
        this.mAudioManager = (AudioManager) getSystemService("audio");
        System.putInt(getContentResolver(), "sound_effects_enabled", 0);
    }

    public void onBackPressed() {
        if (!this.mbCanManualExit) {
            Log.d("AutoTestItemActivity", "AutoTestItemActivity--onBackPressed--mbCanManualExit=false");
        } else if (checkIsAutoAging()) {
            endTest();
        } else {
            Log.d("AutoTestItemActivity", "AutoTestItemActivity--onBackPressed--mbCanManualExit=true");
            endActivity();
        }
    }

    protected void onResume() {
        System.putInt(getContentResolver(), "sound_effects_enabled", 0);
        Log.i("AutoTestItemActivity", "onResume");
        super.onResume();
    }

    protected void onPause() {
        if (this.mSoundEffectOn == 1) {
            System.putInt(getContentResolver(), "sound_effects_enabled", 1);
            Log.i("AutoTestItemActivity", "onPause mSoundEffectOn:" + this.mSoundEffectOn);
        } else if (this.mSoundEffectOn == 0) {
            System.putInt(getContentResolver(), "sound_effects_enabled", 0);
            Log.i("AutoTestItemActivity", "onPause mSoundEffectOn:" + this.mSoundEffectOn);
        }
        super.onPause();
    }

    protected void onDestroy() {
        if (this.mExitHandler != null) {
            this.mExitHandler.removeMessages(1);
            this.mExitHandler = null;
        }
        super.onDestroy();
    }

    protected void endActivity() {
        if (this.mExitHandler != null) {
            this.mExitHandler.removeMessages(1);
        }
        Log.d("AutoTestItemActivity", "endActivity(), intent = " + getIntent().getAction());
        finish();
    }

    protected void endTest() {
        Log.d("AutoTestItemActivity", "endTest()-------------");
        setResult(32);
        finish();
    }

    public void setAutoExit(int delay) {
        if (this.mExitHandler != null) {
            this.mExitHandler.removeMessages(1);
            this.mExitHandler.sendEmptyMessageDelayed(1, (long) delay);
        }
    }

    public boolean checkIsAutoTest() {
        return this.mbIsAutoTest;
    }

    public boolean checkIsAutoAging() {
        return this.mbIsAutoAging;
    }
}
