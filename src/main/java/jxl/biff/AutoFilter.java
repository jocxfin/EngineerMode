package jxl.biff;

import java.io.IOException;

import jxl.write.biff.File;

public class AutoFilter {
    private AutoFilterRecord autoFilter;
    private AutoFilterInfoRecord autoFilterInfo;
    private FilterModeRecord filterMode;

    public AutoFilter(FilterModeRecord fmr, AutoFilterInfoRecord afir) {
        this.filterMode = fmr;
        this.autoFilterInfo = afir;
    }

    public void add(AutoFilterRecord af) {
        this.autoFilter = af;
    }

    public void write(File outputFile) throws IOException {
        if (this.filterMode != null) {
            outputFile.write(this.filterMode);
        }
        if (this.autoFilterInfo != null) {
            outputFile.write(this.autoFilterInfo);
        }
        if (this.autoFilter != null) {
            outputFile.write(this.autoFilter);
        }
    }
}
