package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DoubleFlashLight extends Activity implements OnClickListener, OnSeekBarChangeListener {
    private static Boolean isOn;
    private static int mInterval;
    private Button both_flash;
    private int class_code;
    private Button close_both_flash;
    private Button close_cold_flash;
    private Button close_flash_both;
    private Button close_flash_cold;
    private Button close_flash_warm;
    private Button close_warm_flash;
    private Button cold_flash;
    private Button finish;
    private SeekBar mSeekBar;
    private TextView mText;
    private Button open_both_flash;
    private Button open_cold_flash;
    private Button open_warm_flash;
    private Button warm_flash;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903087);
        setTitle(2131297632);
        this.open_warm_flash = (Button) findViewById(2131493047);
        this.open_warm_flash.setOnClickListener(this);
        this.close_warm_flash = (Button) findViewById(2131493048);
        this.close_warm_flash.setOnClickListener(this);
        this.open_cold_flash = (Button) findViewById(2131493049);
        this.open_cold_flash.setOnClickListener(this);
        this.close_cold_flash = (Button) findViewById(2131493050);
        this.close_cold_flash.setOnClickListener(this);
        this.open_both_flash = (Button) findViewById(2131493051);
        this.open_both_flash.setOnClickListener(this);
        this.close_both_flash = (Button) findViewById(2131493052);
        this.close_both_flash.setOnClickListener(this);
        this.mText = (TextView) findViewById(2131493053);
        this.mSeekBar = (SeekBar) findViewById(2131493054);
        this.mSeekBar.setOnSeekBarChangeListener(this);
        this.warm_flash = (Button) findViewById(2131493055);
        this.warm_flash.setOnClickListener(this);
        this.close_flash_warm = (Button) findViewById(2131493056);
        this.close_flash_warm.setOnClickListener(this);
        this.cold_flash = (Button) findViewById(2131493057);
        this.cold_flash.setOnClickListener(this);
        this.close_flash_cold = (Button) findViewById(2131493058);
        this.close_flash_cold.setOnClickListener(this);
        this.both_flash = (Button) findViewById(2131493059);
        this.both_flash.setOnClickListener(this);
        this.close_flash_both = (Button) findViewById(2131493060);
        this.close_flash_both.setOnClickListener(this);
        this.finish = (Button) findViewById(2131493061);
        this.finish.setOnClickListener(this);
        isOn = Boolean.valueOf(false);
        this.class_code = -1;
        this.mSeekBar.setProgress(1);
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        turnOff();
        super.onPause();
    }

    protected void onDestroy() {
        turnOff();
        super.onDestroy();
    }

    public void onClick(View v) {
        if (v.getId() == this.finish.getId()) {
            turnOff();
            finish();
            return;
        }
        action(v.getId());
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mInterval = progress + 1;
        this.mText.setText(String.format("闪烁间隔：%d秒", new Object[]{Integer.valueOf(mInterval)}));
    }

    private void action(int viewId) {
        if (viewId == this.open_warm_flash.getId()) {
            this.open_warm_flash.setClickable(false);
            this.close_warm_flash.setClickable(true);
            this.open_cold_flash.setClickable(false);
            this.close_cold_flash.setClickable(false);
            this.open_both_flash.setClickable(false);
            this.close_both_flash.setClickable(false);
            this.warm_flash.setClickable(false);
            this.close_flash_warm.setClickable(false);
            this.cold_flash.setClickable(false);
            this.close_flash_cold.setClickable(false);
            this.both_flash.setClickable(false);
            this.close_flash_both.setClickable(false);
            turnOn(2);
        } else if (viewId == this.close_warm_flash.getId()) {
            turnOff();
        } else if (viewId == this.open_cold_flash.getId()) {
            this.open_warm_flash.setClickable(false);
            this.close_warm_flash.setClickable(false);
            this.open_cold_flash.setClickable(false);
            this.close_cold_flash.setClickable(true);
            this.open_both_flash.setClickable(false);
            this.close_both_flash.setClickable(false);
            this.warm_flash.setClickable(false);
            this.close_flash_warm.setClickable(false);
            this.cold_flash.setClickable(false);
            this.close_flash_cold.setClickable(false);
            this.both_flash.setClickable(false);
            this.close_flash_both.setClickable(false);
            turnOn(1);
        } else if (viewId == this.close_cold_flash.getId()) {
            turnOff();
        } else if (viewId == this.open_both_flash.getId()) {
            this.open_warm_flash.setClickable(false);
            this.close_warm_flash.setClickable(false);
            this.open_cold_flash.setClickable(false);
            this.close_cold_flash.setClickable(false);
            this.open_both_flash.setClickable(false);
            this.close_both_flash.setClickable(true);
            this.mSeekBar.setClickable(false);
            this.warm_flash.setClickable(false);
            this.close_flash_warm.setClickable(false);
            this.cold_flash.setClickable(false);
            this.close_flash_cold.setClickable(false);
            this.both_flash.setClickable(false);
            this.close_flash_both.setClickable(false);
            turnOn(3);
        } else if (viewId == this.close_both_flash.getId()) {
            turnOff();
        } else if (viewId == this.warm_flash.getId()) {
            this.open_warm_flash.setClickable(false);
            this.close_warm_flash.setClickable(false);
            this.open_cold_flash.setClickable(false);
            this.close_cold_flash.setClickable(false);
            this.open_both_flash.setClickable(false);
            this.close_both_flash.setClickable(false);
            this.warm_flash.setClickable(false);
            this.close_flash_warm.setClickable(true);
            this.cold_flash.setClickable(false);
            this.close_flash_cold.setClickable(false);
            this.both_flash.setClickable(false);
            this.close_flash_both.setClickable(false);
            this.class_code = 2;
            flashing();
        } else if (viewId == this.close_flash_warm.getId()) {
            turnOff();
        } else if (viewId == this.cold_flash.getId()) {
            this.open_warm_flash.setClickable(false);
            this.close_warm_flash.setClickable(false);
            this.open_cold_flash.setClickable(false);
            this.close_cold_flash.setClickable(false);
            this.open_both_flash.setClickable(false);
            this.close_both_flash.setClickable(false);
            this.warm_flash.setClickable(false);
            this.close_flash_warm.setClickable(false);
            this.cold_flash.setClickable(false);
            this.close_flash_cold.setClickable(true);
            this.both_flash.setClickable(false);
            this.close_flash_both.setClickable(false);
            this.class_code = 1;
            flashing();
        } else if (viewId == this.close_flash_cold.getId()) {
            turnOff();
        } else if (viewId == this.both_flash.getId()) {
            this.open_warm_flash.setClickable(false);
            this.close_warm_flash.setClickable(false);
            this.open_cold_flash.setClickable(false);
            this.close_cold_flash.setClickable(false);
            this.open_both_flash.setClickable(false);
            this.close_both_flash.setClickable(false);
            this.warm_flash.setClickable(false);
            this.close_flash_warm.setClickable(false);
            this.cold_flash.setClickable(false);
            this.close_flash_cold.setClickable(false);
            this.both_flash.setClickable(false);
            this.close_flash_both.setClickable(true);
            this.class_code = 3;
            flashing();
        } else if (viewId == this.close_flash_both.getId()) {
            turnOff();
        }
    }

    private void turnOn(int class_code) {
        if (class_code == 1) {
            WriteNodeValue("/sys/class/leds/led:torch_1/brightness", "1");
            WriteNodeValue("/sys/class/leds/led:switch_0/brightness", "1");
        } else if (class_code == 2) {
            WriteNodeValue("/sys/class/leds/led:torch_0/brightness", "1");
            WriteNodeValue("/sys/class/leds/led:switch_0/brightness", "1");
        } else if (class_code == 3) {
            WriteNodeValue("/sys/class/leds/led:torch_0/brightness", "1");
            WriteNodeValue("/sys/class/leds/led:torch_1/brightness", "1");
            WriteNodeValue("/sys/class/leds/led:switch_0/brightness", "1");
        }
    }

    private void turnOff() {
        WriteNodeValue("/sys/class/leds/led:torch_0/brightness", "0");
        WriteNodeValue("/sys/class/leds/led:torch_1/brightness", "0");
        WriteNodeValue("/sys/class/leds/led:switch_0/brightness", "0");
        isOn = Boolean.valueOf(false);
        this.open_warm_flash.setClickable(true);
        this.close_warm_flash.setClickable(true);
        this.open_cold_flash.setClickable(true);
        this.close_cold_flash.setClickable(true);
        this.open_both_flash.setClickable(true);
        this.close_both_flash.setClickable(true);
        this.warm_flash.setClickable(true);
        this.close_flash_warm.setClickable(true);
        this.cold_flash.setClickable(true);
        this.close_flash_cold.setClickable(true);
        this.both_flash.setClickable(true);
        this.close_flash_both.setClickable(true);
    }

    private void flashing() {
        isOn = Boolean.valueOf(true);
        new Thread(new Runnable() {
            public void run() {
                while (DoubleFlashLight.isOn.booleanValue()) {
                    if (DoubleFlashLight.this.class_code == 1) {
                        DoubleFlashLight.this.WriteNodeValue("/sys/class/leds/led:torch_1/brightness", "1");
                        DoubleFlashLight.this.WriteNodeValue("/sys/class/leds/led:switch_0/brightness", "1");
                    } else if (DoubleFlashLight.this.class_code == 2) {
                        DoubleFlashLight.this.WriteNodeValue("/sys/class/leds/led:torch_0/brightness", "1");
                        DoubleFlashLight.this.WriteNodeValue("/sys/class/leds/led:switch_0/brightness", "1");
                    } else if (DoubleFlashLight.this.class_code == 3) {
                        DoubleFlashLight.this.WriteNodeValue("/sys/class/leds/led:torch_0/brightness", "1");
                        DoubleFlashLight.this.WriteNodeValue("/sys/class/leds/led:torch_1/brightness", "1");
                        DoubleFlashLight.this.WriteNodeValue("/sys/class/leds/led:switch_0/brightness", "1");
                    }
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                    }
                    DoubleFlashLight.this.WriteNodeValue("/sys/class/leds/led:torch_0/brightness", "0");
                    DoubleFlashLight.this.WriteNodeValue("/sys/class/leds/led:torch_1/brightness", "0");
                    DoubleFlashLight.this.WriteNodeValue("/sys/class/leds/led:switch_0/brightness", "0");
                    try {
                        Thread.sleep((long) (DoubleFlashLight.mInterval * 1000));
                    } catch (InterruptedException e2) {
                    }
                }
            }
        }).start();
    }

    private void WriteNodeValue(String nodePath, String value) {
        try {
            FileWriter writer = new FileWriter(new File(nodePath));
            writer.write(String.valueOf(value));
            writer.close();
        } catch (IOException e) {
            Log.e("DoubleFlashLight", "writeNodeValue IO exception:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
