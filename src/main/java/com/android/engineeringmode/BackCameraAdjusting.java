package com.android.engineeringmode;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.engineeringmode.autotest.CameraPreview;
import com.android.engineeringmode.autotest.CameraPreview.OnOpenCameraFailedListener;
import com.android.engineeringmode.functions.Light;

import java.io.File;
import java.io.RandomAccessFile;

public class BackCameraAdjusting extends Activity implements OnClickListener {
    private ToggleButton mBlue;
    private ToggleButton mGreen;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    BackCameraAdjusting.this.mPreview.reOpen();
                    BackCameraAdjusting.this.setBtnEnabled(true);
                    return;
                default:
                    return;
            }
        }
    };
    private RandomAccessFile mParamRandomAccessFile;
    private CameraPreview mPreview;
    private ToggleButton mRed;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        getWindow().setFlags(128, 128);
        setContentView(2130903060);
        this.mPreview = (CameraPreview) findViewById(2131492934);
        this.mPreview.setDevice(0);
        this.mPreview.setOnOpenCameraFailedListener(new OnOpenCameraFailedListener() {
            public void onFail() {
                Toast.makeText(BackCameraAdjusting.this, "Can't open the Camera", 1).show();
            }
        });
        this.mRed = (ToggleButton) findViewById(2131492935);
        this.mGreen = (ToggleButton) findViewById(2131492936);
        this.mBlue = (ToggleButton) findViewById(2131492937);
        this.mRed.setOnClickListener(this);
        this.mGreen.setOnClickListener(this);
        this.mBlue.setOnClickListener(this);
        openFile();
    }

    protected void onResume() {
        super.onResume();
        switch (readParam()) {
            case 0:
                this.mRed.setChecked(false);
                this.mGreen.setChecked(false);
                this.mBlue.setChecked(false);
                break;
            case Light.MAIN_KEY_LIGHT /*1*/:
                this.mRed.setChecked(true);
                this.mGreen.setChecked(false);
                this.mBlue.setChecked(false);
                break;
            case Light.CHARGE_RED_LIGHT /*2*/:
                this.mRed.setChecked(false);
                this.mGreen.setChecked(true);
                this.mBlue.setChecked(false);
                break;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                this.mRed.setChecked(false);
                this.mGreen.setChecked(false);
                this.mBlue.setChecked(true);
                break;
        }
        this.mPreview.open();
    }

    protected void onPause() {
        super.onPause();
        this.mPreview.close();
        save(SystemProperties.getInt("persist.sys.camera.rgbParams", 0));
    }

    protected void onDestroy() {
        super.onDestroy();
        try {
            this.mParamRandomAccessFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Close /persist/camera/adjusting_param failed, please try agin.", 0).show();
        }
    }

    private void setBtnEnabled(boolean enabled) {
        this.mRed.setEnabled(enabled);
        this.mGreen.setEnabled(enabled);
        this.mBlue.setEnabled(enabled);
    }

    private void openFile() {
        try {
            File dir = new File("/persist/camera");
            File paramFile = new File("/persist/camera/adjusting_param");
            if (!dir.exists()) {
                dir.mkdir();
                paramFile.createNewFile();
                paramFile.setReadable(true);
                paramFile.setWritable(true);
            }
            if (!paramFile.exists()) {
                paramFile.createNewFile();
            }
            this.mParamRandomAccessFile = new RandomAccessFile(paramFile, "rws");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Open /persist/camera/adjusting_param failed, please try agin.", 0).show();
        }
    }

    private int readParam() {
        int param = 0;
        try {
            if (this.mParamRandomAccessFile.length() > 0) {
                this.mParamRandomAccessFile.seek(0);
                param = this.mParamRandomAccessFile.readInt();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Read param failed, please try agin.", 0).show();
        }
        SystemProperties.set("persist.sys.camera.rgbParams", String.valueOf(param));
        return param;
    }

    private void save(int param) {
        try {
            this.mParamRandomAccessFile.seek(0);
            this.mParamRandomAccessFile.writeInt(param);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Save param failed, please try agin.", 0).show();
        }
    }

    public void onClick(View v) {
        if (!((ToggleButton) v).isChecked()) {
            SystemProperties.set("persist.sys.camera.rgbParams", String.valueOf(0));
        } else if (this.mRed == v) {
            SystemProperties.set("persist.sys.camera.rgbParams", String.valueOf(1));
            this.mGreen.setChecked(false);
            this.mBlue.setChecked(false);
        } else if (this.mGreen == v) {
            SystemProperties.set("persist.sys.camera.rgbParams", String.valueOf(2));
            this.mRed.setChecked(false);
            this.mBlue.setChecked(false);
        } else if (this.mBlue == v) {
            SystemProperties.set("persist.sys.camera.rgbParams", String.valueOf(3));
            this.mRed.setChecked(false);
            this.mGreen.setChecked(false);
        }
        this.mHandler.sendEmptyMessage(1);
    }
}
