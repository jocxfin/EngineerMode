package jxl.write.biff;

import jxl.biff.CountryCode;
import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;

class CountryRecord extends WritableRecordData {
    private int language;
    private int regionalSettings;

    public CountryRecord(CountryCode lang, CountryCode r) {
        super(Type.COUNTRY);
        this.language = lang.getValue();
        this.regionalSettings = r.getValue();
    }

    public CountryRecord(jxl.read.biff.CountryRecord cr) {
        super(Type.COUNTRY);
        this.language = cr.getLanguageCode();
        this.regionalSettings = cr.getRegionalSettingsCode();
    }

    public byte[] getData() {
        byte[] data = new byte[4];
        IntegerHelper.getTwoBytes(this.language, data, 0);
        IntegerHelper.getTwoBytes(this.regionalSettings, data, 2);
        return data;
    }
}
