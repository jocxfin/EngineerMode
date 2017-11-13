package com.android.engineeringmode.manualtest;

public class DeviceInfo {
    private String mDeviceManufacture;
    private String mDeviceName;
    private String mDeviceOtherInfo;
    private String mDeviceVersion;

    public DeviceInfo(String mDeviceName, String mDeviceVersion, String mDeviceManufacture, String mDeviceOtherInfo) {
        this.mDeviceName = mDeviceName;
        this.mDeviceVersion = mDeviceVersion;
        this.mDeviceManufacture = mDeviceManufacture;
        this.mDeviceOtherInfo = mDeviceOtherInfo;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public void setDeviceName(String mDeviceName) {
        this.mDeviceName = mDeviceName;
    }

    public String getDeviceVersion() {
        return this.mDeviceVersion;
    }

    public void setDeviceVersion(String mDeviceVersion) {
        this.mDeviceVersion = mDeviceVersion;
    }

    public String getDeviceManufacture() {
        return this.mDeviceManufacture;
    }

    public void setDeviceManufacture(String mDeviceManufacture) {
        this.mDeviceManufacture = mDeviceManufacture;
    }

    public String getDeviceOtherInfo() {
        return this.mDeviceOtherInfo;
    }

    public void setDeviceOtherInfo(String mDeviceOtherInfo) {
        this.mDeviceOtherInfo = mDeviceOtherInfo;
    }

    public String toString() {
        return "DeviceInfo [mDeviceName=" + this.mDeviceName + ", mDeviceVersion=" + this.mDeviceVersion + ", mDeviceManufacture=" + this.mDeviceManufacture + ", mDeviceOtherInfo=" + this.mDeviceOtherInfo + "] \n";
    }
}
