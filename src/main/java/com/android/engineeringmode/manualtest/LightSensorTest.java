package com.android.engineeringmode.manualtest;

import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.engineeringmode.LumenEventListener;
import com.android.engineeringmode.autotest.AutoTestItemActivity;
import com.android.engineeringmode.functions.Light;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

public class LightSensorTest extends AutoTestItemActivity implements OnClickListener {
    private View mBottomBar;
    private LumenEventListener mLightListener = null;
    private int mOldBrightness;
    private Button mbtnExit;
    private TextView mtvLightIntensity = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(2131296971);
        setContentView(2130903140);
        this.mtvLightIntensity = (TextView) findViewById(2131493333);
        this.mBottomBar = findViewById(2131493334);
        this.mbtnExit = (Button) findViewById(2131493335);
        if (checkIsAutoAging() || checkIsAutoTest()) {
            this.mBottomBar.setVisibility(0);
            this.mbtnExit.setVisibility(0);
            this.mbtnExit.setOnClickListener(this);
        } else {
            this.mBottomBar.setVisibility(8);
            this.mbtnExit.setVisibility(8);
        }
        Calendar now = Calendar.getInstance();
        String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--LightSensorTest--" + "has entered it";
    }

    public void onClick(View v) {
        if (2131493335 == v.getId()) {
            endActivity();
        }
    }

    protected void onResume() {
        super.onResume();
        Log.w("LightSensorTest", "onResume");
        try {
            this.mOldBrightness = System.getInt(getContentResolver(), "screen_brightness");
        } catch (SettingNotFoundException e) {
            this.mOldBrightness = Light.MAIN_KEY_MAX;
        }
        this.mLightListener = new LumenEventListener(this) {
            public void onLumenChanged(int lumen) {
                String calParameter = LightSensorTest.this.readFileByLines("/persist/sensors/als_rgb_cal_parameter");
                String[] cal = new String[4];
                if (calParameter != null) {
                    cal = calParameter.split(",");
                }
                Log.w("LightSensorTest", "light = " + lumen + "calParameter: " + calParameter);
                LightSensorTest.this.mtvLightIntensity.setText("光强：      " + lumen + "\n校准系数：      " + cal[0]);
                if (lumen > 300) {
                    lumen = 300;
                }
                LightSensorTest.this.setBrightness((int) ((((double) lumen) / 300.0d) * 255.0d));
            }
        };
        this.mLightListener.enable();
    }

    private String readFileByLines(String fileName) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        String tempString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                tempString = reader.readLine();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("LightSensorTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("LightSensorTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("LightSensorTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    return tempString;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("LightSensorTest", "readFileByLines io close exception :" + e122.getMessage());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = reader;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            Log.e("LightSensorTest", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return tempString;
        }
        return tempString;
    }

    protected void onPause() {
        Log.w("LightSensorTest", "onPause");
        this.mLightListener.disable();
        setBrightness(this.mOldBrightness);
        super.onPause();
    }

    private void setBrightness(int nBrightness) {
        PowerManager pwManager = (PowerManager) getSystemService("power");
        if (pwManager != null) {
            pwManager.setBacklightBrightness(nBrightness);
        }
    }
}
