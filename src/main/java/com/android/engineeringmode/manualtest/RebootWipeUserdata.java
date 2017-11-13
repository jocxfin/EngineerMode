package com.android.engineeringmode.manualtest;

import android.content.Context;
import android.os.ConditionVariable;
import android.os.PowerManager;
import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class RebootWipeUserdata {
    private static File COMMAND_FILE = new File(RECOVERY_DIR, "command");
    private static File LOG_FILE = new File(RECOVERY_DIR, "log");
    private static File RECOVERY_DIR = new File("/cache/recovery");

    public static void rebootWipeUserData(Context context, boolean shutdown, String reason, String wipeType) throws IOException {
        ConditionVariable condition = new ConditionVariable();
        String shutdownArg = null;
        if (shutdown) {
            shutdownArg = "--shutdown_after";
        }
        String reasonArg = null;
        if (!TextUtils.isEmpty(reason)) {
            reasonArg = "--reason=" + sanitizeArg(reason);
        }
        String localeArg = "--locale=" + Locale.getDefault().toString();
        bootCommand(context, shutdownArg, wipeType, reasonArg, localeArg);
    }

    private static String sanitizeArg(String arg) {
        return arg.replace('\u0000', '?').replace('\n', '?');
    }

    private static void bootCommand(Context context, String... args) throws IOException {
        RECOVERY_DIR.mkdirs();
        COMMAND_FILE.delete();
        LOG_FILE.delete();
        FileWriter command = new FileWriter(COMMAND_FILE);
        try {
            for (String arg : args) {
                if (!TextUtils.isEmpty(arg)) {
                    command.write(arg);
                    command.write("\n");
                }
            }
            ((PowerManager) context.getSystemService("power")).reboot("recovery");
            throw new IOException("Reboot failed (no permissions?)");
        } finally {
            command.close();
        }
    }
}
