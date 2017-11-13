package com.android.engineeringmode.gps;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;

import com.android.engineeringmode.gps.GpsEnvService.MyBinder;

import java.io.IOException;

public class GpsEnvCameraActivity extends Activity {
    private Handler mHandler;
    private Preview mPreview;
    private GpsEnvService mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("OpenCameraActivity", "service connected");
            GpsEnvCameraActivity.this.mService = ((MyBinder) service).getService();
            boolean backCaeraTest = GpsEnvTestActivity.shouldTestBackCamera();
            boolean frontCameraTest = GpsEnvTestActivity.shouldTestFrontCamera();
            Log.d("ww", " onServiceConnected" + backCaeraTest + frontCameraTest);
            if (backCaeraTest) {
                GpsEnvCameraActivity.this.mPreview.reCreate(0);
                GpsEnvCameraActivity.this.mService.requestLocationUpdate(6);
                if (frontCameraTest) {
                    GpsEnvCameraActivity.this.mHandler.postDelayed(GpsEnvCameraActivity.this.runnableFront, 30090);
                    GpsEnvCameraActivity.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            Log.d("ww", " onServiceConnected GpsEnvVideoTestActivity...");
                            GpsEnvCameraActivity.this.startActivity(new Intent(GpsEnvCameraActivity.this, GpsEnvVideoTestActivity.class));
                            GpsEnvCameraActivity.this.finish();
                        }
                    }, 62000);
                    return;
                }
                GpsEnvCameraActivity.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        GpsEnvCameraActivity.this.startActivity(new Intent(GpsEnvCameraActivity.this, GpsEnvVideoTestActivity.class));
                        GpsEnvCameraActivity.this.finish();
                    }
                }, 30000);
            } else if (frontCameraTest) {
                GpsEnvCameraActivity.this.mPreview.reCreate(1);
                GpsEnvCameraActivity.this.mService.requestLocationUpdate(7);
                GpsEnvCameraActivity.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        GpsEnvCameraActivity.this.startActivity(new Intent(GpsEnvCameraActivity.this, GpsEnvVideoTestActivity.class));
                        GpsEnvCameraActivity.this.finish();
                    }
                }, 30000);
            } else {
                GpsEnvCameraActivity.this.startActivity(new Intent(GpsEnvCameraActivity.this, GpsEnvVideoTestActivity.class));
                GpsEnvCameraActivity.this.finish();
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d("OpenCameraActivity", "service disconnected");
        }
    };
    private boolean mStartPreviewOk = false;
    private WakeLock mWakeLock;
    private Runnable runnableFront = new Runnable() {
        public void run() {
            GpsEnvCameraActivity.this.releaseResource();
            GpsEnvCameraActivity.this.mPreview = new Preview(GpsEnvCameraActivity.this);
            GpsEnvCameraActivity.this.setContentView(GpsEnvCameraActivity.this.mPreview);
            GpsEnvCameraActivity.this.mPreview.reCreate(1);
            GpsEnvCameraActivity.this.mService.requestLocationUpdate(7);
        }
    };

    private class Preview extends SurfaceView implements Callback {
        public Camera mCamera;
        private int mCameraID = 0;
        SurfaceHolder mHolder = getHolder();
        private Boolean mIsPreviewing;

        Preview(Context context) {
            super(context);
            this.mHolder.addCallback(this);
            this.mHolder.setType(3);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            Log.i("Preview--zpk", "surfaceCreated");
            try {
                if (this.mCamera == null) {
                    this.mCamera = Camera.open(this.mCameraID);
                }
                this.mIsPreviewing = Boolean.valueOf(false);
                this.mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                this.mCamera.release();
                this.mCamera = null;
                Toast.makeText(getContext(), "Can't set preview display", 1).show();
            } catch (RuntimeException e2) {
                Toast.makeText(getContext(), "Can't open the Camera", 1).show();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.i("Preview--zpk", "surfaceDestroyed");
            this.mIsPreviewing = Boolean.valueOf(false);
            if (this.mCamera != null) {
                this.mCamera.stopPreview();
                this.mCamera.release();
                this.mCamera = null;
                return;
            }
            Log.w("Preview--zpk", "surfaceDestroyed(), here mCamera = null");
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            Log.i("Preview--zpk", "surfaceChanged");
            Log.i("Preview--zpk", "w = " + w + "  h = " + h);
            if (this.mCamera != null) {
                Parameters parameters = this.mCamera.getParameters();
                parameters.setPreviewSize(640, 480);
                parameters.setAntibanding("50hz");
                this.mCamera.setParameters(parameters);
                Thread thread = new Thread() {
                    public void run() {
                        Preview.this.mCamera.startPreview();
                        Preview.this.mIsPreviewing = Boolean.valueOf(true);
                    }
                };
                if (!this.mIsPreviewing.booleanValue()) {
                    thread.start();
                }
            }
        }

        public void reCreate(int mCameraID) {
            Log.i("Preview--zpk", "reCreate()");
            new Exception().printStackTrace();
            this.mCameraID = mCameraID;
            if (this.mCamera == null) {
                try {
                    this.mCamera = Camera.open(mCameraID);
                    Log.i("Preview--zpk", "reCreate mCameraID :" + mCameraID);
                } catch (RuntimeException e) {
                    Toast.makeText(getContext(), "Can't open the Camera", 1).show();
                    Log.e("Preview--zpk", "reCreate()----------:" + e.toString());
                }
            }
            if (this.mCamera != null) {
                Log.i("Preview--zpk", "reCreate mCamera != null");
                Parameters parameters = this.mCamera.getParameters();
                parameters.setPreviewSize(640, 480);
                parameters.setAntibanding("50hz");
                this.mCamera.setParameters(parameters);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        Log.d("OpenCameraActivity", "oncreate()");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        getWindow().setFlags(128, 128);
        Intent intent = new Intent();
        intent.setClass(this, GpsEnvService.class);
        Log.d("OpenCameraActivity", "start and bind service");
        this.mPreview = new Preview(this);
        this.mStartPreviewOk = true;
        bindService(intent, this.mServiceConnection, 1);
        this.mHandler = new Handler();
    }

    protected void onDestroy() {
        Log.d("OpenCameraActivity", "onDestory()");
        super.onDestroy();
        unbindService(this.mServiceConnection);
        if (GpsEnvTestActivity.shouldTestBackCamera() && GpsEnvTestActivity.shouldTestFrontCamera()) {
            this.mHandler.removeCallbacks(this.runnableFront);
        }
    }

    protected void onPause() {
        super.onPause();
        this.mWakeLock.release();
        releaseResource();
    }

    private void releaseResource() {
        if (this.mPreview != null && this.mPreview.mCamera != null) {
            this.mPreview.mCamera.stopPreview();
            this.mPreview.mCamera.release();
            this.mPreview.mCamera = null;
            Log.e("OpenCameraActivity", "onPause()--------------release camera ok");
        }
    }

    protected void onResume() {
        super.onResume();
        setContentView(this.mPreview);
        this.mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(805306394, "CameraPreview");
        this.mWakeLock.acquire();
    }
}
