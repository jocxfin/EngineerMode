package com.android.engineeringmode.manualtest.modeltest;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CameraTestController extends ModelTest3ItemActivity implements OnClickListener {
    private KeyguardManager mKeyguardManager;
    private TextView mResultTextView = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903209);
        this.mKeyguardManager = (KeyguardManager) getSystemService("keyguard");
        this.mResultTextView = (TextView) findViewById(2131493329);
        initResources();
        if (this.mKeyguardManager == null || !this.mKeyguardManager.isKeyguardLocked()) {
            Intent cameraActivity = new Intent("android.intent.action.MAIN");
            cameraActivity.setClassName("com.oneplus.camera", "com.oneplus.camera.OPCameraActivity").setFlags(268435456);
            cameraActivity.putExtra("CameraActivity.IsDebugMode", true);
            startActivity(cameraActivity);
            return;
        }
        this.mResultTextView.setText(2131297658);
    }

    private void initResources() {
        ((Button) findViewById(2131493015)).setOnClickListener(this);
        ((Button) findViewById(2131493236)).setOnClickListener(this);
        ((Button) findViewById(2131493237)).setOnClickListener(this);
    }

    public void onClick(View v) {
        Log.i("CameraTestController", "onClick");
        switch (v.getId()) {
            case 2131493015:
                setResult(1);
                finish();
                return;
            case 2131493236:
                setResult(2);
                finish();
                return;
            case 2131493237:
                setResult(3);
                finish();
                return;
            default:
                return;
        }
    }
}
