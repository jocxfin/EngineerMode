package com.android.engineeringmode.manualtest.modeltest;

import com.oem.util.Feature;

public class ModelTest3 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest3_list.xml");
    }

    protected int getMarkpostion() {
        return 2;
    }

    protected void deleteTestsForSpeacialDevice() {
        if (!Feature.isFingerPrintSupported(this)) {
            this.testManager.remove("com.oppo.fingerprints.fingerprintsensortest.FingerPrintQualityTest");
            this.testManager.remove("com.oppo.fingerprints.fingerprintsensortest.FingerPrintAutoTest");
        }
    }
}
