package com.android.engineeringmode;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class KeepScreenOnPreActivity extends PreferenceActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
    }
}
