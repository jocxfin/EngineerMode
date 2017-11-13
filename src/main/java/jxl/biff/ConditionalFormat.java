package jxl.biff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jxl.write.biff.File;

public class ConditionalFormat {
    private ArrayList conditions = new ArrayList();
    private ConditionalFormatRangeRecord range;

    public ConditionalFormat(ConditionalFormatRangeRecord cfrr) {
        this.range = cfrr;
    }

    public void addCondition(ConditionalFormatRecord cond) {
        this.conditions.add(cond);
    }

    public void write(File outputFile) throws IOException {
        outputFile.write(this.range);
        Iterator i = this.conditions.iterator();
        while (i.hasNext()) {
            outputFile.write((ConditionalFormatRecord) i.next());
        }
    }
}
