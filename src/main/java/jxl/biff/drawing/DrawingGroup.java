package jxl.biff.drawing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import jxl.common.Assert;
import jxl.common.Logger;
import jxl.read.biff.Record;
import jxl.write.biff.File;

public class DrawingGroup implements EscherStream {
    private static Logger logger = Logger.getLogger(DrawingGroup.class);
    private BStoreContainer bstoreContainer;
    private byte[] drawingData;
    private int drawingGroupId;
    private ArrayList drawings;
    private boolean drawingsOmitted;
    private EscherContainer escherData;
    private HashMap imageFiles;
    private boolean initialized;
    private int maxObjectId;
    private int maxShapeId;
    private int numBlips;
    private int numCharts;
    private Origin origin;

    public DrawingGroup(Origin o) {
        this.origin = o;
        this.initialized = o == Origin.WRITE;
        this.drawings = new ArrayList();
        this.imageFiles = new HashMap();
        this.drawingsOmitted = false;
        this.maxObjectId = 1;
        this.maxShapeId = 1024;
    }

    public DrawingGroup(DrawingGroup dg) {
        this.drawingData = dg.drawingData;
        this.escherData = dg.escherData;
        this.bstoreContainer = dg.bstoreContainer;
        this.initialized = dg.initialized;
        this.drawingData = dg.drawingData;
        this.escherData = dg.escherData;
        this.bstoreContainer = dg.bstoreContainer;
        this.numBlips = dg.numBlips;
        this.numCharts = dg.numCharts;
        this.drawingGroupId = dg.drawingGroupId;
        this.drawingsOmitted = dg.drawingsOmitted;
        this.origin = dg.origin;
        this.imageFiles = (HashMap) dg.imageFiles.clone();
        this.maxObjectId = dg.maxObjectId;
        this.maxShapeId = dg.maxShapeId;
        this.drawings = new ArrayList();
    }

    public void add(MsoDrawingGroupRecord mso) {
        addData(mso.getData());
    }

    public void add(Record cont) {
        addData(cont.getData());
    }

    private void addData(byte[] msodata) {
        if (this.drawingData != null) {
            byte[] newdata = new byte[(this.drawingData.length + msodata.length)];
            System.arraycopy(this.drawingData, 0, newdata, 0, this.drawingData.length);
            System.arraycopy(msodata, 0, newdata, this.drawingData.length, msodata.length);
            this.drawingData = newdata;
            return;
        }
        this.drawingData = new byte[msodata.length];
        System.arraycopy(msodata, 0, this.drawingData, 0, msodata.length);
    }

    final void addDrawing(DrawingGroupObject d) {
        this.drawings.add(d);
        this.maxObjectId = Math.max(this.maxObjectId, d.getObjectId());
        this.maxShapeId = Math.max(this.maxShapeId, d.getShapeId());
    }

    public void add(Chart c) {
        this.numCharts++;
    }

    public void add(DrawingGroupObject d) {
        boolean z = false;
        if (this.origin == Origin.READ) {
            this.origin = Origin.READ_WRITE;
            BStoreContainer bsc = getBStoreContainer();
            this.drawingGroupId = (this.escherData.getChildren()[0].getCluster(1).drawingGroupId - this.numBlips) - 1;
            this.numBlips = bsc == null ? 0 : bsc.getNumBlips();
            if (bsc != null) {
                if (this.numBlips == bsc.getNumBlips()) {
                    z = true;
                }
                Assert.verify(z);
            }
        }
        if (d instanceof Drawing) {
            Drawing drawing = (Drawing) d;
            Drawing refImage = (Drawing) this.imageFiles.get(d.getImageFilePath());
            if (refImage != null) {
                refImage.setReferenceCount(refImage.getReferenceCount() + 1);
                drawing.setDrawingGroup(this);
                drawing.setObjectId(refImage.getObjectId(), refImage.getBlipId(), refImage.getShapeId());
            } else {
                this.maxObjectId++;
                this.maxShapeId++;
                this.drawings.add(drawing);
                drawing.setDrawingGroup(this);
                drawing.setObjectId(this.maxObjectId, this.numBlips + 1, this.maxShapeId);
                this.numBlips++;
                this.imageFiles.put(drawing.getImageFilePath(), drawing);
            }
            return;
        }
        this.maxObjectId++;
        this.maxShapeId++;
        d.setDrawingGroup(this);
        d.setObjectId(this.maxObjectId, this.numBlips + 1, this.maxShapeId);
        if (this.drawings.size() > this.maxObjectId) {
            logger.warn("drawings length " + this.drawings.size() + " exceeds the max object id " + this.maxObjectId);
        }
    }

    private void initialize() {
        boolean z;
        boolean z2 = false;
        EscherRecordData er = new EscherRecordData(this, 0);
        Assert.verify(er.isContainer());
        this.escherData = new EscherContainer(er);
        if (this.escherData.getLength() != this.drawingData.length) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
        if (this.escherData.getType() == EscherRecordType.DGG_CONTAINER) {
            z2 = true;
        }
        Assert.verify(z2);
        this.initialized = true;
    }

    private BStoreContainer getBStoreContainer() {
        if (this.bstoreContainer == null) {
            if (!this.initialized) {
                initialize();
            }
            EscherRecord[] children = this.escherData.getChildren();
            if (children.length > 1 && children[1].getType() == EscherRecordType.BSTORE_CONTAINER) {
                this.bstoreContainer = (BStoreContainer) children[1];
            }
        }
        return this.bstoreContainer;
    }

    public byte[] getData() {
        return this.drawingData;
    }

    public void write(File outputFile) throws IOException {
        DggContainer dggContainer;
        Dgg dgg;
        BStoreContainer bstoreCont;
        Iterator i;
        if (this.origin != Origin.WRITE) {
            if (this.origin == Origin.READ_WRITE) {
                dggContainer = new DggContainer();
                dgg = new Dgg((this.numBlips + this.numCharts) + 1, this.numBlips);
                dgg.addCluster(1, 0);
                dgg.addCluster((this.drawingGroupId + this.numBlips) + 1, 0);
                dggContainer.add(dgg);
                bstoreCont = new BStoreContainer();
                bstoreCont.setNumBlips(this.numBlips);
                BStoreContainer readBStoreContainer = getBStoreContainer();
                if (readBStoreContainer != null) {
                    EscherRecord[] children = readBStoreContainer.getChildren();
                    for (EscherRecord escherRecord : children) {
                        bstoreCont.add((BlipStoreEntry) escherRecord);
                    }
                }
                i = this.drawings.iterator();
                while (i.hasNext()) {
                    DrawingGroupObject dgo = (DrawingGroupObject) i.next();
                    if (dgo instanceof Drawing) {
                        Drawing d = (Drawing) dgo;
                        if (d.getOrigin() == Origin.WRITE) {
                            bstoreCont.add(new BlipStoreEntry(d));
                        }
                    }
                }
                dggContainer.add(bstoreCont);
                EscherRecord opt = new Opt();
                opt.addProperty(191, false, false, 524296);
                opt.addProperty(385, false, false, 134217737);
                opt.addProperty(448, false, false, 134217792);
                dggContainer.add(opt);
                dggContainer.add(new SplitMenuColors());
                this.drawingData = dggContainer.getData();
            }
        } else {
            dggContainer = new DggContainer();
            dgg = new Dgg((this.numBlips + this.numCharts) + 1, this.numBlips);
            dgg.addCluster(1, 0);
            dgg.addCluster(this.numBlips + 1, 0);
            dggContainer.add(dgg);
            int drawingsAdded = 0;
            bstoreCont = new BStoreContainer();
            i = this.drawings.iterator();
            while (i.hasNext()) {
                Drawing o = i.next();
                if (o instanceof Drawing) {
                    bstoreCont.add(new BlipStoreEntry(o));
                    drawingsAdded++;
                }
            }
            if (drawingsAdded > 0) {
                bstoreCont.setNumBlips(drawingsAdded);
                dggContainer.add(bstoreCont);
            }
            dggContainer.add(new Opt());
            dggContainer.add(new SplitMenuColors());
            this.drawingData = dggContainer.getData();
        }
        outputFile.write(new MsoDrawingGroupRecord(this.drawingData));
    }

    byte[] getImageData(int blipId) {
        boolean z;
        boolean z2 = false;
        this.numBlips = getBStoreContainer().getNumBlips();
        if (blipId > this.numBlips) {
            z = false;
        } else {
            z = true;
        }
        Assert.verify(z);
        if (this.origin == Origin.READ || this.origin == Origin.READ_WRITE) {
            z2 = true;
        }
        Assert.verify(z2);
        return getBStoreContainer().getChildren()[blipId - 1].getImageData();
    }

    public void setDrawingsOmitted(MsoDrawingRecord mso, ObjRecord obj) {
        this.drawingsOmitted = true;
        if (obj != null) {
            this.maxObjectId = Math.max(this.maxObjectId, obj.getObjectId());
        }
    }

    public boolean hasDrawingsOmitted() {
        return this.drawingsOmitted;
    }

    public void updateData(DrawingGroup dg) {
        this.drawingsOmitted = dg.drawingsOmitted;
        this.maxObjectId = dg.maxObjectId;
        this.maxShapeId = dg.maxShapeId;
    }
}
