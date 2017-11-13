package com.android.engineeringmode.functions;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.util.AttributeSet;
import android.widget.VideoView;

import java.lang.ref.WeakReference;

public class VideoPlayer extends VideoView {
    private WeakReference<Callback> mCallback;
    private OnCompletionListener mCompletionListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            Callback callback = VideoPlayer.this.getCallback();
            if (callback != null) {
                callback.onComplete();
            }
        }
    };
    private OnErrorListener mErrorListener = new OnErrorListener() {
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Callback callback = VideoPlayer.this.getCallback();
            if (callback != null) {
                callback.onError();
            }
            return true;
        }
    };

    public interface Callback {
        void onComplete();

        void onError();
    }

    public VideoPlayer(Context context) {
        super(context);
        init();
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoPlayer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOnCompletionListener(this.mCompletionListener);
        setOnErrorListener(this.mErrorListener);
    }

    public void setCallback(Callback callback) {
        WeakReference weakReference = null;
        if (callback != null) {
            weakReference = new WeakReference(callback);
        }
        this.mCallback = weakReference;
    }

    public Callback getCallback() {
        if (this.mCallback == null) {
            return null;
        }
        return (Callback) this.mCallback.get();
    }
}
