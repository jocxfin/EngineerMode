package com.android.engineeringmode.manualtest.modeltest;

public class ModelTest14 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest14_list.xml");
    }

    protected int getMarkpostion() {
        return 13;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
