package com.oem.remotecontrols;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;

import com.oem.remotecontrols.BluetoothLeService.ChildBluetoothGattCallback;

import java.util.List;
import java.util.UUID;

public class SimpleKeyService extends BluetoothLeService {
    private static Handler mHandler = null;
    public static ParcelUuid mSimpleKeySrvUUID = null;
    private boolean isSimpleKeyGattServiceConnect = false;
    private ChildBluetoothGattCallback mBluetoothGattCallback = new ChildBluetoothGattCallback(this) {
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (SimpleKeyService.mHandler != null) {
                Message msg = new Message();
                if (newState == 0) {
                    SimpleKeyService.this.isSimpleKeyGattServiceConnect = false;
                    msg.what = 2;
                }
                SimpleKeyService.mHandler.sendMessage(msg);
            }
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == 0 && characteristic != null && characteristic.getUuid().equals(UUID.fromString("f000ffe3-0451-4000-b000-000000000000"))) {
                byte[] data = characteristic.getValue();
                String resultString = "opps";
                if (data != null) {
                    int length = data.length;
                    resultString = new String(data);
                }
                Log.e("SimpleKeyService", "Version is: " + resultString);
                if (SimpleKeyService.mHandler != null) {
                    Message msg = new Message();
                    msg.what = 6;
                    Bundle bundle = new Bundle();
                    bundle.putString("version", resultString);
                    msg.setData(bundle);
                    SimpleKeyService.mHandler.sendMessage(msg);
                }
            }
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        }

        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            if (status == 0) {
                Log.e("SimpleKeyService", "read rssi: " + rssi);
                if (SimpleKeyService.mHandler != null) {
                    Message msg = new Message();
                    msg.what = 3;
                    msg.arg1 = rssi;
                    SimpleKeyService.mHandler.removeMessages(msg.what);
                    SimpleKeyService.mHandler.sendMessageDelayed(msg, 1000);
                }
            }
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == 0) {
                Log.e("SimpleKeyService", "Service discovered.");
                SimpleKeyService.this.mSimpleKeyGattService = SimpleKeyService.this.getService(SimpleKeyService.convertUUIDStringToUUID("0000ffe000001000800000805f9b34fb"));
                if (SimpleKeyService.this.mSimpleKeyGattService != null) {
                    Log.e("SimpleKeyService", "SimleKey service has been connected.");
                    SimpleKeyService.this.isSimpleKeyGattServiceConnect = true;
                    if (SimpleKeyService.mHandler != null) {
                        Message msg = new Message();
                        msg.what = 1;
                        SimpleKeyService.mHandler.sendMessage(msg);
                    } else {
                        return;
                    }
                }
                Log.e("SimpleKeyService", "SimleKey service is not supported.");
                SimpleKeyService.this.isSimpleKeyGattServiceConnect = false;
            }
        }
    };
    private List<BluetoothGattCharacteristic> mCharacteristics;
    private BluetoothGattService mSimpleKeyGattService;

    public boolean initialize() {
        boolean result = super.initialize();
        mSimpleKeySrvUUID = new ParcelUuid(convertUUIDStringToUUID("0000ffe000001000800000805f9b34fb"));
        return result;
    }

    public boolean connect(BluetoothDevice device) {
        boolean result = super.connect(device.getAddress());
        super.setChildBluetoothGattCallback(this.mBluetoothGattCallback);
        return result;
    }

    public static void registerHandler(Handler handler) {
        Log.d("SimpleKeyService", "Registered Handler");
        if (handler != null) {
            mHandler = handler;
        } else {
            Log.e("SimpleKeyService", "handler is null");
        }
    }

    public static void unregisterHandler() {
        Log.e("SimpleKeyService", "UnRegister Handler");
        mHandler = null;
    }

    public void writeBuzzer() {
        Log.d("SimpleKeyService", "E wrietBuzzer()");
        if (writeCharacteristic(4)) {
            Log.i("SimpleKeyService", "write buzzer successful!!!");
        } else {
            Log.e("SimpleKeyService", "write buzzer failed!!!");
        }
    }

    public void writeRedLED() {
        Log.d("SimpleKeyService", "E writeRedLED()");
        if (writeCharacteristic(1)) {
            Log.i("SimpleKeyService", "write red led successful!!!");
        } else {
            Log.e("SimpleKeyService", "write red led failed!!!");
        }
    }

    public boolean readVersion() {
        return readCharacteristic(UUID.fromString("f000ffe3-0451-4000-b000-000000000000"));
    }

    private boolean readCharacteristic(UUID uuid) {
        if (this.mSimpleKeyGattService == null || !this.isSimpleKeyGattServiceConnect) {
            return false;
        }
        BluetoothGattCharacteristic characteristic = this.mSimpleKeyGattService.getCharacteristic(uuid);
        if (characteristic == null) {
            Log.e("SimpleKeyService", "Cannot find the characteristic with uuid: " + uuid.toString());
            return false;
        }
        readCharacteristic(characteristic);
        return true;
    }

    private boolean writeCharacteristic(int value) {
        if (this.mSimpleKeyGattService == null || !this.isSimpleKeyGattServiceConnect) {
            return false;
        }
        boolean ret = false;
        byte[] valBytes = new byte[]{(byte) value};
        try {
            if (this.mCharacteristics == null) {
                this.mCharacteristics = this.mSimpleKeyGattService.getCharacteristics();
            }
            if (this.mCharacteristics != null) {
                BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) this.mCharacteristics.get(1);
                characteristic.setValue(valBytes);
                ret = writeCharacteristic(characteristic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static UUID convertUUIDStringToUUID(String UUIDStr) {
        if (UUIDStr.length() != 32) {
            return null;
        }
        String uuidMsB = UUIDStr.substring(0, 16);
        String uuidLsB = UUIDStr.substring(16, 32);
        if (uuidLsB.equals("800000805f9b34fb")) {
            return new UUID(Long.valueOf(uuidMsB, 16).longValue(), -9223371485494954757L);
        }
        return new UUID(Long.valueOf(uuidMsB, 16).longValue(), Long.valueOf(uuidLsB).longValue());
    }
}
