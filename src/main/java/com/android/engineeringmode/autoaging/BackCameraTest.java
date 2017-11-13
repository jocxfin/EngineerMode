package com.android.engineeringmode.autoaging;

import android.os.Bundle;
import android.widget.Toast;

import com.android.engineeringmode.autotest.CameraPreview;
import com.android.engineeringmode.autotest.CameraPreview.OnOpenCameraFailedListener;

public class BackCameraTest extends BaseTest {
    private CameraPreview mPreview;

    public void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        getWindow().setFlags(128, 128);
        setContentView(2130903072);
        this.mPreview = (CameraPreview) findViewById(2131492934);
        this.mPreview.setDevice(0);
        this.mPreview.setOnOpenCameraFailedListener(new OnOpenCameraFailedListener() {
            public void onFail() {
                Toast.makeText(BackCameraTest.this, "Can't open the Camera", 1).show();
            }
        });
    }

    protected void runTest() {
    }

    protected void endTest() {
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
