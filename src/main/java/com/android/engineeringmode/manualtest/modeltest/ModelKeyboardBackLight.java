package com.android.engineeringmode.manualtest.modeltest;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.engineeringmode.functions.Light;
import com.oem.util.Feature;

public class ModelKeyboardBackLight extends Activity implements OnClickListener {
    private LinearLayout backLightBackground;
    private boolean backlight_close_pressed = false;
    private boolean backlight_open_pressed = false;
    OnClickListener judgeClickLisenter = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case 2131493015:
                    ModelKeyboardBackLight.this.setResult(1);
                    ModelKeyboardBackLight.this.finish();
                    return;
                case 2131493236:
                    ModelKeyboardBackLight.this.setResult(2);
                    ModelKeyboardBackLight.this.finish();
                    return;
                case 2131493237:
                    ModelKeyboardBackLight.this.setResult(3);
                    ModelKeyboardBackLight.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private PowerManager mPowerManager;
    private Window mWindow;
    private int screenBrightnessMode = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903128);
        initResources();
        this.mWindow = getWindow();
        this.mPowerManager = (PowerManager) getSystemService("power");
    }

    private void initResources() {
        this.backLightBackground = (LinearLayout) findViewById(2131493240);
        this.backLightBackground.setBackgroundColor(-65536);
        ((Button) findViewById(2131493241)).setOnClickListener(this);
        ((Button) findViewById(2131493242)).setOnClickListener(this);
        setLisentenersForJudgeButtons();
        if (!Feature.isButtonLightSupported(this)) {
            this.backLightBackground.setVisibility(8);
            this.backlight_close_pressed = true;
            this.backlight_open_pressed = true;
        }
    }

    private void setLisentenersForJudgeButtons() {
        if (getIntent().getBooleanExtra("model_test", false)) {
            ((ViewStub) findViewById(2131493190)).setVisibility(0);
            ((Button) findViewById(2131493015)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493236)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493237)).setOnClickListener(this.judgeClickLisenter);
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
