package com.android.engineeringmode.manualtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Calendar;

public class RTCTestListPreference extends ListPreference implements OnPreferenceChangeListener {
    private Context mContext;
    private long mRtcTestTime;

    public RTCTestListPreference(Context context) {
        this(context, null);
    }

    public RTCTestListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mRtcTestTime = 15000;
        this.mContext = null;
        this.mContext = context;
        setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Calendar now = Calendar.getInstance();
        String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--RTCTestListPreference--" + "has enter it";
        Log.w("RTCTestListPreference", "onPreferenceChange, newValue = " + newValue);
        setPoweronTime(Long.valueOf((String) newValue).longValue());
        return false;
    }

    private void setPoweronTime(long atTimeInMillis) {
        this.mRtcTestTime = atTimeInMillis;
        Log.v("RTCTestListPreference", "setPoweronTime, atTimeInMillis = " + atTimeInMillis);
        AlarmManager am = (AlarmManager) this.mContext.getSystemService("alarm");
        Intent intent = new Intent("com.android.alarmclock.ALARM_ALERT");
        atTimeInMillis += System.currentTimeMillis();
        long alartTime = atTimeInMillis;
        am.setExact(5, atTimeInMillis, PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.android.engineeringmode.ACTION_WAKEUP_TRIGERED"), 1073741824));
        shutdown();
    }

    private final void shutdown() {
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        intent.setFlags(268435456);
        this.mContext.startActivity(intent);
    }
}
