package com.android.engineeringmode.autotest;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.RelativeLayout.LayoutParams;

import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements Callback {
    private String cameraFlashMode;
    public Camera mCamera;
    private int mDevice;
    SurfaceHolder mHolder;
    private OnOpenCameraFailedListener mOnOpenCameraFailedListener;
    List<Size> sizes;

    public interface OnOpenCameraFailedListener {
        void onFail();
    }

    public CameraPreview(Context context) {
        this(context, null);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.cameraFlashMode = "off";
        setLayoutParams(new LayoutParams(-1, -1));
        this.mHolder = getHolder();
        this.mHolder.addCallback(this);
        this.mHolder.setType(3);
    }

    public void setDevice(int device) {
        this.mDevice = device;
    }

    public void setOnOpenCameraFailedListener(OnOpenCameraFailedListener listener) {
        this.mOnOpenCameraFailedListener = listener;
    }

    private Camera openCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (this.mCamera != null) {
            try {
                this.mCamera.setPreviewDisplay(holder);
            } catch (Exception e) {
                e.printStackTrace();
                if (this.mOnOpenCameraFailedListener != null) {
                    this.mOnOpenCameraFailedListener.onFail();
                }
            }
        }
    }

    public void setFlashMode(String mode) {
        if (!mode.equals(this.cameraFlashMode)) {
            this.cameraFlashMode = mode;
        }
    }

    private void setParameters() {
        int i;
        Parameters parameters = this.mCamera.getParameters();
        this.sizes = parameters.getSupportedPreviewSizes();
        Log.d("CameraPreview", "" + this.sizes.size());
        for (i = 0; i < this.sizes.size(); i++) {
            Log.d("CameraPreview", "" + ((Size) this.sizes.get(i)).height + "------" + ((Size) this.sizes.get(i)).width);
        }
        Size previewSize = null;
        Log.d("CameraPreview", "" + this.sizes.size());
        i = 0;
        while (i < this.sizes.size()) {
            if (previewSize == null && ((Size) this.sizes.get(i)).width <= 1920 && ((Size) this.sizes.get(i)).width * 9 == ((Size) this.sizes.get(i)).height * 16) {
                previewSize = (Size) this.sizes.get(i);
            }
            Log.d("CameraPreview", "" + ((Size) this.sizes.get(i)).height + "------" + ((Size) this.sizes.get(i)).width);
            i++;
        }
        if (isSupported("continuous-picture", parameters.getSupportedFocusModes())) {
            parameters.setFocusMode("continuous-picture");
        } else if (isSupported("auto", parameters.getSupportedFocusModes())) {
            parameters.setFocusMode("auto");
        }
        if (previewSize != null) {
            parameters.setPreviewSize(previewSize.width, previewSize.height);
        }
        parameters.setFlashMode(this.cameraFlashMode);
        this.mCamera.setDisplayOrientation(90);
        Log.d("CameraPreview", "cameraFlashMode: " + this.cameraFlashMode);
        applyMaxPicSize(parameters);
        this.mCamera.setParameters(parameters);
    }

    private void applyMaxPicSize(Parameters mParameters) {
        List<Size> picSizes = mParameters.getSupportedPictureSizes();
        Size expectSize = (Size) picSizes.get(0);
        long expectValue = (long) (expectSize.width * expectSize.height);
        for (Size size : picSizes) {
            long tempValue = (long) (size.width * size.height);
            if (expectValue < tempValue) {
                expectValue = tempValue;
                expectSize = size;
            }
        }
        mParameters.setPictureSize(expectSize.width, expectSize.height);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (this.mCamera != null) {
            setParameters();
            this.mCamera.startPreview();
        }
    }

    private static boolean isSupported(String value, List<String> supported) {
        return supported != null && supported.indexOf(value) >= 0;
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (this.mCamera != null) {
            this.mCamera.stopPreview();
        }
    }

    public void open() {
        this.mCamera = openCamera(this.mDevice);
        if (this.mCamera == null && this.mOnOpenCameraFailedListener != null) {
            this.mOnOpenCameraFailedListener.onFail();
        }
        setVisibility(0);
    }

    public void close() {
        setVisibility(8);
        if (this.mCamera != null) {
            Log.i("CameraPreview", "close()");
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    public void reOpen() {
        Log.d("CameraPreview", "reOpen---camera");
        if (this.mCamera != null) {
            this.mCamera.stopPreview();
            this.mCamera.release();
        }
        this.mCamera = openCamera(this.mDevice);
        if (this.mCamera == null) {
            if (this.mOnOpenCameraFailedListener != null) {
                this.mOnOpenCameraFailedListener.onFail();
            }
            return;
        }
        try {
            this.mCamera.setPreviewDisplay(this.mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setParameters();
        this.mCamera.startPreview();
    }
}
