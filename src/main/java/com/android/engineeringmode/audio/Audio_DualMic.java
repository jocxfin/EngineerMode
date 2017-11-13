package com.android.engineeringmode.audio;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Audio_DualMic extends Activity implements OnClickListener {
    private AudioManager mAudioManager = null;
    private Button mClose;
    private RadioGroup mDualMic;
    private Button mOpen;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903043);
        this.mAudioManager = (AudioManager) getSystemService("audio");
        this.mOpen = (Button) findViewById(2131492879);
        this.mClose = (Button) findViewById(2131492880);
        this.mOpen.setOnClickListener(this);
        this.mClose.setOnClickListener(this);
        this.mDualMic = (RadioGroup) findViewById(2131492882);
        this.mDualMic.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (arg0.getCheckedRadioButtonId() == 2131492883) {
                    Audio_DualMic.this.mAudioManager.setParameters("dualmic_enabled=main");
                } else if (arg0.getCheckedRadioButtonId() == 2131492884) {
                    Audio_DualMic.this.mAudioManager.setParameters("dualmic_enabled=sec");
                }
            }
        });
    }

    public void onClick(View v) {
        if (v.getId() == 2131492879) {
            this.mAudioManager.setParameters("dualmic_enabled=true");
            this.mOpen.setEnabled(false);
            this.mClose.setEnabled(true);
        } else if (v.getId() == 2131492880) {
            this.mAudioManager.setParameters("dualmic_enabled=false");
            this.mOpen.setEnabled(true);
            this.mClose.setEnabled(false);
        }
    }
}
