package com.android.engineeringmode.autotest;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.engineeringmode.manualtest.ManualTest;

public class PassUnpassActivity extends Activity implements OnClickListener {
    private AudioManager mAudioManager = null;
    private int mSoundEffectOn = 1;
    private Button mbtnExit = null;
    private Button mbtnPass = null;
    private Button mbtnRetest = null;
    private Button mbtnUnpass = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903176);
        setTitle(2131296277);
        this.mbtnPass = (Button) findViewById(2131493474);
        this.mbtnPass.setOnClickListener(this);
        this.mbtnUnpass = (Button) findViewById(2131493475);
        this.mbtnUnpass.setOnClickListener(this);
        this.mbtnRetest = (Button) findViewById(2131493476);
        this.mbtnRetest.setOnClickListener(this);
        this.mbtnExit = (Button) findViewById(2131493477);
        this.mbtnExit.setOnClickListener(this);
        ManualTest.requestFoucs(this.mbtnPass);
        setRequestedOrientation(1);
        this.mAudioManager = (AudioManager) getSystemService("audio");
        try {
            this.mSoundEffectOn = System.getInt(getContentResolver(), "sound_effects_enabled");
            Log.i("PassUnpassActivity", "mSoundEffectOn=" + this.mSoundEffectOn);
        } catch (SettingNotFoundException e) {
            Log.e("PassUnpassActivity", "get SOUND_EFFECTS_ENABLED state error");
        }
        System.putInt(getContentResolver(), "sound_effects_enabled", 0);
    }

    public void onClick(View v) {
        if (v == this.mbtnPass) {
            endActivity(true, false);
        } else if (v == this.mbtnUnpass) {
            endActivity(false, false);
        } else if (2131493476 == v.getId()) {
            endActivity(true, true);
        } else if (2131493477 == v.getId()) {
            finish();
        }
    }

    public void onBackPressed() {
    }

    protected void onPause() {
        Log.i("PassUnpassActivity", "onPause");
        if (this.mSoundEffectOn == 1) {
            System.putInt(getContentResolver(), "sound_effects_enabled", 1);
            Log.i("PassUnpassActivity", "onPause mSoundEffectOn:" + this.mSoundEffectOn);
        } else if (this.mSoundEffectOn == 0) {
            System.putInt(getContentResolver(), "sound_effects_enabled", 0);
            Log.i("PassUnpassActivity", "onPause mSoundEffectOn:" + this.mSoundEffectOn);
        }
        super.onPause();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, 2131296278);
        menu.add(0, 2, 0, 2131296698);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (1 == item.getItemId()) {
            endActivity(true, true);
            return true;
        } else if (2 != item.getItemId()) {
            return super.onOptionsItemSelected(item);
        } else {
            finish();
            return true;
        }
    }

    private void endActivity(boolean bSuc, boolean bRetest) {
        Intent intnt = new Intent();
        intnt.putExtra("key_isPassed", bSuc);
        intnt.putExtra("key_isneedretest", bRetest);
        setResult(-1, intnt);
        finish();
    }
}
