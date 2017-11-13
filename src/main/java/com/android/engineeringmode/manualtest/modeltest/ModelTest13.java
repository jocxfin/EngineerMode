package com.android.engineeringmode.manualtest.modeltest;

public class ModelTest13 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest13_list.xml");
    }

    protected int getMarkpostion() {
        return 12;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
