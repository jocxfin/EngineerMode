package com.android.engineeringmode;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.backup.BackupManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;

import java.util.Calendar;
import java.util.Locale;

public class SwitchSetupWizardActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restartSetupWizard();
    }

    public void restartSetupWizard() {
        getPackageManager().setComponentEnabledSetting(new ComponentName("com.android.provision", "com.android.provision.WelcomePage"), 1, 1);
        changeLanguage(1);
        if (!SystemProperties.get("persist.sys.oem.region", "CN").equals("MX")) {
            SystemProperties.set("persist.sys.oem.region", "TH");
        }
        setDate(2013, 0, 1, 7);
        startActivity(new Intent("com.oppo.setupwizard.EnableSetupWizard"));
    }

    public boolean changeLanguage(int lang) {
        try {
            IActivityManager am = ActivityManagerNative.getDefault();
            Configuration config = am.getConfiguration();
            config.locale = lang == 0 ? Locale.CHINA : Locale.ENGLISH;
            config.userSetLocale = true;
            am.updateConfiguration(config);
            BackupManager.dataChanged("com.android.providers.settings");
            finish();
            return true;
        } catch (RemoteException e) {
            finish();
            return false;
        }
    }

    void setDate(int year, int month, int day, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, year);
        calendar.set(2, month);
        calendar.set(5, day);
        calendar.set(11, hour);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        long when = calendar.getTimeInMillis();
        if (when / 1000 < 2147483647L) {
            SystemClock.setCurrentTimeMillis(when);
        }
    }
}
