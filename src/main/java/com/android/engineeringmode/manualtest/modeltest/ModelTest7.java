package com.android.engineeringmode.manualtest.modeltest;

public class ModelTest7 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest7_list.xml");
    }

    protected int getMarkpostion() {
        return 6;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
