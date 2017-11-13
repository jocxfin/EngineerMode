package com.android.engineeringmode.autoaging;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.engineeringmode.functions.HDMI;
import com.android.engineeringmode.functions.HDMI.Callback;
import com.android.engineeringmode.functions.HDMI.Resolution;
import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.functions.VideoPlayer;
import com.android.engineeringmode.parameter.GlobalParameter;
import com.android.engineeringmode.parameter.GlobalParameter.FileDescriptor;
import com.android.engineeringmode.util.MessageCenter;

public class MhlTest extends BaseTest implements Callback, VideoPlayer.Callback {
    private MhlHandler mHandler;
    private HDMI mHdmi;
    private long mInterval;
    private boolean mRunning = false;
    private VideoPlayer mVideoPlayer;

    private class MhlHandler extends Handler {
        private static final /* synthetic */ int[] -com-android-engineeringmode-functions-HDMI$ResolutionSwitchesValues =null;
        private Resolution mResolution;

        private static /* synthetic */ int[] -getcom-android-engineeringmode-functions-

        HDMI$ResolutionSwitchesValues() {
            if (-com - android - engineeringmode - functions - HDMI$ResolutionSwitchesValues != null) {
                return -com - android - engineeringmode - functions - HDMI$ResolutionSwitchesValues;
            }
            int[] iArr = new int[Resolution.values().length];
            try {
                iArr[Resolution._1080.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Resolution._480.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Resolution._576.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Resolution._720.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[Resolution._AUTO.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            -com - android - engineeringmode - functions - HDMI$ResolutionSwitchesValues = iArr;
            return iArr;
        }

        private MhlHandler() {
            this.mResolution = null;
        }

        public void handleMessage(Message msg) {
            if (this.mResolution != null && MhlTest.this.mRunning && MhlTest.this.mHdmi.isConnected()) {
                if (false) {
                    MessageCenter.showShortMessage(MhlTest.this, MhlTest.this.getString(2131296791) + this.mResolution.toString());
                } else {
                    MessageCenter.showShortMessage(MhlTest.this, MhlTest.this.getString(2131296792) + this.mResolution.toString());
                }
                stepNext();
                sendEmptyMessageDelayed(0, MhlTest.this.mInterval);
            }
        }

        public void start() {
            this.mResolution = Resolution._AUTO;
            sendEmptyMessage(0);
        }

        public void stop() {
            removeMessages(0);
            this.mResolution = null;
        }

        private void stepNext() {
            switch (-getcom - android - engineeringmode - functions - HDMI$ResolutionSwitchesValues()[this.mResolution.ordinal()]) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    this.mResolution = Resolution._720;
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    this.mResolution = Resolution._AUTO;
                    return;
                case Light.CHARGE_GREEN_LIGHT /*3*/:
                    this.mResolution = Resolution._480;
                    return;
                case 4:
                    this.mResolution = Resolution._576;
                    return;
                case 5:
                    this.mResolution = Resolution._1080;
                    return;
                default:
                    return;
            }
        }
    }

    protected void onInit(Bundle savedInstanceState) {
        long j;
        super.onInit(savedInstanceState);
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        setRequestedOrientation(0);
        setTitle(2131297122);
        setContentView(2130903222);
        this.mHdmi = new HDMI(this);
        this.mHdmi.setCallback(this);
        FileDescriptor videoDescriptor = GlobalParameter.getVideoDescriptor();
        this.mVideoPlayer = (VideoPlayer) findViewById(2131493594);
        this.mVideoPlayer.setCallback(this);
        this.mVideoPlayer.setVideoURI(videoDescriptor.getUri());
        this.mHandler = new MhlHandler();
        long duration = getDuration();
        if (-1 == duration) {
            j = 5000;
        } else {
            j = duration / ((long) this.mHdmi.getSupportResolutionCount());
        }
        this.mInterval = j;
    }

    protected void runTest() {
        if (this.mHdmi.enable()) {
            if (!this.mHdmi.isConnected()) {
                MessageCenter.showLongMessage((Context) this, 2131296788);
            }
            this.mHandler.start();
            startPlay();
            return;
        }
        MessageCenter.showShortMessage((Context) this, 2131296789);
    }

    protected void endTest() {
        if (!this.mHdmi.disable()) {
            MessageCenter.showShortMessage((Context) this, 2131296790);
        }
        this.mHandler.stop();
        stopPlay();
    }

    public void onConnected() {
        MessageCenter.showShortMessage((Context) this, 2131296786);
    }

    public void onDisconnected() {
        MessageCenter.showShortMessage((Context) this, 2131296787);
    }

    private void startPlay() {
        if (!this.mRunning) {
            this.mVideoPlayer.start();
            this.mRunning = true;
        }
    }

    private void stopPlay() {
        if (this.mRunning) {
            this.mVideoPlayer.stopPlayback();
            this.mRunning = false;
        }
    }

    public void onComplete() {
        this.mRunning = false;
        startPlay();
    }

    public void onError() {
        MessageCenter.showLongMessage((Context) this, 2131296285);
        recordResult(false);
        this.mRunning = false;
        endTest();
        finish();
    }
}
