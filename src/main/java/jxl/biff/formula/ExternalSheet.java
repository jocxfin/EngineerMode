package jxl.biff.formula;

import jxl.read.biff.BOFRecord;

public interface ExternalSheet {
    int getExternalSheetIndex(String str);

    String getExternalSheetName(int i);

    BOFRecord getWorkbookBof();
}
