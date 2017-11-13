package com.android.engineeringmode.wifitest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings.System;
import android.util.Log;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

public class WifiEnabler implements OnPreferenceChangeListener {
    private final CheckBoxPreference mCheckBox;
    private AtomicBoolean mConnected = new AtomicBoolean(false);
    private final Context mContext;
    private final IntentFilter mIntentFilter;
    private final CharSequence mOriginalSummary;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                WifiEnabler.this.handleWifiStateChanged(intent.getIntExtra("wifi_state", 4));
            } else if ("android.net.wifi.supplicant.STATE_CHANGE".equals(action)) {
                if (!WifiEnabler.this.mConnected.get()) {
                    WifiEnabler.this.handleStateChanged(WifiInfo.getDetailedStateOf((SupplicantState) intent.getParcelableExtra("newState")));
                }
            } else if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                NetworkInfo info = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                WifiEnabler.this.mConnected.set(info.isConnected());
                WifiEnabler.this.handleStateChanged(info.getDetailedState());
            }
        }
    };
    private final WifiManager mWifiManager;

    public WifiEnabler(Context context, CheckBoxPreference checkBox) {
        this.mContext = context;
        this.mCheckBox = checkBox;
        this.mOriginalSummary = checkBox.getSummary();
        checkBox.setPersistent(false);
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        this.mIntentFilter = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
        this.mIntentFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        this.mIntentFilter.addAction("android.net.wifi.STATE_CHANGE");
    }

    public void resume() {
        this.mContext.registerReceiver(this.mReceiver, this.mIntentFilter);
        this.mCheckBox.setOnPreferenceChangeListener(this);
    }

    public void pause() {
        this.mContext.unregisterReceiver(this.mReceiver);
        this.mCheckBox.setOnPreferenceChangeListener(null);
    }

    public static boolean isAirplaneModeOn(Context context) {
        return System.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 0;
    }

    public static boolean isRadioAllowed(Context context, String type) {
        if (!isAirplaneModeOn(context)) {
            return true;
        }
        String toggleable = System.getString(context.getContentResolver(), "airplane_mode_toggleable_radios");
        return toggleable != null ? toggleable.contains(type) : false;
    }

    public boolean onPreferenceChange(Preference preference, Object value) {
        boolean enable = ((Boolean) value).booleanValue();
        if (!enable || isRadioAllowed(this.mContext, "wifi")) {
            int wifiApState = this.mWifiManager.getWifiApState();
            if (enable && (wifiApState == 12 || wifiApState == 13)) {
                this.mWifiManager.setWifiApEnabled(null, false);
            }
            if (this.mWifiManager.setWifiEnabled(enable)) {
                this.mCheckBox.setEnabled(false);
            } else {
                this.mCheckBox.setSummary(2131296457);
            }
            return false;
        }
        Toast.makeText(this.mContext, 2131296458, 0).show();
        return false;
    }

    private void handleWifiStateChanged(int state) {
        switch (state) {
            case 0:
                this.mCheckBox.setSummary(2131296456);
                this.mCheckBox.setEnabled(false);
                return;
            case Light.MAIN_KEY_LIGHT /*1*/:
                this.mCheckBox.setChecked(false);
                this.mCheckBox.setSummary(this.mOriginalSummary);
                this.mCheckBox.setEnabled(true);
                return;
            case Light.CHARGE_RED_LIGHT /*2*/:
                this.mCheckBox.setSummary(2131296455);
                this.mCheckBox.setEnabled(false);
                return;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                this.mCheckBox.setChecked(true);
                this.mCheckBox.setSummary(null);
                this.mCheckBox.setEnabled(true);
                return;
            default:
                this.mCheckBox.setChecked(false);
                this.mCheckBox.setSummary(2131296457);
                this.mCheckBox.setEnabled(true);
                return;
        }
    }

    private void handleStateChanged(DetailedState state) {
        if (state != null && this.mCheckBox.isChecked()) {
            WifiInfo info = this.mWifiManager.getConnectionInfo();
            if (info != null) {
                String str = Summary.get(this.mContext, info.getSSID(), state);
                if (str != null && str.length() > 3) {
                    String str2 = str.substring(0, 3);
                    String str3 = this.mContext.getResources().getString(2131296444);
                    Log.d("WifiEnabler", str2 + "--------------" + str3);
                    if (str2.equals(str3)) {
                        Calendar now = Calendar.getInstance();
                        String str4 = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--WifiEnabler--" + str;
                    }
                }
                this.mCheckBox.setSummary(Summary.get(this.mContext, info.getSSID(), state));
            }
        }
    }
}
