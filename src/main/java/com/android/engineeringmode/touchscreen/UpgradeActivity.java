package com.android.engineeringmode.touchscreen;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.engineeringmode.functions.Light;

import java.util.ArrayList;
import java.util.List;

public class UpgradeActivity extends Activity implements OnItemClickListener {
    private static final String TAG = null;
    private Object mLock = new Object();
    private int mOldValue;
    private TextView mTextView;
    private int mUpgradeState = -1;
    private int mVersion;

    class UpgradeTask extends AsyncTask<Integer, Integer, Void> {
        UpgradeTask() {
        }

        protected Void doInBackground(Integer... params) {
            Light.touchStartProg(params[0].intValue());
            Log.e("doInbackground", "over");
            return null;
        }

        protected void onPostExecute(Void result) {
            int state = Light.touchGetProgState();
            if (state == 3) {
                UpgradeActivity.this.mTextView.setText(2131297142);
            } else if (state == 4) {
                UpgradeActivity.this.mTextView.setText(2131297143);
            }
            Log.e("AsyncTask", "over upgrade:" + state);
            UpgradeActivity.this.setBackLightTime(UpgradeActivity.this.mOldValue);
            super.onPostExecute(result);
        }

        protected void onPreExecute() {
            UpgradeActivity.this.mTextView.setText(2131297140);
            super.onPreExecute();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903212);
        this.mTextView = (TextView) findViewById(2131493567);
        ListView lv = (ListView) findViewById(2131493568);
        List<String> items = new ArrayList();
        items.add(getText(2131297135) + "");
        items.add(getText(2131297136) + "");
        items.add(getText(2131297137) + "");
        lv.setAdapter(new ArrayAdapter(this, 2130903203, items));
        lv.setOnItemClickListener(this);
    }

    protected void onResume() {
        super.onResume();
        this.mTextView.setText(getResources().getText(2131297138) + ":0x" + Integer.toHexString(Light.touchGetVersion()));
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
        switch (arg2) {
            case 0:
                this.mVersion = 0;
                break;
            case Light.MAIN_KEY_LIGHT /*1*/:
                this.mVersion = 1;
                break;
            case Light.CHARGE_RED_LIGHT /*2*/:
                this.mVersion = 2;
                break;
        }
        this.mOldValue = getBackLightTime();
        setBackLightTime(120000);
        new UpgradeTask().execute(new Integer[]{Integer.valueOf(this.mVersion)});
    }

    protected void onPause() {
        super.onPause();
        setBackLightTime(this.mOldValue);
    }

    private int getBackLightTime() {
        int value = -1;
        try {
            value = System.getInt(getContentResolver(), "screen_off_timeout");
        } catch (NumberFormatException e) {
            Log.e(TAG, "could not persist screen timeout setting", e);
        } catch (SettingNotFoundException ex) {
            Log.e(TAG, "SettingNotFoundException", ex);
        }
        return value;
    }

    private boolean setBackLightTime(int value) {
        boolean ok = false;
        try {
            ok = System.putInt(getContentResolver(), "screen_off_timeout", value);
        } catch (NumberFormatException e) {
            Log.e(TAG, "could not persist screen timeout setting", e);
        }
        return ok;
    }
}
