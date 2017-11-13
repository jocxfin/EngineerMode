package com.android.engineeringmode.audiotest;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.engineeringmode.CallManager;
import com.android.engineeringmode.Log;

import java.io.IOException;
import java.util.Calendar;

public class ReceiverTest extends Activity implements OnClickListener {
    private AudioManager mAudioManager = null;
    private int mCommunicationForuse = 0;
    private MediaPlayer mMediaPlayer;
    private int mSoundEffectOn = 1;
    private int mSoundId = 0;
    private SoundPool mSoundPool = null;
    private int mSoundScreenLockOn = 1;
    private int mStreamVolume = -1;
    private TelephonyManager mTelephonyManager;
    private boolean mbIsOpened = false;
    private Button mbtnOpen = null;
    private boolean speaker_state = false;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setTitle(2131297024);
        setContentView(2130903191);
        this.mTelephonyManager = CallManager.getPhoneInterface(this);
        this.mbtnOpen = (Button) findViewById(2131493505);
        this.mbtnOpen.setOnClickListener(this);
        this.mAudioManager = (AudioManager) getSystemService("audio");
        this.speaker_state = this.mAudioManager.isSpeakerphoneOn();
        Log.i("ReceiverTest", "onCreate speaker_state :" + this.speaker_state);
        try {
            this.mSoundEffectOn = System.getInt(getContentResolver(), "sound_effects_enabled");
            Log.i("ReceiverTest", "mSoundEffectOn=" + this.mSoundEffectOn);
        } catch (SettingNotFoundException e) {
            Log.e("ReceiverTest", "get SOUND_EFFECTS_ENABLED state error");
        }
        try {
            this.mSoundScreenLockOn = System.getInt(getContentResolver(), "lockscreen_sounds_enabled");
            Log.i("ReceiverTest", "mSoundScreenLockOn=" + this.mSoundScreenLockOn);
        } catch (SettingNotFoundException e2) {
            Log.e("ReceiverTest", "get SOUND_LOCKSCREEN_ENABLED state error");
        }
        System.putInt(getContentResolver(), "sound_effects_enabled", 0);
        System.putInt(getContentResolver(), "lockscreen_sounds_enabled", 0);
        this.mMediaPlayer = new MediaPlayer();
    }

    public void onClick(View v) {
        if (v == this.mbtnOpen) {
            if (this.mbIsOpened) {
                Log.i("ReceiverTest", "end test");
                stopSound();
                Log.i("ReceiverTest", "--------");
                this.mbtnOpen.setText(2131296274);
                this.mbIsOpened = false;
            } else {
                Calendar now = Calendar.getInstance();
                String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--ReceiverTest--" + "has tested";
                Log.i("ReceiverTest", "start test");
                playSound();
                Log.i("ReceiverTest", "----------");
                this.mbtnOpen.setText(2131296275);
                this.mbIsOpened = true;
            }
        }
    }

    private void playSound() {
        Log.i("ReceiverTest", "playSound");
        AssetFileDescriptor assetFileDescriptor = null;
        try {
            setVolumeToMax();
            if (this.mMediaPlayer == null) {
                this.mMediaPlayer = new MediaPlayer();
            } else {
                this.mMediaPlayer.reset();
            }
            assetFileDescriptor = getResources().openRawResourceFd(2131034116);
            this.mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getDeclaredLength());
            this.mMediaPlayer.setAudioStreamType(0);
            this.mMediaPlayer.prepare();
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
            this.mMediaPlayer.setLooping(true);
            this.mAudioManager.setParameters("EngineeringMod_ReceiverTest_Enable=true");
            if (this.speaker_state) {
                this.mAudioManager.setSpeakerphoneOn(false);
            }
            this.mMediaPlayer.start();
            if (assetFileDescriptor != null) {
                try {
                    assetFileDescriptor.close();
                } catch (IOException e) {
                    Log.e("ReceiverTest", "fail to close fd :" + assetFileDescriptor);
                }
            }
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            if (assetFileDescriptor != null) {
                try {
                    assetFileDescriptor.close();
                } catch (IOException e3) {
                    Log.e("ReceiverTest", "fail to close fd :" + assetFileDescriptor);
                }
            }
        } catch (IllegalStateException e4) {
            e4.printStackTrace();
            if (assetFileDescriptor != null) {
                try {
                    assetFileDescriptor.close();
                } catch (IOException e5) {
                    Log.e("ReceiverTest", "fail to close fd :" + assetFileDescriptor);
                }
            }
        } catch (IOException e6) {
            e6.printStackTrace();
            if (assetFileDescriptor != null) {
                try {
                    assetFileDescriptor.close();
                } catch (IOException e7) {
                    Log.e("ReceiverTest", "fail to close fd :" + assetFileDescriptor);
                }
            }
        } catch (Throwable th) {
            if (assetFileDescriptor != null) {
                try {
                    assetFileDescriptor.close();
                } catch (IOException e8) {
                    Log.e("ReceiverTest", "fail to close fd :" + assetFileDescriptor);
                }
            }
        }
    }

    private void setVolumeToMax() {
        this.mStreamVolume = AudioSystem.getStreamVolumeIndex(0, 1);
        int maxStreamVolume = this.mAudioManager.getStreamMaxVolume(0);
        AudioSystem.setStreamVolumeIndex(0, maxStreamVolume, 1);
        Log.d("ReceiverTest", "mStreamVolume: " + this.mStreamVolume + ", maxStreamVolume: " + maxStreamVolume);
    }

    private void restoreVolume() {
        if (this.mStreamVolume != -1) {
            AudioSystem.setStreamVolumeIndex(0, this.mStreamVolume, 1);
            this.mStreamVolume = -1;
        }
    }

    private void stopSound() {
        Log.i("ReceiverTest", "stopSound");
        try {
            restoreVolume();
            this.mAudioManager.setParameters("EngineeringMod_ReceiverTest_Enable=false");
            if (this.speaker_state) {
                this.mAudioManager.setSpeakerphoneOn(true);
            }
            this.mMediaPlayer.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            if (this.mMediaPlayer != null) {
                this.mMediaPlayer.release();
                finish();
            }
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.mbIsOpened) {
            this.mbtnOpen.setText(2131296275);
        } else {
            this.mbtnOpen.setText(2131296274);
        }
        System.putInt(getContentResolver(), "sound_effects_enabled", 0);
        System.putInt(getContentResolver(), "lockscreen_sounds_enabled", 0);
    }

    protected void onPause() {
        super.onPause();
        if (this.mSoundEffectOn == 1) {
            Log.i("ReceiverTest", "onPause mSoundEffectOn:" + this.mSoundEffectOn);
        } else if (this.mSoundEffectOn == 0) {
            Log.i("ReceiverTest", "onPause mSoundEffectOn:" + this.mSoundEffectOn);
        }
        if (this.mSoundScreenLockOn == 1) {
            System.putInt(getContentResolver(), "lockscreen_sounds_enabled", 1);
            Log.i("ReceiverTest", "onPause mSoundScreenLockOn:" + this.mSoundScreenLockOn);
        } else if (this.mSoundScreenLockOn == 0) {
            System.putInt(getContentResolver(), "lockscreen_sounds_enabled", 0);
            Log.i("ReceiverTest", "onPause mSoundScreenLockOn:" + this.mSoundScreenLockOn);
        }
        stopSound();
        this.mbIsOpened = false;
    }

    protected void onDestroy() {
        Log.i("ReceiverTest", "onDestroy");
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.release();
        }
        super.onDestroy();
    }
}
