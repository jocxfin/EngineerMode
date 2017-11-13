package jxl;

public final class CellReferenceHelper {
    private CellReferenceHelper() {
    }

    public static void getCellReference(int column, int row, StringBuffer buf) {
        jxl.biff.CellReferenceHelper.getCellReference(column, row, buf);
    }

    public static String getCellReference(int column, int row) {
        return jxl.biff.CellReferenceHelper.getCellReference(column, row);
    }

    public static String getCellReference(Cell c) {
        return getCellReference(c.getColumn(), c.getRow());
    }
}
