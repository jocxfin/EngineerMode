package com.android.engineeringmode.call;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.engineeringmode.functions.Light;
import com.android.internal.telephony.Phone;

import java.lang.ref.WeakReference;

public class ExCallManager {
    private int currVolume = 0;
    private boolean enable_speaker = false;
    private AudioManager mAudioManager = null;
    private WeakReference<Callbacks> mCallbacks = null;
    private Context mContext = null;
    private String mCurrentNumber = "112";
    private Phone mPhone = null;
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case 0:
                    Log.d("ExCallManager", "mPhoneStateListener: idle!!!");
                    ExCallManager.this.CloseSpeaker();
                    ((Callbacks) ExCallManager.this.mCallbacks.get()).onIdle();
                    return;
                case Light.MAIN_KEY_LIGHT /*1*/:
                    Log.d("ExCallManager", "mPhoneStateListener: ring!!!");
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    Log.d("ExCallManager", "mPhoneStateListener: OFFHOOK!!!");
                    SystemClock.sleep(200);
                    ExCallManager.this.OpenSpeaker();
                    ((Callbacks) ExCallManager.this.mCallbacks.get()).onOffhook();
                    return;
                default:
                    return;
            }
        }
    };
    private TelephonyManager mTelephonyManager;

    public interface Callbacks {
        void onIdle();

        void onOffhook();
    }

    public ExCallManager(Context context) {
        this.mContext = context;
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
    }

    public boolean call(String number) {
        if (number == null) {
            return false;
        }
        if (number.equals("112")) {
            return callPrivilegedNumber(number);
        }
        Log.d("ExCallManager", "call the number: " + number);
        this.mCurrentNumber = number;
        Intent localIntent1 = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number));
        localIntent1.setFlags(268435456);
        if (hasDoubleSimCardGemini()) {
            Log.d("ExCallManager", "subscription 0");
            localIntent1.putExtra("subscription", 0);
        }
        this.mContext.startActivity(localIntent1);
        return true;
    }

    public boolean hasDoubleSimCardGemini() {
        Log.d("ExCallManager", "hasDoubleSimCardGemini");
        return TelephonyManager.getDefault().isMultiSimEnabled();
    }

    private boolean callPrivilegedNumber(String number) {
        if (number == null) {
            return false;
        }
        Log.d("ExCallManager", "call the privileged number: " + number);
        this.mCurrentNumber = number;
        Intent localIntent1 = new Intent("android.intent.action.CALL_PRIVILEGED", Uri.parse("tel:" + number));
        localIntent1.setFlags(268435456);
        this.mContext.startActivity(localIntent1);
        return true;
    }

    public void endCall() {
        this.mTelephonyManager.endCall();
        if (this.mAudioManager != null) {
            this.mAudioManager.setMode(0);
        }
    }

    public void start() {
        this.mTelephonyManager.listen(this.mPhoneStateListener, 32);
        this.currVolume = this.mAudioManager.getStreamVolume(0);
    }

    public void stop() {
        this.mTelephonyManager.listen(this.mPhoneStateListener, 0);
    }

    public void setPhoneStaeteCallBacks(Callbacks cb) {
        WeakReference weakReference = null;
        if (cb != null) {
            weakReference = new WeakReference(cb);
        }
        this.mCallbacks = weakReference;
    }

    public void OpenSpeaker() {
        if (this.enable_speaker) {
            try {
                this.mAudioManager.setMode(2);
                if (!this.mAudioManager.isSpeakerphoneOn()) {
                    Log.i("ExCallManager", "OpenSpeaker");
                    this.mAudioManager.setSpeakerphoneOn(true);
                    this.mAudioManager.setStreamVolume(0, this.mAudioManager.getStreamMaxVolume(0), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void CloseSpeaker() {
        if (this.enable_speaker) {
            try {
                if (this.mAudioManager != null && this.mAudioManager.isSpeakerphoneOn()) {
                    Log.i("ExCallManager", "CloseSpeaker");
                    this.mAudioManager.setSpeakerphoneOn(false);
                    this.mAudioManager.setStreamVolume(0, this.currVolume, 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
