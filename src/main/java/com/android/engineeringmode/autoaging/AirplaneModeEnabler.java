package com.android.engineeringmode.autoaging;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

public class AirplaneModeEnabler {
    private final Context mContext;
    private onServiceStateChangedListener mOnServiceStateChangedListener = null;
    Phone mPhone;
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        public void onServiceStateChanged(ServiceState serviceState) {
            Log.w("AirplaneModeEnabler", "serviceState = " + serviceState.getState());
            if (AirplaneModeEnabler.this.mOnServiceStateChangedListener != null) {
                AirplaneModeEnabler.this.mOnServiceStateChangedListener.onServiceStateChanged(serviceState);
            }
        }
    };
    private TelephonyManager mTelephonyManager = null;

    public interface onServiceStateChangedListener {
        void onServiceStateChanged(ServiceState serviceState);
    }

    public AirplaneModeEnabler(Context context) {
        this.mContext = context;
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        this.mPhone = PhoneFactory.getDefaultPhone();
    }

    public void start() {
        this.mTelephonyManager.listen(this.mPhoneStateListener, 1);
        Log.w("AirplaneModeEnabler", "start");
    }

    public void pause() {
        this.mTelephonyManager.listen(this.mPhoneStateListener, 0);
        Log.w("AirplaneModeEnabler", "pause");
    }

    public void setAirplaneModeOn(boolean enabling) {
        this.mPhone.setRadioPower(!enabling);
        Log.e("AirplaneModeEnabler", "setAirplaneModeOn:" + enabling);
    }

    public void setServiceStateChangedListener(onServiceStateChangedListener onServiceStateChangedListener) {
        this.mOnServiceStateChangedListener = onServiceStateChangedListener;
    }
}
