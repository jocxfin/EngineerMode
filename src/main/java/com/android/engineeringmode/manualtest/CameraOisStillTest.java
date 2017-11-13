package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.CameraMetaDataCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemProperties;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;

import java.util.List;

public class CameraOisStillTest extends Activity implements Callback {
    private static final String TAG = CameraOisStillTest.class;
    private boolean isInModelTest = false;
    private Camera mCamera;
    private int mCameraDataX = 0;
    private int mCameraDataY = 0;
    private boolean mCameraInitialized = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    Log.d(CameraOisStillTest.TAG, "handler msg TRUN_OFF");
                    CameraOisStillTest.this.removeAllMessage();
                    SystemProperties.set("persist.camera.ois.disable", "1");
                    CameraOisStillTest.this.startPreview();
                    CameraOisStillTest.this.mtvSwitchOnX.setText("StillX");
                    CameraOisStillTest.this.mtvSwitchOnX.setTextColor(-16711936);
                    CameraOisStillTest.this.mtvSwitchOnX.append("\n");
                    CameraOisStillTest.this.mtvSwitchOnY.setText("StillY");
                    CameraOisStillTest.this.mtvSwitchOnY.setTextColor(-16711936);
                    CameraOisStillTest.this.mtvSwitchOnY.append("\n");
                    CameraOisStillTest.this.mUpdateTime = 0;
                    CameraOisStillTest.this.mSumX = 0;
                    CameraOisStillTest.this.mSumY = 0;
                    CameraOisStillTest.this.mSumBadFrame = 0;
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    Log.d(CameraOisStillTest.TAG, "handler msg UPDATE_TEXT");
                    CameraOisStillTest.this.mCameraDataX = msg.arg1;
                    CameraOisStillTest.this.mCameraDataY = msg.arg2;
                    CameraOisStillTest.this.sbX.append(CameraOisStillTest.this.mCameraDataX);
                    CameraOisStillTest.this.sbY.append(CameraOisStillTest.this.mCameraDataY);
                    if (CameraOisStillTest.this.mUpdateTime != 10) {
                        CameraOisStillTest.this.sbX.append(" | ");
                        CameraOisStillTest.this.sbY.append(" | ");
                    }
                    CameraOisStillTest cameraOisStillTest = CameraOisStillTest.this;
                    cameraOisStillTest.mSumX = cameraOisStillTest.mSumX + CameraOisStillTest.this.mCameraDataX;
                    CameraOisStillTest.this.mMeanX = CameraOisStillTest.this.mSumX / CameraOisStillTest.this.mUpdateTime;
                    cameraOisStillTest = CameraOisStillTest.this;
                    cameraOisStillTest.mSumY = cameraOisStillTest.mSumY + CameraOisStillTest.this.mCameraDataY;
                    CameraOisStillTest.this.mMeanY = CameraOisStillTest.this.mSumY / CameraOisStillTest.this.mUpdateTime;
                    CameraOisStillTest.this.mOffValueX = CameraOisStillTest.this.mMeanX;
                    CameraOisStillTest.this.mOffValueY = CameraOisStillTest.this.mMeanY;
                    if (CameraOisStillTest.this.mUpdateTime == 10) {
                        setSharedPreference(CameraOisStillTest.this.sbX.toString(), CameraOisStillTest.this.mOffValueX, CameraOisStillTest.this.sbY.toString(), CameraOisStillTest.this.mOffValueY, CameraOisStillTest.this.mSumBadFrame);
                        CameraOisStillTest.this.mHandler.sendEmptyMessageDelayed(3, 2000);
                        return;
                    }
                    return;
                case Light.CHARGE_GREEN_LIGHT /*3*/:
                    if (CameraOisStillTest.this.mSumBadFrame >= 2) {
                        CameraOisStillTest.this.mtvResult.setText("Preview is over exposure(" + CameraOisStillTest.this.mSumBadFrame + "), check Test Environment \n X Value: " + CameraOisStillTest.this.mOffValueX + ", Y Value: " + CameraOisStillTest.this.mOffValueY);
                        CameraOisStillTest.this.mtvResult.setTextColor(-65536);
                    } else {
                        CameraOisStillTest.this.mtvResult.setText("X PASS: " + CameraOisStillTest.this.mOffValueX + ", Y PASS: " + CameraOisStillTest.this.mOffValueY);
                        CameraOisStillTest.this.mtvResult.setTextColor(-16711936);
                        if (CameraOisStillTest.this.isInModelTest) {
                            CameraOisStillTest.this.setResult(1);
                            CameraOisStillTest.this.finish();
                        }
                    }
                    CameraOisStillTest.this.mtvResult.setGravity(17);
                    SystemProperties.set("persist.camera.ois.disable", "0");
                    return;
                case 5:
                    Log.d(CameraOisStillTest.TAG, "handler msg UPDATE_WARNING");
                    CameraOisStillTest.this.mtvResult.setText("Preview is over exposure, check Test Environment");
                    CameraOisStillTest.this.mtvResult.setTextColor(-65536);
                    CameraOisStillTest.this.mtvResult.setGravity(17);
                    return;
                default:
                    return;
            }
        }

        private void setSharedPreference(String strX, int valueX, String strY, int valueY, int value) {
            Editor et = CameraOisStillTest.this.getSharedPreferences("com.android.engineeringmode_preferences", 0).edit();
            et.putString("key_is_ois_stillx_value", strX);
            et.putInt("key_is_ois_stillx_mean_value", valueX);
            et.putString("key_is_ois_stilly_value", strY);
            et.putInt("key_is_ois_stilly_mean_value", valueY);
            et.putInt("key_is_ois_still_bad_frame_value", value);
            et.commit();
        }
    };
    private int mMeanX = 0;
    private int mMeanY = 0;
    private final MetaDataCallback mMetaDataCallback = new MetaDataCallback();
    private int mOffValueX = 0;
    private int mOffValueY = 0;
    private SurfaceView mPreview;
    private boolean mPreviewActive = false;
    private int mSumBadFrame = 0;
    private int mSumX = 0;
    private int mSumY = 0;
    private SurfaceHolder mSurfaceHolder;
    private int mUpdateTime = 0;
    private WakeLock mWakeLock;
    private TextView mtvResult = null;
    private TextView mtvSwitchOffX = null;
    private TextView mtvSwitchOffY = null;
    private TextView mtvSwitchOnX = null;
    private TextView mtvSwitchOnY = null;
    private StringBuilder sbX = new StringBuilder();
    private StringBuilder sbY = new StringBuilder();
    List<Size> sizes;
    private boolean startTest = true;

    private final class MetaDataCallback implements CameraMetaDataCallback {
        private MetaDataCallback() {
        }

        public void onCameraMetaData(byte[] data, Camera camera) {
            int[] metadata = new int[4];
            if (CameraOisStillTest.this.startTest && data.length == 16) {
                for (int i = 0; i < 4; i++) {
                    metadata[i] = byteToInt(data, i * 4);
                }
                Log.e(CameraOisStillTest.TAG, "onCameraMetaData " + metadata[0] + ", " + metadata[1] + ", " + metadata[2] + ", " + metadata[3]);
                if ((metadata[0] == 9 || metadata[0] == 10) && CameraOisStillTest.this.mUpdateTime <= 10) {
                    CameraOisStillTest cameraOisStillTest;
                    CameraOisStillTest.this.mtvSwitchOnX.append(Integer.toString(metadata[2]));
                    CameraOisStillTest.this.mtvSwitchOnX.append("\n");
                    CameraOisStillTest.this.mtvSwitchOnY.append(Integer.toString(metadata[3]));
                    CameraOisStillTest.this.mtvSwitchOnY.append("\n");
                    if (metadata[0] == 10 || metadata[2] < 80 || metadata[2] > 240 || metadata[3] < 80 || metadata[3] > 240) {
                        cameraOisStillTest = CameraOisStillTest.this;
                        cameraOisStillTest.mSumBadFrame = cameraOisStillTest.mSumBadFrame + 1;
                        Log.e(CameraOisStillTest.TAG, "onCameraMetaData mSumBadFrame " + CameraOisStillTest.this.mSumBadFrame + ", mUpdateTime " + CameraOisStillTest.this.mUpdateTime);
                        if (CameraOisStillTest.this.mSumBadFrame == 1) {
                            return;
                        }
                    }
                    cameraOisStillTest = CameraOisStillTest.this;
                    cameraOisStillTest.mUpdateTime = cameraOisStillTest.mUpdateTime + 1;
                    if (CameraOisStillTest.this.mUpdateTime <= 10) {
                        Message msg = CameraOisStillTest.this.mHandler.obtainMessage();
                        msg.what = 2;
                        msg.arg1 = metadata[2];
                        msg.arg2 = metadata[3];
                        CameraOisStillTest.this.mHandler.sendMessage(msg);
                    }
                }
            }
        }

        private int byteToInt(byte[] b, int offset) {
            int value = 0;
            for (int i = 0; i < 4; i++) {
                value += (b[(3 - i) + offset] & Light.MAIN_KEY_MAX) << ((3 - i) * 8);
            }
            return value;
        }
    }

    private void removeAllMessage() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(0);
            this.mHandler.removeMessages(1);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
        if (this.isInModelTest) {
            this.startTest = false;
            dialog();
        }
        setContentView(2130903070);
        this.mPreview = (SurfaceView) findViewById(2131492934);
        this.mSurfaceHolder = this.mPreview.getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mSurfaceHolder.setType(3);
        this.mtvSwitchOnX = (TextView) findViewById(2131492986);
        this.mtvSwitchOnY = (TextView) findViewById(2131492987);
        this.mtvSwitchOffX = (TextView) findViewById(2131492989);
        this.mtvSwitchOffY = (TextView) findViewById(2131492988);
        this.mtvSwitchOffX.setVisibility(8);
        this.mtvSwitchOffY.setVisibility(8);
        this.mtvResult = (TextView) findViewById(2131492990);
    }

    private void dialog() {
        Builder builder = new Builder(this);
        builder.setMessage("确认进行光学防抖测试吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                CameraOisStillTest.this.startTest = true;
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(2003);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    protected void onResume() {
        super.onResume();
        this.mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(26, TAG);
        this.mWakeLock.acquire();
        try {
            this.mCamera = Camera.openLegacy(0, 256);
        } catch (RuntimeException e) {
            Log.v(TAG, "openLegacy failed. Using open instead");
            this.mCamera = Camera.open(0);
        }
    }

    public void onPause() {
        if (this.mCamera != null) {
            if (this.mPreviewActive) {
                this.mCamera.stopPreview();
            }
            this.mCamera.release();
            this.mCamera = null;
        }
        this.mPreviewActive = false;
        this.mWakeLock.release();
        removeAllMessage();
        super.onPause();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initializeCamera();
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    private void initializeCamera() {
        initializeCamera(true);
    }

    private void initializeCamera(boolean startPreviewAfterInit) {
        if (this.mCamera != null && this.mSurfaceHolder.getSurface() != null) {
            try {
                int i;
                this.mCamera.setPreviewDisplay(this.mSurfaceHolder);
                Parameters params = setCameraParams(this.mCamera);
                this.sizes = params.getSupportedPreviewSizes();
                Log.d(TAG, "" + this.sizes.size());
                for (i = 0; i < this.sizes.size(); i++) {
                    Log.d(TAG, "" + ((Size) this.sizes.get(i)).height + "------" + ((Size) this.sizes.get(i)).width);
                }
                Size previewSize = null;
                Log.d(TAG, "" + this.sizes.size());
                i = 0;
                while (i < this.sizes.size()) {
                    if (previewSize == null && ((Size) this.sizes.get(i)).width <= 1920) {
                        previewSize = (Size) this.sizes.get(i);
                    }
                    Log.d(TAG, "" + ((Size) this.sizes.get(i)).height + "------" + ((Size) this.sizes.get(i)).width);
                    i++;
                }
                if (previewSize != null) {
                    params.setPreviewSize(previewSize.width, previewSize.height);
                }
                this.mCamera.setParameters(params);
                this.mCameraInitialized = true;
                if (startPreviewAfterInit) {
                    this.mHandler.sendEmptyMessageDelayed(1, 2000);
                }
            } catch (Throwable t) {
                Log.e("TAG", "Could not set preview display", t);
                Toast.makeText(this, t.getMessage(), 1).show();
            }
        }
    }

    private void startPreview() {
        if (this.mCamera != null) {
            setCameraDisplayOrientation(this, 0, this.mCamera);
            this.mCamera.startPreview();
            this.mCamera.setMetadataCb(this.mMetaDataCallback);
            this.mPreviewActive = true;
            int af_pos = SystemProperties.getInt("persist.camera.ois.afpos", 2);
            Log.e(TAG, " get persist.camera.ois.afpos " + af_pos);
            setTargetPosition(af_pos);
        }
    }

    private void setTargetPosition(int targetPos) {
        Parameters params = this.mCamera.getParameters();
        if (isSupported("manual", params.getSupportedFocusModes())) {
            for (int i = 0; i <= targetPos; i++) {
                params.setFocusMode("manual");
                params.setFocusPosition(3, i);
                this.mCamera.setParameters(params);
            }
        }
    }

    private Parameters setCameraParams(Camera camera) {
        Parameters params = camera.getParameters();
        params.setCameraMode(0);
        params.setISOValue("ISO100");
        params.setExposureTime(500);
        params.setSceneMode("manual");
        params.set("ois-testing", 1);
        return params;
    }

    private static boolean isSupported(String value, List<String> supported) {
        return supported != null && supported.indexOf(value) >= 0;
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        int result;
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int degrees = 0;
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case 0:
                degrees = 0;
                break;
            case Light.MAIN_KEY_LIGHT /*1*/:
                degrees = 90;
                break;
            case Light.CHARGE_RED_LIGHT /*2*/:
                degrees = 180;
                break;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                degrees = 270;
                break;
        }
        if (info.facing == 1) {
            result = (360 - ((info.orientation + degrees) % 360)) % 360;
        } else {
            result = ((info.orientation - degrees) + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}
