package com.android.engineeringmode.manualtest.modeltest;

import com.oem.util.Feature;

public class ModelTest2 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest2_list.xml");
    }

    protected int getMarkpostion() {
        return 1;
    }

    protected void deleteTestsForSpeacialDevice() {
        if (Feature.isSmallBoardNotSupported(this)) {
            this.testManager.remove("com.android.engineeringmode.manualtest.modeltest.ModelSmallBoardCheck");
        }
        if (Feature.isPDAFNotSupported(this)) {
            this.testManager.remove("com.android.engineeringmode.manualtest.CheckPdafInfo");
        }
        if (!Feature.isFmRadioSupported(this)) {
            this.testManager.remove("com.caf.fmradio.FMRADIO_ENG_MODE_ACTIVITY");
        }
        if (!Feature.isNfcSupported(this)) {
            this.testManager.remove("com.android.engineeringmode.manualtest.modeltest.ModelNfcTest");
        }
        if (!Feature.isVOOCFastchagerSupported(this)) {
            this.testManager.remove("com.android.engineeringmode.manualtest.BatteryInfoShow.FastCharger");
        }
        if (!Feature.isFingerPrintSupported(this)) {
            this.testManager.remove("com.oppo.fingerprints.fingerprintsensortest.FingerPrintQualityTest");
            this.testManager.remove("com.oppo.fingerprints.fingerprintsensortest.FingerPrintAutoTest");
        }
        if (!Feature.isNfcSupported(this)) {
            this.testManager.remove("com.android.engineeringmode.manualtest.modeltest.ModelNfcTest");
        }
        if (!Feature.isThreeStageKeySupported(this)) {
            this.testManager.remove("com.android.engineeringmode.autotest.TriKeySwitchTest");
        }
    }
}
