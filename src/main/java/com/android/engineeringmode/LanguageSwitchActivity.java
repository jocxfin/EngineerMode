package com.android.engineeringmode;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.backup.BackupManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import java.util.Locale;

public class LanguageSwitchActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String langstr = getIntent().getStringExtra("language_str");
        Log.d("duhuan", "langstr=" + langstr);
        changeLanguage(langstr);
    }

    public boolean changeLanguage(String lang) {
        try {
            String[] lanStr = lang.split("_");
            IActivityManager am = ActivityManagerNative.getDefault();
            Configuration config = am.getConfiguration();
            config.locale = new Locale(lanStr[0], lanStr[1]);
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
