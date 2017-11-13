package com.android.engineeringmode;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DeviceMgr extends PreferenceActivity implements OnPreferenceChangeListener {
    private ListPreference mListPreferSmsAutoReg;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2130903085);
        this.mListPreferSmsAutoReg = (ListPreference) findPreference("sms_auto_reg");
        this.mListPreferSmsAutoReg.setOnPreferenceChangeListener(this);
        int savedCTA = getSavedCTA();
        this.mListPreferSmsAutoReg.setSummary(savedCTA == 1 ? "Enabled" : "Disabled");
        this.mListPreferSmsAutoReg.setValue(String.valueOf(savedCTA));
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if ("sms_auto_reg".equals(preference.getKey())) {
            try {
                String objVauleString = (String) objValue;
                writeCTA(objVauleString);
                Log.i("EM/devmgr", "write cta = [" + objVauleString + "]");
                this.mListPreferSmsAutoReg.setValue(objVauleString.equals("1") ? "1" : "0");
                this.mListPreferSmsAutoReg.setSummary(objVauleString.equals("1") ? "Enabled" : "Disabled");
            } catch (NumberFormatException e) {
                Log.e("EM/devmgr", "set exception.", e);
            }
        }
        return false;
    }

    private int getSavedCTA() {
        int savedCTA = 1;
        String ctaString = readCTA();
        if (!(ctaString == null || ctaString.length() == 0)) {
            Log.i("EM/devmgr", "Get savedCTA = [" + ctaString + "]");
            try {
                savedCTA = Integer.parseInt(ctaString);
            } catch (NumberFormatException e) {
                Log.e("EM/devmgr", "number format exception. ", e);
            }
        }
        return savedCTA;
    }

    public void writeCTA(String cta) {
        File ff = new File("/data/cta.txt");
        try {
            if (cta.equals("0")) {
                File temp = new File("/data/imsi.txt");
                if (temp.exists()) {
                    temp.delete();
                }
            }
            ff.createNewFile();
            BufferedWriter output = new BufferedWriter(new FileWriter(ff));
            output.write(cta);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readCTA() {
        IOException e;
        Throwable th;
        String ctaString = "";
        File ff = new File("/data/cta.txt");
        if (ff.exists()) {
            BufferedReader reader = null;
            try {
                BufferedReader reader2 = new BufferedReader(new FileReader(ff));
                int line = 1;
                while (true) {
                    try {
                        String tempString = reader2.readLine();
                        if (tempString == null) {
                            break;
                        }
                        ctaString = ctaString + tempString;
                        line++;
                    } catch (IOException e2) {
                        e = e2;
                        reader = reader2;
                    } catch (Throwable th2) {
                        th = th2;
                        reader = reader2;
                    }
                }
                reader2.close();
                if (reader2 != null) {
                    try {
                        reader2.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (IOException e4) {
                e = e4;
                try {
                    e.printStackTrace();
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e5) {
                        }
                    }
                    return ctaString;
                } catch (Throwable th3) {
                    th = th3;
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e6) {
                        }
                    }
                    throw th;
                }
            }
        }
        return ctaString;
    }
}
