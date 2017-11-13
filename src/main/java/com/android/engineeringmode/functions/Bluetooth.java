package com.android.engineeringmode.functions;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Bluetooth {
    private final int MAX_DEVICE_TO_SEARCH = 15;
    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.w("BluetoothSearch", "action = " + action);
            Callback callback = Bluetooth.this.getCallback();
            if (action.equals("android.bluetooth.device.action.FOUND")) {
                Log.v("BluetoothSearch", "Receive action ACTION_FOUND");
                String name = intent.getStringExtra("android.bluetooth.device.extra.NAME");
                short rssi = intent.getShortExtra("android.bluetooth.device.extra.RSSI", Short.MIN_VALUE);
                String address = ((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getAddress();
                if (!Bluetooth.this.mJustSearchAudio) {
                    Bluetooth.this.appendNewDevice(name, address, rssi);
                } else if (((BluetoothClass) intent.getParcelableExtra("android.bluetooth.device.extra.CLASS")).getMajorDeviceClass() == 1024) {
                    Bluetooth.this.appendNewDevice(name, address, rssi);
                }
            } else if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                Log.w("BluetoothSearch", "Receive action BLUETOOTH_STATE_CHANGED_ACTION, state = " + state);
                switch (state) {
                    case Integer.MIN_VALUE:
                        if (callback != null) {
                            callback.onError();
                            return;
                        }
                        return;
                    case 10:
                        if (callback != null) {
                            callback.onClosed();
                            return;
                        }
                        return;
                    case 11:
                        if (callback != null) {
                            callback.onOpenning();
                            return;
                        }
                        return;
                    case 12:
                        if (callback != null) {
                            callback.onOpened();
                            return;
                        }
                        return;
                    case 13:
                        if (callback != null) {
                            callback.onClosing();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else if (action.equals("android.bluetooth.adapter.action.DISCOVERY_STARTED")) {
                if (callback != null) {
                    callback.onSearchStart();
                }
            } else if (action.equals("android.bluetooth.adapter.action.DISCOVERY_FINISHED") && callback != null) {
                callback.onSearchEnd();
            }
        }
    };
    private WeakReference<Callback> mCallback;
    private Context mContext;
    private boolean mJustSearchAudio = false;
    private int mMaxSearchedDevices = 15;
    private boolean mReceiverRegistered = false;
    private ArrayList<Device> mSearchedDevices = new ArrayList();

    public interface Callback {
        void onClosed();

        void onClosing();

        void onError();

        void onOpened();

        void onOpenning();

        void onSearchEnd();

        void onSearchStart();

        void onSearched(Device device);
    }

    public class Device {
        private final String mAddress;
        private final String mName;
        private final short mRSSI;

        public Device(String name, String address, short rssi) {
            this.mName = name;
            this.mAddress = address;
            this.mRSSI = rssi;
        }

        public String getName() {
            return this.mName;
        }

        public String getAddress() {
            return this.mAddress;
        }
    }

    public Bluetooth(Context context) {
        init(context);
        reset();
    }

    private void init(Context context) {
        this.mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        filter.addAction("android.bluetooth.adapter.action.DISCOVERY_STARTED");
        filter.addAction("android.bluetooth.device.action.FOUND");
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
        this.mReceiverRegistered = true;
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void recycle() {
        this.mCallback = null;
        if (this.mReceiverRegistered) {
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
            this.mReceiverRegistered = false;
        }
        close();
    }

    public void reset() {
        this.mSearchedDevices.clear();
    }

    public void setCallback(Callback callback) {
        this.mCallback = new WeakReference(callback);
    }

    public Callback getCallback() {
        if (this.mCallback == null) {
            return null;
        }
        return (Callback) this.mCallback.get();
    }

    public boolean isOpen() {
        if (this.mBluetoothAdapter != null) {
            return this.mBluetoothAdapter.isEnabled();
        }
        return false;
    }

    public boolean open() {
        if (isOpen()) {
            return true;
        }
        if (this.mBluetoothAdapter != null) {
            return this.mBluetoothAdapter.enable();
        }
        Log.w("BluetoothSearch", "mBluetoothDev = null");
        return false;
    }

    public boolean close() {
        if (isOpen() && this.mBluetoothAdapter != null) {
            return this.mBluetoothAdapter.disable();
        }
        return true;
    }

    public void search() {
        this.mBluetoothAdapter.startDiscovery();
    }

    public void finalize() throws Throwable {
        recycle();
        super.finalize();
    }

    private void appendNewDevice(String deviceName, String address, short rssi) {
        if (!(this.mSearchedDevices.size() >= this.mMaxSearchedDevices || deviceName == null || address == null)) {
            Device device = new Device(deviceName, address, rssi);
            this.mSearchedDevices.add(device);
            Callback callback = getCallback();
            if (callback != null) {
                callback.onSearched(device);
            }
        }
    }
}
