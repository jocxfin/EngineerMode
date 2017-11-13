package com.android.engineeringmode.mhl;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.preference.ListPreference;
import android.util.Log;

public class HDMIBroadcastReceiver extends BroadcastReceiver {
    private ChangeResolutionTest mChangeResolutionTest;
    private ListPreference mListPreference;
    private Resources mRes;

    public HDMIBroadcastReceiver(ListPreference listPref, ChangeResolutionTest resolutionTest, Resources res) {
        this.mListPreference = listPref;
        this.mChangeResolutionTest = resolutionTest;
        this.mRes = res;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("HDMIBroadcastReceiver", "onReceive() action:" + action + "HDMI_CONNECTED".equals(action));
        if ("HDMI_CONNECTED".equals(action)) {
            Log.e("HDMIBroadcastReceiver", action);
            this.mListPreference.setEnabled(true);
            this.mListPreference.setSummary(this.mRes.getText(2131297128));
        } else if ("HDMI_DISCONNECTED".equals(action)) {
            Dialog dg = this.mListPreference.getDialog();
            if (dg != null) {
                dg.dismiss();
            }
            this.mListPreference.setEnabled(false);
            this.mListPreference.setSummary(this.mRes.getText(2131297125));
            Log.e("HDMIBroadcastReceiver", "onReceive(): HDMI device is removed");
        }
    }
}
