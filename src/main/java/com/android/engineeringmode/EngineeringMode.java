package com.android.engineeringmode;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.oem.util.Feature;
import com.oem.util.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EngineeringMode extends PreferenceActivity {
    public static long lBegin = 0;
    private CheckBoxPreference mChkprefRFConfig;
    private int mPreviousSleepTime = -2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2130968601);
        if (Utils.isOnePlus()) {
            setValueSummary("rf_version", "ro.rf_version");
        } else {
            getPreferenceScreen().removePreference(findPreference("rf_version"));
        }
        if (!Feature.isGpioSwitchSupported(this)) {
            Log.i("EngineeringMode", "/proc/antenna_switch not exist remove it ");
            getPreferenceScreen().removePreference(findPreference("key_gpio_switch"));
        }
        if (!Feature.isNfcContinueTransmitSupported(this)) {
            Log.i("EngineeringMode", "/dev/pn544 not exist remove it ");
            getPreferenceScreen().removePreference(findPreference("key_nfc_continue_transmit"));
            getPreferenceScreen().removePreference(findPreference("key_nfc_se_test"));
        }
        this.mChkprefRFConfig = (CheckBoxPreference) findPreference("rf_cable_config");
        if (Feature.isRFCableConfigSupported()) {
            String on = readFileByLines("/proc/rf_cable_config");
            Log.d("EngineeringMode", "/proc/rf_cable_config: " + on);
            if (on == null) {
                this.mChkprefRFConfig.setChecked(false);
                return;
            } else {
                this.mChkprefRFConfig.setChecked(on.trim().equals("1"));
                return;
            }
        }
        removeUnnecessaryPreference("rf_cable_config");
    }

    private String readFileByLines(String fileName) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        String tempString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                tempString = reader.readLine();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("EngineeringMode", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("EngineeringMode", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("EngineeringMode", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    return tempString;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("EngineeringMode", "readFileByLines io close exception :" + e122.getMessage());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = reader;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            Log.e("EngineeringMode", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return tempString;
        }
        return tempString;
    }

    private void removeUnnecessaryPreference(String preference_key) {
        Preference pre_target = findPreference(preference_key);
        if (pre_target != null) {
            getPreferenceScreen().removePreference(pre_target);
        }
    }

    private void stringToFile(String filename, String string) {
        IOException e;
        Throwable th;
        FileWriter fileWriter = null;
        try {
            FileWriter out = new FileWriter(filename);
            try {
                out.write(string);
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        Log.e("EngineeringMode", "stringToFile io close exception :" + e1.getMessage());
                    }
                }
                fileWriter = out;
            } catch (IOException e2) {
                e = e2;
                fileWriter = out;
                try {
                    Log.e("EngineeringMode", "stringToFile io exception:" + e.getMessage());
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (IOException e12) {
                            Log.e("EngineeringMode", "stringToFile io close exception :" + e12.getMessage());
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (IOException e122) {
                            Log.e("EngineeringMode", "stringToFile io close exception :" + e122.getMessage());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fileWriter = out;
                if (fileWriter != null) {
                    fileWriter.close();
                }
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            Log.e("EngineeringMode", "stringToFile io exception:" + e.getMessage());
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if ("enter_fastbootmode".equals(preference.getKey())) {
            new Thread(new Runnable() {
                public void run() {
                    ((PowerManager) EngineeringMode.this.getSystemService("power")).reboot("bootloader");
                }
            }).start();
        }
        if (preference instanceof CheckBoxPreference) {
            CheckBoxPreference chkpref = (CheckBoxPreference) preference;
            if ("rf_cable_config".equals(chkpref.getKey())) {
                if (chkpref.isChecked()) {
                    stringToFile("/proc/rf_cable_config", "1");
                } else {
                    stringToFile("/proc/rf_cable_config", "0");
                }
            }
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    private void setValueSummary(String preference, String property) {
        try {
            findPreference(preference).setSummary(SystemProperties.get(property, getResources().getString(2131296815)));
        } catch (RuntimeException e) {
            Log.d("EngineeringMode", "setValueSummary(), exception happen, preference = " + preference);
            e.printStackTrace();
        }
    }
}
