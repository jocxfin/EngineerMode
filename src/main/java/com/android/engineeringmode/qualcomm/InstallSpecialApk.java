package com.android.engineeringmode.qualcomm;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import java.io.File;

public class InstallSpecialApk extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String zoneID = getIntent().getStringExtra("zoneID");
        Log.d("cbt", "zoneID=" + zoneID);
        DeleteFolder("data/oppo-app");
        DeleteFolder("data/oppo-sys-app");
        if (zoneID != null) {
            Log.d("cbt", "installSpecialApk before");
            FileTransactionUtil.installSpecialApk(zoneID, this);
            Log.d("cbt", "installSpecialApk after");
        }
        reboot();
        finish();
    }

    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    public boolean deleteDirectory(String filePath) {
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isFile()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } else {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (flag) {
            return true;
        }
        return false;
    }

    public boolean DeleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return deleteFile(filePath);
        }
        return deleteDirectory(filePath);
    }

    private void reboot() {
        new Thread(new Runnable() {
            public void run() {
                Log.i("", "reboot device...");
                ((PowerManager) InstallSpecialApk.this.getSystemService("power")).reboot(null);
            }
        }).start();
    }
}
