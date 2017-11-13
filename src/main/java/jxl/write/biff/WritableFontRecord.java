package jxl.write.biff;

import jxl.biff.FontRecord;
import jxl.format.Font;

public class WritableFontRecord extends FontRecord {
    protected WritableFontRecord(String fn, int ps, int bold, boolean it, int us, int ci, int ss) {
        super(fn, ps, bold, it, us, ci, ss);
    }

    protected WritableFontRecord(Font f) {
        super(f);
    }
}
