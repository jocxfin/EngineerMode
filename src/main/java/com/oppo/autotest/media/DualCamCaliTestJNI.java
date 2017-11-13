package com.oppo.autotest.media;

public class DualCamCaliTestJNI {
    private static String TAG;

    static {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.oppo.autotest.media.DualCamCaliTestJNI.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
	at jadx.core.dex.nodes.MethodNode.addJump(MethodNode.java:370)
	at jadx.core.dex.nodes.MethodNode.initJumps(MethodNode.java:360)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:106)
	... 7 more
*/
        /*
        r1 = "DualCamCaliTestJNI";
        TAG = r1;
        r1 = "CalibrationJNI";	 Catch:{ Exception -> 0x000c }
        java.lang.System.loadLibrary(r1);	 Catch:{ Exception -> 0x000c }
    L_0x000c:
        r0 = move-exception;
        r1 = TAG;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Load library libCalibrationJNI.so failed!";
        android.util.Log.e(r1, r2);
        goto L_0x000b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oppo.autotest.media.DualCamCaliTestJNI.<clinit>():void");
    }

    public native int deInitCalibration();

    public native int initCalibration(String str, int[] iArr, int[] iArr2);

    public native int processCalibration(byte[] bArr, byte[] bArr2, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, byte[] bArr3, float[] fArr);
}
