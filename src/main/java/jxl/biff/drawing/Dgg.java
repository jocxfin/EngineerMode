package jxl.biff.drawing;

import java.util.ArrayList;

import jxl.biff.IntegerHelper;
import jxl.common.Logger;

class Dgg extends EscherAtom {
    private static Logger logger = Logger.getLogger(Dgg.class);
    private ArrayList clusters = new ArrayList();
    private byte[] data;
    private int drawingsSaved;
    private int maxShapeId;
    private int numClusters;
    private int shapesSaved;

    static final class Cluster {
        int drawingGroupId;
        int shapeIdsUsed;

        Cluster(int dgId, int sids) {
            this.drawingGroupId = dgId;
            this.shapeIdsUsed = sids;
        }
    }

    public Dgg(EscherRecordData erd) {
        super(erd);
        byte[] bytes = getBytes();
        this.maxShapeId = IntegerHelper.getInt(bytes[0], bytes[1], bytes[2], bytes[3]);
        this.numClusters = IntegerHelper.getInt(bytes[4], bytes[5], bytes[6], bytes[7]);
        this.shapesSaved = IntegerHelper.getInt(bytes[8], bytes[9], bytes[10], bytes[11]);
        this.drawingsSaved = IntegerHelper.getInt(bytes[12], bytes[13], bytes[14], bytes[15]);
        int pos = 16;
        for (int i = 0; i < this.numClusters; i++) {
            this.clusters.add(new Cluster(IntegerHelper.getInt(bytes[pos], bytes[pos + 1]), IntegerHelper.getInt(bytes[pos + 2], bytes[pos + 3])));
            pos += 4;
        }
    }

    public Dgg(int numShapes, int numDrawings) {
        super(EscherRecordType.DGG);
        this.shapesSaved = numShapes;
        this.drawingsSaved = numDrawings;
    }

    void addCluster(int dgid, int sids) {
        this.clusters.add(new Cluster(dgid, sids));
    }

    byte[] getData() {
        this.numClusters = this.clusters.size();
        this.data = new byte[((this.numClusters * 4) + 16)];
        IntegerHelper.getFourBytes(this.shapesSaved + 1024, this.data, 0);
        IntegerHelper.getFourBytes(this.numClusters, this.data, 4);
        IntegerHelper.getFourBytes(this.shapesSaved, this.data, 8);
        IntegerHelper.getFourBytes(1, this.data, 12);
        int pos = 16;
        for (int i = 0; i < this.numClusters; i++) {
            Cluster c = (Cluster) this.clusters.get(i);
            IntegerHelper.getTwoBytes(c.drawingGroupId, this.data, pos);
            IntegerHelper.getTwoBytes(c.shapeIdsUsed, this.data, pos + 2);
            pos += 4;
        }
        return setHeaderData(this.data);
    }

    Cluster getCluster(int i) {
        return (Cluster) this.clusters.get(i);
    }
}
