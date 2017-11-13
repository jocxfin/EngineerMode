package com.android.engineeringmode.network;

import android.content.Intent;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.engineeringmode.KeepScreenOnPreActivity;
import com.android.engineeringmode.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class NetWorkSet extends KeepScreenOnPreActivity implements OnPreferenceChangeListener {
    int NETWORK_MODE_AUTO_SEL = 20;
    int NETWORK_MODE_GSM_ONLY = 1;
    int NETWORK_MODE_LTE_ONLY = 11;
    int NETWORK_MODE_TDSCDMA_ONLY = 13;
    int NETWORK_MODE_WCDMA_ONLY = 2;
    int NETWORK_MODE_WCDMA_PREF = 0;
    private final String band_key = "band_mode";
    private final String gsm_key = "network_gsm";
    private final String info_key = "network_info";
    private boolean isMultiSimSupported;
    private Preference mBandMode;
    private Preference mGsmPre;
    private Handler mHandler;
    private Preference mInfoPre;
    ArrayList<Integer> mNetWokrModeArrayList = new ArrayList();
    private Phone mPhone;
    private ListPreference mRatPre;
    private ListPreference mServicePre;
    private ListPreference mSim;
    private int mSimNum;
    private String[] ratMode;
    private final String rat_key = "network_rat";
    private final String service_key = "network_service";
    private String[] servideMode;
    private final String sim_key = "sim_select";

    public NetWorkSet() {
        for (int i = 22; i >= 0; i--) {
            this.mNetWokrModeArrayList.add(Integer.valueOf(i));
        }
        this.isMultiSimSupported = false;
        this.mSimNum = 0;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                Log.w("NetWorkSet", "handleMessage:" + msg.what);
                AsyncResult ar = msg.obj;
                if (!(ar == null || ar.exception == null)) {
                    ar.exception.printStackTrace();
                }
                int type;
                if (msg.what == 1) {
                    Log.i("NetWorkSet", "handleMessage RAT_MODE_QUERY_MESSAGE");
                    if (ar.exception == null) {
                        type = ((int[]) ar.result)[0];
                        Log.i("NetWorkSet", "handleMessage type:" + type);
                        for (int i = 0; i < NetWorkSet.this.mNetWokrModeArrayList.size(); i++) {
                            if (type == ((Integer) NetWorkSet.this.mNetWokrModeArrayList.get(i)).intValue()) {
                                Log.i("NetWorkSet", "handleMessage cur index:" + i);
                                Log.i("NetWorkSet", "mNetWokrModeArrayList.get(i):" + NetWorkSet.this.mNetWokrModeArrayList.get(i));
                                NetWorkSet.this.mRatPre.setValueIndex(i);
                                return;
                            }
                        }
                    } else {
                        Log.e("NetWorkSet", "handleMessage RAT_MODE_QUERY_MESSAGE exception:" + ar.exception);
                    }
                } else if (msg.what == 2) {
                    if (ar.exception == null) {
                        Toast.makeText(NetWorkSet.this, "set successfully", 0).show();
                    } else {
                        Toast.makeText(NetWorkSet.this, "set failed", 0).show();
                    }
                } else if (msg.what == 3) {
                    if (ar.exception == null) {
                        String[] serviceValues = NetWorkSet.this.getResources().getStringArray(2131099654);
                        type = ((byte[]) ar.result)[2];
                        if (type >= 0 && type <= 2) {
                            NetWorkSet.this.mServicePre.setValueIndex(type);
                        }
                    }
                } else if (msg.what == 4) {
                    if (ar.exception == null) {
                        Toast.makeText(NetWorkSet.this, "set successfully", 0).show();
                    } else {
                        Toast.makeText(NetWorkSet.this, "set failed", 0).show();
                    }
                }
            }
        };
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2130968607);
        this.mSim = (ListPreference) findPreference("sim_select");
        this.mGsmPre = findPreference("network_gsm");
        this.mInfoPre = findPreference("network_info");
        this.mServicePre = (ListPreference) findPreference("network_service");
        this.mRatPre = (ListPreference) findPreference("network_rat");
        this.mBandMode = findPreference("band_mode");
        this.mServicePre.setOnPreferenceChangeListener(this);
        this.mRatPre.setOnPreferenceChangeListener(this);
        this.mSim.setOnPreferenceChangeListener(this);
        this.servideMode = getResources().getStringArray(2131099653);
        this.ratMode = getResources().getStringArray(2131099655);
        this.isMultiSimSupported = TelephonyManager.getDefault().isMultiSimEnabled();
        if (this.isMultiSimSupported) {
            Log.d("NetWorkSet", "selected sim num: " + Integer.valueOf(this.mSim.getValue()));
            this.mPhone = PhoneFactory.getPhone(Integer.valueOf(this.mSim.getValue()).intValue());
        } else {
            this.mPhone = PhoneFactory.getDefaultPhone();
            getPreferenceScreen().removePreference(this.mSim);
        }
        this.mPhone.getSrvDomainPref(this.mHandler.obtainMessage(3));
    }

    protected void onResume() {
        super.onResume();
        this.mPhone.getPreferredNetworkType(this.mHandler.obtainMessage(1));
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        Intent intent;
        if ("network_gsm".equals(key)) {
            intent = new Intent(this, GsmInfo.class);
            intent.putExtra("phone_type", 1);
            intent.putExtra("sim_num", this.mSimNum);
            startActivity(intent);
        } else if ("network_info".equals(key)) {
            intent = new Intent(this, NetWorkInfo.class);
            intent.putExtra("sim_num", this.mSimNum);
            startActivity(intent);
        } else if ("band_mode".equals(key)) {
            intent = new Intent(this, BandMode2.class);
            intent.putExtra("sim_num", this.mSimNum);
            startActivity(intent);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        int mode;
        if ("network_service".equals(key)) {
            mode = Integer.valueOf((String) newValue).intValue();
            Log.e("NetWorkSet", "ServiceMode:" + mode);
            try {
                this.mPhone.setSrvDomainPref(mode, this.mHandler.obtainMessage(4));
            } catch (InvalidParameterException e) {
                Toast.makeText(this, "has a InvalidParameterException", 0).show();
                e.printStackTrace();
            }
            return true;
        } else if ("network_rat".equals(key)) {
            mode = Integer.valueOf((String) newValue).intValue();
            Log.e("NetWorkSet", "RatMode:" + mode);
            this.mPhone.setPreferredNetworkType(mode, this.mHandler.obtainMessage(2));
            return true;
        } else if (!"sim_select".equals(key)) {
            return false;
        } else {
            int num = Integer.valueOf((String) newValue).intValue();
            this.mSimNum = num;
            Log.e("NetWorkSet", "sim:" + num);
            this.mPhone = PhoneFactory.getPhone(num);
            this.mPhone.getSrvDomainPref(this.mHandler.obtainMessage(3));
            this.mPhone.getPreferredNetworkType(this.mHandler.obtainMessage(1));
            return true;
        }
    }
}
