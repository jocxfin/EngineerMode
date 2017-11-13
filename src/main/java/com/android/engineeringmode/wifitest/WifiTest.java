package com.android.engineeringmode.wifitest;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WifiTest extends PreferenceActivity {
    private final String WIFI_MAC0_PATH = "/data/oemnvitems/4678_0";
    private final String WIFI_MAC1_PATH = "/data/oemnvitems/4678_1";
    private Preference wifiMacAddress;
    private CheckBoxPreference wifiSensitivitySwitch;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2130968622);
        this.wifiSensitivitySwitch = (CheckBoxPreference) findPreference("wifisensitivityswitch");
        this.wifiMacAddress = findPreference("wifi_mac_address");
        if (!getPackageManager().hasSystemFeature("oppo.cmcc.optr")) {
            getPreferenceScreen().removePreference(this.wifiSensitivitySwitch);
        } else if (getWifiSensitivityStatus()) {
            Log.i("WifiTest", "wifisensitivityswitch has opened");
            this.wifiSensitivitySwitch.setChecked(true);
        } else {
            Log.i("WifiTest", "wifisensitivityswitch has closed");
            this.wifiSensitivitySwitch.setChecked(false);
        }
        CharSequence macString = null;
        if (new File("/data/oemnvitems/4678_0").exists()) {
            macString = "mac0 : " + readMacFromFile("/data/oemnvitems/4678_0") + "\n";
        }
        if (new File("/data/oemnvitems/4678_1").exists()) {
            macString = macString + "mac1 : " + readMacFromFile("/data/oemnvitems/4678_1");
        }
        if (macString != null) {
            this.wifiMacAddress.setSummary(macString);
        } else {
            this.wifiMacAddress.setSummary(2131297647);
        }
    }

    private String readMacFromFile(String Path) {
        String str = null;
        try {
            FileInputStream stream = new FileInputStream(Path);
            while (stream.read() != -1) {
                if (str == null) {
                    str = String.format("%02x", new Object[]{Integer.valueOf(c)});
                } else {
                    str = String.format("%02x", new Object[]{Integer.valueOf(c)}) + ":" + str;
                }
            }
            stream.close();
            Log.i("WifiTest", Path + ": " + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private boolean getWifiSensitivityStatus() {
        IOException e;
        Throwable th;
        boolean result = false;
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new FileReader("/persist/wifi_sensitivity_switch"));
            try {
                String line = bufferedReader2.readLine();
                if (line != null) {
                    Log.i("WifiTest", line);
                    if ("1".equals(line.trim())) {
                        result = true;
                    }
                }
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e2) {
                        Log.e("WifiTest", e2.getMessage());
                    }
                }
                bufferedReader = bufferedReader2;
            } catch (IOException e3) {
                e2 = e3;
                bufferedReader = bufferedReader2;
                try {
                    Log.e("WifiTest", e2.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e22) {
                            Log.e("WifiTest", e22.getMessage());
                        }
                    }
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e222) {
                            Log.e("WifiTest", e222.getMessage());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = bufferedReader2;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e222 = e4;
            Log.e("WifiTest", e222.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return result;
        }
        return result;
    }

    private void setWifiSensitivityStatus(boolean enable) {
        File ff = new File("/persist/wifi_sensitivity_switch");
        Log.i("WifiTest", "setWifiSensitivityStatus enable : " + enable);
        try {
            ff.createNewFile();
            BufferedWriter output = new BufferedWriter(new FileWriter(ff));
            output.write(enable ? "1" : "0");
            output.close();
        } catch (IOException e) {
            Log.e("WifiTest", e.getMessage());
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference instanceof CheckBoxPreference) {
            CheckBoxPreference chkpref = (CheckBoxPreference) preference;
            if ("wifisensitivityswitch".equals(chkpref.getKey())) {
                Log.i("WifiTest", "wifisensitivityswitch set " + chkpref.isChecked());
                setWifiSensitivityStatus(chkpref.isChecked());
            }
        }
        return false;
    }
}
