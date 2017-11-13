package com.android.engineeringmode;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.backup.BackupManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.RemoteException;

import java.util.Locale;

public class LanguageSwitchToEnglishActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLanguage(1);
    }

    public boolean changeLanguage(int lang) {
        try {
            IActivityManager am = ActivityManagerNative.getDefault();
            Configuration config = am.getConfiguration();
            config.locale = lang == 0 ? Locale.CHINA : Locale.ENGLISH;
            config.setLocale(config.locale);
            config.userSetLocale = true;
            am.updatePersistentConfiguration(config);
            BackupManager.dataChanged("com.android.providers.settings");
            finish();
            return true;
        } catch (RemoteException e) {
            finish();
            return false;
        }
    }
}
