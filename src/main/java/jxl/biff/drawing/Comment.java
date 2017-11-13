package jxl.biff.drawing;

import java.io.IOException;

import jxl.WorkbookSettings;
import jxl.biff.ContinueRecord;
import jxl.biff.IntegerHelper;
import jxl.biff.StringHelper;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.write.biff.File;

public class Comment implements DrawingGroupObject {
    private static Logger logger = Logger.getLogger(Comment.class);
    private int blipId;
    private int column;
    private String commentText;
    private DrawingData drawingData;
    private DrawingGroup drawingGroup;
    private int drawingNumber;
    private ContinueRecord formatting;
    private double height;
    private boolean initialized;
    private MsoDrawingRecord mso;
    private MsoDrawingRecord msoDrawingRecord;
    private NoteRecord note;
    private ObjRecord objRecord;
    private int objectId;
    private Origin origin;
    private EscherContainer readSpContainer;
    private int referenceCount;
    private int row;
    private int shapeId;
    private EscherContainer spContainer;
    private ContinueRecord text;
    private TextObjectRecord txo;
    private ShapeType type;
    private double width;
    private WorkbookSettings workbookSettings;

    public Comment(MsoDrawingRecord msorec, ObjRecord obj, DrawingData dd, DrawingGroup dg, WorkbookSettings ws) {
        boolean z = false;
        this.initialized = false;
        this.drawingGroup = dg;
        this.msoDrawingRecord = msorec;
        this.drawingData = dd;
        this.objRecord = obj;
        this.initialized = false;
        this.workbookSettings = ws;
        this.origin = Origin.READ;
        this.drawingData.addData(this.msoDrawingRecord.getData());
        this.drawingNumber = this.drawingData.getNumDrawings() - 1;
        this.drawingGroup.addDrawing(this);
        if (!(this.msoDrawingRecord == null || this.objRecord == null)) {
            z = true;
        }
        Assert.verify(z);
        if (!this.initialized) {
            initialize();
        }
    }

    public Comment(DrawingGroupObject dgo, DrawingGroup dg, WorkbookSettings ws) {
        this.initialized = false;
        Comment d = (Comment) dgo;
        Assert.verify(d.origin == Origin.READ);
        this.msoDrawingRecord = d.msoDrawingRecord;
        this.objRecord = d.objRecord;
        this.initialized = false;
        this.origin = Origin.READ;
        this.drawingData = d.drawingData;
        this.drawingGroup = dg;
        this.drawingNumber = d.drawingNumber;
        this.drawingGroup.addDrawing(this);
        this.mso = d.mso;
        this.txo = d.txo;
        this.text = d.text;
        this.formatting = d.formatting;
        this.note = d.note;
        this.width = d.width;
        this.height = d.height;
        this.workbookSettings = ws;
    }

    public Comment(String txt, int c, int r) {
        this.initialized = false;
        this.initialized = true;
        this.origin = Origin.WRITE;
        this.column = c;
        this.row = r;
        this.referenceCount = 1;
        this.type = ShapeType.TEXT_BOX;
        this.commentText = txt;
        this.width = 3.0d;
        this.height = 4.0d;
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
            this.column = ((int) clientAnchor.getX1()) - 1;
            this.row = ((int) clientAnchor.getY1()) + 1;
            this.width = clientAnchor.getX2() - clientAnchor.getX1();
            this.height = clientAnchor.getY2() - clientAnchor.getY1();
        } else {
            logger.warn("client anchor not found");
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
        if (this.spContainer == null) {
            this.spContainer = new SpContainer();
            this.spContainer.add(new Sp(this.type, this.shapeId, 2560));
            Opt opt = new Opt();
            opt.addProperty(344, false, false, 0);
            opt.addProperty(385, false, false, 134217808);
            opt.addProperty(387, false, false, 134217808);
            opt.addProperty(959, false, false, 131074);
            this.spContainer.add(opt);
            this.spContainer.add(new ClientAnchor(((double) this.column) + 1.3d, Math.max(0.0d, ((double) this.row) - 0.6d), (((double) this.column) + 1.3d) + this.width, ((double) this.row) + this.height, 1));
            this.spContainer.add(new ClientData());
            this.spContainer.add(new ClientTextBox());
        }
        return this.spContainer;
    }

    public void setDrawingGroup(DrawingGroup dg) {
        this.drawingGroup = dg;
    }

    public Origin getOrigin() {
        return this.origin;
    }

    public double getWidth() {
        if (!this.initialized) {
            initialize();
        }
        return this.width;
    }

    public void setWidth(double w) {
        if (this.origin == Origin.READ) {
            if (!this.initialized) {
                initialize();
            }
            this.origin = Origin.READ_WRITE;
        }
        this.width = w;
    }

    public double getHeight() {
        if (!this.initialized) {
            initialize();
        }
        return this.height;
    }

    public void setHeight(double h) {
        if (this.origin == Origin.READ) {
            if (!this.initialized) {
                initialize();
            }
            this.origin = Origin.READ_WRITE;
        }
        this.height = h;
    }

    private EscherContainer getReadSpContainer() {
        if (!this.initialized) {
            initialize();
        }
        return this.readSpContainer;
    }

    public void setTextObject(TextObjectRecord t) {
        this.txo = t;
    }

    public void setNote(NoteRecord t) {
        this.note = t;
    }

    public void setText(ContinueRecord t) {
        this.text = t;
    }

    public void setFormatting(ContinueRecord t) {
        this.formatting = t;
    }

    public String getImageFilePath() {
        Assert.verify(false);
        return null;
    }

    public void addMso(MsoDrawingRecord d) {
        this.mso = d;
        this.drawingData.addRawData(this.mso.getData());
    }

    public void writeAdditionalRecords(File outputFile) throws IOException {
        if (this.origin != Origin.READ) {
            outputFile.write(new ObjRecord(this.objectId, ObjRecord.EXCELNOTE));
            outputFile.write(new MsoDrawingRecord(new ClientTextBox().getData()));
            outputFile.write(new TextObjectRecord(getText()));
            byte[] textData = new byte[((this.commentText.length() * 2) + 1)];
            textData[0] = (byte) 1;
            StringHelper.getUnicodeBytes(this.commentText, textData, 1);
            outputFile.write(new ContinueRecord(textData));
            byte[] frData = new byte[16];
            IntegerHelper.getTwoBytes(0, frData, 0);
            IntegerHelper.getTwoBytes(0, frData, 2);
            IntegerHelper.getTwoBytes(this.commentText.length(), frData, 8);
            IntegerHelper.getTwoBytes(0, frData, 10);
            outputFile.write(new ContinueRecord(frData));
            return;
        }
        outputFile.write(this.objRecord);
        if (this.mso != null) {
            outputFile.write(this.mso);
        }
        outputFile.write(this.txo);
        outputFile.write(this.text);
        if (this.formatting != null) {
            outputFile.write(this.formatting);
        }
    }

    public void writeTailRecords(File outputFile) throws IOException {
        if (this.origin != Origin.READ) {
            outputFile.write(new NoteRecord(this.column, this.row, this.objectId));
        } else {
            outputFile.write(this.note);
        }
    }

    public int getRow() {
        return this.note.getRow();
    }

    public int getColumn() {
        return this.note.getColumn();
    }

    public String getText() {
        if (this.commentText == null) {
            boolean z;
            if (this.text == null) {
                z = false;
            } else {
                z = true;
            }
            Assert.verify(z);
            byte[] td = this.text.getData();
            if (td[0] != (byte) 0) {
                this.commentText = StringHelper.getUnicodeString(td, (td.length - 1) / 2, 1);
            } else {
                this.commentText = StringHelper.getString(td, td.length - 1, 1, this.workbookSettings);
            }
        }
        return this.commentText;
    }

    public int hashCode() {
        return this.commentText.hashCode();
    }

    public boolean isFirst() {
        return this.msoDrawingRecord.isFirst();
    }

    public boolean isFormObject() {
        return true;
    }
}
