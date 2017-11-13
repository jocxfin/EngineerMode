package jxl.biff.drawing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jxl.Sheet;
import jxl.common.Assert;
import jxl.common.Logger;

public class Drawing implements DrawingGroupObject {
    public static ImageAnchorProperties MOVE_AND_SIZE_WITH_CELLS = new ImageAnchorProperties(1);
    public static ImageAnchorProperties MOVE_WITH_CELLS = new ImageAnchorProperties(2);
    public static ImageAnchorProperties NO_MOVE_OR_SIZE_WITH_CELLS = new ImageAnchorProperties(3);
    private static Logger logger = Logger.getLogger(Drawing.class);
    private int blipId;
    private DrawingData drawingData;
    private DrawingGroup drawingGroup;
    private int drawingNumber;
    private double height;
    private ImageAnchorProperties imageAnchorProperties;
    private byte[] imageData;
    private File imageFile;
    private boolean initialized = false;
    private MsoDrawingRecord msoDrawingRecord;
    private ObjRecord objRecord;
    private int objectId;
    private Origin origin;
    private EscherContainer readSpContainer;
    private int referenceCount;
    private int shapeId;
    private Sheet sheet;
    private ShapeType type;
    private double width;
    private double x;
    private double y;

    protected static class ImageAnchorProperties {
        private static ImageAnchorProperties[] o = new ImageAnchorProperties[0];
        private int value;

        ImageAnchorProperties(int v) {
            this.value = v;
            ImageAnchorProperties[] oldArray = o;
            o = new ImageAnchorProperties[(oldArray.length + 1)];
            System.arraycopy(oldArray, 0, o, 0, oldArray.length);
            o[oldArray.length] = this;
        }

        int getValue() {
            return this.value;
        }

        static ImageAnchorProperties getImageAnchorProperties(int val) {
            ImageAnchorProperties iap = Drawing.MOVE_AND_SIZE_WITH_CELLS;
            for (int pos = 0; pos < o.length; pos++) {
                if (o[pos].getValue() == val) {
                    return o[pos];
                }
            }
            return iap;
        }
    }

    public Drawing(MsoDrawingRecord mso, ObjRecord obj, DrawingData dd, DrawingGroup dg, Sheet s) {
        boolean z = false;
        this.drawingGroup = dg;
        this.msoDrawingRecord = mso;
        this.drawingData = dd;
        this.objRecord = obj;
        this.sheet = s;
        this.initialized = false;
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

    protected Drawing(DrawingGroupObject dgo, DrawingGroup dg) {
        Drawing d = (Drawing) dgo;
        Assert.verify(d.origin == Origin.READ);
        this.msoDrawingRecord = d.msoDrawingRecord;
        this.objRecord = d.objRecord;
        this.initialized = false;
        this.origin = Origin.READ;
        this.drawingData = d.drawingData;
        this.drawingGroup = dg;
        this.drawingNumber = d.drawingNumber;
        this.drawingGroup.addDrawing(this);
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
        this.shapeId = sp.getShapeId();
        this.objectId = this.objRecord.getObjectId();
        this.type = ShapeType.getType(sp.getShapeType());
        if (this.type == ShapeType.UNKNOWN) {
            logger.warn("Unknown shape type");
        }
        Opt opt = this.readSpContainer.getChildren()[1];
        if (opt.getProperty(260) != null) {
            this.blipId = opt.getProperty(260).value;
        }
        if (opt.getProperty(261) != null) {
            this.imageFile = new File(opt.getProperty(261).stringValue);
        } else if (this.type == ShapeType.PICTURE_FRAME) {
            logger.warn("no filename property for drawing");
            this.imageFile = new File(Integer.toString(this.blipId));
        }
        ClientAnchor clientAnchor = null;
        for (int i = 0; i < children.length && clientAnchor == null; i++) {
            if (children[i].getType() == EscherRecordType.CLIENT_ANCHOR) {
                clientAnchor = children[i];
            }
        }
        if (clientAnchor != null) {
            this.x = clientAnchor.getX1();
            this.y = clientAnchor.getY1();
            this.width = clientAnchor.getX2() - this.x;
            this.height = clientAnchor.getY2() - this.y;
            this.imageAnchorProperties = ImageAnchorProperties.getImageAnchorProperties(clientAnchor.getProperties());
        } else {
            logger.warn("client anchor not found");
        }
        if (this.blipId == 0) {
            logger.warn("linked drawings are not supported");
        }
        this.initialized = true;
    }

    public String getImageFilePath() {
        if (this.imageFile != null) {
            return this.imageFile.getPath();
        }
        return this.blipId == 0 ? "__new__image__" : Integer.toString(this.blipId);
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

    public final int getBlipId() {
        if (!this.initialized) {
            initialize();
        }
        return this.blipId;
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
        SpContainer spContainer = new SpContainer();
        spContainer.add(new Sp(this.type, this.shapeId, 2560));
        Opt opt = new Opt();
        opt.addProperty(260, true, false, this.blipId);
        if (this.type == ShapeType.PICTURE_FRAME) {
            String filePath = this.imageFile == null ? "" : this.imageFile.getPath();
            opt.addProperty(261, true, true, filePath.length() * 2, filePath);
            opt.addProperty(447, false, false, 65536);
            opt.addProperty(959, false, false, 524288);
            spContainer.add(opt);
        }
        spContainer.add(new ClientAnchor(this.x, this.y, this.width + this.x, this.height + this.y, this.imageAnchorProperties.getValue()));
        spContainer.add(new ClientData());
        return spContainer;
    }

    public void setDrawingGroup(DrawingGroup dg) {
        this.drawingGroup = dg;
    }

    public Origin getOrigin() {
        return this.origin;
    }

    public int getReferenceCount() {
        return this.referenceCount;
    }

    public void setReferenceCount(int r) {
        this.referenceCount = r;
    }

    private EscherContainer getReadSpContainer() {
        if (!this.initialized) {
            initialize();
        }
        return this.readSpContainer;
    }

    public byte[] getImageData() {
        boolean z = false;
        if (this.origin == Origin.READ || this.origin == Origin.READ_WRITE) {
            z = true;
        }
        Assert.verify(z);
        if (!this.initialized) {
            initialize();
        }
        return this.drawingGroup.getImageData(this.blipId);
    }

    public byte[] getImageBytes() throws IOException {
        boolean z = false;
        if (this.origin == Origin.READ || this.origin == Origin.READ_WRITE) {
            return getImageData();
        }
        boolean z2;
        if (this.origin != Origin.WRITE) {
            z2 = false;
        } else {
            z2 = true;
        }
        Assert.verify(z2);
        if (this.imageFile != null) {
            byte[] data = new byte[((int) this.imageFile.length())];
            FileInputStream fis = new FileInputStream(this.imageFile);
            fis.read(data, 0, data.length);
            fis.close();
            return data;
        }
        if (this.imageData != null) {
            z = true;
        }
        Assert.verify(z);
        return this.imageData;
    }

    public void writeAdditionalRecords(jxl.write.biff.File outputFile) throws IOException {
        if (this.origin != Origin.READ) {
            outputFile.write(new ObjRecord(this.objectId, ObjRecord.PICTURE));
        } else {
            outputFile.write(this.objRecord);
        }
    }

    public void writeTailRecords(jxl.write.biff.File outputFile) throws IOException {
    }

    public boolean isFirst() {
        return this.msoDrawingRecord.isFirst();
    }

    public boolean isFormObject() {
        return false;
    }
}
