package com.android.engineeringmode;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AutoAnswer extends Activity implements OnClickListener {
    private Button mSetButton;

    public void onCreate(Bundle savedInstanceState) {
        boolean buttonFlag;
        super.onCreate(savedInstanceState);
        setContentView(2130903054);
        this.mSetButton = (Button) findViewById(2131492921);
        this.mSetButton.setOnClickListener(this);
        SharedPreferences autoAnswerSh = getSharedPreferences("AutoAnswer", 0);
        if (getPackageManager().hasSystemFeature("oppo.cmcc.optr") || getPackageManager().hasSystemFeature("oppo.cta.support")) {
            buttonFlag = autoAnswerSh.getBoolean("flag", false);
        } else {
            buttonFlag = autoAnswerSh.getBoolean("flag", true);
        }
        Log.v("EM-AutoAnswer", "onCreate flag is :" + buttonFlag);
        if (buttonFlag) {
            this.mSetButton.setText(2131297098);
        } else {
            this.mSetButton.setText(2131297097);
        }
    }

    public void onClick(View arg0) {
        if (arg0 != this.mSetButton) {
            return;
        }
        if (getString(2131297097).equals(this.mSetButton.getText())) {
            SystemProperties.set("persist.radio.ptcrb.enable", "true");
            this.mSetButton.setText(2131297098);
            writeSharedPreferences(true);
            return;
        }
        SystemProperties.set("persist.radio.ptcrb.enable", "false");
        this.mSetButton.setText(2131297097);
        writeSharedPreferences(false);
    }

    private void writeSharedPreferences(boolean flag) {
        Editor editor = getSharedPreferences("AutoAnswer", 0).edit();
        editor.putBoolean("flag", flag);
        editor.commit();
    }
}
