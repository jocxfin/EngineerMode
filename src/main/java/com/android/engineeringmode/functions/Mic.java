package com.android.engineeringmode.functions;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.util.Log;

import com.android.engineeringmode.parameter.GlobalParameter;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class Mic {
    private WeakReference<Callback> mCallback;
    private OnErrorListener mErrorListener = new OnErrorListener() {
        public void onError(MediaRecorder mr, int what, int extra) {
            Callback callback = Mic.this.getCallback();
            if (callback != null) {
                callback.onRecordError();
            }
        }
    };
    private OnInfoListener mInfoListener = new OnInfoListener() {
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Callback callback = Mic.this.getCallback();
            if (callback == null) {
                return;
            }
            if (800 == what) {
                callback.onRecordInfo(17);
            } else if (801 == what) {
                callback.onRecordInfo(18);
            }
        }
    };
    private MediaRecorder mRecorder;
    File recordFile;

    public interface Callback {
        void onRecordError();

        void onRecordInfo(int i);

        void onRecordStart();

        void onRecordStop();

        void onStorageError(int i);
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

    public void start() {
        start(-1);
    }

    public void start(int maxDuration) {
        Callback callback = getCallback();
        if (GlobalParameter.hasRecordStorage()) {
            String recordDir = GlobalParameter.getRecordStorage();
            this.recordFile = new File(recordDir, GlobalParameter.getCurrentDateTime() + ".amr");
            Log.d("cbt", "" + recordDir.toString());
            if (!this.recordFile.exists() || this.recordFile.delete()) {
                rePrepare();
                this.mRecorder.setOutputFile(this.recordFile.getAbsolutePath());
                if (maxDuration > 0) {
                    try {
                        this.mRecorder.setMaxDuration(maxDuration);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    this.mRecorder.prepare();
                    this.mRecorder.start();
                    if (callback != null) {
                        callback.onRecordStart();
                    }
                } catch (IllegalStateException e2) {
                    e2.printStackTrace();
                    recycle();
                    if (callback != null) {
                        callback.onRecordError();
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                    recycle();
                    if (callback != null) {
                        callback.onRecordError();
                    }
                }
                return;
            }
            if (callback != null) {
                callback.onStorageError(3);
            }
            return;
        }
        if (callback != null) {
            callback.onStorageError(1);
        }
    }

    public void stop() {
        recycle();
        removeFile();
        Callback callback = getCallback();
        if (callback != null) {
            callback.onRecordStop();
        }
    }

    private void rePrepare() {
        recycle();
        prepare();
    }

    private void prepare() {
        if (this.mRecorder == null) {
            this.mRecorder = new MediaRecorder();
            this.mRecorder.setOnInfoListener(this.mInfoListener);
            this.mRecorder.setOnErrorListener(this.mErrorListener);
            this.mRecorder.setAudioSource(1);
            this.mRecorder.setOutputFormat(3);
            this.mRecorder.setAudioEncoder(1);
        }
    }

    private void recycle() {
        if (this.mRecorder != null) {
            try {
                this.mRecorder.stop();
            } catch (IllegalStateException e) {
            }
            this.mRecorder.reset();
            this.mRecorder.release();
            this.mRecorder = null;
        }
    }

    private void removeFile() {
        if (this.recordFile != null) {
            Log.i("recordFile", "remove the file: " + this.recordFile.getAbsolutePath());
            this.recordFile.delete();
        }
        this.recordFile = null;
    }
}
