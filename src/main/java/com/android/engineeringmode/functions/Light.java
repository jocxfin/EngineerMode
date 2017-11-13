package com.android.engineeringmode.functions;

public class Light {
    public static final int CHARGE_GREEN_LIGHT = 3;
    public static final int CHARGE_RED_LIGHT = 2;
    public static final int FRONT_KEY_LIGHT = 0;
    public static final int MAIN_KEY_LIGHT = 1;
    public static final int MAIN_KEY_MAX = 255;
    public static final int MAIN_KEY_MIN = 0;
    public static final int MAIN_KEY_NORMAL = 6;

    public static native int calibrateReset();

    public static native void closeBlueLight();

    public static native void closeBreatheLight();

    public static native void closeContactorTest();

    public static native void closeFlashlight();

    public static native void closeGreenLight();

    public static native void closeHuxiLight();

    public static native int closeLaserSensor();

    public static native void closeLight(int i);

    public static native void closeOpticalImageStabilizer();

    public static native void closeRedLight();

    public static native void closeSerialPort();

    public static native int crossTalkCalibrate();

    public static native void enterShipMode();

    public static native void flashLamp(int i);

    public static native int getLaserConvTimeValue();

    public static native float getLaserCrossTalkValue();

    public static native int getLaserErrorCodeValue();

    public static native int getLaserRangeOffsetValue();

    public static native int getLaserRangeValue();

    public static native int getLaserRawRangeValue();

    public static native int getLaserRefAmbCountValue();

    public static native float getLaserRefAmbRateValue();

    public static native int getLaserRefConvTimeValue();

    public static native int getLaserRefSignalCountValue();

    public static native float getLaserRefSignalRateValue();

    public static native int getLaserRtnAmbCountValue();

    public static native float getLaserRtnAmbRateValue();

    public static native int getLaserRtnConvTimeValue();

    public static native int getLaserRtnSignalCountValue();

    public static native float getLaserRtnSignalRateValue();

    public static native int getLcdBrightness();

    public static native int getLcdGamma();

    public static native void gripSensorCalibrateTest();

    public static native int offsetCalibrate();

    public static native void openBreatheLight(boolean z);

    public static native void openContactorTest();

    public static native void openFlashLamp();

    public static native void openFlashlight();

    public static native void openHuxiLight(int i);

    public static native int openLaserSensor();

    public static native void openLight(int i);

    public static native void openOpticalImageStabilizer();

    public static native void openSerialPort();

    public static native void setBlink(int i, int i2);

    public static native void setBreatheLightBrightness(int i);

    public static native void setBrightness(int i, int i2);

    public static native void setCharging(int i);

    public static native void setElectric(int i, int i2);

    public static native void setHuxiBlueBrightness(int i);

    public static native void setHuxiGreenBrightness(int i);

    public static native void setHuxiRedBrightness(int i);

    public static native void setLCDGammaData(int i);

    public static native void setLcdBrightness(int i);

    public static native int setLcdGamma(int i);

    public static native void setLightFlash(int i);

    public static native void setVibrate(String str);

    public static native int touchGetProgState();

    public static native int touchGetVersion();

    public static native void touchStartProg(int i);

    static {
        System.loadLibrary("light_jni");
    }

    private Light() {
    }
}
