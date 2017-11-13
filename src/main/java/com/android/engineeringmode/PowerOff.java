package com.android.engineeringmode;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageDataObserver.Stub;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.SystemService;
import android.provider.CallLog.Calls;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.util.ExternFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PowerOff extends Activity {
    private boolean app_data_deleted = false;
    private boolean battery_info_loaded = false;
    private boolean has_accessed = true;
    private boolean image_deleted = false;
    private boolean isBootRegisterRestored = false;
    private boolean isClockResetted = false;
    private String list = "";
    private IntentFilter mBatteryFilter = null;
    private int mBatteryLevel = 0;
    BroadcastReceiver mBatteryStatusReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                Log.d("PowerOff", "ACTION_BATTERY_CHANGED receiced");
                PowerOff.this.mBatteryLevel = intent.getIntExtra("level", 0);
                PowerOff.this.battery_info_loaded = true;
                PowerOff.this.mStringBuilder.append("BatteryLevel : ");
                PowerOff.this.mStringBuilder.append(Integer.toString(PowerOff.this.mBatteryLevel));
                PowerOff.this.mStringBuilder.append("\n");
                PowerOff.this.mHandler.sendEmptyMessage(6);
                PowerOff.this.unregisterReceiver(PowerOff.this.mBatteryStatusReceiver);
            }
        }
    };
    private ClearUserDataObserver mClearUserDataObserver;
    private ExternFunction mExternFun;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d("PowerOff", "handle msg:" + msg.what);
            if (msg.what == 0) {
                if (PowerOff.this.detectUseDataExists()) {
                    PowerOff.this.has_accessed = false;
                    PowerOff.this.message_tv.setText(PowerOff.this.getText(2131297489));
                    if (PowerOff.this.mPhotosExistsDialog == null) {
                        Builder b = new Builder(PowerOff.this);
                        b.setTitle(2131297487);
                        b.setView(PowerOff.this.message_tv);
                        b.setCancelable(false);
                        b.setPositiveButton(2131297490, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("PowerOff", "PositiveButton onclicked");
                                PowerOff.this.has_accessed = true;
                                PowerOff.this.mHandler.sendEmptyMessage(1);
                            }
                        });
                        b.setNegativeButton(2131297491, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                PowerOff.this.finish();
                            }
                        });
                        PowerOff.this.mPhotosExistsDialog = b.create();
                        PowerOff.this.mPhotosExistsDialog.setOnDismissListener(new OnDismissListener() {
                            public void onDismiss(DialogInterface dialog) {
                                Log.d("PowerOff", "alertdialog ondismissed");
                                PowerOff.this.mPhotosExistsDialog = null;
                            }
                        });
                        PowerOff.this.mPhotosExistsDialog.getWindow().setType(2003);
                        if (PowerOff.this.message_tv.getParent() != null) {
                            ((ViewGroup) PowerOff.this.message_tv.getParent()).removeView(PowerOff.this.message_tv);
                        }
                        PowerOff.this.mPhotosExistsDialog.show();
                    }
                } else {
                    PowerOff.this.mHandler.sendEmptyMessage(1);
                }
            } else if (msg.what == 1) {
                if (PowerOff.this.has_accessed) {
                    if (PowerOff.this.mProgressDialog == null) {
                        PowerOff.this.mProgressDialog = ProgressDialog.show(PowerOff.this, PowerOff.this.getString(2131297488), PowerOff.this.getString(2131297488), true, false);
                        PowerOff.this.mStringBuilder.append(PowerOff.this.getString(2131297488));
                        PowerOff.this.mStringBuilder.append("\n");
                    }
                    PowerOff.this.mHandler.sendEmptyMessage(5);
                    PowerOff.this.handleDeleteCallLog();
                    PowerOff.this.handleDeleteMediaFile();
                    PowerOff.this.image_deleted = true;
                    PowerOff.this.mHandler.sendMessage(PowerOff.this.mHandler.obtainMessage(6));
                } else {
                    PowerOff.this.mHandler.sendEmptyMessage(0);
                    return;
                }
            } else if (msg.what == 3) {
                if (PowerOff.this.mProgressDialog != null && PowerOff.this.mProgressDialog.isShowing()) {
                    PowerOff.this.mProgressDialog.dismiss();
                    PowerOff.this.mProgressDialog = null;
                }
                Log.i("PowerOff", "before shutdown : " + PowerOff.this.mStringBuilder.toString());
                PowerOff.this.shutdown();
            } else if (msg.what == 5) {
                PowerOff.this.resetAppData();
            } else if (msg.what == 6 && PowerOff.this.mProgressDialog != null && PowerOff.this.mProgressDialog.isShowing()) {
                PowerOff.this.mProgressDialog.setMessage(PowerOff.this.mStringBuilder.toString());
            }
            Log.i("PowerOff", "shutdown_handling : " + PowerOff.this.shutdown_handling + ", battery_info_loaded : " + PowerOff.this.battery_info_loaded + ", app_data_deleted : " + PowerOff.this.app_data_deleted + ", image_deleted : " + PowerOff.this.image_deleted);
            if (!PowerOff.this.shutdown_handling && PowerOff.this.battery_info_loaded && PowerOff.this.app_data_deleted && PowerOff.this.image_deleted) {
                Log.i("PowerOff", "clear finish, shutdown now!!!!");
                PowerOff.this.shutdown_handling = true;
                Global.putInt(PowerOff.this.getContentResolver(), "device_provisioned", 0);
                SystemProperties.set("persist.sys.allcommode", "false");
                SystemProperties.set("persist.sys.adb.engineermode", "1");
                Global.putInt(PowerOff.this.getContentResolver(), "adb_enabled", 0);
                Global.putInt(PowerOff.this.getContentResolver(), "development_settings_enabled", 0);
                Secure.putInt(PowerOff.this.getContentResolver(), "user_setup_complete", 0);
                SystemProperties.set("persist.sys.usb.config", "none");
                SystemProperties.set("persist.sys.911.shutdown", "1");
                SystemProperties.set("persist.sys.device_first_boot", "1");
                if (SystemProperties.get("persist.sys.oem.region", "").equals("OverSeas")) {
                    SystemService.start("deleteXml");
                }
                PowerOff.this.rotate_logs("/cache/fileAfter911", 10);
                PowerOff.this.listFile("/cache/fileAfter911");
                PowerOff.this.recordInCache("/cache/911");
                PowerOff.delFolder("/persist/time/");
                PowerOff.this.mExternFun.setProductLineTestFlagExtraByte(71, (byte) 1);
                PowerOff.this.mExternFun.setProductLineTestFlagExtraByte(72, (byte) PowerOff.this.mBatteryLevel);
                if (!PowerOff.this.mHandler.hasMessages(3)) {
                    PowerOff.this.mHandler.sendEmptyMessageDelayed(3, 500);
                }
            }
        }
    };
    private final Object mLock = new Object();
    private PackageManager mPackageManager;
    private AlertDialog mPhotosExistsDialog = null;
    private ProgressDialog mProgressDialog = null;
    private StringBuilder mStringBuilder = new StringBuilder();
    private TextView message_tv = null;
    private boolean shutdown_handling = false;

    class ClearUserDataObserver extends Stub {
        ClearUserDataObserver() {
        }

        public void onRemoveCompleted(String packageName, boolean succeeded) {
            synchronized (PowerOff.this.mLock) {
                Log.i("PowerOff", "app data RemoveCompleted : " + packageName + ", succeeded ?= " + succeeded);
                if ("com.oneplus.provision".equals(packageName) || "com.google.android.setupwizard".equals(packageName) || "com.oneplus.setupwizard".equals(packageName)) {
                    PowerOff.this.isBootRegisterRestored = succeeded;
                    PowerOff.this.mStringBuilder.append("BootReg reset ");
                    PowerOff.this.mStringBuilder.append(PowerOff.this.isBootRegisterRestored ? "successed" : "failed");
                    PowerOff.this.mStringBuilder.append("\n");
                }
                if ("com.google.android.deskclock".equals(packageName)) {
                    PowerOff.this.isClockResetted = succeeded;
                    PowerOff.this.mStringBuilder.append("AlarmClock reset ");
                    PowerOff.this.mStringBuilder.append(PowerOff.this.isClockResetted ? "successed" : "failed");
                    PowerOff.this.mStringBuilder.append("\n");
                }
                if ("net.oneplus.deskclock".equals(packageName)) {
                    PowerOff.this.isClockResetted = succeeded;
                    PowerOff.this.mStringBuilder.append("AlarmClock reset ");
                    PowerOff.this.mStringBuilder.append(PowerOff.this.isClockResetted ? "successed" : "failed");
                    PowerOff.this.mStringBuilder.append("\n");
                }
                if ("com.oneplus.deskclock".equals(packageName)) {
                    PowerOff.this.isClockResetted = succeeded;
                    PowerOff.this.mStringBuilder.append("AlarmClock reset ");
                    PowerOff.this.mStringBuilder.append(PowerOff.this.isClockResetted ? "successed" : "failed");
                    PowerOff.this.mStringBuilder.append("\n");
                }
                if (PowerOff.this.isBootRegisterRestored && PowerOff.this.isClockResetted) {
                    PowerOff.this.app_data_deleted = true;
                }
                PowerOff.this.mHandler.sendMessage(PowerOff.this.mHandler.obtainMessage(6));
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903182);
        Log.d("PowerOff", "onCreate begin:");
        this.message_tv = new TextView(this);
        this.message_tv.setGravity(17);
        this.mPackageManager = getPackageManager();
        ActivityManager mActivityManager = (ActivityManager) getSystemService("activity");
        Log.d("PowerOff", "onCreate get systemService end.");
        this.mExternFun = new ExternFunction(this);
        this.mExternFun.registerOnServiceConnected(this.mHandler, 0, null);
        Log.d("PowerOff", "onCreate registerOnServiceConnected end.");
        this.mBatteryFilter = new IntentFilter();
        this.mBatteryFilter.addAction("android.intent.action.BATTERY_CHANGED");
        registerReceiver(this.mBatteryStatusReceiver, this.mBatteryFilter);
        Log.d("PowerOff", "onCreate end.");
        rotate_logs("/cache/fileBefore911", 10);
        listFile("/cache/fileBefore911");
    }

    public static void delFolder(String folderPath) {
        try {
            Log.i("PowerOff", "del directory : " + folderPath);
            if (!new File(folderPath).exists()) {
                Log.i("PowerOff", "directory not exists : " + folderPath);
            } else if (new File(folderPath).isDirectory()) {
                delAllFile(folderPath);
                String str = folderPath;
                File myFilePath = new File(folderPath.toString());
                if (!myFilePath.delete()) {
                    Log.e("PowerOff", myFilePath + "can't be deleted now!");
                    myFilePath.deleteOnExit();
                }
            } else {
                Log.i("PowerOff", "isnot a directory : " + folderPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }
        String[] tempList = file.list();
        for (int i = 0; i < tempList.length; i++) {
            File temp;
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                Log.i("PowerOff", "del file : " + temp.toString());
                if (!temp.delete()) {
                    Log.e("PowerOff", temp + "can't be deleted now!");
                    temp.deleteOnExit();
                }
            }
            if (temp.isDirectory()) {
                Log.i("PowerOff", "del directory : " + temp.toString());
                delAllFile(path + "/" + tempList[i]);
                delFolder(path + "/" + tempList[i]);
                flag = true;
            }
        }
        return flag;
    }

    private boolean queryPictures(String path) {
        Cursor cur = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data"}, "_data LIKE ?", new String[]{"%" + path + "%"}, "_id");
        if (cur == null || cur.getCount() <= 0) {
            return false;
        }
        Log.i("PowerOff", "queryPictures : path = " + path + ", count = " + cur.getCount());
        return true;
    }

    private boolean queryVideo(String path) {
        Cursor cur = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data"}, "_data LIKE ?", new String[]{"%" + path + "%"}, "_id");
        if (cur == null || cur.getCount() <= 0) {
            return false;
        }
        Log.i("PowerOff", "queryVideo : path = " + path + ", count = " + cur.getCount());
        return true;
    }

    private boolean queryCallLog() {
        Cursor cur = getContentResolver().query(Calls.CONTENT_URI, new String[]{"number", "type"}, null, null, "number");
        if (cur == null || cur.getCount() <= 0) {
            return false;
        }
        Log.i("PowerOff", "queryCallLog : count= " + cur.getCount());
        return true;
    }

    private int deleteImages(String path) {
        return getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, "_data LIKE ?", new String[]{"%" + path + "%"});
    }

    private int deleteVideos(String path) {
        return getContentResolver().delete(Video.Media.EXTERNAL_CONTENT_URI, "_data LIKE ?", new String[]{"%" + path + "%"});
    }

    private int deleteCallLog() {
        return getContentResolver().delete(Calls.CONTENT_URI, null, null);
    }

    private boolean detectUseDataExists() {
        if (queryPictures("/DCIM") || queryVideo("/DCIM") || queryPictures("/Pictures/Screenshots") || queryCallLog()) {
            return true;
        }
        return false;
    }

    private void handleDeleteMediaFile() {
        Log.i("PowerOff", "handleDeleteMediaFile");
        this.mStringBuilder.append("Delete photos : ");
        this.mStringBuilder.append(Integer.toString(deleteImages("/DCIM")));
        this.mStringBuilder.append("\n");
        this.mStringBuilder.append("Delete screenshots : ");
        this.mStringBuilder.append(Integer.toString(deleteImages("/Pictures/Screenshots")));
        this.mStringBuilder.append("\n");
        this.mStringBuilder.append("Delete video : ");
        this.mStringBuilder.append(Integer.toString(deleteVideos("/DCIM")));
        this.mStringBuilder.append("\n");
        delFolder("/sdcard/DCIM");
        delFolder("/sdcard/Pictures/Screenshots");
    }

    private void handleDeleteCallLog() {
        Log.i("PowerOff", "handleDeleteCallLog");
        this.mStringBuilder.append("Delete CallLogs : ");
        this.mStringBuilder.append(Integer.toString(deleteCallLog()));
        this.mStringBuilder.append("\n");
    }

    private void listDir(String Path) {
        String[] children = new File(Path).list();
        if (children != null) {
            for (String str : children) {
                this.list += str + "\n";
            }
        }
    }

    private void listFile(String dir) {
        this.list = "";
        File file = new File(dir);
        try {
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        listDir("/sdcard/Music/");
        listDir("/sdcard/Pictures/");
        listDir("/sdcard/Movies/");
        writeNode(dir, this.list);
    }

    private void writeNode(String Path, String value) {
        try {
            FileWriter fr = new FileWriter(new File(Path));
            fr.write(value);
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("PowerOff", "write node failed!");
        }
        Log.e("PowerOff", "write node succeed! value=" + value);
    }

    private int readFileByLines(String fileName) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        int result = 0;
        String tempString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                tempString = reader.readLine();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("PowerOff", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("PowerOff", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("PowerOff", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    try {
                        result = Integer.valueOf(tempString).intValue();
                    } catch (NumberFormatException e3) {
                        Log.e("PowerOff", "readFileByLines NumberFormatException:" + e3.getMessage());
                    }
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("PowerOff", "readFileByLines io close exception :" + e122.getMessage());
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
        } catch (IOException e4) {
            e = e4;
            Log.e("PowerOff", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            result = Integer.valueOf(tempString).intValue();
            return result;
        }
        if (!(tempString == null || "".equals(tempString))) {
            result = Integer.valueOf(tempString).intValue();
        }
        return result;
    }

    private void recordInCache(String Path) {
        File recordFile = new File(Path);
        if (recordFile.exists()) {
            writeNode(Path, (readFileByLines(Path) + 1) + "");
            return;
        }
        try {
            recordFile.createNewFile();
        } catch (IOException e) {
            Log.e("PowerOff", "recordInCache io exception:" + e.getMessage());
        }
        writeNode(Path, "1");
    }

    private void rotate_logs(String dir, int max) {
        for (int i = max - 1; i >= 0; i--) {
            File old_file;
            if (i == 0) {
                old_file = new File(dir);
            } else {
                old_file = new File(dir + "." + Integer.toString(i));
            }
            File new_file = new File(dir + "." + Integer.toString(i + 1));
            if (old_file.exists()) {
                old_file.renameTo(new_file);
            }
        }
    }

    protected void onDestroy() {
        Log.d("PowerOff", "onDestroy");
        if (this.mExternFun != null) {
            this.mExternFun.unregisterOnServiceConnected(this.mHandler);
            this.mExternFun.dispose();
        }
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
        if (this.mPhotosExistsDialog != null && this.mPhotosExistsDialog.isShowing()) {
            this.mPhotosExistsDialog.dismiss();
            this.mPhotosExistsDialog = null;
        }
        super.onDestroy();
    }

    private void resetAppData() {
        SystemProperties.set("oem.boot_data.clear", "1");
        if (SystemProperties.get("persist.sys.adbroot").equals("1")) {
            SystemProperties.set("persist.sys.adbroot", "0");
        }
        resetAlarmClockData();
        enableWelcomePage();
    }

    private void resetAlarmClockData() {
        if (this.mClearUserDataObserver == null) {
            this.mClearUserDataObserver = new ClearUserDataObserver();
        }
        ActivityManager am = (ActivityManager) getSystemService("activity");
        if (isAvilible("com.oneplus.deskclock")) {
            am.clearApplicationUserData("com.oneplus.deskclock", this.mClearUserDataObserver);
        }
        if (isAvilible("com.google.android.deskclock")) {
            am.clearApplicationUserData("com.google.android.deskclock", this.mClearUserDataObserver);
        } else if (isAvilible("net.oneplus.deskclock")) {
            am.clearApplicationUserData("net.oneplus.deskclock", this.mClearUserDataObserver);
        }
    }

    private boolean isAvilible(String packageName) {
        List<PackageInfo> pinfo = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    private void clearHomeData(ComponentName name, String pkgName) {
        getPackageManager().setComponentEnabledSetting(name, 1, 1);
        ((ActivityManager) getSystemService("activity")).clearApplicationUserData(pkgName, this.mClearUserDataObserver);
    }

    private void enableWelcomePage() {
        Log.d("PowerOff", "enableWelcomePage begin");
        if (isAvilible("com.oneplus.setupwizard")) {
            Global.putInt(getContentResolver(), "device_provisioned_oneplus", 0);
            Global.putInt(getContentResolver(), "user_setup_complete_oneplus", 0);
            System.putInt(getContentResolver(), "buttons_show_on_screen_navkeys", 0);
            System.putInt(getContentResolver(), "oem_acc_key_lock_mode", 0);
            System.putInt(getContentResolver(), "shelf_enabled_default", 0);
            System.putInt(getContentResolver(), "oem_acc_blackscreen_master_switch", 0);
            System.putInt(getContentResolver(), "oem_acc_blackscreen_gestrue_enable", 0);
            clearHomeData(new ComponentName("com.oneplus.setupwizard", "com.oneplus.setupwizard.OneplusServicesActivity"), "com.oneplus.setupwizard");
        }
        if (isAvilible("com.google.android.setupwizard")) {
            clearHomeData(new ComponentName("com.google.android.setupwizard", "com.google.android.setupwizard.SetupWizardActivity"), "com.google.android.setupwizard");
        }
        if (isAvilible("com.oneplus.provision")) {
            clearHomeData(new ComponentName("com.oneplus.provision", "com.oneplus.provision.WelcomePage"), "com.oneplus.provision");
        }
        if (isAvilible("com.android.launcher3")) {
            ((ActivityManager) getSystemService("activity")).clearApplicationUserData("com.android.launcher3", this.mClearUserDataObserver);
        }
        if (isAvilible("com.oneplus.launcher")) {
            ((ActivityManager) getSystemService("activity")).clearApplicationUserData("com.oneplus.launcher", this.mClearUserDataObserver);
        }
        if (isAvilible("com.oneplus.hydrogen.launcher")) {
            ((ActivityManager) getSystemService("activity")).clearApplicationUserData("com.oneplus.hydrogen.launcher", this.mClearUserDataObserver);
        }
        if (isAvilible("net.oneplus.launcher")) {
            ((ActivityManager) getSystemService("activity")).clearApplicationUserData("net.oneplus.launcher", this.mClearUserDataObserver);
        }
        if (isAvilible("net.oneplus.h2launcher")) {
            ((ActivityManager) getSystemService("activity")).clearApplicationUserData("net.oneplus.h2launcher", this.mClearUserDataObserver);
        }
        if (isAvilible("com.google.android.gms")) {
            ((ActivityManager) getSystemService("activity")).clearApplicationUserData("com.google.android.gms", this.mClearUserDataObserver);
        }
        if (isAvilible("com.google.android.onetimeinitializer")) {
            ((ActivityManager) getSystemService("activity")).clearApplicationUserData("com.google.android.onetimeinitializer", this.mClearUserDataObserver);
        }
    }

    public static void clearRecentApps(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        for (RecentTaskInfo recentTaskInfo : activityManager.getRecentTasks(50, 1)) {
            Intent baseIntent = recentTaskInfo.baseIntent;
            if (baseIntent != null) {
                String packageName = baseIntent.getComponent().getPackageName();
                if (!packageName.equals("com.android.engineeringmode")) {
                    Log.d("PowerOff", "Remove " + packageName);
                    activityManager.removeTask(recentTaskInfo.persistentId);
                }
            }
        }
    }

    private void shutdown() {
        Log.i("PowerOff", "shutdown now!!!!");
        clearRecentApps(this);
        SystemClock.sleep(300);
        if (new File("/proc/ship_mode").exists()) {
            Light.enterShipMode();
        }
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        intent.setFlags(268435456);
        startActivity(intent);
    }
}
