package com.android.engineeringmode.manualtest;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import java.util.Calendar;

public class MediaTest extends PreferenceActivity {
    private volatile Preference currAudio = null;
    private Boolean lock = new Boolean(true);
    private MediaPlayer mMediaPlayer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(2130968604);
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        Calendar now;
        String content;
        if ("audio_test_1".equals(preference.getKey())) {
            now = Calendar.getInstance();
            content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--MediaTest/audioTest--" + "has entered it";
            palyAudio(2131034112, preference);
            return true;
        } else if ("audio_test_2".equals(preference.getKey())) {
            palyAudio(2131034113, preference);
            return true;
        } else if (!"vedio_test_1".equals(preference.getKey())) {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        } else {
            now = Calendar.getInstance();
            content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--MediaTest/vedioTest--" + "has entered it";
            Intent intent = new Intent(this, VideoTest.class);
            intent.setData(Uri.parse("android.resource://" + getPackageName() + "/" + 2131034119));
            startActivity(intent);
            return true;
        }
    }

    private void palyAudio(int resId, Preference preference) {
        synchronized (this.lock) {
            if (this.currAudio != null) {
                if (this.currAudio == preference) {
                    stopAudio();
                    return;
                }
                stopAudio();
            }
            if (this.mMediaPlayer != null) {
                this.mMediaPlayer.stop();
                this.mMediaPlayer.release();
                this.mMediaPlayer = null;
            }
            this.currAudio = preference;
            this.mMediaPlayer = MediaPlayer.create(this, resId);
            this.mMediaPlayer.setAudioStreamType(3);
            this.mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (MediaTest.this.currAudio != null) {
                        MediaTest.this.currAudio.setSummary(null);
                        MediaTest.this.currAudio = null;
                    }
                }
            });
            this.mMediaPlayer.start();
            preference.setSummary(2131297186);
        }
    }

    private void stopAudio() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        if (this.currAudio != null) {
            this.currAudio.setSummary(null);
            this.currAudio = null;
        }
    }

    protected void onPause() {
        super.onPause();
        stopAudio();
    }

    protected void onResume() {
        super.onResume();
        this.mMediaPlayer = null;
        this.currAudio = null;
    }
}
