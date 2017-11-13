package jxl.read.biff;

import jxl.biff.IntegerHelper;
import jxl.biff.RecordData;
import jxl.common.Logger;

public class CountryRecord extends RecordData {
    private static Logger logger = Logger.getLogger(CountryRecord.class);
    private int language;
    private int regionalSettings;

    public CountryRecord(Record t) {
        super(t);
        byte[] data = t.getData();
        this.language = IntegerHelper.getInt(data[0], data[1]);
        this.regionalSettings = IntegerHelper.getInt(data[2], data[3]);
    }

    public int getLanguageCode() {
        return this.language;
    }

    public int getRegionalSettingsCode() {
        return this.regionalSettings;
    }
}
