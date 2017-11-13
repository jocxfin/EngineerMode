package com.android.engineeringmode.gps;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.MediaController;
import android.widget.VideoView;

import com.android.engineeringmode.gps.GpsEnvService.MyBinder;

public class GpsEnvVideoTestActivity extends Activity {
    private Handler mHandler;
    private GpsEnvService mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("GpsEnvVideoTestActivity", " service connected");
            GpsEnvVideoTestActivity.this.mService = ((MyBinder) service).getService();
            GpsEnvVideoTestActivity.this.playVedio();
            GpsEnvVideoTestActivity.this.mService.requestLocationUpdate(8);
            GpsEnvVideoTestActivity.this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    Log.d("GpsEnvVideoTestActivity", "stop play");
                    GpsEnvVideoTestActivity.this.videoView.stopPlayback();
                    GpsEnvVideoTestActivity.this.finish();
                }
            }, 15000);
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d("GpsEnvVideoTestActivity", "service disconnected");
        }
    };
    String[] mediaColumns = new String[]{"_id", "_data", "title", "mime_type", "_display_name"};
    MediaController mediaController;
    private int screenBrightness;
    private int screenMode;
    private VideoView videoView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(-3);
        setContentView(2130903103);
        this.videoView = (VideoView) findViewById(2131493124);
        this.mediaController = new MediaController(this);
        Intent intent = new Intent();
        intent.setClass(this, GpsEnvService.class);
        Log.d("GpsEnvVideoTestActivity", "start and bind service");
        bindService(intent, this.mServiceConnection, 1);
        this.mHandler = new Handler();
        getWindow().addFlags(4194304);
        set_screen_brightness();
    }

    private void playVedio() {
        String path = "android.resource://" + getPackageName() + "/" + 2131034119;
        Log.d("GpsEnvVideoTestActivity", "play video");
        this.videoView.setVideoPath(path);
        this.videoView.setMediaController(this.mediaController);
        this.mediaController.setMediaPlayer(this.videoView);
        this.videoView.requestFocus();
        this.videoView.start();
    }

    protected void onResume() {
        super.onResume();
    }

    private void set_screen_brightness() {
        try {
            this.screenMode = System.getInt(getContentResolver(), "screen_brightness_mode");
            Log.i("GpsEnvVideoTestActivity", "screenMode = " + this.screenMode);
            this.screenBrightness = System.getInt(getContentResolver(), "screen_brightness");
            Log.i("GpsEnvVideoTestActivity", "screenBrightness = " + this.screenBrightness);
            if (this.screenMode == 1) {
                setScreenMode(0);
            }
            setScreenBrightness(255.0f);
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setScreenMode(int value) {
        System.putInt(getContentResolver(), "screen_brightness_mode", value);
    }

    private void setScreenBrightness(float value) {
        Window mWindow = getWindow();
        LayoutParams mParams = mWindow.getAttributes();
        mParams.screenBrightness = value / 255.0f;
        mWindow.setAttributes(mParams);
        System.putInt(getContentResolver(), "screen_brightness", (int) value);
    }

    private void restore_brightness() {
        setScreenBrightness((float) this.screenBrightness);
        setScreenMode(this.screenMode);
    }

    protected void onStop() {
        super.onStop();
        restore_brightness();
    }
}
