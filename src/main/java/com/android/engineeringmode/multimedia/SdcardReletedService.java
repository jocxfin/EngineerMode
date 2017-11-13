package com.android.engineeringmode.multimedia;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.android.engineeringmode.functions.Light;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SdcardReletedService extends Service {
    private Object lock = new Object();
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            boolean result;
            switch (msg.what) {
                case 0:
                    String pcba = msg.obj.getString("pcba");
                    Log.i("SdcardReletedService", "pcba received : " + pcba);
                    result = SdcardReletedService.this.writeSdFile(new File("/mnt/sdcard/", "pcba.txt"), pcba);
                    Message pcbaMsg = Message.obtain();
                    pcbaMsg.what = 100;
                    pcbaMsg.obj = Boolean.valueOf(result);
                    try {
                        msg.replyTo.send(pcbaMsg);
                        return;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        return;
                    }
                case Light.MAIN_KEY_LIGHT /*1*/:
                    String download_status = ((Bundle) msg.obj).getString("download_status");
                    Log.i("SdcardReletedService", "download_status received : " + download_status);
                    result = SdcardReletedService.this.writeSdFile(new File("/mnt/sdcard/", "download_status.txt"), download_status);
                    Message downloadStatusMsg = Message.obtain();
                    downloadStatusMsg.what = 101;
                    downloadStatusMsg.obj = Boolean.valueOf(result);
                    try {
                        msg.replyTo.send(downloadStatusMsg);
                        return;
                    } catch (RemoteException e2) {
                        e2.printStackTrace();
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private Messenger pcbaCallback = null;

    private boolean writeSdFile(File file, String content) {
        boolean result = false;
        if (Environment.getExternalStorageState().equals("mounted")) {
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
                FileOutputStream fout = new FileOutputStream(file);
                if (fout != null) {
                    fout.write(content.getBytes());
                    fout.close();
                }
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("SdcardReletedService", "writeSdFile file : " + file.getPath() + ", content : " + content + ", result : " + result);
        return result;
    }

    public IBinder onBind(Intent intent) {
        Log.d("SdcardReletedService", "onBind()");
        return new Messenger(this.mHandler).getBinder();
    }

    public void onCreate() {
        super.onCreate();
        Log.d("SdcardReletedService", "onCreate()");
    }

    public void onDestroy() {
        Log.d("SdcardReletedService", "onDestroy()");
        super.onDestroy();
    }
}
