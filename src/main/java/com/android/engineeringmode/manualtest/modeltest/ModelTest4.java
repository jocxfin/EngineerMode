package com.android.engineeringmode.manualtest.modeltest;

import com.oem.util.Feature;

public class ModelTest4 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest4_list.xml");
    }

    protected int getMarkpostion() {
        return 3;
    }

    protected void deleteTestsForSpeacialDevice() {
        if (!Feature.isLaserFocusSupported(this)) {
            this.testManager.remove("com.android.engineeringmode.manualtest.LaserFocusCalibrateTest");
            this.testManager.remove("com.android.engineeringmode.manualtest.LaserFocusBasicTest");
        }
    }
}
