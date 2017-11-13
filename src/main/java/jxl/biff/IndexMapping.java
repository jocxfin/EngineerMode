package jxl.biff;

import jxl.common.Logger;

public final class IndexMapping {
    private static Logger logger = Logger.getLogger(IndexMapping.class);
    private int[] newIndices;

    public IndexMapping(int size) {
        this.newIndices = new int[size];
    }

    public void setMapping(int oldIndex, int newIndex) {
        this.newIndices[oldIndex] = newIndex;
    }

    public int getNewIndex(int oldIndex) {
        return this.newIndices[oldIndex];
    }
}
