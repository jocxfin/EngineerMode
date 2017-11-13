package com.android.engineeringmode.LCDtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.android.engineeringmode.Log;

public class LCDContrast extends Activity implements OnSeekBarChangeListener, OnClickListener {
    private int DEFAULT_BACKLIGHT;
    private int MAXIMUM_BACKLIGHT;
    private int MINIMUM_BACKLIGHT;
    private boolean isInModelTest = false;
    private int mDestinyBrightness;
    private int mOldBrightness;
    private PowerManager mPowerManager;
    private Window mWin;
    private SeekBar msekBrightness;
    private TextView mtvInfo;

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mWin = getWindow();
        setContentView(2130903137);
        setTitle(2131296776);
        this.mPowerManager = (PowerManager) getSystemService("power");
        this.MINIMUM_BACKLIGHT = this.mPowerManager.getMinimumScreenBrightnessSetting();
        this.MAXIMUM_BACKLIGHT = this.mPowerManager.getMaximumScreenBrightnessSetting();
        this.DEFAULT_BACKLIGHT = this.mPowerManager.getDefaultScreenBrightnessSetting();
        this.mDestinyBrightness = this.mPowerManager.getDefaultScreenBrightnessSetting();
        this.msekBrightness = (SeekBar) findViewById(2131493321);
        this.msekBrightness.setMax(this.MAXIMUM_BACKLIGHT - this.MINIMUM_BACKLIGHT);
        this.msekBrightness.setOnSeekBarChangeListener(this);
        this.mtvInfo = (TextView) findViewById(2131493320);
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
        initResources();
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void initResources() {
        Button pass = (Button) findViewById(2131493015);
        pass.setOnClickListener(this);
        Button reset = (Button) findViewById(2131493236);
        reset.setOnClickListener(this);
        Button fail = (Button) findViewById(2131493237);
        fail.setOnClickListener(this);
        ((Button) findViewById(2131493322)).setOnClickListener(this);
        ((Button) findViewById(2131493323)).setOnClickListener(this);
        Button mSelectButton = (Button) findViewById(2131493325);
        mSelectButton.setOnClickListener(this);
        ((Button) findViewById(2131493324)).setOnClickListener(this);
        if (this.isInModelTest) {
            mSelectButton.setVisibility(8);
            return;
        }
        pass.setVisibility(8);
        reset.setVisibility(8);
        fail.setVisibility(8);
    }

    private void setScreenMode(int value) {
        System.putInt(getContentResolver(), "screen_brightness_mode", value);
    }

    public void onClick(View v) {
        Log.i("LCDContrast", "onClick");
        switch (v.getId()) {
            case 2131493015:
                setResult(1);
                finish();
                return;
            case 2131493236:
                setResult(2);
                finish();
                return;
            case 2131493237:
                setResult(3);
                finish();
                return;
            case 2131493322:
                this.msekBrightness.setProgress(this.msekBrightness.getProgress() + 1);
                return;
            case 2131493323:
                this.msekBrightness.setProgress(this.msekBrightness.getProgress() - 1);
                return;
            case 2131493324:
                setScreenMode(0);
                this.msekBrightness.setProgress(this.DEFAULT_BACKLIGHT - this.MINIMUM_BACKLIGHT);
                setBrightness(this.DEFAULT_BACKLIGHT);
                return;
            case 2131493325:
                setBrightness(this.mDestinyBrightness);
                finish();
                return;
            default:
                return;
        }
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == this.msekBrightness) {
            if (this.MAXIMUM_BACKLIGHT - this.MINIMUM_BACKLIGHT != 0) {
                this.mtvInfo.setText(getString(2131296780) + (this.MINIMUM_BACKLIGHT + progress));
            }
            Log.i("LCDContrast", "onProgressChanged : progress = " + progress);
            setCurrentWindowBrightness(this.MINIMUM_BACKLIGHT + progress);
        }
    }

    private void setCurrentWindowBrightness(int level) {
        this.mDestinyBrightness = level;
        LayoutParams localLayoutParams = this.mWin.getAttributes();
        localLayoutParams.screenBrightness = ((float) level) / 255.0f;
        this.mWin.setAttributes(localLayoutParams);
    }

    protected void onResume() {
        try {
            this.mOldBrightness = System.getInt(getContentResolver(), "screen_brightness");
        } catch (SettingNotFoundException e) {
            this.mOldBrightness = this.MAXIMUM_BACKLIGHT;
        }
        this.mtvInfo.setText(getString(2131296780) + this.mOldBrightness);
        this.msekBrightness.setProgress(this.mOldBrightness - this.MINIMUM_BACKLIGHT);
        setCurrentWindowBrightness(this.mOldBrightness);
        super.onResume();
    }

    private void setBrightness(int nBrightness) {
        Log.i("LCDContrast", "confirm setBrightness : " + nBrightness);
        if (nBrightness != this.mOldBrightness) {
            System.putInt(getContentResolver(), "screen_brightness", nBrightness);
        }
    }
}
