package com.android.engineeringmode.manualtest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.engineeringmode.KeepScreenOnPreActivity;
import com.android.engineeringmode.call.CallTestService;
import com.android.engineeringmode.call.CallTestService.LocalBinder;

public class CallTest extends KeepScreenOnPreActivity {
    private static String callNumber;
    private ServiceConnection conn = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            Log.i("CallTest", "onServiceConnected");
            CallTest.this.mCallTestService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("CallTest", "onServiceConnected");
            CallTest.this.mCallTestService = ((LocalBinder) service).getService();
            CallTest.this.mCallTestService.setAutoTestMode(false);
            if (CallTest.this.mTelephonyManager.getSimState() == 5) {
                CallTest.this.mCall10086.setEnabled(true);
                CallTest.this.mCall10010.setEnabled(true);
                CallTest.this.mCall10000.setEnabled(true);
            }
            CallTest.this.mCall112.setEnabled(true);
        }
    };
    private volatile boolean isCalling = false;
    private AudioManager mAudioManager = null;
    private Preference mCall10000 = null;
    private Preference mCall10010 = null;
    private Preference mCall10086 = null;
    private Preference mCall112 = null;
    private CallTestService mCallTestService = null;
    private Preference mCurPref = null;
    private MediaPlayer mMediaPlayer = null;
    private CheckBoxPreference mSignalTest = null;
    private TelephonyManager mTelephonyManager;

    private void palyAudio(int resId) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        this.mMediaPlayer = MediaPlayer.create(this, resId);
        this.mMediaPlayer.setAudioStreamType(3);
        this.mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (CallTest.this.mSignalTest != null) {
                    CallTest.this.mSignalTest.setChecked(false);
                }
            }
        });
        this.mMediaPlayer.start();
        if (this.mSignalTest != null) {
            this.mSignalTest.setSummary(2131297186);
        }
    }

    private void stopAudio() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        if (this.mSignalTest != null) {
            this.mSignalTest.setChecked(false);
            this.mSignalTest.setSummary(null);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2130968602);
        this.mCall10086 = findPreference("10086");
        this.mCall10010 = findPreference("10010");
        this.mCall10000 = findPreference("10000");
        this.mCall112 = findPreference("112");
        this.mTelephonyManager = (TelephonyManager) getSystemService("phone");
        bindService();
        String product_name = SystemProperties.get("ro.product.name", "Spirng");
        if ("R8207".equals(product_name) || "R8200".equals(product_name) || "R8205".equals(product_name) || "R8206".equals(product_name) || "R8201".equals(product_name)) {
            this.mSignalTest = (CheckBoxPreference) findPreference("20_4k");
            this.mAudioManager = (AudioManager) getSystemService("audio");
            return;
        }
        getPreferenceScreen().removePreference(findPreference("20_4k"));
    }

    private void bindService() {
        bindService(new Intent(this, CallTestService.class), this.conn, 1);
    }

    protected void onRestart() {
        super.onRestart();
        Log.w("CallTest", "onRestart");
    }

    protected void onResume() {
        super.onResume();
        Log.w("CallTest", "onResume");
        if (this.mAudioManager != null) {
            Log.d("Liangqiang", "onResume============");
            this.mAudioManager.setMode(2);
        }
    }

    protected void onPause() {
        super.onPause();
        Log.w("CallTest", "onPause");
        if (this.mAudioManager != null) {
            Log.d("Liangqiang", "onPause============");
            this.mAudioManager.setMode(0);
        }
        stopAudio();
    }

    protected void onDestroy() {
        super.onDestroy();
        unbindService(this.conn);
        Log.w("CallTest", "onDestroy");
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if ("20_4k".equals(preference.getKey())) {
            if (((CheckBoxPreference) preference).isChecked()) {
                palyAudio(2131034112);
                this.mCall10086.setEnabled(false);
                this.mCall10010.setEnabled(false);
                this.mCall10000.setEnabled(false);
                this.mCall112.setEnabled(false);
            } else {
                stopAudio();
                if (this.mTelephonyManager.getSimState() == 5) {
                    this.mCall10086.setEnabled(true);
                    this.mCall10010.setEnabled(true);
                    this.mCall10000.setEnabled(true);
                }
                this.mCall112.setEnabled(true);
            }
            return true;
        }
        this.mCurPref = preference;
        callNumber = this.mCurPref.getKey();
        Log.i("CallTest", "callNumber = " + callNumber);
        this.mCallTestService.dialNumber(callNumber);
        return false;
    }
}
