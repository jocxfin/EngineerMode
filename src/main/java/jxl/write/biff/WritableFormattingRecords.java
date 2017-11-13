package jxl.write.biff;

import jxl.biff.Fonts;
import jxl.biff.FormattingRecords;
import jxl.biff.NumFormatRecordsException;
import jxl.common.Assert;
import jxl.write.NumberFormats;

public class WritableFormattingRecords extends FormattingRecords {
    public WritableFormattingRecords(Fonts f, Styles styles) {
        super(f);
        try {
            StyleXFRecord sxf = new StyleXFRecord(styles.getArial10Pt(), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            addStyle(sxf);
            sxf = new StyleXFRecord(getFonts().getFont(1), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            sxf.setCellOptions(62464);
            addStyle(sxf);
            sxf = new StyleXFRecord(getFonts().getFont(1), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            sxf.setCellOptions(62464);
            addStyle(sxf);
            sxf = new StyleXFRecord(getFonts().getFont(1), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            sxf.setCellOptions(62464);
            addStyle(sxf);
            sxf = new StyleXFRecord(getFonts().getFont(2), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            sxf.setCellOptions(62464);
            addStyle(sxf);
            sxf = new StyleXFRecord(getFonts().getFont(3), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            sxf.setCellOptions(62464);
            addStyle(sxf);
            sxf = new StyleXFRecord(styles.getArial10Pt(), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            sxf.setCellOptions(62464);
            addStyle(sxf);
            sxf = new StyleXFRecord(styles.getArial10Pt(), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            sxf.setCellOptions(62464);
            addStyle(sxf);
            sxf = new StyleXFRecord(styles.getArial10Pt(), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            sxf.setCellOptions(62464);
            addStyle(sxf);
            sxf = new StyleXFRecord(styles.getArial10Pt(), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            sxf.setCellOptions(62464);
            addStyle(sxf);
            sxf = new StyleXFRecord(styles.getArial10Pt(), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            sxf.setCellOptions(62464);
            addStyle(sxf);
            sxf = new StyleXFRecord(styles.getArial10Pt(), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            sxf.setCellOptions(62464);
            addStyle(sxf);
            sxf = new StyleXFRecord(styles.getArial10Pt(), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            sxf.setCellOptions(62464);
            addStyle(sxf);
            sxf = new StyleXFRecord(styles.getArial10Pt(), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            sxf.setCellOptions(62464);
            addStyle(sxf);
            sxf = new StyleXFRecord(styles.getArial10Pt(), NumberFormats.DEFAULT);
            sxf.setLocked(true);
            sxf.setCellOptions(62464);
            addStyle(sxf);
            addStyle(styles.getNormalStyle());
            sxf = new StyleXFRecord(getFonts().getFont(1), NumberFormats.FORMAT7);
            sxf.setLocked(true);
            sxf.setCellOptions(63488);
            addStyle(sxf);
            sxf = new StyleXFRecord(getFonts().getFont(1), NumberFormats.FORMAT5);
            sxf.setLocked(true);
            sxf.setCellOptions(63488);
            addStyle(sxf);
            sxf = new StyleXFRecord(getFonts().getFont(1), NumberFormats.FORMAT8);
            sxf.setLocked(true);
            sxf.setCellOptions(63488);
            addStyle(sxf);
            sxf = new StyleXFRecord(getFonts().getFont(1), NumberFormats.FORMAT6);
            sxf.setLocked(true);
            sxf.setCellOptions(63488);
            addStyle(sxf);
            sxf = new StyleXFRecord(getFonts().getFont(1), NumberFormats.PERCENT_INTEGER);
            sxf.setLocked(true);
            sxf.setCellOptions(63488);
            addStyle(sxf);
        } catch (NumFormatRecordsException e) {
            Assert.verify(false, e.getMessage());
        }
    }
}
