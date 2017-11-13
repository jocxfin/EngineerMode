package com.android.engineeringmode.functions;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class TimeNotifier {
    private WeakReference<Callback> mCallback;
    private long mInterval = 500;
    private long mStartTime;
    private boolean mStopped = false;
    private NotifyTask mTask = new NotifyTask();
    private Timer mTimer = new Timer("NotifyTimer");
    private UpdateHandler mUpdateHandler = new UpdateHandler();

    public interface Callback {
        void onUpdate(long j, long j2);
    }

    private class NotifyTask extends TimerTask {
        private NotifyTask() {
        }

        public void run() {
            if (!TimeNotifier.this.mStopped) {
                TimeNotifier.this.mUpdateHandler.sendEmptyMessage(1);
            }
        }
    }

    private class UpdateHandler extends Handler {
        private UpdateHandler() {
        }

        public void handleMessage(Message msg) {
            if (1 == msg.what) {
                Callback callback = TimeNotifier.this.getCallback();
                if (callback != null) {
                    callback.onUpdate(TimeNotifier.this.mStartTime, System.currentTimeMillis());
                }
            }
        }
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
        this.mStartTime = System.currentTimeMillis();
        this.mStopped = false;
        this.mTimer.schedule(this.mTask, 0, this.mInterval);
    }

    public void stop() {
        this.mStopped = true;
        this.mTimer.cancel();
        this.mTimer.purge();
        this.mStartTime = 0;
    }
}
