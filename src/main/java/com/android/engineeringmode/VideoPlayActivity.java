package com.android.engineeringmode;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.engineeringmode.autotest.AutoTestItemActivity;
import com.android.engineeringmode.parameter.GlobalParameter;

import java.io.File;

public class VideoPlayActivity extends AutoTestItemActivity implements OnErrorListener, OnCompletionListener {
    private VideoView mVideoView = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setRequestedOrientation(0);
        Uri videoUri = getDefaultVedioUri();
        if (videoUri == null) {
            Toast.makeText(this, getPrintableFragment(GlobalParameter.getVideoPath()), 1).show();
            if (checkIsAutoTest() || checkIsAutoAging()) {
                setAutoExit(0);
            } else {
                finish();
            }
            return;
        }
        setContentView(2130903219);
        this.mVideoView = (VideoView) findViewById(2131493591);
        this.mVideoView.setOnErrorListener(this);
        this.mVideoView.setOnCompletionListener(this);
        this.mVideoView.setVideoURI(videoUri);
        this.mVideoView.requestFocus();
        this.mVideoView.start();
        if (checkIsAutoTest()) {
            setAutoExit(5000);
        }
    }

    private Uri getDefaultVedioUri() {
        String videoFilePath = GlobalParameter.getVideoPath();
        return new File(videoFilePath).exists() ? Uri.parse(videoFilePath) : null;
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        releaseMediaPlayer(mp);
        Toast.makeText(this, 2131296284, 1).show();
        if (checkIsAutoTest() || checkIsAutoAging()) {
            setAutoExit(0);
            return false;
        }
        finish();
        return false;
    }

    public void onCompletion(MediaPlayer mp) {
        releaseMediaPlayer(mp);
        if (checkIsAutoTest() || checkIsAutoAging()) {
            setAutoExit(0);
        } else {
            finish();
        }
    }

    private void releaseMediaPlayer(MediaPlayer mp) {
        if (mp != null) {
            try {
                mp.stop();
                mp.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getPrintableFragment(String fileName) {
        return String.format(getString(2131296286), new Object[]{fileName});
    }
}
