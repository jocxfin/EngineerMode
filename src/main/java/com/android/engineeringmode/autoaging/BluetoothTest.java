package com.android.engineeringmode.autoaging;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.engineeringmode.functions.Bluetooth;
import com.android.engineeringmode.functions.Bluetooth.Callback;
import com.android.engineeringmode.functions.Bluetooth.Device;
import com.android.engineeringmode.util.MessageCenter;

import java.util.ArrayList;

public class BluetoothTest extends BaseTest implements Callback {
    private BluetoothItemAdapter mAdapter;
    private Bluetooth mBluetooth;
    private int mCurrentErrorMsg = 2131296632;
    private ListView mListView;
    private OnCancelListener mOpenCancelListener = new OnCancelListener() {
        public void onCancel(DialogInterface dialog) {
            MessageCenter.showShortMessage(BluetoothTest.this, 2131296621);
            if (BluetoothTest.this.mBluetooth.isOpen()) {
                BluetoothTest.this.mBluetooth.close();
                BluetoothTest.this.mCurrentErrorMsg = 2131296625;
            }
        }
    };
    private ProgressDialog mProgressDialog;
    private OnCancelListener mSearchCancelListener = new OnCancelListener() {
        public void onCancel(DialogInterface dialog) {
            MessageCenter.showShortMessage(BluetoothTest.this, 2131296630);
            if (BluetoothTest.this.mBluetooth.isOpen()) {
                BluetoothTest.this.mBluetooth.close();
                BluetoothTest.this.mCurrentErrorMsg = 2131296625;
            }
        }
    };

    private class BluetoothItemAdapter extends BaseAdapter {
        private ArrayList<Device> mDataList = new ArrayList();
        private LayoutInflater mInflater;

        public BluetoothItemAdapter(Context context) {
            this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        public int getCount() {
            return this.mDataList.size();
        }

        public Object getItem(int position) {
            if (position < 0 || position >= getCount()) {
                return null;
            }
            return this.mDataList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public int add(Device device) {
            if (device == null) {
                return -1;
            }
            this.mDataList.add(device);
            notifyDataSetChanged();
            return getCount() - 1;
        }

        public int clear() {
            int count = getCount();
            this.mDataList.clear();
            notifyDataSetChanged();
            return count;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null) {
                view = this.mInflater.inflate(2130903047, null);
            }
            Device device = (Device) getItem(position);
            if (device == null) {
                return view;
            }
            TextView addressView = (TextView) view.findViewById(2131492897);
            ((TextView) view.findViewById(2131492896)).setText(device.getName());
            addressView.setText(device.getAddress());
            return view;
        }
    }

    public void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        setTitle(2131296611);
        setContentView(2130903046);
        this.mAdapter = new BluetoothItemAdapter(this);
        this.mListView = (ListView) findViewById(2131492895);
        this.mListView.setAdapter(this.mAdapter);
        this.mBluetooth = new Bluetooth(getApplicationContext());
        this.mBluetooth.setCallback(this);
    }

    protected void runTest() {
        this.mAdapter.clear();
        this.mBluetooth.reset();
        if (this.mBluetooth.isOpen()) {
            startSearch();
            return;
        }
        this.mBluetooth.open();
        this.mCurrentErrorMsg = 2131296620;
    }

    protected void endTest() {
        this.mBluetooth.recycle();
    }

    private void startSearch() {
        if (this.mBluetooth.isOpen()) {
            this.mBluetooth.search();
        } else {
            MessageCenter.showShortMessage((Context) this, 2131296620);
        }
    }

    public void onOpened() {
        dismissProgressDialog();
        startSearch();
    }

    public void onClosed() {
        MessageCenter.showShortMessage((Context) this, 2131296624);
    }

    public void onOpenning() {
        showProgressDialog(this.mOpenCancelListener, 2131296262, 2131296619);
    }

    public void onClosing() {
        MessageCenter.showShortMessage((Context) this, 2131296623);
    }

    public void onSearchStart() {
        showProgressDialog(this.mSearchCancelListener, 2131296262, 2131296629);
    }

    public void onSearchEnd() {
        dismissProgressDialog();
        this.mBluetooth.close();
        this.mCurrentErrorMsg = 2131296625;
    }

    public void onSearched(Device device) {
        this.mAdapter.add(device);
    }

    public void onError() {
        dismissProgressDialog();
        MessageCenter.showLongMessage((Context) this, this.mCurrentErrorMsg != 0 ? this.mCurrentErrorMsg : 2131296632);
    }

    private void showProgressDialog(OnCancelListener cancelListener, int title, int msg) {
        dismissProgressDialog();
        this.mProgressDialog = MessageCenter.showProgress((Context) this, cancelListener, title, msg);
    }

    private void dismissProgressDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
    }
}
