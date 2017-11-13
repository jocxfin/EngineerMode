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

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

public class AntennaSwitchTest extends Activity implements OnClickListener {
    private Button btnOffGpio01;
    private Button btnOffGpio02;
    private Button btnOffGpio03;
    private Button btnOffGpio04;
    private Button btnOffGpio05;
    private Button btnOffGpio06;
    private Button btnOffGpio07;
    private Button btnOnGpio01;
    private Button btnOnGpio02;
    private Button btnOnGpio03;
    private Button btnOnGpio04;
    private Button btnOnGpio05;
    private Button btnOnGpio06;
    private Button btnOnGpio07;
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
    private Phone mPhone = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903041);
        if (this.mPhone == null) {
            this.mPhone = PhoneFactory.getDefaultPhone();
        }
        this.btnOnGpio01 = (Button) findViewById(2131492864);
        this.btnOffGpio01 = (Button) findViewById(2131492865);
        this.btnOnGpio02 = (Button) findViewById(2131492866);
        this.btnOffGpio02 = (Button) findViewById(2131492867);
        this.btnOnGpio03 = (Button) findViewById(2131492868);
        this.btnOffGpio03 = (Button) findViewById(2131492869);
        this.btnOnGpio04 = (Button) findViewById(2131492870);
        this.btnOffGpio04 = (Button) findViewById(2131492871);
        this.btnOnGpio05 = (Button) findViewById(2131492872);
        this.btnOffGpio05 = (Button) findViewById(2131492873);
        this.btnOnGpio06 = (Button) findViewById(2131492874);
        this.btnOffGpio06 = (Button) findViewById(2131492875);
        this.btnOnGpio07 = (Button) findViewById(2131492876);
        this.btnOffGpio07 = (Button) findViewById(2131492877);
        this.btnOnGpio01.setOnClickListener(this);
        this.btnOffGpio01.setOnClickListener(this);
        this.btnOnGpio02.setOnClickListener(this);
        this.btnOffGpio02.setOnClickListener(this);
        this.btnOnGpio03.setOnClickListener(this);
        this.btnOffGpio03.setOnClickListener(this);
        this.btnOnGpio04.setOnClickListener(this);
        this.btnOffGpio04.setOnClickListener(this);
        this.btnOnGpio05.setOnClickListener(this);
        this.btnOffGpio05.setOnClickListener(this);
        this.btnOnGpio06.setOnClickListener(this);
        this.btnOffGpio06.setOnClickListener(this);
        this.btnOnGpio07.setOnClickListener(this);
        this.btnOffGpio07.setOnClickListener(this);
    }

    public void onClick(View view) {
        this.btnOnGpio01.setTextColor(-1);
        this.btnOffGpio01.setTextColor(-1);
        this.btnOnGpio02.setTextColor(-1);
        this.btnOffGpio02.setTextColor(-1);
        this.btnOnGpio03.setTextColor(-1);
        this.btnOffGpio03.setTextColor(-1);
        this.btnOnGpio04.setTextColor(-1);
        this.btnOffGpio04.setTextColor(-1);
        this.btnOnGpio05.setTextColor(-1);
        this.btnOffGpio05.setTextColor(-1);
        this.btnOnGpio06.setTextColor(-1);
        this.btnOffGpio06.setTextColor(-1);
        this.btnOnGpio07.setTextColor(-1);
        this.btnOffGpio07.setTextColor(-1);
        ((Button) view).setTextColor(-16776961);
        Message msg = this.mHandler.obtainMessage(100);
        Log.d("AntennaSwitchTest", "AntennaSwitchTest onClick:" + view.getId());
        switch (view.getId()) {
            case 2131492864:
                this.mPhone.setFactoryModeModemGPIO(1, 97, msg);
                return;
            case 2131492865:
                this.mPhone.setFactoryModeModemGPIO(0, 97, msg);
                return;
            case 2131492866:
                this.mPhone.setFactoryModeModemGPIO(1, 98, msg);
                return;
            case 2131492867:
                this.mPhone.setFactoryModeModemGPIO(0, 98, msg);
                return;
            case 2131492868:
                this.mPhone.setFactoryModeModemGPIO(1, 100, msg);
                return;
            case 2131492869:
                this.mPhone.setFactoryModeModemGPIO(0, 100, msg);
                return;
            case 2131492870:
                this.mPhone.setFactoryModeModemGPIO(1, 115, msg);
                return;
            case 2131492871:
                this.mPhone.setFactoryModeModemGPIO(0, 115, msg);
                return;
            case 2131492872:
                this.mPhone.setFactoryModeModemGPIO(1, 127, msg);
                return;
            case 2131492873:
                this.mPhone.setFactoryModeModemGPIO(0, 127, msg);
                return;
            case 2131492874:
                this.mPhone.setFactoryModeModemGPIO(1, 133, msg);
                return;
            case 2131492875:
                this.mPhone.setFactoryModeModemGPIO(0, 133, msg);
                return;
            case 2131492876:
                this.mPhone.setFactoryModeModemGPIO(1, 136, msg);
                return;
            case 2131492877:
                this.mPhone.setFactoryModeModemGPIO(0, 136, msg);
                return;
            default:
                return;
        }
    }
}
