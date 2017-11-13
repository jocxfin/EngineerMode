package com.android.engineeringmode.qualcomm;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemProperties;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class OtaSwitch extends Activity implements OnCheckedChangeListener {
    private ToggleButton mOta;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903174);
        this.mOta = (ToggleButton) findViewById(2131493471);
        this.mOta.setOnCheckedChangeListener(this);
    }

    protected void onResume() {
        super.onResume();
        this.mOta.setChecked("1".equals(SystemProperties.get("sys.ota.test", "0")));
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            SystemProperties.set("sys.ota.test", "1");
        } else {
            SystemProperties.set("sys.ota.test", "0");
        }
    }
}
