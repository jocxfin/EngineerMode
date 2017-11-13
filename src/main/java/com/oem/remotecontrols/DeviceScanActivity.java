package com.oem.remotecontrols;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DeviceScanActivity extends ListActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private LeScanCallback mLeScanCallback = new LeScanCallback() {
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            int rs = rssi;
            DeviceScanActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    DeviceScanActivity.this.mLeDeviceListAdapter.addDevice(device, rssi);
                    DeviceScanActivity.this.mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };
    private boolean mScanning;

    private class BluetoothDeviceInfo {
        private final BluetoothDevice mDevice;
        private final int mRssi;

        public BluetoothDeviceInfo(BluetoothDevice dev, int rssi) {
            this.mRssi = rssi;
            this.mDevice = dev;
        }

        public int getRssi() {
            return this.mRssi;
        }

        public BluetoothDevice getDevice() {
            return this.mDevice;
        }
    }

    private class LeDeviceListAdapter extends BaseAdapter {
        private LayoutInflater mInflator;
        private ArrayList<BluetoothDeviceInfo> mLeDevicesInfos = new ArrayList();

        public LeDeviceListAdapter() {
            this.mInflator = DeviceScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device, int rssi) {
            boolean contains = false;
            int count = this.mLeDevicesInfos.size();
            for (int i = 0; i != count; i++) {
                if (((BluetoothDeviceInfo) this.mLeDevicesInfos.get(i)).getDevice().equals(device)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                this.mLeDevicesInfos.add(new BluetoothDeviceInfo(device, rssi));
                Collections.sort(this.mLeDevicesInfos, new SortByRssi());
            }
        }

        public BluetoothDevice getDevice(int position) {
            return ((BluetoothDeviceInfo) this.mLeDevicesInfos.get(position)).getDevice();
        }

        public void clear() {
            this.mLeDevicesInfos.clear();
        }

        public int getCount() {
            return this.mLeDevicesInfos.size();
        }

        public Object getItem(int i) {
            return this.mLeDevicesInfos.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = this.mInflator.inflate(2130903142, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(2131493336);
                viewHolder.deviceName = (TextView) view.findViewById(2131493027);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            BluetoothDevice device = ((BluetoothDeviceInfo) this.mLeDevicesInfos.get(i)).getDevice();
            String deviceName = device.getName();
            if (deviceName == null || deviceName.length() <= 0) {
                viewHolder.deviceName.setText(2131297304);
            } else {
                viewHolder.deviceName.setText(deviceName);
            }
            viewHolder.deviceAddress.setText(device.getAddress());
            return view;
        }
    }

    private class SortByRssi implements Comparator<BluetoothDeviceInfo> {
        private SortByRssi() {
        }

        public int compare(BluetoothDeviceInfo lhs, BluetoothDeviceInfo rhs) {
            return rhs.getRssi() - lhs.getRssi();
        }
    }

    static class ViewHolder {
        TextView deviceAddress;
        TextView deviceName;

        ViewHolder() {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle(2131297299);
        this.mHandler = new Handler();
        if (!getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            Toast.makeText(this, 2131297300, 0).show();
            finish();
        }
        this.mBluetoothAdapter = ((BluetoothManager) getSystemService("bluetooth")).getAdapter();
        if (this.mBluetoothAdapter == null) {
            Toast.makeText(this, 2131297301, 0).show();
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131427328, menu);
        if (this.mScanning) {
            menu.findItem(2131493704).setVisible(true);
            menu.findItem(2131493703).setVisible(false);
            menu.findItem(2131493702).setActionView(2130903040);
        } else {
            menu.findItem(2131493704).setVisible(false);
            menu.findItem(2131493703).setVisible(true);
            menu.findItem(2131493702).setActionView(null);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 2131493703:
                this.mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case 2131493704:
                scanLeDevice(false);
                break;
        }
        return true;
    }

    protected void onResume() {
        super.onResume();
        if (this.mBluetoothAdapter.isEnabled()) {
            this.mLeDeviceListAdapter = new LeDeviceListAdapter();
            setListAdapter(this.mLeDeviceListAdapter);
            scanLeDevice(true);
            return;
        }
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 0) {
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        if (this.mLeDeviceListAdapter != null) {
            this.mLeDeviceListAdapter.clear();
        }
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        BluetoothDevice device = this.mLeDeviceListAdapter.getDevice(position);
        if (device != null) {
            if (this.mScanning) {
                this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
                this.mScanning = false;
            }
            SimpleKeyService simpleKeyService = (SimpleKeyService) BluetoothLeService.getInstance();
            if (simpleKeyService != null) {
                simpleKeyService.connect(device);
            }
            finish();
        }
    }

    private void scanLeDevice(boolean enable) {
        if (enable) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    DeviceScanActivity.this.mScanning = false;
                    DeviceScanActivity.this.mBluetoothAdapter.stopLeScan(DeviceScanActivity.this.mLeScanCallback);
                    DeviceScanActivity.this.invalidateOptionsMenu();
                }
            }, 10000);
            this.mScanning = true;
            this.mBluetoothAdapter.startLeScan(this.mLeScanCallback);
        } else {
            this.mScanning = false;
            this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
        }
        invalidateOptionsMenu();
    }
}
