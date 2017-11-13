package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.autotest.CameraPreview;
import com.android.engineeringmode.autotest.CameraPreview.OnOpenCameraFailedListener;
import com.oem.util.Feature;

public class FoolproofCameraTestExp extends Activity {
    private TextView backcamear_result_tv;
    private Runnable closeFrontRunnable = new Runnable() {
        public void run() {
            FoolproofCameraTestExp.this.mPreview.close();
            if (FoolproofCameraTestExp.this.isDualCameraSupport) {
                FoolproofCameraTestExp.this.swithchhandler.postDelayed(FoolproofCameraTestExp.this.switchSecondBackRunnable, 1000);
            } else {
                FoolproofCameraTestExp.this.mResultHandler.sendEmptyMessage(0);
            }
        }
    };
    private Runnable closeSecondBackRunnable = new Runnable() {
        public void run() {
            FoolproofCameraTestExp.this.mPreview.close();
            FoolproofCameraTestExp.this.mResultHandler.sendEmptyMessage(0);
        }
    };
    private TextView frontcamear_result_tv;
    boolean isDualCameraSupport = false;
    boolean isbackcamerapass = true;
    boolean isfontcmaearpass = true;
    boolean isinbackcameratest = true;
    boolean isinfrontcameratest = false;
    boolean issecondbackcamerapass = true;
    private CameraPreview mPreview;
    private Handler mResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            boolean isInModelTest = FoolproofCameraTestExp.this.getIntent().getBooleanExtra("model_test", false);
            if (msg.what == 0) {
                FoolproofCameraTestExp.this.mPreview.setVisibility(8);
                FoolproofCameraTestExp.this.setRequestedOrientation(1);
                FoolproofCameraTestExp.this.backcamear_result_tv.setVisibility(0);
                FoolproofCameraTestExp.this.frontcamear_result_tv.setVisibility(0);
                if (FoolproofCameraTestExp.this.isDualCameraSupport) {
                    FoolproofCameraTestExp.this.secondbackcamear_result_tv.setVisibility(0);
                }
                if (FoolproofCameraTestExp.this.isbackcamerapass) {
                    FoolproofCameraTestExp.this.backcamear_result_tv.setText("BackCamera PASS");
                    FoolproofCameraTestExp.this.backcamear_result_tv.setTextColor(-16711936);
                } else {
                    FoolproofCameraTestExp.this.backcamear_result_tv.setText("BackCamera Fail");
                    FoolproofCameraTestExp.this.backcamear_result_tv.setTextColor(-65536);
                }
                if (FoolproofCameraTestExp.this.isfontcmaearpass) {
                    FoolproofCameraTestExp.this.frontcamear_result_tv.setText("FrontCamera PASS");
                    FoolproofCameraTestExp.this.frontcamear_result_tv.setTextColor(-16711936);
                } else {
                    FoolproofCameraTestExp.this.frontcamear_result_tv.setText("FrontCamera Fail");
                    FoolproofCameraTestExp.this.frontcamear_result_tv.setTextColor(-65536);
                }
                if (FoolproofCameraTestExp.this.isDualCameraSupport) {
                    if (FoolproofCameraTestExp.this.issecondbackcamerapass) {
                        FoolproofCameraTestExp.this.secondbackcamear_result_tv.setText("SecondBackCamera PASS");
                        FoolproofCameraTestExp.this.secondbackcamear_result_tv.setTextColor(-16711936);
                    } else {
                        FoolproofCameraTestExp.this.secondbackcamear_result_tv.setText("SecondBackCamera Fail");
                        FoolproofCameraTestExp.this.secondbackcamear_result_tv.setTextColor(-65536);
                    }
                }
                if (FoolproofCameraTestExp.this.isbackcamerapass && FoolproofCameraTestExp.this.isfontcmaearpass && (!FoolproofCameraTestExp.this.isDualCameraSupport || FoolproofCameraTestExp.this.issecondbackcamerapass)) {
                    FoolproofCameraTestExp.this.setResult(1);
                    Log.d("FoolproofCameraTestExp", "test pass");
                } else {
                    FoolproofCameraTestExp.this.setResult(3);
                }
                if (isInModelTest) {
                    FoolproofCameraTestExp.this.finish();
                }
            }
        }
    };
    private TextView secondbackcamear_result_tv;
    private Runnable switchFrontRunnable = new Runnable() {
        public void run() {
            FoolproofCameraTestExp.this.isinbackcameratest = false;
            FoolproofCameraTestExp.this.isinfrontcameratest = true;
            FoolproofCameraTestExp.this.switchToFontCamera();
        }
    };
    private Runnable switchSecondBackRunnable = new Runnable() {
        public void run() {
            FoolproofCameraTestExp.this.isinbackcameratest = false;
            FoolproofCameraTestExp.this.isinfrontcameratest = false;
            FoolproofCameraTestExp.this.switchToSecondBackCamera();
        }
    };
    private Handler swithchhandler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FoolproofCameraTestExp", "FoolproofCameraTestExp oncreate");
        this.isDualCameraSupport = Feature.isDualBackCameraSupported(this);
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        getWindow().setFlags(128, 128);
        setContentView(2130903097);
        this.mPreview = (CameraPreview) findViewById(2131492934);
        this.backcamear_result_tv = (TextView) findViewById(2131493084);
        this.frontcamear_result_tv = (TextView) findViewById(2131493085);
        this.secondbackcamear_result_tv = (TextView) findViewById(2131493086);
        this.mPreview.setDevice(0);
        this.mPreview.setOnOpenCameraFailedListener(new OnOpenCameraFailedListener() {
            public void onFail() {
                Toast.makeText(FoolproofCameraTestExp.this, "Can't open the Camera", 1).show();
                if (FoolproofCameraTestExp.this.isinbackcameratest && !FoolproofCameraTestExp.this.isinfrontcameratest) {
                    FoolproofCameraTestExp.this.isbackcamerapass = false;
                } else if (!FoolproofCameraTestExp.this.isinbackcameratest && FoolproofCameraTestExp.this.isinfrontcameratest) {
                    FoolproofCameraTestExp.this.isfontcmaearpass = false;
                } else if (!FoolproofCameraTestExp.this.isinbackcameratest && !FoolproofCameraTestExp.this.isinfrontcameratest) {
                    FoolproofCameraTestExp.this.issecondbackcamerapass = false;
                }
            }
        });
    }

    protected void onResume() {
        super.onResume();
        this.mPreview.open();
        this.swithchhandler.postDelayed(this.switchFrontRunnable, 1000);
    }

    private void switchToFontCamera() {
        this.mPreview.close();
        this.mPreview.setDevice(1);
        this.mPreview.open();
        this.swithchhandler.postDelayed(this.closeFrontRunnable, 1000);
    }

    private void switchToSecondBackCamera() {
        this.mPreview.close();
        this.mPreview.setDevice(2);
        this.mPreview.open();
        this.swithchhandler.postDelayed(this.closeSecondBackRunnable, 1000);
    }

    protected void onPause() {
        super.onPause();
        this.mPreview.close();
        this.swithchhandler.removeCallbacks(this.switchFrontRunnable);
        this.swithchhandler.removeCallbacks(this.switchSecondBackRunnable);
        this.swithchhandler.removeCallbacks(this.closeFrontRunnable);
        this.swithchhandler.removeCallbacks(this.closeSecondBackRunnable);
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
