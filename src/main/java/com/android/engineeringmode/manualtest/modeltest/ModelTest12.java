package com.android.engineeringmode.manualtest.modeltest;

public class ModelTest12 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest12_list.xml");
    }

    protected int getMarkpostion() {
        return 11;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
