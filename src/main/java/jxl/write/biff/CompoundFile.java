package jxl.write.biff;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import jxl.biff.BaseCompoundFile;
import jxl.biff.BaseCompoundFile.PropertyStorage;
import jxl.biff.IntegerHelper;
import jxl.common.Assert;
import jxl.common.Logger;
import jxl.read.biff.BiffException;

final class CompoundFile extends BaseCompoundFile {
    private static Logger logger = Logger.getLogger(CompoundFile.class);
    private int additionalPropertyBlocks;
    private ArrayList additionalPropertySets;
    private int bbdPos;
    private int bbdStartBlock;
    private byte[] bigBlockDepot;
    private ExcelDataOutput excelData;
    private int excelDataBlocks;
    private int excelDataStartBlock;
    private int extensionBlock;
    private int numBigBlockDepotBlocks;
    private int numExtensionBlocks;
    private int numPropertySets;
    private int numRootEntryBlocks = 1;
    private int numSmallBlockDepotBlocks;
    private int numSmallBlockDepotChainBlocks;
    private int numSmallBlocks;
    private OutputStream out;
    private int requiredSize;
    private int rootStartBlock;
    private int sbdStartBlock;
    private int sbdStartBlockChain;
    private int size;
    private HashMap standardPropertySets;

    private static final class ReadPropertyStorage {
        byte[] data;
        int number;
        PropertyStorage propertyStorage;

        ReadPropertyStorage(PropertyStorage ps, byte[] d, int n) {
            this.propertyStorage = ps;
            this.data = d;
            this.number = n;
        }
    }

    private void writePropertySets() throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: jxl.write.biff.CompoundFile.writePropertySets():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
	at jadx.core.dex.nodes.MethodNode.addJump(MethodNode.java:370)
	at jadx.core.dex.nodes.MethodNode.initJumps(MethodNode.java:360)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:106)
	... 5 more
*/
        /*
        r0 = this;
        r0 = r23;
        r19 = r0;
        r0 = r19;
        r0 = r0 * 512;
        r19 = r0;
        r0 = r19;
        r14 = new byte[r0];
        r9 = 0;
        r0 = r23;
        r19 = r0;
        if (r19 != 0) goto L_0x016c;
    L_0x0017:
        r6 = 0;
        r13 = 0;
        r11 = 0;
        r17 = 0;
        r0 = r23;
        r19 = r0;
        if (r19 != 0) goto L_0x01d7;
    L_0x0023:
        r15 = new jxl.biff.BaseCompoundFile$PropertyStorage;
        r19 = "Root Entry";
        r0 = r23;
        r1 = r19;
        r15.<init>(r1);
        r19 = 5;
        r0 = r19;
        r0 = r23;
        r19 = r0;
        r0 = r19;
        r0 = r17;
        r19 = -1;
        r0 = r19;
        r19 = -1;
        r0 = r19;
        r19 = 0;
        r0 = r19;
        r6 = 1;
        r0 = r23;
        r19 = r0;
        if (r19 != 0) goto L_0x0279;
    L_0x0058:
        r19 = r0;
        r20 = 0;
        r21 = 0;
        r22 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r0 = r19;
        r1 = r20;
        r2 = r21;
        r3 = r22;
        java.lang.System.arraycopy(r0, r1, r14, r2, r3);
        r12 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r15 = new jxl.biff.BaseCompoundFile$PropertyStorage;
        r19 = "Workbook";
        r0 = r23;
        r1 = r19;
        r15.<init>(r1);
        r19 = 2;
        r0 = r19;
        r0 = r23;
        r19 = r0;
        r0 = r19;
        r0 = r23;
        r19 = r0;
        r0 = r19;
        r13 = 3;
        r11 = -1;
        r0 = r23;
        r19 = r0;
        if (r19 != 0) goto L_0x0293;
    L_0x0099:
        r19 = -1;
        r0 = r19;
        r19 = r0;
        r20 = 0;
        r21 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r0 = r19;
        r1 = r20;
        r2 = r21;
        java.lang.System.arraycopy(r0, r1, r14, r12, r2);
        r12 = r12 + 128;
        r15 = new jxl.biff.BaseCompoundFile$PropertyStorage;
        r19 = "\u0005SummaryInformation";
        r0 = r23;
        r1 = r19;
        r15.<init>(r1);
        r19 = 2;
        r0 = r19;
        r0 = r23;
        r19 = r0;
        r0 = r23;
        r20 = r0;
        r19 = r19 + r20;
        r0 = r19;
        r19 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = r19;
        r13 = 1;
        r11 = 3;
        r0 = r23;
        r19 = r0;
        if (r19 != 0) goto L_0x02e1;
    L_0x00e0:
        r19 = -1;
        r0 = r19;
        r19 = r0;
        r20 = 0;
        r21 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r0 = r19;
        r1 = r20;
        r2 = r21;
        java.lang.System.arraycopy(r0, r1, r14, r12, r2);
        r12 = r12 + 128;
        r15 = new jxl.biff.BaseCompoundFile$PropertyStorage;
        r19 = "\u0005DocumentSummaryInformation";
        r0 = r23;
        r1 = r19;
        r15.<init>(r1);
        r19 = 2;
        r0 = r19;
        r0 = r23;
        r19 = r0;
        r0 = r23;
        r20 = r0;
        r19 = r19 + r20;
        r19 = r19 + 8;
        r0 = r19;
        r19 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = r19;
        r19 = -1;
        r0 = r19;
        r19 = -1;
        r0 = r19;
        r19 = -1;
        r0 = r19;
        r19 = r0;
        r20 = 0;
        r21 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r0 = r19;
        r1 = r20;
        r2 = r21;
        java.lang.System.arraycopy(r0, r1, r14, r12, r2);
        r12 = r12 + 128;
        r0 = r23;
        r19 = r0;
        if (r19 == 0) goto L_0x0331;
    L_0x0146:
        r0 = r23;
        r19 = r0;
        r0 = r23;
        r20 = r0;
        r19 = r19 + r20;
        r4 = r19 + 16;
        r18 = 0;
        r0 = r23;
        r19 = r0;
        r19 = r8.hasNext();
        if (r19 != 0) goto L_0x033a;
    L_0x0163:
        r0 = r23;
        r19 = r0;
        r0 = r19;
    L_0x016c:
        r0 = r23;
        r19 = r0;
        r0 = r19;
        r9 = new int[r0];
        r7 = 0;
    L_0x0176:
        r19 = STANDARD_PROPERTY_SETS;
        r0 = r19;
        r0 = r0.length;
        r19 = r0;
        r0 = r19;
        if (r7 < r0) goto L_0x01a3;
    L_0x0181:
        r19 = STANDARD_PROPERTY_SETS;
        r0 = r19;
        r10 = r0.length;
        r0 = r23;
        r19 = r0;
    L_0x018d:
        r19 = r8.hasNext();
        if (r19 == 0) goto L_0x0017;
    L_0x0193:
        r16 = r8.next();
        r16 = (jxl.write.biff.CompoundFile.ReadPropertyStorage) r16;
        r0 = r16;
        r19 = r0;
        r9[r19] = r10;
        r10 = r10 + 1;
        goto L_0x018d;
    L_0x01a3:
        r0 = r23;
        r19 = r0;
        r20 = STANDARD_PROPERTY_SETS;
        r20 = r20[r7];
        r16 = (jxl.write.biff.CompoundFile.ReadPropertyStorage) r16;
        if (r16 != 0) goto L_0x01cf;
    L_0x01b2:
        r19 = logger;
        r20 = new java.lang.StringBuilder;
        r20.<init>();
        r21 = "Standard property set ";
        r21 = STANDARD_PROPERTY_SETS;
        r21 = r21[r7];
        r21 = " not present in source file";
    L_0x01cc:
        r7 = r7 + 1;
        goto L_0x0176;
    L_0x01cf:
        r0 = r16;
        r19 = r0;
        r9[r19] = r7;
        goto L_0x01cc;
    L_0x01d7:
        r0 = r23;
        r19 = r0;
        r0 = r23;
        r1 = r19;
        r19 = r0.getBigBlocksRequired(r1);
        r0 = r19;
        r0 = r0 * 512;
        r19 = r0;
        r17 = r19 + 0;
        r19 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = r23;
        r1 = r19;
        r19 = r0.getBigBlocksRequired(r1);
        r0 = r19;
        r0 = r0 * 512;
        r19 = r0;
        r17 = r17 + r19;
        r19 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = r23;
        r1 = r19;
        r19 = r0.getBigBlocksRequired(r1);
        r0 = r19;
        r0 = r0 * 512;
        r19 = r0;
        r17 = r17 + r19;
        r0 = r23;
        r19 = r0;
    L_0x0217:
        r19 = r8.hasNext();
        if (r19 == 0) goto L_0x0023;
    L_0x021d:
        r16 = r8.next();
        r16 = (jxl.write.biff.CompoundFile.ReadPropertyStorage) r16;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r20 = 1;
        r0 = r19;
        r1 = r20;
        if (r0 == r1) goto L_0x0217;
    L_0x0235:
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r20 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = r19;
        r1 = r20;
        if (r0 >= r1) goto L_0x025e;
    L_0x0247:
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r0 = r23;
        r1 = r19;
        r19 = r0.getSmallBlocksRequired(r1);
        r19 = r19 * 64;
        r17 = r17 + r19;
        goto L_0x0217;
    L_0x025e:
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r0 = r23;
        r1 = r19;
        r19 = r0.getBigBlocksRequired(r1);
        r0 = r19;
        r0 = r0 * 512;
        r19 = r0;
        r17 = r17 + r19;
        goto L_0x0217;
    L_0x0279:
        r0 = r23;
        r19 = r0;
        r20 = "Root Entry";
        r16 = (jxl.write.biff.CompoundFile.ReadPropertyStorage) r16;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r6 = r9[r19];
        goto L_0x0056;
    L_0x0293:
        r0 = r23;
        r19 = r0;
        r20 = "Workbook";
        r16 = (jxl.write.biff.CompoundFile.ReadPropertyStorage) r16;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r20 = -1;
        r0 = r19;
        r1 = r20;
        if (r0 != r1) goto L_0x02c7;
        r13 = -1;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r20 = -1;
        r0 = r19;
        r1 = r20;
        if (r0 != r1) goto L_0x02d4;
        r11 = -1;
        goto L_0x0097;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r13 = r9[r19];
        goto L_0x02b2;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r11 = r9[r19];
        goto L_0x02c5;
    L_0x02e1:
        r0 = r23;
        r19 = r0;
        r20 = "\u0005SummaryInformation";
        r16 = (jxl.write.biff.CompoundFile.ReadPropertyStorage) r16;
        if (r16 == 0) goto L_0x00de;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r20 = -1;
        r0 = r19;
        r1 = r20;
        if (r0 != r1) goto L_0x0317;
        r13 = -1;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r20 = -1;
        r0 = r19;
        r1 = r20;
        if (r0 != r1) goto L_0x0324;
        r11 = -1;
        goto L_0x00de;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r13 = r9[r19];
        goto L_0x0302;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r11 = r9[r19];
        goto L_0x0315;
    L_0x0331:
        r0 = r23;
        r19 = r0;
        r0 = r19;
    L_0x033a:
        r16 = r8.next();
        r16 = (jxl.write.biff.CompoundFile.ReadPropertyStorage) r16;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r0 = r0.length;
        r19 = r0;
        r20 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = r19;
        r1 = r20;
        if (r0 > r1) goto L_0x03f8;
        r5 = r18;
        r15 = new jxl.biff.BaseCompoundFile$PropertyStorage;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r0 = r23;
        r1 = r19;
        r15.<init>(r1);
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r0 = r19;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r0 = r19;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r20 = -1;
        r0 = r19;
        r1 = r20;
        if (r0 != r1) goto L_0x03fb;
        r13 = -1;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r20 = -1;
        r0 = r19;
        r1 = r20;
        if (r0 != r1) goto L_0x0409;
        r11 = -1;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r20 = -1;
        r0 = r19;
        r1 = r20;
        if (r0 != r1) goto L_0x0416;
        r6 = -1;
        r19 = r0;
        r20 = 0;
        r21 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r0 = r19;
        r1 = r20;
        r2 = r21;
        java.lang.System.arraycopy(r0, r1, r14, r12, r2);
        r12 = r12 + 128;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r0 = r0.length;
        r19 = r0;
        r20 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = r19;
        r1 = r20;
        if (r0 > r1) goto L_0x0423;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r0 = r0.length;
        r19 = r0;
        r0 = r23;
        r1 = r19;
        r19 = r0.getSmallBlocksRequired(r1);
        r18 = r18 + r19;
        goto L_0x015d;
        r5 = r4;
        goto L_0x0354;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r13 = r9[r19];
        goto L_0x0395;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r11 = r9[r19];
        goto L_0x03a8;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r19 = r0;
        r6 = r9[r19];
        goto L_0x03bb;
        r0 = r16;
        r19 = r0;
        r0 = r19;
        r0 = r0.length;
        r19 = r0;
        r0 = r23;
        r1 = r19;
        r19 = r0.getBigBlocksRequired(r1);
        r4 = r4 + r19;
        goto L_0x015d;
        */
        throw new UnsupportedOperationException("Method not decompiled: jxl.write.biff.CompoundFile.writePropertySets():void");
    }

    public CompoundFile(ExcelDataOutput data, int l, OutputStream os, jxl.read.biff.CompoundFile rcf) throws CopyAdditionalPropertySetsException, IOException {
        this.size = l;
        this.excelData = data;
        readAdditionalPropertySets(rcf);
        this.numPropertySets = (this.additionalPropertySets == null ? 0 : this.additionalPropertySets.size()) + 4;
        if (this.additionalPropertySets != null) {
            this.numSmallBlockDepotChainBlocks = getBigBlocksRequired(this.numSmallBlocks * 4);
            this.numSmallBlockDepotBlocks = getBigBlocksRequired(this.numSmallBlocks * 64);
            this.numRootEntryBlocks += getBigBlocksRequired(this.additionalPropertySets.size() * 128);
        }
        int blocks = getBigBlocksRequired(l);
        if (l >= 4096) {
            this.requiredSize = blocks * 512;
        } else {
            this.requiredSize = 4096;
        }
        this.out = os;
        this.excelDataBlocks = this.requiredSize / 512;
        this.numBigBlockDepotBlocks = 1;
        int startTotalBlocks = (((((this.excelDataBlocks + 8) + 8) + this.additionalPropertyBlocks) + this.numSmallBlockDepotBlocks) + this.numSmallBlockDepotChainBlocks) + this.numRootEntryBlocks;
        this.numBigBlockDepotBlocks = (int) Math.ceil(((double) (startTotalBlocks + this.numBigBlockDepotBlocks)) / 128.0d);
        this.numBigBlockDepotBlocks = (int) Math.ceil(((double) (startTotalBlocks + this.numBigBlockDepotBlocks)) / 128.0d);
        int totalBlocks = startTotalBlocks + this.numBigBlockDepotBlocks;
        if (this.numBigBlockDepotBlocks <= 108) {
            this.extensionBlock = -2;
            this.numExtensionBlocks = 0;
        } else {
            this.extensionBlock = 0;
            this.numExtensionBlocks = (int) Math.ceil(((double) ((this.numBigBlockDepotBlocks - 109) + 1)) / 127.0d);
            this.numBigBlockDepotBlocks = (int) Math.ceil(((double) ((this.numExtensionBlocks + startTotalBlocks) + this.numBigBlockDepotBlocks)) / 128.0d);
            totalBlocks = (this.numExtensionBlocks + startTotalBlocks) + this.numBigBlockDepotBlocks;
        }
        this.excelDataStartBlock = this.numExtensionBlocks;
        this.sbdStartBlock = -2;
        if (!(this.additionalPropertySets == null || this.numSmallBlockDepotBlocks == 0)) {
            this.sbdStartBlock = ((this.excelDataStartBlock + this.excelDataBlocks) + this.additionalPropertyBlocks) + 16;
        }
        this.sbdStartBlockChain = -2;
        if (this.sbdStartBlock != -2) {
            this.sbdStartBlockChain = this.sbdStartBlock + this.numSmallBlockDepotBlocks;
        }
        if (this.sbdStartBlockChain == -2) {
            this.bbdStartBlock = ((this.excelDataStartBlock + this.excelDataBlocks) + this.additionalPropertyBlocks) + 16;
        } else {
            this.bbdStartBlock = this.sbdStartBlockChain + this.numSmallBlockDepotChainBlocks;
        }
        this.rootStartBlock = this.bbdStartBlock + this.numBigBlockDepotBlocks;
        if (totalBlocks != this.rootStartBlock + this.numRootEntryBlocks) {
            logger.warn("Root start block and total blocks are inconsistent  generated file may be corrupt");
            logger.warn("RootStartBlock " + this.rootStartBlock + " totalBlocks " + totalBlocks);
        }
    }

    private void readAdditionalPropertySets(jxl.read.biff.CompoundFile readCompoundFile) throws CopyAdditionalPropertySetsException, IOException {
        if (readCompoundFile != null) {
            this.additionalPropertySets = new ArrayList();
            this.standardPropertySets = new HashMap();
            int blocksRequired = 0;
            int numPropertySets = readCompoundFile.getNumberOfPropertySets();
            for (int i = 0; i < numPropertySets; i++) {
                PropertyStorage ps = readCompoundFile.getPropertySet(i);
                boolean standard = false;
                if (ps.name.equalsIgnoreCase("Root Entry")) {
                    standard = true;
                    this.standardPropertySets.put("Root Entry", new ReadPropertyStorage(ps, null, i));
                }
                for (int j = 0; j < STANDARD_PROPERTY_SETS.length && !standard; j++) {
                    if (ps.name.equalsIgnoreCase(STANDARD_PROPERTY_SETS[j])) {
                        boolean z;
                        PropertyStorage ps2 = readCompoundFile.findPropertyStorage(ps.name);
                        if (ps2 == null) {
                            z = false;
                        } else {
                            z = true;
                        }
                        Assert.verify(z);
                        if (ps2 == ps) {
                            standard = true;
                            this.standardPropertySets.put(STANDARD_PROPERTY_SETS[j], new ReadPropertyStorage(ps, null, i));
                        }
                    }
                }
                if (!standard) {
                    byte[] data;
                    if (ps.size <= 0) {
                        data = new byte[0];
                    } else {
                        try {
                            data = readCompoundFile.getStream(i);
                        } catch (BiffException e) {
                            logger.error(e);
                            throw new CopyAdditionalPropertySetsException();
                        }
                    }
                    this.additionalPropertySets.add(new ReadPropertyStorage(ps, data, i));
                    if (data.length <= 4096) {
                        this.numSmallBlocks += getSmallBlocksRequired(data.length);
                    } else {
                        blocksRequired += getBigBlocksRequired(data.length);
                    }
                }
            }
            this.additionalPropertyBlocks = blocksRequired;
        }
    }

    public void write() throws IOException {
        writeHeader();
        writeExcelData();
        writeDocumentSummaryData();
        writeSummaryData();
        writeAdditionalPropertySets();
        writeSmallBlockDepot();
        writeSmallBlockDepotChain();
        writeBigBlockDepot();
        writePropertySets();
    }

    private void writeAdditionalPropertySets() throws IOException {
        if (this.additionalPropertySets != null) {
            Iterator i = this.additionalPropertySets.iterator();
            while (i.hasNext()) {
                byte[] data = ((ReadPropertyStorage) i.next()).data;
                if (data.length > 4096) {
                    int requiredSize = getBigBlocksRequired(data.length) * 512;
                    this.out.write(data, 0, data.length);
                    byte[] padding = new byte[(requiredSize - data.length)];
                    this.out.write(padding, 0, padding.length);
                }
            }
        }
    }

    private void writeExcelData() throws IOException {
        this.excelData.writeData(this.out);
        this.out.write(new byte[(this.requiredSize - this.size)]);
    }

    private void writeDocumentSummaryData() throws IOException {
        this.out.write(new byte[4096]);
    }

    private void writeSummaryData() throws IOException {
        this.out.write(new byte[4096]);
    }

    private void writeHeader() throws IOException {
        int i;
        byte[] headerBlock = new byte[512];
        byte[] extensionBlockData = new byte[(this.numExtensionBlocks * 512)];
        System.arraycopy(IDENTIFIER, 0, headerBlock, 0, IDENTIFIER.length);
        headerBlock[24] = (byte) 62;
        headerBlock[26] = (byte) 3;
        headerBlock[28] = (byte) -2;
        headerBlock[29] = (byte) -1;
        headerBlock[30] = (byte) 9;
        headerBlock[32] = (byte) 6;
        headerBlock[57] = (byte) 16;
        IntegerHelper.getFourBytes(this.numBigBlockDepotBlocks, headerBlock, 44);
        IntegerHelper.getFourBytes(this.sbdStartBlockChain, headerBlock, 60);
        IntegerHelper.getFourBytes(this.numSmallBlockDepotChainBlocks, headerBlock, 64);
        IntegerHelper.getFourBytes(this.extensionBlock, headerBlock, 68);
        IntegerHelper.getFourBytes(this.numExtensionBlocks, headerBlock, 72);
        IntegerHelper.getFourBytes(this.rootStartBlock, headerBlock, 48);
        int pos = 76;
        int blocksToWrite = Math.min(this.numBigBlockDepotBlocks, 109);
        int blocksWritten = 0;
        for (i = 0; i < blocksToWrite; i++) {
            IntegerHelper.getFourBytes(this.bbdStartBlock + i, headerBlock, pos);
            pos += 4;
            blocksWritten++;
        }
        for (i = pos; i < 512; i++) {
            headerBlock[i] = (byte) -1;
        }
        this.out.write(headerBlock);
        pos = 0;
        int extBlock = 0;
        while (extBlock < this.numExtensionBlocks) {
            blocksToWrite = Math.min(this.numBigBlockDepotBlocks - blocksWritten, 127);
            for (int j = 0; j < blocksToWrite; j++) {
                IntegerHelper.getFourBytes((this.bbdStartBlock + blocksWritten) + j, extensionBlockData, pos);
                pos += 4;
            }
            blocksWritten += blocksToWrite;
            IntegerHelper.getFourBytes(blocksWritten != this.numBigBlockDepotBlocks ? extBlock + 1 : -2, extensionBlockData, pos);
            pos += 4;
            extBlock++;
        }
        if (this.numExtensionBlocks > 0) {
            for (i = pos; i < extensionBlockData.length; i++) {
                extensionBlockData[i] = (byte) -1;
            }
            this.out.write(extensionBlockData);
        }
    }

    private void checkBbdPos() throws IOException {
        if (this.bbdPos >= 512) {
            this.out.write(this.bigBlockDepot);
            this.bigBlockDepot = new byte[512];
            this.bbdPos = 0;
        }
    }

    private void writeBlockChain(int startBlock, int numBlocks) throws IOException {
        int blocksToWrite = numBlocks - 1;
        int blockNumber = startBlock + 1;
        while (blocksToWrite > 0) {
            int bbdBlocks = Math.min(blocksToWrite, (512 - this.bbdPos) / 4);
            for (int i = 0; i < bbdBlocks; i++) {
                IntegerHelper.getFourBytes(blockNumber, this.bigBlockDepot, this.bbdPos);
                this.bbdPos += 4;
                blockNumber++;
            }
            blocksToWrite -= bbdBlocks;
            checkBbdPos();
        }
        IntegerHelper.getFourBytes(-2, this.bigBlockDepot, this.bbdPos);
        this.bbdPos += 4;
        checkBbdPos();
    }

    private void writeAdditionalPropertySetBlockChains() throws IOException {
        if (this.additionalPropertySets != null) {
            int blockNumber = (this.excelDataStartBlock + this.excelDataBlocks) + 16;
            Iterator i = this.additionalPropertySets.iterator();
            while (i.hasNext()) {
                ReadPropertyStorage rps = (ReadPropertyStorage) i.next();
                if (rps.data.length > 4096) {
                    int numBlocks = getBigBlocksRequired(rps.data.length);
                    writeBlockChain(blockNumber, numBlocks);
                    blockNumber += numBlocks;
                }
            }
        }
    }

    private void writeSmallBlockDepotChain() throws IOException {
        if (this.sbdStartBlockChain != -2) {
            byte[] smallBlockDepotChain = new byte[(this.numSmallBlockDepotChainBlocks * 512)];
            int pos = 0;
            int sbdBlockNumber = 1;
            Iterator i = this.additionalPropertySets.iterator();
            while (i.hasNext()) {
                ReadPropertyStorage rps = (ReadPropertyStorage) i.next();
                if (rps.data.length <= 4096 && rps.data.length != 0) {
                    int numSmallBlocks = getSmallBlocksRequired(rps.data.length);
                    for (int j = 0; j < numSmallBlocks - 1; j++) {
                        IntegerHelper.getFourBytes(sbdBlockNumber, smallBlockDepotChain, pos);
                        pos += 4;
                        sbdBlockNumber++;
                    }
                    IntegerHelper.getFourBytes(-2, smallBlockDepotChain, pos);
                    pos += 4;
                    sbdBlockNumber++;
                }
            }
            this.out.write(smallBlockDepotChain);
        }
    }

    private void writeSmallBlockDepot() throws IOException {
        if (this.additionalPropertySets != null) {
            byte[] smallBlockDepot = new byte[(this.numSmallBlockDepotBlocks * 512)];
            int pos = 0;
            Iterator i = this.additionalPropertySets.iterator();
            while (i.hasNext()) {
                ReadPropertyStorage rps = (ReadPropertyStorage) i.next();
                if (rps.data.length <= 4096) {
                    int length = getSmallBlocksRequired(rps.data.length) * 64;
                    System.arraycopy(rps.data, 0, smallBlockDepot, pos, rps.data.length);
                    pos += length;
                }
            }
            this.out.write(smallBlockDepot);
        }
    }

    private void writeBigBlockDepot() throws IOException {
        int i;
        this.bigBlockDepot = new byte[512];
        this.bbdPos = 0;
        for (i = 0; i < this.numExtensionBlocks; i++) {
            IntegerHelper.getFourBytes(-3, this.bigBlockDepot, this.bbdPos);
            this.bbdPos += 4;
            checkBbdPos();
        }
        writeBlockChain(this.excelDataStartBlock, this.excelDataBlocks);
        int summaryInfoBlock = (this.excelDataStartBlock + this.excelDataBlocks) + this.additionalPropertyBlocks;
        for (i = summaryInfoBlock; i < summaryInfoBlock + 7; i++) {
            IntegerHelper.getFourBytes(i + 1, this.bigBlockDepot, this.bbdPos);
            this.bbdPos += 4;
            checkBbdPos();
        }
        IntegerHelper.getFourBytes(-2, this.bigBlockDepot, this.bbdPos);
        this.bbdPos += 4;
        checkBbdPos();
        for (i = summaryInfoBlock + 8; i < summaryInfoBlock + 15; i++) {
            IntegerHelper.getFourBytes(i + 1, this.bigBlockDepot, this.bbdPos);
            this.bbdPos += 4;
            checkBbdPos();
        }
        IntegerHelper.getFourBytes(-2, this.bigBlockDepot, this.bbdPos);
        this.bbdPos += 4;
        checkBbdPos();
        writeAdditionalPropertySetBlockChains();
        if (this.sbdStartBlock != -2) {
            writeBlockChain(this.sbdStartBlock, this.numSmallBlockDepotBlocks);
            writeBlockChain(this.sbdStartBlockChain, this.numSmallBlockDepotChainBlocks);
        }
        for (i = 0; i < this.numBigBlockDepotBlocks; i++) {
            IntegerHelper.getFourBytes(-3, this.bigBlockDepot, this.bbdPos);
            this.bbdPos += 4;
            checkBbdPos();
        }
        writeBlockChain(this.rootStartBlock, this.numRootEntryBlocks);
        if (this.bbdPos != 0) {
            for (i = this.bbdPos; i < 512; i++) {
                this.bigBlockDepot[i] = (byte) -1;
            }
            this.out.write(this.bigBlockDepot);
        }
    }

    private int getBigBlocksRequired(int length) {
        int blocks = length / 512;
        return length % 512 <= 0 ? blocks : blocks + 1;
    }

    private int getSmallBlocksRequired(int length) {
        int blocks = length / 64;
        return length % 64 <= 0 ? blocks : blocks + 1;
    }
}
