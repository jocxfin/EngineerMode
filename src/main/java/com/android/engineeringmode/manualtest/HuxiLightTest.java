package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.engineeringmode.functions.Light;
import com.oem.util.Feature;
import com.oem.util.Utils;

import java.util.Calendar;

public class HuxiLightTest extends Activity implements OnCheckedChangeListener {
    private boolean isRgbIoctl = false;
    OnClickListener judgeClickLisenter = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case 2131493015:
                    HuxiLightTest.this.setResult(1);
                    HuxiLightTest.this.finish();
                    return;
                case 2131493236:
                    HuxiLightTest.this.setResult(2);
                    HuxiLightTest.this.finish();
                    return;
                case 2131493237:
                    HuxiLightTest.this.setResult(3);
                    HuxiLightTest.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    private ToggleButton mBlueLight;
    private Button mFlashBlueButton;
    private Button mFlashGreenButton;
    private Button mFlashRedButton;
    private ToggleButton mGreenLight;
    private LinearLayout mMasterLayout;
    private ToggleButton mMasterSwitch;
    private ToggleButton mRedLight;
    private SeekBar msekBlueBrightness;
    private SeekBar msekGreenBrightness;
    private SeekBar msekRedBrightness;
    private TextView mtvBlueInfo;
    private TextView mtvGreenInfo;
    private TextView mtvRedInfo;
    private int screenBrightnessMode = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903120);
        Calendar now = Calendar.getInstance();
        String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--HuxiLightTest--" + "has entered it";
        this.isRgbIoctl = Feature.isBreathingLightICSupported();
        Log.e("HuxiLightTest", "isRgbIoctl: " + this.isRgbIoctl);
        this.mRedLight = (ToggleButton) findViewById(2131493206);
        this.mRedLight.setOnCheckedChangeListener(this);
        this.mGreenLight = (ToggleButton) findViewById(2131493208);
        this.mGreenLight.setOnCheckedChangeListener(this);
        this.mBlueLight = (ToggleButton) findViewById(2131493209);
        this.mBlueLight.setOnCheckedChangeListener(this);
        this.mMasterSwitch = (ToggleButton) findViewById(2131493204);
        this.mMasterSwitch.setOnCheckedChangeListener(this);
        this.mMasterLayout = (LinearLayout) findViewById(2131493203);
        this.mMasterLayout.setVisibility(8);
        this.mFlashRedButton = (Button) findViewById(2131493216);
        this.mFlashRedButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                HuxiLightTest.this.mGreenLight.setChecked(false);
                HuxiLightTest.this.mBlueLight.setChecked(false);
                HuxiLightTest.this.mRedLight.setChecked(false);
                HuxiLightTest.this.msekRedBrightness.setEnabled(false);
                HuxiLightTest.this.msekGreenBrightness.setEnabled(false);
                HuxiLightTest.this.msekBlueBrightness.setEnabled(false);
                if (HuxiLightTest.this.isRgbIoctl) {
                    Light.closeHuxiLight();
                    Light.setLightFlash(1);
                    return;
                }
                Light.setBlink(1, 0);
                Light.setBlink(2, 0);
                Light.setBlink(3, 0);
                Light.setBlink(1, 1);
            }
        });
        this.mFlashGreenButton = (Button) findViewById(2131493217);
        this.mFlashGreenButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                HuxiLightTest.this.mGreenLight.setChecked(false);
                HuxiLightTest.this.mBlueLight.setChecked(false);
                HuxiLightTest.this.mRedLight.setChecked(false);
                HuxiLightTest.this.msekRedBrightness.setEnabled(false);
                HuxiLightTest.this.msekGreenBrightness.setEnabled(false);
                HuxiLightTest.this.msekBlueBrightness.setEnabled(false);
                if (HuxiLightTest.this.isRgbIoctl) {
                    Light.closeHuxiLight();
                    Light.setLightFlash(2);
                    return;
                }
                Light.setBlink(1, 0);
                Light.setBlink(2, 0);
                Light.setBlink(3, 0);
                Light.setBlink(2, 1);
            }
        });
        this.mFlashBlueButton = (Button) findViewById(2131493218);
        this.mFlashBlueButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                HuxiLightTest.this.mGreenLight.setChecked(false);
                HuxiLightTest.this.mBlueLight.setChecked(false);
                HuxiLightTest.this.mRedLight.setChecked(false);
                HuxiLightTest.this.msekRedBrightness.setEnabled(false);
                HuxiLightTest.this.msekGreenBrightness.setEnabled(false);
                HuxiLightTest.this.msekBlueBrightness.setEnabled(false);
                if (Utils.isOppoFind7s() || Utils.isOppoFind7sWCDMA() || Utils.isOppoN5207() || Utils.isOppoFind7sExp() || Utils.isOppoN5206()) {
                    Light.openBreatheLight(true);
                } else if (HuxiLightTest.this.isRgbIoctl) {
                    Light.closeHuxiLight();
                    Light.setLightFlash(3);
                } else {
                    Light.setBlink(1, 0);
                    Light.setBlink(2, 0);
                    Light.setBlink(3, 0);
                    Light.setBlink(3, 1);
                }
            }
        });
        this.msekRedBrightness = (SeekBar) findViewById(2131493211);
        this.msekRedBrightness.setMax(Light.MAIN_KEY_MAX);
        this.msekGreenBrightness = (SeekBar) findViewById(2131493213);
        this.msekGreenBrightness.setMax(Light.MAIN_KEY_MAX);
        this.msekBlueBrightness = (SeekBar) findViewById(2131493215);
        this.msekBlueBrightness.setMax(Light.MAIN_KEY_MAX);
        this.msekRedBrightness.setProgress(120);
        this.msekGreenBrightness.setProgress(120);
        this.msekBlueBrightness.setProgress(120);
        this.msekRedBrightness.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                HuxiLightTest.this.mtvRedInfo.setText(HuxiLightTest.this.getString(2131297203) + ((int) (((((float) progress) / 255.0f) * 1000.0f) / 10.0f)) + "%");
                if (HuxiLightTest.this.isRgbIoctl) {
                    Light.setHuxiRedBrightness(progress + 0);
                } else {
                    Light.setBrightness(1, progress + 0);
                }
            }
        });
        this.msekGreenBrightness.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                HuxiLightTest.this.mtvGreenInfo.setText(HuxiLightTest.this.getString(2131297204) + ((int) (((((float) progress) / 255.0f) * 1000.0f) / 10.0f)) + "%");
                if (HuxiLightTest.this.isRgbIoctl) {
                    Light.setHuxiGreenBrightness(progress + 0);
                } else {
                    Light.setBrightness(2, progress + 0);
                }
            }
        });
        this.msekBlueBrightness.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                HuxiLightTest.this.mtvBlueInfo.setText(HuxiLightTest.this.getString(2131297205) + ((int) (((((float) progress) / 255.0f) * 1000.0f) / 10.0f)) + "%");
                if (Utils.isOppoFind7s() || Utils.isOppoFind7sWCDMA() || Utils.isOppoN5207() || Utils.isOppoFind7sExp() || Utils.isOppoN5206()) {
                    Light.setBreatheLightBrightness(progress + 0);
                } else if (HuxiLightTest.this.isRgbIoctl) {
                    Light.setHuxiBlueBrightness(progress + 0);
                } else {
                    Light.setBrightness(3, progress + 0);
                }
            }
        });
        this.mtvRedInfo = (TextView) findViewById(2131493210);
        this.mtvGreenInfo = (TextView) findViewById(2131493212);
        this.mtvBlueInfo = (TextView) findViewById(2131493214);
        if (Utils.isOppoFind7() || Utils.isOppoFind7s() || Utils.isOppoFind7sWCDMA() || Utils.isOppoFind7WCDMA() || Utils.isOppoN5207() || Utils.isOppoFind7Exp() || Utils.isOppoFind7sExp() || Utils.isOppoN5206()) {
            this.mtvRedInfo.setVisibility(8);
            this.msekRedBrightness.setVisibility(8);
            this.mtvGreenInfo.setVisibility(8);
            this.msekGreenBrightness.setVisibility(8);
            findViewById(2131493203).setVisibility(8);
            findViewById(2131493205).setVisibility(8);
            findViewById(2131493207).setVisibility(8);
            this.mFlashRedButton.setVisibility(8);
            this.mFlashGreenButton.setVisibility(8);
        }
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
        this.msekRedBrightness.setEnabled(false);
        this.msekGreenBrightness.setEnabled(false);
        this.msekBlueBrightness.setEnabled(false);
        if (SystemProperties.getBoolean("sys.debug.breathe", false)) {
            this.mMasterSwitch.setChecked(true);
        }
        if (Utils.isOppoFind7s() || Utils.isOppoFind7sWCDMA() || Utils.isOppoN5207() || Utils.isOppoFind7sExp() || Utils.isOppoN5206()) {
            Light.closeBreatheLight();
        } else if (this.isRgbIoctl) {
            Light.closeHuxiLight();
        } else {
            Light.setBrightness(1, 0);
            Light.setBrightness(2, 0);
            Light.setBrightness(3, 0);
        }
        setScreenBrightnessMode();
    }

    private void setScreenBrightnessMode() {
        try {
            this.screenBrightnessMode = System.getInt(getContentResolver(), "screen_brightness_mode");
            Log.d("HuxiLightTest", "setScreenBrightnessMode mode before put" + this.screenBrightnessMode);
            if (this.screenBrightnessMode == 1) {
                Log.d("HuxiLightTest", "setScreenBrightnessMode try to put 0");
                System.putInt(getContentResolver(), "screen_brightness_mode", 0);
            }
            Log.d("HuxiLightTest", "setScreenBrightnessMode mode after put" + System.getInt(getContentResolver(), "screen_brightness_mode"));
        } catch (SettingNotFoundException e) {
        }
    }

    protected void onPause() {
        super.onPause();
        resetScreenBrightnessMode();
    }

    private void resetScreenBrightnessMode() {
        if (this.screenBrightnessMode == 1) {
            System.putInt(getContentResolver(), "screen_brightness_mode", 1);
            this.screenBrightnessMode = 0;
        }
    }

    protected void onStop() {
        super.onStop();
        this.mGreenLight.setChecked(false);
        this.mBlueLight.setChecked(false);
        this.mRedLight.setChecked(false);
        this.msekRedBrightness.setEnabled(false);
        this.msekGreenBrightness.setEnabled(false);
        this.msekBlueBrightness.setEnabled(false);
        if (Utils.isOppoFind7s() || Utils.isOppoFind7sWCDMA() || Utils.isOppoN5207() || Utils.isOppoFind7sExp() || Utils.isOppoN5206()) {
            Light.closeBreatheLight();
        } else if (this.isRgbIoctl) {
            Light.closeHuxiLight();
        } else {
            Light.setBrightness(1, 0);
            Light.setBrightness(2, 0);
            Light.setBrightness(3, 0);
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case 2131493204:
                turnOffHuxiLight(isChecked);
                return;
            case 2131493206:
                if (isChecked) {
                    if (this.isRgbIoctl) {
                        Light.openHuxiLight(1);
                    } else {
                        Light.setBrightness(1, 0);
                        Light.setBrightness(1, 120);
                    }
                    this.msekRedBrightness.setEnabled(true);
                    return;
                }
                this.msekRedBrightness.setEnabled(false);
                if (this.isRgbIoctl) {
                    Light.closeHuxiLight();
                    return;
                } else {
                    Light.setBrightness(1, 0);
                    return;
                }
            case 2131493208:
                if (isChecked) {
                    if (this.isRgbIoctl) {
                        Light.openHuxiLight(2);
                    } else {
                        Light.setBrightness(2, 0);
                        Light.setBrightness(2, 120);
                    }
                    this.msekGreenBrightness.setEnabled(true);
                    return;
                }
                this.msekGreenBrightness.setEnabled(false);
                if (this.isRgbIoctl) {
                    Light.closeHuxiLight();
                    return;
                } else {
                    Light.setBrightness(2, 0);
                    return;
                }
            case 2131493209:
                if (isChecked) {
                    if (Utils.isOppoFind7s() || Utils.isOppoFind7sWCDMA() || Utils.isOppoN5207() || Utils.isOppoFind7sExp() || Utils.isOppoN5206()) {
                        Light.openBreatheLight(false);
                    } else if (this.isRgbIoctl) {
                        Light.openHuxiLight(3);
                    } else {
                        Light.setBrightness(3, 0);
                        Light.setBrightness(3, 120);
                    }
                    this.msekBlueBrightness.setEnabled(true);
                    return;
                }
                this.msekBlueBrightness.setEnabled(false);
                if (Utils.isOppoFind7s() || Utils.isOppoFind7sWCDMA() || Utils.isOppoN5207() || Utils.isOppoFind7sExp() || Utils.isOppoN5206()) {
                    Light.closeBreatheLight();
                    return;
                } else if (this.isRgbIoctl) {
                    Light.closeHuxiLight();
                    return;
                } else {
                    Light.setBrightness(3, 0);
                    return;
                }
            default:
                return;
        }
    }

    private void turnOffHuxiLight(boolean turnoff) {
        SystemProperties.set("sys.debug.breathe", Boolean.toString(turnoff));
    }
}
