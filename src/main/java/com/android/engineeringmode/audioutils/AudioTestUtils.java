package com.android.engineeringmode.audioutils;

import android.content.Context;

public class AudioTestUtils {
    private Context mContext;

    public native int getMaxAmplitude();

    public native int setparameter(String str);

    public AudioTestUtils(Context context) {
        this.mContext = context;
    }

    static {
        System.loadLibrary("audiotest_jni");
    }
}
