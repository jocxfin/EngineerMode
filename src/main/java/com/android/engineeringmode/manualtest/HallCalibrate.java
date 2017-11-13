package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.util.ExternFunction;

public class HallCalibrate extends Activity {
    private static final int[] CALI_ROTATE_PARA_STEP_1 = new int[]{6, 1, 1000, 90, 1, 1};
    private static final int[] CALI_ROTATE_PARA_STEP_2 = new int[]{6, 1, 500, 20, 1, 1};
    private short[] INVALID_VALUE = new short[]{(short) 0, (short) 0};
    private short[] averange_value = new short[]{(short) 0, (short) 0};
    private int calibrate_index = 0;
    private short[] current_value;
    private TextView mAverageHallCalirateHighThresTv;
    private TextView mAverageHallCalirateLowThresTv;
    private TextView mCalirateResultTv;
    private TextView mCurrentHallCalirateHighThresTv;
    private TextView mCurrentHallCalirateLowThresTv;
    private ExternFunction mExternFunction;
    private HallCalibrateThread mHallCalibrateThread;
    private Button mHallCalirateBtn;
    private int mHallCalirateCount;
    private TextView mHallCalirateStatusTv;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.i("HallCalibrate", "onCreate before calibrate_hall");
                HallCalibrate.this.mHallCalirateCount = 0;
                HallCalibrate.this.calibrate_index = 0;
                HallCalibrate.this.current_value = HallCalibrate.this.readAdjustResultFromNvram();
                HallCalibrate.this.updateHallCalibrateStatusUI(2131297370);
                HallCalibrate.this.updateCurrentHallCalibrateStatusUI(HallCalibrate.this.current_value);
                HallCalibrate.this.mAverageHallCalirateLowThresTv.setText("" + HallCalibrate.this.INVALID_VALUE[0]);
                HallCalibrate.this.mAverageHallCalirateHighThresTv.setText("" + HallCalibrate.this.INVALID_VALUE[1]);
                HallCalibrate.this.mHallCalirateBtn.setText(2131297380);
                HallCalibrate.this.mHallCalirateBtn.setEnabled(true);
            } else if (msg.what == 10) {
                HallCalibrate.this.mHallCalirateBtn.setEnabled(false);
                if (HallCalibrate.this.calibrate_index == 0) {
                    HallCalibrate.this.mHallCalirateBtn.setText(2131297381);
                    HallCalibrate.this.prepareRotation(HallCalibrate.CALI_ROTATE_PARA_STEP_1);
                    HallCalibrate.this.mHandler.sendEmptyMessageDelayed(2, (long) HallCalibrate.CALI_ROTATE_PARA_STEP_1[2]);
                } else if (HallCalibrate.this.calibrate_index == 1) {
                    HallCalibrate.this.prepareRotation(HallCalibrate.CALI_ROTATE_PARA_STEP_2);
                    HallCalibrate.this.mHandler.sendEmptyMessageDelayed(2, (long) HallCalibrate.CALI_ROTATE_PARA_STEP_2[2]);
                }
            } else if (msg.what == 2) {
                if (HallCalibrate.this.mHallCalibrateThread == null) {
                    HallCalibrate.this.mHallCalirateCount = 0;
                    HallCalibrate.this.averange_value[HallCalibrate.this.calibrate_index] = (short) 0;
                    HallCalibrate.this.updateHallCalibrateStatusUI(2131297377);
                    HallCalibrate.this.mHallCalibrateThread = new HallCalibrateThread();
                    HallCalibrate.this.mHallCalibrateThread.start();
                }
            } else if (msg.what == 3) {
                if (HallCalibrate.this.calibrate_index == 0) {
                    HallCalibrate.this.mHallCalirateBtn.setText(2131297382);
                } else if (HallCalibrate.this.calibrate_index == 1) {
                    HallCalibrate.this.mHallCalirateBtn.setText(2131297383);
                }
                if (HallCalibrate.this.calibrate_index == 0) {
                    HallCalibrate.this.mAverageHallCalirateLowThresTv.setText("" + HallCalibrate.this.averange_value[0]);
                    HallCalibrate.this.mHandler.sendEmptyMessageDelayed(10, 1000);
                } else if (HallCalibrate.this.calibrate_index == 1) {
                    HallCalibrate.this.mAverageHallCalirateHighThresTv.setText("" + HallCalibrate.this.averange_value[1]);
                    HallCalibrate.this.mHandler.sendEmptyMessageDelayed(9, 1000);
                }
                HallCalibrate hallCalibrate = HallCalibrate.this;
                hallCalibrate.calibrate_index = hallCalibrate.calibrate_index + 1;
                if (HallCalibrate.this.mHallCalibrateThread != null) {
                    HallCalibrate.this.mHallCalibrateThread.forceStopThread();
                    HallCalibrate.this.mHallCalibrateThread = null;
                }
            } else if (msg.what == 9) {
                if (HallCalibrate.this.averange_value[0] > HallCalibrate.this.averange_value[1]) {
                    HallCalibrate.this.min = HallCalibrate.this.averange_value[1];
                    HallCalibrate.this.max = HallCalibrate.this.averange_value[0];
                } else {
                    HallCalibrate.this.min = HallCalibrate.this.averange_value[0];
                    HallCalibrate.this.max = HallCalibrate.this.averange_value[1];
                }
                HallCalibrate.this.mHallCalirateBtn.setEnabled(false);
                if (HallCalibrate.this.mRotationUtils.set_hall_calibrate_data((short) 1, HallCalibrate.this.min, HallCalibrate.this.max) == 1) {
                    HallCalibrate.this.result_value[0] = (short) 1;
                    HallCalibrate.this.result_value[1] = HallCalibrate.this.min;
                    HallCalibrate.this.result_value[2] = HallCalibrate.this.max;
                    HallCalibrate.this.saveAdjustResultToNvram(HallCalibrate.this.result_value);
                    HallCalibrate.this.mHandler.sendEmptyMessageDelayed(4, 200);
                    return;
                }
                HallCalibrate.this.mHandler.sendEmptyMessage(5);
            } else if (msg.what == 4) {
                if (HallCalibrate.this.mHallCalibrateThread != null) {
                    HallCalibrate.this.mHallCalibrateThread.forceStopThread();
                    HallCalibrate.this.mHallCalibrateThread = null;
                }
                short[] new_value = HallCalibrate.this.readAdjustResultFromNvram();
                if (new_value[1] == HallCalibrate.this.min && new_value[2] == HallCalibrate.this.max) {
                    HallCalibrate.this.mCurrentHallCalirateLowThresTv.setText("" + HallCalibrate.this.min);
                    HallCalibrate.this.mCurrentHallCalirateHighThresTv.setText("" + HallCalibrate.this.max);
                    HallCalibrate.this.updateHallCalibrateSucessUI();
                    return;
                }
                HallCalibrate.this.updateHallCalibrateFailUI();
                HallCalibrate.this.mHallCalirateStatusTv.setText("WRITE NV FAIL" + HallCalibrate.this.getSummaryString());
            } else if (msg.what == 5) {
                if (HallCalibrate.this.mHallCalibrateThread != null) {
                    HallCalibrate.this.mHallCalibrateThread.forceStopThread();
                    HallCalibrate.this.mHallCalibrateThread = null;
                }
                HallCalibrate.this.updateHallCalibrateFailUI();
            }
        }
    };
    private int mOldRotationEnableCamera = -1;
    private RotationUtils mRotationUtils;
    private short max = (short) 0;
    private short min = (short) 0;
    private boolean motor_started = false;
    private short[] result_value = new short[]{(short) 0, (short) 0, (short) 0};

    private class HallCalibrateThread extends Thread {
        private boolean forcestop;

        private HallCalibrateThread() {
            this.forcestop = false;
        }

        public void forceStopThread() {
            this.forcestop = true;
        }

        public void run() {
            while (!this.forcestop && HallCalibrate.this.mHallCalirateCount < 20) {
                short[]-get3 = HallCalibrate.this.averange_value;
                int-get4 = HallCalibrate.this.calibrate_index;
                -get3[-get4] = (short) (-get3[-get4] + ((short) HallCalibrate.this.mRotationUtils.get_calibrate_adc_data()));
                HallCalibrate hallCalibrate = HallCalibrate.this;
                hallCalibrate.mHallCalirateCount = hallCalibrate.mHallCalirateCount + 1;
                if (HallCalibrate.this.mHallCalirateCount >= 20) {
                    HallCalibrate.this.averange_value[HallCalibrate.this.calibrate_index] = (short) (HallCalibrate.this.averange_value[HallCalibrate.this.calibrate_index] / 20);
                    if (HallCalibrate.this.calibrate_index <= 1) {
                        HallCalibrate.this.mHandler.sendEmptyMessage(3);
                    }
                }
            }
            HallCalibrate.this.mHallCalirateCount = 0;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903113);
        this.mHallCalirateBtn = (Button) findViewById(2131493183);
        this.mHallCalirateStatusTv = (TextView) findViewById(2131493177);
        this.mCurrentHallCalirateLowThresTv = (TextView) findViewById(2131493178);
        this.mCurrentHallCalirateHighThresTv = (TextView) findViewById(2131493179);
        this.mAverageHallCalirateLowThresTv = (TextView) findViewById(2131493180);
        this.mAverageHallCalirateHighThresTv = (TextView) findViewById(2131493181);
        this.mCalirateResultTv = (TextView) findViewById(2131493182);
        this.mExternFunction = new ExternFunction(this);
        this.mExternFunction.registerOnServiceConnected(this.mHandler, 1, null);
        this.mRotationUtils = new RotationUtils();
        updateHallCalibrateStatusUI(2131297370);
        this.mOldRotationEnableCamera = -1;
        this.mHallCalirateBtn.setEnabled(false);
        this.mHallCalirateBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                HallCalibrate.this.mHallCalirateBtn.setEnabled(false);
                HallCalibrate.this.mHandler.sendEmptyMessage(10);
            }
        });
    }

    private String getSummaryString() {
        return getString(2131297373);
    }

    private void updateHallCalibrateStatusUI(int resid) {
        this.mHallCalirateStatusTv.setText(getString(resid) + getSummaryString());
    }

    private void updateCurrentHallCalibrateStatusUI(short[] calibrate_value) {
        this.mCurrentHallCalirateLowThresTv.setText("" + calibrate_value[1]);
        this.mCurrentHallCalirateHighThresTv.setText("" + calibrate_value[2]);
    }

    private void updateHallCalibrateFailUI() {
        this.mHallCalirateBtn.setEnabled(false);
        updateHallCalibrateStatusUI(2131297379);
        this.mCalirateResultTv.setVisibility(0);
        this.mCalirateResultTv.setText(2131297379);
        this.mCalirateResultTv.setTextColor(-65536);
    }

    private void updateHallCalibrateSucessUI() {
        updateHallCalibrateStatusUI(2131297378);
        this.mAverageHallCalirateLowThresTv.setText("" + this.averange_value[0]);
        this.mAverageHallCalirateHighThresTv.setText("" + this.averange_value[1]);
        this.mCalirateResultTv.setVisibility(0);
        this.mCalirateResultTv.setText(2131297378);
        this.mCalirateResultTv.setTextColor(-16711936);
    }

    protected void onResume() {
        super.onResume();
        if (this.mOldRotationEnableCamera == -1) {
        }
    }

    protected void onPause() {
        removeAllMsg();
        stopRotation();
        super.onPause();
        if (this.mOldRotationEnableCamera == -1) {
        }
    }

    protected void onStop() {
        super.onStop();
        if (this.mHandler != null) {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mHandler.removeMessages(4);
            this.mHandler.removeMessages(5);
        }
        if (this.mHallCalibrateThread != null) {
            this.mHallCalibrateThread.forceStopThread();
            this.mHallCalibrateThread = null;
        }
    }

    protected void onDestroy() {
        this.mExternFunction.unregisterOnServiceConnected(this.mHandler);
        this.mExternFunction.dispose();
        super.onDestroy();
    }

    private byte[] short2ByteArray(short target) {
        byte[] array = new byte[2];
        for (int i = 0; i < 2; i++) {
            array[i] = (byte) ((target >> (((array.length - i) - 1) * 8)) & Light.MAIN_KEY_MAX);
        }
        return array;
    }

    private short byteArray2Short(byte[] array) {
        short result = (short) 0;
        for (int i = 0; i < 2; i++) {
            result = (short) (((array[i] & Light.MAIN_KEY_MAX) << (((array.length - i) - 1) * 8)) + result);
        }
        return result;
    }

    private void saveAdjustResultToNvram(short[] adjustValue) {
        byte[] buff = this.mExternFunction.getProductLineTestFlag();
        for (int i = 0; i < adjustValue.length; i++) {
            byte[] temp = short2ByteArray(adjustValue[i]);
            System.arraycopy(temp, 0, buff, (temp.length * i) + 3, temp.length);
        }
        this.mExternFunction.setProductLineTestFlag(buff);
    }

    private short[] readAdjustResultFromNvram() {
        byte[] buff = this.mExternFunction.getProductLineTestFlag();
        short[] result = new short[3];
        for (int i = 0; i < result.length; i++) {
            temp = new byte[2];
            System.arraycopy(buff, (temp.length * i) + 3, temp, 0, temp.length);
            result[i] = byteArray2Short(temp);
            Log.i("HallCalibrate", "result read from is : " + result[i]);
        }
        return result;
    }

    private void prepareRotation(int[] parameter) {
        if (!this.motor_started) {
            RotationUtils.open_motor();
        }
        stopRotation();
        RotationUtils.motor_rotation_set_para(parameter[0], parameter[1], parameter[3], parameter[4], parameter[5]);
    }

    private void stopRotation() {
        if (this.motor_started) {
            RotationUtils.close_motor();
        }
    }

    private void removeAllMsg() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mHandler.removeMessages(4);
            this.mHandler.removeMessages(5);
            this.mHandler.removeMessages(9);
            this.mHandler.removeMessages(10);
        }
    }
}
