package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;

public class FlashLampTest2 extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onResume() {
        super.onResume();
        RotationUtils.open_motor();
        RotationUtils.set_fill_flash_brightness(200);
    }

    protected void onPause() {
        super.onPause();
        RotationUtils.set_fill_flash_brightness(0);
        RotationUtils.close_motor();
    }
}
