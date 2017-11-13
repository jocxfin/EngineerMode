package com.oem.sensorselftest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class SensorListAdapter extends BaseAdapter {
    private Context context;
    private List<SensorStatus> sensorList;

    public SensorListAdapter(Context context, List<SensorStatus> sensorList) {
        this.context = context;
        this.sensorList = sensorList;
    }

    public int getCount() {
        return this.sensorList.size();
    }

    public Object getItem(int position) {
        return this.sensorList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SensorStatus testSensor = (SensorStatus) this.sensorList.get(position);
        if (convertView == null) {
            return new SensorListItemView(this.context, testSensor);
        }
        return (SensorListItemView) convertView;
    }
}
