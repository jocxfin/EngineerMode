package jxl.biff.drawing;

import jxl.WorkbookSettings;
import jxl.biff.ByteData;
import jxl.biff.IndexMapping;
import jxl.biff.IntegerHelper;
import jxl.biff.Type;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.read.biff.File;

public class Chart implements ByteData, EscherStream {
    private static final Logger logger = Logger.getLogger(Chart.class);
    private byte[] data;
    private DrawingData drawingData;
    private int drawingNumber;
    private int endpos;
    private File file;
    private boolean initialized;
    private MsoDrawingRecord msoDrawingRecord;
    private ObjRecord objRecord;
    private int startpos;
    private WorkbookSettings workbookSettings;

    public Chart(MsoDrawingRecord mso, ObjRecord obj, DrawingData dd, int sp, int ep, File f, WorkbookSettings ws) {
        boolean z = false;
        this.msoDrawingRecord = mso;
        this.objRecord = obj;
        this.startpos = sp;
        this.endpos = ep;
        this.file = f;
        this.workbookSettings = ws;
        if (this.msoDrawingRecord != null) {
            this.drawingData = dd;
            this.drawingData.addData(this.msoDrawingRecord.getRecord().getData());
            this.drawingNumber = this.drawingData.getNumDrawings() - 1;
        }
        this.initialized = false;
        if (mso == null || obj == null) {
            if (mso == null) {
                if (obj != null) {
                }
            }
            Assert.verify(z);
        }
        z = true;
        Assert.verify(z);
    }

    public byte[] getBytes() {
        if (!this.initialized) {
            initialize();
        }
        return this.data;
    }

    public byte[] getData() {
        return this.msoDrawingRecord.getRecord().getData();
    }

    private void initialize() {
        this.data = this.file.read(this.startpos, this.endpos - this.startpos);
        this.initialized = true;
    }

    public void rationalize(IndexMapping xfMapping, IndexMapping fontMapping, IndexMapping formatMapping) {
        if (!this.initialized) {
            initialize();
        }
        int pos = 0;
        while (pos < this.data.length) {
            int code = IntegerHelper.getInt(this.data[pos], this.data[pos + 1]);
            int length = IntegerHelper.getInt(this.data[pos + 2], this.data[pos + 3]);
            Type type = Type.getType(code);
            if (type == Type.FONTX) {
                IntegerHelper.getTwoBytes(fontMapping.getNewIndex(IntegerHelper.getInt(this.data[pos + 4], this.data[pos + 5])), this.data, pos + 4);
            } else if (type == Type.FBI) {
                IntegerHelper.getTwoBytes(fontMapping.getNewIndex(IntegerHelper.getInt(this.data[pos + 12], this.data[pos + 13])), this.data, pos + 12);
            } else if (type == Type.IFMT) {
                IntegerHelper.getTwoBytes(formatMapping.getNewIndex(IntegerHelper.getInt(this.data[pos + 4], this.data[pos + 5])), this.data, pos + 4);
            } else if (type == Type.ALRUNS) {
                int numRuns = IntegerHelper.getInt(this.data[pos + 4], this.data[pos + 5]);
                int fontPos = pos + 6;
                for (int i = 0; i < numRuns; i++) {
                    IntegerHelper.getTwoBytes(fontMapping.getNewIndex(IntegerHelper.getInt(this.data[fontPos + 2], this.data[fontPos + 3])), this.data, fontPos + 2);
                    fontPos += 4;
                }
            }
            pos += length + 4;
        }
    }

    EscherContainer getSpContainer() {
        return this.drawingData.getSpContainer(this.drawingNumber);
    }

    MsoDrawingRecord getMsoDrawingRecord() {
        return this.msoDrawingRecord;
    }

    ObjRecord getObjRecord() {
        return this.objRecord;
    }
}
