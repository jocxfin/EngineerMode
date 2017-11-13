package com.android.engineeringmode.manualtest;

import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraMetaDataCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.manualtest.OemCameraPreview.OnOpenCameraFailedListener;
import com.android.engineeringmode.manualtest.modeltest.ModelTest3ItemActivity;
import com.android.engineeringmode.util.ExternFunction;

import java.io.File;
import java.util.List;

public class DualLedCalibrationFirstTest extends ModelTest3ItemActivity {
    private final String calibrationFilePath = "/persist/rear_camera_dual_led_calibration";
    private MetaDataCallback mCameraMetaDataCallback;
    private ExternFunction mExFunction;
    private Handler mHandler;
    private boolean mIsPausing;
    private Parameters mParameters;
    private OemCameraPreview mPreview;
    private ImageButton mShutterBtn;

    private final class MetaDataCallback implements CameraMetaDataCallback {
        private MetaDataCallback() {
        }

        public void onCameraMetaData(byte[] data, Camera camera) {
            int[] metaData = new int[3];
            if (data.length == 12) {
                for (int i = 0; i < 3; i++) {
                    metaData[i] = DualLedCalibrationFirstTest.this.byteToInt(data, i * 4);
                }
                if (metaData[0] == 4) {
                    Log.e("DualLedCalibrationFirstTest", "onCameraMetaData - QCAMERA_METADATA_LED_CALIB : " + metaData[0]);
                    Log.e("DualLedCalibrationFirstTest", "onCameraMetaData - length : " + metaData[1]);
                    Log.e("DualLedCalibrationFirstTest", "onCameraMetaData - value : " + metaData[2]);
                    if (!DualLedCalibrationFirstTest.this.mIsPausing) {
                        DualLedCalibrationFirstTest.this.setShutterBtnProperty(0, true);
                        if (DualLedCalibrationFirstTest.this.getCamera() != null) {
                            DualLedCalibrationFirstTest.this.mParameters.set("led-calibration", "off");
                            DualLedCalibrationFirstTest.this.getCamera().setParameters(DualLedCalibrationFirstTest.this.mParameters);
                        }
                        if (!DualLedCalibrationFirstTest.this.getIntent().getBooleanExtra("model_test", false)) {
                            Intent myIntent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt("info", metaData[2]);
                            myIntent.putExtras(bundle);
                            myIntent.setClass(DualLedCalibrationFirstTest.this, DualLedCalibrationSecondTest.class);
                            DualLedCalibrationFirstTest.this.startActivity(myIntent);
                            DualLedCalibrationFirstTest.this.finish();
                        } else if (metaData[2] == 1) {
                            DualLedCalibrationFirstTest.this.onTestPassed();
                        } else {
                            DualLedCalibrationFirstTest.this.setResult(3);
                            DualLedCalibrationFirstTest.this.finish();
                        }
                    }
                }
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DualLedCalibrationFirstTest", "DualLedCalibrationFirstTest");
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        getWindow().setFlags(128, 128);
        setContentView(2130903089);
        this.mShutterBtn = (ImageButton) findViewById(2131493072);
        this.mShutterBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (DualLedCalibrationFirstTest.this.getCamera() != null) {
                    DualLedCalibrationFirstTest.this.setShutterBtnProperty(0, false);
                    DualLedCalibrationFirstTest.this.delTheDualCalibrationFile();
                    DualLedCalibrationFirstTest.this.setCameraMetaDataCallBack();
                    DualLedCalibrationFirstTest.this.applyMaxPicSize();
                    DualLedCalibrationFirstTest.this.mParameters.set("led-calibration", "on");
                    DualLedCalibrationFirstTest.this.getCamera().setParameters(DualLedCalibrationFirstTest.this.mParameters);
                }
            }
        });
        this.mPreview = (OemCameraPreview) findViewById(2131493071);
        this.mPreview.setDevice(0);
        this.mPreview.setOnOpenCameraFailedListener(new OnOpenCameraFailedListener() {
            public void onFail() {
                Toast.makeText(DualLedCalibrationFirstTest.this.getApplicationContext(), "Can't open the Camera", 1).show();
                DualLedCalibrationFirstTest.this.onBackPressed();
            }

            public void startPreviewSucesss() {
                Log.v("DualLedCalibrationFirstTest", "startPreviewSucesss");
                DualLedCalibrationFirstTest.this.setShutterBtnProperty(0, true);
                DualLedCalibrationFirstTest.this.mParameters = DualLedCalibrationFirstTest.this.getParameters();
            }
        });
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Light.CHARGE_RED_LIGHT /*2*/:
                        Log.v("DualLedCalibrationFirstTest", "  mHandler");
                        return;
                    default:
                        return;
                }
            }
        };
        this.mExFunction = new ExternFunction(this);
        this.mExFunction.registerOnServiceConnected(this.mHandler, 2, null);
    }

    private void delTheDualCalibrationFile() {
        if (isTheDualCalibrationFileExist()) {
            new File("/persist/rear_camera_dual_led_calibration").delete();
        }
    }

    private boolean isTheDualCalibrationFileExist() {
        File file = new File("/persist/rear_camera_dual_led_calibration");
        Log.v("DualLedCalibrationFirstTest", "isTheDualCalibrationFileExist file= " + file);
        if (file == null) {
            return false;
        }
        Log.v("DualLedCalibrationFirstTest", "isTheDualCalibrationFileExist + =  filestring = " + file.getAbsolutePath() + "exists =" + file.exists());
        return file.exists();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    private void setShutterBtnProperty(int visibility, boolean enable) {
        if (this.mShutterBtn != null) {
            this.mShutterBtn.setVisibility(visibility);
            this.mShutterBtn.setEnabled(enable);
        }
    }

    protected void onResume() {
        super.onResume();
        this.mPreview.open();
        this.mIsPausing = false;
    }

    protected void onPause() {
        super.onPause();
        this.mIsPausing = true;
        this.mHandler.removeCallbacks(null);
        this.mPreview.close();
        this.mParameters = null;
    }

    private Camera getCamera() {
        if (this.mPreview != null) {
            return this.mPreview.getCamera();
        }
        return null;
    }

    private Parameters getParameters() {
        Camera camera = getCamera();
        if (camera != null) {
            return camera.getParameters();
        }
        return null;
    }

    protected void onDestroy() {
        this.mExFunction.unregisterOnServiceConnected(this.mHandler);
        this.mExFunction.dispose();
        this.mHandler = null;
        this.mParameters = null;
        super.onDestroy();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setCameraMetaDataCallBack() {
        if (this.mCameraMetaDataCallback == null) {
            this.mCameraMetaDataCallback = new MetaDataCallback();
        }
        getCamera().setMetadataCb(this.mCameraMetaDataCallback);
    }

    private int byteToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            value += (b[(3 - i) + offset] & Light.MAIN_KEY_MAX) << ((3 - i) * 8);
        }
        return value;
    }

    private void applyMaxPicSize() {
        if (this.mParameters == null) {
            Log.e("DualLedCalibrationFirstTest", "applyMaxPicSize - parameters is null.");
        }
        List<Size> picSizes = this.mParameters.getSupportedPictureSizes();
        Size expectSize = (Size) picSizes.get(0);
        long expectValue = (long) (expectSize.width * expectSize.height);
        for (Size size : picSizes) {
            long tempValue = (long) (size.width * size.height);
            if (expectValue < tempValue) {
                expectValue = tempValue;
                expectSize = size;
            }
        }
        this.mParameters.setPictureSize(expectSize.width, expectSize.height);
    }
}
