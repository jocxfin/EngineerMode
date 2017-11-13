package com.android.engineeringmode.wifitest;

import android.content.ContentResolver;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;

public class AdvancedSettings extends PreferenceActivity implements OnPreferenceChangeListener {
    private static int DEBUGGABLE;
    private String[] mPreferenceKeys = new String[]{"ip_address", "gateway", "netmask", "dns1", "dns2"};
    private String[] mSettingNames = new String[]{"wifi_static_ip", "wifi_static_gateway", "wifi_static_netmask", "wifi_static_dns1", "wifi_static_dns2"};
    private CheckBoxPreference mUseStaticIpCheckBox;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2130968618);
        this.mUseStaticIpCheckBox = (CheckBoxPreference) findPreference("use_static_ip");
        this.mUseStaticIpCheckBox.setOnPreferenceChangeListener(this);
        for (CharSequence findPreference : this.mPreferenceKeys) {
            findPreference(findPreference).setOnPreferenceChangeListener(this);
        }
        DEBUGGABLE = 0;
        if (DEBUGGABLE == 1) {
            initNumChannelsPreference();
            return;
        }
        Preference chanPref = findPreference("num_channels");
        if (chanPref != null) {
            getPreferenceScreen().removePreference(chanPref);
        }
    }

    protected void onResume() {
        super.onResume();
        updateUi();
        if (DEBUGGABLE == 1) {
            initNumChannelsPreference();
        }
        initSleepPolicyPreference();
        refreshMacAddress();
    }

    private void initNumChannelsPreference() {
    }

    private void initSleepPolicyPreference() {
        ListPreference pref = (ListPreference) findPreference("sleep_policy");
        pref.setOnPreferenceChangeListener(this);
        pref.setValue(String.valueOf(System.getInt(getContentResolver(), "wifi_sleep_policy", 0)));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            updateSettingsProvider();
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if (key != null && !key.equals("num_channels")) {
            if (key.equals("sleep_policy")) {
                try {
                    System.putInt(getContentResolver(), "wifi_sleep_policy", Integer.parseInt((String) newValue));
                } catch (NumberFormatException e) {
                    Toast.makeText(this, 2131296501, 0).show();
                    return false;
                }
            } else if (key.equals("use_static_ip")) {
                boolean value = ((Boolean) newValue).booleanValue();
                try {
                    int i;
                    ContentResolver contentResolver = getContentResolver();
                    String str = "wifi_use_static_ip";
                    if (value) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    System.putInt(contentResolver, str, i);
                } catch (NumberFormatException e2) {
                    return false;
                }
            } else {
                String value2 = (String) newValue;
                if (isIpAddress(value2)) {
                    preference.setSummary(value2);
                    for (int i2 = 0; i2 < this.mSettingNames.length; i2++) {
                        if (key.equals(this.mPreferenceKeys[i2])) {
                            System.putString(getContentResolver(), this.mSettingNames[i2], value2);
                            break;
                        }
                    }
                } else {
                    Toast.makeText(this, 2131296506, 1).show();
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isIpAddress(String value) {
        boolean z = false;
        int start = 0;
        int end = value.indexOf(46);
        int numBlocks = 0;
        while (start < value.length()) {
            if (end == -1) {
                end = value.length();
            }
            try {
                int block = Integer.parseInt(value.substring(start, end));
                if (block > Light.MAIN_KEY_MAX || block < 0) {
                    return false;
                }
                numBlocks++;
                start = end + 1;
                end = value.indexOf(46, start);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        if (numBlocks == 4) {
            z = true;
        }
        return z;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, 2131296504).setIcon(17301582);
        menu.add(0, 2, 0, 2131296505).setIcon(17301560);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                updateSettingsProvider();
                finish();
                return true;
            case Light.CHARGE_RED_LIGHT /*2*/:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUi() {
        boolean z = false;
        ContentResolver contentResolver = getContentResolver();
        CheckBoxPreference checkBoxPreference = this.mUseStaticIpCheckBox;
        if (System.getInt(contentResolver, "wifi_use_static_ip", 0) != 0) {
            z = true;
        }
        checkBoxPreference.setChecked(z);
        for (int i = 0; i < this.mSettingNames.length; i++) {
            EditTextPreference preference = (EditTextPreference) findPreference(this.mPreferenceKeys[i]);
            String settingValue = System.getString(contentResolver, this.mSettingNames[i]);
            preference.setText(settingValue);
            preference.setSummary(settingValue);
        }
    }

    private void updateSettingsProvider() {
        ContentResolver contentResolver = getContentResolver();
        System.putInt(contentResolver, "wifi_use_static_ip", this.mUseStaticIpCheckBox.isChecked() ? 1 : 0);
        for (int i = 0; i < this.mSettingNames.length; i++) {
            EditTextPreference preference = (EditTextPreference) findPreference(this.mPreferenceKeys[i]);
            if (!(preference == null || preference.getText() == null)) {
                System.putString(contentResolver, this.mSettingNames[i], preference.getText());
            }
        }
    }

    private void refreshMacAddress() {
        String macAddress = null;
        WifiInfo wifiInfo = ((WifiManager) getSystemService("wifi")).getConnectionInfo();
        Preference wifiMacAddressPref = findPreference("mac_address");
        if (wifiInfo != null) {
            macAddress = wifiInfo.getMacAddress();
        }
        if (TextUtils.isEmpty(macAddress)) {
            macAddress = getString(2131296579);
        }
        wifiMacAddressPref.setSummary(macAddress);
    }
}
