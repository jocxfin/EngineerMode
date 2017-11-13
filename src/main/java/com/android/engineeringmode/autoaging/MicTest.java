package com.android.engineeringmode.autoaging;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

import com.android.engineeringmode.functions.Mic;
import com.android.engineeringmode.functions.Mic.Callback;
import com.android.engineeringmode.functions.TimeNotifier;
import com.android.engineeringmode.util.MessageCenter;

public class MicTest extends BaseTest implements Callback, TimeNotifier.Callback {
    private TextView mInfoView;
    private Mic mMic = new Mic();
    private TimeNotifier mTimeNotifier;
    private TextView mTimeView;

    public void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setTitle(2131296903);
        setContentView(2130903051);
        this.mInfoView = (TextView) findViewById(2131492911);
        this.mTimeView = (TextView) findViewById(2131492912);
        this.mTimeView.setText(getFormatTime(57600000));
        this.mMic.setCallback(this);
        this.mTimeNotifier = new TimeNotifier();
        this.mTimeNotifier.setCallback(this);
    }

    protected void runTest() {
        this.mMic.start();
    }

    protected void endTest() {
        this.mMic.stop();
    }

    public void onRecordStart() {
        this.mInfoView.setText(2131296905);
        this.mTimeNotifier.start();
    }

    public void onRecordStop() {
        this.mInfoView.setText(2131296907);
        this.mTimeNotifier.stop();
    }

    public void onRecordError() {
        this.mInfoView.setText(2131296910);
        this.mTimeNotifier.stop();
    }

    public void onRecordInfo(int what) {
    }

    public void onStorageError(int what) {
        this.mInfoView.setText(2131296910);
        this.mTimeNotifier.stop();
        if (1 == what) {
            MessageCenter.showShortMessage((Context) this, 2131296917);
        } else if (2 == what) {
            MessageCenter.showShortMessage((Context) this, 2131296918);
        } else {
            MessageCenter.showShortMessage((Context) this, 2131296919);
        }
    }

    public void onUpdate(long startTime, long currentTime) {
        long duration = currentTime - startTime;
        Log.d("xxxx", "" + duration);
        this.mTimeView.setText(getFormatTime(57600000 + duration));
    }

    private String getFormatTime(long time) {
        return DateFormat.format("kk:mm:ss", time).toString();
    }
}
