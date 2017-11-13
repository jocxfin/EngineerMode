package jxl.write;

import jxl.Hyperlink;
import jxl.write.biff.HyperlinkRecord;

public class WritableHyperlink extends HyperlinkRecord implements Hyperlink {
    public WritableHyperlink(Hyperlink h, WritableSheet ws) {
        super(h, ws);
    }
}
