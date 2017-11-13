package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InfraredGestureSensorTest extends Activity implements OnClickListener {
    private Button mAgingTest;
    private Button mManualTest;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903125);
        this.mManualTest = (Button) findViewById(2131493234);
        this.mAgingTest = (Button) findViewById(2131493235);
        this.mManualTest.setOnClickListener(this);
        this.mAgingTest.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131493234:
                manualTest();
                return;
            case 2131493235:
                agingTest();
                return;
            default:
                return;
        }
    }

    private void manualTest() {
        Intent intent = new Intent();
        intent.setClass(this, InfraredGestureManualTest.class);
        startActivity(intent);
    }

    private void agingTest() {
        Intent intent = new Intent();
        intent.setClass(this, InfraredGestureAgingTest.class);
        startActivity(intent);
    }
}
