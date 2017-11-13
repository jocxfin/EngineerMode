package jxl.write.biff;

import jxl.SheetSettings;
import jxl.biff.DoubleHelper;
import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;
import jxl.common.Logger;
import jxl.format.PageOrder;
import jxl.format.PageOrientation;

class SetupRecord extends WritableRecordData {
    private int copies;
    private byte[] data;
    private int fitHeight;
    private int fitWidth;
    private double footerMargin;
    private double headerMargin;
    private int horizontalPrintResolution;
    private boolean initialized;
    Logger logger = Logger.getLogger(SetupRecord.class);
    private PageOrder order;
    private PageOrientation orientation;
    private int pageStart;
    private int paperSize;
    private int scaleFactor;
    private int verticalPrintResolution;

    public SetupRecord(SheetSettings s) {
        super(Type.SETUP);
        this.orientation = s.getOrientation();
        this.order = s.getPageOrder();
        this.headerMargin = s.getHeaderMargin();
        this.footerMargin = s.getFooterMargin();
        this.paperSize = s.getPaperSize().getValue();
        this.horizontalPrintResolution = s.getHorizontalPrintResolution();
        this.verticalPrintResolution = s.getVerticalPrintResolution();
        this.fitWidth = s.getFitWidth();
        this.fitHeight = s.getFitHeight();
        this.pageStart = s.getPageStart();
        this.scaleFactor = s.getScaleFactor();
        this.copies = s.getCopies();
        this.initialized = true;
    }

    public byte[] getData() {
        this.data = new byte[34];
        IntegerHelper.getTwoBytes(this.paperSize, this.data, 0);
        IntegerHelper.getTwoBytes(this.scaleFactor, this.data, 2);
        IntegerHelper.getTwoBytes(this.pageStart, this.data, 4);
        IntegerHelper.getTwoBytes(this.fitWidth, this.data, 6);
        IntegerHelper.getTwoBytes(this.fitHeight, this.data, 8);
        int options = 0;
        if (this.order == PageOrder.RIGHT_THEN_DOWN) {
            options = 1;
        }
        if (this.orientation == PageOrientation.PORTRAIT) {
            options |= 2;
        }
        if (this.pageStart != 0) {
            options |= 128;
        }
        if (!this.initialized) {
            options |= 4;
        }
        IntegerHelper.getTwoBytes(options, this.data, 10);
        IntegerHelper.getTwoBytes(this.horizontalPrintResolution, this.data, 12);
        IntegerHelper.getTwoBytes(this.verticalPrintResolution, this.data, 14);
        DoubleHelper.getIEEEBytes(this.headerMargin, this.data, 16);
        DoubleHelper.getIEEEBytes(this.footerMargin, this.data, 24);
        IntegerHelper.getTwoBytes(this.copies, this.data, 32);
        return this.data;
    }
}
