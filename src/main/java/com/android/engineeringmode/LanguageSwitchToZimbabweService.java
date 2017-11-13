package com.android.engineeringmode;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.Service;
import android.app.backup.BackupManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;

import java.util.Locale;

public class LanguageSwitchToZimbabweService extends Service {
    static Locale mLocale = null;
    private SharedPreferences mSharedPref;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.mSharedPref = getSharedPreferences("com.android.engineeringmode_preferences", 2);
        String language = this.mSharedPref.getString("last_language", null);
        String country = this.mSharedPref.getString("last_conuntry", null);
        if (!(language == null || country == null)) {
            mLocale = new Locale(language, country);
        }
        changeLanguage(2);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean changeLanguage(int lang) {
        try {
            IActivityManager am = ActivityManagerNative.getDefault();
            Configuration config = am.getConfiguration();
            String language = SystemProperties.get("persist.sys.language", "zh");
            String country = SystemProperties.get("persist.sys.country", "CN");
            if (!country.equals("ZA")) {
                Editor editor = this.mSharedPref.edit();
                editor.putString("last_language", language);
                editor.putString("last_conuntry", country);
                editor.commit();
                config.locale = new Locale("en", "ZA");
            } else if (mLocale != null) {
                config.locale = mLocale;
            }
            config.userSetLocale = true;
            am.updateConfiguration(config);
            BackupManager.dataChanged("com.android.providers.settings");
            stopSelf();
            return true;
        } catch (RemoteException e) {
            stopSelf();
            return false;
        }
    }
}
