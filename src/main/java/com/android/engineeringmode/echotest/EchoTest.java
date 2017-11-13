package com.android.engineeringmode.echotest;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.engineeringmode.Log;
import com.android.engineeringmode.audioutils.AudioTestUtils;
import com.android.engineeringmode.autotest.AutoTestItemActivity;

import java.util.Calendar;

public class EchoTest extends AutoTestItemActivity implements OnClickListener {
    private static String prjname;
    private Button mAnc;
    private Boolean mAncPassed = Boolean.valueOf(false);
    private AudioManager mAudioManager = null;
    private AudioTestUtils mAudioTest;
    private Button mClose;
    private Boolean mClosePassed = Boolean.valueOf(false);
    private Button mOpen;
    private boolean mbIsmicOpen;
    private TextView mtvShowInfo = null;

    public Boolean isThreeMicProject() {
        prjname = SystemProperties.get("ro.boot.project_name");
        if (prjname.compareTo("16859") == 0) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903092);
        setTitle(2131296338);
        this.mOpen = (Button) findViewById(2131493077);
        this.mClose = (Button) findViewById(2131493078);
        this.mAnc = (Button) findViewById(2131493079);
        this.mOpen.setOnClickListener(this);
        this.mClose.setOnClickListener(this);
        if (isThreeMicProject().booleanValue()) {
            this.mAnc.setOnClickListener(this);
        }
        this.mOpen.setEnabled(false);
        if (!isThreeMicProject().booleanValue()) {
            this.mAnc.setVisibility(8);
        }
        this.mAudioTest = new AudioTestUtils(this);
        this.mtvShowInfo = (TextView) findViewById(2131493076);
        this.mAudioManager = (AudioManager) getSystemService("audio");
        this.mbIsmicOpen = this.mAudioManager.isMicrophoneMute();
        if (checkIsAutoTest() || checkIsAutoAging()) {
            setAutoExit(12000);
        }
        finishMusic(this);
        setRequestedOrientation(1);
    }

    protected void onStart() {
        super.onStart();
        startEchoTest();
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == 2131493077) {
            this.mOpen.setEnabled(false);
            this.mClose.setEnabled(true);
            this.mAnc.setEnabled(true);
            this.mAudioTest.setparameter("LoopBackTest=main");
        } else if (id == 2131493078) {
            this.mOpen.setEnabled(true);
            this.mClose.setEnabled(false);
            this.mAnc.setEnabled(true);
            this.mClosePassed = Boolean.valueOf(true);
            this.mAudioTest.setparameter("LoopBackTest=sec");
        } else {
            this.mOpen.setEnabled(true);
            this.mClose.setEnabled(true);
            this.mAnc.setEnabled(false);
            this.mAncPassed = Boolean.valueOf(true);
            this.mAudioTest.setparameter("LoopBackTest=nsmic");
        }
    }

    public void startEchoTest() {
        this.mAudioTest.setparameter("LoopBackTest=main");
    }

    public void endEchoTest() {
        this.mAudioTest.setparameter("LoopBackTest=false");
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
        Log.d("EchoTest", "onStop()");
        Calendar now = Calendar.getInstance();
        String time = now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13);
        String content;
        if (this.mClosePassed.booleanValue() && this.mAncPassed.booleanValue()) {
            content = time + "--EchoTest--" + "All of the three items has been tested";
        } else {
            content = time + "--EchoTest--" + "not all of the three items has been tested";
        }
        endEchoTest();
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public static final void finishMusic(Context ctx) {
        AudioManager am = (AudioManager) ctx.getSystemService("audio");
        if (am == null) {
            Log.w("EchoTest", "isMusicActive: couldn't get AudioManager reference");
            return;
        }
        if (am.isMusicActive()) {
            Intent intent = new Intent("com.oppo.music.musicservicecommand.pause");
            Log.v("EchoTest", "Broadcast : " + intent.getAction());
            ctx.sendBroadcast(intent);
        } else {
            Log.v("EchoTest", "Music no active.");
        }
    }
}
