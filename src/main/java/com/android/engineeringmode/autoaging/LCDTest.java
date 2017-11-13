package com.android.engineeringmode.autoaging;

import android.os.Bundle;

import com.android.engineeringmode.LCDtest.LCDTestView;

public class LCDTest extends BaseTest {
    private LCDTestView mView;

    protected void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        setContentView(2130903136);
        this.mView = (LCDTestView) findViewById(2131493319);
        this.mView.setIsAutoShowing(true);
        this.mView.setRepeatTime(-1);
        this.mView.setIsTouchable(false);
        this.mView.setKeepScreenOn(true);
    }

    protected void runTest() {
        this.mView.startShowing();
    }

    protected void endTest() {
        this.mView.stopShowing();
        this.mView.setKeepScreenOn(false);
    }
}
