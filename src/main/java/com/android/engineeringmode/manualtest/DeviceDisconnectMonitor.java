package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.engineeringmode.audioutils.AudioTestUtils;
import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.util.ExternFunction;
import com.oem.util.Feature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DeviceDisconnectMonitor extends Activity implements OnErrorListener {
    private static int CAMERA_MODULE_DEVICE_SUPPORT_MAX = 1;
    private TextView backCameraStatusTv;
    private CameraDetectThread cameraThread = null;
    private TextView countDownTv;
    private TextView flashlightStatusTv;
    private TextView frontCameraStatusTv;
    private final Object lock = new Object();
    private AudioManager mAudioManager = null;
    private AudioTestUtils mAudioTest;
    public Camera mCamera;
    private CountDownTimer mCountDownTimer = null;
    private ExternFunction mExFunction;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.i("DeviceDisconnectMonitor", "handleMessage : " + msg.what);
            switch (msg.what) {
                case 0:
                    DeviceDisconnectMonitor.this.nv_registered = true;
                    break;
                case Light.MAIN_KEY_LIGHT /*1*/:
                    int[] results = msg.obj;
                    if (results.length == 5) {
                        for (int i = 0; i < results.length; i++) {
                            TextView textview = (TextView) DeviceDisconnectMonitor.this.viewList.get(i);
                            if (results[i] == 0) {
                                textview.setTextColor(-65536);
                                textview.setText(2131297584);
                                DeviceDisconnectMonitor.this.mHandler.sendEmptyMessage(4);
                            } else if (results[i] == 1) {
                                textview.setText(2131297586);
                            } else if (results[i] == -1) {
                                textview.setText(2131297585);
                            }
                        }
                        break;
                    }
                    Log.e("DeviceDisconnectMonitor", "recevied message invalid");
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    int main_mic_status = msg.arg1;
                    Log.i("DeviceDisconnectMonitor", "main_mic_status : " + main_mic_status);
                    if (main_mic_status <= 100) {
                        DeviceDisconnectMonitor.this.micStatusTv.setTextColor(-65536);
                        DeviceDisconnectMonitor.this.micStatusTv.setText(2131297584);
                        DeviceDisconnectMonitor.this.mHandler.sendEmptyMessage(4);
                        break;
                    }
                    DeviceDisconnectMonitor.this.micStatusTv.setText("" + main_mic_status);
                    break;
                case Light.CHARGE_GREEN_LIGHT /*3*/:
                    DeviceDisconnectMonitor.this.countDownTv.setTextColor(-16711936);
                    DeviceDisconnectMonitor.this.countDownTv.setText(2131296272);
                    DeviceDisconnectMonitor.this.test_done = true;
                    if (DeviceDisconnectMonitor.this.nv_registered) {
                        DeviceDisconnectMonitor.this.mExFunction.setProductLineTestFlagExtraByte(68, (byte) 1);
                        break;
                    }
                    break;
                case 4:
                    DeviceDisconnectMonitor.this.mHandler.removeMessages(1);
                    DeviceDisconnectMonitor.this.mHandler.removeMessages(2);
                    DeviceDisconnectMonitor.this.mHandler.removeMessages(3);
                    if (DeviceDisconnectMonitor.this.micThread != null) {
                        DeviceDisconnectMonitor.this.micThread.stopDetect();
                        DeviceDisconnectMonitor.this.micThread = null;
                    }
                    if (DeviceDisconnectMonitor.this.cameraThread != null) {
                        DeviceDisconnectMonitor.this.cameraThread.stopDetect();
                        DeviceDisconnectMonitor.this.cameraThread = null;
                    }
                    if (DeviceDisconnectMonitor.this.mCountDownTimer != null) {
                        DeviceDisconnectMonitor.this.mCountDownTimer.cancel();
                        DeviceDisconnectMonitor.this.mCountDownTimer = null;
                    }
                    DeviceDisconnectMonitor.this.countDownTv.setTextColor(-65536);
                    DeviceDisconnectMonitor.this.countDownTv.setText(2131296273);
                    DeviceDisconnectMonitor.this.test_done = true;
                    if (DeviceDisconnectMonitor.this.nv_registered) {
                        DeviceDisconnectMonitor.this.mExFunction.setProductLineTestFlagExtraByte(68, (byte) 2);
                        break;
                    }
                    break;
            }
        }
    };
    private MediaRecorder mMediaRecord = null;
    private String mPathName = null;
    private File mSampleFile = null;
    private TextView micStatusTv;
    private MainMicDetectThread micThread = null;
    private boolean nv_registered = false;
    private TextView secondbackCameraStatusTv;
    private boolean test_done = false;
    private TextView vibratorStatusTv;
    private ArrayList<TextView> viewList = null;

    private class CameraDetectThread extends Thread {
        private boolean looping;

        private CameraDetectThread() {
            this.looping = false;
        }

        public void stopDetect() {
            this.looping = true;
            interrupt();
        }

        public void run() {
            this.looping = true;
            while (this.looping && !isInterrupted()) {
                try {
                    int[] results = new int[5];
                    for (int i = 0; i < 5; i++) {
                        results[i] = DeviceDisconnectMonitor.this.detectCameraStatus(i);
                        Log.i("DeviceDisconnectMonitor", "CameraDetectThread : current device = " + DeviceDisconnectMonitor.this.translateDeviceId(i) + ", status = " + results[i]);
                        Thread.sleep(200);
                    }
                    DeviceDisconnectMonitor.this.mHandler.sendMessage(Message.obtain(DeviceDisconnectMonitor.this.mHandler, 1, results));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.i("DeviceDisconnectMonitor", "InterruptedException caught");
                    this.looping = false;
                }
            }
        }
    }

    private class MainMicDetectThread extends Thread {
        private boolean startRecorded;

        private MainMicDetectThread() {
            this.startRecorded = false;
        }

        public void stopDetect() {
            this.startRecorded = false;
            interrupt();
        }

        public void run() {
            if (DeviceDisconnectMonitor.this.mMediaRecord != null) {
                DeviceDisconnectMonitor.this.stopRecord();
            }
            DeviceDisconnectMonitor.this.removeFile();
            this.startRecorded = DeviceDisconnectMonitor.this.beginToRecord();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Log.i("DeviceDisconnectMonitor", "InterruptedException caught");
                this.startRecorded = false;
            }
            while (this.startRecorded && !isInterrupted()) {
                try {
                    Thread.sleep(1000);
                    int[] levels = new int[3];
                    for (int i = 0; i < 3; i++) {
                        Thread.sleep(200);
                        if (DeviceDisconnectMonitor.this.mMediaRecord != null) {
                            levels[i] = (DeviceDisconnectMonitor.this.mMediaRecord.getMaxAmplitude() + 1) / 4;
                            Log.i("DeviceDisconnectMonitor", "main mic record level : " + levels[i]);
                        }
                    }
                    DeviceDisconnectMonitor.this.mHandler.sendMessage(DeviceDisconnectMonitor.this.mHandler.obtainMessage(2, ((levels[0] + levels[1]) + levels[2]) / 3, 0));
                } catch (InterruptedException e2) {
                    Log.i("DeviceDisconnectMonitor", "InterruptedException caught");
                    this.startRecorded = false;
                }
            }
            DeviceDisconnectMonitor.this.stopRecord();
            DeviceDisconnectMonitor.this.removeFile();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        int i;
        super.onCreate(savedInstanceState);
        if (Feature.isDualBackCameraSupported(this)) {
            i = 2;
        } else {
            i = 1;
        }
        CAMERA_MODULE_DEVICE_SUPPORT_MAX = i;
        setContentView(2130903081);
        getWindow().addFlags(128);
        this.countDownTv = (TextView) findViewById(2131493026);
        this.micStatusTv = (TextView) findViewById(2131493025);
        this.viewList = new ArrayList();
        this.backCameraStatusTv = (TextView) findViewById(2131493022);
        this.viewList.add(0, this.backCameraStatusTv);
        this.frontCameraStatusTv = (TextView) findViewById(2131493020);
        this.viewList.add(1, this.frontCameraStatusTv);
        this.secondbackCameraStatusTv = (TextView) findViewById(2131493021);
        this.viewList.add(2, this.secondbackCameraStatusTv);
        this.vibratorStatusTv = (TextView) findViewById(2131493023);
        this.viewList.add(3, this.vibratorStatusTv);
        this.flashlightStatusTv = (TextView) findViewById(2131493024);
        this.viewList.add(4, this.flashlightStatusTv);
        for (int i2 = CAMERA_MODULE_DEVICE_SUPPORT_MAX + 1; i2 < 5; i2++) {
            ((ViewGroup) ((TextView) this.viewList.get(i2)).getParent()).setVisibility(8);
        }
        if (Feature.isDualBackCameraSupported(this)) {
            ((ViewGroup) this.secondbackCameraStatusTv.getParent()).setVisibility(0);
        } else {
            ((ViewGroup) this.secondbackCameraStatusTv.getParent()).setVisibility(8);
        }
        this.mAudioManager = (AudioManager) getSystemService("audio");
        this.mAudioTest = new AudioTestUtils(this);
        this.mExFunction = new ExternFunction(this);
        this.nv_registered = false;
        this.mExFunction.registerOnServiceConnected(this.mHandler, 0, null);
    }

    private int detectCameraStatus(int camera) {
        int state = -1;
        if (camera > CAMERA_MODULE_DEVICE_SUPPORT_MAX) {
            return -1;
        }
        if (camera == 1) {
            if (this.mCamera == null) {
                try {
                    this.mCamera = Camera.open(1);
                    state = 0;
                } catch (RuntimeException e) {
                    state = -1;
                }
                if (this.mCamera != null) {
                    this.mCamera.release();
                    this.mCamera = null;
                }
            }
        } else if (camera == 2) {
            if (this.mCamera == null) {
                try {
                    this.mCamera = Camera.open(2);
                    state = 0;
                } catch (RuntimeException e2) {
                    state = -1;
                }
                if (this.mCamera != null) {
                    this.mCamera.release();
                    this.mCamera = null;
                }
            }
        } else if (camera != 0) {
            state = -1;
            Log.d("DeviceDisconnectMonitor", "not supported");
        } else if (this.mCamera == null) {
            try {
                this.mCamera = Camera.open(0);
                state = 0;
            } catch (RuntimeException e3) {
                state = -1;
            }
            if (this.mCamera != null) {
                this.mCamera.release();
                this.mCamera = null;
            }
        }
        if (state < 0) {
            return 0;
        }
        return 1;
    }

    protected void onResume() {
        super.onResume();
        this.mCountDownTimer = new CountDownTimer(32000, 1000) {
            public void onFinish() {
                String second = DeviceDisconnectMonitor.this.getString(2131297577);
                if (DeviceDisconnectMonitor.this.countDownTv != null) {
                    DeviceDisconnectMonitor.this.countDownTv.setText("0" + second);
                }
                if (DeviceDisconnectMonitor.this.micThread != null) {
                    DeviceDisconnectMonitor.this.micThread.stopDetect();
                    DeviceDisconnectMonitor.this.micThread = null;
                }
                if (DeviceDisconnectMonitor.this.cameraThread != null) {
                    DeviceDisconnectMonitor.this.cameraThread.stopDetect();
                    DeviceDisconnectMonitor.this.cameraThread = null;
                }
                DeviceDisconnectMonitor.this.mHandler.sendEmptyMessageDelayed(3, 1000);
            }

            public void onTick(long millisUntilFinished) {
                String second = DeviceDisconnectMonitor.this.getString(2131297577);
                if (DeviceDisconnectMonitor.this.countDownTv != null) {
                    DeviceDisconnectMonitor.this.countDownTv.setText((millisUntilFinished / 1000) + second);
                }
            }
        };
        if (!this.test_done) {
            this.mCountDownTimer.start();
            this.micThread = new MainMicDetectThread();
            this.micThread.start();
            this.cameraThread = new CameraDetectThread();
            this.cameraThread.start();
        }
    }

    protected void onPause() {
        super.onPause();
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(3);
        if (this.micThread != null) {
            this.micThread.stopDetect();
            this.micThread = null;
        }
        if (this.cameraThread != null) {
            this.cameraThread.stopDetect();
            this.cameraThread = null;
        }
        if (this.mCountDownTimer != null) {
            this.mCountDownTimer.cancel();
        }
    }

    private String translateDeviceId(int id) {
        switch (id) {
            case 0:
                return "BACK_CAMERA";
            case Light.MAIN_KEY_LIGHT /*1*/:
                return "FRONT_CAMERA";
            case Light.CHARGE_RED_LIGHT /*2*/:
                return "SECOND_BACK_CAMERA";
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                return "VIBRATOR";
            case 4:
                return "FLASH_LIGHT";
            default:
                return null;
        }
    }

    private boolean beginToRecord() {
        if (createTempFile(".amr")) {
            this.mMediaRecord = new MediaRecorder();
            this.mMediaRecord.setOnErrorListener(this);
            this.mMediaRecord.setAudioSource(1);
            this.mMediaRecord.setOutputFormat(3);
            this.mMediaRecord.setAudioEncoder(1);
            this.mMediaRecord.setOutputFile(this.mSampleFile.getAbsolutePath());
            try {
                Log.w("DeviceDisconnectMonitor", "prepare to record");
                this.mMediaRecord.prepare();
                this.mMediaRecord.start();
                return true;
            } catch (IOException ioe) {
                Log.w("DeviceDisconnectMonitor", "prepare to record failed, an ioe happen");
                ioe.printStackTrace();
                stopRecord();
                return false;
            } catch (IllegalStateException ioe2) {
                Log.w("DeviceDisconnectMonitor", "prepare to record failed, an IllegalStateException happen");
                ioe2.printStackTrace();
                stopRecord();
                return false;
            }
        }
        Log.w("DeviceDisconnectMonitor", "createTempFileFailed");
        return false;
    }

    private boolean createTempFile(String extension) {
        Log.i("DeviceDisconnectMonitor", "create temp file, extension = " + extension);
        if (this.mSampleFile == null && this.mSampleFile == null) {
            this.mPathName = Environment.getDataDirectory().getAbsolutePath();
            File sampleDir = new File(this.mPathName + "/backup");
            Log.i("DeviceDisconnectMonitor", "mPathName = " + this.mPathName);
            if (!sampleDir.canWrite()) {
                sampleDir = new File(Environment.getDataDirectory().getAbsolutePath() + "/sdcard");
            }
            try {
                this.mSampleFile = File.createTempFile("recording", extension, sampleDir);
                Log.i("DeviceDisconnectMonitor", "File Path = " + this.mSampleFile.getAbsolutePath());
                Log.i("DeviceDisconnectMonitor", "File Name = " + this.mSampleFile.getName());
            } catch (IOException e) {
                Log.e("DeviceDisconnectMonitor", "A error happen when createTempFile");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private boolean stopRecord() {
        Log.w("DeviceDisconnectMonitor", "stopRecord");
        try {
            if (this.mMediaRecord != null) {
                this.mMediaRecord.stop();
                this.mMediaRecord.reset();
                this.mMediaRecord.release();
                this.mMediaRecord = null;
            }
            return true;
        } catch (IllegalStateException iie) {
            Log.w("DeviceDisconnectMonitor", "An IllegalStateException happen when stopRecerd");
            iie.printStackTrace();
            return false;
        }
    }

    public void onError(MediaRecorder mr, int what, int extra) {
        Log.w("DeviceDisconnectMonitor", "some error happen when recording");
        Log.w("DeviceDisconnectMonitor", "Record Error info, what = " + what + "  extra = " + extra);
        if (this.micThread != null) {
            this.micThread.stopDetect();
        }
        this.micStatusTv.setTextColor(-65536);
        this.micStatusTv.setText(2131297584);
        this.mHandler.sendEmptyMessage(4);
    }

    protected void onDestroy() {
        this.mExFunction.unregisterOnServiceConnected(this.mHandler);
        this.mExFunction.dispose();
        super.onDestroy();
    }

    private void removeFile() {
        if (this.mSampleFile != null) {
            Log.i("DeviceDisconnectMonitor", "remove the file: " + this.mSampleFile.getAbsolutePath());
            this.mSampleFile.delete();
        }
        this.mSampleFile = null;
    }
}
