package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.multimedia.SdcardReletedService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DownloadStatus extends Activity {
    private static String TAG = "DownloadStatus";
    private static String downloadInfo = "/sys/devices/download_info";
    private String downloadSmtTime;
    private String downloadSmtVersion;
    private TextView mDownloadInfoSmtTime;
    private TextView mDownloadInfoSmtVersion;
    private TextView mDownloadInfoUpgradeTime1;
    private TextView mDownloadInfoUpgradeTime2;
    private TextView mDownloadInfoUpgradeTime3;
    private TextView mDownloadInfoUpgradeVersion1;
    private TextView mDownloadInfoUpgradeVersion2;
    private TextView mDownloadInfoUpgradeVersion3;
    private TextView mDownloadStatus;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    if (((Boolean) msg.obj).booleanValue()) {
                        Toast.makeText(DownloadStatus.this, "download_status success!", 3000).show();
                        Log.i(DownloadStatus.TAG, "write download_status to sdcard successed");
                    } else {
                        Log.e(DownloadStatus.TAG, "query download_status fail");
                    }
                    DownloadStatus.this.finish();
                    DownloadStatus.this.unBindSdService();
                    return;
                default:
                    return;
            }
        }
    };
    private Messenger mSdClientMessenger;
    private ServiceConnection mSdServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(DownloadStatus.TAG, "mSdServiceConnection onServiceConnected");
            DownloadStatus.this.mSdServiceMessenger = new Messenger(service);
            DownloadStatus.this.writeDownloadStatusToSdcard();
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.i(DownloadStatus.TAG, "mSdServiceConnection onServiceDisconnected");
            DownloadStatus.this.mSdServiceMessenger = null;
        }
    };
    private Messenger mSdServiceMessenger = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(2131297548);
        setContentView(2130903088);
        this.mDownloadStatus = (TextView) findViewById(2131493062);
        this.mDownloadInfoSmtTime = (TextView) findViewById(2131493063);
        this.mDownloadInfoSmtVersion = (TextView) findViewById(2131493064);
        this.mDownloadInfoUpgradeTime1 = (TextView) findViewById(2131493065);
        this.mDownloadInfoUpgradeVersion1 = (TextView) findViewById(2131493066);
        this.mDownloadInfoUpgradeTime2 = (TextView) findViewById(2131493067);
        this.mDownloadInfoUpgradeVersion2 = (TextView) findViewById(2131493068);
        this.mDownloadInfoUpgradeTime3 = (TextView) findViewById(2131493069);
        this.mDownloadInfoUpgradeVersion3 = (TextView) findViewById(2131493070);
        getWindow().addFlags(6815744);
        if (isFileExist(downloadInfo + "/smt_download_time")) {
            this.downloadSmtTime = readStringFromFile(downloadInfo + "/smt_download_time");
        }
        if (isFileExist(downloadInfo + "/smt_download_version")) {
            this.downloadSmtVersion = readStringFromFile(downloadInfo + "/smt_download_version");
        }
        if (isFileExist(downloadInfo)) {
            Log.i(TAG, "set");
            SystemProperties.set("sys.eng.download.status", "1");
            Log.i(TAG, "get");
            this.mDownloadStatus.setTextColor(-16711936);
            this.mDownloadStatus.setText("PASS");
        } else {
            this.mDownloadStatus.setTextColor(-65536);
            this.mDownloadStatus.setText("FAIL");
        }
        if (getIntent() != null) {
            String extra_au = getIntent().getStringExtra("auto_start");
            if (extra_au != null) {
                Log.i(TAG, "auto_test = " + extra_au);
                if (extra_au.equals("true")) {
                    this.mSdClientMessenger = new Messenger(this.mHandler);
                    bindSdService();
                }
            }
        }
    }

    private boolean isFileExist(String fileName) {
        return new File(fileName).exists();
    }

    private String readStringFromFile(String fileName) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        String tempString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                tempString = reader.readLine();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e(TAG, "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e(TAG, "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e(TAG, "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    return tempString;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e(TAG, "readFileByLines io close exception :" + e122.getMessage());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = reader;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            Log.e(TAG, "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return tempString;
        }
        return tempString;
    }

    private void bindSdService() {
        Log.i(TAG, "bindSdService");
        bindService(new Intent(this, SdcardReletedService.class), this.mSdServiceConnection, 1);
    }

    private void unBindSdService() {
        Log.i(TAG, "unBindSdService");
        if (this.mSdServiceConnection != null) {
            unbindService(this.mSdServiceConnection);
            this.mSdServiceConnection = null;
        }
    }

    private void writeDownloadStatusToSdcard() {
        Message msg = Message.obtain();
        msg.what = 1;
        Bundle bundle = new Bundle();
        bundle.putString("download_status", "download over SMT time: " + this.downloadSmtTime + " +SMT version: " + this.downloadSmtVersion);
        msg.obj = bundle;
        msg.replyTo = this.mSdClientMessenger;
        try {
            this.mSdServiceMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
