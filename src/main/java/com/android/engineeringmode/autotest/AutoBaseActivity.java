package com.android.engineeringmode.autotest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.engineeringmode.Log;

public abstract class AutoBaseActivity extends Activity {
    protected AutoTestBaseManager mAutoTestManager = null;

    public abstract AutoTestBaseManager newTestManager();

    public abstract void onAllTestEnd();

    public abstract void onOneTestEnd(Intent intent);

    public abstract void onTestFailed(String str);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAutoTestManager = newTestManager();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("AutoBaseActivity", "onActivityResult(), requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (32 == resultCode) {
            onAllTestEnd();
            finish();
        } else if (16 == resultCode) {
            onAllTestEnd();
        } else {
            if (1 == requestCode) {
                if (18 == resultCode) {
                    onTestFailed(this.mAutoTestManager.getCurTitle());
                } else {
                    onOneTestEnd(data);
                }
            } else if (2 == requestCode) {
                if (data != null) {
                    if (data.hasExtra("key_isneedretest")) {
                        boolean bRetest = data.getBooleanExtra("key_isneedretest", false);
                        Log.d("AutoBaseActivity", "onActivityResult(), bRetest = " + bRetest);
                        if (bRetest) {
                            startNormalActivityWithData(this.mAutoTestManager.getCurIntent());
                            return;
                        }
                    }
                    if (data.hasExtra("key_isPassed")) {
                        boolean bSuc = data.getBooleanExtra("key_isPassed", false);
                        Log.d("AutoBaseActivity", "onActivityResult(), bSuc = " + bSuc + ", mAutoTestPraser = " + this.mAutoTestManager);
                        if (!bSuc) {
                            onTestFailed(this.mAutoTestManager.getCurTitle());
                        }
                        if (this.mAutoTestManager.hasNext()) {
                            this.mAutoTestManager.gotoNext();
                            Intent intent = this.mAutoTestManager.getCurIntent();
                            if (intent == null) {
                                onAllTestEnd();
                                return;
                            }
                            startNormalActivityWithData(intent);
                        } else {
                            onAllTestEnd();
                        }
                    }
                } else {
                    Log.w("AutoBaseActivity", "onActivityResult(), the data = null");
                    onAllTestEnd();
                }
            }
        }
    }

    public void beginTest() {
        this.mAutoTestManager.beginTest();
        if (this.mAutoTestManager.hasNext()) {
            this.mAutoTestManager.gotoNext();
            Intent intent = this.mAutoTestManager.getCurIntent();
            if (intent == null) {
                onAllTestEnd();
                return;
            } else {
                startNormalActivityWithData(intent);
                return;
            }
        }
        onAllTestEnd();
    }

    public final void startNormalActivityWithData(Intent intent) {
        if (intent != null) {
            int index = this.mAutoTestManager.getCurIndex();
            long duration = this.mAutoTestManager.getParser().getItemDuration(index);
            boolean toggled = this.mAutoTestManager.getParser().isItemToggled(index);
            intent.putExtra("test_duration", duration);
            intent.putExtra("test_toggled", toggled);
            startActivityForResult(intent, 1);
        }
    }

    public final void startPassUnpassActivity(Intent intent) {
        if (intent != null) {
            startActivityForResult(intent, 2);
        }
    }
}
