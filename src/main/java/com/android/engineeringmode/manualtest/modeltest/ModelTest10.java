package com.android.engineeringmode.manualtest.modeltest;

public class ModelTest10 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest10_list.xml");
    }

    protected int getMarkpostion() {
        return 9;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
