package com.android.engineeringmode.manualtest.modeltest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.engineeringmode.LumenEventListener;
import com.android.engineeringmode.functions.Light;

public class LightSensorAndBackLight extends Activity implements OnClickListener {
    private final String BUTTON_LIGHT_INTENT = "com.android.engineeringmode.manualtest.modeltest.ButtonLight";
    private final String LIGHT_SENSOR_INTENT = "com.android.engineeringmode.manualtest.modeltest.LightSensor";
    private final String LIGHT_SENSOR_INTENT1 = "com.android.engineeringmode.manualtest.modeltest.ModelLightSensor";
    private LinearLayout backLightBackground;
    private boolean backlight_close_pressed = false;
    private boolean backlight_open_pressed = false;
    OnClickListener judgeClickLisenter = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case 2131493015:
                    LightSensorAndBackLight.this.setResult(1);
                    LightSensorAndBackLight.this.finish();
                    return;
                case 2131493236:
                    LightSensorAndBackLight.this.setResult(2);
                    LightSensorAndBackLight.this.finish();
                    return;
                case 2131493237:
                    LightSensorAndBackLight.this.setResult(3);
                    LightSensorAndBackLight.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private TextView lightInsensity;
    private LinearLayout lightSensorBackground;
    private LumenEventListener mLightListener = null;
    private int screenBrightnessMode = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903138);
        initResources();
    }

    private void initResources() {
        this.lightSensorBackground = (LinearLayout) findViewById(2131493326);
        this.lightInsensity = (TextView) findViewById(2131493328);
        this.lightSensorBackground.setBackgroundColor(-65536);
        this.backLightBackground = (LinearLayout) findViewById(2131493240);
        this.backLightBackground.setBackgroundColor(-65536);
        ((Button) findViewById(2131493241)).setOnClickListener(this);
        ((Button) findViewById(2131493242)).setOnClickListener(this);
        setLisentenersForJudgeButtons();
    }

    private void setLisentenersForJudgeButtons() {
        boolean isInModelTest = getIntent().getBooleanExtra("model_test", false);
        Log.d("LightSensorAndBackLight", getIntent().getAction());
        if (getIntent().getAction().equals("com.android.engineeringmode.manualtest.modeltest.ButtonLight")) {
            this.lightSensorBackground.setVisibility(4);
        } else if (getIntent().getAction().equals("com.android.engineeringmode.manualtest.modeltest.LightSensor") || getIntent().getAction().equals("com.android.engineeringmode.manualtest.modeltest.ModelLightSensor")) {
            this.backLightBackground.setVisibility(4);
            isInModelTest = false;
        }
        if (isInModelTest) {
            ((ViewStub) findViewById(2131493190)).setVisibility(0);
            ((Button) findViewById(2131493015)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493236)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493237)).setOnClickListener(this.judgeClickLisenter);
        }
    }

    protected void onResume() {
        super.onResume();
        setScreenBrightnessMode();
        this.mLightListener = new LumenEventListener(this) {
            public void onLumenChanged(int lumen) {
                Log.w("LightSensorAndBackLight", "light = " + lumen);
                if (lumen <= 10) {
                    LightSensorAndBackLight.this.lightSensorBackground.setBackgroundColor(-16777216);
                }
                LightSensorAndBackLight.this.lightInsensity.setText(String.valueOf(lumen));
                if (LightSensorAndBackLight.this.getIntent().getBooleanExtra("model_test", false) && lumen < 5) {
                    LightSensorAndBackLight.this.setResult(1);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            LightSensorAndBackLight.this.finish();
                        }
                    }, 2000);
                }
            }
        };
        this.mLightListener.enable();
    }

    private void setScreenBrightnessMode() {
        try {
            this.screenBrightnessMode = System.getInt(getContentResolver(), "screen_brightness_mode");
            Log.d("LightSensorAndBackLight", "setScreenBrightnessMode mode before put" + this.screenBrightnessMode);
            if (this.screenBrightnessMode == 1) {
                Log.d("LightSensorAndBackLight", "setScreenBrightnessMode try to put 0");
                System.putInt(getContentResolver(), "screen_brightness_mode", 0);
            }
            Log.d("LightSensorAndBackLight", "setScreenBrightnessMode mode after put" + System.getInt(getContentResolver(), "screen_brightness_mode"));
        } catch (SettingNotFoundException e) {
        }
    }

    protected void onPause() {
        super.onPause();
        resetScreenBrightnessMode();
        this.mLightListener.disable();
    }

    private void resetScreenBrightnessMode() {
        if (this.screenBrightnessMode == 1) {
            System.putInt(getContentResolver(), "screen_brightness_mode", 1);
            this.screenBrightnessMode = 0;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131493241:
                this.backlight_close_pressed = true;
                Light.setElectric(1, 0);
                if (this.backlight_close_pressed && this.backlight_open_pressed) {
                    this.backLightBackground.setBackgroundColor(-16777216);
                    return;
                }
                return;
            case 2131493242:
                this.backlight_open_pressed = true;
                Light.setElectric(1, Light.MAIN_KEY_MAX);
                if (this.backlight_close_pressed && this.backlight_open_pressed) {
                    this.backLightBackground.setBackgroundColor(-16777216);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
