package com.android.engineeringmode.autoaging;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.android.engineeringmode.functions.VideoPlayer;
import com.android.engineeringmode.functions.VideoPlayer.Callback;
import com.android.engineeringmode.util.MessageCenter;

public class VideoTest extends BaseTest implements Callback {
    private boolean mRunning = false;
    Uri mUri;
    private VideoPlayer mVideoPlayer;

    public void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        setRequestedOrientation(0);
        setTitle(2131296283);
        setContentView(2130903222);
        this.mVideoPlayer = (VideoPlayer) findViewById(2131493594);
        this.mVideoPlayer.setCallback(this);
        Uri uri = this.mUri;
        this.mUri = Uri.parse("android.resource://" + getPackageName() + "/" + 2131034119);
        this.mVideoPlayer.setVideoURI(this.mUri);
    }

    protected void runTest() {
        startPlay();
    }

    protected void endTest() {
        stopPlay();
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
        startPlay();
    }

    public void onError() {
        MessageCenter.showLongMessage((Context) this, 2131296285);
        recordResult(false);
        this.mRunning = false;
        endTest();
        finish();
    }
}
