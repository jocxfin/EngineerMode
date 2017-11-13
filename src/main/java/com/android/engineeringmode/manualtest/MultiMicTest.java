package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.audioutils.AudioTestUtils;

import java.io.File;

public class MultiMicTest extends Activity implements OnErrorListener, OnClickListener {
    private static String prjname;
    private final int UPDATE_TEXT = 3;
    private boolean isStop = false;
    private boolean isThreadStart = false;
    private TextView mAmplitude;
    private Button mAncMic;
    private AudioManager mAudioManager = null;
    private AudioTestUtils mAudioTest = null;
    private int mButtonChosen = 0;
    private int mCurStep = 0;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Log.w("MultiMicTest", "handler message BEGIN_RCD");
                MultiMicTest.this.setCurInfo(2131296910);
                MultiMicTest.this.mHandler.sendEmptyMessageDelayed(6, 2000);
            } else if (1 == msg.what) {
                Log.w("MultiMicTest", "handler message END_RCD");
                if (MultiMicTest.this.stopRecord()) {
                    MultiMicTest.this.mHandler.sendEmptyMessageDelayed(2, 500);
                } else {
                    MultiMicTest.this.setCurInfo(2131296910);
                    MultiMicTest.this.mHandler.sendEmptyMessageDelayed(6, 2000);
                    Log.w("MultiMicTest", "stopRecord() Failed");
                }
            } else {
                if (6 == msg.what) {
                    Log.w("MultiMicTest", "handler message ENDACTIVITY");
                    MultiMicTest.this.endActivity();
                }
                if (msg.what == 3) {
                    int info = (MultiMicTest.this.mAudioTest.getMaxAmplitude() + 1) / 4;
                    Log.d("MultiMicTest", "info=" + info);
                    MultiMicTest.this.mAmplitude.setText("" + info);
                }
            }
        }
    };
    private Button mMainMic;
    private MediaRecorder mMediaRecord = null;
    private String mPathName = null;
    File mSampleFile = null;
    private Button mSecondMic;
    private Thread mThread;
    private boolean mbIsAutoTest = false;
    private String mstrCurrentShow = null;
    private TextView mtvInfoShow = null;
    private Runnable runnable = new Runnable() {
        public void run() {
            Log.i("MultiMicTest", "Thread start");
            while (!MultiMicTest.this.isStop) {
                Log.d("MultiMicTest", "Adjust ret: " + MultiMicTest.this.mAmplitude);
                MultiMicTest.this.mHandler.sendEmptyMessage(3);
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    Log.i("MultiMicTest", Log.getStackTraceString(e));
                }
            }
            MultiMicTest.this.isThreadStart = false;
        }
    };

    public Boolean isThreeMicProject() {
        prjname = SystemProperties.get("ro.boot.project_name");
        if (prjname.compareTo("16859") == 0) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(2130903164);
        this.mMainMic = (Button) findViewById(2131493442);
        this.mSecondMic = (Button) findViewById(2131493443);
        this.mAncMic = (Button) findViewById(2131493444);
        this.mAmplitude = (TextView) findViewById(2131493441);
        this.mMainMic.setOnClickListener(this);
        this.mSecondMic.setOnClickListener(this);
        this.mAncMic.setOnClickListener(this);
        if (!isThreeMicProject().booleanValue()) {
            this.mAncMic.setVisibility(8);
        }
        AudioSystem.setParameters("inTestMode=1");
        checkSdcard();
        this.mAudioManager = (AudioManager) getSystemService("audio");
        this.mAudioTest = new AudioTestUtils(this);
        this.mThread = new Thread(null, this.runnable);
        this.mButtonChosen = 0;
    }

    protected void onPause() {
        this.mAudioTest.setparameter("SingleMicTest=false");
        removeFile();
        try {
            if (this.mMediaRecord != null) {
                this.mMediaRecord.release();
                this.mMediaRecord = null;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.isStop = true;
        Log.i("MultiMicTest", "Thread stop");
        removeAllMsg();
        endActivity();
        super.onPause();
    }

    protected void onResume() {
        AudioSystem.setParameters("inTestMode=1");
        if (!(this.mButtonChosen == 0 || this.isThreadStart)) {
            this.isStop = false;
            this.mThread = new Thread(null, this.runnable);
            this.mThread.start();
            this.isThreadStart = true;
        }
        if (this.mButtonChosen == 1) {
            if (this.mMediaRecord != null) {
                stopRecord();
            }
            removeFile();
            this.mAudioTest.setparameter("SingleMicTest=main");
            Toast.makeText(this, "mMainMic", 0).show();
            this.mMainMic.setTextColor(-65536);
            this.mSecondMic.setTextColor(-16777216);
            this.mAncMic.setTextColor(-7829368);
            this.mMainMic.setEnabled(false);
            this.mSecondMic.setEnabled(true);
            this.mAncMic.setEnabled(false);
            ManualTest.requestFoucs(this.mMainMic);
        }
        if (this.mButtonChosen == 2) {
            stopRecord();
            removeFile();
            this.mAudioTest.setparameter("SingleMicTest=sec");
            Toast.makeText(this, "mSecondMic", 0).show();
            this.mMainMic.setTextColor(-7829368);
            this.mSecondMic.setTextColor(-65536);
            this.mAncMic.setTextColor(-16777216);
            if (isThreeMicProject().booleanValue()) {
                this.mMainMic.setEnabled(false);
                this.mSecondMic.setEnabled(false);
                this.mAncMic.setEnabled(true);
            } else {
                this.mMainMic.setEnabled(true);
                this.mSecondMic.setEnabled(false);
                this.mAncMic.setEnabled(false);
            }
            ManualTest.requestFoucs(this.mSecondMic);
        }
        if (this.mButtonChosen == 3) {
            stopRecord();
            removeFile();
            this.mAudioTest.setparameter("SingleMicTest=nsmic");
            Toast.makeText(this, "mAncMic", 0).show();
            this.mMainMic.setTextColor(-16777216);
            this.mSecondMic.setTextColor(-7829368);
            this.mAncMic.setTextColor(-65536);
            this.mMainMic.setEnabled(true);
            this.mSecondMic.setEnabled(false);
            this.mAncMic.setEnabled(false);
            ManualTest.requestFoucs(this.mAncMic);
        }
        super.onResume();
    }

    private void checkSdcard() {
        Log.w("MultiMicTest", "checkSdcard");
        setCurInfo(2131296916);
        this.mMainMic.setEnabled(true);
        this.mSecondMic.setEnabled(false);
        this.mAncMic.setEnabled(false);
    }

    public void onError(MediaRecorder mr, int what, int extra) {
        Log.w("MultiMicTest", "some error happen when recording");
        Log.w("MultiMicTest", "Record Error info, what = " + what + "  extra = " + extra);
        setCurInfo(2131296910);
        removeAllMsg();
        this.mHandler.sendEmptyMessageDelayed(6, 2000);
    }

    private void removeFile() {
        if (this.mSampleFile != null) {
            Log.i("MultiMicTest", "remove the file: " + this.mSampleFile.getAbsolutePath());
            this.mSampleFile.delete();
        }
        this.mSampleFile = null;
    }

    private void removeAllMsg() {
        this.mHandler.removeMessages(0);
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(5);
    }

    private void setCurInfo(int resId) {
        this.mstrCurrentShow = getString(resId);
        this.mAmplitude.setText(this.mstrCurrentShow);
    }

    public void onClick(View v) {
        if (v == this.mMainMic) {
            this.mButtonChosen = 1;
            if (this.mMediaRecord != null) {
                stopRecord();
            }
            removeFile();
            this.mAudioTest.setparameter("SingleMicTest=main");
            if (!this.isThreadStart) {
                this.mThread.start();
                this.isThreadStart = true;
            }
            Toast.makeText(this, "mMainMic", 0).show();
            this.mMainMic.setTextColor(-65536);
            this.mSecondMic.setTextColor(-16777216);
            this.mAncMic.setTextColor(-7829368);
            this.mMainMic.setEnabled(false);
            this.mSecondMic.setEnabled(true);
            this.mAncMic.setEnabled(false);
            ManualTest.requestFoucs(this.mMainMic);
        } else if (v == this.mSecondMic) {
            this.mButtonChosen = 2;
            stopRecord();
            removeFile();
            this.mAudioTest.setparameter("SingleMicTest=sec");
            Toast.makeText(this, "mSecondMic", 0).show();
            this.mMainMic.setTextColor(-7829368);
            this.mSecondMic.setTextColor(-65536);
            this.mAncMic.setTextColor(-16777216);
            if (isThreeMicProject().booleanValue()) {
                this.mMainMic.setEnabled(false);
                this.mSecondMic.setEnabled(false);
                this.mAncMic.setEnabled(true);
            } else {
                this.mMainMic.setEnabled(true);
                this.mSecondMic.setEnabled(false);
                this.mAncMic.setEnabled(false);
            }
            ManualTest.requestFoucs(this.mSecondMic);
        } else if (v == this.mAncMic) {
            this.mButtonChosen = 3;
            stopRecord();
            removeFile();
            this.mAudioTest.setparameter("SingleMicTest=nsmic");
            Toast.makeText(this, "mAncMic", 0).show();
            this.mMainMic.setTextColor(-16777216);
            this.mSecondMic.setTextColor(-7829368);
            this.mAncMic.setTextColor(-65536);
            this.mMainMic.setEnabled(true);
            this.mSecondMic.setEnabled(false);
            this.mAncMic.setEnabled(false);
            ManualTest.requestFoucs(this.mAncMic);
        }
    }

    private boolean stopRecord() {
        Log.w("MultiMicTest", "stopRecord");
        try {
            if (this.mMediaRecord != null) {
                this.mMediaRecord.stop();
                this.mMediaRecord.reset();
                this.mMediaRecord.release();
                this.mMediaRecord = null;
            }
            setCurInfo(2131296907);
            return true;
        } catch (IllegalStateException iie) {
            Log.w("MultiMicTest", "An IllegalStateException happen when stopRecerd");
            iie.printStackTrace();
            return false;
        }
    }

    private void endActivity() {
        removeFile();
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mAudioTest.setparameter("SingleMicTest=false");
        removeFile();
        try {
            if (this.mMediaRecord != null) {
                this.mMediaRecord.release();
                this.mMediaRecord = null;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.isStop = true;
        Log.i("MultiMicTest", "Thread stop");
        this.mHandler = null;
        this.mstrCurrentShow = null;
        this.mButtonChosen = 0;
    }
}
