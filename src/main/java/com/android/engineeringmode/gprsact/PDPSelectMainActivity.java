package com.android.engineeringmode.gprsact;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.android.engineeringmode.AllTest;
import com.android.engineeringmode.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

public class PDPSelectMainActivity extends PreferenceActivity implements OnPreferenceClickListener {
    private Phone mPhone = null;
    private Preference mPrefSendData = null;
    private int mUplinkDataLen = -1;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(2130968608);
        this.mPrefSendData = findPreference("key_gprs_act_send_data");
        this.mPrefSendData.setOnPreferenceClickListener(this);
        this.mPhone = PhoneFactory.getDefaultPhone();
        if (this.mPhone == null) {
            finish();
        }
    }

    public boolean onPreferenceClick(Preference preference) {
        if (preference == this.mPrefSendData) {
            showUplinkDataDialog();
        }
        return false;
    }

    private void showUplinkDataDialog() {
        Log.i("PDPSelectMainActivity", "in showUplinkDataDialog()");
        View textEntryView = LayoutInflater.from(this).inflate(2130903099, null);
        final EditText etUplinkDataLen = (EditText) textEntryView.findViewById(2131493104);
        new Builder(this).setTitle(2131296297).setView(textEntryView).setPositiveButton(17039370, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String strDataLen = etUplinkDataLen.getText().toString();
                if (strDataLen == null) {
                    Log.w("PDPSelectMainActivity", "strDataLen = null");
                    return;
                }
                PDPSelectMainActivity.this.mUplinkDataLen = Integer.valueOf(strDataLen).intValue();
                if (PDPSelectMainActivity.this.mUplinkDataLen >= 1024) {
                    AllTest.showShortMessage(PDPSelectMainActivity.this, 2131296298);
                    return;
                }
                if (PDPSelectMainActivity.this.mUplinkDataLen <= 0) {
                    AllTest.showShortMessage(PDPSelectMainActivity.this, 2131296298);
                    Log.w("PDPSelectMainActivity", "Data len is small than 0");
                }
            }
        }).setNegativeButton(17039360, null).create().show();
    }
}
