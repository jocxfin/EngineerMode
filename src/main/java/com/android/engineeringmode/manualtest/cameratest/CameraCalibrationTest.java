package com.android.engineeringmode.manualtest.cameratest;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.manualtest.cameratest.CameraTestPreview.SufaceCreatedCallback;
import com.android.engineeringmode.util.ExternFunction;
import com.oppo.autotest.media.DualCamCaliTestJNI;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CameraCalibrationTest extends Activity {
    private static int SCREEN_HEIGHT;
    private static int SCREEN_WIDTH;
    private static int calculateTimeOutStatus = -1;
    private static int calibrationStatus = -1;
    private static int deInitCaliStatus = -1;
    private static int initCaliStatus = -1;
    private static boolean mCaliResultWriteStatus = false;
    private static int mDynamicStatus = 0;
    private static int mInstallAbsStatus = -1;
    static String[] mPermissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA", "android.permission.RECORD_AUDIO"};
    private static boolean mbStartPreviewed = false;
    private final String TAG = "CameraCalibrationTest";
    private byte[] calibrationOutput;
    private float[] calibrationResults;
    private int[] lensShift;
    private final AutoFocusCallback mAutoFocusCallback = new AutoFocusCallback() {
        public void onAutoFocus(boolean succcess, Camera camera) {
            Log.d("CameraCalibrationTest", "AutoFocus focused = " + succcess + ", mbTestStart" + CameraCalibrationTest.this.mbTestStart);
            CameraCalibrationTest.this.mbFocused = succcess;
            if (succcess) {
                if (CameraCalibrationTest.this.mbTestStart) {
                    CameraCalibrationTest.this.mCamera.autoFocus(null);
                }
                if (CameraCalibrationTest.this.mCameraId == 2) {
                    CameraCalibrationTest.this.mTimerView.onStart(3);
                    CameraCalibrationTest.this.mMyHandler.sendEmptyMessageDelayed(1001, 4500);
                }
            }
        }
    };
    private Thread mCalculateThread;
    private Camera mCamera;
    private int mCameraId = -1;
    private CameraTestPreview mCameraPreview;
    private RelativeLayout mContentLayout;
    private ExternFunction mExFunction;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                CameraCalibrationTest.this.mPcb = CameraCalibrationTest.this.mExFunction.getPCBNumber();
                Log.i("CameraCalibrationTest", "Connected");
            }
        }
    };
    private boolean mInitFirst = false;
    private InitInputParamsTask mInitParamTask1;
    private InitInputParamsTask mInitParamTask2;
    private boolean mInitSecond = false;
    private int mLastOrientation = 0;
    private Handler mMyHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1000:
                    if (CameraCalibrationTest.this.mCameraId == 0) {
                        CameraCalibrationTest.this.mCameraId = 2;
                    } else {
                        CameraCalibrationTest.this.mCameraId = 0;
                    }
                    if (!CameraCalibrationTest.this.switchCamera(CameraCalibrationTest.this.mCameraId)) {
                        CameraCalibrationTest.this.mMyHandler.removeMessages(1001);
                        return;
                    }
                    return;
                case 1001:
                    CameraCalibrationTest.this.takePicture();
                    return;
                case 1003:
                    CameraCalibrationTest.this.mTakePicNum = 0;
                    CameraCalibrationTest.this.mTimerView.setText("拍照完成，正在计算标定结果...");
                    return;
                case 1004:
                    CameraCalibrationTest.calculateTimeOutStatus = 0;
                    CameraCalibrationTest.this.mCalculateThread = new Thread(new Runnable() {
                        public void run() {
                            CameraCalibrationTest.this.calculateCalResult();
                            CameraCalibrationTest.this.writeResult2File();
                        }
                    });
                    CameraCalibrationTest.this.mCalculateThread.start();
                    CameraCalibrationTest.this.mMyHandler.sendEmptyMessageDelayed(1006, 20000);
                    return;
                case 1005:
                    CameraCalibrationTest.this.showCaliStatusDialog();
                    return;
                case 1006:
                    CameraCalibrationTest.calculateTimeOutStatus = 1;
                    CameraCalibrationTest.this.showCaliStatusDialog();
                    return;
                default:
                    return;
            }
        }
    };
    private MyOrientationEventListener mMyOrientationEventListener = null;
    Parameters mParameters;
    private String mPcb = "";
    private String mPictureFilename = null;
    private String mPictureSavePath = null;
    private SavePictureTask mSavePictureTask1;
    private SavePictureTask mSavePictureTask2;
    private ImageButton mShutterBtn;
    private final ShutterCallback mShutterCallbakc = new ShutterCallback() {
        public void onShutter() {
        }
    };
    private SufaceCreatedCallback mSufaceCreatedCallback = new SufaceCreatedCallback() {
        public void onSurfaceChanged(SurfaceHolder holder) {
            Log.d("CameraCalibrationTest", "onSurfaceChanged");
        }

        public void onSurfaceCreated(SurfaceHolder holder) {
            if (!CameraCalibrationTest.mbStartPreviewed) {
                try {
                    CameraCalibrationTest.this.mCamera.setPreviewDisplay(holder);
                    CameraCalibrationTest.this.mCamera.startPreview();
                    CameraCalibrationTest.mbStartPreviewed = true;
                    Log.d("CameraCalibrationTest", "onSurfaceCreated startPreview ");
                } catch (IOException e) {
                    Log.d("CameraCalibrationTest", "startPreview Error: " + e.getMessage());
                }
            }
        }
    };
    private boolean mTakePicDone = true;
    private int mTakePicNum = 0;
    private TimerView mTimerView;
    private byte[] mainBitmapArray;
    private int[] mainCalibrationImageData;
    private int[] mainPicSize;
    private boolean mbFocused = false;
    private boolean mbTestStart = false;
    private float[] moduleValues;
    private int[] oisData;
    private float pitchModuleValue = 0.0f;
    private float pitchValue = 0.0f;
    private float rollModuleValue = 0.0f;
    private float rollValue = 0.0f;
    private byte[] subBitmapArray;
    private int[] subCalibrationImageData;
    private int[] subPicSize;
    private String systemParamPath;
    private float yawModuleValue = 0.0f;
    private float yawValue = 0.0f;

    private class InitInputParamsTask extends AsyncTask<Integer, Integer, String> {
        private int mHeight;
        private int mInitNum;
        private byte[] mJpegData;
        private int mWidth;

        public InitInputParamsTask(byte[] data, int w, int h, int num) {
            this.mJpegData = data;
            this.mWidth = w;
            this.mHeight = h;
            this.mInitNum = num;
        }

        protected String doInBackground(Integer... params) {
            Bitmap bm = BitmapFactory.decodeByteArray(this.mJpegData, 0, this.mJpegData.length);
            CameraCalibrationTest.this.initInputParams(CameraCaliUtil.getNV21(bm.getWidth(), bm.getHeight(), bm), bm.getWidth(), bm.getHeight(), this.mInitNum);
            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (this.mInitNum == 1) {
                CameraCalibrationTest.this.mInitFirst = true;
            }
            if (this.mInitNum == 2) {
                CameraCalibrationTest.this.mInitSecond = true;
            }
            if (CameraCalibrationTest.this.mInitFirst && CameraCalibrationTest.this.mInitSecond) {
                CameraCalibrationTest.this.mMyHandler.sendEmptyMessageDelayed(1004, 100);
            }
        }
    }

    private class MyOrientationEventListener extends OrientationEventListener {
        public MyOrientationEventListener(Context context) {
            super(context);
        }

        public void onOrientationChanged(int orientation) {
            orientation = roundOrientation(orientation);
            if (CameraCalibrationTest.this.mLastOrientation != orientation) {
                CameraCalibrationTest.this.mLastOrientation = orientation;
                if (CameraCalibrationTest.this.mTimerView != null) {
                    CameraCalibrationTest.this.mTimerView.setRotation((float) (-CameraCalibrationTest.this.mLastOrientation));
                }
            }
        }

        public int roundOrientation(int orientationInput) {
            int orientation = orientationInput;
            if (orientationInput == -1) {
                orientation = 0;
            }
            orientation %= 360;
            if (orientation < 45) {
                return 0;
            }
            if (orientation < 135) {
                return 90;
            }
            if (orientation < 225) {
                return 180;
            }
            if (orientation < 315) {
                return 270;
            }
            return 0;
        }
    }

    private class SavePictureTask extends AsyncTask<Integer, Integer, String> {
        private int mHeight;
        private byte[] mJpegData;
        private int mSaveNum;
        private int mWidth;

        public SavePictureTask(byte[] data, int w, int h, int num) {
            this.mJpegData = data;
            this.mWidth = w;
            this.mHeight = h;
            this.mSaveNum = num;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Integer... params) {
            CameraCalibrationTest.this.savePicture(this.mJpegData, this.mWidth, this.mHeight, this.mSaveNum);
            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public static class TimerView extends View {
        private static int mStartNum;
        private boolean isNeedDraw = false;
        private Canvas mCanvas;
        private Context mContext;
        private Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        TimerView.mStartNum = TimerView.mStartNum - 1;
                        TimerView.this.invalidate();
                        TimerView.this.startAnimation();
                        if (TimerView.mStartNum > 0) {
                            TimerView.this.mHandler.sendEmptyMessageDelayed(1000, 1000);
                            return;
                        } else {
                            TimerView.this.mHandler.sendEmptyMessageDelayed(1001, 750);
                            return;
                        }
                    case 1001:
                        TimerView.mStartNum = TimerView.mStartNum - 1;
                        TimerView.this.setScaleX(2.0f);
                        TimerView.this.setScaleY(2.0f);
                        TimerView.this.setAlpha(1.0f);
                        TimerView.this.invalidate();
                        return;
                    default:
                        return;
                }
            }
        };
        private Paint mPaint;
        private String mText;

        public TimerView(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.mContext = context;
            mStartNum = 3;
            this.isNeedDraw = false;
            Paint p = new Paint();
            p.setTextSize(150.0f);
            p.setAntiAlias(true);
            p.setTextAlign(Align.CENTER);
            p.setColor(-256);
            p.setAlpha(200);
            p.setStyle(Style.FILL);
            this.mPaint = p;
        }

        protected void onDraw(Canvas canvas) {
            this.mCanvas = canvas;
            if (this.isNeedDraw) {
                FontMetricsInt fontMetrics = this.mPaint.getFontMetricsInt();
                int baseline = (((getMeasuredHeight() - fontMetrics.bottom) + fontMetrics.top) / 2) - fontMetrics.top;
                if (this.mText != null && !"".equals(this.mText)) {
                    this.mPaint.setTextSize(30.0f);
                    this.mCanvas.drawText(this.mText, (float) (getMeasuredWidth() / 2), (float) baseline, this.mPaint);
                } else if (mStartNum >= 0) {
                    this.mPaint.setTextSize(150.0f);
                    this.mCanvas.drawText("" + mStartNum, (float) (getMeasuredWidth() / 2), (float) baseline, this.mPaint);
                } else {
                    this.mPaint.setTextSize(50.0f);
                    this.mCanvas.drawText(this.mContext.getString(2131297655), (float) (getMeasuredWidth() / 2), (float) baseline, this.mPaint);
                }
            }
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
        }

        public void onStart(int statrNum) {
            setVisibility(0);
            this.isNeedDraw = true;
            mStartNum = statrNum;
            invalidate();
            startAnimation();
            this.mHandler.sendEmptyMessageDelayed(1000, 1000);
        }

        public void startAnimation() {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(500);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", new float[]{4.0f, 0.2f});
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", new float[]{4.0f, 0.2f});
            animatorSet.play(scaleX).with(scaleY).with(ObjectAnimator.ofFloat(this, "alpha", new float[]{1.0f, 0.0f}));
            animatorSet.start();
        }

        public void setText(String text) {
            this.mText = text;
            invalidate();
        }

        public void onStop() {
            this.isNeedDraw = false;
            setVisibility(8);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(128, 128);
        setContentView(2130903069);
        this.mCameraId = 0;
        SCREEN_WIDTH = getResources().getDisplayMetrics().widthPixels;
        SCREEN_HEIGHT = getResources().getDisplayMetrics().heightPixels;
        this.mCameraPreview = new CameraTestPreview(this);
        this.mContentLayout = (RelativeLayout) findViewById(2131493016);
        this.mContentLayout.addView(this.mCameraPreview);
        this.mTimerView = (TimerView) findViewById(2131492984);
        this.mShutterBtn = (ImageButton) findViewById(2131492985);
        this.mShutterBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Log.d("CameraCalibrationTest", "mCameraId = " + CameraCalibrationTest.this.mCameraId + ", mbFocused = " + CameraCalibrationTest.this.mbFocused + ", mbTestStart = " + CameraCalibrationTest.this.mbTestStart);
                if (CameraCalibrationTest.this.mCameraId >= 0 && !CameraCalibrationTest.this.mbTestStart) {
                    if (CameraCalibrationTest.this.mbFocused) {
                        CameraCalibrationTest.this.mbTestStart = true;
                        CameraCalibrationTest.this.setShutterBtnProperty(0, false);
                        CameraCalibrationTest.this.mTimerView.onStart(3);
                        CameraCalibrationTest.this.mMyHandler.sendEmptyMessageDelayed(1001, 3500);
                    } else {
                        CameraCalibrationTest.this.mbTestStart = false;
                        CameraCalibrationTest.this.setShutterBtnProperty(0, true);
                    }
                }
            }
        });
        this.mMyOrientationEventListener = new MyOrientationEventListener(this);
        this.mExFunction = new ExternFunction(this);
        this.mExFunction.registerOnServiceConnected(this.mHandler, 1, null);
    }

    private Camera getCameraInstance(int id) {
        Camera c = null;
        try {
            c = Camera.open(id);
        } catch (Exception e) {
            Log.e("CameraCalibrationTest", "getCameraInstance fail to open camera");
        }
        return c;
    }

    protected void onResume() {
        super.onResume();
        Log.d("CameraCalibrationTest", "onResume");
        this.mbTestStart = false;
        if (checkPermissionEnable()) {
            switchCamera(this.mCameraId);
        } else {
            grantRuntimePermissions();
        }
        this.mTakePicDone = true;
        this.mMyOrientationEventListener.enable();
    }

    protected void onPause() {
        super.onPause();
        this.mTakePicDone = true;
        this.mMyHandler.removeMessages(1004);
        this.mMyHandler.removeMessages(1003);
        this.mMyHandler.removeMessages(1005);
        this.mMyHandler.removeMessages(1006);
        this.mMyHandler.removeMessages(1000);
        this.mMyHandler.removeMessages(1001);
        this.mMyOrientationEventListener.disable();
        if (this.mCamera != null) {
            this.mCamera.release();
            this.mCamera = null;
            this.mCameraPreview.setCamera(null);
        }
    }

    protected void onDestroy() {
        if (this.mExFunction != null) {
            this.mExFunction.unregisterOnServiceConnected(this.mHandler);
            this.mExFunction.dispose();
        }
        super.onDestroy();
    }

    protected int getJpegRotation(int cameraId, int orientation) {
        if (orientation == -1) {
            return 0;
        }
        if (cameraId != 1) {
            return (orientation + 90) % 360;
        }
        if (orientation == 90 || orientation == 270) {
            return (orientation + 90) % 360;
        }
        return (orientation + 270) % 360;
    }

    private void setShutterBtnProperty(int visibility, boolean enable) {
        if (this.mShutterBtn != null) {
            this.mShutterBtn.setVisibility(visibility);
            this.mShutterBtn.setEnabled(enable);
        }
    }

    private void takePicture() {
        Log.i("CameraCalibrationTest", "cameraId = " + this.mCameraId + ", mTakePicDone = " + this.mTakePicDone + ", start takepicture:");
        if (this.mCamera == null || !this.mTakePicDone) {
            this.mTakePicDone = true;
            Log.i("CameraCalibrationTest", "takePicture fail");
            return;
        }
        this.mTakePicDone = false;
        this.mParameters.setRotation(getJpegRotation(0, this.mLastOrientation));
        this.mCamera.setParameters(this.mParameters);
        this.mCamera.takePicture(this.mShutterCallbakc, null, new PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                int width;
                int height;
                CameraCalibrationTest cameraCalibrationTest = CameraCalibrationTest.this;
                cameraCalibrationTest.mTakePicNum = cameraCalibrationTest.mTakePicNum + 1;
                Size size = CameraCalibrationTest.this.mCamera.getParameters().getPictureSize();
                switch (CameraCalibrationTest.this.mLastOrientation) {
                    case 0:
                    case 180:
                        width = size.height;
                        height = size.width;
                        break;
                    default:
                        width = size.width;
                        height = size.height;
                        break;
                }
                if (CameraCalibrationTest.this.mTakePicNum == 1) {
                    CameraCalibrationTest.this.mInitParamTask1 = new InitInputParamsTask(data, width, height, CameraCalibrationTest.this.mTakePicNum);
                    CameraCalibrationTest.this.mInitParamTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Integer[0]);
                    CameraCalibrationTest.this.mSavePictureTask1 = new SavePictureTask(data, width, height, CameraCalibrationTest.this.mTakePicNum);
                    CameraCalibrationTest.this.mSavePictureTask1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Integer[0]);
                    CameraCalibrationTest.this.mMyHandler.sendEmptyMessageDelayed(1000, 500);
                } else if (CameraCalibrationTest.this.mTakePicNum == 2) {
                    CameraCalibrationTest.this.mInitParamTask2 = new InitInputParamsTask(data, width, height, CameraCalibrationTest.this.mTakePicNum);
                    CameraCalibrationTest.this.mInitParamTask2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Integer[0]);
                    CameraCalibrationTest.this.mSavePictureTask2 = new SavePictureTask(data, width, height, CameraCalibrationTest.this.mTakePicNum);
                    CameraCalibrationTest.this.mSavePictureTask2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Integer[0]);
                    CameraCalibrationTest.this.mMyHandler.sendEmptyMessageDelayed(1003, 500);
                }
                CameraCalibrationTest.this.mCamera.startPreview();
                if (CameraCalibrationTest.this.mCameraId == 0) {
                    Toast.makeText(CameraCalibrationTest.this, CameraCalibrationTest.this.getResources().getString(2131297653), 0).show();
                } else if (CameraCalibrationTest.this.mCameraId == 2) {
                    Toast.makeText(CameraCalibrationTest.this, CameraCalibrationTest.this.getResources().getString(2131297654), 0).show();
                }
                CameraCalibrationTest.this.mTakePicDone = true;
            }
        });
    }

    private void savePicture(byte[] data, int width, int height, int num) {
        if (data != null) {
            Log.i("CameraCalibrationTest", "take photo, ok!");
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            String picName = "";
            if (num == 1) {
                picName = generatePictureFilename(0);
            } else if (num == 2) {
                picName = generatePictureFilename(2);
            }
            File myCaptureFile = new File(picName);
            if (myCaptureFile.exists()) {
                myCaptureFile.delete();
            }
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                bm.compress(CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                Log.i("CameraCalibrationTest", "save picture, ok!");
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        Log.i("CameraCalibrationTest", "save take pic, failed!");
    }

    private String generatePictureFilename(int cameraId) {
        long dateTaken = System.currentTimeMillis();
        String filename = "CaliPic" + ".jpg";
        if (cameraId == 0) {
            filename = "REAR_CAMERA-" + filename;
        } else if (this.mCameraId == 2) {
            filename = "SECOND_REAR_CAMERA-" + filename;
        } else if (this.mCameraId == 1) {
            filename = "FRONT_CAMERA-" + filename;
        }
        String userDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String str = userDir;
        this.mPictureSavePath = userDir;
        String path = this.mPictureSavePath + "/" + filename;
        File cameraDir = new File(this.mPictureSavePath);
        if (cameraDir.exists() || cameraDir.mkdirs()) {
            this.mPictureFilename = path;
            return path;
        }
        this.mPictureFilename = null;
        return null;
    }

    private void initInputParams(byte[] YuvData, int width, int height, int num) {
        Log.d("CameraCalibrationTest", "cameraid = " + this.mCameraId + ", initnum = " + num);
        if (num == 1) {
            File caliResultPath = new File("/persist/dual_camera_calibration/");
            if (!(caliResultPath.exists() || caliResultPath.mkdirs())) {
                Log.e("CameraCalibrationTest", "mkdir /persist/dual_camera_calibration/ failed");
            }
            if (caliResultPath.exists()) {
                CameraCaliUtil.setFileChmod(caliResultPath, "chmod 777");
            }
            CameraCaliUtil.saveAssetsToSdcard(this, "dual_camera_cali_param", "CalibrationParams.json", "/persist/dual_camera_calibration");
            this.systemParamPath = "/persist/dual_camera_calibration/CalibrationParams.json";
            this.mainPicSize = new int[2];
            this.mainPicSize[0] = width;
            this.mainPicSize[1] = height;
            this.mainBitmapArray = YuvData;
            this.mainCalibrationImageData = new int[]{1, width, height, width, height};
        } else if (num == 2) {
            this.subPicSize = new int[2];
            this.subPicSize[0] = width;
            this.subPicSize[1] = height;
            this.subBitmapArray = YuvData;
            this.subCalibrationImageData = new int[]{1, width, height, width, height};
            this.lensShift = new int[]{0, 0};
            this.oisData = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            this.calibrationOutput = new byte[512];
            this.calibrationResults = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        }
        Log.d("CameraCalibrationTest", "initparam, ok");
    }

    private boolean switchCamera(int cameraId) {
        Log.d("CameraCalibrationTest", "switch camera id = " + cameraId);
        if (this.mCamera != null) {
            this.mCamera.release();
            this.mCamera = null;
            this.mCameraPreview.setCamera(null);
        }
        this.mCameraPreview.setOnTouchListener(null);
        this.mCamera = getCameraInstance(cameraId);
        if (this.mCamera == null) {
            if (this.mCameraId == 0) {
                showCameraOpenFailedDialog("主摄无法打开");
            } else if (this.mCameraId == 2) {
                showCameraOpenFailedDialog("副摄无法打开");
            }
            return false;
        }
        mbStartPreviewed = false;
        this.mbFocused = false;
        this.mCameraPreview.setSurfaceCreatedCallbac(this.mSufaceCreatedCallback);
        this.mParameters = this.mCamera.getParameters();
        this.mParameters.setFocusMode("continuous-picture");
        this.mCamera.autoFocus(this.mAutoFocusCallback);
        this.mParameters.setRotation(getJpegRotation(this.mCameraId, this.mLastOrientation));
        List<Size> picSizes = this.mParameters.getSupportedPictureSizes();
        List<Size> previewSizes = this.mParameters.getSupportedPreviewSizes();
        Size previewSize1 = this.mParameters.getPreviewSize();
        Size pictureSize = CameraCaliUtil.getMaxSize(picSizes);
        Size previewSize = CameraCaliUtil.getMaxSize(previewSizes);
        LayoutParams params = new LayoutParams(SCREEN_WIDTH, SCREEN_HEIGHT);
        if (pictureSize != null) {
            if (Math.abs((((double) pictureSize.width) / ((double) pictureSize.height)) - 1.3333333333333333d) < 0.015d) {
                previewSize = CameraCaliUtil.getMaxSize(previewSizes, 0, SCREEN_WIDTH);
                params = new LayoutParams(SCREEN_WIDTH, (SCREEN_WIDTH * 4) / 3);
                params.topMargin = 114;
                this.mContentLayout.setBackgroundColor(-16777216);
            } else if (Math.abs((((double) pictureSize.width) / ((double) pictureSize.height)) - 1.7777777777777777d) < 0.015d) {
                previewSize = CameraCaliUtil.getMaxSize(previewSizes, 1, SCREEN_WIDTH);
                params = new LayoutParams(SCREEN_WIDTH, (SCREEN_WIDTH * 16) / 9);
            }
            this.mCameraPreview.setLayoutParams(params);
            this.mParameters.setPictureSize(pictureSize.width, pictureSize.height);
        }
        if (previewSize != null) {
            this.mParameters.setPreviewSize(previewSize.width, previewSize.height);
        }
        this.mParameters.set("zsl", "on");
        this.mCamera.setParameters(this.mParameters);
        this.mCamera.setDisplayOrientation(90);
        this.mCameraPreview.setCamera(this.mCamera);
        if (!mbStartPreviewed) {
            try {
                this.mCamera.setPreviewDisplay(this.mCameraPreview.getHolder());
                this.mCamera.startPreview();
                Log.d("CameraCalibrationTest", "start preview success");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CameraCalibrationTest", "start preview failed");
            }
        }
        return true;
    }

    private void calculateCalResult() {
        Log.d("CameraCalibrationTest", "calculateCalResult, start");
        initCaliStatus = -1;
        calibrationStatus = -1;
        deInitCaliStatus = -1;
        try {
            DualCamCaliTestJNI camCaliTestJNI = new DualCamCaliTestJNI();
            initCaliStatus = camCaliTestJNI.initCalibration(this.systemParamPath, this.mainPicSize, this.subPicSize);
            calibrationStatus = camCaliTestJNI.processCalibration(this.mainBitmapArray, this.subBitmapArray, this.mainCalibrationImageData, this.subCalibrationImageData, this.lensShift, this.oisData, this.calibrationOutput, this.calibrationResults);
            deInitCaliStatus = camCaliTestJNI.deInitCalibration();
            this.mainBitmapArray = null;
        } catch (Exception e) {
            e.printStackTrace();
            this.mainBitmapArray = null;
        } catch (Throwable th) {
            this.mainBitmapArray = null;
            this.subBitmapArray = null;
        }
        this.subBitmapArray = null;
        Log.d("CameraCalibrationTest", "calculateCalResult, end");
    }

    private void writeResult2File() {
        Exception e;
        Throwable th;
        mCaliResultWriteStatus = false;
        File persistFile = new File("/persist/dual_camera_calibration/calibrationOutput.bin");
        File resultsFile = new File("/persist/dual_camera_calibration/calibrationResultsAndOTP.csv");
        File resultsFileUsed = new File("/persist/dual_camera_calibration/calibrationResults.csv");
        File caliResultPath = new File("/persist/dual_camera_calibration/");
        FileWriter fileWriter = null;
        FileWriter resultsUsedOut = null;
        if (!(caliResultPath.exists() || caliResultPath.mkdirs())) {
            mCaliResultWriteStatus = false;
        }
        FileOutputStream fileOutputStream = null;
        try {
            if (calibrationStatus == 0) {
                if (caliResultPath.exists()) {
                    CameraCaliUtil.setFileChmod(caliResultPath, "chmod 777");
                }
                if (!persistFile.exists()) {
                    persistFile.createNewFile();
                }
                FileOutputStream outputStream = new FileOutputStream(persistFile, true);
                try {
                    outputStream.write(this.calibrationOutput);
                    if (!resultsFileUsed.exists()) {
                        resultsFileUsed.createNewFile();
                    }
                    FileWriter resultsUsedOut2 = new FileWriter(resultsFileUsed, true);
                    int i = 0;
                    while (i < this.calibrationResults.length) {
                        try {
                            if (i == this.calibrationResults.length - 1) {
                                resultsUsedOut2.write(this.calibrationResults[i] + "" + "\n");
                            } else {
                                resultsUsedOut2.write(this.calibrationResults[i] + "" + ",");
                            }
                            i++;
                        } catch (Exception e2) {
                            e = e2;
                            fileOutputStream = outputStream;
                            resultsUsedOut = resultsUsedOut2;
                        } catch (Throwable th2) {
                            th = th2;
                            fileOutputStream = outputStream;
                            resultsUsedOut = resultsUsedOut2;
                        }
                    }
                    if (persistFile.exists() && !persistFile.setReadable(true, false)) {
                        Log.d("CameraCalibrationTest", "setReadable /persist/dual_camera_calibration/calibrationOutput.bin failed!");
                    }
                    if (resultsFileUsed.exists() && !resultsFileUsed.setReadable(true, false)) {
                        Log.d("CameraCalibrationTest", "setReadable /persist/dual_camera_calibration/calibrationResults.csv failed!");
                    }
                    if (!resultsFile.exists()) {
                        resultsFile.createNewFile();
                    }
                    CameraCaliUtil.setFileChmod(resultsFile, "chmod 755");
                    FileWriter resultsOut = new FileWriter(resultsFile, true);
                    try {
                        resultsOut.write(this.mPcb + ",");
                        for (i = 1; i < 4; i++) {
                            resultsOut.write(this.calibrationResults[i] + "" + ",");
                        }
                        fileOutputStream = outputStream;
                        resultsUsedOut = resultsUsedOut2;
                        fileWriter = resultsOut;
                    } catch (Exception e3) {
                        e = e3;
                        fileOutputStream = outputStream;
                        resultsUsedOut = resultsUsedOut2;
                        fileWriter = resultsOut;
                        try {
                            e.printStackTrace();
                            mCaliResultWriteStatus = false;
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (Exception e4) {
                                    e4.printStackTrace();
                                }
                            }
                            if (fileWriter != null) {
                                fileWriter.close();
                            }
                            if (resultsUsedOut != null) {
                                resultsUsedOut.close();
                            }
                            performCalibrationMessage();
                            this.mMyHandler.post(new Runnable() {
                                public void run() {
                                    CameraCalibrationTest.this.mMyHandler.removeMessages(1006);
                                    CameraCalibrationTest.this.showCaliStatusDialog();
                                }
                            });
                        } catch (Throwable th3) {
                            th = th3;
                            if (fileOutputStream != null) {
                                try {
                                    fileOutputStream.close();
                                } catch (Exception e42) {
                                    e42.printStackTrace();
                                    throw th;
                                }
                            }
                            if (fileWriter != null) {
                                fileWriter.close();
                            }
                            if (resultsUsedOut != null) {
                                resultsUsedOut.close();
                            }
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        fileOutputStream = outputStream;
                        resultsUsedOut = resultsUsedOut2;
                        fileWriter = resultsOut;
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        if (fileWriter != null) {
                            fileWriter.close();
                        }
                        if (resultsUsedOut != null) {
                            resultsUsedOut.close();
                        }
                        throw th;
                    }
                } catch (Exception e5) {
                    e42 = e5;
                    fileOutputStream = outputStream;
                    e42.printStackTrace();
                    mCaliResultWriteStatus = false;
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                    if (resultsUsedOut != null) {
                        resultsUsedOut.close();
                    }
                    performCalibrationMessage();
                    this.mMyHandler.post(/* anonymous class already generated */);
                } catch (Throwable th5) {
                    th = th5;
                    fileOutputStream = outputStream;
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                    if (resultsUsedOut != null) {
                        resultsUsedOut.close();
                    }
                    throw th;
                }
            }
            mCaliResultWriteStatus = true;
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e422) {
                    e422.printStackTrace();
                }
            }
            if (fileWriter != null) {
                fileWriter.close();
            }
            if (resultsUsedOut != null) {
                resultsUsedOut.close();
            }
        } catch (Exception e6) {
            e422 = e6;
            e422.printStackTrace();
            mCaliResultWriteStatus = false;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if (fileWriter != null) {
                fileWriter.close();
            }
            if (resultsUsedOut != null) {
                resultsUsedOut.close();
            }
            performCalibrationMessage();
            this.mMyHandler.post(/* anonymous class already generated */);
        }
        performCalibrationMessage();
        this.mMyHandler.post(/* anonymous class already generated */);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int readDualCamOtpData() {
        /*
        r27 = this;
        r19 = -1;
        r14 = new java.io.File;
        r24 = "/persist/dual_cam_stero_otp.bin";
        r0 = r24;
        r14.<init>(r0);
        r23 = new java.io.File;
        r24 = "/persist/dual_camera_calibration/calibrationResultsAndOTP.csv";
        r23.<init>(r24);
        r17 = 0;
        r5 = 0;
        r24 = r14.exists();
        if (r24 == 0) goto L_0x0205;
    L_0x001d:
        r6 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x020e }
        r6.<init>(r14);	 Catch:{ Exception -> 0x020e }
        r8 = r6.available();	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r3 = new byte[r8];	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r6.read(r3);	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r24 = "UTF-8";
        r0 = r24;
        r16 = org.apache.http.util.EncodingUtils.getString(r3, r0);	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r24 = "CameraCalibrationTest";
        r25 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r25.<init>();	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r26 = "otpdata = ";
        r25 = r25.append(r26);	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r0 = r25;
        r1 = r16;
        r25 = r0.append(r1);	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r25 = r25.toString();	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        android.util.Log.i(r24, r25);	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r24 = ",";
        r0 = r16;
        r1 = r24;
        r15 = r0.split(r1);	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r24 = 3;
        r0 = r24;
        r0 = new float[r0];	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r24 = r0;
        r25 = 0;
        r25 = r15[r25];	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r25 = java.lang.Float.parseFloat(r25);	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r26 = 0;
        r24[r26] = r25;	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r25 = 1;
        r25 = r15[r25];	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r25 = java.lang.Float.parseFloat(r25);	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r26 = 1;
        r24[r26] = r25;	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r25 = 2;
        r25 = r15[r25];	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r25 = java.lang.Float.parseFloat(r25);	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r26 = 2;
        r24[r26] = r25;	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r0 = r24;
        r1 = r27;
        r1.moduleValues = r0;	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r24 = calibrationStatus;	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        if (r24 != 0) goto L_0x019b;
    L_0x0093:
        r24 = r23.exists();	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        if (r24 != 0) goto L_0x00a6;
    L_0x0099:
        r24 = -1;
        if (r6 == 0) goto L_0x00a0;
    L_0x009d:
        r6.close();	 Catch:{ Exception -> 0x00a1 }
    L_0x00a0:
        return r24;
    L_0x00a1:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x00a0;
    L_0x00a6:
        r18 = new java.io.FileWriter;	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r24 = 1;
        r0 = r18;
        r1 = r23;
        r2 = r24;
        r0.<init>(r1, r2);	 Catch:{ Exception -> 0x0211, all -> 0x020b }
        r7 = 0;
    L_0x00b4:
        r24 = 3;
        r0 = r24;
        if (r7 >= r0) goto L_0x011e;
    L_0x00ba:
        r24 = 2;
        r0 = r24;
        if (r0 != r7) goto L_0x00e6;
    L_0x00c0:
        r24 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r24.<init>();	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r0 = r27;
        r0 = r0.moduleValues;	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r25 = r0;
        r25 = r25[r7];	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r24 = r24.append(r25);	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r25 = "\n";
        r24 = r24.append(r25);	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r24 = r24.toString();	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r0 = r18;
        r1 = r24;
        r0.append(r1);	 Catch:{ Exception -> 0x010a, all -> 0x01af }
    L_0x00e3:
        r7 = r7 + 1;
        goto L_0x00b4;
    L_0x00e6:
        r24 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r24.<init>();	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r0 = r27;
        r0 = r0.moduleValues;	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r25 = r0;
        r25 = r25[r7];	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r24 = r24.append(r25);	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r25 = ",";
        r24 = r24.append(r25);	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r24 = r24.toString();	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r0 = r18;
        r1 = r24;
        r0.append(r1);	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        goto L_0x00e3;
    L_0x010a:
        r4 = move-exception;
        r5 = r6;
        r17 = r18;
    L_0x010e:
        r4.printStackTrace();	 Catch:{ all -> 0x0209 }
        r19 = -1;
        if (r5 == 0) goto L_0x0118;
    L_0x0115:
        r5.close();	 Catch:{ Exception -> 0x01fa }
    L_0x0118:
        if (r17 == 0) goto L_0x011d;
    L_0x011a:
        r17.close();	 Catch:{ Exception -> 0x01fa }
    L_0x011d:
        return r19;
    L_0x011e:
        r10 = new java.io.File;	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r24 = "/persist/dual_cam_module_id.bin";
        r0 = r24;
        r10.<init>(r0);	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r20 = new java.io.File;	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r24 = "/persist/dual_camera_calibration/calibrationModuleID.bin";
        r0 = r20;
        r1 = r24;
        r0.<init>(r1);	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r21 = 0;
        r12 = 0;
        r24 = r10.exists();	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        if (r24 == 0) goto L_0x01e9;
    L_0x013d:
        r13 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x01be }
        r13.<init>(r10);	 Catch:{ Exception -> 0x01be }
        r11 = r13.available();	 Catch:{ Exception -> 0x021d, all -> 0x0215 }
        r9 = new byte[r11];	 Catch:{ Exception -> 0x021d, all -> 0x0215 }
        r13.read(r9);	 Catch:{ Exception -> 0x021d, all -> 0x0215 }
        r24 = "CameraCalibrationTest";
        r25 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x021d, all -> 0x0215 }
        r25.<init>();	 Catch:{ Exception -> 0x021d, all -> 0x0215 }
        r26 = "module ID ";
        r25 = r25.append(r26);	 Catch:{ Exception -> 0x021d, all -> 0x0215 }
        r0 = r25;
        r25 = r0.append(r9);	 Catch:{ Exception -> 0x021d, all -> 0x0215 }
        r25 = r25.toString();	 Catch:{ Exception -> 0x021d, all -> 0x0215 }
        android.util.Log.e(r24, r25);	 Catch:{ Exception -> 0x021d, all -> 0x0215 }
        r24 = r20.exists();	 Catch:{ Exception -> 0x021d, all -> 0x0215 }
        if (r24 != 0) goto L_0x017a;
    L_0x016d:
        r20.createNewFile();	 Catch:{ Exception -> 0x021d, all -> 0x0215 }
        r24 = "chmod 777";
        r0 = r20;
        r1 = r24;
        com.android.engineeringmode.manualtest.cameratest.CameraCaliUtil.setFileChmod(r0, r1);	 Catch:{ Exception -> 0x021d, all -> 0x0215 }
    L_0x017a:
        r22 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x021d, all -> 0x0215 }
        r24 = 1;
        r0 = r22;
        r1 = r20;
        r2 = r24;
        r0.<init>(r1, r2);	 Catch:{ Exception -> 0x021d, all -> 0x0215 }
        r0 = r22;
        r0.write(r9);	 Catch:{ Exception -> 0x0220, all -> 0x0218 }
        if (r13 == 0) goto L_0x0191;
    L_0x018e:
        r13.close();	 Catch:{ Exception -> 0x01aa, all -> 0x01af }
    L_0x0191:
        if (r22 == 0) goto L_0x0199;
    L_0x0193:
        r22.flush();	 Catch:{ Exception -> 0x01aa, all -> 0x01af }
        r22.close();	 Catch:{ Exception -> 0x01aa, all -> 0x01af }
    L_0x0199:
        r17 = r18;
    L_0x019b:
        r19 = 1;
        if (r6 == 0) goto L_0x01a2;
    L_0x019f:
        r6.close();	 Catch:{ Exception -> 0x01f5 }
    L_0x01a2:
        if (r17 == 0) goto L_0x01a7;
    L_0x01a4:
        r17.close();	 Catch:{ Exception -> 0x01f5 }
    L_0x01a7:
        r5 = r6;
        goto L_0x011d;
    L_0x01aa:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        goto L_0x0199;
    L_0x01af:
        r24 = move-exception;
        r5 = r6;
        r17 = r18;
    L_0x01b3:
        if (r5 == 0) goto L_0x01b8;
    L_0x01b5:
        r5.close();	 Catch:{ Exception -> 0x0200 }
    L_0x01b8:
        if (r17 == 0) goto L_0x01bd;
    L_0x01ba:
        r17.close();	 Catch:{ Exception -> 0x0200 }
    L_0x01bd:
        throw r24;
    L_0x01be:
        r4 = move-exception;
    L_0x01bf:
        r4.printStackTrace();	 Catch:{ all -> 0x01d5 }
        if (r12 == 0) goto L_0x01c7;
    L_0x01c4:
        r12.close();	 Catch:{ Exception -> 0x01d0, all -> 0x01af }
    L_0x01c7:
        if (r21 == 0) goto L_0x0199;
    L_0x01c9:
        r21.flush();	 Catch:{ Exception -> 0x01d0, all -> 0x01af }
        r21.close();	 Catch:{ Exception -> 0x01d0, all -> 0x01af }
        goto L_0x0199;
    L_0x01d0:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        goto L_0x0199;
    L_0x01d5:
        r24 = move-exception;
    L_0x01d6:
        if (r12 == 0) goto L_0x01db;
    L_0x01d8:
        r12.close();	 Catch:{ Exception -> 0x01e4, all -> 0x01af }
    L_0x01db:
        if (r21 == 0) goto L_0x01e3;
    L_0x01dd:
        r21.flush();	 Catch:{ Exception -> 0x01e4, all -> 0x01af }
        r21.close();	 Catch:{ Exception -> 0x01e4, all -> 0x01af }
    L_0x01e3:
        throw r24;	 Catch:{ Exception -> 0x010a, all -> 0x01af }
    L_0x01e4:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        goto L_0x01e3;
    L_0x01e9:
        r24 = "CameraCalibrationTest";
        r25 = "/persist/dual_cam_module_id.bin does't exist";
        android.util.Log.e(r24, r25);	 Catch:{ Exception -> 0x010a, all -> 0x01af }
        r17 = r18;
        goto L_0x019b;
    L_0x01f5:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x01a7;
    L_0x01fa:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x011d;
    L_0x0200:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x01bd;
    L_0x0205:
        r19 = -1;
        goto L_0x011d;
    L_0x0209:
        r24 = move-exception;
        goto L_0x01b3;
    L_0x020b:
        r24 = move-exception;
        r5 = r6;
        goto L_0x01b3;
    L_0x020e:
        r4 = move-exception;
        goto L_0x010e;
    L_0x0211:
        r4 = move-exception;
        r5 = r6;
        goto L_0x010e;
    L_0x0215:
        r24 = move-exception;
        r12 = r13;
        goto L_0x01d6;
    L_0x0218:
        r24 = move-exception;
        r12 = r13;
        r21 = r22;
        goto L_0x01d6;
    L_0x021d:
        r4 = move-exception;
        r12 = r13;
        goto L_0x01bf;
    L_0x0220:
        r4 = move-exception;
        r12 = r13;
        r21 = r22;
        goto L_0x01bf;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.engineeringmode.manualtest.cameratest.CameraCalibrationTest.readDualCamOtpData():int");
    }

    private void performCalibrationMessage() {
        this.yawValue = this.calibrationResults[1];
        this.pitchValue = this.calibrationResults[2];
        this.rollValue = this.calibrationResults[3];
        int otpStatus = readDualCamOtpData();
        if (this.moduleValues != null && this.moduleValues.length > 0) {
            this.yawModuleValue = this.moduleValues[0];
            this.pitchModuleValue = this.moduleValues[1];
            this.rollModuleValue = this.moduleValues[2];
        }
        if (((double) Math.abs(this.yawValue)) >= 1.9d || ((double) Math.abs(this.pitchValue)) >= 1.9d || ((double) Math.abs(this.rollValue)) >= 1.45d) {
            mInstallAbsStatus = 1;
        } else {
            mInstallAbsStatus = 0;
        }
    }

    private void Copy(String oldPath, String newPath) {
        int bytesum = 0;
        try {
            if (new File(oldPath).exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while (true) {
                    int byteread = inStream.read(buffer);
                    if (byteread != -1) {
                        bytesum += byteread;
                        Log.i("CameraCalibrationTest", "bytesum: " + bytesum);
                        fs.write(buffer, 0, byteread);
                    } else {
                        inStream.close();
                        return;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("CameraCalibrationTest", "error");
            e.printStackTrace();
        }
    }

    private void showCaliStatusDialog() {
        this.mMyHandler.removeMessages(1005);
        this.mTimerView.onStop();
        String caliStatus = "";
        String caliFailCause = "";
        String dynamicFailCause = "";
        TextView message_tv = new TextView(this);
        message_tv.setGravity(17);
        message_tv.setTextSize(60.0f);
        if (calibrationStatus == 0 && mInstallAbsStatus == 0 && mDynamicStatus == 0 && mCaliResultWriteStatus) {
            Copy("/persist/dual_camera_calibration/calibrationResultsAndOTP.csv", "/sdcard/calibrationResults.csv");
            Copy("/persist/dual_camera_calibration/calibrationOutput.bin", "/sdcard/calibrationOutput.bin");
            Copy("/persist/dual_cam_otp.bin", "/sdcard/dual_cam_otp.bin");
            caliStatus = "标定成功";
            this.mExFunction.setProductLineTestFlagExtraByte(78, (byte) 1);
            message_tv.setText("PASS");
            message_tv.setTextColor(-16711936);
        } else {
            caliStatus = "标定失败";
            this.mExFunction.setProductLineTestFlagExtraByte(78, (byte) 2);
            message_tv.setText("FAIL");
            message_tv.setTextColor(-65536);
        }
        switch (mDynamicStatus) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                dynamicFailCause = "动态变化超出范围";
                break;
            case Light.CHARGE_RED_LIGHT /*2*/:
                dynamicFailCause = "OTP文件有误或不存在";
                break;
            default:
                dynamicFailCause = "";
                break;
        }
        if (mInstallAbsStatus == 1) {
            caliFailCause = "组装公差超出范围";
        }
        if (calibrationStatus == 1) {
            caliFailCause = "标定图有误，请更换正确的场景重新标定 " + caliFailCause;
        }
        caliFailCause = caliFailCause + " " + dynamicFailCause;
        Builder builder = new Builder(this);
        builder.setView(message_tv);
        if (calculateTimeOutStatus == 1) {
            caliFailCause = "计算超时";
            this.mMyHandler.removeMessages(1005);
            if (this.mCalculateThread != null) {
                this.mCalculateThread.interrupt();
                this.mCalculateThread = null;
            }
            builder.setTitle(caliFailCause);
            builder.setMessage("请更换正确的场景重新标定");
            builder.setPositiveButton(2131296260, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    CameraCalibrationTest.this.finish();
                    ((ActivityManager) CameraCalibrationTest.this.getSystemService("activity")).forceStopPackage(CameraCalibrationTest.this.getPackageName());
                    Log.i("CameraCalibrationTest", "showCaliStatusDialog Press OK");
                }
            });
        } else {
            this.mMyHandler.removeMessages(1006);
            builder.setTitle(caliStatus);
            if (calibrationStatus == 1 || mDynamicStatus == 1 || mDynamicStatus == -1 || mInstallAbsStatus == 1) {
                builder.setMessage("失败原因: \n " + caliFailCause + "\n yaw = " + this.yawValue + ", pitch = " + this.pitchValue + ", roll = " + this.rollValue + "\n yawMoudle = " + this.yawModuleValue + ", pitchMoudle = " + this.pitchModuleValue + ", rollMoudle = " + this.rollModuleValue);
            } else if (mCaliResultWriteStatus) {
                builder.setMessage("标定结果已写入目录：/persist/dual_camera_calibration/");
            } else {
                builder.setMessage("标定结果写入失败：无写入权限");
            }
            builder.setPositiveButton(2131296260, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    CameraCalibrationTest.this.finish();
                    Log.i("CameraCalibrationTest", "showCaliStatusDialog Press OK");
                }
            });
        }
        Log.i("CameraCalibrationTest", "caliFailCause = " + caliFailCause + ", calculateTimeOutStatus" + calculateTimeOutStatus + ", initCaliStatus = " + initCaliStatus + ", calibrationStatus = " + calibrationStatus + ", deInitCaliStatus = " + deInitCaliStatus + ", mDynamicStatus = " + mDynamicStatus + ", mInstallAbsStatus = " + mInstallAbsStatus);
        if (!isFinishing()) {
            builder.create().show();
        }
    }

    private void showCameraOpenFailedDialog(String message) {
        Builder builder = new Builder(this, 3);
        builder.setTitle("相机打开失败");
        builder.setMessage(message);
        builder.setPositiveButton(2131297656, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                CameraCalibrationTest.this.finish();
                Log.i("CameraCalibrationTest", "showOpenCameraFailedDialog Press Exit");
            }
        });
        if (!isFinishing()) {
            builder.create().show();
        }
    }

    private boolean checkPermissionEnable() {
        for (String permission : mPermissions) {
            if (checkSelfPermission(permission) != 0) {
                return false;
            }
        }
        return true;
    }

    private void grantRuntimePermissions() {
        PackageManager pk = getPackageManager();
        ArrayList<String> permissions = new ArrayList();
        for (String permission : mPermissions) {
            if (checkSelfPermission(permission) != 0) {
                permissions.add(permission);
            }
        }
        if (permissions.isEmpty()) {
            switchCamera(this.mCameraId);
        } else {
            requestPermissions((String[]) permissions.toArray(new String[permissions.size()]), 2001);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 2001) {
            boolean granted = true;
            for (int i : grantResults) {
                if (i != 0) {
                    granted = false;
                }
            }
            if (granted) {
                switchCamera(this.mCameraId);
            } else {
                finish();
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (this.mCalculateThread != null) {
            this.mCalculateThread.interrupt();
            this.mCalculateThread = null;
        }
        finish();
    }
}
