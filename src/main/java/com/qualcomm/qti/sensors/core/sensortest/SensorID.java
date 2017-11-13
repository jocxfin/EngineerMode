package com.qualcomm.qti.sensors.core.sensortest;

import android.hardware.Sensor;

import com.android.engineeringmode.functions.Light;

public class SensorID {
    private int sensorCount_;
    private int sensorID_;
    private SensorType sensorType_;

    public enum SensorType {
        ACCEL(0),
        GYRO(10),
        MAG(20),
        PRESSURE(30),
        PROX_LIGHT(40),
        RGB_LIGHT(240),
        RGB_CCT(60);

        private final int typeValue;

        private SensorType(int typeValue) {
            this.typeValue = typeValue;
        }

        public int typeValue() {
            return this.typeValue;
        }

        public static SensorType getSensorType(Sensor sensor) {
            switch (sensor.getType()) {
                case Light.MAIN_KEY_LIGHT /*1*/:
                    return ACCEL;
                case Light.CHARGE_RED_LIGHT /*2*/:
                    return MAG;
                case 4:
                    return GYRO;
                case 5:
                    return RGB_LIGHT;
                case Light.MAIN_KEY_NORMAL /*6*/:
                    return PRESSURE;
                case 8:
                    return PROX_LIGHT;
                case 33171020:
                    return RGB_CCT;
                default:
                    return null;
            }
        }
    }

    public SensorID(SensorType sensorType, int sensorCount) throws IllegalArgumentException {
        if (sensorCount > 9 || sensorCount < 0) {
            throw new IllegalArgumentException("Range Exception: the value of sensor count must be between 0 and 9, inclusive");
        } else if (sensorType == null) {
            throw new IllegalArgumentException("Range Exception: sensor type must not be null");
        } else {
            this.sensorCount_ = sensorCount;
            this.sensorType_ = sensorType;
            this.sensorID_ = sensorType.typeValue() + sensorCount;
        }
    }

    public int getSensorID() {
        return this.sensorID_;
    }
}
