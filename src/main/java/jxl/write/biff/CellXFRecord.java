package jxl.write.biff;

import jxl.biff.DisplayFormat;
import jxl.biff.FontRecord;
import jxl.biff.XFRecord;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WriteException;

public class CellXFRecord extends XFRecord {
    protected CellXFRecord(FontRecord fnt, DisplayFormat form) {
        super(fnt, form);
        setXFDetails(XFRecord.cell, 0);
    }

    CellXFRecord(XFRecord fmt) {
        super(fmt);
        setXFDetails(XFRecord.cell, 0);
    }

    public void setBorder(Border b, BorderLineStyle ls, Colour c) throws WriteException {
        if (isInitialized()) {
            throw new JxlWriteException(JxlWriteException.formatInitialized);
        } else if (b == Border.ALL) {
            super.setXFBorder(Border.LEFT, ls, c);
            super.setXFBorder(Border.RIGHT, ls, c);
            super.setXFBorder(Border.TOP, ls, c);
            super.setXFBorder(Border.BOTTOM, ls, c);
        } else if (b != Border.NONE) {
            super.setXFBorder(b, ls, c);
        } else {
            super.setXFBorder(Border.LEFT, BorderLineStyle.NONE, Colour.BLACK);
            super.setXFBorder(Border.RIGHT, BorderLineStyle.NONE, Colour.BLACK);
            super.setXFBorder(Border.TOP, BorderLineStyle.NONE, Colour.BLACK);
            super.setXFBorder(Border.BOTTOM, BorderLineStyle.NONE, Colour.BLACK);
        }
    }
}
