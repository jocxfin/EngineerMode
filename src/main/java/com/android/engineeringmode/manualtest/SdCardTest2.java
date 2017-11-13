package com.android.engineeringmode.manualtest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;

public class SdCardTest2 extends PreferenceActivity {
    Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                SdCardTest2.this.sendMessageToService(SdCardTest2.this.mResultHandler, Environment.getExternalStorageDirectory().getPath());
            } else if (message.what == 2) {
                StorageManager storageManager = (StorageManager) SdCardTest2.this.getSystemService("storage");
                VolumeInfo sdcardVolume = null;
                for (VolumeInfo vol : storageManager.getVolumes()) {
                    if (vol.getDisk() != null && vol.getDisk().isSd()) {
                        sdcardVolume = vol;
                    }
                }
                if (sdcardVolume == null) {
                    SdCardTest2.this.mSdSize.setSummary("sdcard is not available");
                    SdCardTest2.this.mSdAvail.setSummary("sdcard is not available");
                    return;
                }
                String volumeState;
                String sdcardPath = sdcardVolume.getPath().toString();
                try {
                    volumeState = storageManager.getVolumeState(sdcardPath);
                } catch (IllegalArgumentException e) {
                    volumeState = null;
                }
                if (volumeState != null) {
                    SdCardTest2.this.sendMessageToService(SdCardTest2.this.mResultHandler2, new File(sdcardPath).getPath());
                    return;
                }
                SdCardTest2.this.mSdSize.setSummary("sdcard is not available");
                SdCardTest2.this.mSdAvail.setSummary("sdcard is not available");
            }
        }
    };
    private Preference mInternalAvail;
    private Preference mInternalSize;
    private Messenger mMessenger;
    private Handler mResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle res = msg.getData();
            long blockSize = res.getLong("blockSize");
            long totalBlocks = res.getLong("totalBlocks");
            long availableBlocks = res.getLong("availableBlocks");
            if (blockSize > 0 && totalBlocks > 0 && availableBlocks > 0) {
                Calendar now = Calendar.getInstance();
                String str = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--SdCardTest2--" + "has entered it";
            }
            String strTotalSize = SdCardTest2.this.formatSize(totalBlocks * blockSize);
            String strAvailable = SdCardTest2.this.formatSize(availableBlocks * blockSize);
            SdCardTest2.this.mInternalSize.setSummary(strTotalSize);
            SdCardTest2.this.mInternalAvail.setSummary(strAvailable);
        }
    };
    private Handler mResultHandler2 = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle res = msg.getData();
            long blockSize = res.getLong("blockSize");
            long totalBlocks = res.getLong("totalBlocks");
            long availableBlocks = res.getLong("availableBlocks");
            if (blockSize <= 0 || totalBlocks <= 0 || availableBlocks <= 0) {
                SdCardTest2.this.mSdSize.setSummary("sdcard is not available");
                SdCardTest2.this.mSdAvail.setSummary("sdcard is not available");
                return;
            }
            String strTotalSize = SdCardTest2.this.formatSize(totalBlocks * blockSize);
            String strAvailable = SdCardTest2.this.formatSize(availableBlocks * blockSize);
            SdCardTest2.this.mSdSize.setSummary(strTotalSize);
            SdCardTest2.this.mSdAvail.setSummary(strAvailable);
        }
    };
    private Preference mSdAvail;
    private Preference mSdSize;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            SdCardTest2.this.mMessenger = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            SdCardTest2.this.mMessenger = new Messenger(service);
        }
    };

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(2130968614);
        setTitle(2131296894);
        this.mSdSize = findPreference("sdcard_total_cap");
        this.mSdAvail = findPreference("sdcard_available");
        this.mInternalSize = findPreference("internal_total_cap");
        this.mInternalAvail = findPreference("internal_available");
        initService();
        this.mHandler.sendEmptyMessageDelayed(1, 2000);
        this.mHandler.sendEmptyMessageDelayed(2, 2000);
    }

    private void initService() {
        Intent intent = new Intent("com.oppo.sdcard.command");
        intent.setPackage("com.oneplus.sdcardservice");
        bindService(intent, this.mServiceConnection, 1);
    }

    protected void onDestroy() {
        unbindService(this.mServiceConnection);
        super.onDestroy();
    }

    private void sendMessageToService(Handler resultHandler, String path) {
        Message msg = Message.obtain(null, 0);
        msg.replyTo = new Messenger(resultHandler);
        msg.arg1 = 1;
        Bundle data = new Bundle();
        data.putString("KEY_RENAME_FROM", path);
        data.putString("KEY_RENAME_TO", path);
        msg.setData(data);
        try {
            this.mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
