package jxl.biff.drawing;

import java.io.IOException;

import jxl.common.Assert;
import jxl.common.Logger;
import jxl.write.biff.File;

public class Drawing2 implements DrawingGroupObject {
    private static Logger logger = Logger.getLogger(Drawing.class);
    private int blipId;
    private DrawingData drawingData;
    private DrawingGroup drawingGroup;
    private boolean initialized = false;
    private MsoDrawingRecord msoDrawingRecord;
    private int objectId;
    private Origin origin;
    private EscherContainer readSpContainer;
    private int shapeId;

    public Drawing2(MsoDrawingRecord mso, DrawingData dd, DrawingGroup dg) {
        boolean z = false;
        this.drawingGroup = dg;
        this.msoDrawingRecord = mso;
        this.drawingData = dd;
        this.initialized = false;
        this.origin = Origin.READ;
        this.drawingData.addRawData(this.msoDrawingRecord.getData());
        this.drawingGroup.addDrawing(this);
        if (mso != null) {
            z = true;
        }
        Assert.verify(z);
        initialize();
    }

    private void initialize() {
        this.initialized = true;
    }

    public final void setObjectId(int objid, int bip, int sid) {
        this.objectId = objid;
        this.blipId = bip;
        this.shapeId = sid;
        if (this.origin == Origin.READ) {
            this.origin = Origin.READ_WRITE;
        }
    }

    public final int getObjectId() {
        if (!this.initialized) {
            initialize();
        }
        return this.objectId;
    }

    public int getShapeId() {
        if (!this.initialized) {
            initialize();
        }
        return this.shapeId;
    }

    public MsoDrawingRecord getMsoDrawingRecord() {
        return this.msoDrawingRecord;
    }

    public EscherContainer getSpContainer() {
        boolean z = false;
        if (!this.initialized) {
            initialize();
        }
        if (this.origin == Origin.READ) {
            z = true;
        }
        Assert.verify(z);
        return getReadSpContainer();
    }

    public void setDrawingGroup(DrawingGroup dg) {
        this.drawingGroup = dg;
    }

    public Origin getOrigin() {
        return this.origin;
    }

    private EscherContainer getReadSpContainer() {
        if (!this.initialized) {
            initialize();
        }
        return this.readSpContainer;
    }

    public void writeAdditionalRecords(File outputFile) throws IOException {
    }

    public void writeTailRecords(File outputFile) throws IOException {
    }

    public boolean isFirst() {
        return this.msoDrawingRecord.isFirst();
    }

    public boolean isFormObject() {
        return false;
    }

    public String getImageFilePath() {
        Assert.verify(false);
        return null;
    }
}
