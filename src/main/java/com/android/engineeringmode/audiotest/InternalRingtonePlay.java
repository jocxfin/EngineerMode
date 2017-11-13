package com.android.engineeringmode.audiotest;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;

import com.android.engineeringmode.Log;

public class InternalRingtonePlay implements OnCompletionListener, OnErrorListener {
    private Context mContext = null;
    private int mCurRepeatTime = 0;
    private MediaPlayer mMediaPlayer = null;
    private RingtonePlayCompletionListener mPlayCompletionListener = null;
    private RingtonePlayErrorListener mPlayErrorListener = null;
    private int mRepeatTime = 0;
    private Uri mUri = null;

    public interface RingtonePlayCompletionListener {
        boolean onCompletion();
    }

    public interface RingtonePlayErrorListener {
        boolean onError(MediaPlayer mediaPlayer, int i, int i2);
    }

    public InternalRingtonePlay(Context context) {
        this.mContext = context;
        this.mMediaPlayer = new MediaPlayer();
        this.mMediaPlayer.setOnErrorListener(this);
        this.mMediaPlayer.setOnCompletionListener(this);
    }

    public void setRepeatTime(int nRepeatTime) {
        this.mRepeatTime = nRepeatTime;
    }

    public void play() {
        try {
            prepare();
            this.mMediaPlayer.start();
        } catch (IllegalStateException ile) {
            Log.w("InternalRingtonePlay", "An Exception happen when start Music play!");
            ile.printStackTrace();
        }
        Log.w("InternalRingtonePlay", "Music play begin and keep screen on");
    }

    public void stop() {
        if (this.mMediaPlayer != null) {
            try {
                this.mMediaPlayer.stop();
                this.mMediaPlayer.release();
                this.mMediaPlayer = null;
                Log.w("InternalRingtonePlay", "stop, Music play stop");
                return;
            } catch (Exception e) {
                Log.w("InternalRingtonePlay", "An Exception happen when stop Music play!");
                e.printStackTrace();
                return;
            }
        }
        Log.w("InternalRingtonePlay", "stop, mMediaPlayer = null");
    }

    private void prepare() {
        if (this.mMediaPlayer == null) {
            this.mMediaPlayer = new MediaPlayer();
        }
        this.mMediaPlayer.setOnCompletionListener(this);
        this.mMediaPlayer.setOnErrorListener(this);
        this.mMediaPlayer.setVolume(1.25f, 1.25f);
        if (this.mUri != null) {
            try {
                this.mMediaPlayer.setDataSource(this.mContext, this.mUri);
                this.mMediaPlayer.prepare();
                return;
            } catch (Exception e) {
                Log.e("InternalRingtonePlay", "play() caught Exception: ");
                e.printStackTrace();
                return;
            }
        }
        try {
            AssetFileDescriptor afd = this.mContext.getResources().openRawResourceFd(2131034113);
            if (afd.getDeclaredLength() < 0) {
                this.mMediaPlayer.setDataSource(afd.getFileDescriptor());
                Log.w("InternalRingtonePlay", "afd.getDeclaredLength() < 0");
            } else {
                this.mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
                Log.w("InternalRingtonePlay", "afd.getDeclaredLength() >= 0");
            }
            this.mMediaPlayer.prepare();
        } catch (Exception e2) {
            Log.e("InternalRingtonePlay", "A Error hapen when prepare: ");
            e2.printStackTrace();
        }
    }

    public void onCompletion(MediaPlayer mp) {
        Log.w("InternalRingtonePlay", "onCompletion, Music play end");
        if (-1 == this.mRepeatTime) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.reset();
            prepare();
            this.mMediaPlayer.start();
            return;
        }
        if (this.mCurRepeatTime < this.mRepeatTime) {
            Log.i("InternalRingtonePlay", "mCurRepeatTime = " + this.mCurRepeatTime);
            this.mMediaPlayer.stop();
            this.mMediaPlayer.reset();
            prepare();
            this.mMediaPlayer.start();
            this.mCurRepeatTime++;
        } else {
            if (this.mPlayCompletionListener != null) {
                this.mPlayCompletionListener.onCompletion();
            }
            mp.stop();
            mp.release();
        }
    }

    public void setOnErrorListener(RingtonePlayErrorListener playErrorListener) {
        this.mPlayErrorListener = playErrorListener;
    }

    public void setOnCompletionListener(RingtonePlayCompletionListener playCompletionListener) {
        this.mPlayCompletionListener = playCompletionListener;
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (this.mPlayErrorListener != null) {
            return this.mPlayErrorListener.onError(mp, what, extra);
        }
        return false;
    }
}
