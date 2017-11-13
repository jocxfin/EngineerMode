package com.android.engineeringmode.autotest;

import android.os.Bundle;
import android.widget.Toast;

import com.android.engineeringmode.autotest.CameraPreview.OnOpenCameraFailedListener;

public class SecondBackCameraTest extends AutoTestItemActivity {
    private CameraPreview mPreview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        getWindow().setFlags(128, 128);
        setContentView(2130903072);
        this.mPreview = (CameraPreview) findViewById(2131492934);
        this.mPreview.setDevice(2);
        this.mPreview.setOnOpenCameraFailedListener(new OnOpenCameraFailedListener() {
            public void onFail() {
                Toast.makeText(SecondBackCameraTest.this, "Can't open the Camera", 1).show();
                SecondBackCameraTest.this.setAutoExit(1000);
            }
        });
        if (checkIsAutoAging() || checkIsAutoTest()) {
            setAutoExit(15000);
        }
    }

    protected void onResume() {
        super.onResume();
        this.mPreview.open();
    }

    protected void onPause() {
        super.onPause();
        this.mPreview.close();
    }
}
