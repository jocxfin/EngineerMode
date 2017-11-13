package jxl.biff.drawing;

import java.io.IOException;

import jxl.WorkbookSettings;
import jxl.biff.ContinueRecord;
import jxl.biff.IntegerHelper;
import jxl.biff.StringHelper;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.write.biff.File;

public class Button implements DrawingGroupObject {
    private static Logger logger = Logger.getLogger(Button.class);
    private int blipId;
    private int column;
    private String commentText;
    private DrawingData drawingData;
    private DrawingGroup drawingGroup;
    private int drawingNumber;
    private ContinueRecord formatting;
    private boolean initialized = false;
    private MsoDrawingRecord mso;
    private MsoDrawingRecord msoDrawingRecord;
    private ObjRecord objRecord;
    private int objectId;
    private Origin origin;
    private EscherContainer readSpContainer;
    private int row;
    private int shapeId;
    private EscherContainer spContainer;
    private ContinueRecord text;
    private TextObjectRecord txo;
    private ShapeType type;
    private WorkbookSettings workbookSettings;

    public Button(MsoDrawingRecord msodr, ObjRecord obj, DrawingData dd, DrawingGroup dg, WorkbookSettings ws) {
        boolean z = false;
        this.drawingGroup = dg;
        this.msoDrawingRecord = msodr;
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
        initialize();
    }

    public Button(DrawingGroupObject dgo, DrawingGroup dg, WorkbookSettings ws) {
        Button d = (Button) dgo;
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
        this.workbookSettings = ws;
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
        Assert.verify(false);
        return this.spContainer;
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

    public void setTextObject(TextObjectRecord t) {
        this.txo = t;
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
            Assert.verify(false);
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

    public void writeTailRecords(File outputFile) {
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
        return this.mso.isFirst();
    }

    public boolean isFormObject() {
        return true;
    }
}
