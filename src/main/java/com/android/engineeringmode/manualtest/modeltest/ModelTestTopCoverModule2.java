package com.android.engineeringmode.manualtest.modeltest;

public class ModelTestTopCoverModule2 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest_top_cover_module_list2.xml");
    }

    protected int getMarkpostion() {
        return -1;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
