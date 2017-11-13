package com.android.engineeringmode.manualtest.cameratest;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.media.MediaActionSound;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;

public class CameraTestPreview extends SurfaceView implements Callback {
    private static float downX = 0.0f;
    private static float downY = 0.0f;
    private static boolean needFocusflag = false;
    private static boolean switch2Picflag = false;
    private static boolean switch2Videoflag = false;
    private MediaActionSound mAutofocusActionSound = null;
    private Camera mCamera;
    private Canvas mCanvas;
    private Context mContext;
    private SurfaceHolder mHolder;
    private OnTouchCallback mOnTouchCallback;
    private SufaceCreatedCallback mSufaceCreatedCallback;
    AutoFocusCallback myFocusCallback = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            Log.d("CameraTestPreview", "onAutoFocus success " + success);
            if (CameraTestPreview.this.mAutofocusActionSound != null) {
                CameraTestPreview.this.mAutofocusActionSound.play(1);
            }
            if (success) {
                Toast.makeText(CameraTestPreview.this.mContext, "聚焦完成", 1);
            } else {
                Toast.makeText(CameraTestPreview.this.mContext, "聚焦失败", 1);
            }
        }
    };

    public interface SufaceCreatedCallback {
        void onSurfaceChanged(SurfaceHolder surfaceHolder);

        void onSurfaceCreated(SurfaceHolder surfaceHolder);
    }

    public interface OnTouchCallback {
        void onDrawFocusRecAera(float f, float f2);

        void onFocusAeraDismiss();

        void switch2PictureMode();

        void switch2VideoMode();
    }

    public CameraTestPreview(Context context) {
        super(context);
        this.mContext = context;
        this.mHolder = getHolder();
        this.mHolder.addCallback(this);
        this.mAutofocusActionSound = new MediaActionSound();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            needFocusflag = true;
            float x = event.getX();
            float y = event.getY();
            downX = x;
            downY = y;
        }
        if (2 == event.getAction()) {
            float moveX = event.getX();
            float moveY = event.getY();
            if (Math.abs(moveX - downX) >= 10.0f || Math.abs(moveX - downX) >= 10.0f) {
                needFocusflag = false;
            } else {
                needFocusflag = true;
            }
            if (moveX - downX > 100.0f) {
                switch2Picflag = true;
            } else if (moveX - downX < -100.0f) {
                switch2Videoflag = true;
            }
        }
        if (1 == event.getAction()) {
            if (this.mOnTouchCallback == null) {
                return false;
            }
            if (needFocusflag) {
                this.mOnTouchCallback.onDrawFocusRecAera(downX, downY);
                this.mOnTouchCallback.onFocusAeraDismiss();
                needFocusflag = false;
            }
            if (switch2Picflag) {
                this.mOnTouchCallback.switch2PictureMode();
                switch2Picflag = false;
            }
            if (switch2Videoflag) {
                this.mOnTouchCallback.switch2VideoMode();
                switch2Videoflag = false;
            }
        }
        return true;
    }

    protected void onDraw(Canvas canvas) {
        this.mCanvas = canvas;
    }

    public void setCamera(Camera camera) {
        this.mCamera = camera;
    }

    public void setSurfaceCreatedCallbac(SufaceCreatedCallback callback) {
        this.mSufaceCreatedCallback = callback;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("CameraTestPreview", "surfaceCreated");
        if (this.mCamera != null) {
            if (this.mAutofocusActionSound != null) {
                this.mAutofocusActionSound.load(1);
            }
            if (this.mSufaceCreatedCallback != null) {
                this.mSufaceCreatedCallback.onSurfaceCreated(holder);
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("CameraTestPreview", "surfaceDestroyed");
        if (this.mAutofocusActionSound != null) {
            this.mAutofocusActionSound.release();
            this.mAutofocusActionSound = null;
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (!(this.mHolder.getSurface() == null || this.mCamera == null || this.mSufaceCreatedCallback == null)) {
            this.mSufaceCreatedCallback.onSurfaceChanged(this.mHolder);
        }
    }
}
