package com.oem.sensorselftest;

import android.hardware.Sensor;

import com.android.engineeringmode.functions.Light;
import com.qualcomm.qti.sensors.core.sensortest.SensorTest.DataType;

public final class SensorUtilityFunctions {
    public static String getSensorTypeString(Sensor sensor) {
        switch (sensor.getType()) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                return "ACCEL";
            case Light.CHARGE_RED_LIGHT /*2*/:
                return "MAG";
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                return "ORI";
            case 4:
                return "GYRO";
            case 5:
                return "LIGHT";
            case Light.MAIN_KEY_NORMAL /*6*/:
                return "PRESS";
            case 7:
                return "TEMP";
            case 8:
                return "PROX";
            case 9:
                return "GRAVITY";
            case 10:
                return "L.ACCEL";
            case 11:
                return "ROT VEC";
            default:
                return sensor.getName();
        }
    }

    public static DataType getDataType(Sensor sensor) {
        switch (sensor.getType()) {
            case 5:
                return DataType.SECONDARY;
            case 8:
                return DataType.PRIMARY;
            default:
                return DataType.PRIMARY;
        }
    }
}
