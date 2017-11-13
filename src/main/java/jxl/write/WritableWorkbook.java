package jxl.write;

import java.io.IOException;

import jxl.format.Colour;
import jxl.format.UnderlineStyle;

public abstract class WritableWorkbook {
    public static final WritableFont ARIAL_10_PT = new WritableFont(WritableFont.ARIAL);
    public static final WritableCellFormat HIDDEN_STYLE = new WritableCellFormat(new DateFormat(";;;"));
    public static final WritableFont HYPERLINK_FONT = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.SINGLE, Colour.BLUE);
    public static final WritableCellFormat HYPERLINK_STYLE = new WritableCellFormat(HYPERLINK_FONT);
    public static final WritableCellFormat NORMAL_STYLE = new WritableCellFormat(ARIAL_10_PT, NumberFormats.DEFAULT);

    public abstract void close() throws IOException, WriteException;

    public abstract WritableSheet createSheet(String str, int i);

    public abstract WritableSheet[] getSheets();

    public abstract void write() throws IOException;

    protected WritableWorkbook() {
    }
}
