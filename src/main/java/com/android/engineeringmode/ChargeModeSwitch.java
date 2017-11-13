package com.android.engineeringmode;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemProperties;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.autotest.CameraPreview;
import com.android.engineeringmode.autotest.CameraPreview.OnOpenCameraFailedListener;

import java.io.IOException;

public class ChargeModeSwitch extends Activity {
    private static final long[] sVibratePattern = new long[]{500, 1000, 500};
    private final int CHARGE_MODE = 1;
    private final String CHARGE_SWITCH_PROPERTY = "sys.fastcharger";
    private final int CHARGING_TARGET_BATTERY_LEVEL = 70;
    private final int DISCHARGE_MODE = 2;
    private final int DISCHARGING_TARGET_BATTERY_LEVEL = 70;
    private int MAX_BRIGHTNESS_LEVEL;
    private int MIN_BRIGHTNESS_LEVEL;
    private final int NORMAL_MODE = 0;
    private final int RE_CHARGING_TARGET_BATTERY_LEVEL = 65;
    private boolean camera_opened = false;
    private boolean charge_done = false;
    private int charge_mode = 0;
    private Button charge_mode_btn;
    private Button discharge_mode_btn;
    private AudioManager mAudioManager;
    private int mBatteryLevel = 0;
    private BroadcastReceiver mBatteryStateChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.BATTERY_CHANGED")) {
                ChargeModeSwitch.this.mBatteryLevel = intent.getIntExtra("level", 0);
                ChargeModeSwitch.this.mPlugType = intent.getIntExtra("plugged", 0);
                if (ChargeModeSwitch.this.charge_mode == 1) {
                    if (!ChargeModeSwitch.this.charge_done && ChargeModeSwitch.this.mBatteryLevel >= 70) {
                        ChargeModeSwitch.this.charge_done = true;
                        ChargeModeSwitch.this.setCurrentWindowBrightness(-ChargeModeSwitch.this.MAX_BRIGHTNESS_LEVEL);
                        ChargeModeSwitch.this.summary_tv.setText(ChargeModeSwitch.this.getString(2131297533, new Object[]{Integer.valueOf(ChargeModeSwitch.this.mBatteryLevel)}));
                        ChargeModeSwitch.this.result_layout.setBackgroundColor(-16711936);
                    }
                    if (ChargeModeSwitch.this.charge_done && ChargeModeSwitch.this.mBatteryLevel <= 65) {
                        ChargeModeSwitch.this.charge_done = false;
                        ChargeModeSwitch.this.enableCharge();
                        ChargeModeSwitch.this.result_layout.setBackgroundColor(-16777216);
                    }
                    if (ChargeModeSwitch.this.charge_done && ChargeModeSwitch.this.mPlugType != 0) {
                        ChargeModeSwitch.this.disableCharge();
                    }
                    if (!ChargeModeSwitch.this.charge_done) {
                        if (ChargeModeSwitch.this.mPlugType == 0) {
                            ChargeModeSwitch.this.setCurrentWindowBrightness(-ChargeModeSwitch.this.MAX_BRIGHTNESS_LEVEL);
                            ChargeModeSwitch.this.summary_tv.setText(2131297534);
                        } else {
                            if (ChargeModeSwitch.this.mLastPlugType == 0) {
                                ChargeModeSwitch.this.enableCharge();
                            }
                            ChargeModeSwitch.this.setCurrentWindowBrightness(ChargeModeSwitch.this.MIN_BRIGHTNESS_LEVEL);
                            ChargeModeSwitch.this.summary_tv.setText(ChargeModeSwitch.this.getString(2131297535, new Object[]{Integer.valueOf(ChargeModeSwitch.this.mBatteryLevel)}));
                        }
                    }
                } else if (ChargeModeSwitch.this.charge_mode == 2) {
                    if (ChargeModeSwitch.this.mPlugType != 0) {
                        ChargeModeSwitch.this.summary_tv.setText(2131297538);
                    } else {
                        ChargeModeSwitch.this.summary_tv.setText(ChargeModeSwitch.this.getString(2131297537, new Object[]{Integer.valueOf(ChargeModeSwitch.this.mBatteryLevel)}));
                        ChargeModeSwitch.this.result_layout.setBackgroundColor(-16777216);
                        if (ChargeModeSwitch.this.mBatteryLevel <= 70) {
                            ChargeModeSwitch.this.stopAging();
                            ChargeModeSwitch.this.localUnregisterReceiver();
                            ChargeModeSwitch.this.setCurrentWindowBrightness(-ChargeModeSwitch.this.MAX_BRIGHTNESS_LEVEL);
                            ChargeModeSwitch.this.result_layout.setBackgroundColor(-16711936);
                            ChargeModeSwitch.this.summary_tv.setText(ChargeModeSwitch.this.getString(2131297536, new Object[]{Integer.valueOf(ChargeModeSwitch.this.mBatteryLevel)}));
                            ChargeModeSwitch.this.discharge_mode_btn.setText(2131297530);
                            ChargeModeSwitch.this.charge_mode = 0;
                        }
                    }
                }
                ChargeModeSwitch.this.mLastPlugType = ChargeModeSwitch.this.mPlugType;
            }
        }
    };
    private CameraPreview mCameraPreview;
    private WakeLock mCpuWakeLock;
    private WakeLock mFullWakeLock;
    private int mLastPlugType = 0;
    private MediaPlayer mMediaPlayer = null;
    private int mPlugType = 0;
    private PowerManager mPowerManager;
    private int mStreaMVolume = -1;
    private Vibrator mVibrator = null;
    private boolean receiver_registered = false;
    private LinearLayout result_layout;
    private TextView summary_tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903074);
        setTitle(2131297528);
        this.charge_mode_btn = (Button) findViewById(2131493003);
        this.charge_mode_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ChargeModeSwitch.this.charge_mode != 1) {
                    ChargeModeSwitch.this.resetChargeMode();
                    ChargeModeSwitch.this.enableCharge();
                    ChargeModeSwitch.this.charge_mode_btn.setText(2131297531);
                    ChargeModeSwitch.this.charge_mode = 1;
                    ChargeModeSwitch.this.localRegisterReceiver();
                    return;
                }
                ChargeModeSwitch.this.resetChargeMode();
            }
        });
        this.discharge_mode_btn = (Button) findViewById(2131493004);
        this.discharge_mode_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ChargeModeSwitch.this.charge_mode != 2) {
                    ChargeModeSwitch.this.resetChargeMode();
                    ChargeModeSwitch.this.setCurrentWindowBrightness(ChargeModeSwitch.this.MAX_BRIGHTNESS_LEVEL);
                    ChargeModeSwitch.this.discharge_mode_btn.setText(2131297532);
                    ChargeModeSwitch.this.charge_mode = 2;
                    ChargeModeSwitch.this.startAging();
                    ChargeModeSwitch.this.localRegisterReceiver();
                    return;
                }
                ChargeModeSwitch.this.resetChargeMode();
            }
        });
        this.summary_tv = (TextView) findViewById(2131493006);
        this.mCameraPreview = (CameraPreview) findViewById(2131493007);
        this.mCameraPreview.setDevice(0);
        this.mCameraPreview.setOnOpenCameraFailedListener(new OnOpenCameraFailedListener() {
            public void onFail() {
                ChargeModeSwitch.this.camera_opened = false;
                Toast.makeText(ChargeModeSwitch.this, "Can't open the Camera", 1).show();
            }
        });
        this.result_layout = (LinearLayout) findViewById(2131493005);
        this.mVibrator = (Vibrator) getSystemService("vibrator");
        this.mAudioManager = (AudioManager) getSystemService("audio");
        this.mPowerManager = (PowerManager) getSystemService("power");
        this.MIN_BRIGHTNESS_LEVEL = this.mPowerManager.getMinimumScreenBrightnessSetting();
        this.MAX_BRIGHTNESS_LEVEL = this.mPowerManager.getMaximumScreenBrightnessSetting();
        this.mFullWakeLock = this.mPowerManager.newWakeLock(26, "ChargeModeSwitch");
        this.mCpuWakeLock = this.mPowerManager.newWakeLock(1, "ChargeModeSwitch");
        if (this.mCpuWakeLock != null && !this.mCpuWakeLock.isHeld()) {
            this.mCpuWakeLock.acquire();
        }
    }

    private void setCurrentWindowBrightness(int level) {
        LayoutParams localLayoutParams = getWindow().getAttributes();
        localLayoutParams.screenBrightness = ((float) level) / 255.0f;
        getWindow().setAttributes(localLayoutParams);
    }

    private void startAging() {
        if (!(this.mFullWakeLock == null || this.mFullWakeLock.isHeld())) {
            this.mFullWakeLock.acquire();
        }
        openCamera();
        this.mVibrator.vibrate(sVibratePattern, 0);
        playRingtone();
    }

    private void stopAging() {
        if (this.mFullWakeLock != null && this.mFullWakeLock.isHeld()) {
            this.mFullWakeLock.release();
        }
        closeCamera();
        this.mVibrator.cancel();
        stopRingtone();
    }

    private void openCamera() {
        if (!this.camera_opened) {
            Log.i("ChargeModeSwitch", "openCamera");
            this.mCameraPreview.setFlashMode("torch");
            this.mCameraPreview.open();
            this.camera_opened = true;
        }
    }

    private void closeCamera() {
        if (this.camera_opened) {
            Log.i("ChargeModeSwitch", "closeCamera");
            this.mCameraPreview.setFlashMode("off");
            this.mCameraPreview.close();
            this.camera_opened = false;
        }
    }

    private void localRegisterReceiver() {
        if (!this.receiver_registered) {
            Log.i("ChargeModeSwitch", "localRegisterReceiver");
            registerReceiver(this.mBatteryStateChangeReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        }
    }

    private void localUnregisterReceiver() {
        if (this.receiver_registered) {
            Log.i("ChargeModeSwitch", "localUnregisterReceiver");
            unregisterReceiver(this.mBatteryStateChangeReceiver);
        }
    }

    private void resetChargeMode() {
        localUnregisterReceiver();
        setCurrentWindowBrightness(-this.MAX_BRIGHTNESS_LEVEL);
        stopAging();
        enableCharge();
        this.summary_tv.setText("");
        this.result_layout.setBackgroundColor(-16777216);
        this.charge_mode_btn.setText(2131297529);
        this.discharge_mode_btn.setText(2131297530);
        this.charge_done = false;
        this.charge_mode = 0;
    }

    private void disableCharge() {
        if (SystemProperties.getBoolean("sys.fastcharger", false)) {
            SystemProperties.set("sys.fastcharger", "false");
            Log.i("ChargeModeSwitch", "disableCharge");
        }
    }

    private void enableCharge() {
        if (!SystemProperties.getBoolean("sys.fastcharger", false)) {
            SystemProperties.set("sys.fastcharger", "true");
            Log.i("ChargeModeSwitch", "enableCharge");
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.charge_mode != 0) {
            resetChargeMode();
        }
        if (this.mCpuWakeLock != null && this.mCpuWakeLock.isHeld()) {
            this.mCpuWakeLock.release();
        }
    }

    private void setVolumeToMax() {
        if (this.mStreaMVolume == -1) {
            this.mStreaMVolume = this.mAudioManager.getStreamVolume(3);
            this.mAudioManager.setStreamVolume(3, this.mAudioManager.getStreamMaxVolume(3), 0);
        }
    }

    private void restoreVolume() {
        if (this.mStreaMVolume != -1) {
            this.mAudioManager.setStreamVolume(3, this.mStreaMVolume, 4);
            this.mStreaMVolume = -1;
        }
    }

    private void playRingtone() {
        setVolumeToMax();
        this.mMediaPlayer = MediaPlayer.create(this, RingtoneManager.getActualDefaultRingtoneUri(this, 1));
        this.mMediaPlayer.setLooping(true);
        try {
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
            this.mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        this.mMediaPlayer.start();
    }

    private void stopRingtone() {
        if (this.mMediaPlayer != null) {
            try {
                this.mMediaPlayer.stop();
                this.mMediaPlayer.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                this.mMediaPlayer.release();
            } catch (Throwable th) {
                this.mMediaPlayer.release();
                this.mMediaPlayer = null;
            }
            this.mMediaPlayer = null;
        }
        restoreVolume();
    }

    protected void onStop() {
        super.onStop();
        if (this.charge_mode == 2) {
            resetChargeMode();
        }
        finish();
    }
}
