package com.android.engineeringmode.manualtest.cameratest;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera.Size;
import android.util.Log;

import com.android.engineeringmode.functions.Light;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CameraCaliUtil {
    public static byte[] getNV21(int inputWidth, int inputHeight, Bitmap scaled) {
        int[] argb = new int[(inputWidth * inputHeight)];
        scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);
        byte[] yuv = new byte[(((inputWidth * inputHeight) * 3) / 2)];
        encodeYUV420SP(yuv, argb, inputWidth, inputHeight);
        scaled.recycle();
        return yuv;
    }

    public static void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
        int yIndex = 0;
        int uvIndex = width * height;
        int index = 0;
        int j = 0;
        while (j < height) {
            int i = 0;
            int uvIndex2 = uvIndex;
            int yIndex2 = yIndex;
            while (i < width) {
                int A = (argb[index] & -16777216) >> 24;
                int R = (argb[index] & 16711680) >> 16;
                int G = (argb[index] & 65280) >> 8;
                int B = (argb[index] & Light.MAIN_KEY_MAX) >> 0;
                int Y = (((((R * 66) + (G * 129)) + (B * 25)) + 128) >> 8) + 16;
                int U = (((((R * -38) - (G * 74)) + (B * 112)) + 128) >> 8) + 128;
                int V = (((((R * 112) - (G * 94)) - (B * 18)) + 128) >> 8) + 128;
                yIndex = yIndex2 + 1;
                if (Y < 0) {
                    Y = 0;
                } else if (Y > 255) {
                    Y = Light.MAIN_KEY_MAX;
                }
                yuv420sp[yIndex2] = (byte) Y;
                if (j % 2 == 0 && index % 2 == 0) {
                    uvIndex = uvIndex2 + 1;
                    if (V < 0) {
                        V = 0;
                    } else if (V > 255) {
                        V = Light.MAIN_KEY_MAX;
                    }
                    yuv420sp[uvIndex2] = (byte) V;
                    uvIndex2 = uvIndex + 1;
                    if (U < 0) {
                        U = 0;
                    } else if (U > 255) {
                        U = Light.MAIN_KEY_MAX;
                    }
                    yuv420sp[uvIndex] = (byte) U;
                    uvIndex = uvIndex2;
                } else {
                    uvIndex = uvIndex2;
                }
                index++;
                i++;
                uvIndex2 = uvIndex;
                yIndex2 = yIndex;
            }
            j++;
            uvIndex = uvIndex2;
            yIndex = yIndex2;
        }
    }

    public static void saveAssetsToSdcard(Context context, String assetsPath, String filename, String sdCardPath) {
        try {
            InputStream mIs = context.getAssets().open(assetsPath + File.separator + filename);
            byte[] mByte = new byte[1024];
            File file = new File(sdCardPath + File.separator + filename);
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            } else {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            while (true) {
                int bt = mIs.read(mByte);
                if (bt != -1) {
                    fos.write(mByte, 0, bt);
                } else {
                    fos.flush();
                    mIs.close();
                    fos.close();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Size getMaxSize(List<Size> supportSizes, int sizeMode, int maxWidthLimit) {
        if (supportSizes == null) {
            return null;
        }
        int picWidth = 0;
        int index = 0;
        double ratio;
        switch (sizeMode) {
            case 0:
                ratio = 1.3333333333333333d;
                break;
            case Light.MAIN_KEY_LIGHT /*1*/:
                ratio = 1.7777777777777777d;
                break;
            default:
                ratio = 1.3333333333333333d;
                break;
        }
        int i = 0;
        while (i < supportSizes.size()) {
            if ((((Size) supportSizes.get(i)).width <= ((Size) supportSizes.get(i)).height ? ((Size) supportSizes.get(i)).width : ((Size) supportSizes.get(i)).height) <= maxWidthLimit && Math.abs((((double) ((Size) supportSizes.get(i)).width) / ((double) ((Size) supportSizes.get(i)).height)) - ratio) < 0.015d && ((Size) supportSizes.get(i)).width > picWidth) {
                picWidth = ((Size) supportSizes.get(i)).width;
                index = i;
            }
            i++;
        }
        return (Size) supportSizes.get(index);
    }

    public static Size getMaxSize(List<Size> supportSizes) {
        if (supportSizes == null) {
            return null;
        }
        int picWidth = 0;
        int picHeight = 0;
        int index = 0;
        for (int i = 0; i < supportSizes.size(); i++) {
            if (((Size) supportSizes.get(i)).height * ((Size) supportSizes.get(i)).width > picWidth * picHeight) {
                picWidth = ((Size) supportSizes.get(i)).width;
                picHeight = ((Size) supportSizes.get(i)).height;
                index = i;
            }
        }
        return (Size) supportSizes.get(index);
    }

    public static void setFileChmod(File file, String chmodSet) {
        try {
            String command = chmodSet + " " + file.getAbsolutePath();
            Log.i("CameraCali", "command = " + command);
            Process exec = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            Log.i("CameraCali", "chmod fail!");
            e.printStackTrace();
        }
    }
}
