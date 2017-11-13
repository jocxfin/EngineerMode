package com.android.engineeringmode.manualtest.modeltest;

public class ModelTest8 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest8_list.xml");
    }

    protected int getMarkpostion() {
        return 7;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
