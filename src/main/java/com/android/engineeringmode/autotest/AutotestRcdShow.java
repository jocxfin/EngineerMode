package com.android.engineeringmode.autotest;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.android.engineeringmode.Log;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class AutotestRcdShow extends PreferenceActivity {
    private boolean allPassed;
    private String mRcdInfo = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2130968584);
        this.allPassed = true;
        if (!readFromFile()) {
            Log.d("AutotestRcdShow", "" + this.allPassed);
            if (this.allPassed) {
                Preference pref = new Preference(this);
                pref.setTitle(2131296270);
                getPreferenceScreen().addPreference(pref);
            }
        }
    }

    public boolean readFromFile() {
        Log.i("AutotestRcdShow", "in readFromFile()");
        try {
            DataInputStream dis = new DataInputStream(openFileInput("engineermode_failed.data"));
            while (dis.available() > 0) {
                String strFailrcd = dis.readUTF();
                Preference pref = new Preference(this);
                pref.setTitle(strFailrcd);
                getPreferenceScreen().addPreference(pref);
                this.mRcdInfo += strFailrcd + "/";
                this.allPassed = false;
            }
            dis.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e("AutotestRcdShow", "in readFromFile(), FileNotFoundException happen");
            e.printStackTrace();
            return false;
        } catch (IOException ioe) {
            Log.e("AutotestRcdShow", "in readFromFile(), ioException happen");
            ioe.printStackTrace();
            return false;
        }
    }

    protected void onStop() {
        Calendar now = Calendar.getInstance();
        String time = now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13);
        String content;
        if (this.allPassed) {
            content = time + "--AutotestRcdShow--" + this.mRcdInfo + "all passed";
        } else {
            content = time + "--AutotestRcdShow--" + this.mRcdInfo + "fail";
        }
        super.onStop();
    }
}
