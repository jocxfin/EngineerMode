package com.oem.remotecontrols;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.UUID;

public abstract class BluetoothLeService extends Service {
    private static final String TAG = BluetoothLeService.class;
    public static BluetoothLeService mBluetoothLeService = null;
    private final IBinder mBinder = new LocalBinder();
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothManager mBluetoothManager;
    private ChildBluetoothGattCallback mChildBluetoothGattCallback;
    private int mConnectionState = 0;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == 2) {
                BluetoothLeService.this.mConnectionState = 2;
                Log.i(BluetoothLeService.TAG, "Connected to GATT server.");
                BluetoothLeService.this.mBluetoothGatt.discoverServices();
            } else if (newState == 0) {
                BluetoothLeService.this.mConnectionState = 0;
                Log.i(BluetoothLeService.TAG, "Disconnected from GATT server.");
            }
            if (BluetoothLeService.this.mChildBluetoothGattCallback != null) {
                BluetoothLeService.this.mChildBluetoothGattCallback.onConnectionStateChange(gatt, status, newState);
            }
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (BluetoothLeService.this.mChildBluetoothGattCallback != null) {
                BluetoothLeService.this.mChildBluetoothGattCallback.onServicesDiscovered(gatt, status);
            }
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (BluetoothLeService.this.mChildBluetoothGattCallback != null) {
                BluetoothLeService.this.mChildBluetoothGattCallback.onCharacteristicRead(gatt, characteristic, status);
            }
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (BluetoothLeService.this.mChildBluetoothGattCallback != null) {
                BluetoothLeService.this.mChildBluetoothGattCallback.onCharacteristicChanged(gatt, characteristic);
            }
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (BluetoothLeService.this.mChildBluetoothGattCallback != null) {
                BluetoothLeService.this.mChildBluetoothGattCallback.onCharacteristicWrite(gatt, characteristic, status);
            }
        }

        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            if (BluetoothLeService.this.mChildBluetoothGattCallback != null) {
                BluetoothLeService.this.mChildBluetoothGattCallback.onReadRemoteRssi(gatt, rssi, status);
            }
        }
    };

    public abstract class ChildBluetoothGattCallback extends BluetoothGattCallback {
    }

    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        mBluetoothLeService = this;
        return this.mBinder;
    }

    public boolean onUnbind(Intent intent) {
        close();
        mBluetoothLeService = null;
        return super.onUnbind(intent);
    }

    public static BluetoothLeService getInstance() {
        if (mBluetoothLeService != null) {
            return mBluetoothLeService;
        }
        Log.e(TAG, "mBluetoothLeService has not been initialize!");
        return null;
    }

    public boolean initialize() {
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) getSystemService("bluetooth");
            if (this.mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        if (this.mBluetoothAdapter != null) {
            return true;
        }
        Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
        return false;
    }

    public boolean connect(String address) {
        if (this.mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        } else if (this.mBluetoothDeviceAddress == null || !address.equals(this.mBluetoothDeviceAddress) || this.mBluetoothGatt == null) {
            BluetoothDevice device = this.mBluetoothAdapter.getRemoteDevice(address);
            if (device == null) {
                Log.w(TAG, "Device not found.  Unable to connect.");
                return false;
            }
            this.mBluetoothGatt = device.connectGatt(this, true, this.mGattCallback);
            Log.d(TAG, "Trying to create a new connection.");
            this.mBluetoothDeviceAddress = address;
            this.mConnectionState = 1;
            return true;
        } else {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (!this.mBluetoothGatt.connect()) {
                return false;
            }
            this.mConnectionState = 1;
            return true;
        }
    }

    public void close() {
        if (this.mBluetoothGatt != null) {
            this.mBluetoothGatt.close();
            this.mBluetoothGatt = null;
        }
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (this.mBluetoothAdapter == null || this.mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            this.mBluetoothGatt.readCharacteristic(characteristic);
        }
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (this.mBluetoothAdapter != null && this.mBluetoothGatt != null) {
            return this.mBluetoothGatt.writeCharacteristic(characteristic);
        }
        Log.w(TAG, "BluetoothAdapter not initialized");
        return false;
    }

    public boolean readRemoteRssi() {
        if (this.mBluetoothAdapter != null && this.mBluetoothGatt != null) {
            return this.mBluetoothGatt.readRemoteRssi();
        }
        Log.w(TAG, "BluetoothAdapter not initialized");
        return false;
    }

    public BluetoothGattService getService(UUID uuid) {
        return this.mBluetoothGatt.getService(uuid);
    }

    public void setChildBluetoothGattCallback(ChildBluetoothGattCallback callback) {
        this.mChildBluetoothGattCallback = callback;
    }
}
