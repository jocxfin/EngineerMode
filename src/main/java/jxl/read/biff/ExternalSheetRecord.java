package jxl.read.biff;

import jxl.WorkbookSettings;
import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

public class ExternalSheetRecord extends RecordData {
    public static Biff7 biff7 = new Biff7();
    private static Logger logger = Logger.getLogger(ExternalSheetRecord.class);
    private XTI[] xtiArray;

    private static class Biff7 {
        private Biff7() {
        }
    }

    private static class XTI {
        int firstTab;
        int lastTab;
        int supbookIndex;

        XTI(int s, int f, int l) {
            this.supbookIndex = s;
            this.firstTab = f;
            this.lastTab = l;
        }
    }

    ExternalSheetRecord(Record t, WorkbookSettings ws) {
        super(t);
        byte[] data = getRecord().getData();
        int numxtis = IntegerHelper.getInt(data[0], data[1]);
        if (data.length >= (numxtis * 6) + 2) {
            this.xtiArray = new XTI[numxtis];
            int pos = 2;
            for (int i = 0; i < numxtis; i++) {
                this.xtiArray[i] = new XTI(IntegerHelper.getInt(data[pos], data[pos + 1]), IntegerHelper.getInt(data[pos + 2], data[pos + 3]), IntegerHelper.getInt(data[pos + 4], data[pos + 5]));
                pos += 6;
            }
            return;
        }
        this.xtiArray = new XTI[0];
        logger.warn("Could not process external sheets.  Formulas may be compromised.");
    }

    ExternalSheetRecord(Record t, WorkbookSettings settings, Biff7 dummy) {
        super(t);
        logger.warn("External sheet record for Biff 7 not supported");
    }

    public int getNumRecords() {
        return this.xtiArray == null ? 0 : this.xtiArray.length;
    }

    public int getSupbookIndex(int index) {
        return this.xtiArray[index].supbookIndex;
    }

    public int getFirstTabIndex(int index) {
        return this.xtiArray[index].firstTab;
    }

    public int getLastTabIndex(int index) {
        return this.xtiArray[index].lastTab;
    }
}
