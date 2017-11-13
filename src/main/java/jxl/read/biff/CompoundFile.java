package jxl.read.biff;

import java.util.ArrayList;
import java.util.Iterator;

import jxl.WorkbookSettings;
import jxl.biff.BaseCompoundFile;
import jxl.biff.BaseCompoundFile.PropertyStorage;
import jxl.biff.IntegerHelper;
import jxl.common.Logger;

public final class CompoundFile extends BaseCompoundFile {
    private static Logger logger = Logger.getLogger(CompoundFile.class);
    private int[] bigBlockChain;
    private int[] bigBlockDepotBlocks;
    private byte[] data;
    private int extensionBlock;
    private int numBigBlockDepotBlocks;
    private int numExtensionBlocks;
    private ArrayList propertySets;
    private byte[] rootEntry;
    private PropertyStorage rootEntryPropertyStorage;
    private int rootStartBlock;
    private int sbdStartBlock;
    private WorkbookSettings settings;
    private int[] smallBlockChain;

    public CompoundFile(byte[] d, WorkbookSettings ws) throws BiffException {
        this.data = d;
        this.settings = ws;
        int i = 0;
        while (i < IDENTIFIER.length) {
            if (this.data[i] == IDENTIFIER[i]) {
                i++;
            } else {
                throw new BiffException(BiffException.unrecognizedOLEFile);
            }
        }
        this.propertySets = new ArrayList();
        this.numBigBlockDepotBlocks = IntegerHelper.getInt(this.data[44], this.data[45], this.data[46], this.data[47]);
        this.sbdStartBlock = IntegerHelper.getInt(this.data[60], this.data[61], this.data[62], this.data[63]);
        this.rootStartBlock = IntegerHelper.getInt(this.data[48], this.data[49], this.data[50], this.data[51]);
        this.extensionBlock = IntegerHelper.getInt(this.data[68], this.data[69], this.data[70], this.data[71]);
        this.numExtensionBlocks = IntegerHelper.getInt(this.data[72], this.data[73], this.data[74], this.data[75]);
        this.bigBlockDepotBlocks = new int[this.numBigBlockDepotBlocks];
        int pos = 76;
        int bbdBlocks = this.numBigBlockDepotBlocks;
        if (this.numExtensionBlocks != 0) {
            bbdBlocks = 109;
        }
        for (i = 0; i < bbdBlocks; i++) {
            this.bigBlockDepotBlocks[i] = IntegerHelper.getInt(d[pos], d[pos + 1], d[pos + 2], d[pos + 3]);
            pos += 4;
        }
        for (int j = 0; j < this.numExtensionBlocks; j++) {
            pos = (this.extensionBlock + 1) * 512;
            int blocksToRead = Math.min(this.numBigBlockDepotBlocks - bbdBlocks, 127);
            for (i = bbdBlocks; i < bbdBlocks + blocksToRead; i++) {
                this.bigBlockDepotBlocks[i] = IntegerHelper.getInt(d[pos], d[pos + 1], d[pos + 2], d[pos + 3]);
                pos += 4;
            }
            bbdBlocks += blocksToRead;
            if (bbdBlocks < this.numBigBlockDepotBlocks) {
                this.extensionBlock = IntegerHelper.getInt(d[pos], d[pos + 1], d[pos + 2], d[pos + 3]);
            }
        }
        readBigBlockDepot();
        readSmallBlockDepot();
        this.rootEntry = readData(this.rootStartBlock);
        readPropertySets();
    }

    private void readBigBlockDepot() {
        int index = 0;
        this.bigBlockChain = new int[((this.numBigBlockDepotBlocks * 512) / 4)];
        for (int i = 0; i < this.numBigBlockDepotBlocks; i++) {
            int pos = (this.bigBlockDepotBlocks[i] + 1) * 512;
            for (int j = 0; j < 128; j++) {
                this.bigBlockChain[index] = IntegerHelper.getInt(this.data[pos], this.data[pos + 1], this.data[pos + 2], this.data[pos + 3]);
                pos += 4;
                index++;
            }
        }
    }

    private void readSmallBlockDepot() throws BiffException {
        int index = 0;
        int sbdBlock = this.sbdStartBlock;
        this.smallBlockChain = new int[0];
        if (sbdBlock != -1) {
            int blockCount = 0;
            while (blockCount <= this.bigBlockChain.length && sbdBlock != -2) {
                int[] oldChain = this.smallBlockChain;
                this.smallBlockChain = new int[(this.smallBlockChain.length + 128)];
                System.arraycopy(oldChain, 0, this.smallBlockChain, 0, oldChain.length);
                int pos = (sbdBlock + 1) * 512;
                for (int j = 0; j < 128; j++) {
                    this.smallBlockChain[index] = IntegerHelper.getInt(this.data[pos], this.data[pos + 1], this.data[pos + 2], this.data[pos + 3]);
                    pos += 4;
                    index++;
                }
                sbdBlock = this.bigBlockChain[sbdBlock];
                blockCount++;
            }
            if (blockCount > this.bigBlockChain.length) {
                throw new BiffException(BiffException.corruptFileFormat);
            }
            return;
        }
        logger.warn("invalid small block depot number");
    }

    private void readPropertySets() {
        for (int offset = 0; offset < this.rootEntry.length; offset += 128) {
            byte[] d = new byte[128];
            System.arraycopy(this.rootEntry, offset, d, 0, d.length);
            PropertyStorage ps = new PropertyStorage(d);
            if (ps.name == null || ps.name.length() == 0) {
                if (ps.type == 5) {
                    ps.name = "Root Entry";
                    logger.warn("Property storage name for " + ps.type + " is empty - setting to " + "Root Entry");
                } else if (ps.size != 0) {
                    logger.warn("Property storage type " + ps.type + " is non-empty and has no associated name");
                }
            }
            this.propertySets.add(ps);
            if (ps.name.equalsIgnoreCase("Root Entry")) {
                this.rootEntryPropertyStorage = ps;
            }
        }
        if (this.rootEntryPropertyStorage == null) {
            this.rootEntryPropertyStorage = (PropertyStorage) this.propertySets.get(0);
        }
    }

    public byte[] getStream(String streamName) throws BiffException {
        PropertyStorage ps = findPropertyStorage(streamName, this.rootEntryPropertyStorage);
        if (ps == null) {
            ps = getPropertyStorage(streamName);
        }
        if (ps.size < 4096 && !streamName.equalsIgnoreCase("Root Entry")) {
            return getSmallBlockStream(ps);
        }
        return getBigBlockStream(ps);
    }

    public byte[] getStream(int psIndex) throws BiffException {
        PropertyStorage ps = getPropertyStorage(psIndex);
        if (ps.size < 4096 && !ps.name.equalsIgnoreCase("Root Entry")) {
            return getSmallBlockStream(ps);
        }
        return getBigBlockStream(ps);
    }

    public PropertyStorage findPropertyStorage(String name) {
        return findPropertyStorage(name, this.rootEntryPropertyStorage);
    }

    private PropertyStorage findPropertyStorage(String name, PropertyStorage base) {
        if (base.child == -1) {
            return null;
        }
        PropertyStorage child = getPropertyStorage(base.child);
        if (child.name.equalsIgnoreCase(name)) {
            return child;
        }
        PropertyStorage prev = child;
        while (prev.previous != -1) {
            prev = getPropertyStorage(prev.previous);
            if (prev.name.equalsIgnoreCase(name)) {
                return prev;
            }
        }
        PropertyStorage next = child;
        while (next.next != -1) {
            next = getPropertyStorage(next.next);
            if (next.name.equalsIgnoreCase(name)) {
                return next;
            }
        }
        return findPropertyStorage(name, child);
    }

    private PropertyStorage getPropertyStorage(String name) throws BiffException {
        Iterator i = this.propertySets.iterator();
        boolean found = false;
        boolean multiple = false;
        PropertyStorage propertyStorage = null;
        while (i.hasNext()) {
            PropertyStorage ps2 = (PropertyStorage) i.next();
            if (ps2.name.equalsIgnoreCase(name)) {
                if (!found) {
                    multiple = false;
                } else {
                    multiple = true;
                }
                found = true;
                propertyStorage = ps2;
            }
        }
        if (multiple) {
            logger.warn("found multiple copies of property set " + name);
        }
        if (found) {
            return propertyStorage;
        }
        throw new BiffException(BiffException.streamNotFound);
    }

    private PropertyStorage getPropertyStorage(int index) {
        return (PropertyStorage) this.propertySets.get(index);
    }

    private byte[] getBigBlockStream(PropertyStorage ps) {
        int numBlocks = ps.size / 512;
        if (ps.size % 512 != 0) {
            numBlocks++;
        }
        byte[] streamData = new byte[(numBlocks * 512)];
        int block = ps.startBlock;
        int count = 0;
        while (block != -2 && count < numBlocks) {
            System.arraycopy(this.data, (block + 1) * 512, streamData, count * 512, 512);
            count++;
            block = this.bigBlockChain[block];
        }
        if (block != -2 && count == numBlocks) {
            logger.warn("Property storage size inconsistent with block chain.");
        }
        return streamData;
    }

    private byte[] getSmallBlockStream(PropertyStorage ps) throws BiffException {
        byte[] rootdata = readData(this.rootEntryPropertyStorage.startBlock);
        byte[] sbdata = new byte[0];
        int block = ps.startBlock;
        int blockCount = 0;
        while (blockCount <= this.smallBlockChain.length && block != -2) {
            byte[] olddata = sbdata;
            sbdata = new byte[(olddata.length + 64)];
            System.arraycopy(olddata, 0, sbdata, 0, olddata.length);
            System.arraycopy(rootdata, block * 64, sbdata, olddata.length, 64);
            block = this.smallBlockChain[block];
            if (block == -1) {
                logger.warn("Incorrect terminator for small block stream " + ps.name);
                block = -2;
            }
            blockCount++;
        }
        if (blockCount <= this.smallBlockChain.length) {
            return sbdata;
        }
        throw new BiffException(BiffException.corruptFileFormat);
    }

    private byte[] readData(int bl) throws BiffException {
        int block = bl;
        byte[] entry = new byte[0];
        int blockCount = 0;
        while (blockCount <= this.bigBlockChain.length && block != -2) {
            byte[] oldEntry = entry;
            entry = new byte[(oldEntry.length + 512)];
            System.arraycopy(oldEntry, 0, entry, 0, oldEntry.length);
            System.arraycopy(this.data, (block + 1) * 512, entry, oldEntry.length, 512);
            if (this.bigBlockChain[block] != block) {
                block = this.bigBlockChain[block];
                blockCount++;
            } else {
                throw new BiffException(BiffException.corruptFileFormat);
            }
        }
        if (blockCount <= this.bigBlockChain.length) {
            return entry;
        }
        throw new BiffException(BiffException.corruptFileFormat);
    }

    public int getNumberOfPropertySets() {
        return this.propertySets.size();
    }

    public PropertyStorage getPropertySet(int index) {
        return getPropertyStorage(index);
    }
}
