package com.android.engineeringmode.gps;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.gps.GpsEnvService.MyBinder;

public class GpsEnvTestActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
    private static String TAG = "GpsEnvTestActivity";
    private static CheckBox mBackCameraCheckBox;
    private static CheckBox mFrontCameraCheckBox;
    private static boolean mIsBackCmaeraChecked;
    private static boolean mIsFrontCameraChecked;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 5:
                    GpsEnvTestActivity.this.snr_sv[0] = ((Float) msg.obj).floatValue();
                    GpsEnvTestActivity.this.moff_lcd.setText("LCD OFF SNR VALUE IS = " + GpsEnvTestActivity.this.snr_sv[0]);
                    return;
                case Light.MAIN_KEY_NORMAL /*6*/:
                    GpsEnvTestActivity.this.snr_sv[1] = ((Float) msg.obj).floatValue();
                    GpsEnvTestActivity.this.mback_camera.setText("BACK CAMERA SNR VALUE IS = " + GpsEnvTestActivity.this.snr_sv[1]);
                    return;
                case 7:
                    GpsEnvTestActivity.this.snr_sv[2] = ((Float) msg.obj).floatValue();
                    GpsEnvTestActivity.this.mfront_camera.setText("FRONT CAMERA SNR VALUE IS = " + GpsEnvTestActivity.this.snr_sv[2]);
                    return;
                case 8:
                    GpsEnvTestActivity.this.snr_sv[3] = ((Float) msg.obj).floatValue();
                    GpsEnvTestActivity.this.mvideo.setText("PLAYING VEDIO SNR VALUE IS = " + GpsEnvTestActivity.this.snr_sv[3]);
                    sendMessage(obtainMessage(9));
                    return;
                case 9:
                    GpsEnvTestActivity.this.showResult();
                    return;
                default:
                    return;
            }
        }
    };
    private GpsEnvService mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(GpsEnvTestActivity.TAG, "service connected");
            GpsEnvTestActivity.this.mbound = true;
            GpsEnvTestActivity.this.mService = ((MyBinder) service).getService();
            GpsEnvTestActivity.this.mService.setHandler(GpsEnvTestActivity.this.mHandler);
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d(GpsEnvTestActivity.TAG, "service disconnected");
            GpsEnvTestActivity.this.mbound = false;
        }
    };
    private ToggleButton mToggleButton;
    private TextView mback_camera;
    private boolean mbound;
    private TextView mfront_camera;
    private TextView moff_lcd;
    private TextView mvideo;
    private boolean show_hidden = false;
    private float[] snr_sv = new float[4];
    private TextView success_fail;

    protected void onPause() {
        Log.d(TAG, "onpause()");
        super.onPause();
    }

    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        if (this.show_hidden) {
            this.moff_lcd.setText("LCD OFF SNR VALUE IS = " + this.snr_sv[0]);
            if (mIsBackCmaeraChecked) {
                this.mback_camera.setText("BACK CAMERA SNR VALUE IS = " + this.snr_sv[1]);
                this.mback_camera.setVisibility(0);
            } else {
                this.mback_camera.setVisibility(8);
            }
            if (mIsFrontCameraChecked) {
                this.mfront_camera.setText("FRONT CAMERA SNR VALUE IS = " + this.snr_sv[2]);
                this.mfront_camera.setVisibility(0);
            } else {
                this.mfront_camera.setVisibility(8);
            }
            this.mvideo.setText("PLAYING VEDIO SNR VALUE IS = " + this.snr_sv[3]);
        }
    }

    protected void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        if (this.mbound) {
            unbindService(this.mServiceConnection);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903101);
        this.mToggleButton = (ToggleButton) findViewById(2131493109);
        this.moff_lcd = (TextView) findViewById(2131493110);
        this.mback_camera = (TextView) findViewById(2131493111);
        this.mfront_camera = (TextView) findViewById(2131493112);
        this.mvideo = (TextView) findViewById(2131493113);
        this.success_fail = (TextView) findViewById(2131493114);
        mFrontCameraCheckBox = (CheckBox) findViewById(2131493107);
        mBackCameraCheckBox = (CheckBox) findViewById(2131493108);
        mFrontCameraCheckBox.setOnCheckedChangeListener(this);
        mBackCameraCheckBox.setOnCheckedChangeListener(this);
        this.mToggleButton.setOnClickListener(this);
        mIsFrontCameraChecked = mFrontCameraCheckBox.isChecked();
        mIsBackCmaeraChecked = mBackCameraCheckBox.isChecked();
        getWindow().addFlags(4718592);
        if (this.mService == null) {
            Intent intent = new Intent();
            intent.setClass(this, GpsEnvService.class);
            Log.d(TAG, "start and bind service");
            bindService(intent, this.mServiceConnection, 1);
        }
    }

    public void onClick(View v) {
        if (v == this.mToggleButton) {
            if (this.mToggleButton.isChecked()) {
                Log.d(TAG, "checked == false");
                if (this.mService != null) {
                    this.mService.set_lcd_off();
                }
            } else {
                Log.d(TAG, "checked == true");
            }
            this.mback_camera.setText("");
            this.mfront_camera.setText("");
            this.mvideo.setText("");
            this.success_fail.setText("");
            this.moff_lcd.setText("");
            this.show_hidden = false;
        }
    }

    private void showResult() {
        this.show_hidden = true;
        boolean success = false;
        if (this.snr_sv[0] > 38.0f && Math.abs(this.snr_sv[0] - this.snr_sv[3]) < 4.0f) {
            success = true;
            if (mIsBackCmaeraChecked && Math.abs(this.snr_sv[0] - this.snr_sv[1]) >= 5.0f) {
                success = false;
            }
            if (mIsFrontCameraChecked && Math.abs(this.snr_sv[0] - this.snr_sv[2]) >= 5.0f) {
                success = false;
            }
        }
        if (success) {
            this.success_fail.setTextColor(-16711936);
            this.success_fail.setText("PASS");
            return;
        }
        this.success_fail.setTextColor(-65536);
        this.success_fail.setText("FAIL");
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case 2131493107:
                mIsFrontCameraChecked = isChecked;
                mFrontCameraCheckBox.setChecked(isChecked);
                Log.e(TAG, "onCheckedChanged mIsFrontCameraChecked: " + mIsFrontCameraChecked);
                return;
            case 2131493108:
                mIsBackCmaeraChecked = isChecked;
                mBackCameraCheckBox.setChecked(isChecked);
                Log.e(TAG, "onCheckedChanged mIsBackCmaeraChecked:" + mIsBackCmaeraChecked);
                return;
            default:
                return;
        }
    }

    public static boolean shouldTestFrontCamera() {
        return mFrontCameraCheckBox.isChecked();
    }

    public static boolean shouldTestBackCamera() {
        return mBackCameraCheckBox.isChecked();
    }
}
