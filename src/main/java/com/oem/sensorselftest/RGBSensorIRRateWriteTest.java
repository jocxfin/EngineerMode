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

public class RGBSensorIRRateWriteTest extends Activity implements OnClickListener, SensorEventListener {
    private Button btn_write;
    private Sensor cctSensor;
    private TextView cctvalue;
    private TextView cmdresult;
    private EditText editText;
    private Sensor lightSensor;
    private TextView lightvalue;
    SensorManager mSensorManager;
    private Sensor rgbSensor;
    private TextView rgbvalue;

    private class SensorTestTask extends AsyncTask<Object, Void, Integer> {
        private SensorTestTask() {
        }

        protected Integer doInBackground(Object... params) {
            return Integer.valueOf(SensorTest.sendDAFRequest(1, 100000 + ((Integer) params[0]).intValue()));
        }

        protected void onPostExecute(Integer testResult) {
            if (testResult.intValue() == 0) {
                Toast.makeText(RGBSensorIRRateWriteTest.this, "IRRATE WRITE SUCCESS", 0).show();
                Log.d("RGBSensorIRRateWriteTest:", "IRRATE WRITE SUCCESS");
                return;
            }
            Toast.makeText(RGBSensorIRRateWriteTest.this, "IRRATE WRITE FAIL", 0).show();
            Log.d("RGBSensorIRRateWriteTest:", "IRRATE WRITE FAIL");
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903194);
        this.editText = (EditText) findViewById(2131493330);
        this.btn_write = (Button) findViewById(2131493331);
        this.btn_write.setOnClickListener(this);
        this.lightvalue = (TextView) findViewById(2131493508);
        this.rgbvalue = (TextView) findViewById(2131493509);
        this.cctvalue = (TextView) findViewById(2131493510);
        this.cmdresult = (TextView) findViewById(2131493511);
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.lightSensor = this.mSensorManager.getDefaultSensor(5);
        this.rgbSensor = this.mSensorManager.getDefaultSensor(33171013);
        this.cctSensor = this.mSensorManager.getDefaultSensor(33171020);
        this.mSensorManager.registerListener(this, this.lightSensor, 3);
        this.mSensorManager.registerListener(this, this.rgbSensor, 3);
        this.mSensorManager.registerListener(this, this.cctSensor, 3);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case 2131493331:
                String irrate_string = this.editText.getText().toString();
                if (irrate_string == null || irrate_string == "" || irrate_string.trim().length() == 0) {
                    Toast.makeText(this, "Please input offset value", 0).show();
                    return;
                }
                int irrate = Integer.parseInt(irrate_string);
                if (irrate < 1 || irrate > 1000) {
                    Toast.makeText(this, "invalid value", 0).show();
                    return;
                }
                new SensorTestTask().execute(new Object[]{Integer.valueOf(irrate)});
                return;
            default:
                return;
        }
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == this.lightSensor) {
            this.lightvalue.setText("Light: " + String.valueOf(event.values[0]));
        } else if (event.sensor == this.rgbSensor) {
            this.rgbvalue.setText("R: " + String.valueOf(event.values[0]) + "\nG: " + String.valueOf(event.values[1]) + "\nB: " + String.valueOf(event.values[2]));
        } else if (event.sensor == this.cctSensor) {
            Log.d("RGBSensorIRRateWriteTest:", " cct value: " + String.valueOf(event.values[0]) + String.valueOf(event.values[1]));
            Float f = new Float(event.values[1]);
            int clear_raw = f.intValue() / 100;
            this.cctvalue.setText("C: " + String.valueOf(clear_raw) + "\nCCT: " + String.valueOf(event.values[0]) + "\ngain: " + String.valueOf(f.intValue() - (clear_raw * 100)));
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("RGBSensorIRRateWriteTest:", " Accuracy Changed: " + accuracy);
    }

    public void onResume() {
        super.onResume();
        Log.d("RGBSensorIRRateWriteTest:", "onResume");
    }

    public void onPause() {
        super.onPause();
        Log.d("RGBSensorIRRateWriteTest:", "onPause");
    }

    public void onDestroy() {
        super.onDestroy();
        this.mSensorManager.unregisterListener(this);
        Log.d("RGBSensorIRRateWriteTest:", "onDestroy");
    }
}
