package com.android.engineeringmode.gps;

import android.app.Service;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import com.android.engineeringmode.functions.Light;

import java.util.HashMap;

public class GpsEnvService extends Service {
    private static String TAG = "GpsEnvService";
    private HashMap<Integer, Float> hashmap = new HashMap();
    private MyBinder mBinder = new MyBinder();
    private Listener mGpsListener = new Listener() {
        public void onGpsStatusChanged(int event) {
            GpsStatus status = GpsEnvService.this.mLocationManager.getGpsStatus(null);
            switch (event) {
                case 4:
                    GpsEnvService.this.caculate_satellite_snr(status);
                    return;
                default:
                    return;
            }
        }
    };
    private final LocationListener mLocListener = new LocationListener() {
        public void onLocationChanged(Location location) {
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    private LocationManager mLocationManager = null;
    private Handler mcallBack;
    private Handler mhandler;
    private PowerManager pm;
    private float snr_value = 0.0f;
    private volatile int type;

    public class MyBinder extends Binder {
        GpsEnvService getService() {
            return GpsEnvService.this;
        }
    }

    private void caculate_satellite_snr(GpsStatus status) {
        for (GpsSatellite sate : status.getSatellites()) {
            float snr = sate.getSnr();
            int pnr = sate.getPrn();
            if (pnr == 29 && snr != 0.0f) {
                Log.d(TAG, "snr =" + snr + "pnr =" + pnr);
                if (this.hashmap.containsKey(Integer.valueOf(pnr))) {
                    float snr_prv = ((Float) this.hashmap.get(Integer.valueOf(pnr))).floatValue();
                    Log.d(TAG, "snr value is =" + snr_prv);
                    snr = (snr + snr_prv) / 2.0f;
                    this.snr_value = snr;
                    Log.e(TAG, "snr value is =" + this.snr_value);
                    this.hashmap.put(Integer.valueOf(pnr), Float.valueOf(snr));
                } else {
                    this.hashmap.put(Integer.valueOf(pnr), Float.valueOf(snr));
                }
            }
        }
    }

    public IBinder onBind(Intent arg0) {
        Log.d(TAG, "onBind()");
        return this.mBinder;
    }

    public void onCreate() {
        Log.d(TAG, "onCreate()");
        super.onCreate();
        this.mhandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Log.d(GpsEnvService.TAG, "wakeup()");
                        GpsEnvService.this.pm.wakeUp(SystemClock.uptimeMillis());
                        GpsEnvService.this.mLocationManager.removeUpdates(GpsEnvService.this.mLocListener);
                        Message mMessage = Message.obtain(GpsEnvService.this.mcallBack, GpsEnvService.this.type, Float.valueOf(GpsEnvService.this.snr_value));
                        if (mMessage != null && mMessage.getTarget() != null) {
                            mMessage.sendToTarget();
                            return;
                        }
                        return;
                    case Light.MAIN_KEY_LIGHT /*1*/:
                        Log.d(GpsEnvService.TAG, "wakeup()");
                        GpsEnvService.this.pm.wakeUp(SystemClock.uptimeMillis());
                        Intent intent_0 = new Intent(GpsEnvService.this, GpsEnvCameraActivity.class);
                        intent_0.addFlags(268435456);
                        GpsEnvService.this.startActivity(intent_0);
                        return;
                    default:
                        return;
                }
            }
        };
        this.pm = (PowerManager) getSystemService("power");
        this.mLocationManager = (LocationManager) getSystemService("location");
        if (this.mLocationManager != null) {
            this.mLocationManager.addGpsStatusListener(this.mGpsListener);
        }
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        this.mhandler.removeMessages(0);
        this.mhandler.removeMessages(1);
        this.mLocationManager.removeGpsStatusListener(this.mGpsListener);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind()");
        return super.onUnbind(intent);
    }

    public void set_lcd_off() {
        this.pm.goToSleep(SystemClock.uptimeMillis());
        requestLocationUpdate(5);
        this.mhandler.sendMessageDelayed(this.mhandler.obtainMessage(1), 31000);
    }

    public void requestLocationUpdate(int type) {
        this.type = type;
        Log.d(TAG, "type == " + type);
        this.mLocationManager.requestLocationUpdates("gps", 1000, 0.0f, this.mLocListener);
        if (type == 5) {
            this.mhandler.sendMessageDelayed(this.mhandler.obtainMessage(0), 30000);
        } else {
            this.mhandler.sendMessageDelayed(this.mhandler.obtainMessage(0), 15000);
        }
    }

    public void setHandler(Handler callBack) {
        this.mcallBack = callBack;
    }
}
