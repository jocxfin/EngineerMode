package com.android.engineeringmode.manualtest.modeltest;

import android.os.Bundle;

import com.android.engineeringmode.LCDtest.LCDTestView;
import com.android.engineeringmode.LCDtest.LCDTestView.OnFinishListener;
import com.android.engineeringmode.functions.Light;

public class ModelLcdColorTest extends ModelTest3ItemActivity implements OnFinishListener {
    private LCDTestView mLcdTestView = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        setContentView(2130903136);
        this.mLcdTestView = (LCDTestView) findViewById(2131493319);
        this.mLcdTestView.setRepeatTime(1);
        this.mLcdTestView.setIsAutoShowing(false);
        this.mLcdTestView.setIsTouchable(true);
        this.mLcdTestView.setNeedShowPicture(true);
        this.mLcdTestView.setOnFinishListener(this);
        this.mLcdTestView.startShowing();
        setRequestedOrientation(1);
        Light.setElectric(1, Light.MAIN_KEY_MAX);
    }

    public void onFinished() {
        this.mLcdTestView.stopShowing();
        onTestPassed();
    }
}
