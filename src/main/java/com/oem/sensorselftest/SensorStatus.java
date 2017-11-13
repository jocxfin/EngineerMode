package com.oem.sensorselftest;

import android.hardware.Sensor;

import java.util.ArrayList;
import java.util.List;

public class SensorStatus {
    private int color;
    private Sensor sensor;
    private String status;

    public SensorStatus(Sensor sensor, String status, int color) {
        this.sensor = sensor;
        this.status = status;
        this.color = color;
    }

    public Sensor getSensor() {
        return this.sensor;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public static List<SensorStatus> createSensorList(List<Sensor> sensorList) {
        List<SensorStatus> testSensorList = new ArrayList(sensorList.size());
        for (Sensor sensor : sensorList) {
            testSensorList.add(new SensorStatus(sensor, "Operation not started", -1));
        }
        return testSensorList;
    }
}
