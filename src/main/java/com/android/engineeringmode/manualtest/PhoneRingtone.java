package com.android.engineeringmode.manualtest;

import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.RingtonePreference;
import android.util.AttributeSet;

import com.android.engineeringmode.Log;

public class PhoneRingtone extends RingtonePreference {
    public PhoneRingtone(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onPrepareRingtonePickerIntent(Intent ringtonePickerIntent) {
        super.onPrepareRingtonePickerIntent(ringtonePickerIntent);
        ringtonePickerIntent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", false);
        ringtonePickerIntent.putExtra("android.intent.extra.ringtone.SHOW_SILENT", false);
    }

    protected void onSaveRingtone(Uri ringtoneUri) {
        Log.d("PhoneRingtone", "onSaveRingtone(), ringtoneUri = " + ringtoneUri);
    }

    protected Uri onRestoreRingtone() {
        return RingtoneManager.getActualDefaultRingtoneUri(getContext(), getRingtoneType());
    }
}
