package com.android.engineeringmode;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.android.engineeringmode.functions.Light;

public class ChargingSettings extends Activity {
    private Button mBtn_100;
    private Button mBtn_60;
    private Button mBtn_80;

    private class BtnOnClickListenner implements OnClickListener {
        private BtnOnClickListenner() {
        }

        public void onClick(View view) {
            if (ChargingSettings.this.mBtn_60 == view) {
                Light.setCharging(60);
                Toast.makeText(ChargingSettings.this, "Set 60%", 0).show();
            } else if (ChargingSettings.this.mBtn_80 == view) {
                Light.setCharging(80);
                Toast.makeText(ChargingSettings.this, "Set 80%", 0).show();
            } else {
                Light.setCharging(100);
                Toast.makeText(ChargingSettings.this, "Set 100%", 0).show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903075);
        setTitle(2131297191);
        this.mBtn_60 = (Button) findViewById(2131493008);
        this.mBtn_80 = (Button) findViewById(2131493009);
        this.mBtn_100 = (Button) findViewById(2131493010);
        this.mBtn_60.setOnClickListener(new BtnOnClickListenner());
        this.mBtn_80.setOnClickListener(new BtnOnClickListenner());
        this.mBtn_100.setOnClickListener(new BtnOnClickListenner());
    }
}
