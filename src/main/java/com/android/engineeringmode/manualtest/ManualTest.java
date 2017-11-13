package com.android.engineeringmode.manualtest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.Log;
import android.view.View;

import com.android.engineeringmode.audioutils.AudioTestUtils;
import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.nvbackup.OemHookManager;
import com.oem.util.Feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ManualTest extends PreferenceActivity implements OnPreferenceChangeListener {
    private static WakeLock sCpuWakeLock;
    private static final long[] sVibratePattern = new long[]{500, 1000, 500};
    private final int EVENT_QCRIL_HOOK_READY = 55;
    private boolean bOtgEnabled = false;
    private String[] granteRruntimePermissions = new String[0];
    private CheckBoxPreference mAnccSwitch;
    private AudioManager mAudioManager = null;
    private Preference mAudioPreference = null;
    private AudioTestUtils mAudioTest;
    private CheckBoxPreference mChkprefVirbate;
    Preference mEchoTestPref;
    private Handler mHandler;
    private boolean mIsAudioFinished = true;
    private boolean mIsNfcDisable = false;
    private MediaPlayer mMediaPlayer;
    Preference mModeTest4;
    Preference mMultimicTestPref;
    private NfcAdapter mNfcAdapter = null;
    Preference mReceiverTestPref;
    private Preference mRingPref = null;
    private int mSoundEffectOn = 1;
    Preference mSpeakerTestPref;
    private Vibrator mVibrator = null;
    private final String[] runtimePermissions = new String[]{"android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR", "android.permission.CAMERA", "android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.GET_ACCOUNTS", "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.RECORD_AUDIO", "android.permission.READ_PHONE_STATE", "android.permission.CALL_PHONE", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "com.android.voicemail.permission.ADD_VOICEMAIL", "android.permission.USE_SIP", "android.permission.PROCESS_OUTGOING_CALLS", "android.permission.BODY_SENSORS", "android.permission.SEND_SMS", "android.permission.RECEIVE_SMS", "android.permission.READ_SMS", "android.permission.RECEIVE_WAP_PUSH", "android.permission.RECEIVE_MMS", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(2130968603);
        Log.d("ManualTest", "ManualTest--onCreate");
        this.mChkprefVirbate = (CheckBoxPreference) findPreference("testvirbate");
        this.mChkprefVirbate.setChecked(false);
        this.mAnccSwitch = (CheckBoxPreference) findPreference("anccswitch");
        this.mAudioManager = (AudioManager) getSystemService("audio");
        this.mReceiverTestPref = findPreference("receiver_test");
        this.mSpeakerTestPref = findPreference("audio_test_1");
        this.mEchoTestPref = findPreference("echo_test");
        this.mMultimicTestPref = findPreference("multimic_test");
        this.mModeTest4 = findPreference("model_test_4");
        this.mAudioTest = new AudioTestUtils(this);
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Light.MAIN_KEY_LIGHT /*1*/:
                        ManualTest.this.mReceiverTestPref.setEnabled(false);
                        ManualTest.this.mEchoTestPref.setEnabled(false);
                        ManualTest.this.mMultimicTestPref.setEnabled(false);
                        ManualTest.this.mModeTest4.setEnabled(false);
                        return;
                    case Light.CHARGE_RED_LIGHT /*2*/:
                        ManualTest.this.mReceiverTestPref.setEnabled(true);
                        ManualTest.this.mEchoTestPref.setEnabled(true);
                        ManualTest.this.mMultimicTestPref.setEnabled(true);
                        ManualTest.this.mModeTest4.setEnabled(true);
                        return;
                    case 55:
                        try {
                            int simLockStatus = OemHookManager.getInstance().getSimLockStatus();
                            if (simLockStatus == 0) {
                                ManualTest.this.setItemSummary("sim_lock", ManualTest.this.getString(2131297546));
                                return;
                            } else if (simLockStatus == 1) {
                                ManualTest.this.setItemSummary("sim_lock", ManualTest.this.getString(2131297545));
                                return;
                            } else {
                                ManualTest.this.setItemSummary("sim_lock", "unknown");
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("ManualTest", "get getSimLockStatus error");
                            return;
                        }
                    default:
                        return;
                }
            }
        };
        sleepForAudiotest();
        try {
            this.mSoundEffectOn = System.getInt(getContentResolver(), "sound_effects_enabled");
            Log.i("ManualTest", "mSoundEffectOn=" + this.mSoundEffectOn);
        } catch (SettingNotFoundException e) {
            Log.e("ManualTest", "get SOUND_EFFECTS_ENABLED state error");
        }
        System.putInt(getContentResolver(), "sound_effects_enabled", 0);
        String status = this.mAudioManager.getParameters("anc_enabled");
        Log.d("ManualTest", "status=" + status);
        if (status.equals("anc_enabled=0")) {
            Log.d("TAG", "status.equals(false)");
            this.mAnccSwitch.setChecked(false);
            this.mAnccSwitch.setSummary(2131297269);
        }
        if (status.equals("anc_enabled=1")) {
            Log.d("TAG", "status.equals(true)");
            this.mAnccSwitch.setChecked(true);
            this.mAnccSwitch.setSummary(2131297268);
        }
        this.mVibrator = (Vibrator) getSystemService("vibrator");
        if (((NfcManager) getSystemService("nfc")) == null) {
            findPreference("nfctest").setEnabled(false);
        }
        if (!new File("/sys/project_info/component_info/Aboard").exists()) {
            removeUnnecessaryPreference("small_board_check_test");
        }
        if ("N5207".equals(SystemProperties.get("ro.product.name", "oem")) || "N5200".equals(SystemProperties.get("ro.product.name", "oem")) || "N5206".equals(SystemProperties.get("ro.product.name", "oem"))) {
            removeUnnecessaryPreference("frontcameratest");
        } else {
            removeUnnecessaryPreference("rotation_component");
            removeUnnecessaryPreference("HallTest");
            removeUnnecessaryPreference("HallCalibrate");
            removeUnnecessaryPreference("fill_falsh");
        }
        if (!Feature.isGpioSwitchSupported(this)) {
            Log.d("ManualTest", "/proc/antenna_switch not exist ,remove it");
            removeUnnecessaryPreference("antenna_switch_test");
        }
        if (getPackageManager().hasSystemFeature("oem.otgSwitch.support")) {
            if (isOtgEnabled()) {
                this.bOtgEnabled = true;
            } else {
                this.bOtgEnabled = false;
                enableOtg(true);
            }
        }
        if (!Feature.isFingerPrintSupported(this)) {
            removeUnnecessaryPreference("finger_print_test");
        }
        if (!Feature.isHeatPipeSupported(this)) {
            removeUnnecessaryPreference("heat_pipe_test");
        }
        if (!Feature.isCoverSupported(this)) {
            removeUnnecessaryPreference("cover_test");
        }
        if (!Feature.isLaserFocusSupported(this)) {
            removeUnnecessaryPreference("laser_focus_test");
        }
        if (!Feature.isNfcSupported(this)) {
            removeUnnecessaryPreference("nfctest");
        }
        if (!Feature.isOpticalStabilizerSupported(this)) {
            removeUnnecessaryPreference("camera_optical_image_stabilizer");
        }
        if (!Feature.isFmRadioSupported(this)) {
            removeUnnecessaryPreference("radiotest");
        }
        if (!Feature.isButtonLightSupported(this)) {
            removeUnnecessaryPreference("back_light");
        }
        if (!Feature.isCameraPdafSupported()) {
            removeUnnecessaryPreference("pdaf_cameeprom_info_pre");
        }
        if (!Feature.isQcomFastchagerSupported(this)) {
            removeUnnecessaryPreference("qcom_fastcharger");
        }
        if (!Feature.isVOOCFastchagerSupported(this)) {
            removeUnnecessaryPreference("fast_charger_test");
        }
        if (!Feature.isRGBSensorSupported(this)) {
            removeUnnecessaryPreference("rgb_sensor_cct_test");
            removeUnnecessaryPreference("rgb_sensor_test");
        }
        if (!Feature.isLinearMotorSupported(this)) {
            removeUnnecessaryPreference("linear_motor_test");
        }
        if (!Feature.isDualLEDSupported(this)) {
            removeUnnecessaryPreference("dual_led_first_calibration");
            removeUnnecessaryPreference("double_flashlight_test");
        }
        if (!Feature.isDualBackCameraSupported(this)) {
            removeUnnecessaryPreference("second_back_camera_test");
            removeUnnecessaryPreference("calibration_takepicture");
        }
        if (Feature.isSmallBoardNotSupported(this)) {
            removeUnnecessaryPreference("small_board_check_test");
        }
        if (!isThreeMicProject().booleanValue()) {
            removeUnnecessaryPreference("third_mic_test");
        }
        for (int i = 13; i <= 20; i++) {
            if (!Feature.isModelTestSupported(i)) {
                removeUnnecessaryPreference("modeltest" + i);
            }
        }
        if (Feature.isSecureBootSupported()) {
            String Secure_Boot = readFileByLines("/sys/project_info/secboot_status");
            if ("0".equals(Secure_Boot) || Secure_Boot == null) {
                setItemSummary("Secure_Boot", getString(2131297546));
            } else {
                setItemSummary("Secure_Boot", getString(2131297545));
            }
        } else {
            removeUnnecessaryPreference("Secure_Boot");
        }
        OemHookManager.getInstance().registerQcRilHookReady(this.mHandler, 55, null);
        Light.setElectric(1, Light.MAIN_KEY_MAX);
        new Thread(new Runnable() {
            public void run() {
                ManualTest.this.grantRuntimepermission();
            }
        }).start();
    }

    private void grantRuntimepermission() {
        List<PackageInfo> pinfo = getPackageManager().getInstalledPackages(4096);
        int i = 0;
        while (i < pinfo.size()) {
            if (((PackageInfo) pinfo.get(i)).requestedPermissions != null && "com.oneplus.camera".equals(((PackageInfo) pinfo.get(i)).packageName)) {
                for (String permissionString : ((PackageInfo) pinfo.get(i)).requestedPermissions) {
                    if (Arrays.asList(this.runtimePermissions).contains(permissionString)) {
                        Log.d("ManualTest", "requestedPermissions: " + permissionString);
                        this.granteRruntimePermissions = (String[]) Arrays.copyOf(this.granteRruntimePermissions, this.granteRruntimePermissions.length + 1);
                        this.granteRruntimePermissions[this.granteRruntimePermissions.length - 1] = permissionString;
                        shellRun("pm grant " + ((PackageInfo) pinfo.get(i)).packageName + " " + permissionString);
                    }
                }
            }
            i++;
        }
    }

    private String shellRun(String command) {
        IOException e;
        InterruptedException e2;
        Throwable th;
        Process process = null;
        BufferedReader bufferedReader = null;
        String result = "";
        try {
            byte[] b = new byte[1024];
            process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (true) {
                try {
                    String line = bufferedReader2.readLine();
                    if (line == null) {
                        break;
                    }
                    Log.e("ManualTest", "line:" + line);
                    result = result + line;
                } catch (IOException e3) {
                    e = e3;
                    bufferedReader = bufferedReader2;
                } catch (InterruptedException e4) {
                    e2 = e4;
                    bufferedReader = bufferedReader2;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = bufferedReader2;
                }
            }
            process.waitFor();
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (IOException e5) {
                }
            }
            if (process != null) {
                process.destroy();
            }
            bufferedReader = bufferedReader2;
        } catch (IOException e6) {
            e = e6;
            e.printStackTrace();
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e7) {
                }
            }
            if (process != null) {
                process.destroy();
            }
            return result;
        } catch (InterruptedException e8) {
            e2 = e8;
            try {
                e2.printStackTrace();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e9) {
                    }
                }
                if (process != null) {
                    process.destroy();
                }
                return result;
            } catch (Throwable th3) {
                th = th3;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e10) {
                    }
                }
                if (process != null) {
                    process.destroy();
                }
                throw th;
            }
        }
        return result;
    }

    private Boolean isThreeMicProject() {
        if (SystemProperties.get("ro.boot.project_name").compareTo("16859") == 0) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    private void removeUnnecessaryPreference(String preference_key) {
        Preference pre_target = findPreference(preference_key);
        if (pre_target != null) {
            getPreferenceScreen().removePreference(pre_target);
        }
    }

    private void setItemSummary(String preference_key, String summary) {
        Preference pre_target = findPreference(preference_key);
        if (pre_target != null) {
            pre_target.setSummary(summary);
        }
    }

    private String readFileByLines(String fileName) {
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
                        Log.e("ManualTest", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("ManualTest", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("ManualTest", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    return tempString;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("ManualTest", "readFileByLines io close exception :" + e122.getMessage());
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
            Log.e("ManualTest", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return tempString;
        }
        return tempString;
    }

    protected void sleepForAudiotest() {
        new Thread(null, new Runnable() {
            public void run() {
                ManualTest.this.mHandler.sendEmptyMessage(1);
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    Log.i("ManualTest", Log.getStackTraceString(e));
                }
                ManualTest.this.mHandler.sendEmptyMessage(2);
            }
        }).start();
    }

    protected void onResume() {
        SystemClock.sleep(200);
        System.putInt(getContentResolver(), "sound_effects_enabled", 0);
        Log.i("ManualTest", "onResume");
        sleepForAudiotest();
        super.onResume();
        this.mMediaPlayer = null;
        this.mAudioPreference = null;
    }

    protected void onPause() {
        Log.i("ManualTest", "onPause");
        if (!this.mIsAudioFinished) {
            this.mAudioTest.setparameter("SpeakerTest=false");
        }
        if (this.mSoundEffectOn == 1) {
            System.putInt(getContentResolver(), "sound_effects_enabled", 1);
            Log.i("ManualTest", "onPause mSoundEffectOn:" + this.mSoundEffectOn);
        } else if (this.mSoundEffectOn == 0) {
            System.putInt(getContentResolver(), "sound_effects_enabled", 0);
            Log.i("ManualTest", "onPause mSoundEffectOn:" + this.mSoundEffectOn);
        }
        super.onPause();
    }

    public void onDestroy() {
        if (!this.mIsAudioFinished) {
            this.mAudioTest.setparameter("SpeakerTest=false");
        }
        if (getPackageManager().hasSystemFeature("oem.otgSwitch.support") && !this.bOtgEnabled) {
            enableOtg(false);
        }
        Light.setElectric(1, 0);
        super.onDestroy();
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference instanceof CheckBoxPreference) {
            CheckBoxPreference chkpref = (CheckBoxPreference) preference;
            if ("anccswitch".equals(chkpref.getKey())) {
                if (chkpref.isChecked()) {
                    this.mAudioManager.setParameters("anc_enabled=true");
                    this.mAnccSwitch.setSummary(2131297268);
                    this.mAnccSwitch.setChecked(true);
                } else {
                    this.mAudioManager.setParameters("anc_enabled=false");
                    this.mAnccSwitch.setSummary(2131297269);
                    this.mAnccSwitch.setChecked(false);
                }
            }
            if ("testvirbate".equals(chkpref.getKey())) {
                if (chkpref.isChecked()) {
                    Log.w("ManualTest", "vibrator is begin!");
                    Calendar now = Calendar.getInstance();
                    String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--ManualTest/vibrate--" + "has entered it";
                    this.mChkprefVirbate.setSummary(2131296743);
                    acquireCpuWakeLock(this);
                    this.mVibrator.vibrate(sVibratePattern, 0);
                } else {
                    Log.i("ManualTest", "stop test");
                    cancelVirbate();
                }
            }
            if ("freepathshot".equals(chkpref.getKey())) {
                if (chkpref.isChecked()) {
                    Log.w("ManualTest", "freepathshot begin!");
                    SystemProperties.set("persist.sys.oppo.freepathshot", "1");
                } else {
                    Log.w("ManualTest", "freepathshot end!");
                    SystemProperties.set("persist.sys.oppo.freepathshot", "0");
                }
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
        if ("audio_test_1".equals(preference.getKey())) {
            this.mAudioPreference = preference;
            if (this.mIsAudioFinished) {
                this.mIsAudioFinished = false;
                this.mAudioTest.setparameter("SpeakerTest=true");
                if (this.mAudioPreference != null) {
                    preference.setSummary(2131297186);
                }
            } else {
                this.mIsAudioFinished = true;
                this.mAudioTest.setparameter("SpeakerTest=false");
                sleepForAudiotest();
                if (this.mAudioPreference != null) {
                    this.mAudioPreference.setSummary(null);
                }
            }
        }
        if ("enter_reboot".equals(preference.getKey())) {
            new Thread(new Runnable() {
                public void run() {
                    ((PowerManager) ManualTest.this.getSystemService("power")).reboot(null);
                }
            }).start();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.w("ManualTest", "ring tone changed, new Uri = " + newValue.toString());
        return false;
    }

    protected void onStop() {
        Log.i("ManualTest", "onStop");
        cancelVirbate();
        super.onStop();
    }

    private void cancelVirbate() {
        this.mVibrator.cancel();
        this.mChkprefVirbate.setSummary("");
        this.mChkprefVirbate.setChecked(false);
        releaseCpuLock();
    }

    public static void requestFoucs(View view) {
    }

    static void acquireCpuWakeLock(Context context) {
        if (sCpuWakeLock == null) {
            sCpuWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(805306394, "Eng");
            sCpuWakeLock.acquire();
        }
    }

    static void releaseCpuLock() {
        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }
    }

    private boolean isOtgEnabled() {
        if (SystemProperties.get("persist.sys.oem.otg_support", "false").equals("true")) {
            return true;
        }
        return false;
    }

    private void enableOtg(boolean enable) {
        SystemProperties.set("persist.sys.oem.otg_support", enable ? "true" : "false");
    }
}
