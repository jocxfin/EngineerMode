package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;

import com.android.engineeringmode.functions.Light;

import java.util.Calendar;

public class FlashLampTest extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onResume() {
        super.onResume();
        Light.flashLamp(0);
        Light.flashLamp(1);
        Calendar now = Calendar.getInstance();
        String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--FlashLampTest--" + "opened Flashlight";
    }

    protected void onPause() {
        super.onPause();
        Light.flashLamp(0);
    }
}
