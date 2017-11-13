package com.oem.sensorselftest;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qualcomm.qti.sensors.core.sensortest.SensorTest;

public class LightSensorOffsetTest extends Activity implements OnClickListener, SensorEventListener {
    private Button btn_write;
    private EditText editText;
    private Sensor mLightSensor;
    SensorManager mSensorManager;
    private TextView result_text;
    private TextView textView2;

    private class SensorTestTask extends AsyncTask<Object, Void, Integer> {
        private SensorTestTask() {
        }

        protected Integer doInBackground(Object... params) {
            return Integer.valueOf(SensorTest.runSensorOffsetTest(40, 1, ((Integer) params[0]).intValue(), true, true));
        }

        protected void onPostExecute(Integer testResult) {
            if (testResult.intValue() == 0) {
                LightSensorOffsetTest.this.result_text.setText("SUCCESS");
            } else {
                LightSensorOffsetTest.this.result_text.setText("FAILED");
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903139);
        this.textView2 = (TextView) findViewById(2131493110);
        this.editText = (EditText) findViewById(2131493330);
        this.btn_write = (Button) findViewById(2131493331);
        this.result_text = (TextView) findViewById(2131493332);
        this.btn_write.setOnClickListener(this);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mLightSensor = this.mSensorManager.getDefaultSensor(5);
        this.mSensorManager.registerListener(this, this.mLightSensor, 3);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case 2131493331:
                String offset_string = this.editText.getText().toString();
                if (offset_string == null || offset_string == "" || offset_string.trim().length() == 0) {
                    Toast.makeText(this, "Please input offset value", 0).show();
                    return;
                }
                int offset = Integer.parseInt(offset_string);
                if (offset <= 0 || offset >= 256) {
                    Toast.makeText(this, "invalid value", 0).show();
                    return;
                }
                Toast.makeText(this, Integer.toString(offset), 0).show();
                new SensorTestTask().execute(new Object[]{Integer.valueOf(offset)});
                return;
            default:
                return;
        }
    }

    public void onSensorChanged(SensorEvent event) {
        this.textView2.setText(getResources().getString(2131296977, new Object[]{Float.toString(event.values[0])}));
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("LightSensorOffsetTest", " Accuracy Changed: " + accuracy);
    }

    public void onResume() {
        super.onResume();
        Log.d("LightSensorOffsetTest", "onResume");
    }

    public void onPause() {
        super.onPause();
        Log.d("LightSensorOffsetTest", "onPause");
    }

    public void onDestroy() {
        super.onDestroy();
        this.mSensorManager.unregisterListener(this);
        Log.d("LightSensorOffsetTest", "onDestroy");
    }
}
