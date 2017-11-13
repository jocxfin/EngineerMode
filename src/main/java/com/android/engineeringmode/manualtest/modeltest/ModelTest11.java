package com.android.engineeringmode.manualtest.modeltest;

public class ModelTest11 extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "modeltest11_list.xml");
    }

    protected int getMarkpostion() {
        return 10;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
