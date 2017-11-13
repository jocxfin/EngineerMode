package com.android.engineeringmode.wifitest.wifiautoconnect;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.android.engineeringmode.Log;
import com.android.engineeringmode.wifitest.WifiEnabler;

public class WifiAutoConnectActivity extends PreferenceActivity implements Callback {
    private CheckBoxPreference mWifiEnabled;
    private WifiEnabler mWifiEnabler;
    private WifiAutoConnectLayer mWifiLayer;
    private boolean mbIsBackPressed;

    public WifiAutoConnectActivity() {
        this.mWifiEnabled = null;
        this.mbIsBackPressed = false;
        this.mWifiLayer = null;
        this.mWifiEnabler = null;
        this.mWifiLayer = new WifiAutoConnectLayer(this, this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreatePreferences();
        this.mWifiLayer.onCreate();
    }

    private void onCreatePreferences() {
        addPreferencesFromResource(2130968620);
        this.mWifiEnabled = (CheckBoxPreference) findPreference("wifi_enabled");
        this.mWifiEnabler = new WifiEnabler(this, this.mWifiEnabled);
    }

    protected void onResume() {
        super.onResume();
        this.mWifiLayer.onResume();
        this.mWifiEnabler.resume();
    }

    protected void onPause() {
        super.onPause();
        this.mWifiLayer.onPause();
        this.mWifiEnabler.pause();
    }

    public void onBackPressed() {
        this.mbIsBackPressed = true;
        if (this.mWifiLayer.isWifiEnabled()) {
            if (!this.mWifiLayer.setWifiEnable(false)) {
                finish();
            }
            return;
        }
        super.onBackPressed();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mWifiLayer != null) {
            this.mWifiLayer.onDestory();
        } else {
            Log.i("WifiSettings", "mWifiLayer = null in onDestory()");
        }
    }

    public void onCloseBtError(int state) {
        this.mWifiEnabled.setSummary(2131296625);
    }

    public void onSummaryChanged(String summary) {
        Log.d("WifiSettings", "onSummaryChanged(), summary = " + summary);
        this.mWifiEnabled.setSummary(summary);
    }

    public void onWifiColsed() {
        if (this.mbIsBackPressed) {
            finish();
        } else {
            Log.d("WifiSettings", "onWifiColsed(), wifi is closed");
        }
    }

    public void onScanFailed() {
        this.mWifiEnabled.setSummary(getString(2131296429));
        Toast.makeText(this, 2131296429, 1).show();
    }
}
