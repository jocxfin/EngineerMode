package com.android.engineeringmode.qualcomm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

public class LogSwitchExt extends Activity {
    private Phone mPhone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String action = getIntent().getAction();
        Log.i("LogSwitch", "LogSwitchExt=" + action);
        this.mPhone = PhoneFactory.getDefaultPhone();
        if ("com.android.engineeringmode.qualcomm.LogSwitchExtVer".equals(action)) {
            String version = this.mPhone.getModemVersion();
            if (TextUtils.isEmpty(version)) {
                setResult(0);
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("ver", version);
            setResult(-1, intent);
            Log.i("LogSwitch", "LogSwitchExt.version=" + version);
        } else if ("com.android.engineeringmode.qualcomm.LogSwitchExtCrash".equals(action)) {
            Log.i("LogSwitch", "LogSwitchExt.setModemCrash=");
            this.mPhone.setModemCrash(null);
        }
    }

    protected void onResume() {
        super.onResume();
        finish();
    }
}
