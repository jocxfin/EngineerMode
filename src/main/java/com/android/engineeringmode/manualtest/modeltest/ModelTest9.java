package com.android.engineeringmode.manualtest.modeltest;

import com.oem.util.Feature;

public class ModelTest9 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest9_list.xml");
    }

    protected int getMarkpostion() {
        return 8;
    }

    protected void deleteTestsForSpeacialDevice() {
        if (Feature.isSmallBoardNotSupported(this)) {
            this.testManager.remove("com.android.engineeringmode.manualtest.modeltest.ModelSmallBoardCheck");
        }
    }
}
