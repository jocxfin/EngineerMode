package com.android.engineeringmode.gyroscopetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.engineeringmode.gyroscopetest.GyroDispalyData.Data;

import java.util.List;

public class GyroDataAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<Data> mList;

    public GyroDataAdapter(Context context, List<Data> list) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mList = list;
    }

    public int getCount() {
        if (this.mList != null) {
            return this.mList.size();
        }
        return 0;
    }

    public Object getItem(int position) {
        if (this.mList != null) {
            return this.mList.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View arg1, ViewGroup arg2) {
        if (this.mLayoutInflater == null || this.mList == null) {
            return null;
        }
        View item = this.mLayoutInflater.inflate(2130903108, null);
        Data data = (Data) this.mList.get(position);
        TextView x = (TextView) item.findViewById(2131493145);
        TextView y = (TextView) item.findViewById(2131493146);
        TextView z = (TextView) item.findViewById(2131493147);
        ((TextView) item.findViewById(2131493053)).setText(data.count);
        x.setText(data.x);
        y.setText(data.y);
        z.setText(data.z);
        return item;
    }
}
