package com.android.engineeringmode.manualtest.modeltest;

public class ModelTest15 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest15_list.xml");
    }

    protected int getMarkpostion() {
        return 14;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
