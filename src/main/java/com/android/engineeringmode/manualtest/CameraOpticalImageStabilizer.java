package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.engineeringmode.util.ExternFunction;

import java.util.ArrayList;
import java.util.List;

public class CameraOpticalImageStabilizer extends Activity implements Callback {
    private static final String TAG = CameraOpticalImageStabilizer.class;
    private int AXVALUEX = 435;
    private int AXVALUEY = 435;
    private int UPDATE_SWITCH = -1;
    private ArrayList<Integer> dataListOffX;
    private ArrayList<Integer> dataListOffY;
    private ArrayList<Integer> dataListOnX;
    private ArrayList<Integer> dataListOnY;
    private ArrayList<Integer> dataListRst;
    private AlertDialog dialog = null;
    private boolean isInModelTest = false;
    private Camera mCamera;
    private int mCameraDataX = 0;
    private int mCameraDataY = 0;
    private boolean mCameraInitialized = false;
    private boolean mConnected = false;
    private ExternFunction mExFunction;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.d(CameraOpticalImageStabilizer.TAG, "handler msg TRUN_ON");
                    SystemProperties.set("persist.camera.ois.disable", "0");
                    CameraOpticalImageStabilizer.this.startPreview();
                    CameraOpticalImageStabilizer.this.mtvSwitchOnX.setText("ON X");
                    CameraOpticalImageStabilizer.this.mtvSwitchOnX.setTextColor(-16711936);
                    CameraOpticalImageStabilizer.this.mtvSwitchOnX.append("\n");
                    CameraOpticalImageStabilizer.this.mtvSwitchOnY.setText("ON Y");
                    CameraOpticalImageStabilizer.this.mtvSwitchOnY.setTextColor(-16711936);
                    CameraOpticalImageStabilizer.this.mtvSwitchOnY.append("\n");
                    CameraOpticalImageStabilizer.this.mUpdateTime = 0;
                    CameraOpticalImageStabilizer.this.UPDATE_SWITCH = 0;
                    CameraOpticalImageStabilizer.this.mSumX = 0;
                    CameraOpticalImageStabilizer.this.mSumY = 0;
                    CameraOpticalImageStabilizer.this.removeAllMessage();
                    return;
                case Light.MAIN_KEY_LIGHT /*1*/:
                    Log.d(CameraOpticalImageStabilizer.TAG, "handler msg TRUN_OFF");
                    CameraOpticalImageStabilizer.this.removeAllMessage();
                    SystemProperties.set("persist.camera.ois.disable", "1");
                    CameraOpticalImageStabilizer.this.startPreview();
                    CameraOpticalImageStabilizer.this.mtvSwitchOffX.setText("OFF X");
                    CameraOpticalImageStabilizer.this.mtvSwitchOffX.setTextColor(-16711936);
                    CameraOpticalImageStabilizer.this.mtvSwitchOffX.append("\n");
                    CameraOpticalImageStabilizer.this.mtvSwitchOffY.setText("OFF Y");
                    CameraOpticalImageStabilizer.this.mtvSwitchOffY.setTextColor(-16711936);
                    CameraOpticalImageStabilizer.this.mtvSwitchOffY.append("\n");
                    CameraOpticalImageStabilizer.this.mUpdateTime = 0;
                    CameraOpticalImageStabilizer.this.UPDATE_SWITCH = 1;
                    CameraOpticalImageStabilizer.this.mSumX = 0;
                    CameraOpticalImageStabilizer.this.mSumY = 0;
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    Log.d(CameraOpticalImageStabilizer.TAG, "handler msg UPDATE_TEXT");
                    CameraOpticalImageStabilizer.this.mCameraDataX = msg.arg1;
                    CameraOpticalImageStabilizer.this.mCameraDataY = msg.arg2;
                    CameraOpticalImageStabilizer cameraOpticalImageStabilizer = CameraOpticalImageStabilizer.this;
                    cameraOpticalImageStabilizer.mSumX = cameraOpticalImageStabilizer.mSumX + CameraOpticalImageStabilizer.this.mCameraDataX;
                    CameraOpticalImageStabilizer.this.mMeanX = CameraOpticalImageStabilizer.this.mSumX / CameraOpticalImageStabilizer.this.mUpdateTime;
                    cameraOpticalImageStabilizer = CameraOpticalImageStabilizer.this;
                    cameraOpticalImageStabilizer.mSumY = cameraOpticalImageStabilizer.mSumY + CameraOpticalImageStabilizer.this.mCameraDataY;
                    CameraOpticalImageStabilizer.this.mMeanY = CameraOpticalImageStabilizer.this.mSumY / CameraOpticalImageStabilizer.this.mUpdateTime;
                    if (CameraOpticalImageStabilizer.this.UPDATE_SWITCH == 0) {
                        CameraOpticalImageStabilizer.this.dataListOnX.add(Integer.valueOf(CameraOpticalImageStabilizer.this.mCameraDataX));
                        CameraOpticalImageStabilizer.this.dataListOnY.add(Integer.valueOf(CameraOpticalImageStabilizer.this.mCameraDataY));
                        CameraOpticalImageStabilizer.this.mOnValueX = CameraOpticalImageStabilizer.this.mMeanX;
                        CameraOpticalImageStabilizer.this.mOnValueY = CameraOpticalImageStabilizer.this.mMeanY;
                        if (CameraOpticalImageStabilizer.this.mUpdateTime == 10) {
                            CameraOpticalImageStabilizer.this.mHandler.sendEmptyMessageDelayed(1, 2000);
                            CameraOpticalImageStabilizer.this.stopPreview();
                            return;
                        }
                        return;
                    } else if (CameraOpticalImageStabilizer.this.UPDATE_SWITCH == 1) {
                        CameraOpticalImageStabilizer.this.dataListOffX.add(Integer.valueOf(CameraOpticalImageStabilizer.this.mCameraDataX));
                        CameraOpticalImageStabilizer.this.dataListOffY.add(Integer.valueOf(CameraOpticalImageStabilizer.this.mCameraDataY));
                        CameraOpticalImageStabilizer.this.mOffValueX = CameraOpticalImageStabilizer.this.mMeanX;
                        CameraOpticalImageStabilizer.this.mOffValueY = CameraOpticalImageStabilizer.this.mMeanY;
                        if (CameraOpticalImageStabilizer.this.mUpdateTime == 10) {
                            CameraOpticalImageStabilizer.this.mHandler.sendEmptyMessageDelayed(3, 2000);
                            return;
                        }
                        return;
                    } else {
                        return;
                    }
                case Light.CHARGE_GREEN_LIGHT /*3*/:
                    if (CameraOpticalImageStabilizer.this.mOffValueX - CameraOpticalImageStabilizer.this.AXVALUEX == 0) {
                        CameraOpticalImageStabilizer.this.mtvResult.setText("X: Infinity FAIL, ");
                        CameraOpticalImageStabilizer.this.mtvResult.setTextColor(-65536);
                        CameraOpticalImageStabilizer.this.mtvResult.setGravity(17);
                        CameraOpticalImageStabilizer.this.resultStringX = "X: Infinity FAIL\n";
                        SystemProperties.set("persist.camera.ois.disable", "0");
                        CameraOpticalImageStabilizer.this.mHandler.sendEmptyMessageDelayed(4, 1000);
                        return;
                    }
                    int evaluationValueX = ((CameraOpticalImageStabilizer.this.mOnValueX - CameraOpticalImageStabilizer.this.AXVALUEX) * 100) / (CameraOpticalImageStabilizer.this.mOffValueX - CameraOpticalImageStabilizer.this.AXVALUEX);
                    if (CameraOpticalImageStabilizer.this.mOffValueX - CameraOpticalImageStabilizer.this.AXVALUEX > 0) {
                        Log.e(CameraOpticalImageStabilizer.TAG, "evaluation X value = " + evaluationValueX);
                    }
                    if (CameraOpticalImageStabilizer.this.mOffValueX - CameraOpticalImageStabilizer.this.AXVALUEX > 0 && evaluationValueX <= 18) {
                        CameraOpticalImageStabilizer.this.mtvResult.setText("X: " + evaluationValueX + "% PASS, ");
                        CameraOpticalImageStabilizer.this.mtvResult.setTextColor(-16711936);
                        CameraOpticalImageStabilizer.this.mtvResult.setGravity(17);
                        CameraOpticalImageStabilizer.this.resultStringX = "X: " + evaluationValueX + "% PASS\n";
                    } else if (CameraOpticalImageStabilizer.this.mOffValueX - CameraOpticalImageStabilizer.this.AXVALUEX >= 5 || CameraOpticalImageStabilizer.this.mOffValueX - CameraOpticalImageStabilizer.this.AXVALUEX <= -5) {
                        CameraOpticalImageStabilizer.this.mtvResult.setText("X: " + evaluationValueX + "% FAIL, ");
                        CameraOpticalImageStabilizer.this.mtvResult.setTextColor(-65536);
                        CameraOpticalImageStabilizer.this.mtvResult.setGravity(17);
                        CameraOpticalImageStabilizer.this.resultStringX = "X: " + evaluationValueX + "% FAIL\n";
                    } else {
                        CameraOpticalImageStabilizer.this.mtvResult.setText("X: Check the test platform, ");
                        CameraOpticalImageStabilizer.this.mtvResult.setTextColor(-65536);
                        CameraOpticalImageStabilizer.this.mtvResult.setGravity(17);
                        CameraOpticalImageStabilizer.this.resultStringX = "X: Check the test platform\n";
                    }
                    if (CameraOpticalImageStabilizer.this.mOffValueY - CameraOpticalImageStabilizer.this.AXVALUEY == 0) {
                        CameraOpticalImageStabilizer.this.mtvResult.append("Y: Infinity FAIL");
                        CameraOpticalImageStabilizer.this.mtvResult.setTextColor(-65536);
                        CameraOpticalImageStabilizer.this.mtvResult.setGravity(17);
                        CameraOpticalImageStabilizer.this.resultStringY = "Y: Infinity FAIL";
                        SystemProperties.set("persist.camera.ois.disable", "0");
                        CameraOpticalImageStabilizer.this.mHandler.sendEmptyMessageDelayed(4, 1000);
                        return;
                    }
                    int evaluationValueY = ((CameraOpticalImageStabilizer.this.mOnValueY - CameraOpticalImageStabilizer.this.AXVALUEY) * 100) / (CameraOpticalImageStabilizer.this.mOffValueY - CameraOpticalImageStabilizer.this.AXVALUEY);
                    if (CameraOpticalImageStabilizer.this.mOffValueY - CameraOpticalImageStabilizer.this.AXVALUEY > 0) {
                        Log.e(CameraOpticalImageStabilizer.TAG, "evaluation Y value = " + evaluationValueY);
                    }
                    if (CameraOpticalImageStabilizer.this.mOffValueY - CameraOpticalImageStabilizer.this.AXVALUEY > 0 && evaluationValueY <= 18) {
                        CameraOpticalImageStabilizer.this.mtvResult.append("Y: " + evaluationValueY + "% PASS");
                        CameraOpticalImageStabilizer.this.mtvResult.setGravity(17);
                        CameraOpticalImageStabilizer.this.resultStringY = "Y: " + evaluationValueY + "% PASS";
                    } else if (CameraOpticalImageStabilizer.this.mOffValueY - CameraOpticalImageStabilizer.this.AXVALUEY >= 5 || CameraOpticalImageStabilizer.this.mOffValueY - CameraOpticalImageStabilizer.this.AXVALUEY <= -5) {
                        CameraOpticalImageStabilizer.this.mtvResult.append("Y: " + evaluationValueY + "% FAIL");
                        CameraOpticalImageStabilizer.this.mtvResult.setTextColor(-65536);
                        CameraOpticalImageStabilizer.this.mtvResult.setGravity(17);
                        CameraOpticalImageStabilizer.this.resultStringY = "Y: " + evaluationValueY + "% FAIL";
                    } else {
                        CameraOpticalImageStabilizer.this.mtvResult.append("Y: Check the test platform");
                        CameraOpticalImageStabilizer.this.mtvResult.setTextColor(-65536);
                        CameraOpticalImageStabilizer.this.mtvResult.setGravity(17);
                        CameraOpticalImageStabilizer.this.resultStringY = "Y: Check the test platform";
                    }
                    SystemProperties.set("persist.camera.ois.disable", "0");
                    CameraOpticalImageStabilizer.this.mHandler.sendEmptyMessageDelayed(4, 1000);
                    return;
                case 4:
                    Intent intent = new Intent();
                    intent.setClass(CameraOpticalImageStabilizer.this, CameraResultPage.class);
                    Bundle bundle = new Bundle();
                    ArrayList list = new ArrayList();
                    CameraOpticalImageStabilizer.this.dataListRst.add(Integer.valueOf(CameraOpticalImageStabilizer.this.mSumBadFrameSwitchOn));
                    CameraOpticalImageStabilizer.this.dataListRst.add(Integer.valueOf(CameraOpticalImageStabilizer.this.mSumBadFrameSwitchOff));
                    list.add(CameraOpticalImageStabilizer.this.dataListOnX);
                    list.add(CameraOpticalImageStabilizer.this.dataListOnY);
                    list.add(CameraOpticalImageStabilizer.this.dataListOffX);
                    list.add(CameraOpticalImageStabilizer.this.dataListOffY);
                    list.add(CameraOpticalImageStabilizer.this.dataListRst);
                    bundle.putParcelableArrayList("list", list);
                    bundle.putString("result", CameraOpticalImageStabilizer.this.resultStringX + CameraOpticalImageStabilizer.this.resultStringY);
                    if (CameraOpticalImageStabilizer.this.getIntent().getBooleanExtra("model_test", false)) {
                        bundle.putBoolean("model_test", true);
                    }
                    intent.putExtras(bundle);
                    CameraOpticalImageStabilizer.this.startActivityForResult(intent, 1);
                    return;
                case 5:
                    Log.d(CameraOpticalImageStabilizer.TAG, "handler msg UPDATE_WARNING");
                    CameraOpticalImageStabilizer.this.mtvResult.setText("Preview is over exposure, check Test Environment");
                    CameraOpticalImageStabilizer.this.mtvResult.setTextColor(-65536);
                    CameraOpticalImageStabilizer.this.mtvResult.setGravity(17);
                    return;
                default:
                    return;
            }
        }
    };
    private int mMeanX = 0;
    private int mMeanY = 0;
    private final MetaDataCallback mMetaDataCallback = new MetaDataCallback();
    private Handler mNVHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                CameraOpticalImageStabilizer.this.mConnected = true;
            }
        }
    };
    private int mOffValueX = 0;
    private int mOffValueY = 0;
    private int mOnValueX = 0;
    private int mOnValueY = 0;
    private SurfaceView mPreview;
    private boolean mPreviewActive = false;
    private int mSumBadFrameSwitchOff = 0;
    private int mSumBadFrameSwitchOn = 0;
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
    private String resultStringX;
    private String resultStringY;
    List<Size> sizes;
    private boolean startTest = true;

    private final class MetaDataCallback implements CameraMetaDataCallback {
        private MetaDataCallback() {
        }

        public void onCameraMetaData(byte[] data, Camera camera) {
            int[] metadata = new int[4];
            if (CameraOpticalImageStabilizer.this.startTest && data.length == 16) {
                for (int i = 0; i < 4; i++) {
                    metadata[i] = byteToInt(data, i * 4);
                }
                Log.e(CameraOpticalImageStabilizer.TAG, "onCameraMetaData " + metadata[0] + ", " + metadata[1] + ", " + metadata[2] + ", " + metadata[3]);
                if ((metadata[0] == 9 || metadata[0] == 10) && CameraOpticalImageStabilizer.this.mUpdateTime <= 10) {
                    CameraOpticalImageStabilizer cameraOpticalImageStabilizer;
                    if (CameraOpticalImageStabilizer.this.UPDATE_SWITCH == 0) {
                        CameraOpticalImageStabilizer.this.mtvSwitchOnX.append(Integer.toString(metadata[2]));
                        CameraOpticalImageStabilizer.this.mtvSwitchOnX.append("\n");
                        CameraOpticalImageStabilizer.this.mtvSwitchOnY.append(Integer.toString(metadata[3]));
                        CameraOpticalImageStabilizer.this.mtvSwitchOnY.append("\n");
                    } else if (CameraOpticalImageStabilizer.this.UPDATE_SWITCH == 1) {
                        CameraOpticalImageStabilizer.this.mtvSwitchOffX.append(Integer.toString(metadata[2]));
                        CameraOpticalImageStabilizer.this.mtvSwitchOffX.append("\n");
                        CameraOpticalImageStabilizer.this.mtvSwitchOffY.append(Integer.toString(metadata[3]));
                        CameraOpticalImageStabilizer.this.mtvSwitchOffY.append("\n");
                    }
                    if (metadata[0] == 10 || metadata[2] < 80 || metadata[2] > 240 || metadata[3] < 80 || metadata[3] > 240) {
                        if (CameraOpticalImageStabilizer.this.UPDATE_SWITCH == 0) {
                            cameraOpticalImageStabilizer = CameraOpticalImageStabilizer.this;
                            cameraOpticalImageStabilizer.mSumBadFrameSwitchOn = cameraOpticalImageStabilizer.mSumBadFrameSwitchOn + 1;
                            if (CameraOpticalImageStabilizer.this.mSumBadFrameSwitchOn == 1) {
                                return;
                            }
                        } else if (CameraOpticalImageStabilizer.this.UPDATE_SWITCH == 1) {
                            cameraOpticalImageStabilizer = CameraOpticalImageStabilizer.this;
                            cameraOpticalImageStabilizer.mSumBadFrameSwitchOff = cameraOpticalImageStabilizer.mSumBadFrameSwitchOff + 1;
                            if (CameraOpticalImageStabilizer.this.mSumBadFrameSwitchOff == 1) {
                                return;
                            }
                        }
                    }
                    cameraOpticalImageStabilizer = CameraOpticalImageStabilizer.this;
                    cameraOpticalImageStabilizer.mUpdateTime = cameraOpticalImageStabilizer.mUpdateTime + 1;
                    if (CameraOpticalImageStabilizer.this.mUpdateTime <= 10) {
                        Message msg = CameraOpticalImageStabilizer.this.mHandler.obtainMessage();
                        msg.what = 2;
                        msg.arg1 = metadata[2];
                        msg.arg2 = metadata[3];
                        CameraOpticalImageStabilizer.this.mHandler.sendMessage(msg);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.isInModelTest = false;
        setResult(1);
        finish();
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
        this.mtvResult = (TextView) findViewById(2131492990);
        this.dataListOnX = new ArrayList();
        this.dataListOnY = new ArrayList();
        this.dataListOffX = new ArrayList();
        this.dataListOffY = new ArrayList();
        this.dataListRst = new ArrayList();
        SharedPreferences sp = getSharedPreferences("com.android.engineeringmode_preferences", 0);
        this.AXVALUEX = sp.getInt("key_is_ois_stillx_mean_value", 435);
        this.AXVALUEY = sp.getInt("key_is_ois_stilly_mean_value", 435);
        Log.v(TAG, "AXVALUE X " + this.AXVALUEX + ", AXVALUE Y " + this.AXVALUEY);
        this.mExFunction = new ExternFunction(this);
        this.mExFunction.registerOnServiceConnected(this.mNVHandler, 1, null);
    }

    private void dialog() {
        Builder builder = new Builder(this);
        builder.setMessage("确认进行光学防抖测试吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                CameraOpticalImageStabilizer.this.startTest = true;
                dialog.dismiss();
            }
        });
        this.dialog = builder.create();
        this.dialog.getWindow().setType(2003);
        this.dialog.setCancelable(false);
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.show();
    }

    protected void onDestroy() {
        if (this.mExFunction != null) {
            this.mExFunction.unregisterOnServiceConnected(this.mNVHandler);
            this.mExFunction.dispose();
        }
        if (this.dialog != null) {
            this.dialog.dismiss();
        }
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
        this.mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(26, TAG);
        this.mWakeLock.acquire();
        try {
            this.mCamera = Camera.openLegacy(0, 256);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Can't open the Camera", 1).show();
            if (this.mCamera != null) {
                this.mCamera.release();
                this.mCamera = null;
            }
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
                    this.mHandler.sendEmptyMessageDelayed(0, 2000);
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

    private void stopPreview() {
        if (this.mCamera != null) {
            if (this.mPreviewActive) {
                this.mCamera.stopPreview();
            }
            this.mPreviewActive = false;
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
