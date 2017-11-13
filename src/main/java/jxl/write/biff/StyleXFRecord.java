package jxl.write.biff;

import jxl.biff.DisplayFormat;
import jxl.biff.FontRecord;
import jxl.biff.XFRecord;

public class StyleXFRecord extends XFRecord {
    public StyleXFRecord(FontRecord fnt, DisplayFormat form) {
        super(fnt, form);
        setXFDetails(XFRecord.style, 65520);
    }

    public final void setCellOptions(int opt) {
        super.setXFCellOptions(opt);
    }

    public void setLocked(boolean l) {
        super.setXFLocked(l);
    }
}
