package com.android.engineeringmode.wifimtk;

public class EMWifi {
    public static int initial() {
        return 0;
    }

    public static int UnInitial() {
        return 0;
    }

    public static int getXtalTrimToCr(long[] value) {
        return 0;
    }

    public static int setTestMode() {
        return 0;
    }

    public static int setNormalMode() {
        return 0;
    }

    public static int setStandBy() {
        return 0;
    }

    public static int setEEPRomSize(long i4EepromSz) {
        return 0;
    }

    public static int setEEPRomFromFile(String atcFileName) {
        return 0;
    }

    public static int readTxPowerFromEEPromEx(long i4ChnFreg, long i4Rate, long[] PowerStatus, int arraylen) {
        return 0;
    }

    public static int setEEPromCKSUpdated() {
        return 0;
    }

    public static int getPacketRxStatus(long[] i4Init, int arraylen) {
        return 0;
    }

    public static int setOutputPower(long i4Rate, long i4TxPwrGain, int i4TxAnt) {
        return 0;
    }

    public static int setLocalFrequecy(long i4TxPwrGain, long i4TxAnt) {
        return 0;
    }

    public static int setCarrierSuppression(long i4Modulation, long i4TxPwrGain, long i4TxAnt) {
        return 0;
    }

    public static int setChannel(long i4ChFreqkHz) {
        return 0;
    }

    public static int setOutputPin(long i4PinIndex, long i4OutputLevel) {
        return 0;
    }

    public static int readEEPRom16(long u4Offset, long[] pu4Value) {
        return 0;
    }

    public static int writeEEPRom16(long u4Offset, long u4Value) {
        return 0;
    }

    public static int eepromReadByteStr(long u4Addr, long u4Length, byte[] paucStr) {
        return 0;
    }

    public static int eepromWriteByteStr(long u4Addr, long u4Length, String paucStr) {
        return 0;
    }

    public static int setATParam(long u4FuncIndex, long u4FuncData) {
        return 0;
    }

    public static int getATParam(long u4FuncIndex, long[] pu4FuncData) {
        return 0;
    }

    public static int setXtalTrimToCr(long u4Value) {
        return 0;
    }

    public static int queryThermoInfo(long[] pi4Enable, int len) {
        return 0;
    }

    public static int setThermoEn(long i4Enable) {
        return 0;
    }

    public static int getEEPRomSize(long[] value) {
        return 0;
    }

    public static int setPnpPower(long i4PowerMode) {
        return 0;
    }

    public static int writeMCR32(long offset, long value) {
        return 0;
    }

    public static int readMCR32(long offset, long[] value) {
        return 0;
    }
}
