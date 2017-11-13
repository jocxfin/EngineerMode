package com.android.engineeringmode.gps;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.engineeringmode.KeepScreenOnListActivity;
import com.android.engineeringmode.functions.Light;
import com.android.engineeringmode.util.ExternFunction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class GpsActivity extends KeepScreenOnListActivity {
    private final String COLD_START_COMMAND = "delete_aiding_data";
    private final int JUDGE_NUM = 15;
    private final int NUM_OF_SATE_TO_SAVE = 4;
    private final int NUM_OF_SNR_TO_SAVE = 124;
    private final int SNR_HIGH_THRESHOLD = 46;
    private final int SNR_LOW_THRESHOLD = 38;
    private int delayToRecord = 6;
    private boolean hasJudge;
    private boolean isStartJudge;
    private TextView mAccuracy;
    ArrayAdapter<String> mAdapter;
    private SimpleDateFormat mDateFormat;
    private TextView mEmptyText;
    private ExternFunction mExternFun;
    ArrayList<String> mGateInfolist;
    private boolean mGpsEnabled = false;
    private GpsListener mGpsListener;
    private byte[] mGpsSnr;
    private BroadcastReceiver mGpsStatReceiver;
    private GpsStatus mGpsStatus;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            boolean status;
            switch (msg.what) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    GpsActivity.this.startInit();
                    status = GpsActivity.this.mLocationManager.sendExtraCommand("gps", "delete_aiding_data", null);
                    Log.d("GpsActivity", "COLD_START : still waits for 2 second ");
                    SystemClock.sleep(2000);
                    if (status) {
                        GpsActivity.this.mLocationManager.addGpsStatusListener(GpsActivity.this.mGpsListener);
                        GpsActivity.this.mLocationManager.requestLocationUpdates("gps", 1000, 0.0f, GpsActivity.this.mLocListener);
                        return;
                    }
                    GpsActivity.this.showFailedDialog();
                    return;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    Bundle extras = new Bundle();
                    extras.putBoolean("iono", true);
                    extras.putBoolean("svdir", true);
                    extras.putBoolean("svsteer", true);
                    extras.putBoolean("sadata", true);
                    extras.putBoolean("rti", true);
                    extras.putBoolean("celldb-info", true);
                    extras.putBoolean("ephemeris", true);
                    extras.putBoolean("ephemeris-GLO", true);
                    GpsActivity.this.startInit();
                    status = GpsActivity.this.mLocationManager.sendExtraCommand("gps", "delete_aiding_data", extras);
                    Log.d("GpsActivity", "WARM_START : still waits for a second  though status=" + status);
                    SystemClock.sleep(1000);
                    if (status) {
                        GpsActivity.this.mLocationManager.addGpsStatusListener(GpsActivity.this.mGpsListener);
                        GpsActivity.this.mLocationManager.requestLocationUpdates("gps", 1000, 0.0f, GpsActivity.this.mLocListener);
                        return;
                    }
                    GpsActivity.this.showFailedDialog();
                    return;
                case Light.CHARGE_GREEN_LIGHT /*3*/:
                    Bundle extra = new Bundle();
                    extra.putBoolean("celldb-info", true);
                    GpsActivity.this.startInit();
                    status = GpsActivity.this.mLocationManager.sendExtraCommand("gps", "delete_aiding_data", extra);
                    Log.d("GpsActivity", "HOT_START : still waits for a second though status=" + status);
                    SystemClock.sleep(1000);
                    if (status) {
                        GpsActivity.this.mLocationManager.addGpsStatusListener(GpsActivity.this.mGpsListener);
                        GpsActivity.this.mLocationManager.requestLocationUpdates("gps", 1000, 0.0f, GpsActivity.this.mLocListener);
                        return;
                    }
                    GpsActivity.this.showFailedDialog();
                    return;
                case 5:
                    GpsActivity.this.hasJudge = true;
                    GpsActivity.this.mIsPassed = Boolean.valueOf(false);
                    GpsActivity.this.startActivity(new Intent("com.android.engineeringmode.gps.GpsSearchDialog"));
                    return;
                case Light.MAIN_KEY_NORMAL /*6*/:
                    Message message = Message.obtain(null, 0);
                    message.replyTo = new Messenger(GpsActivity.this.mResultHandler);
                    message.arg1 = 0;
                    Bundle data = new Bundle();
                    data.putString("KEY_RENAME_FROM", Environment.getExternalStorageDirectory().getPath() + "/PCBA.txt");
                    data.putString("KEY_RENAME_TO", Environment.getExternalStorageDirectory().getPath() + "/PCBA.txt");
                    data.putString("pcb", GpsActivity.this.pcbNum);
                    message.setData(data);
                    try {
                        GpsActivity.this.mMessenger.send(message);
                        return;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        return;
                    }
                case 8:
                    GpsActivity.this.pcbNum = "" + GpsActivity.this.getPCBNumber();
                    GpsActivity.this.mHandler.sendEmptyMessageDelayed(6, 2000);
                    return;
                default:
                    return;
            }
        }
    };
    private TextView mInUse;
    private TextView mInView;
    private Boolean mIsPassed = Boolean.valueOf(false);
    private int mJudgeLen;
    private int[] mJudgeSnr;
    private TextView mLatitude;
    private ListView mListView;
    private LocListener mLocListener;
    private LocationProvider mLocProvider;
    private TextView mLocalTime;
    private TextView mLocation;
    private LocationManager mLocationManager;
    private TextView mLongitude;
    private Messenger mMessenger;
    private NmeaListener mNmeaListener;
    private Handler mResultHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.getData().getBoolean("RESULT_RENAME")) {
                Toast.makeText(GpsActivity.this, GpsActivity.this.getResources().getString(2131297229), 0).show();
                return;
            }
            Toast.makeText(GpsActivity.this, GpsActivity.this.getResources().getString(2131297230), 0).show();
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            GpsActivity.this.mMessenger = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            GpsActivity.this.mMessenger = new Messenger(service);
        }
    };
    private TextView mSpeed;
    private IntentFilter mSpsStatFilter;
    private TextView mTextInfo;
    private int numRecord;
    private int passedCount;
    private String pcbNum;
    private int records;

    private class GpsListener implements Listener {
        private GpsListener() {
        }

        public void onGpsStatusChanged(int event) {
            if (event == 3) {
                GpsActivity.this.mGpsStatus = GpsActivity.this.mLocationManager.getGpsStatus(null);
                int ttff = GpsActivity.this.mGpsStatus.getTimeToFirstFix();
                GpsActivity.this.mTextInfo.setText("ttff:" + (ttff / 1000) + "s");
                GpsActivity.this.setTitle((ttff / 1000) + "s");
            }
            if (event == 4) {
                GpsActivity.this.updateSatellites();
            }
        }
    }

    private class GpsStatReceiver extends BroadcastReceiver {
        private GpsStatReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.location.GPS_FIX_CHANGE".equals(intent.getAction()) && !intent.getBooleanExtra("enabled", true)) {
                GpsActivity.this.init();
            }
        }
    }

    private class LocListener implements LocationListener {
        private LocListener() {
        }

        public void onLocationChanged(Location location) {
            System.out.println("onLocationChanged()");
            GpsActivity.this.mAccuracy.setText("Accuracy:" + location.getAccuracy());
            GpsActivity.this.mLatitude.setText("Latitude:" + (((double) Math.round(location.getLatitude() * 10000.0d)) / 10000.0d));
            GpsActivity.this.mLongitude.setText("Longitude:" + (((double) Math.round(location.getLongitude() * 10000.0d)) / 10000.0d));
            GpsActivity.this.mSpeed.setText("Speed:" + location.getSpeed());
            GpsActivity.this.mLocalTime.setText("time:" + GpsActivity.this.mDateFormat.format(Long.valueOf(location.getTime())));
            GpsActivity.this.mLocation.setVisibility(8);
        }

        public void onProviderDisabled(String provider) {
            Log.e("GpsActivity", "onProviderDisabled");
        }

        public void onProviderEnabled(String provider) {
            Log.e("GpsActivity", "onProviderEnabled");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private class Mycomparator implements Comparator<GpsSatellite> {
        private Mycomparator() {
        }

        public int compare(GpsSatellite g1, GpsSatellite g2) {
            return -Float.compare(g1.getSnr(), g2.getSnr());
        }
    }

    private class NmeaListener implements android.location.GpsStatus.NmeaListener {
        private boolean firstReciever = true;
        private boolean recorded = false;

        public void reSet() {
            this.firstReciever = true;
            this.recorded = false;
        }

        public void onNmeaReceived(long timestamp, String nmea) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903102);
        initService();
        this.mLocListener = new LocListener();
        this.mNmeaListener = new NmeaListener();
        this.mGpsListener = new GpsListener();
        this.mExternFun = new ExternFunction(this);
        this.mExternFun.registerOnServiceConnected(this.mHandler, 8, null);
        this.mGateInfolist = new ArrayList();
        this.mAdapter = new ArrayAdapter(this, 2130903203, 2131493145, this.mGateInfolist);
        this.mListView = getListView();
        this.mEmptyText = (TextView) findViewById(16908292);
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setEmptyView(this.mEmptyText);
        this.mTextInfo = (TextView) findViewById(2131493115);
        this.mLatitude = (TextView) findViewById(2131493117);
        this.mLongitude = (TextView) findViewById(2131493118);
        this.mInView = (TextView) findViewById(2131493119);
        this.mInUse = (TextView) findViewById(2131493120);
        this.mSpeed = (TextView) findViewById(2131493121);
        this.mLocalTime = (TextView) findViewById(2131493122);
        this.mAccuracy = (TextView) findViewById(2131493116);
        this.mLocation = (TextView) findViewById(2131493123);
        this.mGpsSnr = new byte[124];
        this.numRecord = 1;
        this.records = 0;
        this.mDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm");
        this.mGpsStatReceiver = new GpsStatReceiver();
        this.mSpsStatFilter = new IntentFilter();
        this.mSpsStatFilter.addAction("android.location.GPS_FIX_CHANGE");
        this.mGpsEnabled = Secure.isLocationProviderEnabled(getContentResolver(), "gps");
    }

    private void enableGps(boolean enable) {
        Secure.setLocationProviderEnabled(getContentResolver(), "gps", enable);
    }

    private void init() {
        this.mTextInfo.setText("ttff:");
        this.mLatitude.setText("Latitude:");
        this.mLongitude.setText("Longitude:");
        this.mInView.setText("in view:");
        this.mInUse.setText("in use:");
        this.mSpeed.setText("speed:");
        this.mLocalTime.setText("time:");
        this.mAccuracy.setText("Accuracy:");
        this.mLocation.setText("");
        setTitle("TTFF:");
    }

    private void startInit() {
        this.mLocationManager = (LocationManager) getSystemService("location");
        this.mLocProvider = this.mLocationManager.getProvider("gps");
        this.mEmptyText.setText(2131296322);
        this.mListView.setAdapter(null);
        this.mGateInfolist = new ArrayList();
        this.mAdapter = new ArrayAdapter(this, 2130903203, 2131493145, this.mGateInfolist);
        this.mListView.setAdapter(this.mAdapter);
        this.delayToRecord = 6;
        this.records = 0;
        this.mGpsSnr = new byte[124];
        this.mNmeaListener.reSet();
        this.mJudgeSnr = new int[15];
        this.mJudgeLen = 0;
        this.isStartJudge = false;
        this.hasJudge = false;
        init();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void onResume() {
        super.onResume();
        if (!this.mGpsEnabled) {
            enableGps(true);
        }
        startInit();
        this.mHandler.sendEmptyMessageDelayed(1, 1500);
        registerReceiver(this.mGpsStatReceiver, this.mSpsStatFilter);
    }

    protected void onPause() {
        super.onPause();
        this.mHandler.removeMessages(5);
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(3);
        this.mHandler.removeMessages(6);
        this.mLocationManager.removeGpsStatusListener(this.mGpsListener);
        this.mLocationManager.removeNmeaListener(this.mNmeaListener);
        try {
            this.mLocationManager.removeUpdates(this.mLocListener);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        unregisterReceiver(this.mGpsStatReceiver);
        if (!this.mGpsEnabled) {
            Log.e("ww", "disable");
            enableGps(false);
        }
    }

    private void updateSatellites() {
        this.mGpsStatus = this.mLocationManager.getGpsStatus(null);
        int ttff = this.mGpsStatus.getTimeToFirstFix();
        ArrayList<GpsSatellite> arraylist = new ArrayList();
        int i = 0;
        int inView = 0;
        for (GpsSatellite gsate : this.mGpsStatus.getSatellites()) {
            GpsSatellite gsate2;
            if (gsate2.getSnr() > 0.0f) {
                Object obj = (((float) gsate2.getPrn()) < 33.0f || ((float) gsate2.getPrn()) > 64.0f) ? null : 1;
                if (obj == null) {
                    arraylist.add(gsate2);
                    if (gsate2.usedInFix()) {
                        i++;
                    }
                    inView++;
                }
                if (this.delayToRecord < 0 && this.records < 122) {
                    byte[] bArr = this.mGpsSnr;
                    int i2 = this.records;
                    this.records = i2 + 1;
                    bArr[i2] = (byte) gsate2.getPrn();
                    bArr = this.mGpsSnr;
                    i2 = this.records;
                    this.records = i2 + 1;
                    bArr[i2] = (byte) Math.round(gsate2.getSnr());
                }
                if (!this.isStartJudge) {
                    this.isStartJudge = true;
                    this.passedCount = 0;
                    this.mHandler.sendEmptyMessageDelayed(5, 20000);
                }
            }
        }
        GpsActivity gpsActivity = this;
        Collections.sort(arraylist, new Mycomparator());
        this.mGateInfolist.clear();
        for (int k = 0; k < arraylist.size(); k++) {
            gsate2 = (GpsSatellite) arraylist.get(k);
            StringBuilder sb = new StringBuilder();
            sb.append("Snr:").append(gsate2.getSnr());
            sb.append("\tAzimuth:").append(gsate2.getAzimuth()).append("\n");
            sb.append("Elevation:").append(gsate2.getElevation());
            sb.append("\tPrn:").append(gsate2.getPrn());
            this.mGateInfolist.add(sb.toString());
        }
        if (arraylist.size() > 0) {
            gsate2 = (GpsSatellite) arraylist.get(0);
            if (gsate2.getSnr() < 38.0f || gsate2.getSnr() > 46.0f) {
                this.passedCount = 0;
            } else {
                this.passedCount++;
                if (this.passedCount >= 5 && !this.hasJudge) {
                    this.mHandler.removeMessages(5);
                    this.hasJudge = true;
                    if (!isFinishing()) {
                        try {
                            TextView view = new TextView(this);
                            view.setGravity(17);
                            view.setTextSize(40.0f);
                            view.setText("Pass!");
                            view.setTextColor(-16711936);
                            this.mIsPassed = Boolean.valueOf(true);
                            new Builder(this).setTitle("Pass").setView(view).setPositiveButton(17039370, null).show();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        this.delayToRecord--;
        this.mAdapter.notifyDataSetChanged();
        this.mAdapter.notifyDataSetInvalidated();
        this.mInUse.setText("in use:" + i);
        this.mInView.setText("in view:" + inView);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, 2131296323);
        menu.add(0, 2, 0, 2131296324);
        menu.add(0, 3, 0, 2131296325);
        menu.add(0, 4, 0, "view records");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 1) {
            this.mLocationManager.removeGpsStatusListener(this.mGpsListener);
            this.mLocationManager.removeUpdates(this.mLocListener);
            clearData();
            this.mHandler.sendEmptyMessageDelayed(1, 1500);
            return true;
        } else if (id == 2) {
            this.mLocationManager.removeGpsStatusListener(this.mGpsListener);
            this.mLocationManager.removeUpdates(this.mLocListener);
            clearData();
            this.mHandler.sendEmptyMessageDelayed(2, 1500);
            return true;
        } else if (id == 3) {
            this.mLocationManager.removeGpsStatusListener(this.mGpsListener);
            this.mLocationManager.removeUpdates(this.mLocListener);
            clearData();
            this.mHandler.sendEmptyMessageDelayed(3, 1500);
            return true;
        } else if (id != 4) {
            return super.onOptionsItemSelected(item);
        } else {
            startActivity(new Intent(this, GpsViewRecords.class));
            return true;
        }
    }

    private void clearData() {
        this.mGateInfolist = new ArrayList();
        this.mAdapter = new ArrayAdapter(this, 2130903203, 2131493145, this.mGateInfolist);
        this.mListView.setAdapter(this.mAdapter);
        this.mHandler.removeMessages(5);
    }

    private void showFailedDialog() {
        new Builder(this).setTitle("Failed").setMessage(2131296326).setPositiveButton(17039370, null).show();
        this.mEmptyText.setText("Stop search");
        this.mListView.setAdapter(null);
    }

    protected void onStop() {
        Calendar now = Calendar.getInstance();
        String time = now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13);
        String content;
        if (this.mIsPassed.booleanValue()) {
            this.mExternFun.setProductLineTestFlagExtraByte(20, (byte) 1);
            content = time + "--GpsActivity--" + "PASS";
        } else {
            this.mExternFun.setProductLineTestFlagExtraByte(20, (byte) 2);
            content = time + "--GpsActivity--" + "FAIL";
        }
        super.onStop();
    }

    private void initService() {
        Intent intent = new Intent("com.oppo.sdcard.command");
        intent.setPackage("com.oneplus.sdcardservice");
        bindService(intent, this.mServiceConnection, 1);
    }

    protected void onDestroy() {
        unbindService(this.mServiceConnection);
        this.mExternFun.unregisterOnServiceConnected(this.mHandler);
        this.mExternFun.dispose();
        super.onDestroy();
    }

    public String getPCBNumber() {
        return this.mExternFun.getPCBNumber();
    }
}
