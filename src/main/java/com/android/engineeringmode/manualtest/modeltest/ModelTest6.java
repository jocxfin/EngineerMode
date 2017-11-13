package com.android.engineeringmode.manualtest.modeltest;

public class ModelTest6 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest6_list.xml");
    }

    protected int getMarkpostion() {
        return 5;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
