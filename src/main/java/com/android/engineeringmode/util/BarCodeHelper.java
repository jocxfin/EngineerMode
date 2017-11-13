package com.android.engineeringmode.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

public class BarCodeHelper {
    public static Bitmap createQRImage(String content, int width, int height) {
        if (content != null) {
            try {
                if (!"".equals(content) && content.length() >= 1) {
                    Hashtable<EncodeHintType, String> hints = new Hashtable();
                    hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                    BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
                    int[] pixels = new int[(width * height)];
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            if (bitMatrix.get(x, y)) {
                                pixels[(y * width) + x] = -16777216;
                            } else {
                                pixels[(y * width) + x] = -1;
                            }
                        }
                    }
                    Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
                    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
                    return bitmap;
                }
            } catch (WriterException e) {
                e.printStackTrace();
                return null;
            }
        }
        Log.e("BarCodeHelper", "content invalid");
        return null;
    }
}
