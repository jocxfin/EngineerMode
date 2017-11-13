package com.android.engineeringmode.manualtest.modeltest;

public class ModelTest19 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest19_list.xml");
    }

    protected int getMarkpostion() {
        return 18;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
