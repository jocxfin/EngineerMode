package com.android.engineeringmode.manualtest;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class FingerPrintTest extends PreferenceActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2130968593);
        if (!isIntentAvailable(new Intent("com.fingerprints.fingerprintsensortest.ModuleQualityTest"))) {
            removeUnnecessaryPreference("module_quality_test");
        }
    }

    private boolean isIntentAvailable(Intent intent) {
        if (getPackageManager().queryIntentActivities(intent, 1).size() > 0) {
            return true;
        }
        return false;
    }

    private void removeUnnecessaryPreference(String preference_key) {
        Preference pre_target = findPreference(preference_key);
        if (pre_target != null) {
            getPreferenceScreen().removePreference(pre_target);
        }
    }
}
