package com.android.engineeringmode.audio;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Audio_ParamSetting extends Activity {
    private ArrayAdapter<String> LevelAdatper;
    private final int MAX_LEVEL = 15;
    private final int MAX_VOLUMN = 100;
    private final String TAG = "Audio_ParamSetting";
    private EditText mEditText;
    private int mMode;
    private TextView mSeekInfo;
    private Button mSetBtn;
    private Spinner mSpinner;
    private int mType;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903045);
        this.mMode = getIntent().getIntExtra("current_mode", -1);
        this.mType = getIntent().getIntExtra("current_type", -1);
        this.mSpinner = (Spinner) findViewById(2131492891);
        this.mSetBtn = (Button) findViewById(2131492894);
        this.mSeekInfo = (TextView) findViewById(2131492893);
        this.mEditText = (EditText) findViewById(2131492892);
        this.LevelAdatper = new ArrayAdapter(this, 17367048);
        this.LevelAdatper.setDropDownViewResource(17367049);
        for (int i = 0; i <= 15; i++) {
            this.LevelAdatper.add("Level " + i);
        }
        this.mSpinner.setAdapter(this.LevelAdatper);
        this.mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
