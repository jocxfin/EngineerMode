package com.android.engineeringmode.manualtest.modeltest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.engineeringmode.Log;
import com.android.engineeringmode.audioutils.AudioTestUtils;
import com.android.engineeringmode.util.ExternFunction;

public class ModelEchoTest extends Activity implements OnClickListener {
    private static String prjname;
    private boolean isInThirdMicTest = false;
    private Button mAnc;
    private AudioManager mAudioManager = null;
    private AudioTestUtils mAudioTest;
    private Button mClose;
    private ExternFunction mExFunction;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.i("EchoTest", "Connected");
            }
        }
    };
    private Button mOpen;
    private int mSoundEffectOn = 1;
    private Button pass;

    public Boolean isThreeMicProject() {
        prjname = SystemProperties.get("ro.boot.project_name");
        if (prjname.compareTo("16859") == 0) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903151);
        setTitle(2131296338);
        this.mOpen = (Button) findViewById(2131493077);
        this.mClose = (Button) findViewById(2131493078);
        this.mAnc = (Button) findViewById(2131493079);
        this.mOpen.setOnClickListener(this);
        this.mClose.setOnClickListener(this);
        if (isThreeMicProject().booleanValue()) {
            this.mAnc.setOnClickListener(this);
        }
        this.mOpen.setEnabled(false);
        if (!isThreeMicProject().booleanValue()) {
            this.mAnc.setVisibility(8);
        }
        this.mAudioTest = new AudioTestUtils(this);
        this.mAudioManager = (AudioManager) getSystemService("audio");
        finishMusic(this);
        setRequestedOrientation(1);
        SystemClock.sleep(300);
        try {
            this.mSoundEffectOn = System.getInt(getContentResolver(), "sound_effects_enabled");
            Log.i("EchoTest", "mSoundEffectOn=" + this.mSoundEffectOn);
        } catch (SettingNotFoundException e) {
            Log.e("EchoTest", "get SOUND_EFFECTS_ENABLED state error");
        }
        System.putInt(getContentResolver(), "sound_effects_enabled", 0);
        if (getIntent() != null && getIntent().getAction().equals("com.android.engineeringmode.manualtest.modeltest.ThirdMicTest")) {
            this.mOpen.setVisibility(8);
            this.mClose.setVisibility(8);
            setTitle(2131296342);
            this.isInThirdMicTest = true;
        }
        initResources();
        this.mExFunction = new ExternFunction(this);
        this.mExFunction.registerOnServiceConnected(this.mHandler, 1, null);
    }

    public void startEchoTest() {
        this.mAudioTest.setparameter("LoopBackTest=main");
    }

    public void endEchoTest() {
        this.mAudioTest.setparameter("LoopBackTest=false");
    }

    public void onClick(View v) {
        Log.i("EchoTest", "onClick");
        switch (v.getId()) {
            case 2131493015:
                setResult(1);
                if (this.isInThirdMicTest) {
                    this.mExFunction.setProductLineTestFlagExtraByte(77, (byte) 1);
                }
                finish();
                return;
            case 2131493077:
                this.mOpen.setEnabled(false);
                this.mClose.setEnabled(true);
                this.mAnc.setEnabled(true);
                this.mAudioTest.setparameter("LoopBackTest=main");
                return;
            case 2131493078:
                this.mOpen.setEnabled(true);
                this.mClose.setEnabled(false);
                this.mAnc.setEnabled(true);
                this.mAudioTest.setparameter("LoopBackTest=sec");
                this.pass.setEnabled(true);
                return;
            case 2131493079:
                this.mOpen.setEnabled(true);
                this.mClose.setEnabled(true);
                this.mAnc.setEnabled(false);
                this.mAudioTest.setparameter("LoopBackTest=nsmic");
                if (this.isInThirdMicTest) {
                    this.pass.setEnabled(true);
                    return;
                }
                return;
            case 2131493236:
                this.mOpen.setEnabled(false);
                this.mClose.setEnabled(true);
                this.pass.setEnabled(true);
                this.mAudioTest.setparameter("LoopBackTest=main");
                return;
            case 2131493237:
                setResult(3);
                if (this.isInThirdMicTest) {
                    this.mExFunction.setProductLineTestFlagExtraByte(77, (byte) 2);
                }
                finish();
                return;
            default:
                return;
        }
    }

    protected void onStart() {
        super.onStart();
        if (!this.isInThirdMicTest) {
            startEchoTest();
        }
    }

    protected void onResume() {
        super.onResume();
        System.putInt(getContentResolver(), "sound_effects_enabled", 0);
    }

    protected void onStop() {
        super.onStop();
        if (this.mSoundEffectOn == 1) {
            System.putInt(getContentResolver(), "sound_effects_enabled", 1);
            Log.i("EchoTest", "onPause mSoundEffectOn:" + this.mSoundEffectOn);
        } else if (this.mSoundEffectOn == 0) {
            System.putInt(getContentResolver(), "sound_effects_enabled", 0);
            Log.i("EchoTest", "onPause mSoundEffectOn:" + this.mSoundEffectOn);
        }
        endEchoTest();
        this.mExFunction.unregisterOnServiceConnected(this.mHandler);
        this.mExFunction.dispose();
    }

    private void initResources() {
        this.pass = (Button) findViewById(2131493015);
        this.pass.setEnabled(false);
        this.pass.setOnClickListener(this);
        Button reset = (Button) findViewById(2131493236);
        reset.setOnClickListener(this);
        ((Button) findViewById(2131493237)).setOnClickListener(this);
        if (this.isInThirdMicTest) {
            reset.setVisibility(8);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mAudioManager = null;
    }

    public static final void finishMusic(Context ctx) {
        AudioManager am = (AudioManager) ctx.getSystemService("audio");
        if (am == null) {
            Log.w("EchoTest", "isMusicActive: couldn't get AudioManager reference");
            return;
        }
        if (am.isMusicActive()) {
            Intent intent = new Intent("com.oppo.music.musicservicecommand.pause");
            Log.v("EchoTest", "Broadcast : " + intent.getAction());
            ctx.sendBroadcast(intent);
        } else {
            Log.v("EchoTest", "Music no active.");
        }
    }
}
