package com.android.engineeringmode.manualtest.modeltest;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.engineeringmode.LumenEventListener;

public class ModelLightSensor extends Activity {
    private LinearLayout backLightBackground;
    OnClickListener judgeClickLisenter = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case 2131493015:
                    ModelLightSensor.this.setResult(1);
                    ModelLightSensor.this.finish();
                    return;
                case 2131493236:
                    ModelLightSensor.this.setResult(2);
                    ModelLightSensor.this.finish();
                    return;
                case 2131493237:
                    ModelLightSensor.this.setResult(3);
                    ModelLightSensor.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private TextView lightInsensity;
    private LinearLayout lightSensorBackground;
    private LumenEventListener mLightListener = null;
    private PowerManager mPowerManager;
    private Window mWindow;
    private int screenBrightnessMode = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903138);
        initResources();
        this.mWindow = getWindow();
        this.mPowerManager = (PowerManager) getSystemService("power");
    }

    private void initResources() {
        this.lightSensorBackground = (LinearLayout) findViewById(2131493326);
        this.lightInsensity = (TextView) findViewById(2131493328);
        this.lightSensorBackground.setBackgroundColor(-65536);
        this.backLightBackground = (LinearLayout) findViewById(2131493240);
        this.backLightBackground.setVisibility(8);
        setLisentenersForJudgeButtons();
    }

    private void setLisentenersForJudgeButtons() {
        if (getIntent().getBooleanExtra("model_test", false)) {
            ((ViewStub) findViewById(2131493190)).setVisibility(0);
            ((Button) findViewById(2131493015)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493236)).setOnClickListener(this.judgeClickLisenter);
            ((Button) findViewById(2131493237)).setOnClickListener(this.judgeClickLisenter);
        }
    }

    protected void onResume() {
        super.onResume();
        this.mLightListener = new LumenEventListener(this) {
            public void onLumenChanged(int lumen) {
                Log.w("ModelLightSensor", "light = " + lumen);
                if (lumen <= 10) {
                    ModelLightSensor.this.setWinBrightness(ModelLightSensor.this.mPowerManager.getMinimumScreenBrightnessSetting());
                    ModelLightSensor.this.lightSensorBackground.setBackgroundColor(-16777216);
                } else {
                    ModelLightSensor.this.setWinBrightness(ModelLightSensor.this.mPowerManager.getMaximumScreenBrightnessSetting());
                }
                ModelLightSensor.this.lightInsensity.setText(String.valueOf(lumen));
            }
        };
        this.mLightListener.enable();
    }

    private void setWinBrightness(int brightness) {
        LayoutParams attr = this.mWindow.getAttributes();
        float fbrightnessValue = ((float) brightness) / 255.0f;
        if (attr.screenBrightness != fbrightnessValue) {
            attr.screenBrightness = fbrightnessValue;
            this.mWindow.setAttributes(attr);
        }
    }

    protected void onPause() {
        super.onPause();
        this.mLightListener.disable();
    }
}
