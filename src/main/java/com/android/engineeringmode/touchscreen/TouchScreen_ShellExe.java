package com.android.engineeringmode.touchscreen;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TouchScreen_ShellExe {
    public static String ERROR = "ERROR";
    private static StringBuilder sb = new StringBuilder("");

    public static String getOutput() {
        return sb.toString();
    }

    public static int execCommand(String[] command) throws IOException {
        Process proc = Runtime.getRuntime().exec(command);
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        sb.delete(0, sb.length());
        try {
            if (proc.waitFor() != 0) {
                Log.i("MTK", "exit value = " + proc.exitValue());
                sb.append(ERROR);
                return -1;
            }
            while (true) {
                String line = bufferedreader.readLine();
                if (line == null) {
                    return 0;
                }
                sb.append(line);
            }
        } catch (InterruptedException e) {
            Log.i("MTK", "exe fail " + e.toString());
            sb.append(ERROR);
            return -1;
        }
    }
}
