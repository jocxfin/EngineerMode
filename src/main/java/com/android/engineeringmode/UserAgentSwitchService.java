package com.android.engineeringmode;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemProperties;
import android.widget.Toast;

public class UserAgentSwitchService extends Service {
    static String useragent = null;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        useragent = "Mozilla/5.0 (Linux; U; Android 4.2.2; es-mx; N1 Build/JDQ39) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
        changeUserAgent();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean changeUserAgent() {
        if (SystemProperties.get("persist.sys.uamodify", "false").equals("true")) {
            SystemProperties.set("persist.sys.uamodify", "false");
            Toast.makeText(this, "Change UserAgent to default", 2000).show();
        } else {
            SystemProperties.set("persist.sys.uamodify", "true");
            Toast.makeText(this, "Change UserAgent to N1", 2000).show();
        }
        stopSelf();
        return true;
    }
}
