package com.android.engineeringmode.qualcomm;

import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LogSwitch extends PreferenceActivity implements OnPreferenceClickListener {
    private static final String[] WIFI_VERSION_PATH = new String[]{"/sys/devices/soc/18800000.qcom,icnss/cnss_version_information", "/sys/devices/fb000000.qcom,wcnss-wlan/wcnss_build_version", "/sys/devices/soc.0/6300000.qcom,cnss/cnss_version_information", "/sys/devices/soc/soc:qcom,cnss/cnss_version_information"};
    private String btVersion = "";
    private CheckBoxPreference mAssert;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("LogSwitch", "action = " + action);
            if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                Log.d("LogSwitch", "ACTION_STATE_CHANGED state: " + state);
                if (12 == state && LogSwitch.this.mBtdisable) {
                    LogSwitch.this.btVersion = SystemProperties.get("bluetooth.fwversion", "");
                    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                    LogSwitch.this.mBtdisable = false;
                    boolean success = adapter.disable();
                    Log.d("LogSwitch", "btVersion = " + LogSwitch.this.btVersion);
                    if (LogSwitch.this.btVersion.equals("")) {
                        LogSwitch.this.btVersion = LogSwitch.this.getString(2131297438);
                    }
                    LogSwitch.this.findPreference("key_bt_version").setSummary(LogSwitch.this.btVersion);
                }
            }
        }
    };
    private CheckBoxPreference mBtLog;
    private boolean mBtdisable = false;
    private Preference mDeviceLog;
    private CheckBoxPreference mDump;
    private Preference mOneClickLogkit;
    private Preference mOppoLogkit;
    private CheckBoxPreference mQeAssert;
    private CheckBoxPreference mSystemMonitor;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(2130968576);
        this.mAssert = (CheckBoxPreference) findPreference("assert");
        this.mQeAssert = (CheckBoxPreference) findPreference("qeassert");
        this.mDump = (CheckBoxPreference) findPreference("dump");
        this.mBtLog = (CheckBoxPreference) findPreference("btstacklog");
        this.mOppoLogkit = findPreference("key_oppo_logkit");
        this.mDeviceLog = findPreference("device_log");
        getPreferenceScreen().removePreference(this.mAssert);
        getPreferenceScreen().removePreference(this.mQeAssert);
        getPreferenceScreen().removePreference(this.mBtLog);
        getPreferenceScreen().removePreference(this.mDeviceLog);
        this.mOppoLogkit.setOnPreferenceClickListener(this);
        this.mOneClickLogkit = findPreference("key_oneclick_logkit");
        this.mOneClickLogkit.setOnPreferenceClickListener(this);
        if (!("N5207".equals(SystemProperties.get("ro.product.name", "oppo")) || "N5200".equals(SystemProperties.get("ro.product.name", "oppo")) || "N5206".equals(SystemProperties.get("ro.product.name", "oppo")) || "N5209".equals(SystemProperties.get("ro.product.name", "oppo")))) {
            getPreferenceScreen().removePreference(this.mAssert);
        }
        this.mAssert.setOnPreferenceClickListener(this);
        this.mQeAssert.setOnPreferenceClickListener(this);
        this.mDump.setOnPreferenceClickListener(this);
        this.mSystemMonitor = (CheckBoxPreference) findPreference("systemmonitor");
        this.mSystemMonitor.setOnPreferenceClickListener(this);
        this.mBtLog.setOnPreferenceClickListener(this);
        findPreference("key_modem_crash").setOnPreferenceClickListener(this);
        findPreference("key_modem_version").setOnPreferenceClickListener(this);
        findPreference("key_wifi_version").setOnPreferenceClickListener(this);
        findPreference("key_bt_version").setOnPreferenceClickListener(this);
        String type = SystemProperties.get("ro.prj_name", "");
        if (type.equals("15055") || type.equals("14001")) {
            getPreferenceScreen().removePreference(findPreference("key_bt_version"));
        }
    }

    protected void onResume() {
        boolean z = true;
        super.onResume();
        this.mAssert.setChecked(SystemProperties.getBoolean("persist.sys.assert.enable", false));
        this.mQeAssert.setChecked(SystemProperties.getBoolean("persist.sys.assert.panic", false));
        this.mBtLog.setChecked(SystemProperties.getBoolean("persist.sys.btlog.enable", false));
        int mode = SystemProperties.getInt("sys.oem.long_pwr_dump", 0);
        int systemMonitorMode = SystemProperties.getInt("persist.sys.sysdaemon", 0);
        CheckBoxPreference checkBoxPreference = this.mSystemMonitor;
        if (systemMonitorMode != 1) {
            z = false;
        }
        checkBoxPreference.setChecked(z);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        registerReceiver(this.mBroadcastReceiver, filter);
    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.mBroadcastReceiver);
    }

    private void setStringSummary(String preference, String value) {
        try {
            findPreference(preference).setSummary(value);
        } catch (RuntimeException e) {
            findPreference(preference).setSummary(getResources().getString(2131297361));
        }
    }

    private void reboot() {
        new Thread(new Runnable() {
            public void run() {
                Log.i("LogSwitch", "reboot device...");
                ((PowerManager) LogSwitch.this.getSystemService("power")).reboot(null);
            }
        }).start();
    }

    public boolean onPreferenceClick(Preference preference) {
        boolean isChecked;
        int check;
        BluetoothAdapter adapter;
        Intent nextIntent;
        Log.i("LogSwitch", "onPreferenceClick = " + preference.getKey());
        if (preference.getKey().equals("assert")) {
            isChecked = this.mAssert.isChecked();
            Log.d("LogSwitch", "ASSERT_KEY--isChecked=" + isChecked);
            if (isChecked != SystemProperties.getBoolean("persist.sys.assert.enable", false)) {
                SystemProperties.set("persist.sys.assert.enable", Boolean.toString(isChecked));
                if (isChecked) {
                    SystemProperties.set("persist.sys.assert.panic", Boolean.toString(!isChecked));
                    this.mQeAssert.setEnabled(!isChecked);
                    this.mQeAssert.setChecked(!isChecked);
                    reboot();
                }
            }
        }
        if (preference.getKey().equals("qeassert")) {
            isChecked = this.mQeAssert.isChecked();
            Log.d("LogSwitch", "QEASSERT_KEY--isChecked=" + isChecked);
            if (isChecked != SystemProperties.getBoolean("persist.sys.assert.panic", false)) {
                SystemProperties.set("persist.sys.assert.panic", Boolean.toString(isChecked));
                if (isChecked) {
                    SystemProperties.set("persist.sys.assert.enable", Boolean.toString(!isChecked));
                    this.mAssert.setEnabled(!isChecked);
                    this.mAssert.setChecked(!isChecked);
                    reboot();
                }
            }
        }
        if (preference.getKey().equals("dump")) {
            isChecked = this.mDump.isChecked();
            if (isChecked) {
            }
            Log.d("LogSwitch", "DUMP_KEY--isChecked=" + isChecked + "/mode:" + SystemProperties.getInt("sys.oem.long_pwr_dump", 0));
        }
        if (preference.getKey().equals("systemmonitor")) {
            int mode;
            isChecked = this.mSystemMonitor.isChecked();
            check = isChecked ? 1 : 0;
            mode = SystemProperties.getInt("persist.sys.sysdaemon", 0);
            Log.d("LogSwitch", "SYSTEM_MONITOR_KEY --SYSTEM_MONITOR_KEY = " + isChecked + "/ mode:" + mode);
            if (check != mode) {
                SystemProperties.set("persist.sys.sysdaemon", Integer.toString(check));
            }
        }
        if (preference.getKey().equals("btstacklog")) {
            check = this.mBtLog.isChecked() ? 1 : 0;
            mode = SystemProperties.getInt("persist.sys.btlog.enable", 0);
            Log.d("LogSwitch", "BTLog_KEY--mode=" + mode);
            Log.d("LogSwitch", "BTLog_KEY--isChecked=" + Integer.toString(check));
            if (check != mode) {
                SystemProperties.set("persist.sys.btlog.enable", Integer.toString(check));
                adapter = BluetoothAdapter.getDefaultAdapter();
                if (adapter.isEnabled()) {
                    Log.d("LogSwitch", "BTLog_KEY--disable =" + String.valueOf(adapter.disable()));
                }
            }
        }
        if (preference.getKey().equals("key_modem_crash")) {
            new Builder(this).setTitle(2131297496).setPositiveButton(17039370, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    LogSwitch.this.startActivity(new Intent("com.android.engineeringmode.qualcomm.LogSwitchExtCrash"));
                }
            }).setNegativeButton(17039360, null).show();
        }
        if (preference.getKey().equals("key_modem_version")) {
            startActivityForResult(new Intent("com.android.engineeringmode.qualcomm.LogSwitchExtVer"), 1);
        }
        if (preference.getKey().equals("key_wifi_version")) {
            int failCount = 0;
            String wifiVersion = "";
            for (String wifiVersion2 : WIFI_VERSION_PATH) {
                wifiVersion = getWifiVersion(wifiVersion2);
                Log.d("LogSwitch", "wifi version = " + wifiVersion);
                if (!wifiVersion.equals("")) {
                    break;
                }
                failCount++;
            }
            if (failCount >= WIFI_VERSION_PATH.length) {
                wifiVersion = getString(2131297435);
            }
            findPreference("key_wifi_version").setSummary(wifiVersion);
        }
        if (preference.getKey().equals("key_bt_version")) {
            this.btVersion = SystemProperties.get("bluetooth.fwversion", "");
            Log.d("LogSwitch", "btVersion = " + this.btVersion);
            if (this.btVersion.equals("")) {
                adapter = BluetoothAdapter.getDefaultAdapter();
                if (!(adapter.isEnabled() || this.mBtdisable)) {
                    this.mBtdisable = true;
                    Log.d("LogSwitch", "BTLog_KEY--disable =" + String.valueOf(adapter.enable()));
                }
                this.btVersion = getString(2131297438);
            }
            findPreference("key_bt_version").setSummary(this.btVersion);
        }
        if (preference.getKey().equals("key_oppo_logkit")) {
            nextIntent = new Intent();
            nextIntent.setAction("android.intent.action.MAIN");
            nextIntent.setComponent(new ComponentName("com.oem.oemlogkit", "com.oem.oemlogkit.OEMLogKitMainActivity"));
            nextIntent.setFlags(268435456);
            if (getPackageManager().resolveActivity(nextIntent, 0) != null) {
                startActivity(nextIntent);
            }
        }
        if (preference.getKey().equals("key_oneclick_logkit")) {
            nextIntent = new Intent();
            nextIntent.setComponent(new ComponentName("com.oem.oemlogkit", "com.oem.oemlogkit.OneClickLogKitMainActivity"));
            nextIntent.setFlags(268435456);
            if (getPackageManager().resolveActivity(nextIntent, 0) != null) {
                startActivity(nextIntent);
            }
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == -1 && data != null) {
            setStringSummary("key_modem_version", data.getStringExtra("ver"));
        }
    }

    private String getWifiVersion(String path) {
        IOException e;
        Throwable th;
        String versionString = "";
        File file = new File(path);
        BufferedReader bufferedReader = null;
        if (!file.exists()) {
            return versionString;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (true) {
                try {
                    String tempString = reader.readLine();
                    if (tempString == null) {
                        break;
                    }
                    versionString = versionString + tempString;
                } catch (IOException e2) {
                    e = e2;
                    bufferedReader = reader;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = reader;
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e3) {
                }
                return versionString;
            }
        } catch (IOException e4) {
            e = e4;
            try {
                e.printStackTrace();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e5) {
                    }
                }
                return versionString;
            } catch (Throwable th3) {
                th = th3;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e6) {
                    }
                }
                throw th;
            }
        }
        return versionString;
    }
}
