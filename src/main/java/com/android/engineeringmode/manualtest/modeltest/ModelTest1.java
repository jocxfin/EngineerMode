package com.android.engineeringmode.manualtest.modeltest;

import com.oem.util.Feature;

import java.io.File;

public class ModelTest1 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest1_list.xml");
    }

    protected int getMarkpostion() {
        return 0;
    }

    protected void deleteTestsForSpeacialDevice() {
        String GESTURE_PATH = "/sys/class/input/input4/prox_ges_cali";
        String InfraredGestureAction = "com.android.engineeringmode.manualtest.modeltest.ModelInfraredGestureTest";
        if (!new File("/sys/class/input/input4/prox_ges_cali").exists()) {
            this.testManager.remove("com.android.engineeringmode.manualtest.modeltest.ModelInfraredGestureTest");
        }
        if (!new File("/sys/project_info/component_info/Aboard").exists()) {
            this.testManager.remove("com.android.engineeringmode.manualtest.modeltest.ModelSmallBoardCheck");
        }
        if (!Feature.isIrRemoteControlerSupported(this)) {
            this.testManager.remove("com.honestar.irtest.MainActivity");
        }
        if (!Feature.isButtonLightSupported(this)) {
            this.testManager.remove("com.android.engineeringmode.manualtest.modeltest.ModelKeyboardBackLight");
        }
        if (!Feature.isCameraPdafSupported()) {
            this.testManager.remove("com.android.engineeringmode.manualtest.CheckPdafInfo");
        }
        if (!Feature.isQcomFastchagerSupported(this)) {
            this.testManager.remove("com.android.engineeringmode.manualtest.QcomFastChargerTest");
        }
    }
}
