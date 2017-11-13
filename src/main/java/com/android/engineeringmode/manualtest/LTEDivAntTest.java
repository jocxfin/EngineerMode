package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.internal.telephony.Phone;

public class LTEDivAntTest extends Activity implements OnClickListener {
    private Boolean mClosePressed = Boolean.valueOf(false);
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    AsyncResult ar = msg.obj;
                    return;
                default:
                    return;
            }
        }
    };
    private EditText mInput;
    private Boolean mOpenPressed = Boolean.valueOf(false);
    private Phone mPhone = null;
    private Button mPriOpen;

    public static native boolean sendData(int i);

    static {
        System.loadLibrary("diagdci");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903144);
        this.mInput = (EditText) findViewById(2131493341);
        this.mPriOpen = (Button) findViewById(2131493342);
        this.mPriOpen.setOnClickListener(this);
    }

    protected void onResume() {
        super.onResume();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131493342:
                if (!this.mInput.getText().toString().isEmpty()) {
                    sendData(Integer.valueOf(this.mInput.getText().toString()).intValue());
                    Log.d("LTEDivAntTest", "open lte pri ant  " + this.mInput.getText());
                    return;
                }
                return;
            default:
                return;
        }
    }

    protected void onStop() {
        super.onStop();
    }
}
