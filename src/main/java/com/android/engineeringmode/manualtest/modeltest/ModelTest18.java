package com.android.engineeringmode.manualtest.modeltest;

public class ModelTest18 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest18_list.xml");
    }

    protected int getMarkpostion() {
        return 17;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
