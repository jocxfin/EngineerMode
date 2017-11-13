package jxl.write;

import jxl.biff.DisplayFormat;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.biff.CellXFRecord;

public class WritableCellFormat extends CellXFRecord {
    public WritableCellFormat() {
        this(WritableWorkbook.ARIAL_10_PT, NumberFormats.DEFAULT);
    }

    public WritableCellFormat(WritableFont font) {
        this(font, NumberFormats.DEFAULT);
    }

    public WritableCellFormat(DisplayFormat format) {
        this(WritableWorkbook.ARIAL_10_PT, format);
    }

    public WritableCellFormat(WritableFont font, DisplayFormat format) {
        super(font, format);
    }

    public void setBorder(Border b, BorderLineStyle ls, Colour c) throws WriteException {
        super.setBorder(b, ls, c);
    }
}
