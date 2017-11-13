package com.android.engineeringmode;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;

public class RebootManager extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mode = getIntent().getStringExtra("order");
        if (mode != null && mode.equals("*#912#")) {
            ((PowerManager) getSystemService("power")).reboot("mos");
        }
    }
}
