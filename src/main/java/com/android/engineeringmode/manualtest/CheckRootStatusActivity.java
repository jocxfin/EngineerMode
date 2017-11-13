package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Method;

public class CheckRootStatusActivity extends Activity {
    private TextView mPhoneHistoryInfo;
    private TextView mTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903181);
        this.mTextView = (TextView) findViewById(2131493487);
        this.mTextView.setTextSize(60.0f);
        if (isHasRootPermission()) {
            this.mTextView.setText(getString(2131297306));
            this.mTextView.setTextColor(-16711936);
        } else {
            this.mTextView.setText(getString(2131297307));
            this.mTextView.setTextColor(-65536);
        }
        this.mPhoneHistoryInfo = (TextView) findViewById(2131493488);
        try {
            Object oRemoteService = Class.forName("android.os.ServiceManager").getMethod("getService", new Class[]{String.class}).invoke(null, new Object[]{"OEMExService"});
            Object oIOemExService = Class.forName("com.oem.os.IOemExService$Stub").getMethod("asInterface", new Class[]{IBinder.class}).invoke(null, new Object[]{oRemoteService});
            Method getPhoneHistoryRecord = oIOemExService.getClass().getMethod("getPhoneHistoryRecord", new Class[]{Integer.TYPE});
            int rebootCnt = ((Integer) getPhoneHistoryRecord.invoke(oIOemExService, new Object[]{Integer.valueOf(0)})).intValue();
            int abrebootCnt = ((Integer) getPhoneHistoryRecord.invoke(oIOemExService, new Object[]{Integer.valueOf(1)})).intValue();
            int updateCnt = ((Integer) getPhoneHistoryRecord.invoke(oIOemExService, new Object[]{Integer.valueOf(2)})).intValue();
            this.mPhoneHistoryInfo.setText("ARC : " + abrebootCnt + "\nUC : " + updateCnt + "\nFC : " + ((Integer) getPhoneHistoryRecord.invoke(oIOemExService, new Object[]{Integer.valueOf(3)})).intValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isHasRootPermission() {
        File file;
        Exception e;
        boolean z = true;
        String[] suPaths = new String[]{"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};
        int i = 0;
        File file2 = null;
        while (i < suPaths.length) {
            try {
                file = new File(suPaths[i] + "su");
                if (file != null) {
                    try {
                        if (file.exists()) {
                            Log.i("CheckRootStatusActivity", "device has root permission :" + true);
                            return true;
                        }
                    } catch (Exception e2) {
                        e = e2;
                    }
                }
                i++;
                file2 = file;
            } catch (Exception e3) {
                e = e3;
                file = file2;
            }
        }
        file = file2;
        if (!(false || checkRoot())) {
            z = checkAngelaRoot();
        }
        return z;
        Log.i("CheckRootStatusActivity", "IOException:" + e.getMessage());
        z = checkAngelaRoot();
        return z;
    }

    private boolean checkAngelaRoot() {
        boolean isAngelaRoot = SystemProperties.get("persist.sys.adbroot", "").equals("1");
        Log.i("CheckRootStatusActivity", "my device has been angela root  :" + isAngelaRoot);
        return isAngelaRoot;
    }

    private boolean checkRoot() {
        try {
            Object oRemoteService = Class.forName("android.os.ServiceManager").getMethod("getService", new Class[]{String.class}).invoke(null, new Object[]{"OEMExService"});
            Object oIOemExService = Class.forName("com.oem.os.IOemExService$Stub").getMethod("asInterface", new Class[]{IBinder.class}).invoke(null, new Object[]{oRemoteService});
            boolean ret = ((Boolean) oIOemExService.getClass().getMethod("getRootStatus", new Class[0]).invoke(oIOemExService, new Object[0])).booleanValue();
            Log.i("CheckRootStatusActivity", "my device has root permission :" + ret);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
