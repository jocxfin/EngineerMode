package com.android.engineeringmode.qualcomm;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.android.engineeringmode.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

public class QualCommNv2 extends PreferenceActivity {
    IntentFilter mIntentFilter;
    private final BroadcastReceiver mNVCommand = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int diffNum;
            if (intent.getAction().equals("factorymode.nvcmd.dynamicNvAutoCheck")) {
                int isOK;
                Log.d("QualCommNv2", "dynamicNvAutoCheck");
                QualCommNv2.this.dismissDialog();
                isOK = intent.getIntExtra("result", 0);
                Log.d("QualCommNv2", "" + isOK);
                if (isOK == 1) {
                    QualCommNv2.this.mprefDYC_NV_CRC.setSummary(2131296665);
                    Toast.makeText(QualCommNv2.this, "Success", 1).show();
                } else {
                    QualCommNv2.this.mprefDYC_NV_CRC.setSummary(2131296664);
                    Toast.makeText(QualCommNv2.this, "Fail", 1).show();
                }
            }
            if (intent.getAction().equals("factorymode.nvcmd.dynamicNvCheck")) {
                Log.d("QualCommNv2", "dynamicNvCheck");
                QualCommNv2.this.dismissDialog();
                isOK = intent.getIntExtra("reason", 0);
                diffNum = intent.getIntExtra("result", 0);
                Log.d("QualCommNv2", "" + isOK);
                if (isOK == 1) {
                    QualCommNv2.this.mprefDYC_NV_CMP.setSummary(2131296665);
                    Toast.makeText(QualCommNv2.this, "Pass,there are " + diffNum + " different items.", 1).show();
                } else {
                    QualCommNv2.this.mprefDYC_NV_CMP.setSummary(2131296664);
                    Toast.makeText(QualCommNv2.this, "Fail", 1).show();
                }
            }
            if (intent.getAction().equals("factorymode.nvcmd.dynamicNvRestore")) {
                Log.d("QualCommNv2", "dynamicNvRestore");
                QualCommNv2.this.dismissDialog();
                isOK = intent.getIntExtra("result", 0);
                Log.d("QualCommNv2", "" + isOK);
                if (isOK == 1) {
                    QualCommNv2.this.mprefDYC_NV_RES.setSummary(2131296665);
                    Toast.makeText(QualCommNv2.this, "Success", 1).show();
                } else {
                    QualCommNv2.this.mprefDYC_NV_RES.setSummary(2131296664);
                    Toast.makeText(QualCommNv2.this, "Fail", 1).show();
                }
            }
            if (intent.getAction().equals("factorymode.nvcmd.dynamicNvBackup")) {
                Log.d("QualCommNv2", "dynamicNvBackup");
                QualCommNv2.this.dismissDialog();
                isOK = intent.getIntExtra("result", 0);
                Log.d("QualCommNv2", "" + isOK);
                if (isOK == 1) {
                    QualCommNv2.this.mprefDYC_NV_FBK.setSummary(2131296665);
                    Toast.makeText(QualCommNv2.this, "Success", 1).show();
                } else {
                    QualCommNv2.this.mprefDYC_NV_FBK.setSummary(2131296664);
                    Toast.makeText(QualCommNv2.this, "Fail", 1).show();
                }
            }
            if (intent.getAction().equals("factorymode.nvcmd.staticNvAutoCheck")) {
                Log.d("QualCommNv2", "staticNvAutoCheck");
                QualCommNv2.this.dismissDialog();
                isOK = intent.getIntExtra("result", 0);
                Log.d("QualCommNv2", "" + isOK);
                if (isOK == 1) {
                    QualCommNv2.this.mprefSTA_NV_CRC.setSummary(2131296665);
                    Toast.makeText(QualCommNv2.this, "Success", 1).show();
                } else {
                    QualCommNv2.this.mprefSTA_NV_CRC.setSummary(2131296664);
                    Toast.makeText(QualCommNv2.this, "Fail", 1).show();
                }
            }
            if (intent.getAction().equals("factorymode.nvcmd.staticNvCheck")) {
                Log.d("QualCommNv2", "staticNvCheck");
                QualCommNv2.this.dismissDialog();
                isOK = intent.getIntExtra("reason", 0);
                diffNum = intent.getIntExtra("result", 0);
                Log.d("QualCommNv2", "" + isOK);
                if (isOK == 1) {
                    QualCommNv2.this.mprefSTA_NV_CMP.setSummary(2131296665);
                    Toast.makeText(QualCommNv2.this, "Pass,there are " + diffNum + " different items.", 1).show();
                } else {
                    QualCommNv2.this.mprefSTA_NV_CMP.setSummary(2131296664);
                    Toast.makeText(QualCommNv2.this, "Fail", 1).show();
                }
            }
            if (intent.getAction().equals("factorymode.nvcmd.staticNvRestore")) {
                Log.d("QualCommNv2", "staticNvRestore");
                QualCommNv2.this.dismissDialog();
                isOK = intent.getIntExtra("result", 0);
                Log.d("QualCommNv2", "" + isOK);
                if (isOK == 1) {
                    QualCommNv2.this.mprefSTA_NV_RES.setSummary(2131296665);
                    Toast.makeText(QualCommNv2.this, "Success", 1).show();
                } else {
                    QualCommNv2.this.mprefSTA_NV_RES.setSummary(2131296664);
                    Toast.makeText(QualCommNv2.this, "Fail", 1).show();
                }
            }
            if (intent.getAction().equals("factorymode.nvcmd.staticNvBackup")) {
                Log.d("QualCommNv2", "staticNvBackup");
                QualCommNv2.this.dismissDialog();
                isOK = intent.getIntExtra("result", 0);
                Log.d("QualCommNv2", "" + isOK);
                if (isOK == 1) {
                    QualCommNv2.this.mprefSTA_NV_FBK.setSummary(2131296665);
                    Toast.makeText(QualCommNv2.this, "Success", 1).show();
                } else {
                    QualCommNv2.this.mprefSTA_NV_FBK.setSummary(2131296664);
                    Toast.makeText(QualCommNv2.this, "Fail", 1).show();
                }
            }
            if (intent.getAction().equals("factorymode.nvcmd.lteNvChange")) {
                Log.d("QualCommNv2", "lteNvChange");
                QualCommNv2.this.dismissDialog();
                isOK = intent.getIntExtra("result", 0);
                Log.d("QualCommNv2", "" + isOK);
                if (isOK == 1) {
                    Toast.makeText(QualCommNv2.this, "Success", 1).show();
                } else {
                    Toast.makeText(QualCommNv2.this, "Fail", 1).show();
                }
            }
        }
    };
    private CheckBoxPreference mNvAdjustAccess;
    private Preference mNvCount;
    private Preference mNvFtm;
    private CheckBoxPreference mNvStaticAccess;
    private Preference mNvStaticCount;
    private Preference mNvStk;
    private Phone mPhone = null;
    private ProgressDialog mProgressDialog;
    String[] mTips;
    private Preference mprefDYC_NV_BAK;
    private Preference mprefDYC_NV_CMP;
    private Preference mprefDYC_NV_CRC;
    private Preference mprefDYC_NV_FBK;
    private Preference mprefDYC_NV_RES;
    private Preference mprefNV_DUMP;
    private Preference mprefSTA_NV_BAK;
    private Preference mprefSTA_NV_CMP;
    private Preference mprefSTA_NV_CRC;
    private Preference mprefSTA_NV_FBK;
    private Preference mprefSTA_NV_RES;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPhone = PhoneFactory.getDefaultPhone();
        addPreferencesFromResource(2130968612);
        this.mprefDYC_NV_CRC = findPreference("dynamic_nv_crc");
        this.mprefDYC_NV_CMP = findPreference("dynamic_nv_crc_compare");
        this.mprefDYC_NV_BAK = findPreference("dynamic_nv_backup");
        this.mprefDYC_NV_FBK = findPreference("dynamic_nv_force_backup");
        this.mprefDYC_NV_RES = findPreference("dynamic_nv_restore");
        this.mprefSTA_NV_CRC = findPreference("static_nv_crc");
        this.mprefSTA_NV_CMP = findPreference("static_nv_crc_compare");
        this.mprefSTA_NV_BAK = findPreference("static_nv_backup");
        this.mprefSTA_NV_FBK = findPreference("static_nv_force_backup");
        this.mprefSTA_NV_RES = findPreference("static_nv_restore");
        this.mNvAdjustAccess = (CheckBoxPreference) findPreference("nv_adjust_access");
        this.mNvStaticAccess = (CheckBoxPreference) findPreference("nv_static_access");
        this.mNvCount = findPreference("nv_adjust_count");
        this.mNvStaticCount = findPreference("nv_static_count");
        this.mNvFtm = findPreference("nv_ftm");
        this.mNvStk = findPreference("nv_stk");
        this.mprefNV_DUMP = findPreference("nv_dump");
        PreferenceScreen prefSet = getPreferenceScreen();
        prefSet.removePreference(this.mNvAdjustAccess);
        prefSet.removePreference(this.mNvStaticAccess);
        prefSet.removePreference(this.mNvCount);
        prefSet.removePreference(this.mNvStaticCount);
        prefSet.removePreference(this.mprefSTA_NV_BAK);
        prefSet.removePreference(this.mprefDYC_NV_BAK);
        prefSet.removePreference(this.mprefNV_DUMP);
        prefSet.removePreference(this.mNvFtm);
        prefSet.removePreference(this.mNvStk);
        Log.d("QualCommNv2", "mTelephonyManager");
        this.mIntentFilter = new IntentFilter();
        this.mIntentFilter.addAction("factorymode.nvcmd.dynamicNvAutoCheck");
        this.mIntentFilter.addAction("factorymode.nvcmd.dynamicNvCheck");
        this.mIntentFilter.addAction("factorymode.nvcmd.dynamicNvRestore");
        this.mIntentFilter.addAction("factorymode.nvcmd.dynamicNvBackup");
        this.mIntentFilter.addAction("factorymode.nvcmd.staticNvAutoCheck");
        this.mIntentFilter.addAction("factorymode.nvcmd.staticNvCheck");
        this.mIntentFilter.addAction("factorymode.nvcmd.staticNvRestore");
        this.mIntentFilter.addAction("factorymode.nvcmd.staticNvBackup");
        this.mIntentFilter.addAction("factorymode.nvcmd.lteNvChange");
        Log.d("QualCommNv2", "mTelephonyManager");
        this.mTips = getResources().getStringArray(2131099684);
    }

    protected void onResume() {
        registerReceiver(this.mNVCommand, this.mIntentFilter);
        super.onResume();
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        if (key.equals("dynamic_nv_crc")) {
            showDialog(this.mTips[0]);
            Log.d("QualCommNv2", "onPreferenceTreeClick----1");
        } else if (key.equals("dynamic_nv_crc_compare")) {
            showDialog(this.mTips[13]);
            Log.d("QualCommNv2", "onPreferenceTreeClick----2");
        } else if (key.equals("dynamic_nv_restore")) {
            showDialog(this.mTips[6]);
            Log.d("QualCommNv2", "onPreferenceTreeClick----3");
        } else if (key.equals("dynamic_nv_force_backup")) {
            showDialog(this.mTips[4]);
            Log.d("QualCommNv2", "onPreferenceTreeClick----4");
        } else if (key.equals("static_nv_crc")) {
            showDialog(this.mTips[1]);
            Log.d("QualCommNv2", "onPreferenceTreeClick----5");
        } else if (key.equals("static_nv_crc_compare")) {
            showDialog(this.mTips[14]);
            Log.d("QualCommNv2", "onPreferenceTreeClick----6");
        } else if (key.equals("static_nv_restore")) {
            showDialog(this.mTips[7]);
            Log.d("QualCommNv2", "onPreferenceTreeClick----7");
        } else if (key.equals("static_nv_force_backup")) {
            showDialog(this.mTips[5]);
            Log.d("QualCommNv2", "onPreferenceTreeClick----8");
        }
        return true;
    }

    protected void onDestroy() {
        this.mIntentFilter = null;
        super.onDestroy();
    }

    protected void onPause() {
        unregisterReceiver(this.mNVCommand);
        super.onPause();
    }

    private void showDialog(String message) {
        this.mProgressDialog = null;
        this.mProgressDialog = new ProgressDialog(this);
        this.mProgressDialog.setProgressStyle(0);
        this.mProgressDialog.setMessage(message);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
        this.mProgressDialog.show();
    }

    private void dismissDialog() {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
        }
    }
}
