package com.android.engineeringmode.mhl;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;

public class MHLActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    private static final String[] RESOLUTION_LIST = new String[]{"auto", "1080p", "720p", "480p or 576p"};
    private HDMIBroadcastReceiver mBroadcastReceiver;
    private ChangeResolutionTest mChangeResolutionTest;
    private IntentFilter mFilter;
    private CheckBoxPreference mHDCPPreference;
    private boolean mIsConnected;
    private ListPreference mListPreference;
    private int mOldValue;
    private boolean mPlayTest = true;
    private SharedPreferences mSharedPref;

    private int getResolution(SharedPreferences sharePref) {
        return Integer.parseInt(this.mSharedPref.getString("resolution_list", "0").trim());
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2130968605);
        this.mSharedPref = getSharedPreferences("com.android.engineeringmode_preferences", 2);
        this.mListPreference = (ListPreference) findPreference("resolution_list");
        this.mHDCPPreference = (CheckBoxPreference) findPreference("hdcp_switcher");
        this.mChangeResolutionTest = new ChangeResolutionTest();
        this.mBroadcastReceiver = new HDMIBroadcastReceiver(this.mListPreference, this.mChangeResolutionTest, getResources());
        this.mFilter = new IntentFilter();
        this.mFilter.setPriority(999);
        this.mFilter.addAction("HDMI_CONNECTED");
        this.mFilter.addAction("HDMI_DISCONNECTED");
        this.mFilter.addCategory("android.intent.category.DEFAULT");
    }

    protected void onResume() {
        super.onResume();
        this.mHDCPPreference.setChecked(this.mSharedPref.getBoolean("hdcp_switcher", true));
        this.mListPreference.setValue(String.valueOf(getResolution(this.mSharedPref)));
        registerReceiver(this.mBroadcastReceiver, this.mFilter);
        this.mIsConnected = false;
        if (this.mIsConnected) {
            this.mListPreference.setEnabled(true);
            this.mListPreference.setSummary(getText(2131297129) + getResolutionString(getResolution(this.mSharedPref)));
        } else {
            Toast.makeText(this, getText(2131297125), 0);
            this.mListPreference.setEnabled(false);
            this.mListPreference.setSummary(2131297125);
        }
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.mBroadcastReceiver);
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ("resolution_list".equals(key)) {
            int resolution = getResolution(this.mSharedPref);
            this.mPlayTest = false;
            this.mSharedPref.edit().putString("resolution_list", this.mOldValue + "").commit();
            this.mListPreference.setValue(getResolution(this.mSharedPref) + "");
            Toast.makeText(this, getText(2131297126), 0).show();
            this.mPlayTest = true;
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == this.mHDCPPreference) {
            int result;
            if (this.mHDCPPreference.isChecked()) {
                result = 0;
            } else {
                result = -1;
            }
            if (result == 0) {
                Toast.makeText(this, "Set HDCP successfully!", 0).show();
            } else {
                Toast.makeText(this, "Set HDCP unsuccessfully!", 0).show();
            }
        }
        return false;
    }

    private String getResolutionString(int key) {
        return RESOLUTION_LIST[key];
    }
}
