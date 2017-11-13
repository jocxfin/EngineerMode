package com.android.engineeringmode.functions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.lang.ref.WeakReference;

public class HDMI {
    private boolean mBroadcastRegistered = false;
    private WeakReference<Callback> mCallback;
    private Context mContext;
    private HDMIBroadcastReceiver mHDMIBroadcastReceiver;
    private IntentFilter mIntentFilter;

    public interface Callback {
        void onConnected();

        void onDisconnected();
    }

    private class HDMIBroadcastReceiver extends BroadcastReceiver {
        private HDMIBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Callback callback = HDMI.this.getCallback();
            if (callback != null) {
                String action = intent.getAction();
                if ("HDMI_CONNECTED".equals(action)) {
                    callback.onConnected();
                } else if ("HDMI_DISCONNECTED".equals(action)) {
                    callback.onDisconnected();
                }
            }
        }
    }

    public enum Resolution {
        _AUTO,
        _1080,
        _720,
        _576,
        _480
    }

    public HDMI(Context context) {
        this.mContext = context;
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

    public boolean enable() {
        registerBroadcast();
        return true;
    }

    public boolean disable() {
        unRegisterBroadcast();
        return true;
    }

    public boolean isConnected() {
        return false;
    }

    public int getSupportResolutionCount() {
        return Resolution.values().length;
    }

    private void registerBroadcast() {
        if (!this.mBroadcastRegistered) {
            ensureBroadcast();
            this.mContext.registerReceiver(this.mHDMIBroadcastReceiver, this.mIntentFilter);
            this.mBroadcastRegistered = true;
        }
    }

    private void unRegisterBroadcast() {
        if (this.mBroadcastRegistered) {
            this.mContext.unregisterReceiver(this.mHDMIBroadcastReceiver);
            this.mBroadcastRegistered = false;
        }
    }

    private void ensureBroadcast() {
        if (this.mHDMIBroadcastReceiver == null) {
            this.mHDMIBroadcastReceiver = new HDMIBroadcastReceiver();
        }
        if (this.mIntentFilter == null) {
            this.mIntentFilter = new IntentFilter();
            this.mIntentFilter.addAction("HDMI_CONNECTED");
            this.mIntentFilter.addAction("HDMI_DISCONNECTED");
        }
    }
}
