package jxl.write.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class CalcModeRecord extends WritableRecordData {
    static CalcMode automatic = new CalcMode(1);
    static CalcMode automaticNoTables = new CalcMode(-1);
    static CalcMode manual = new CalcMode(0);
    private CalcMode calculationMode;

    private static class CalcMode {
        int value;

        public CalcMode(int m) {
            this.value = m;
        }
    }

    public CalcModeRecord(CalcMode cm) {
        super(Type.CALCMODE);
        this.calculationMode = cm;
    }

    public byte[] getData() {
        byte[] data = new byte[2];
        IntegerHelper.getTwoBytes(this.calculationMode.value, data, 0);
        return data;
    }
}
