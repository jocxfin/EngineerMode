package com.android.engineeringmode;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class CfuActivity extends Activity {
    private RadioButton mRadioBtnOff;
    private RadioButton mRadioBtnOn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903073);
        this.mRadioBtnOn = (RadioButton) findViewById(2131492999);
        this.mRadioBtnOff = (RadioButton) findViewById(2131493000);
        String cfuSetting = SystemProperties.get("persist.sys.cfu_auto", "1");
        if (cfuSetting.equals("0")) {
            this.mRadioBtnOff.setChecked(true);
        } else if (cfuSetting.equals("1")) {
            this.mRadioBtnOn.setChecked(true);
        } else {
            Toast.makeText(this, "Invalid status : " + cfuSetting, 0).show();
        }
        ((Button) findViewById(2131493001)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (CfuActivity.this.mRadioBtnOn.isChecked()) {
                    SystemProperties.set("persist.sys.cfu_auto", "1");
                    Log.i("CFU", "Set Query CFU Status : on");
                } else if (CfuActivity.this.mRadioBtnOff.isChecked()) {
                    SystemProperties.set("persist.sys.cfu_auto", "0");
                    Log.i("CFU", "Set Query CFU Status : off");
                } else {
                    Toast.makeText(CfuActivity.this, "please select a status!", 0).show();
                }
            }
        });
        ((Button) findViewById(2131493002)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CfuActivity.this.mRadioBtnOn.setChecked(true);
                SystemProperties.set("persist.sys.cfu_auto", "1");
            }
        });
    }
}
