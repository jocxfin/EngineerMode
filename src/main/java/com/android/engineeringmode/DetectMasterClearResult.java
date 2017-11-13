package com.android.engineeringmode;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.CallLog.Calls;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetectMasterClearResult extends Activity {
    private List<String> afterFilesList = new ArrayList();
    private List<String> buildInList = new ArrayList();
    private PowerManager mPowerManager;
    private StringBuilder mStringBuilder = new StringBuilder();
    private WakeLock mWakelock;
    private List<String> whiteList = Arrays.asList(this.whiteListArray);
    private String[] whiteListArray = new String[]{"Alarms", "Android", "DCIM", "Notifications", "Reader", "ThemeStore", "Download", "CloudDisk", "Browser"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView detect_result_tv = new TextView(this);
        setContentView(detect_result_tv);
        detect_result_tv.setTextSize(20.0f);
        detect_result_tv.setGravity(17);
        this.mPowerManager = (PowerManager) getSystemService("power");
        this.mWakelock = this.mPowerManager.newWakeLock(268435482, "DetectMasterClearResult");
        if (!(this.mWakelock == null || this.mWakelock.isHeld())) {
            this.mWakelock.acquire(2000);
        }
        if (!detectCallLog()) {
            detect_result_tv.setText("CallLog Not Clear");
            detect_result_tv.setTextColor(-65536);
        } else if (detectMasterClearProcess() && detectMasterClearException() && detectPhotosVideosExists() && detectOthers()) {
            Log.d("DetectMasterClearResult", "Detect Succes.");
            detect_result_tv.setText("Detect Success");
            detect_result_tv.setTextColor(-16711936);
            SystemProperties.set("sys.masterclear.result", "1");
            SystemClock.sleep(200);
            finish();
            return;
        } else {
            detect_result_tv.setText("Sdcard Files FAIL\n" + this.mStringBuilder.toString());
            detect_result_tv.setTextColor(-65536);
        }
        SystemProperties.set("sys.masterclear.result", "0");
        SystemClock.sleep(200);
    }

    private boolean detectMasterClearException() {
        int i = 0;
        while (i < this.buildInList.size()) {
            File file_path = new File((String) this.buildInList.get(i));
            if (file_path.exists()) {
                i++;
            } else {
                this.mStringBuilder.append("File ").append(file_path).append(" missed...");
                this.mStringBuilder.append("\n");
                Log.e("DetectMasterClearResult", "File " + file_path + " missed...");
                return false;
            }
        }
        return true;
    }

    private boolean detectOthers() {
        String[] need_check_file_path = new String[]{"/sdcard/Pictures/Screenshots", "/sdcard/PCBA.txt", "/sdcard/Recordings"};
        for (int i = 0; i < need_check_file_path.length; i++) {
            if (new File(need_check_file_path[i]).exists()) {
                Log.e("DetectMasterClearResult", "path " + need_check_file_path[i] + " exists...");
                this.mStringBuilder.append(need_check_file_path[i]);
                this.mStringBuilder.append(" exists \n");
                return false;
            }
        }
        return true;
    }

    private boolean detectPhotosVideosExists() {
        File camera_dir = new File("/sdcard/DCIM/Camera");
        if (!camera_dir.exists()) {
            return true;
        }
        if (!camera_dir.isDirectory()) {
            Log.e("DetectMasterClearResult", "camera_dir " + camera_dir.toString() + " not a Dir...");
            this.mStringBuilder.append(camera_dir.toString());
            this.mStringBuilder.append(" is not a Dir \n");
        } else if (camera_dir.list().length == 0) {
            return true;
        } else {
            Log.e("DetectMasterClearResult", "camera_dir " + camera_dir.toString() + " not empty...");
            for (String str : camera_dir.list()) {
                this.mStringBuilder.append(str);
                this.mStringBuilder.append(" in DCIM \n");
            }
        }
        return false;
    }

    private boolean detectMasterClearProcess() {
        if (new File("/sdcard/.sd.txt").exists()) {
            loadFilesList("/sdcard/.sd.txt", this.buildInList);
            return true;
        }
        this.mStringBuilder.append("File /sdcard/.sd.txt is not exist...");
        this.mStringBuilder.append("\n");
        Log.e("DetectMasterClearResult", "File /sdcard/.sd.txt is not exist...");
        return false;
    }

    private boolean detectCallLog() {
        if (getContentResolver().query(Calls.CONTENT_URI, null, null, null, "date DESC").moveToFirst()) {
            return false;
        }
        Log.i("DetectMasterClearResult", "call log clear successed!!");
        return true;
    }

    private void loadFilesList(String path, List<String> des) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new FileReader(path));
            while (true) {
                try {
                    String line = bufferedReader2.readLine();
                    if (line == null) {
                        break;
                    }
                    des.add(line.trim());
                } catch (IOException e2) {
                    e = e2;
                    bufferedReader = bufferedReader2;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = bufferedReader2;
                }
            }
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (IOException e3) {
                    Log.e("DetectMasterClearResult", e3.getMessage());
                }
            }
        } catch (IOException e4) {
            e3 = e4;
            try {
                Log.e("DetectMasterClearResult", e3.getMessage());
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e32) {
                        Log.e("DetectMasterClearResult", e32.getMessage());
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e322) {
                        Log.e("DetectMasterClearResult", e322.getMessage());
                    }
                }
                throw th;
            }
        }
    }
}
