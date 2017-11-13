package com.android.engineeringmode.qualcomm;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class QualCommActivity extends PreferenceActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2130968611);
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (!"nv_function".equals(preference.getKey())) {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
        Intent intent = new Intent();
        if ("msm8996".equals(SystemProperties.get("ro.board.platform")) || VERSION.SDK_INT >= 24) {
            intent.setAction("cn.oneplus.nvbackup.NVBackupUI");
        } else {
            intent.setAction("com.android.engineeringmode.qualcomm.QualCommNv2");
        }
        intent.setFlags(268435456);
        startActivity(intent);
        return true;
    }
}
