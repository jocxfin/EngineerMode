package com.android.engineeringmode.manualtest.modeltest;

public class ModelTest16 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest16_list.xml");
    }

    protected int getMarkpostion() {
        return 15;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
