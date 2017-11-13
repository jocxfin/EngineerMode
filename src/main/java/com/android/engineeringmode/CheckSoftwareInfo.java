package com.android.engineeringmode;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import android.util.Log;

import com.android.engineeringmode.util.ExternFunction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckSoftwareInfo extends PreferenceActivity {
    private ExternFunction mExFunction;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.d("CheckSoftwareInfo", "handleMessage");
                CheckSoftwareInfo.this.pcbString = CheckSoftwareInfo.this.mExFunction.getPCBNumber();
                CheckSoftwareInfo.this.setStringSummary("key_pcb_number", CheckSoftwareInfo.this.pcbString);
            }
        }
    };
    private String pcbString = "";
    private String region = SystemProperties.get("persist.sys.oem.region", "CN");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2130968588);
        setTitle(2131296800);
        this.mExFunction = new ExternFunction(this);
        this.mExFunction.registerOnServiceConnected(this.mHandler, 1, null);
        Calendar now = Calendar.getInstance();
        String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--CheckSoftwareInfo--" + "has entered it";
        setValueSummary("device_name", "ro.display.series");
        setValueSummary("device_model", "ro.product.model");
        setStringSummary("firmware_version", VERSION.RELEASE);
        setValueSummary("baseband_version", "gsm.version.baseband");
        setStringSummary("fenzhi", SystemProperties.get("ro.build.soft.version"));
        String kerneVersion = getFormattedKernelVersion().replace(")", "");
        setStringSummary("kernel_version", SystemProperties.get("ro.build.kernel.id"));
        String buildNumber = SystemProperties.get("ro.build.display.id");
        String model = SystemProperties.get("ro.product.model", getResources().getString(2131296815));
        if (!this.region.equals("CN")) {
            buildNumber = buildNumber.replace("EX", getCarrierVersion());
        }
        if (SystemProperties.getBoolean("ro.assert.enable", false)) {
            buildNumber = buildNumber + "_Debug";
        }
        setStringSummary("build_number", buildNumber);
        if (this.region.equals("CN")) {
            setValueSummary("rom_version", "ro.rom.version");
        } else {
            setStringSummary("rom_version", "Oxygen OS");
        }
        setBranch("key_branch", "ro.product.model");
        setValueSummary("branch_version", "ro.xxversion");
        setStringSummary("build_time", getBuildTime());
        setStringSummary("key_pcb_number", this.pcbString);
        String otaVersion = SystemProperties.get("ro.build.version.ota");
        if (!this.region.equals("CN")) {
            otaVersion = otaVersion.replace("EX", getCarrierVersion());
        }
        if (!TextUtils.isEmpty(otaVersion)) {
            setStringSummary("key_ota_version", otaVersion);
        }
    }

    protected void onDestroy() {
        this.mExFunction.unregisterOnServiceConnected(this.mHandler);
        this.mExFunction.dispose();
        super.onDestroy();
    }

    private String getBuildTime() {
        String strBuildTime = SystemProperties.get("ro.build.date.YmdHM");
        StringBuilder rs = new StringBuilder();
        if (strBuildTime.length() >= 12) {
            rs.append(strBuildTime.substring(0, 4));
            rs.append("/");
            rs.append(strBuildTime.substring(4, 6));
            rs.append("/");
            rs.append(strBuildTime.substring(6, 8));
            rs.append(" ");
            rs.append(strBuildTime.substring(8, 10));
            rs.append(":");
            rs.append(strBuildTime.substring(10, 12));
        }
        return rs.toString();
    }

    private String getFormattedKernelVersion() {
        BufferedReader reader;
        String unKnown = getResources().getString(2131296815);
        try {
            reader = new BufferedReader(new FileReader("/proc/version"), 256);
            String procVersionStr = reader.readLine();
            reader.close();
            String PROC_VERSION_REGEX = "\\w+\\s+\\w+\\s+([^\\s]+)\\s+\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+([^\\s]+)\\s+(?:PREEMPT\\s+)?(.+)";
            Matcher m = Pattern.compile("\\w+\\s+\\w+\\s+([^\\s]+)\\s+\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+([^\\s]+)\\s+(?:PREEMPT\\s+)?(.+)").matcher(procVersionStr);
            if (!m.matches()) {
                Log.e("CheckSoftwareInfo", "Regex did not match on /proc/version: " + procVersionStr);
                return unKnown;
            } else if (m.groupCount() < 4) {
                Log.e("CheckSoftwareInfo", "Regex match on /proc/version only returned " + m.groupCount() + " groups");
                return unKnown;
            } else {
                String strRlt;
                String strTemp = new StringBuilder(m.group(1)).append("\n").append(m.group(2)).append(" ").append(m.group(3)).append("\n").append(m.group(4)).toString();
                int index = strTemp.indexOf("-perf");
                if (-1 != index) {
                    strRlt = strTemp.substring(0, index);
                } else {
                    strRlt = strTemp + "-" + m.group(3);
                }
                if (!(strRlt == null || -1 == strRlt.indexOf("-svn"))) {
                    strRlt = strRlt.substring(0, strRlt.indexOf("svn") - 1);
                }
                return strRlt;
            }
        } catch (IOException e) {
            Log.e("CheckSoftwareInfo", "IO Exception when getting kernel version for Device Info screen", e);
            return unKnown;
        } catch (Exception e2) {
            Log.e("CheckSoftwareInfo", "Exception when getting kernel version for Device Info screen", e2);
            return unKnown;
        } catch (Throwable th) {
            reader.close();
        }
    }

    private void setStringSummary(String preference, String value) {
        try {
            findPreference(preference).setSummary(value);
        } catch (RuntimeException e) {
            findPreference(preference).setSummary(getResources().getString(2131296815));
        }
    }

    private void setBranch(String preference, String property) {
        try {
            findPreference(preference).setSummary(SystemProperties.get(property, getResources().getString(2131296815)));
        } catch (RuntimeException e) {
            Log.d("CheckSoftwareInfo", "setValueSummary(), exception happen, preference = " + preference);
            e.printStackTrace();
        }
    }

    private void setValueSummary(String preference, String property) {
        try {
            findPreference(preference).setSummary(SystemProperties.get(property, getResources().getString(2131296815)));
        } catch (RuntimeException e) {
            Log.d("CheckSoftwareInfo", "setValueSummary(), exception happen, preference = " + preference);
            e.printStackTrace();
        }
    }

    private String getCarrierVersion() {
        String carrierVersion = SystemProperties.get("ro.oppo.operator", "EX");
        if (carrierVersion.equalsIgnoreCase("CHT")) {
            return "TW";
        }
        if (carrierVersion.equalsIgnoreCase("FET")) {
            return "TW-FET";
        }
        if (carrierVersion.equalsIgnoreCase("TWM")) {
            return "TW-TWM";
        }
        if (carrierVersion.equalsIgnoreCase("VBO")) {
            return "TW-VBO";
        }
        if (carrierVersion.equalsIgnoreCase("SINGTEL")) {
            return "SG-SGT";
        }
        if (carrierVersion.equalsIgnoreCase("STARHUB")) {
            return "SG-STH";
        }
        if (carrierVersion.equalsIgnoreCase("M1")) {
            return "SG-M1";
        }
        return carrierVersion;
    }
}
