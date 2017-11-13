package com.android.engineeringmode.autotest;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.android.engineeringmode.Log;
import com.android.engineeringmode.audiotest.InternalRingtonePlay;
import com.android.engineeringmode.audiotest.InternalRingtonePlay.RingtonePlayCompletionListener;
import com.android.engineeringmode.audiotest.InternalRingtonePlay.RingtonePlayErrorListener;

public class AutoTestMusicPlay extends AutoTestItemActivity implements RingtonePlayErrorListener, RingtonePlayCompletionListener {
    private InternalRingtonePlay mInternalRngPlayer = null;
    private TextView mtvShowInfo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(2131296694);
        this.mtvShowInfo = new TextView(this);
        this.mtvShowInfo.setText(2131296694);
        this.mtvShowInfo.setTextSize(40.0f);
        this.mtvShowInfo.setGravity(17);
        this.mtvShowInfo.setTextColor(getResources().getColor(17170444));
        setContentView(this.mtvShowInfo);
        this.mInternalRngPlayer = new InternalRingtonePlay(this);
        this.mInternalRngPlayer.setOnCompletionListener(this);
        this.mInternalRngPlayer.setOnErrorListener(this);
        this.mInternalRngPlayer.play();
        setAutoExit(6000);
        setRequestedOrientation(1);
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.w("AutoTestMusicPlay", "some error happen when player music");
        this.mtvShowInfo.setText(2131296696);
        this.mInternalRngPlayer.stop();
        return false;
    }

    protected void onDestroy() {
        Log.i("AutoTestMusicPlay", "onDestroy");
        this.mInternalRngPlayer.stop();
        this.mInternalRngPlayer = null;
        super.onDestroy();
    }

    public boolean onCompletion() {
        Log.i("AutoTestMusicPlay", "onCompletion");
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("AutoTestMusicPlay", "keyCode = " + keyCode);
        return false;
    }
}
