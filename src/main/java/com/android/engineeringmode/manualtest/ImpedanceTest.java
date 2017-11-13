package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.media.AudioSystem;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;

import java.io.File;

public class ImpedanceTest extends Activity {
    private static String dirpath;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    ImpedanceTest.this.doTest();
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    Log.i("ImpedanceTest", "start impedance test");
                    ImpedanceTest.this.palyAudio(2131034117);
                    ImpedanceTest.this.mHandler.sendEmptyMessageDelayed(1, 3000);
                    return;
                case Light.CHARGE_GREEN_LIGHT /*3*/:
                    ImpedanceTest.this.stopAudio();
                    return;
                default:
                    return;
            }
        }
    };
    private int mMediaForuse = 0;
    private MediaPlayer mMediaPlayer;
    TextView mTvResult;
    private int testCount = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903122);
        this.mTvResult = (TextView) findViewById(2131493222);
        this.mHandler.sendEmptyMessage(2);
    }

    public void getProject() {
        dirpath = SystemProperties.get("ro.boot.project_name");
        String project2 = "15801";
        String project3 = "15811";
        String project4 = "16859";
        if (dirpath.compareTo("14049") == 0) {
            dirpath = "/sys/bus/i2c/devices/i2c-5/5-0036/calibra";
        } else if (dirpath.compareTo(project2) == 0 || dirpath.compareTo(project3) == 0) {
            dirpath = "/sys/bus/i2c/devices/i2c-3/3-0036/calibra";
        } else if (dirpath.compareTo(project4) == 0) {
            dirpath = "/sys/bus/i2c/devices/i2c-9/9-0036/calibra";
        } else {
            dirpath = "/sys/bus/i2c/devices/i2c-6/6-0036/calibra";
        }
    }

    void doTest() {
        String tmp = null;
        getProject();
        Log.i("ImpedanceTest", "testCount:" + this.testCount);
        try {
            tmp = FileUtils.readTextFile(new File(dirpath), 0, null).trim();
        } catch (Exception e) {
            Log.e("ImpedanceTest", "kang failed to read from " + dirpath);
        }
        Log.i("ImpedanceTest", "readstring:" + tmp);
        if (tmp != null) {
            String[] sourceStrArray = tmp.split(":");
            for (int i = 0; i < sourceStrArray.length; i++) {
                Log.i("ImpedanceTest", "sourceStrArray" + i + ":" + sourceStrArray[i]);
            }
            if (sourceStrArray.length < 2) {
                Log.e("ImpedanceTest", "sourceStrArray.length < 2");
                this.mTvResult.setText("FAIL\n error value\n(6000~9000)");
                this.mTvResult.setTextColor(-65536);
                this.mHandler.sendEmptyMessage(3);
                return;
            }
            int value = Integer.parseInt(sourceStrArray[1]);
            Log.i("ImpedanceTest", "impedance value:" + value);
            if (!"2".equals(sourceStrArray[0])) {
                this.testCount++;
                Log.e("ImpedanceTest", "calibrate error");
                if (this.testCount > 3) {
                    this.mTvResult.setText("FAIL\n calibrate error\n(6000~9000)");
                    this.mTvResult.setTextColor(-65536);
                    this.mHandler.sendEmptyMessage(3);
                    return;
                }
                this.mHandler.sendEmptyMessageDelayed(1, 1000);
                return;
            } else if (value < 6000 || value > 9000) {
                this.testCount++;
                Log.e("ImpedanceTest", "impedance error");
                if (this.testCount > 3) {
                    this.mTvResult.setText("FAIL\n " + value + "\n" + "(" + 6000 + "~" + 9000 + ")");
                    this.mTvResult.setTextColor(-65536);
                    this.mHandler.sendEmptyMessage(3);
                    return;
                }
                this.mHandler.sendEmptyMessageDelayed(1, 1000);
                return;
            } else {
                Log.d("ImpedanceTest", "success");
                this.mTvResult.setText("PASS\n " + value + "\n" + "(" + 6000 + "~" + 9000 + ")");
                this.mTvResult.setTextColor(-16711936);
            }
        } else {
            Log.d("ImpedanceTest", "no value");
            this.mTvResult.setText("FAIL\n error value\n(6000~9000)");
            this.mTvResult.setTextColor(-65536);
        }
        this.mHandler.sendEmptyMessage(3);
    }

    private void palyAudio(int resId) {
        Log.d("ImpedanceTest", "palyAudio");
        if (this.mMediaPlayer == null) {
            this.mMediaPlayer = MediaPlayer.create(this, resId);
            this.mMediaPlayer.setAudioStreamType(3);
            this.mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                }
            });
        }
        this.mMediaPlayer.start();
    }

    private void stopAudio() {
        Log.d("ImpedanceTest", "stopAudio");
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    protected void onResume() {
        super.onResume();
        this.mMediaForuse = AudioSystem.getForceUse(1);
        Log.i("ImpedanceTest", "store mMediaForuse:" + this.mMediaForuse);
        AudioSystem.setForceUse(1, 1);
    }

    protected void onPause() {
        super.onPause();
        Log.i("ImpedanceTest", "restore mMediaForuse:" + this.mMediaForuse);
        AudioSystem.setForceUse(1, this.mMediaForuse);
    }

    protected void onStop() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(3);
        super.onStop();
        stopAudio();
    }
}
