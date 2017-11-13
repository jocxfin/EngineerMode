package com.android.engineeringmode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.Phone;

public class CallManager {
    private boolean mBroadcastRegistered;
    private boolean mCallEnabled;
    private Object mCallLock;
    private BroadcastReceiver mCallStateReceiver;
    private boolean mCalling;
    private Context mContext;
    private String mNumber;
    private Phone mPhone;
    private boolean mThrough;

    public void endCall() {
        Log.w("CallManager", "end call, number=" + this.mNumber);
        synchronized (this.mCallLock) {
            if ((this.mCalling || this.mThrough) && this.mNumber != null) {
                this.mPhone.emHungup(this.mNumber);
                this.mNumber = null;
            }
        }
        ensureCallDisabled();
    }

    public void recycle() {
        synchronized (this.mCallLock) {
            unregisterBroadcast();
            endCall();
            ensureCallDisabled();
        }
    }

    public void finalize() throws Throwable {
        recycle();
        super.finalize();
    }

    private void ensureCallDisabled() {
        if (this.mCallEnabled) {
            setCallAudioMode(0);
            this.mPhone.enableEngineerTest(false);
            notifyMusicResume((AudioManager) this.mContext.getSystemService("audio"));
            this.mCallEnabled = false;
        }
    }

    private void setCallAudioMode(int mode) {
        ((AudioManager) this.mContext.getSystemService("audio")).setMode(mode);
    }

    private void unregisterBroadcast() {
        if (this.mBroadcastRegistered) {
            this.mContext.unregisterReceiver(this.mCallStateReceiver);
            this.mBroadcastRegistered = false;
        }
    }

    public static TelephonyManager getPhoneInterface(Context context) {
        return (TelephonyManager) context.getSystemService("phone");
    }

    public void notifyMusicResume(AudioManager mAudioManager) {
        mAudioManager.abandonAudioFocus(null);
    }
}
