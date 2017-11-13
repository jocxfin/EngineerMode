package com.android.engineeringmode.manualtest.modeltest;

public class ModelTest20 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest20_list.xml");
    }

    protected int getMarkpostion() {
        return 19;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
