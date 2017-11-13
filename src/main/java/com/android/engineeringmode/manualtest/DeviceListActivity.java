package com.android.engineeringmode.manualtest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.engineeringmode.manualtest.DeviceManager.OnGetDeviceInfoCompletedListener;

import java.util.List;

public class DeviceListActivity extends Activity {
    private DeviceManager deviceManager = null;
    private ListView mListView;

    class DeviceInfoAdapter extends BaseAdapter {
        private List<DeviceInfo> mDeviceInfos;
        private LayoutInflater mLayoutInflater;

        class ViewHolder {
            public TextView mManufactureTextView;
            public TextView mNameTextView;
            public TextView mOtherInfoTextView;
            public TextView mVersionTextView;

            ViewHolder() {
            }
        }

        public DeviceInfoAdapter(Context context, List<DeviceInfo> deviceInfos) {
            this.mDeviceInfos = deviceInfos;
            this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
            Log.e("DeviceInfoAdapter", "DeviceInfoAdapter deviceInfo size:" + this.mDeviceInfos.size());
        }

        public int getCount() {
            return this.mDeviceInfos == null ? 0 : this.mDeviceInfos.size();
        }

        public Object getItem(int position) {
            return this.mDeviceInfos == null ? null : this.mDeviceInfos.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = this.mLayoutInflater.inflate(2130903082, null);
                viewHolder.mNameTextView = (TextView) convertView.findViewById(2131493027);
                viewHolder.mVersionTextView = (TextView) convertView.findViewById(2131493028);
                viewHolder.mManufactureTextView = (TextView) convertView.findViewById(2131493029);
                viewHolder.mOtherInfoTextView = (TextView) convertView.findViewById(2131493030);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            DeviceInfo deviceInfo = (DeviceInfo) this.mDeviceInfos.get(position);
            viewHolder.mNameTextView.setText(deviceInfo.getDeviceName());
            viewHolder.mVersionTextView.setText(processText(deviceInfo.getDeviceVersion()));
            viewHolder.mManufactureTextView.setText(processText(deviceInfo.getDeviceManufacture()));
            viewHolder.mOtherInfoTextView.setText(processText(deviceInfo.getDeviceOtherInfo()));
            return convertView;
        }

        private String processText(String context) {
            if (context == null || context.contains("(null)")) {
                return "    /";
            }
            return context;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903083);
        this.mListView = (ListView) findViewById(16908298);
        ((TextView) findViewById(2131493032)).setText(SystemProperties.get("ro.product.model", getResources().getString(2131296815)));
        this.deviceManager = new DeviceManager(this);
        this.deviceManager.setOnGetDeviceInfoCompleted(new OnGetDeviceInfoCompletedListener() {
            public void onCompleted(List<DeviceInfo> deviceInfos) {
                DeviceListActivity.this.mListView.setAdapter(new DeviceInfoAdapter(DeviceListActivity.this, deviceInfos));
            }
        });
        this.deviceManager.run();
    }
}
