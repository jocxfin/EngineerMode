package jxl.write.biff;

import java.util.ArrayList;
import java.util.Iterator;

import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class ExternalSheetRecord extends WritableRecordData {
    private ArrayList xtis;

    private static class XTI {
        int firstTab;
        int lastTab;
        int supbookIndex;

        XTI(int s, int f, int l) {
            this.supbookIndex = s;
            this.firstTab = f;
            this.lastTab = l;
        }

        void sheetInserted(int index) {
            if (this.firstTab >= index) {
                this.firstTab++;
            }
            if (this.lastTab >= index) {
                this.lastTab++;
            }
        }
    }

    public ExternalSheetRecord(jxl.read.biff.ExternalSheetRecord esf) {
        super(Type.EXTERNSHEET);
        this.xtis = new ArrayList(esf.getNumRecords());
        for (int i = 0; i < esf.getNumRecords(); i++) {
            this.xtis.add(new XTI(esf.getSupbookIndex(i), esf.getFirstTabIndex(i), esf.getLastTabIndex(i)));
        }
    }

    public ExternalSheetRecord() {
        super(Type.EXTERNSHEET);
        this.xtis = new ArrayList();
    }

    int getIndex(int supbookind, int sheetind) {
        Iterator i = this.xtis.iterator();
        boolean found = false;
        int pos = 0;
        while (i.hasNext() && !found) {
            XTI xti = (XTI) i.next();
            if (xti.supbookIndex == supbookind && xti.firstTab == sheetind) {
                found = true;
            } else {
                pos++;
            }
        }
        if (found) {
            return pos;
        }
        this.xtis.add(new XTI(supbookind, sheetind, sheetind));
        return this.xtis.size() - 1;
    }

    public byte[] getData() {
        byte[] data = new byte[((this.xtis.size() * 6) + 2)];
        IntegerHelper.getTwoBytes(this.xtis.size(), data, 0);
        int pos = 2;
        Iterator i = this.xtis.iterator();
        while (i.hasNext()) {
            XTI xti = (XTI) i.next();
            IntegerHelper.getTwoBytes(xti.supbookIndex, data, pos);
            IntegerHelper.getTwoBytes(xti.firstTab, data, pos + 2);
            IntegerHelper.getTwoBytes(xti.lastTab, data, pos + 4);
            pos += 6;
        }
        return data;
    }

    public int getSupbookIndex(int index) {
        return ((XTI) this.xtis.get(index)).supbookIndex;
    }

    public int getFirstTabIndex(int index) {
        return ((XTI) this.xtis.get(index)).firstTab;
    }

    void sheetInserted(int index) {
        Iterator i = this.xtis.iterator();
        while (i.hasNext()) {
            ((XTI) i.next()).sheetInserted(index);
        }
    }
}
