package com.android.engineeringmode.bluetoothtest;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class BluetoothTest extends PreferenceActivity implements OnPreferenceClickListener {
    private Preference mBluetoothAudio;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(2131296611);
        addPreferencesFromResource(2130968586);
        this.mBluetoothAudio = findPreference("bluetooth_search_audio");
        this.mBluetoothAudio.setOnPreferenceClickListener(this);
    }

    public boolean onPreferenceClick(Preference preference) {
        if (preference != this.mBluetoothAudio) {
            return false;
        }
        Intent intent = new Intent(this, BluetoothSearch.class);
        intent.putExtra("audio_device", 1024);
        startActivity(intent);
        return true;
    }
}
