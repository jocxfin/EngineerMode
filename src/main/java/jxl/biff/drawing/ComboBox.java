package jxl.biff.drawing;

import java.io.IOException;

import jxl.WorkbookSettings;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.write.biff.File;

public class ComboBox implements DrawingGroupObject {
    private static Logger logger = Logger.getLogger(ComboBox.class);
    private int blipId;
    private int column;
    private DrawingData drawingData;
    private DrawingGroup drawingGroup;
    private int drawingNumber;
    private boolean initialized;
    private MsoDrawingRecord msoDrawingRecord;
    private ObjRecord objRecord;
    private int objectId;
    private Origin origin;
    private EscherContainer readSpContainer;
    private int referenceCount;
    private int row;
    private int shapeId;
    private ShapeType type;
    private WorkbookSettings workbookSettings;

    public ComboBox(MsoDrawingRecord mso, ObjRecord obj, DrawingData dd, DrawingGroup dg, WorkbookSettings ws) {
        boolean z = false;
        this.initialized = false;
        this.drawingGroup = dg;
        this.msoDrawingRecord = mso;
        this.drawingData = dd;
        this.objRecord = obj;
        this.initialized = false;
        this.workbookSettings = ws;
        this.origin = Origin.READ;
        this.drawingData.addData(this.msoDrawingRecord.getData());
        this.drawingNumber = this.drawingData.getNumDrawings() - 1;
        this.drawingGroup.addDrawing(this);
        if (!(mso == null || obj == null)) {
            z = true;
        }
        Assert.verify(z);
        initialize();
    }

    public ComboBox(DrawingGroupObject dgo, DrawingGroup dg, WorkbookSettings ws) {
        this.initialized = false;
        ComboBox d = (ComboBox) dgo;
        Assert.verify(d.origin == Origin.READ);
        this.msoDrawingRecord = d.msoDrawingRecord;
        this.objRecord = d.objRecord;
        this.initialized = false;
        this.origin = Origin.READ;
        this.drawingData = d.drawingData;
        this.drawingGroup = dg;
        this.drawingNumber = d.drawingNumber;
        this.drawingGroup.addDrawing(this);
        this.workbookSettings = ws;
    }

    public ComboBox() {
        this.initialized = false;
        this.initialized = true;
        this.origin = Origin.WRITE;
        this.referenceCount = 1;
        this.type = ShapeType.HOST_CONTROL;
    }

    private void initialize() {
        boolean z;
        this.readSpContainer = this.drawingData.getSpContainer(this.drawingNumber);
        if (this.readSpContainer == null) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
        EscherRecord[] children = this.readSpContainer.getChildren();
        Sp sp = this.readSpContainer.getChildren()[0];
        this.objectId = this.objRecord.getObjectId();
        this.shapeId = sp.getShapeId();
        this.type = ShapeType.getType(sp.getShapeType());
        if (this.type == ShapeType.UNKNOWN) {
            logger.warn("Unknown shape type");
        }
        ClientAnchor clientAnchor = null;
        for (int i = 0; i < children.length && clientAnchor == null; i++) {
            if (children[i].getType() == EscherRecordType.CLIENT_ANCHOR) {
                clientAnchor = children[i];
            }
        }
        if (clientAnchor != null) {
            this.column = (int) clientAnchor.getX1();
            this.row = (int) clientAnchor.getY1();
        } else {
            logger.warn("Client anchor not found");
        }
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

    public final int getShapeId() {
        if (!this.initialized) {
            initialize();
        }
        return this.shapeId;
    }

    public MsoDrawingRecord getMsoDrawingRecord() {
        return this.msoDrawingRecord;
    }

    public EscherContainer getSpContainer() {
        if (!this.initialized) {
            initialize();
        }
        if (this.origin == Origin.READ) {
            return getReadSpContainer();
        }
        SpContainer spc = new SpContainer();
        spc.add(new Sp(this.type, this.shapeId, 2560));
        Opt opt = new Opt();
        opt.addProperty(127, false, false, 17039620);
        opt.addProperty(191, false, false, 524296);
        opt.addProperty(511, false, false, 524288);
        opt.addProperty(959, false, false, 131072);
        spc.add(opt);
        spc.add(new ClientAnchor((double) this.column, (double) this.row, (double) (this.column + 1), (double) (this.row + 1), 1));
        spc.add(new ClientData());
        return spc;
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

    public String getImageFilePath() {
        Assert.verify(false);
        return null;
    }

    public void writeAdditionalRecords(File outputFile) throws IOException {
        if (this.origin != Origin.READ) {
            outputFile.write(new ObjRecord(this.objectId, ObjRecord.COMBOBOX));
        } else {
            outputFile.write(this.objRecord);
        }
    }

    public void writeTailRecords(File outputFile) {
    }

    public int hashCode() {
        return getClass().getName().hashCode();
    }

    public boolean isFirst() {
        return this.msoDrawingRecord.isFirst();
    }

    public boolean isFormObject() {
        return false;
    }
}
