package com.android.engineeringmode.manualtest.modeltest;

public class ModelTestBatteryCover extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest_battery_cover_list.xml");
    }

    protected int getMarkpostion() {
        return -1;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
