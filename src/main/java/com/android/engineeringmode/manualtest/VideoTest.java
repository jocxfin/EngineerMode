package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.android.engineeringmode.functions.VideoPlayer;
import com.android.engineeringmode.functions.VideoPlayer.Callback;
import com.android.engineeringmode.util.MessageCenter;

public class VideoTest extends Activity implements Callback {
    private boolean mRunning = false;
    private VideoPlayer mVideoPlayer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        setRequestedOrientation(0);
        setTitle(2131296283);
        setContentView(2130903222);
        this.mVideoPlayer = (VideoPlayer) findViewById(2131493594);
        this.mVideoPlayer.setCallback(this);
    }

    private void startPlay() {
        if (!this.mRunning) {
            this.mVideoPlayer.start();
            this.mRunning = true;
        }
    }

    private void stopPlay() {
        if (this.mRunning) {
            this.mVideoPlayer.stopPlayback();
            this.mRunning = false;
        }
    }

    public void onComplete() {
        this.mRunning = false;
        finish();
    }

    public void onError() {
        MessageCenter.showLongMessage((Context) this, 2131296285);
        this.mRunning = false;
        finish();
    }

    protected void onPause() {
        super.onPause();
        stopPlay();
    }

    protected void onResume() {
        super.onResume();
        this.mVideoPlayer.setVideoURI(getIntent().getData());
        startPlay();
    }
}
