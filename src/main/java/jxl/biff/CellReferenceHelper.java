package jxl.biff;

import jxl.biff.formula.ExternalSheet;
import jxl.common.Logger;

public final class CellReferenceHelper {
    private static Logger logger = Logger.getLogger(CellReferenceHelper.class);

    private CellReferenceHelper() {
    }

    public static void getCellReference(int column, int row, StringBuffer buf) {
        getColumnReference(column, buf);
        buf.append(Integer.toString(row + 1));
    }

    public static void getCellReference(int column, boolean colabs, int row, boolean rowabs, StringBuffer buf) {
        if (colabs) {
            buf.append('$');
        }
        getColumnReference(column, buf);
        if (rowabs) {
            buf.append('$');
        }
        buf.append(Integer.toString(row + 1));
    }

    public static void getColumnReference(int column, StringBuffer buf) {
        int r = column % 26;
        StringBuffer tmp = new StringBuffer();
        for (int v = column / 26; v != 0; v /= 26) {
            tmp.append((char) (r + 65));
            r = (v % 26) - 1;
        }
        tmp.append((char) (r + 65));
        for (int i = tmp.length() - 1; i >= 0; i--) {
            buf.append(tmp.charAt(i));
        }
    }

    public static void getCellReference(int sheet, int column, int row, ExternalSheet workbook, StringBuffer buf) {
        buf.append(StringHelper.replace(workbook.getExternalSheetName(sheet), "'", "''"));
        buf.append('!');
        getCellReference(column, row, buf);
    }

    public static void getCellReference(int sheet, int column, boolean colabs, int row, boolean rowabs, ExternalSheet workbook, StringBuffer buf) {
        buf.append(workbook.getExternalSheetName(sheet));
        buf.append('!');
        getCellReference(column, colabs, row, rowabs, buf);
    }

    public static String getCellReference(int column, int row) {
        StringBuffer buf = new StringBuffer();
        getCellReference(column, row, buf);
        return buf.toString();
    }

    public static int getColumn(String s) {
        int colnum = 0;
        int numindex = getNumberIndex(s);
        String s2 = s.toUpperCase();
        int startPos = s.lastIndexOf(33) + 1;
        if (s.charAt(startPos) == '$') {
            startPos++;
        }
        int endPos = numindex;
        if (s.charAt(numindex - 1) == '$') {
            endPos = numindex - 1;
        }
        for (int i = startPos; i < endPos; i++) {
            if (i != startPos) {
                colnum = (colnum + 1) * 26;
            }
            colnum += s2.charAt(i) - 65;
        }
        return colnum;
    }

    public static int getRow(String s) {
        try {
            return Integer.parseInt(s.substring(getNumberIndex(s))) - 1;
        } catch (NumberFormatException e) {
            logger.warn(e, e);
            return 65535;
        }
    }

    private static int getNumberIndex(String s) {
        boolean numberFound = false;
        int pos = s.lastIndexOf(33) + 1;
        while (!numberFound && pos < s.length()) {
            char c = s.charAt(pos);
            if (c >= '0' && c <= '9') {
                numberFound = true;
            } else {
                pos++;
            }
        }
        return pos;
    }

    public static boolean isColumnRelative(String s) {
        return s.charAt(0) != '$';
    }

    public static boolean isRowRelative(String s) {
        return s.charAt(getNumberIndex(s) + -1) != '$';
    }
}
