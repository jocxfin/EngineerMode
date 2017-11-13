package com.android.engineeringmode.manualtest.modeltest;

public class ModelTest17 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest17_list.xml");
    }

    protected int getMarkpostion() {
        return 16;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
