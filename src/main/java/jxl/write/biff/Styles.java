package jxl.write.biff;

import jxl.biff.XFRecord;
import jxl.common.Logger;
import jxl.write.DateFormat;
import jxl.write.DateFormats;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableWorkbook;

class Styles {
    private static Logger logger = Logger.getLogger(Styles.class);
    private WritableFont arial10pt = null;
    private WritableCellFormat defaultDateFormat;
    private WritableCellFormat hiddenStyle = null;
    private WritableFont hyperlinkFont = null;
    private WritableCellFormat hyperlinkStyle = null;
    private WritableCellFormat normalStyle = null;

    private synchronized void initNormalStyle() {
        this.normalStyle = new WritableCellFormat(getArial10Pt(), NumberFormats.DEFAULT);
        this.normalStyle.setFont(getArial10Pt());
    }

    public WritableCellFormat getNormalStyle() {
        if (this.normalStyle == null) {
            initNormalStyle();
        }
        return this.normalStyle;
    }

    private synchronized void initHiddenStyle() {
        this.hiddenStyle = new WritableCellFormat(getArial10Pt(), new DateFormat(";;;"));
    }

    public WritableCellFormat getHiddenStyle() {
        if (this.hiddenStyle == null) {
            initHiddenStyle();
        }
        return this.hiddenStyle;
    }

    private synchronized void initHyperlinkStyle() {
        this.hyperlinkStyle = new WritableCellFormat(getHyperlinkFont(), NumberFormats.DEFAULT);
    }

    public WritableCellFormat getHyperlinkStyle() {
        if (this.hyperlinkStyle == null) {
            initHyperlinkStyle();
        }
        return this.hyperlinkStyle;
    }

    private synchronized void initArial10Pt() {
        this.arial10pt = new WritableFont(WritableWorkbook.ARIAL_10_PT);
    }

    public WritableFont getArial10Pt() {
        if (this.arial10pt == null) {
            initArial10Pt();
        }
        return this.arial10pt;
    }

    private synchronized void initHyperlinkFont() {
        this.hyperlinkFont = new WritableFont(WritableWorkbook.HYPERLINK_FONT);
    }

    public WritableFont getHyperlinkFont() {
        if (this.hyperlinkFont == null) {
            initHyperlinkFont();
        }
        return this.hyperlinkFont;
    }

    private synchronized void initDefaultDateFormat() {
        this.defaultDateFormat = new WritableCellFormat(DateFormats.DEFAULT);
    }

    public WritableCellFormat getDefaultDateFormat() {
        if (this.defaultDateFormat == null) {
            initDefaultDateFormat();
        }
        return this.defaultDateFormat;
    }

    public XFRecord getFormat(XFRecord wf) {
        XFRecord format = wf;
        if (format == WritableWorkbook.NORMAL_STYLE) {
            format = getNormalStyle();
        } else if (format == WritableWorkbook.HYPERLINK_STYLE) {
            format = getHyperlinkStyle();
        } else if (format == WritableWorkbook.HIDDEN_STYLE) {
            format = getHiddenStyle();
        } else if (format == DateRecord.defaultDateFormat) {
            format = getDefaultDateFormat();
        }
        if (format.getFont() == WritableWorkbook.ARIAL_10_PT) {
            format.setFont(getArial10Pt());
        } else if (format.getFont() == WritableWorkbook.HYPERLINK_FONT) {
            format.setFont(getHyperlinkFont());
        }
        return format;
    }
}
