package jxl.biff.drawing;

import java.util.ArrayList;

import jxl.common.Assert;
import jxl.common.Logger;

public class DrawingData implements EscherStream {
    private static Logger logger = Logger.getLogger(DrawingData.class);
    private byte[] drawingData = null;
    private boolean initialized = false;
    private int numDrawings = 0;
    private EscherRecord[] spContainers;

    private void initialize() {
        int i;
        boolean z = false;
        EscherRecordData er = new EscherRecordData(this, 0);
        Assert.verify(er.isContainer());
        EscherContainer dgContainer = new EscherContainer(er);
        EscherRecord[] children = dgContainer.getChildren();
        children = dgContainer.getChildren();
        EscherContainer spgrContainer = null;
        for (i = 0; i < children.length && spgrContainer == null; i++) {
            EscherRecord child = children[i];
            if (child.getType() == EscherRecordType.SPGR_CONTAINER) {
                spgrContainer = (EscherContainer) child;
            }
        }
        if (spgrContainer != null) {
            z = true;
        }
        Assert.verify(z);
        EscherRecord[] spgrChildren = spgrContainer.getChildren();
        boolean nestedContainers = false;
        for (i = 0; i < spgrChildren.length && !nestedContainers; i++) {
            if (spgrChildren[i].getType() == EscherRecordType.SPGR_CONTAINER) {
                nestedContainers = true;
            }
        }
        if (nestedContainers) {
            ArrayList sps = new ArrayList();
            getSpContainers(spgrContainer, sps);
            this.spContainers = new EscherRecord[sps.size()];
            this.spContainers = (EscherRecord[]) sps.toArray(this.spContainers);
        } else {
            this.spContainers = spgrChildren;
        }
        this.initialized = true;
    }

    private void getSpContainers(EscherContainer spgrContainer, ArrayList sps) {
        EscherRecord[] spgrChildren = spgrContainer.getChildren();
        for (int i = 0; i < spgrChildren.length; i++) {
            if (spgrChildren[i].getType() == EscherRecordType.SP_CONTAINER) {
                sps.add(spgrChildren[i]);
            } else if (spgrChildren[i].getType() != EscherRecordType.SPGR_CONTAINER) {
                logger.warn("Spgr Containers contains a record other than Sp/Spgr containers");
            } else {
                getSpContainers((EscherContainer) spgrChildren[i], sps);
            }
        }
    }

    public void addData(byte[] data) {
        addRawData(data);
        this.numDrawings++;
    }

    public void addRawData(byte[] data) {
        if (this.drawingData != null) {
            byte[] newArray = new byte[(this.drawingData.length + data.length)];
            System.arraycopy(this.drawingData, 0, newArray, 0, this.drawingData.length);
            System.arraycopy(data, 0, newArray, this.drawingData.length, data.length);
            this.drawingData = newArray;
            this.initialized = false;
            return;
        }
        this.drawingData = data;
    }

    final int getNumDrawings() {
        return this.numDrawings;
    }

    EscherContainer getSpContainer(int drawingNum) {
        boolean z = false;
        if (!this.initialized) {
            initialize();
        }
        if (drawingNum + 1 < this.spContainers.length) {
            EscherContainer spContainer = this.spContainers[drawingNum + 1];
            if (spContainer != null) {
                z = true;
            }
            Assert.verify(z);
            return spContainer;
        }
        throw new DrawingDataException();
    }

    public byte[] getData() {
        return this.drawingData;
    }
}
