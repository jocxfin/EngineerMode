package com.android.engineeringmode.manualtest.modeltest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.engineeringmode.autotest.FailRecordManager;
import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.util.ExternFunction;
import com.oem.util.Feature;

import java.io.File;

public abstract class ModelTestBaseAcivity extends Activity {
    private FailRecordManager failRecords;
    private boolean mConnected = false;
    private ExternFunction mExFunction;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                ModelTestBaseAcivity.this.mConnected = true;
            }
        }
    };
    protected ModelTestManager testManager;

    protected abstract void deleteTestsForSpeacialDevice();

    protected abstract int getMarkpostion();

    protected abstract ModelTestManager getTestManager();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.failRecords = new FailRecordManager();
        this.testManager = getTestManager();
        deleteTestsForSpeacialDevice();
        if (savedInstanceState != null) {
            this.testManager.setCurrentIndex(savedInstanceState.getInt("model_test_current_index"));
            Log.d("ModelTestBaseAcivity", "current test index after release :" + savedInstanceState.getInt("model_test_current_index"));
        }
        if (this.testManager.hasNextIntent()) {
            this.failRecords.clearFailList();
            startNextTestItem();
        }
        this.mExFunction = new ExternFunction(this);
        this.mExFunction.registerOnServiceConnected(this.mHandler, 1, null);
    }

    protected void onDestroy() {
        this.mExFunction.unregisterOnServiceConnected(this.mHandler);
        this.mExFunction.dispose();
        super.onDestroy();
    }

    private void oppoMarkResult() {
        if (getMarkpostion() != -1) {
            if (this.mConnected) {
                if (Feature.isSdcardSupported(this) && Environment.getStorageState(new File("/storage/sdcard1")).equals("mounted")) {
                    this.mExFunction.setProductLineTestFlagExtraByte(23, (byte) 1);
                }
                if (this.failRecords.getFailList().size() > 0) {
                    this.mExFunction.setProductLineTestFlagExtraByte(getMarkpostion(), (byte) 2);
                } else {
                    this.mExFunction.setProductLineTestFlagExtraByte(getMarkpostion(), (byte) 1);
                }
            } else {
                Log.d("ModelTestBaseAcivity", "Connected service failed...");
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("ModelTestBaseAcivity", "current test index :" + this.testManager.getCurrentIndex());
        outState.putInt("model_test_current_index", this.testManager.getCurrentIndex());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                handleTestResult(resultCode);
                return;
            case Light.CHARGE_RED_LIGHT /*2*/:
                finish();
                return;
            default:
                return;
        }
    }

    private void handleTestResult(int resultCode) {
        String cur_intent = this.testManager.getCurrentIntent().getAction();
        if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelMicTest")) {
            saveSingleItemTestResult(31, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.specialtest.sdcardrelative.MultiMediaPlayerTest")) {
            saveSingleItemTestResult(33, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.BatteryInfoShow.FastCharger")) {
            saveSingleItemTestResult(34, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.BatteryInfoShow.NormalCharger")) {
            saveSingleItemTestResult(63, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelKeyboardBackLight")) {
            saveSingleItemTestResult(35, resultCode);
        } else if (cur_intent.equals("com.honestar.irtest.MainActivity")) {
            saveSingleItemTestResult(36, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelInfraredGestureTest")) {
            saveSingleItemTestResult(37, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.SimCardTest")) {
            saveSingleItemTestResult(38, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelOTGTest")) {
            saveSingleItemTestResult(39, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelSmallBoardCheck")) {
            saveSingleItemTestResult(40, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.wifitest.ModelWifiConnectTest")) {
            saveSingleItemTestResult(41, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelBTSearch")) {
            saveSingleItemTestResult(42, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelLcdBacklightTest")) {
            saveSingleItemTestResult(43, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelBreathLigthTest")) {
            saveSingleItemTestResult(44, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelLcdColorTest")) {
            saveSingleItemTestResult(45, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelTouchScreeen")) {
            saveSingleItemTestResult(46, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelVibratorTest")) {
            saveSingleItemTestResult(47, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelLightSensor")) {
            saveSingleItemTestResult(48, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelSensorCalibration")) {
            saveSingleItemTestResult(49, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelGsensorTest")) {
            saveSingleItemTestResult(50, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelMSensorAutoTest")) {
            saveSingleItemTestResult(51, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.SensorTextProx")) {
            saveSingleItemTestResult(52, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.GyroscopeGraph")) {
            saveSingleItemTestResult(53, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelNfcTest")) {
            saveSingleItemTestResult(54, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.autotest.KeypadTest")) {
            saveSingleItemTestResult(56, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ModelHolzerTest")) {
            saveSingleItemTestResult(57, resultCode);
        } else if (cur_intent.equals("com.oppo.fingerprints.fingerprintsensortest.FingerPrintAutoTest")) {
            saveSingleItemTestResult(24, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.LaserFocusBasicTest")) {
            saveSingleItemTestResult(22, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.CheckPdafInfo")) {
            saveSingleItemTestResult(62, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.autotest.TriKeySwitchTest")) {
            saveSingleItemTestResult(59, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.QcomFastChargerTest")) {
            saveSingleItemTestResult(58, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.autotest.HeadsetPlayTest")) {
            saveSingleItemTestResult(64, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtes.MotorProductionTest")) {
            saveSingleItemTestResult(76, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.ThirdMicTest")) {
            saveSingleItemTestResult(77, resultCode);
        } else if (cur_intent.equals("com.android.engineeringmode.manualtest.modeltest.CameraTestController")) {
            saveSingleItemTestResult(27, resultCode);
        }
        switch (resultCode) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                if (this.testManager.hasNextIntent()) {
                    startNextTestItem();
                    return;
                }
                oppoMarkResult();
                showTestResult();
                return;
            case Light.CHARGE_RED_LIGHT /*2*/:
                startCurrentTestItem();
                return;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                addResultToFailRecord();
                if (this.testManager.hasNextIntent()) {
                    startNextTestItem();
                    return;
                }
                oppoMarkResult();
                showTestResult();
                return;
            case 4:
                finish();
                return;
            default:
                finish();
                return;
        }
    }

    private void showTestResult() {
        Intent inent = new Intent(this, ModelTestResultReport.class);
        inent.putStringArrayListExtra("FailedList", this.failRecords.getFailList());
        startActivityForResult(inent, 2);
    }

    private void startCurrentTestItem() {
        Intent currentIntent = this.testManager.getCurrentIntent();
        Log.d("ModelTestBaseAcivity", "startCurrentTestItem:" + currentIntent.toString(), new Throwable());
        currentIntent.putExtra("model_test", true);
        startActivityForResult(currentIntent, 1);
    }

    private void startNextTestItem() {
        Intent nextIntent = this.testManager.getNextIntent();
        Log.d("ModelTestBaseAcivity", "startNextTestItem:" + nextIntent.toString(), new Throwable());
        nextIntent.putExtra("model_test", true);
        startActivityForResult(nextIntent, 1);
    }

    private void addResultToFailRecord() {
        String testItemTitle = this.testManager.getCurrentTitle();
        String fragment = getString(2131296701);
        this.failRecords.addFailRecord(String.format(fragment, new Object[]{testItemTitle}));
    }

    private void saveSingleItemTestResult(int nvPosition, int resultCode) {
        if (resultCode == 1) {
            this.mExFunction.setProductLineTestFlagExtraByte(nvPosition, (byte) 1);
        } else if (resultCode == 3) {
            this.mExFunction.setProductLineTestFlagExtraByte(nvPosition, (byte) 2);
        }
    }
}
