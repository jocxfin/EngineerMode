package com.android.engineeringmode;

import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class CpuBasicFrequency extends PreferenceActivity implements OnPreferenceChangeListener {
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
