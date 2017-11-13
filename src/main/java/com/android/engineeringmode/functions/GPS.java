package com.android.engineeringmode.functions;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GPS {
    private final String START_COMMAND = "delete_aiding_data";
    private WeakReference<Callback> mCallback;
    private GpsStatusChangedListener mGpsStatusListener;
    private LocationListenerImpl mLocationListener;
    private LocationManager mLocationManager;
    private float mMinDistance = 0.0f;
    private long mMinTime = 1000;
    private NmeaListenerImpl mNmeaListener;
    private boolean mOtherListenerRegistered = false;
    private boolean mUpdateListenerRegistered = false;

    public interface Callback {
        void onGpsStarted();

        void onGpsStopped();

        void onLocationChanged(Location location);

        void onNmeaReceived(long j, String str);

        void onProviderDisabled();

        void onProviderEnabled();

        void onSatellitesChanged(ArrayList<GpsSatellite> arrayList);

        void onStatusChanged(int i, Bundle bundle);

        void onTtffReceived(int i);
    }

    private class GpsStatusChangedListener implements Listener {
        private GpsStatus mGpsStatus;
        private SatelliteComparator mSatelliteComparator = new SatelliteComparator();

        private class SatelliteComparator implements Comparator<GpsSatellite> {
            private SatelliteComparator() {
            }

            public int compare(GpsSatellite g1, GpsSatellite g2) {
                return -Float.compare(g1.getSnr(), g2.getSnr());
            }
        }

        public GpsStatusChangedListener() {
            this.mGpsStatus = GPS.this.mLocationManager.getGpsStatus(null);
        }

        public void onGpsStatusChanged(int event) {
            Callback callback = GPS.this.getCallback();
            if (callback != null) {
                if (3 == event) {
                    GPS.this.mLocationManager.getGpsStatus(this.mGpsStatus);
                    callback.onTtffReceived(this.mGpsStatus.getTimeToFirstFix());
                } else if (1 == event) {
                    callback.onGpsStarted();
                } else if (2 == event) {
                    callback.onGpsStopped();
                } else if (4 == event) {
                    GPS.this.mLocationManager.getGpsStatus(this.mGpsStatus);
                    callback.onSatellitesChanged(obtainSatellites());
                }
            }
        }

        private ArrayList<GpsSatellite> obtainSatellites() {
            ArrayList<GpsSatellite> arraylist = new ArrayList();
            for (GpsSatellite satellite : this.mGpsStatus.getSatellites()) {
                arraylist.add(satellite);
            }
            Collections.sort(arraylist, this.mSatelliteComparator);
            return arraylist;
        }
    }

    private class LocationListenerImpl implements LocationListener {
        private LocationListenerImpl() {
        }

        public void onLocationChanged(Location location) {
            Callback callback = GPS.this.getCallback();
            if (callback != null) {
                callback.onLocationChanged(location);
            }
        }

        public void onProviderDisabled(String provider) {
            Callback callback = GPS.this.getCallback();
            if (callback != null) {
                callback.onProviderDisabled();
            }
        }

        public void onProviderEnabled(String provider) {
            Callback callback = GPS.this.getCallback();
            if (callback != null) {
                callback.onProviderEnabled();
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Callback callback = GPS.this.getCallback();
            if (callback != null) {
                callback.onStatusChanged(status, extras);
            }
        }
    }

    private class NmeaListenerImpl implements NmeaListener {
        private NmeaListenerImpl() {
        }

        public void onNmeaReceived(long timestamp, String nmea) {
            Callback callback = GPS.this.getCallback();
            if (callback != null) {
                callback.onNmeaReceived(timestamp, nmea);
            }
        }
    }

    public GPS(Context context) {
        this.mLocationManager = (LocationManager) context.getSystemService("location");
        initListeners();
    }

    private void initListeners() {
        this.mLocationListener = new LocationListenerImpl();
        this.mNmeaListener = new NmeaListenerImpl();
        this.mGpsStatusListener = new GpsStatusChangedListener();
    }

    private void registerUpdateListener() {
        if (!this.mUpdateListenerRegistered) {
            this.mLocationManager.requestLocationUpdates("gps", this.mMinTime, this.mMinDistance, this.mLocationListener);
            this.mUpdateListenerRegistered = true;
        }
    }

    private void unRegisterUpdateListener() {
        if (this.mUpdateListenerRegistered) {
            try {
                this.mLocationManager.removeUpdates(this.mLocationListener);
            } catch (IllegalArgumentException e) {
            }
            this.mUpdateListenerRegistered = false;
        }
    }

    private void registerOtherListeners() {
        if (!this.mOtherListenerRegistered) {
            this.mLocationManager.addGpsStatusListener(this.mGpsStatusListener);
            this.mLocationManager.addNmeaListener(this.mNmeaListener);
            this.mOtherListenerRegistered = true;
        }
    }

    private void unRegisterOtherListeners() {
        if (this.mOtherListenerRegistered) {
            this.mLocationManager.removeGpsStatusListener(this.mGpsStatusListener);
            this.mLocationManager.removeNmeaListener(this.mNmeaListener);
            this.mOtherListenerRegistered = false;
        }
    }

    public void setCallback(Callback callback) {
        WeakReference weakReference = null;
        if (callback != null) {
            weakReference = new WeakReference(callback);
        }
        this.mCallback = weakReference;
    }

    public Callback getCallback() {
        if (this.mCallback == null) {
            return null;
        }
        return (Callback) this.mCallback.get();
    }

    public boolean isEnabled() {
        return this.mLocationManager.isProviderEnabled("gps");
    }

    public void startUpdate() {
        registerUpdateListener();
        registerOtherListeners();
    }

    public void stopUpdate() {
        unRegisterUpdateListener();
        unRegisterOtherListeners();
    }

    public void finalize() throws Throwable {
        stopUpdate();
        super.finalize();
    }
}
