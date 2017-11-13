package com.android.engineeringmode.LCDtest;

import android.os.Bundle;

import com.android.engineeringmode.LCDtest.LCDTestView.OnFinishListener;
import com.android.engineeringmode.autotest.AutoTestItemActivity;

import java.util.Calendar;

public class LCDColorTest extends AutoTestItemActivity implements OnFinishListener {
    private LCDTestView mLcdTestView = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        setContentView(2130903136);
        this.mLcdTestView = (LCDTestView) findViewById(2131493319);
        if (getIntent().hasExtra("key_set_special_color")) {
            this.mLcdTestView.showColor(getIntent().getIntExtra("key_set_special_color", 0));
        } else {
            boolean z;
            this.mLcdTestView.setRepeatTime(1);
            LCDTestView lCDTestView = this.mLcdTestView;
            if (checkIsAutoTest()) {
                z = true;
            } else {
                z = checkIsAutoAging();
            }
            lCDTestView.setIsAutoShowing(z);
            this.mLcdTestView.setIsTouchable(true);
            this.mLcdTestView.setNeedShowPicture(true);
            this.mLcdTestView.setOnFinishListener(this);
            this.mLcdTestView.startShowing();
        }
        setRequestedOrientation(1);
    }

    public void onBackPressed() {
        this.mLcdTestView.stopShowing();
        super.onBackPressed();
    }

    protected void onPause() {
        super.onPause();
        this.mLcdTestView.onDestroy();
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void onFinished() {
        Calendar now = Calendar.getInstance();
        String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--LCDColorTest--" + "test all items";
        this.mLcdTestView.stopShowing();
        endActivity();
    }
}
