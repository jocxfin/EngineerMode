package com.android.engineeringmode.autoaging;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.android.engineeringmode.LumenEventListener;

public class LightSensorTest extends BaseTest {
    private LumenEventListener mLightListener = null;
    private TextView mtvLightIntensity = null;

    protected void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setTitle(2131296993);
        setContentView(2130903140);
        this.mtvLightIntensity = (TextView) findViewById(2131493333);
        this.mLightListener = new LumenEventListener(this) {
            public void onLumenChanged(int lumen) {
                Log.w("LightSensorTest AutoAging", "light = " + lumen);
                LightSensorTest.this.mtvLightIntensity.setText("光强：      " + lumen);
            }
        };
        this.mLightListener.enable();
        ((Button) findViewById(2131493335)).setVisibility(4);
    }

    protected void endTest() {
        this.mLightListener.disable();
    }

    protected void runTest() {
    }
}
