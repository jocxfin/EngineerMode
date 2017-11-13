package com.android.engineeringmode.manualtest;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.KeepScreenOnActivity;

import java.io.File;
import java.io.IOException;

public class MicTest extends KeepScreenOnActivity implements OnErrorListener, OnCompletionListener, MediaRecorder.OnErrorListener, OnClickListener {
    private AudioManager mAudioManager = null;
    private int mCurStep = 0;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Log.w("MicTest", "handler message BEGIN_RCD");
                if (MicTest.this.beginToRecord()) {
                    MicTest.this.mHandler.sendEmptyMessageDelayed(1, 5000);
                } else {
                    MicTest.this.setCurInfo(2131296910);
                    MicTest.this.mHandler.sendEmptyMessageDelayed(6, 2000);
                }
            } else if (1 == msg.what) {
                Log.w("MicTest", "handler message END_RCD");
                if (MicTest.this.stopRecord()) {
                    MicTest.this.mHandler.sendEmptyMessageDelayed(2, 500);
                } else {
                    MicTest.this.setCurInfo(2131296910);
                    MicTest.this.mHandler.sendEmptyMessageDelayed(6, 2000);
                    Log.w("MicTest", "stopRecord() Failed");
                }
            } else if (2 == msg.what) {
                Log.w("MicTest", "handler message BEGIN_PLAY");
                if (MicTest.this.playRcdFile()) {
                    MicTest.this.setCurInfo(2131296908);
                } else {
                    MicTest.this.setCurInfo(2131296911);
                }
            } else {
                if (6 == msg.what) {
                    Log.w("MicTest", "handler message ENDACTIVITY");
                    MicTest.this.endActivity();
                }
            }
        }
    };
    private MediaPlayer mMediaPlayer = null;
    private MediaRecorder mMediaRecord = null;
    private String mPathName = null;
    File mSampleFile = null;
    private boolean mbIsAutoTest = false;
    private Button mbtnStartPlay = null;
    private Button mbtnStartRcd = null;
    private Button mbtnStopPlay = null;
    private Button mbtnStopRcd = null;
    private String mstrCurrentShow = null;
    private TextView mtvInfoShow = null;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setTitle(2131296903);
        setContentView(2130903148);
        this.mbtnStartRcd = (Button) findViewById(2131493357);
        this.mbtnStartRcd.setOnClickListener(this);
        this.mbtnStopRcd = (Button) findViewById(2131493358);
        this.mbtnStopRcd.setOnClickListener(this);
        this.mbtnStartPlay = (Button) findViewById(2131493359);
        this.mbtnStartPlay.setOnClickListener(this);
        this.mbtnStopPlay = (Button) findViewById(2131493360);
        this.mbtnStopPlay.setOnClickListener(this);
        this.mtvInfoShow = (TextView) findViewById(2131493356);
        this.mstrCurrentShow = getString(2131296905);
        this.mAudioManager = (AudioManager) getSystemService("audio");
        checkSdcard();
    }

    private boolean beginToRecord() {
        setCurInfo(2131296906);
        removeFile();
        if (createTempFile(".amr")) {
            this.mMediaRecord = new MediaRecorder();
            this.mMediaPlayer = new MediaPlayer();
            this.mMediaPlayer.setOnErrorListener(this);
            this.mMediaPlayer.setOnCompletionListener(this);
            this.mMediaRecord.setOnErrorListener(this);
            this.mMediaRecord.setAudioSource(1);
            this.mMediaRecord.setOutputFormat(6);
            this.mMediaRecord.setAudioEncoder(3);
            this.mMediaRecord.setAudioChannels(2);
            this.mMediaRecord.setAudioEncodingBitRate(156000);
            this.mMediaRecord.setAudioSamplingRate(48000);
            Log.i("MicTest", "File Path = " + this.mSampleFile.getAbsolutePath());
            this.mMediaRecord.setOutputFile(this.mSampleFile.getAbsolutePath());
            try {
                Log.w("MicTest", "prepare to record");
                this.mMediaRecord.prepare();
                this.mAudioManager.setParameters("EngineeringMod_MIC_Test_Enable=true");
                this.mMediaRecord.start();
                return true;
            } catch (IOException ioe) {
                Log.w("MicTest", "prepare to record failed, an ioe happen");
                ioe.printStackTrace();
                this.mHandler.sendEmptyMessageDelayed(6, 2000);
                return false;
            } catch (IllegalStateException ioe2) {
                setCurInfo(2131296910);
                Log.w("MicTest", "prepare to record failed, an IllegalStateException happen");
                ioe2.printStackTrace();
                this.mHandler.sendEmptyMessageDelayed(6, 2000);
                return false;
            }
        }
        Log.w("MicTest", "createTempFileFailed");
        return false;
    }

    private boolean stopRecord() {
        Log.w("MicTest", "stopRecord");
        try {
            if (this.mMediaRecord != null) {
                this.mAudioManager.setParameters("EngineeringMod_MIC_Test_Enable=false");
                this.mMediaRecord.stop();
                this.mMediaRecord.reset();
                this.mMediaRecord.release();
                this.mMediaRecord = null;
            }
            setCurInfo(2131296907);
            return true;
        } catch (IllegalStateException iie) {
            Log.w("MicTest", "An IllegalStateException happen when stopRecerd");
            iie.printStackTrace();
            return false;
        }
    }

    private boolean playRcdFile() {
        try {
            Log.w("MicTest", "play File Path: " + this.mSampleFile.getAbsolutePath());
            this.mMediaPlayer.setDataSource(this.mSampleFile.getAbsolutePath());
            this.mMediaPlayer.prepare();
            this.mMediaPlayer.start();
            setCurInfo(2131296908);
            return true;
        } catch (IllegalArgumentException e) {
            Log.w("MicTest", "a IllegalArgumentException happen when prepare to play");
            e.printStackTrace();
            this.mHandler.sendEmptyMessageDelayed(6, 2000);
            return false;
        } catch (IllegalStateException e2) {
            Log.w("MicTest", "a IllegalStateException happen when prepare to play");
            e2.printStackTrace();
            this.mHandler.sendEmptyMessageDelayed(6, 2000);
            return false;
        } catch (IOException e3) {
            Log.w("MicTest", "a IOException happen when prepare to play");
            e3.printStackTrace();
            this.mHandler.sendEmptyMessageDelayed(6, 2000);
            return false;
        }
    }

    private void stopPlay() {
        if (this.mMediaPlayer != null) {
            Log.w("MicTest", "stopPlay");
            try {
                this.mMediaPlayer.stop();
                this.mMediaPlayer.reset();
                this.mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            setCurInfo(2131296909);
            return;
        }
        Log.w("MicTest", "in StopPlay, mMediaPlayer = null");
    }

    private void checkSdcard() {
        Log.w("MicTest", "checkSdcard");
        if (SDCardTest.isInternalAvaiable()) {
            setCurInfo(2131296916);
            this.mbtnStartRcd.setEnabled(true);
            this.mbtnStopRcd.setEnabled(false);
            this.mbtnStartPlay.setEnabled(false);
            this.mbtnStopPlay.setEnabled(false);
            return;
        }
        setCurInfo(2131296899);
        this.mbtnStartRcd.setEnabled(false);
        this.mbtnStopRcd.setEnabled(false);
        this.mbtnStartPlay.setEnabled(false);
        this.mbtnStopPlay.setEnabled(false);
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.w("MicTest", "some error happen when play the music");
        Log.w("MicTest", "Play Error info, what = " + what + "  extra = " + extra);
        setCurInfo(2131296911);
        removeAllMsg();
        this.mHandler.sendEmptyMessageDelayed(6, 2000);
        return false;
    }

    public void onError(MediaRecorder mr, int what, int extra) {
        Log.w("MicTest", "some error happen when recording");
        Log.w("MicTest", "Record Error info, what = " + what + "  extra = " + extra);
        setCurInfo(2131296910);
        removeAllMsg();
        this.mHandler.sendEmptyMessageDelayed(6, 2000);
    }

    public void onCompletion(MediaPlayer mp) {
        Log.w("MicTest", "Play End");
        stopPlay();
        this.mbtnStopPlay.setEnabled(false);
        this.mbtnStartRcd.setEnabled(true);
        this.mbtnStopRcd.setEnabled(true);
        this.mbtnStartPlay.setEnabled(false);
        removeAllMsg();
    }

    private void removeFile() {
        if (this.mSampleFile != null) {
            Log.i("MicTest", "remove the file: " + this.mSampleFile.getAbsolutePath());
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
        this.mtvInfoShow.setText(this.mstrCurrentShow);
    }

    public void onClick(View v) {
        if (v == this.mbtnStartRcd) {
            if (beginToRecord()) {
                Toast.makeText(this, "press-record", 0).show();
                this.mbtnStartRcd.setEnabled(false);
                this.mbtnStopRcd.setEnabled(true);
                ManualTest.requestFoucs(this.mbtnStartRcd);
                return;
            }
            Toast.makeText(this, "failbegin", 0).show();
            setCurInfo(2131296910);
        } else if (v == this.mbtnStopRcd) {
            stopRecord();
            Toast.makeText(this, "stop-record", 0).show();
            this.mbtnStartRcd.setEnabled(false);
            this.mbtnStopRcd.setEnabled(false);
            this.mbtnStartPlay.setEnabled(true);
            this.mbtnStopPlay.setEnabled(true);
            ManualTest.requestFoucs(this.mbtnStartPlay);
        } else if (v == this.mbtnStartPlay) {
            playRcdFile();
            Toast.makeText(this, "press-play", 0).show();
            this.mbtnStartPlay.setEnabled(false);
            this.mbtnStopPlay.setEnabled(true);
            ManualTest.requestFoucs(this.mbtnStopPlay);
        } else if (v == this.mbtnStopPlay) {
            stopPlay();
            Toast.makeText(this, "stop-play", 0).show();
            this.mbtnStopPlay.setEnabled(false);
            this.mbtnStartRcd.setEnabled(true);
            this.mbtnStopRcd.setEnabled(true);
            this.mbtnStartPlay.setEnabled(false);
            Log.i("MicTest", "mbIsAutoTest = " + this.mbIsAutoTest);
            if (this.mbIsAutoTest) {
                finish();
            }
        }
    }

    private boolean createTempFile(String extension) {
        Log.i("MicTest", "create temp file, extension = " + extension);
        if (this.mSampleFile == null && this.mSampleFile == null) {
            this.mPathName = Environment.getDataDirectory().getAbsolutePath();
            File sampleDir = new File(this.mPathName + "/backup");
            Log.i("MicTest", "mPathName = " + this.mPathName);
            if (!sampleDir.canWrite()) {
                sampleDir = new File(Environment.getDataDirectory().getAbsolutePath() + "/sdcard");
            }
            try {
                Log.d("MicTest", "0000000000000000000000000000000000000000000");
                this.mSampleFile = File.createTempFile("recording", extension, sampleDir);
                Log.i("MicTest", "File Path = " + this.mSampleFile.getAbsolutePath());
                Log.i("MicTest", "File Name = " + this.mSampleFile.getName());
            } catch (IOException e) {
                Log.e("MicTest", "A error happen when createTempFile");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private void endActivity() {
        removeFile();
        finish();
    }

    protected void onResume() {
        Log.i("MicTest", "onResume");
        super.onResume();
    }

    protected void onPause() {
        Log.i("MicTest", "onPause");
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        removeFile();
        try {
            if (this.mMediaRecord != null) {
                this.mMediaRecord.release();
                this.mMediaRecord = null;
            }
            if (this.mMediaPlayer != null) {
                this.mMediaPlayer.release();
                this.mMediaPlayer = null;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.mHandler = null;
        this.mstrCurrentShow = null;
    }
}
