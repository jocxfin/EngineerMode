package com.android.engineeringmode.supltool;

import android.app.Activity;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SUPLToolTest extends Activity {
    private static Button delButton;
    private static String host;
    private static EditText hostText;
    private static int port;
    private static EditText portText;
    private static int posMode;
    private static Button startButton;
    private static Button stopButton;
    private TextView download;
    private Bundle extra;
    private TextView loc;
    private GpsListener mGpsListener;
    private GpsStatus mGpsStatus;
    private LocListener mLocListener;
    private LocationManager mLocationManager;
    private RadioGroup mode;
    private RadioButton msa;
    private RadioButton msb;
    private RadioButton standalone;
    private TextView ttff;
    private TextView used;
    private TextView view;

    private class GpsListener implements Listener {
        private GpsListener() {
        }

        public void onGpsStatusChanged(int event) {
            Log.d("SuplToolTest", "event: " + event);
            if (event == 3) {
                SUPLToolTest.this.mGpsStatus = SUPLToolTest.this.mLocationManager.getGpsStatus(null);
                SUPLToolTest.this.ttff.setText("TTFF: " + (SUPLToolTest.this.mGpsStatus.getTimeToFirstFix() / 1000) + "s");
            } else if (event == 4) {
                SUPLToolTest.this.updateSatellites();
            }
        }
    }

    private class LocListener implements LocationListener {
        private LocListener() {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }

        public void onLocationChanged(Location location) {
            SUPLToolTest.this.loc.setText("Location: latitude=" + location.getLatitude() + ", longitude=" + location.getLongitude());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903206);
        this.mLocationManager = (LocationManager) getSystemService("location");
        this.mLocListener = new LocListener();
        this.mGpsListener = new GpsListener();
        this.extra = new Bundle();
        hostText = (EditText) findViewById(2131493546);
        portText = (EditText) findViewById(2131493548);
        this.mode = (RadioGroup) findViewById(2131493549);
        this.msa = (RadioButton) findViewById(2131493550);
        this.msb = (RadioButton) findViewById(2131493551);
        this.standalone = (RadioButton) findViewById(2131493552);
        this.view = (TextView) findViewById(2131493553);
        this.used = (TextView) findViewById(2131493554);
        this.download = (TextView) findViewById(2131493555);
        this.loc = (TextView) findViewById(2131493556);
        this.ttff = (TextView) findViewById(2131493557);
        startButton = (Button) findViewById(2131493163);
        stopButton = (Button) findViewById(2131493558);
        delButton = (Button) findViewById(2131493559);
        this.mode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == SUPLToolTest.this.msa.getId()) {
                    SUPLToolTest.posMode = 2;
                } else if (checkedId == SUPLToolTest.this.msb.getId()) {
                    SUPLToolTest.posMode = 1;
                } else if (checkedId == SUPLToolTest.this.standalone.getId()) {
                    SUPLToolTest.posMode = 0;
                } else {
                    SUPLToolTest.posMode = 0;
                }
            }
        });
        startButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SUPLToolTest.this.mLocationManager.removeGpsStatusListener(SUPLToolTest.this.mGpsListener);
                SUPLToolTest.this.mLocationManager.removeUpdates(SUPLToolTest.this.mLocListener);
                SUPLToolTest.host = SUPLToolTest.hostText.getText().toString();
                SUPLToolTest.port = Integer.parseInt(SUPLToolTest.portText.getText().toString());
                Log.d("SuplToolTest", "host:" + SUPLToolTest.host + ", port:" + SUPLToolTest.port + ", mode:" + SUPLToolTest.posMode);
                Toast.makeText(SUPLToolTest.this, "host:" + SUPLToolTest.host + ", port:" + SUPLToolTest.port + ", mode:" + SUPLToolTest.posMode, 0).show();
                SUPLToolTest.this.extra.putString("host", SUPLToolTest.host);
                SUPLToolTest.this.extra.putInt("port", SUPLToolTest.port);
                SUPLToolTest.this.extra.putInt("mode", SUPLToolTest.posMode);
                SUPLToolTest.this.mLocationManager.sendExtraCommand("gps", "set_position_mode", SUPLToolTest.this.extra);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                SUPLToolTest.this.mLocationManager.addGpsStatusListener(SUPLToolTest.this.mGpsListener);
                SUPLToolTest.this.mLocationManager.requestLocationUpdates("gps", 1000, 0.0f, SUPLToolTest.this.mLocListener);
            }
        });
        stopButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(SUPLToolTest.this, "stop", 0).show();
                SUPLToolTest.this.mLocationManager.removeGpsStatusListener(SUPLToolTest.this.mGpsListener);
                SUPLToolTest.this.mLocationManager.removeUpdates(SUPLToolTest.this.mLocListener);
            }
        });
        delButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(SUPLToolTest.this, "del all aiding data", 0).show();
                SUPLToolTest.this.mLocationManager.sendExtraCommand("gps", "delete_aiding_data", null);
            }
        });
    }

    private void updateSatellites() {
        this.mGpsStatus = this.mLocationManager.getGpsStatus(null);
        ArrayList<GpsSatellite> arrayList = new ArrayList();
        int nView = 0;
        int nUsed = 0;
        int nDownload = 0;
        for (GpsSatellite gstate : this.mGpsStatus.getSatellites()) {
            Log.d("SuplToolTest", "satellite: " + gstate.toString());
            if (gstate.getSnr() > 0.0f) {
                arrayList.add(gstate);
                nView++;
                if (gstate.usedInFix()) {
                    nUsed++;
                }
                if (gstate.getAzimuth() > 0.0f && gstate.getElevation() > 0.0f) {
                    nDownload++;
                }
            }
        }
        this.view.setText("view: " + nView);
        this.used.setText("used: " + nUsed);
        this.download.setText("download: " + nDownload);
    }
}
