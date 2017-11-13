package jxl.write.biff;

import jxl.biff.Fonts;
import jxl.write.WritableFont;

public class WritableFonts extends Fonts {
    public WritableFonts(WritableWorkbookImpl w) {
        addFont(w.getStyles().getArial10Pt());
        addFont(new WritableFont(WritableFont.ARIAL));
        addFont(new WritableFont(WritableFont.ARIAL));
        addFont(new WritableFont(WritableFont.ARIAL));
    }
}
