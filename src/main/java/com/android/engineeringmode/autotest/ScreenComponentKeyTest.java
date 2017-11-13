package com.android.engineeringmode.autotest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ServiceManager;
import android.util.Log;
import android.view.IWindowManager.Stub;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.KeepScreenOnActivity;
import com.android.engineeringmode.functions.Light;
import com.oem.util.Feature;

import java.util.HashMap;
import java.util.Map.Entry;

public class ScreenComponentKeyTest extends KeepScreenOnActivity {
    private static boolean mIsOnePlus = false;
    private boolean isInModelTest = false;
    private int mCurrentTime = 0;
    private Button mExitButton;
    private Handler mHandler = null;
    private HashMap<Integer, String> mKeyValues;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903131);
        setTitle(2131296692);
        mIsOnePlus = Feature.isThreeStageKeySupported(this);
        this.mExitButton = (Button) findViewById(2131493266);
        this.mExitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ScreenComponentKeyTest.this.finish();
            }
        });
        this.mExitButton.setVisibility(8);
        initHashMap();
        initLayout();
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
        if (this.isInModelTest) {
            this.mHandler = new Handler();
        }
        Light.setElectric(1, Light.MAIN_KEY_MAX);
    }

    protected void onResume() {
        super.onResume();
        Intent intent = new Intent("com.oem.intent.action.KEY_LOCK_MODE");
        intent.putExtra("KeyLockMode", 2);
        intent.putExtra("ProcessName", "com.android.engineeringmode");
        sendBroadcast(intent);
    }

    protected void onPause() {
        super.onPause();
        Intent intent = new Intent("com.oem.intent.action.KEY_LOCK_MODE");
        intent.putExtra("KeyLockMode", 0);
        intent.putExtra("ProcessName", "com.android.engineeringmode");
        sendBroadcast(intent);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("ScreenComponentKeyTest", "onKeyDown KEY:" + keyCode);
        View v = findViewById(keyCode);
        if (v != null) {
            v.setVisibility(4);
            this.mKeyValues.remove(Integer.valueOf(keyCode));
        }
        refreshTestResult();
        return true;
    }

    private void refreshTestResult() {
        if (this.mKeyValues.size() <= 0) {
            Toast.makeText(this, "all keys have been pressed", 0).show();
            if (this.isInModelTest) {
                setResult(1);
                if (this.mHandler != null) {
                    this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            ScreenComponentKeyTest.this.finish();
                        }
                    }, 2000);
                    return;
                } else {
                    finish();
                    return;
                }
            }
            finish();
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("ScreenComponentKeyTest", "onKeyUp KEY:" + keyCode);
        return true;
    }

    private void initHashMap() {
        int[] keysToTest;
        String[] keyValues;
        if (isHasNavigationBar()) {
            keysToTest = new int[]{4, 187};
            keyValues = new String[]{"back", "app_switch"};
        } else {
            keysToTest = new int[]{4, 187};
            keyValues = new String[]{"back", "app_switch"};
        }
        if (mIsOnePlus) {
            keysToTest = new int[]{4, 187};
            keyValues = new String[]{"back", "app_switch"};
        }
        this.mKeyValues = new HashMap();
        for (int i = 0; i < keysToTest.length; i++) {
            this.mKeyValues.put(Integer.valueOf(keysToTest[i]), keyValues[i]);
        }
    }

    private void initLayout() {
        int[] layouts = new int[]{2131493257, 2131493258, 2131493259, 2131493260, 2131493261, 2131493262, 2131493263, 2131493264, 2131493265};
        LayoutInflater inflater = LayoutInflater.from(this);
        int i = 0;
        for (Entry<Integer, String> entry : this.mKeyValues.entrySet()) {
            LinearLayout layout = (LinearLayout) findViewById(layouts[i / 1]);
            TextView tv = (TextView) inflater.inflate(2130903091, null);
            tv.setId(((Integer) entry.getKey()).intValue());
            Log.d("ScreenComponentKeyTest", "initKeyTestLayout:" + entry.getKey());
            tv.setPadding(0, 0, 5, 0);
            tv.setText((CharSequence) entry.getValue());
            Log.d("ScreenComponentKeyTest", "initKeyTestLayout:" + ((String) entry.getValue()));
            layout.addView(tv);
            i++;
        }
    }

    public boolean isHasNavigationBar() {
        boolean mHasNavigationBar = false;
        try {
            mHasNavigationBar = Stub.asInterface(ServiceManager.getService("window")).hasNavigationBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mHasNavigationBar;
    }
}
