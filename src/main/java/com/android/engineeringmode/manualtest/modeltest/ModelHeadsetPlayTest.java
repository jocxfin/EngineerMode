package com.android.engineeringmode.manualtest.modeltest;

public class ModelHeadsetPlayTest extends ModelTestBaseAcivity {
    protected ModelTestManager getTestManager() {
        return new ModelTestManager(this, "model_headset_play_list.xml");
    }

    protected int getMarkpostion() {
        return -1;
    }

    protected void deleteTestsForSpeacialDevice() {
    }
}
