package com.android.engineeringmode.autoaging;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.engineeringmode.functions.GPS;
import com.android.engineeringmode.functions.GPS.Callback;
import com.android.engineeringmode.util.MessageCenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GpsTest extends BaseTest implements Callback {
    private final String TAG = "GpsTest";
    private TextView mAccuracyView;
    private ArrayAdapter<String> mAdapter;
    private SimpleDateFormat mDateFormat;
    private GPS mGps;
    private TextView mLatitudeView;
    private TextView mLongitudeView;
    private ArrayList<String> mSatellitesInfo = new ArrayList();
    private ListView mSatellitesList;
    private TextView mSpeedView;
    private StringBuilder mStringBuilder = new StringBuilder();
    private TextView mTimeView;
    private TextView mTtffView;

    public void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setTitle(2131296320);
        setContentView(2130903048);
        this.mTtffView = (TextView) findViewById(2131492898);
        this.mTimeView = (TextView) findViewById(2131492899);
        this.mLongitudeView = (TextView) findViewById(2131492900);
        this.mLatitudeView = (TextView) findViewById(2131492902);
        this.mAccuracyView = (TextView) findViewById(2131492901);
        this.mSpeedView = (TextView) findViewById(2131492903);
        this.mSatellitesList = (ListView) findViewById(2131492904);
        this.mSatellitesList.setEmptyView(findViewById(2131492905));
        this.mAdapter = new ArrayAdapter(this, 2130903049, 2131492906, this.mSatellitesInfo);
        this.mSatellitesList.setAdapter(this.mAdapter);
        this.mGps = new GPS(this);
        this.mGps.setCallback(this);
        this.mDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm");
    }

    protected void runTest() {
        if (this.mGps.isEnabled()) {
            this.mGps.startUpdate();
        } else {
            MessageCenter.showShortMessage((Context) this, 2131296327);
        }
    }

    protected void endTest() {
        this.mGps.stopUpdate();
    }

    public void onLocationChanged(Location location) {
        long time = location.getTime();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float accuracy = location.getAccuracy();
        float speed = location.getSpeed();
        this.mTimeView.setText(this.mDateFormat.format(Long.valueOf(time)));
        this.mLatitudeView.setText(String.valueOf(latitude));
        this.mLongitudeView.setText(String.valueOf(longitude));
        this.mAccuracyView.setText(String.valueOf(accuracy));
        this.mSpeedView.setText(String.valueOf(speed));
    }

    public void onStatusChanged(int status, Bundle extras) {
    }

    public void onProviderDisabled() {
    }

    public void onProviderEnabled() {
    }

    public void onNmeaReceived(long timestamp, String nmea) {
    }

    public void onTtffReceived(int ttff) {
        this.mTtffView.setText(String.valueOf(ttff / 1000));
    }

    public void onGpsStarted() {
    }

    public void onGpsStopped() {
    }

    public void onSatellitesChanged(ArrayList<GpsSatellite> satellites) {
        this.mSatellitesInfo.clear();
        Log.d("GpsTest", "onSatellitesChanged");
        StringBuilder sb = this.mStringBuilder;
        for (GpsSatellite satellite : satellites) {
            if (satellite != null) {
                sb.delete(0, sb.length());
                sb.append("Snr:").append(satellite.getSnr());
                sb.append("\tAzimuth:").append(satellite.getAzimuth()).append("\n");
                sb.append("Elevation:").append(satellite.getElevation());
                sb.append("\tPrn:").append(satellite.getPrn());
                Log.d("GpsTest", "onSatellitesChanged " + sb.toString());
                this.mSatellitesInfo.add(sb.toString());
            }
        }
        this.mSatellitesList.invalidateViews();
        this.mAdapter.notifyDataSetChanged();
        this.mAdapter.notifyDataSetInvalidated();
    }
}
