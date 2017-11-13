package com.android.engineeringmode.qualcomm;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class QualCommNv extends PreferenceActivity {
    private String mFailed;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                switch (msg.arg1) {
                    case 0:
                        if (msg.arg2 == 0) {
                            QualCommNv.this.mprefDYC_NV_CRC.setSummary(QualCommNv.this.mSuccess);
                            return;
                        } else {
                            QualCommNv.this.mprefDYC_NV_CRC.setSummary(QualCommNv.this.mFailed);
                            return;
                        }
                    case 10:
                        if (msg.arg2 == 0) {
                            QualCommNv.this.mprefSTA_NV_CRC.setSummary(QualCommNv.this.mSuccess);
                            return;
                        } else {
                            QualCommNv.this.mprefSTA_NV_CRC.setSummary(QualCommNv.this.mFailed);
                            return;
                        }
                    case 11:
                        if (msg.arg2 == 0) {
                            QualCommNv.this.mprefDYC_NV_BAK.setSummary(QualCommNv.this.mSuccess);
                            return;
                        } else {
                            QualCommNv.this.mprefDYC_NV_BAK.setSummary(QualCommNv.this.mFailed);
                            return;
                        }
                    case 12:
                        if (msg.arg2 == 0) {
                            QualCommNv.this.mprefSTA_NV_BAK.setSummary(QualCommNv.this.mSuccess);
                            return;
                        } else {
                            QualCommNv.this.mprefSTA_NV_BAK.setSummary(QualCommNv.this.mFailed);
                            return;
                        }
                    case 13:
                        if (msg.arg2 == 0) {
                            QualCommNv.this.mprefDYC_NV_FBK.setSummary(QualCommNv.this.mSuccess);
                            return;
                        } else {
                            QualCommNv.this.mprefDYC_NV_FBK.setSummary(QualCommNv.this.mFailed);
                            return;
                        }
                    case 14:
                        if (msg.arg2 == 0) {
                            QualCommNv.this.mprefSTA_NV_FBK.setSummary(QualCommNv.this.mSuccess);
                            return;
                        } else {
                            QualCommNv.this.mprefSTA_NV_FBK.setSummary(QualCommNv.this.mFailed);
                            return;
                        }
                    case 15:
                        if (msg.arg2 == 0) {
                            QualCommNv.this.mprefDYC_NV_RES.setSummary(QualCommNv.this.mSuccess);
                            if (QualCommNv.this.mIsVisible) {
                                QualCommNv.this.showAlertDialog(QualCommNv.this.getString(2131297086), "handset_reboot");
                                return;
                            }
                            return;
                        }
                        QualCommNv.this.mprefDYC_NV_RES.setSummary(QualCommNv.this.mFailed);
                        return;
                    case 16:
                        if (msg.arg2 == 0) {
                            QualCommNv.this.mprefSTA_NV_RES.setSummary(QualCommNv.this.mSuccess);
                            if (QualCommNv.this.mIsVisible) {
                                QualCommNv.this.showAlertDialog(QualCommNv.this.getString(2131297086), "handset_reboot");
                                return;
                            }
                            return;
                        }
                        QualCommNv.this.mprefSTA_NV_RES.setSummary(QualCommNv.this.mFailed);
                        return;
                    case 17:
                        if (msg.arg2 == 0) {
                            QualCommNv.this.mprefDYC_NV_CMP.setSummary(QualCommNv.this.mSuccess);
                            if (QualCommNv.this.mIsVisible) {
                                QualCommNv.this.showDetailDialog("Dynamic NV cmp");
                                return;
                            }
                            return;
                        }
                        QualCommNv.this.mprefDYC_NV_CMP.setSummary(QualCommNv.this.mFailed);
                        return;
                    case 18:
                        if (msg.arg2 == 0) {
                            QualCommNv.this.mprefSTA_NV_CMP.setSummary(QualCommNv.this.mSuccess);
                            if (QualCommNv.this.mIsVisible) {
                                QualCommNv.this.showDetailDialog("Static NV cmp");
                                return;
                            }
                            return;
                        }
                        QualCommNv.this.mprefSTA_NV_CMP.setSummary(QualCommNv.this.mFailed);
                        return;
                    case 19:
                        if (msg.arg2 == 0) {
                            QualCommNv.this.mprefNV_DUMP.setSummary(QualCommNv.this.mSuccess);
                            if (QualCommNv.this.mIsVisible) {
                                QualCommNv.this.showAlertDialog("dump file: /data/oppo_dump_nv.bin", "None");
                                return;
                            }
                            return;
                        }
                        QualCommNv.this.mprefNV_DUMP.setSummary(QualCommNv.this.mFailed);
                        return;
                    default:
                        return;
                }
            } else if (msg.what == 2) {
                QualCommNv.this.updateStatus();
            }
        }
    };
    private volatile boolean mIsVisible;
    private CheckBoxPreference mNvAdjustAccess;
    private Preference mNvCount;
    private Preference mNvFtm;
    private CheckBoxPreference mNvStaticAccess;
    private Preference mNvStaticCount;
    private Preference mNvStk;
    private QualCommNvUtils mNvUtils;
    private ProgressDialog mProgressDialog;
    private String mSuccess;
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

    private class RestoreListener implements OnClickListener {
        private String mNvType;

        public RestoreListener(String type) {
            this.mNvType = new String(type);
        }

        public void onClick(DialogInterface dialog, int which) {
            if ("dynamic_nv_force_backup".equals(this.mNvType)) {
                QualCommNv.this.showDialog(QualCommNv.this.mTips[4]);
                QualCommNv.this.F_backupDynamicNv();
            } else if ("static_nv_force_backup".equals(this.mNvType)) {
                QualCommNv.this.showDialog(QualCommNv.this.mTips[5]);
                QualCommNv.this.F_backupStaticNv();
            } else if ("dynamic_nv_restore".equals(this.mNvType)) {
                QualCommNv.this.showDialog(QualCommNv.this.mTips[6]);
                QualCommNv.this.restoreDynamicNv();
            } else if ("static_nv_restore".equals(this.mNvType)) {
                QualCommNv.this.showDialog(QualCommNv.this.mTips[7]);
                QualCommNv.this.restoreStaticNv();
            } else if ("handset_reboot".equals(this.mNvType)) {
                new Thread(new Runnable() {
                    public void run() {
                        Log.i("QualCommNv", "reboot device...");
                        ((PowerManager) QualCommNv.this.getSystemService("power")).reboot(null);
                    }
                }).start();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2130968612);
        this.mNvAdjustAccess = (CheckBoxPreference) findPreference("nv_adjust_access");
        this.mNvStaticAccess = (CheckBoxPreference) findPreference("nv_static_access");
        this.mNvCount = findPreference("nv_adjust_count");
        this.mNvStaticCount = findPreference("nv_static_count");
        this.mNvFtm = findPreference("nv_ftm");
        this.mNvStk = findPreference("nv_stk");
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
        this.mprefNV_DUMP = findPreference("nv_dump");
        this.mNvUtils = new QualCommNvUtils();
        this.mTips = getResources().getStringArray(2131099684);
        this.mSuccess = getResources().getString(2131296665);
        this.mFailed = getResources().getString(2131296664);
        PreferenceScreen prefSet = getPreferenceScreen();
        prefSet.removePreference(this.mNvAdjustAccess);
        prefSet.removePreference(this.mNvStaticAccess);
        prefSet.removePreference(this.mNvCount);
        prefSet.removePreference(this.mNvStaticCount);
        prefSet.removePreference(this.mprefSTA_NV_BAK);
        prefSet.removePreference(this.mprefDYC_NV_BAK);
    }

    protected void onResume() {
        super.onResume();
        this.mIsVisible = true;
        checkStkValid();
    }

    protected void onStop() {
        super.onStop();
        this.mIsVisible = false;
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();
        if (key.equals("dynamic_nv_crc")) {
            showDialog(this.mTips[0]);
            CRCcheckDynamicNV();
        } else if (key.equals("static_nv_crc")) {
            showDialog(this.mTips[1]);
            CRCcheckStaticNV();
        } else if (key.equals("dynamic_nv_backup")) {
            showDialog(this.mTips[2]);
            backupDynamicNv();
        } else if (key.equals("static_nv_backup")) {
            showDialog(this.mTips[3]);
            backupStaticNv();
        } else if (key.equals("dynamic_nv_force_backup")) {
            showAlertDialog(getString(2131297065), "dynamic_nv_force_backup");
        } else if (key.equals("static_nv_force_backup")) {
            showAlertDialog(getString(2131297077), "static_nv_force_backup");
        } else if (key.equals("dynamic_nv_restore")) {
            showAlertDialog(getString(2131297061), "dynamic_nv_restore");
        } else if (key.equals("static_nv_restore")) {
            showAlertDialog(getString(2131297073), "static_nv_restore");
        } else if (key.equals("dynamic_nv_crc_compare")) {
            showDialog(this.mTips[13]);
            CmpDynamicNV();
        } else if (key.equals("static_nv_crc_compare")) {
            showDialog(this.mTips[14]);
            CmpStaticNV();
        } else if (key.equals("nv_dump")) {
            showDialog(this.mTips[15]);
            NVDump();
        }
        return true;
    }

    private void CRCcheckDynamicNV() {
        new Thread(null, new Runnable() {
            public void run() {
                QualCommNv.this.mHandler.obtainMessage(0, 0, QualCommNv.this.mNvUtils.CRCcheckDynamicNV()).sendToTarget();
                QualCommNv.this.dismissDialog();
            }
        }).start();
    }

    private void CRCcheckStaticNV() {
        new Thread(null, new Runnable() {
            public void run() {
                QualCommNv.this.mHandler.obtainMessage(0, 10, QualCommNv.this.mNvUtils.CRCcheckStaticNV()).sendToTarget();
                QualCommNv.this.dismissDialog();
            }
        }).start();
    }

    private void CmpDynamicNV() {
        new Thread(null, new Runnable() {
            public void run() {
                QualCommNv.this.mHandler.obtainMessage(0, 17, QualCommNv.this.mNvUtils.CmpDynamicNV()).sendToTarget();
                QualCommNv.this.dismissDialog();
            }
        }).start();
    }

    private void CmpStaticNV() {
        new Thread(null, new Runnable() {
            public void run() {
                QualCommNv.this.mHandler.obtainMessage(0, 18, QualCommNv.this.mNvUtils.CmpStaticNV()).sendToTarget();
                QualCommNv.this.dismissDialog();
            }
        }).start();
    }

    private void NVDump() {
        new Thread(null, new Runnable() {
            public void run() {
                QualCommNv.this.mHandler.obtainMessage(0, 19, QualCommNv.this.mNvUtils.NVDump()).sendToTarget();
                QualCommNv.this.dismissDialog();
            }
        }).start();
    }

    private void backupDynamicNv() {
        new Thread(null, new Runnable() {
            public void run() {
                QualCommNv.this.mHandler.obtainMessage(0, 11, QualCommNv.this.mNvUtils.backupDynamicNv(0)).sendToTarget();
                QualCommNv.this.dismissDialog();
            }
        }).start();
    }

    private void backupStaticNv() {
        new Thread(null, new Runnable() {
            public void run() {
                QualCommNv.this.mHandler.obtainMessage(0, 12, QualCommNv.this.mNvUtils.backupStaticNv(0)).sendToTarget();
                QualCommNv.this.dismissDialog();
            }
        }).start();
    }

    private void F_backupDynamicNv() {
        new Thread(null, new Runnable() {
            public void run() {
                QualCommNv.this.mHandler.obtainMessage(0, 13, QualCommNv.this.mNvUtils.backupDynamicNv(1)).sendToTarget();
                QualCommNv.this.dismissDialog();
            }
        }).start();
    }

    private void F_backupStaticNv() {
        new Thread(null, new Runnable() {
            public void run() {
                QualCommNv.this.mHandler.obtainMessage(0, 14, QualCommNv.this.mNvUtils.backupStaticNv(1)).sendToTarget();
                QualCommNv.this.dismissDialog();
            }
        }).start();
    }

    private void restoreDynamicNv() {
        new Thread(null, new Runnable() {
            public void run() {
                QualCommNv.this.mHandler.obtainMessage(0, 15, QualCommNv.this.mNvUtils.restoreDynamicNv()).sendToTarget();
                QualCommNv.this.dismissDialog();
            }
        }).start();
    }

    private void restoreStaticNv() {
        new Thread(null, new Runnable() {
            public void run() {
                QualCommNv.this.mHandler.obtainMessage(0, 16, QualCommNv.this.mNvUtils.restoreStaticNv()).sendToTarget();
                QualCommNv.this.dismissDialog();
            }
        }).start();
    }

    private void checkStkValid() {
        if (false) {
            this.mNvStk.setSummary(2131297094);
        } else {
            this.mNvStk.setSummary(2131297095);
        }
    }

    private void updateStatus() {
        boolean z = true;
        int[] status = this.mNvUtils.getNvStatus();
        if (status != null && status.length == 4) {
            boolean z2;
            CheckBoxPreference checkBoxPreference = this.mNvAdjustAccess;
            if (status[0] != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            checkBoxPreference.setChecked(z2);
            CheckBoxPreference checkBoxPreference2 = this.mNvStaticAccess;
            if (status[1] == 0) {
                z = false;
            }
            checkBoxPreference2.setChecked(z);
            this.mNvCount.setSummary("" + status[2]);
            this.mNvStaticCount.setSummary("" + status[3]);
        }
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

    private void showAlertDialog(String message, String type) {
        new Builder(this).setTitle("Nv").setMessage(message).setPositiveButton(17039370, new RestoreListener(type)).setNegativeButton(17039360, null).show();
    }

    private void showDetailDialog(String title) {
        FileNotFoundException e;
        IOException e2;
        StringBuilder stringBuilder;
        Throwable th;
        InputStreamReader inputStreamReader = null;
        try {
            StringBuilder data = new StringBuilder(2048);
            try {
                char[] tmp = new char[2048];
                InputStreamReader inputReader = new FileReader("/persist/eng_nv_check_data.dat");
                while (true) {
                    try {
                        int numRead = inputReader.read(tmp);
                        if (numRead < 0) {
                            break;
                        }
                        data.append(tmp, 0, numRead);
                    } catch (FileNotFoundException e3) {
                        e = e3;
                        inputStreamReader = inputReader;
                    } catch (IOException e4) {
                        e2 = e4;
                        stringBuilder = data;
                        inputStreamReader = inputReader;
                    } catch (Throwable th2) {
                        th = th2;
                        inputStreamReader = inputReader;
                    }
                }
                if (inputReader != null) {
                    try {
                        inputReader.close();
                    } catch (IOException e5) {
                    }
                }
                ScrollView s_view = new ScrollView(getApplicationContext());
                TextView t_view = new TextView(getApplicationContext());
                t_view.setText(data.toString());
                t_view.setTextSize(14.0f);
                s_view.addView(t_view);
                new Builder(this).setView(s_view).setTitle(title).setPositiveButton(17039370, null).show();
            } catch (FileNotFoundException e6) {
                e = e6;
                try {
                    Log.e("QualCommNv", "Nv detail file not found at /persist/eng_nv_check_data.dat", e);
                    showErrorAndFinish();
                    if (inputStreamReader != null) {
                        try {
                            inputStreamReader.close();
                        } catch (IOException e7) {
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (inputStreamReader != null) {
                        try {
                            inputStreamReader.close();
                        } catch (IOException e8) {
                        }
                    }
                    throw th;
                }
            } catch (IOException e9) {
                e2 = e9;
                stringBuilder = data;
                Log.e("QualCommNv", "Error reading Nv detail file at /persist/eng_nv_check_data.dat", e2);
                showErrorAndFinish();
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e10) {
                    }
                }
            } catch (Throwable th4) {
                th = th4;
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e11) {
            e = e11;
            Log.e("QualCommNv", "Nv detail file not found at /persist/eng_nv_check_data.dat", e);
            showErrorAndFinish();
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
        } catch (IOException e12) {
            e2 = e12;
            Log.e("QualCommNv", "Error reading Nv detail file at /persist/eng_nv_check_data.dat", e2);
            showErrorAndFinish();
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
        }
    }

    private void showErrorAndFinish() {
        Toast.makeText(this, "Nv details file unavailable", 1).show();
        finish();
    }
}
