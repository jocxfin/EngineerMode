package com.android.engineeringmode.manualtest.modeltest;

import com.oem.util.Feature;

public class ModelTest5 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest5_list.xml");
    }

    protected int getMarkpostion() {
        return 4;
    }

    protected void deleteTestsForSpeacialDevice() {
        if (!Feature.isOpticalStabilizerSupported(this)) {
            this.testManager.remove("com.android.engineeringmode.manualtest.CameraOisStillTest");
            this.testManager.remove("com.android.engineeringmode.manualtest.CameraOpticalImageStabilizer");
        }
        if (!Feature.isLinearMotorSupported(this)) {
            this.testManager.remove("com.android.engineeringmode.manualtes.MotorProductionTest");
        }
    }
}
