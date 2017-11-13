package com.android.engineeringmode.manualtest;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.manualtest.OemCameraPreview.OnOpenCameraFailedListener;
import com.android.engineeringmode.manualtest.modeltest.ModelTest3ItemActivity;
import com.android.engineeringmode.util.ExternFunction;

import java.io.File;
import java.util.List;

public class DualLedCalibrationSecondTest extends ModelTest3ItemActivity {
    private final String calibrationFilePath = "/persist/second_rear_camera_dual_led_calibration";
    private AlertDialog dialog = null;
    private MetaDataCallback mCameraMetaDataCallback;
    private ExternFunction mExFunction;
    private int mFirstCameraTestResult;
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
                    metaData[i] = DualLedCalibrationSecondTest.this.byteToInt(data, i * 4);
                }
                if (metaData[0] == 4) {
                    Log.e("DualLedCalibrationSecondTest", "onCameraMetaData - QCAMERA_METADATA_LED_CALIB : " + metaData[0]);
                    Log.e("DualLedCalibrationSecondTest", "onCameraMetaData - length : " + metaData[1]);
                    Log.e("DualLedCalibrationSecondTest", "onCameraMetaData - value : " + metaData[2]);
                    if (!DualLedCalibrationSecondTest.this.mIsPausing) {
                        if (metaData[2] == 1) {
                            Toast.makeText(DualLedCalibrationSecondTest.this, "Test Pass", 0).show();
                        } else {
                            Toast.makeText(DualLedCalibrationSecondTest.this, "Test Failed", 0).show();
                        }
                        DualLedCalibrationSecondTest.this.setShutterBtnProperty(0, true);
                        if (DualLedCalibrationSecondTest.this.getCamera() != null) {
                            DualLedCalibrationSecondTest.this.mParameters.set("led-calibration", "off");
                            DualLedCalibrationSecondTest.this.getCamera().setParameters(DualLedCalibrationSecondTest.this.mParameters);
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(DualLedCalibrationSecondTest.this.getString(2131297553)).append(DualLedCalibrationSecondTest.this.mFirstCameraTestResult == 1 ? "Pass" : "Fail").append("\n");
                        stringBuilder.append(DualLedCalibrationSecondTest.this.getString(2131297554)).append(metaData[2] == 1 ? "Pass" : "Fail").append("\n");
                        if (DualLedCalibrationSecondTest.this.getIntent().getBooleanExtra("model_test", false)) {
                            if (metaData[2] == 1) {
                                DualLedCalibrationSecondTest.this.onTestPassed();
                                return;
                            }
                            DualLedCalibrationSecondTest.this.setResult(3);
                            DualLedCalibrationSecondTest.this.finish();
                        } else if (DualLedCalibrationSecondTest.this.mFirstCameraTestResult == 1 && metaData[2] == 1) {
                            DualLedCalibrationSecondTest.this.dialog(stringBuilder.toString(), true);
                        } else {
                            DualLedCalibrationSecondTest.this.dialog(stringBuilder.toString(), false);
                        }
                    }
                }
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DualLedCalibrationSecondTest", "DualLedCalibrationTest");
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        getWindow().setFlags(128, 128);
        setContentView(2130903089);
        this.mFirstCameraTestResult = getIntent().getExtras().getInt("info");
        this.mShutterBtn = (ImageButton) findViewById(2131493072);
        this.mShutterBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (DualLedCalibrationSecondTest.this.getCamera() != null) {
                    DualLedCalibrationSecondTest.this.setShutterBtnProperty(0, false);
                    DualLedCalibrationSecondTest.this.delTheDualCalibrationFile();
                    DualLedCalibrationSecondTest.this.setCameraMetaDataCallBack();
                    DualLedCalibrationSecondTest.this.applyMaxPicSize();
                    DualLedCalibrationSecondTest.this.mParameters.set("led-calibration", "on");
                    DualLedCalibrationSecondTest.this.getCamera().setParameters(DualLedCalibrationSecondTest.this.mParameters);
                }
            }
        });
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Light.CHARGE_RED_LIGHT /*2*/:
                        Log.v("DualLedCalibrationSecondTest", "  mHandler");
                        return;
                    case Light.CHARGE_GREEN_LIGHT /*3*/:
                        DualLedCalibrationSecondTest.this.delTheDualCalibrationFile();
                        DualLedCalibrationSecondTest.this.setCameraMetaDataCallBack();
                        DualLedCalibrationSecondTest.this.applyMaxPicSize();
                        DualLedCalibrationSecondTest.this.mParameters.set("led-calibration", "on");
                        DualLedCalibrationSecondTest.this.getCamera().setParameters(DualLedCalibrationSecondTest.this.mParameters);
                        return;
                    default:
                        return;
                }
            }
        };
        this.mPreview = (OemCameraPreview) findViewById(2131493071);
        this.mPreview.setDevice(2);
        this.mPreview.setOnOpenCameraFailedListener(new OnOpenCameraFailedListener() {
            public void onFail() {
                Toast.makeText(DualLedCalibrationSecondTest.this.getApplicationContext(), "Can't open the Camera", 1).show();
                DualLedCalibrationSecondTest.this.onBackPressed();
            }

            public void startPreviewSucesss() {
                Log.v("DualLedCalibrationSecondTest", "startPreviewSucesss");
                DualLedCalibrationSecondTest.this.setShutterBtnProperty(0, false);
                DualLedCalibrationSecondTest.this.mParameters = DualLedCalibrationSecondTest.this.getParameters();
                DualLedCalibrationSecondTest.this.mHandler.sendEmptyMessageDelayed(3, 1000);
            }
        });
        this.mExFunction = new ExternFunction(this);
        this.mExFunction.registerOnServiceConnected(this.mHandler, 2, null);
    }

    private void delTheDualCalibrationFile() {
        if (isTheDualCalibrationFileExist()) {
            new File("/persist/second_rear_camera_dual_led_calibration").delete();
        }
    }

    private boolean isTheDualCalibrationFileExist() {
        File file = new File("/persist/second_rear_camera_dual_led_calibration");
        Log.v("DualLedCalibrationSecondTest", "isTheDualCalibrationFileExist file= " + file);
        if (file == null) {
            return false;
        }
        Log.v("DualLedCalibrationSecondTest", "isTheDualCalibrationFileExist + =  filestring = " + file.getAbsolutePath() + "exists =" + file.exists());
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
        if (this.dialog != null) {
            this.dialog.dismiss();
        }
        super.onDestroy();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setCameraMetaDataCallBack() {
        if (this.mCameraMetaDataCallback == null) {
            this.mCameraMetaDataCallback = new MetaDataCallback();
        }
        if (getCamera() != null) {
            getCamera().setMetadataCb(this.mCameraMetaDataCallback);
        }
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
            Log.e("DualLedCalibrationSecondTest", "applyMaxPicSize - parameters is null.");
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

    private void dialog(String message, boolean success) {
        Builder builder = new Builder(this);
        builder.setMessage(message);
        TextView message_tv = new TextView(this);
        message_tv.setGravity(17);
        message_tv.setTextSize(30.0f);
        if (success) {
            message_tv.setText("PASS");
            message_tv.setTextColor(-16711936);
        } else {
            message_tv.setText("FAIL");
            message_tv.setTextColor(-65536);
        }
        builder.setView(message_tv);
        builder.setTitle("Test Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DualLedCalibrationSecondTest.this.finish();
            }
        });
        this.dialog = builder.create();
        this.dialog.getWindow().setType(2003);
        this.dialog.setCancelable(false);
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.show();
    }
}
