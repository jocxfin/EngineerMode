package com.android.engineeringmode.manualtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

import java.text.DecimalFormat;

public class SDCardTest extends PreferenceActivity {
    private IntentFilter intentFilter;
    private Preference mInternalAvail;
    private Preference mInternalSize;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.w("SDCardTest", "receiver an Intent, intent = " + intent.getAction());
            SDCardTest.this.updateMemoryStatus();
            SDCardTest.this.updateInternalStatus();
        }
    };
    private Resources mRes;
    private Preference mSdAvail;
    private Preference mSdSize;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(2130968614);
        setTitle(2131296894);
        this.mSdSize = findPreference("sdcard_total_cap");
        this.mSdAvail = findPreference("sdcard_available");
        this.mInternalSize = findPreference("internal_total_cap");
        this.mInternalAvail = findPreference("internal_available");
        this.mRes = getResources();
    }

    protected void onResume() {
        super.onResume();
        this.intentFilter = new IntentFilter("android.intent.action.MEDIA_REMOVED");
        this.intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        this.intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        this.intentFilter.addAction("android.intent.action.MEDIA_SHARED");
        this.intentFilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
        this.intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTABLE");
        this.intentFilter.addAction("android.intent.action.MEDIA_NOFS");
        this.intentFilter.addAction("android.intent.action.MEDIA_SCANNER_STARTED");
        this.intentFilter.addAction("android.intent.action.MEDIA_SCANNER_FINISHED");
        this.intentFilter.addDataScheme("file");
        registerReceiver(this.mReceiver, this.intentFilter);
        updateMemoryStatus();
        updateInternalStatus();
    }

    protected void onPause() {
        unregisterReceiver(this.mReceiver);
        super.onPause();
    }

    protected void onDestroy() {
        this.intentFilter = null;
        super.onDestroy();
    }

    public static boolean isInternalAvaiable() {
        String status = Environment.getExternalStorageState();
        if (status.equals("unmounted") || status.equals("removed") || status.equals("shared")) {
            Log.w("SDCardTest", "Internal is unavaiable");
            return false;
        }
        Log.w("SDCardTest", "Internal is avaiable");
        return true;
    }

    private void updateMemoryStatus() {
        String status = Environment.getExternalStorageState();
        Log.d("SDCardTest", "SSSSSSSSSSSSSSSSS__updateMemoryStatus" + status);
        String readOnly = "";
        if (status.equals("mounted_ro")) {
            status = "mounted";
            readOnly = this.mRes.getString(2131296900);
            Log.w("SDCardTest", "readOnly = " + readOnly);
        }
        if (status.equals("mounted")) {
            try {
                StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
                long blockSize = (long) stat.getBlockSize();
                long availableBlocks = (long) stat.getAvailableBlocks();
                String strTotalSize = formatSize(((long) stat.getBlockCount()) * blockSize);
                String strAvailable = formatSize(availableBlocks * blockSize);
                this.mSdSize.setSummary(strTotalSize);
                this.mSdAvail.setSummary(strAvailable + readOnly);
                Log.w("SDCardTest", "TotalSize = " + strTotalSize);
                Log.w("SDCardTest", "Available = " + strAvailable);
                Log.w("SDCardTest", "readonly = " + readOnly);
                return;
            } catch (IllegalArgumentException e) {
                status = "removed";
                Log.w("SDCardTest", "SDcard is removed!");
                return;
            }
        }
        this.mSdSize.setSummary(this.mRes.getString(2131296899));
        this.mSdAvail.setSummary(this.mRes.getString(2131296899));
    }

    private void updateInternalStatus() {
        String status = Environment.getExternalStorageState();
        Log.e("SDCardTest", "updateInternalStatus:" + status);
        String readOnly = "";
        if (status.equals("mounted_ro")) {
            status = "mounted";
            readOnly = this.mRes.getString(2131296900);
            Log.w("SDCardTest", "readOnly = " + readOnly);
        }
        if (status.equals("mounted")) {
            try {
                StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
                long blockSize = (long) stat.getBlockSize();
                long availableBlocks = (long) stat.getAvailableBlocks();
                String strTotalSize = formatSize(((long) stat.getBlockCount()) * blockSize);
                String strAvailable = formatSize(availableBlocks * blockSize);
                this.mInternalSize.setSummary(strTotalSize);
                this.mInternalAvail.setSummary(strAvailable + readOnly);
                Log.w("SDCardTest", "TotalSize = " + strTotalSize);
                Log.w("SDCardTest", "Available = " + strAvailable);
                Log.w("SDCardTest", "readonly = " + readOnly);
                return;
            } catch (IllegalArgumentException e) {
                status = "removed";
                return;
            }
        }
        this.mInternalSize.setSummary(this.mRes.getString(2131296902));
        this.mInternalAvail.setSummary(this.mRes.getString(2131296902));
    }

    private String formatSize(long size) {
        String str = null;
        if (size >= 1024) {
            str = " KB";
            size /= 1024;
            if (size >= 1024) {
                str = " MB";
                size /= 1024;
            }
        }
        DecimalFormat formatter = new DecimalFormat();
        formatter.setGroupingSize(3);
        String result = formatter.format(size);
        if (str != null) {
            return result + str;
        }
        return result;
    }
}
