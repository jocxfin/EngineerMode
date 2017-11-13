package com.oem.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Feature {
    @SuppressLint({"NewApi"})
    public static boolean isThreeStageKeySupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.threeStageKey.support");
        Log.i("Eng-Feature", "isThreeStageKeySupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isFingerPrintSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.finger.print.support");
        Log.i("Eng-Feature", "isFingerPrintSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isNfcSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("android.hardware.nfc");
        Log.i("Eng-Feature", "isNfcSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isLaserFocusSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.laser.focus.support");
        Log.i("Eng-Feature", "isLaserFocusSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isCameraPdafSupported() {
        Throwable th;
        BufferedReader bufferedReader = null;
        if (new File("/proc/pdaf_info").exists()) {
            Log.i("Eng-Feature", "PDAF_INFO_PATH exists() : true");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/pdaf_info")));
                try {
                    String line = br.readLine();
                    if (line == null || line.startsWith("0")) {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e) {
                            }
                        }
                        bufferedReader = br;
                    } else {
                        Log.i("Eng-Feature", "isCameraPdafSupported : true");
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e2) {
                            }
                        }
                        return true;
                    }
                } catch (Exception e3) {
                    bufferedReader = br;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e4) {
                        }
                    }
                    Log.i("Eng-Feature", "isCameraPdafSupported : false");
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = br;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e5) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e6) {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                Log.i("Eng-Feature", "isCameraPdafSupported : false");
                return false;
            } catch (Throwable th3) {
                th = th3;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        }
        Log.i("Eng-Feature", "isCameraPdafSupported : false");
        return false;
    }

    @SuppressLint({"NewApi"})
    public static boolean isOpticalStabilizerSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.optical.stabilizer.support");
        Log.i("Eng-Feature", "isOpticalStabilizerSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isHeatPipeSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.heat.pipe.support");
        Log.i("Eng-Feature", "isHeatPipeSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isCoverSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.cover.support");
        Log.i("Eng-Feature", "isCoverSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isProxCalibrationSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.prox.calibration.support");
        Log.i("Eng-Feature", "isProxCalibrationSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isLightCalibrationSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.light.calibration.support");
        Log.i("Eng-Feature", "isLightCalibrationSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    public static boolean isActivityAvailable(Context context, Intent intent) {
        boolean z = false;
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        if (context.getPackageManager().resolveActivity(intent, 0) != null) {
            z = true;
        }
        return z;
    }

    @SuppressLint({"NewApi"})
    public static boolean isFmRadioSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = isActivityAvailable(context, new Intent("com.caf.fmradio.FMRADIO_ENG_MODE_ACTIVITY"));
        Log.i("Eng-Feature", "isFmRadioSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isButtonLightSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.button.light.support");
        Log.i("Eng-Feature", "isButtonLightSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isSerialCdevSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.serial_cdev.support");
        Log.i("Eng-Feature", "isSerialCdevSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isUsbMicroBSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.usb.microb.support");
        Log.i("Eng-Feature", "isUsbMicroBSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isOtgPositiveNegativePlugSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.otg.positive.negative.plug.support");
        Log.i("Eng-Feature", "isOtgPositiveNegativePlugSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isModelTestSupported(int i) {
        String filePath = "/sdcard/modeltest/modeltest" + i + "_list.xml";
        if (new File(filePath).exists()) {
            Log.i("Eng-Feature", "isModelTest" + i + "Supported : " + true);
            return true;
        }
        Log.e("Eng-Feature", "File " + filePath + " is not exist...");
        return false;
    }

    @SuppressLint({"NewApi"})
    public static boolean isSdcardSupported(Context context) {
        boolean z = false;
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        if (context.getObbDirs().length >= 2) {
            z = true;
        }
        return z;
    }

    @SuppressLint({"NewApi"})
    public static boolean isIrRemoteControlerSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = isActivityAvailable(context, new Intent("com.honestar.irtest.MainActivity"));
        Log.i("Eng-Feature", "isIrRemoteControlerSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isQcomFastchagerSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.qcom.fastchager.support");
        Log.i("Eng-Feature", "isQcomFastchagerSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isVOOCFastchagerSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.vooc.fastchager.support");
        Log.i("Eng-Feature", "isVOOCFastchagerSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isGpioSwitchSupported(Context context) {
        return new File("/proc/antenna_switch").exists();
    }

    @SuppressLint({"NewApi"})
    public static boolean isNfcContinueTransmitSupported(Context context) {
        return new File("/dev/pn544").exists();
    }

    @SuppressLint({"NewApi"})
    public static boolean isSecureBootSupported() {
        return new File("/sys/project_info/secboot_status").exists();
    }

    @SuppressLint({"NewApi"})
    public static boolean isNoAgingSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.no_aging.support");
        Log.i("Eng-Feature", "isNoAgingSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isRFCableConfigSupported() {
        return new File("/proc/rf_cable_config").exists();
    }

    @SuppressLint({"NewApi"})
    public static boolean isTouchKeyBaselineTestSupported() {
        return new File("proc/s1302/touchkey_baseline_test").exists();
    }

    @SuppressLint({"NewApi"})
    public static boolean isBreathingLightICSupported() {
        return new File("/dev/SN3193").exists();
    }

    @SuppressLint({"NewApi"})
    public static boolean isRGBSensorSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.rgb.sensor.support");
        Log.i("Eng-Feature", "isRGBSensorSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isLinearMotorSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.linear.motor.support");
        Log.i("Eng-Feature", "isLinearMotorSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isDualLEDSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.dual.led.support");
        Log.i("Eng-Feature", "isDualLEDSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isDualBackCameraSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.dual.back.camera.support");
        Log.i("Eng-Feature", "isDualBackCameraSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isSmallBoardNotSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.small.board.not.support");
        Log.i("Eng-Feature", "isSmallBoardNotSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isPDAFNotSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.pdaf.not.support");
        Log.i("Eng-Feature", "isPDAFNotSupported : " + isSuppoerted);
        return isSuppoerted;
    }

    @SuppressLint({"NewApi"})
    public static boolean isHallAutoTestSupported(Context context) {
        if (context == null) {
            Log.i("Eng-Feature", "context is null");
            return false;
        }
        boolean isSuppoerted = context.getPackageManager().hasSystemFeature("oem.hall.auto.test.support");
        Log.i("Eng-Feature", "isHallAutoTestSupported : " + isSuppoerted);
        return isSuppoerted;
    }
}
