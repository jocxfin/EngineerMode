package com.android.engineeringmode.manualtest;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.engineeringmode.Log;
import com.android.engineeringmode.manualtest.modeltest.ModelTest3ItemActivity;
import com.oem.util.Feature;
import com.oem.util.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class BatteryInfoShow extends ModelTest3ItemActivity {
    private static String CHAGREE_VOL_PATH = "/sys/class/power_supply/battery/charge_now";
    private final int SKIP_NUM = 7;
    private String charger_mode = "normal_charger";
    private int clickCount = 0;
    private boolean isInModelTest = false;
    private boolean mAuthiticate = false;
    private TextView mBatteryCapShow;
    IntentFilter mBatteryFilter = null;
    BroadcastReceiver mBatteryStatusReceiver = new BroadcastReceiver() {
        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r27, android.content.Intent r28) {
            /*
            r26 = this;
            r4 = r28.getAction();
            r20 = "android.intent.action.ADDITIONAL_BATTERY_CHANGED";
            r0 = r20;
            r20 = r0.equals(r4);
            if (r20 == 0) goto L_0x009e;
        L_0x000f:
            r20 = "chargefastcharger";
            r21 = 0;
            r0 = r28;
            r1 = r20;
            r2 = r21;
            r12 = r0.getBooleanExtra(r1, r2);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mIsFastCharger;
            r0 = r20;
            if (r12 == r0) goto L_0x0053;
        L_0x002c:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = 0;
            r20.mHasShowResult = r21;
            if (r12 == 0) goto L_0x0083;
        L_0x0039:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mFastchargerShow;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r21 = r0;
            r22 = 2131297340; // 0x7f09043c float:1.8212622E38 double:1.053000797E-314;
            r21 = r21.getString(r22);
            r20.setText(r21);
        L_0x0053:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r0 = r20;
            r0.mIsFastCharger = r12;
            r20 = "BatteryInfoShow";
            r21 = new java.lang.StringBuilder;
            r21.<init>();
            r22 = "current is fastcharger ? ";
            r21 = r21.append(r22);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r22 = r0;
            r22 = r22.mIsFastCharger;
            r21 = r21.append(r22);
            r21 = r21.toString();
            com.android.engineeringmode.Log.i(r20, r21);
            return;
        L_0x0083:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mFastchargerShow;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r21 = r0;
            r22 = 2131297341; // 0x7f09043d float:1.8212624E38 double:1.0530007973E-314;
            r21 = r21.getString(r22);
            r20.setText(r21);
            goto L_0x0053;
        L_0x009e:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = "/sys/class/power_supply/usb/type";
            r20 = r20.isFileExist(r21);
            if (r20 == 0) goto L_0x00ce;
        L_0x00ad:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = "/sys/class/power_supply/usb/type";
            r19 = r20.readStringFromFile(r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = "DASH";
            r0 = r19;
            r1 = r21;
            r21 = r0.equals(r1);
            r20.mIsFastCharger = r21;
        L_0x00ce:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mIsFastCharger;
            if (r20 == 0) goto L_0x05e3;
        L_0x00da:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mFastchargerShow;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r21 = r0;
            r22 = 2131297340; // 0x7f09043c float:1.8212622E38 double:1.053000797E-314;
            r21 = r21.getString(r22);
            r20.setText(r21);
        L_0x00f4:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTypeInfo;
            r21 = 0;
            r20.setVisibility(r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTypeShow;
            r21 = 0;
            r20.setVisibility(r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = "/sys/class/power_supply/bms/battery_type";
            r20 = r20.isFileExist(r21);
            if (r20 == 0) goto L_0x013f;
        L_0x0121:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = "/sys/class/power_supply/bms/battery_type";
            r19 = r20.readStringFromFile(r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTypeShow;
            r0 = r20;
            r1 = r19;
            r0.setText(r1);
        L_0x013f:
            r5 = 0;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mIsOppoFind7;
            if (r20 == 0) goto L_0x01ea;
        L_0x014c:
            r20 = "health";
            r21 = 1;
            r0 = r28;
            r1 = r20;
            r2 = r21;
            r6 = r0.getIntExtra(r1, r2);
            r20 = "BatteryInfoShow";
            r21 = new java.lang.StringBuilder;
            r21.<init>();
            r22 = "batteryHealth:";
            r21 = r21.append(r22);
            r0 = r21;
            r21 = r0.append(r6);
            r21 = r21.toString();
            com.android.engineeringmode.Log.i(r20, r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTypeInfo;
            r21 = 0;
            r20.setVisibility(r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTypeShow;
            r21 = 0;
            r20.setVisibility(r21);
            r20 = 1;
            r0 = r20;
            if (r6 == r0) goto L_0x063d;
        L_0x019b:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mAuthiticate;
            if (r20 == 0) goto L_0x061d;
        L_0x01a7:
            r20 = "/sys/class/power_supply/battery/authenticate";
            r5 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r20);
            r20 = "BatteryInfoShow";
            r21 = new java.lang.StringBuilder;
            r21.<init>();
            r22 = "authiticate:";
            r21 = r21.append(r22);
            r0 = r21;
            r21 = r0.append(r5);
            r21 = r21.toString();
            com.android.engineeringmode.Log.i(r20, r21);
            r20 = 1;
            r0 = r20;
            if (r5 != r0) goto L_0x05ff;
        L_0x01d0:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTypeShow;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r21 = r0;
            r22 = 2131296727; // 0x7f0901d7 float:1.8211379E38 double:1.053000494E-314;
            r21 = r21.getString(r22);
            r20.setText(r21);
        L_0x01ea:
            r20 = "android.intent.action.BATTERY_CHANGED";
            r0 = r20;
            r20 = r0.equals(r4);
            if (r20 == 0) goto L_0x05e2;
        L_0x01f5:
            r20 = "level";
            r21 = 0;
            r0 = r28;
            r1 = r20;
            r2 = r21;
            r14 = r0.getIntExtra(r1, r2);
            r20 = "scale";
            r21 = 100;
            r0 = r28;
            r1 = r20;
            r2 = r21;
            r15 = r0.getIntExtra(r1, r2);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryCapShow;
            r21 = new java.lang.StringBuilder;
            r21.<init>();
            r22 = r14 * 100;
            r22 = r22 / r15;
            r22 = java.lang.String.valueOf(r22);
            r21 = r21.append(r22);
            r22 = "%";
            r21 = r21.append(r22);
            r21 = r21.toString();
            r20.setText(r21);
            r20 = "voltage";
            r21 = 0;
            r0 = r28;
            r1 = r20;
            r2 = r21;
            r16 = r0.getIntExtra(r1, r2);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryVolShow;
            r21 = new java.lang.StringBuilder;
            r21.<init>();
            r0 = r16;
            r0 = (double) r0;
            r22 = r0;
            r24 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
            r22 = r22 / r24;
            r22 = java.lang.String.valueOf(r22);
            r21 = r21.append(r22);
            r22 = " ";
            r21 = r21.append(r22);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r22 = r0;
            r23 = 2131296721; // 0x7f0901d1 float:1.8211367E38 double:1.053000491E-314;
            r22 = r22.getString(r23);
            r21 = r21.append(r22);
            r21 = r21.toString();
            r20.setText(r21);
            r20 = "temperature";
            r21 = 0;
            r0 = r28;
            r1 = r20;
            r2 = r21;
            r10 = r0.getIntExtra(r1, r2);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r0 = (float) r10;
            r21 = r0;
            r20.nTemp = r21;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r21 = r0;
            r21 = r21.nTemp;
            r0 = r21;
            r0 = (double) r0;
            r22 = r0;
            r24 = 4621819117588971520; // 0x4024000000000000 float:0.0 double:10.0;
            r22 = r22 / r24;
            r0 = r22;
            r0 = (float) r0;
            r21 = r0;
            r20.nTemp = r21;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTempShow;
            r21 = new java.lang.StringBuilder;
            r21.<init>();
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r22 = r0;
            r22 = r22.nTemp;
            r22 = java.lang.String.valueOf(r22);
            r21 = r21.append(r22);
            r22 = " ";
            r21 = r21.append(r22);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r22 = r0;
            r23 = 2131296723; // 0x7f0901d3 float:1.821137E38 double:1.053000492E-314;
            r22 = r22.getString(r23);
            r21 = r21.append(r22);
            r21 = r21.toString();
            r20.setText(r21);
            r20 = "/sys/class/power_supply/battery/current_now";
            r20 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r20);
            r0 = r20;
            r11 = r0 / 1000;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mChargerElecShow;
            r21 = new java.lang.StringBuilder;
            r21.<init>();
            r22 = java.lang.String.valueOf(r11);
            r21 = r21.append(r22);
            r22 = " ";
            r21 = r21.append(r22);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r22 = r0;
            r23 = 2131296722; // 0x7f0901d2 float:1.8211369E38 double:1.0530004914E-314;
            r22 = r22.getString(r23);
            r21 = r21.append(r22);
            r21 = r21.toString();
            r20.setText(r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = "plugged";
            r22 = 0;
            r0 = r28;
            r1 = r21;
            r2 = r22;
            r21 = r0.getIntExtra(r1, r2);
            r20.plugType = r21;
            r20 = "status";
            r21 = 1;
            r0 = r28;
            r1 = r20;
            r2 = r21;
            r17 = r0.getIntExtra(r1, r2);
            r18 = 0;
            r8 = 0;
            r9 = 0;
            r20 = 2;
            r0 = r17;
            r1 = r20;
            if (r0 == r1) goto L_0x0385;
        L_0x037d:
            r20 = 5;
            r0 = r17;
            r1 = r20;
            if (r0 != r1) goto L_0x0675;
        L_0x0385:
            r20 = 5;
            r0 = r17;
            r1 = r20;
            if (r0 != r1) goto L_0x0659;
        L_0x038d:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = 2131296738; // 0x7f0901e2 float:1.8211401E38 double:1.0530004993E-314;
            r18 = r20.getString(r21);
        L_0x039a:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.plugType;
            if (r20 <= 0) goto L_0x03e3;
        L_0x03a6:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r21 = r0;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.plugType;
            r22 = 1;
            r0 = r20;
            r1 = r22;
            if (r0 != r1) goto L_0x0670;
        L_0x03be:
            r20 = 2131296734; // 0x7f0901de float:1.8211393E38 double:1.0530004974E-314;
        L_0x03c1:
            r0 = r21;
            r1 = r20;
            r8 = r0.getString(r1);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mChargerTypeShow;
            r0 = r20;
            r0.setText(r8);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = 1;
            r20.mShowResult = r21;
        L_0x03e3:
            r20 = com.android.engineeringmode.manualtest.BatteryInfoShow.CHAGREE_VOL_PATH;
            r9 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r20);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mChargerVolShow;
            r21 = new java.lang.StringBuilder;
            r21.<init>();
            r0 = (double) r9;
            r22 = r0;
            r24 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
            r22 = r22 / r24;
            r22 = java.lang.String.valueOf(r22);
            r21 = r21.append(r22);
            r22 = " ";
            r21 = r21.append(r22);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r22 = r0;
            r23 = 2131296721; // 0x7f0901d1 float:1.8211367E38 double:1.053000491E-314;
            r22 = r22.getString(r23);
            r21 = r21.append(r22);
            r21 = r21.toString();
            r20.setText(r21);
        L_0x042b:
            r20 = "BatteryInfoShow";
            r21 = new java.lang.StringBuilder;
            r21.<init>();
            r22 = "mShowResult:";
            r21 = r21.append(r22);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r22 = r0;
            r22 = r22.mShowResult;
            r21 = r21.append(r22);
            r22 = "  mHasShowResult:";
            r21 = r21.append(r22);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r22 = r0;
            r22 = r22.mHasShowResult;
            r21 = r21.append(r22);
            r21 = r21.toString();
            com.android.engineeringmode.Log.e(r20, r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mIsFastCharger;
            if (r20 != 0) goto L_0x047c;
        L_0x0470:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mHasShowResult;
            if (r20 == 0) goto L_0x0700;
        L_0x047c:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.plugType;
            r21 = 1;
            r0 = r20;
            r1 = r21;
            if (r0 != r1) goto L_0x05c8;
        L_0x048e:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mShowResult;
            if (r20 == 0) goto L_0x05c8;
        L_0x049a:
            r20 = "BatteryInfoShow";
            r21 = "plugType is AC";
            com.android.engineeringmode.Log.e(r20, r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mHasShowResult;
            if (r20 != 0) goto L_0x05c8;
        L_0x04af:
            r7 = 0;
            r20 = "BatteryInfoShow";
            r21 = new java.lang.StringBuilder;
            r21.<init>();
            r22 = "mIsFastCharger:";
            r21 = r21.append(r22);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r22 = r0;
            r22 = r22.mIsFastCharger;
            r21 = r21.append(r22);
            r22 = "  elec:";
            r21 = r21.append(r22);
            r0 = r21;
            r21 = r0.append(r11);
            r22 = "  nTemp:";
            r21 = r21.append(r22);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r22 = r0;
            r22 = r22.nTemp;
            r21 = r21.append(r22);
            r22 = "  chargerVol:";
            r21 = r21.append(r22);
            r0 = r21;
            r21 = r0.append(r9);
            r21 = r21.toString();
            com.android.engineeringmode.Log.e(r20, r21);
            r20 = "BatteryInfoShow";
            r21 = new java.lang.StringBuilder;
            r21.<init>();
            r22 = "curTemp:";
            r21 = r21.append(r22);
            r22 = "temperature";
            r23 = 0;
            r0 = r28;
            r1 = r22;
            r2 = r23;
            r22 = r0.getIntExtra(r1, r2);
            r21 = r21.append(r22);
            r22 = "  mLastTemp:";
            r21 = r21.append(r22);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r22 = r0;
            r22 = r22.mLastTemp;
            r21 = r21.append(r22);
            r22 = "  FASTCHG_START:";
            r21 = r21.append(r22);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r22 = r0;
            r23 = "sys/class/power_supply/battery/fastchg_starting";
            r22 = r22.readStringFromFile(r23);
            r21 = r21.append(r22);
            r21 = r21.toString();
            com.android.engineeringmode.Log.e(r20, r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mIsFastCharger;
            if (r20 == 0) goto L_0x0809;
        L_0x0565:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.charger_mode;
            r21 = "normal_charger";
            r20 = r20.equals(r21);
            if (r20 == 0) goto L_0x0809;
        L_0x0578:
            r20 = "BatteryInfoShow";
            r21 = "insert a wrong type charger";
            com.android.engineeringmode.Log.i(r20, r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mResult;
            r21 = 0;
            r20.setVisibility(r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mResult;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r21 = r0;
            r21 = r21.getResources();
            r22 = 2131296749; // 0x7f0901ed float:1.8211423E38 double:1.053000505E-314;
            r21 = r21.getString(r22);
            r20.setText(r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mResult;
            r21 = -65536; // 0xffffffffffff0000 float:NaN double:NaN;
            r20.setTextColor(r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = 0;
            r20.skipNum = r21;
        L_0x05c8:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mChargerStatusShow;
            r0 = r20;
            r1 = r18;
            r0.setText(r1);
            r20 = "BatteryInfoShow";
            r21 = "Receice Broadcast: ACTION_BATTERY_CHANGED";
            com.android.engineeringmode.Log.w(r20, r21);
        L_0x05e2:
            return;
        L_0x05e3:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mFastchargerShow;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r21 = r0;
            r22 = 2131297341; // 0x7f09043d float:1.8212624E38 double:1.0530007973E-314;
            r21 = r21.getString(r22);
            r20.setText(r21);
            goto L_0x00f4;
        L_0x05ff:
            if (r5 != 0) goto L_0x01ea;
        L_0x0601:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTypeShow;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r21 = r0;
            r22 = 2131296728; // 0x7f0901d8 float:1.821138E38 double:1.0530004944E-314;
            r21 = r21.getString(r22);
            r20.setText(r21);
            goto L_0x01ea;
        L_0x061d:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTypeInfo;
            r21 = 8;
            r20.setVisibility(r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTypeShow;
            r21 = 8;
            r20.setVisibility(r21);
            goto L_0x01ea;
        L_0x063d:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTypeShow;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r21 = r0;
            r22 = 2131296729; // 0x7f0901d9 float:1.8211383E38 double:1.053000495E-314;
            r21 = r21.getString(r22);
            r20.setText(r21);
            goto L_0x01ea;
        L_0x0659:
            r20 = 2;
            r0 = r17;
            r1 = r20;
            if (r0 != r1) goto L_0x039a;
        L_0x0661:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = 2131296733; // 0x7f0901dd float:1.821139E38 double:1.053000497E-314;
            r18 = r20.getString(r21);
            goto L_0x039a;
        L_0x0670:
            r20 = 2131296735; // 0x7f0901df float:1.8211395E38 double:1.053000498E-314;
            goto L_0x03c1;
        L_0x0675:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mChargerTypeShow;
            r21 = "";
            r20.setText(r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mChargerVolShow;
            r21 = "";
            r20.setText(r21);
            r20 = 3;
            r0 = r17;
            r1 = r20;
            if (r0 != r1) goto L_0x06dc;
        L_0x069d:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = 2131296736; // 0x7f0901e0 float:1.8211397E38 double:1.0530004984E-314;
            r18 = r20.getString(r21);
        L_0x06aa:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = 0;
            r20.mHasShowResult = r21;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r0 = r20;
            r0.mLastTemp = r10;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mResult;
            r21 = 4;
            r20.setVisibility(r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = 0;
            r20.skipNum = r21;
            goto L_0x042b;
        L_0x06dc:
            r20 = 4;
            r0 = r17;
            r1 = r20;
            if (r0 != r1) goto L_0x06f2;
        L_0x06e4:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = 2131296737; // 0x7f0901e1 float:1.82114E38 double:1.053000499E-314;
            r18 = r20.getString(r21);
            goto L_0x06aa;
        L_0x06f2:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = 2131296732; // 0x7f0901dc float:1.8211389E38 double:1.0530004964E-314;
            r18 = r20.getString(r21);
            goto L_0x06aa;
        L_0x0700:
            r20 = -200; // 0xffffffffffffff38 float:NaN double:NaN;
            r0 = r20;
            if (r11 >= r0) goto L_0x07b6;
        L_0x0706:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mChargerElecShow;
            r21 = -1;
            r20.setTextColor(r21);
        L_0x0715:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.nTemp;
            r21 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
            r20 = (r20 > r21 ? 1 : (r20 == r21 ? 0 : -1));
            if (r20 < 0) goto L_0x07c7;
        L_0x0725:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.nTemp;
            r21 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
            r20 = (r20 > r21 ? 1 : (r20 == r21 ? 0 : -1));
            if (r20 > 0) goto L_0x07c7;
        L_0x0735:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTempShow;
            r21 = -1;
            r20.setTextColor(r21);
        L_0x0744:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = "/sys/class/power_supply/bms/battery_type";
            r20 = r20.isFileExist(r21);
            if (r20 == 0) goto L_0x0769;
        L_0x0753:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = "/sys/class/power_supply/bms/battery_type";
            r20 = r20.readStringFromFile(r21);
            r21 = "Unknown Battery";
            r20 = r20.equals(r21);
            if (r20 == 0) goto L_0x07d8;
        L_0x0769:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTypeShow;
            r21 = -65536; // 0xffffffffffff0000 float:NaN double:NaN;
            r20.setTextColor(r21);
        L_0x0778:
            r20 = 4500; // 0x1194 float:6.306E-42 double:2.2233E-320;
            r0 = r20;
            if (r9 < r0) goto L_0x07e8;
        L_0x077e:
            r20 = 5500; // 0x157c float:7.707E-42 double:2.7174E-320;
            r0 = r20;
            if (r9 > r0) goto L_0x07e8;
        L_0x0784:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mChargerVolShow;
            r21 = -1;
            r20.setTextColor(r21);
        L_0x0793:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.plugType;
            r21 = 1;
            r0 = r20;
            r1 = r21;
            if (r0 != r1) goto L_0x07f8;
        L_0x07a5:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mChargerTypeShow;
            r21 = -1;
            r20.setTextColor(r21);
            goto L_0x047c;
        L_0x07b6:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mChargerElecShow;
            r21 = -65536; // 0xffffffffffff0000 float:NaN double:NaN;
            r20.setTextColor(r21);
            goto L_0x0715;
        L_0x07c7:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTempShow;
            r21 = -65536; // 0xffffffffffff0000 float:NaN double:NaN;
            r20.setTextColor(r21);
            goto L_0x0744;
        L_0x07d8:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mBatteryTypeShow;
            r21 = -1;
            r20.setTextColor(r21);
            goto L_0x0778;
        L_0x07e8:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mChargerVolShow;
            r21 = -65536; // 0xffffffffffff0000 float:NaN double:NaN;
            r20.setTextColor(r21);
            goto L_0x0793;
        L_0x07f8:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mChargerTypeShow;
            r21 = -65536; // 0xffffffffffff0000 float:NaN double:NaN;
            r20.setTextColor(r21);
            goto L_0x047c;
        L_0x0809:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mIsFastChargerSupported;
            if (r20 == 0) goto L_0x05c8;
        L_0x0815:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.charger_mode;
            r21 = "fast_charger";
            r20 = r20.equals(r21);
            if (r20 == 0) goto L_0x05c8;
        L_0x0828:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mIsFastCharger;
            if (r20 == 0) goto L_0x05c8;
        L_0x0834:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = "sys/class/power_supply/battery/fastchg_starting";
            r20 = r20.isFileExist(r21);
            if (r20 == 0) goto L_0x05c8;
        L_0x0843:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = "sys/class/power_supply/battery/fastchg_starting";
            r20 = r20.readStringFromFile(r21);
            r21 = "1";
            r20 = r20.equals(r21);
            if (r20 == 0) goto L_0x05c8;
        L_0x0859:
            r20 = "/sys/class/power_supply/battery/current_now";
            r20 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r20);
            r0 = r20;
            r11 = r0 / 1000;
            r20 = -1500; // 0xfffffffffffffa24 float:NaN double:NaN;
            r0 = r20;
            if (r11 >= r0) goto L_0x0939;
        L_0x086a:
            r20 = "/sys/class/power_supply/battery/capacity";
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r20);
            r20 = 70;
            r0 = r20;
            if (r7 < r0) goto L_0x0939;
        L_0x0877:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.nTemp;
            r21 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
            r20 = (r20 > r21 ? 1 : (r20 == r21 ? 0 : -1));
            if (r20 < 0) goto L_0x05c8;
        L_0x0887:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.nTemp;
            r21 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
            r20 = (r20 > r21 ? 1 : (r20 == r21 ? 0 : -1));
            if (r20 > 0) goto L_0x05c8;
        L_0x0897:
            r20 = "BatteryInfoShow";
            r21 = new java.lang.StringBuilder;
            r21.<init>();
            r22 = "  elec:";
            r21 = r21.append(r22);
            r0 = r21;
            r21 = r0.append(r11);
            r22 = " capacity: ";
            r21 = r21.append(r22);
            r0 = r21;
            r21 = r0.append(r7);
            r21 = r21.toString();
            com.android.engineeringmode.Log.e(r20, r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mHander;
            r21 = 2;
            r13 = r20.obtainMessage(r21);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mIsOppoFind7;
            if (r20 == 0) goto L_0x0959;
        L_0x08dc:
            r13.arg1 = r5;
            r20 = "BatteryInfoShow";
            r21 = new java.lang.StringBuilder;
            r21.<init>();
            r22 = "test res:";
            r21 = r21.append(r22);
            r0 = r21;
            r21 = r0.append(r5);
            r21 = r21.toString();
            com.android.engineeringmode.Log.e(r20, r21);
        L_0x08fa:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mHander;
            r0 = r20;
            r0.sendMessage(r13);
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = 0;
            r20.mShowResult = r21;
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.isInModelTest;
            if (r20 != 0) goto L_0x05c8;
        L_0x0920:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r20 = r20.mIsFastCharger;
            if (r20 == 0) goto L_0x05c8;
        L_0x092c:
            r0 = r26;
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r20 = r0;
            r21 = 1;
            r20.mHasShowResult = r21;
            goto L_0x05c8;
        L_0x0939:
            r20 = "/sys/class/power_supply/battery/current_now";
            r20 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r20);
            r0 = r20;
            r11 = r0 / 1000;
            r20 = -2000; // 0xfffffffffffff830 float:NaN double:NaN;
            r0 = r20;
            if (r11 >= r0) goto L_0x05c8;
        L_0x094a:
            r20 = "/sys/class/power_supply/battery/capacity";
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r20);
            r20 = 70;
            r0 = r20;
            if (r7 >= r0) goto L_0x05c8;
        L_0x0957:
            goto L_0x0877;
        L_0x0959:
            r20 = 1;
            r0 = r20;
            r13.arg1 = r0;
            r20 = "BatteryInfoShow";
            r21 = "test success";
            com.android.engineeringmode.Log.e(r20, r21);
            goto L_0x08fa;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.engineeringmode.manualtest.BatteryInfoShow.1.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    private TextView mBatteryTempShow;
    private TextView mBatteryTypeInfo;
    private TextView mBatteryTypeShow;
    private TextView mBatteryVolShow;
    private TextView mChargerElecShow;
    private boolean mChargerPass = false;
    private TextView mChargerStatusShow;
    private TextView mChargerTypeShow;
    private TextView mChargerVolShow;
    private boolean mFastchargerPass = false;
    private TextView mFastchargerShow;
    private TextView mFastchargerType;
    private Handler mHander = new Handler() {
        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r15) {
            /*
            r14 = this;
            r7 = r15.what;
            r8 = 1;
            if (r8 != r7) goto L_0x001f;
        L_0x0005:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = new android.content.Intent;
            r9 = "oppo.intent.action.engineering.mode.update.battery";
            r8.<init>(r9);
            r7.sendBroadcast(r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mHander;
            r8 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
            r10 = 1;
            r7.sendEmptyMessageDelayed(r10, r8);
        L_0x001e:
            return;
        L_0x001f:
            r7 = r15.what;
            r8 = 2;
            if (r8 != r7) goto L_0x0118;
        L_0x0024:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mResult;
            r8 = 0;
            r7.setVisibility(r8);
            r7 = r15.arg1;
            if (r7 != 0) goto L_0x0054;
        L_0x0032:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mResult;
            r8 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = r8.getResources();
            r9 = 2131296280; // 0x7f090018 float:1.8210472E38 double:1.053000273E-314;
            r8 = r8.getString(r9);
            r7.setText(r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mResult;
            r8 = -65536; // 0xffffffffffff0000 float:NaN double:NaN;
            r7.setTextColor(r8);
            goto L_0x001e;
        L_0x0054:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.charger_mode;
            r8 = "normal_charger";
            r7 = r7.equals(r8);
            if (r7 == 0) goto L_0x00e1;
        L_0x0063:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mResult;
            r8 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = r8.getResources();
            r9 = 2131296279; // 0x7f090017 float:1.821047E38 double:1.0530002726E-314;
            r8 = r8.getString(r9);
            r7.setText(r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mResult;
            r8 = -16711936; // 0xffffffffff00ff00 float:-1.7146522E38 double:NaN;
            r7.setTextColor(r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = 1;
            r7.mChargerPass = r8;
        L_0x008b:
            r7 = "BatteryInfoShow";
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r9 = "isInModelTest:";
            r8 = r8.append(r9);
            r9 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r9 = r9.isInModelTest;
            r8 = r8.append(r9);
            r8 = r8.toString();
            com.android.engineeringmode.Log.d(r7, r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.isInModelTest;
            if (r7 == 0) goto L_0x001e;
        L_0x00b3:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.charger_mode;
            r8 = "normal_charger";
            r7 = r7.equals(r8);
            if (r7 == 0) goto L_0x010a;
        L_0x00c2:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mChargerPass;
        L_0x00c8:
            if (r7 == 0) goto L_0x001e;
        L_0x00ca:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mIsOppoFind7;
            if (r7 == 0) goto L_0x0111;
        L_0x00d2:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mIsFastCharger;
            if (r7 == 0) goto L_0x001e;
        L_0x00da:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7.onTestPassed();
            goto L_0x001e;
        L_0x00e1:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mResult;
            r8 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = r8.getResources();
            r9 = 2131296281; // 0x7f090019 float:1.8210474E38 double:1.0530002736E-314;
            r8 = r8.getString(r9);
            r7.setText(r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mResult;
            r8 = -16711936; // 0xffffffffff00ff00 float:-1.7146522E38 double:NaN;
            r7.setTextColor(r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = 1;
            r7.mFastchargerPass = r8;
            goto L_0x008b;
        L_0x010a:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mFastchargerPass;
            goto L_0x00c8;
        L_0x0111:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7.onTestPassed();
            goto L_0x001e;
        L_0x0118:
            r7 = r15.what;
            r8 = 3;
            if (r8 != r7) goto L_0x001e;
        L_0x011d:
            r7 = "/sys/class/power_supply/battery/current_now";
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r7);
            r5 = r7 / 1000;
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mChargerElecShow;
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r9 = java.lang.String.valueOf(r5);
            r8 = r8.append(r9);
            r9 = " ";
            r8 = r8.append(r9);
            r9 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r10 = 2131296722; // 0x7f0901d2 float:1.8211369E38 double:1.0530004914E-314;
            r9 = r9.getString(r10);
            r8 = r8.append(r9);
            r8 = r8.toString();
            r7.setText(r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.CHAGREE_VOL_PATH;
            r3 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r7);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = "/sys/class/power_supply/usb/type";
            r7 = r7.isFileExist(r8);
            if (r7 == 0) goto L_0x01b4;
        L_0x0167:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = "/sys/class/power_supply/usb/type";
            r2 = r7.readStringFromFile(r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = "DASH";
            r8 = r2.equals(r8);
            r7.mIsFastCharger = r8;
            r7 = "Unknown";
            r7 = r2.equals(r7);
            if (r7 != 0) goto L_0x01f1;
        L_0x0185:
            r7 = "USB";
            r7 = r2.equals(r7);
        L_0x018c:
            if (r7 != 0) goto L_0x019a;
        L_0x018e:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = 1;
            r7.plugType = r8;
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = 1;
            r7.mShowResult = r8;
        L_0x019a:
            r7 = "BatteryInfoShow";
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r9 = "chargeType: ";
            r8 = r8.append(r9);
            r8 = r8.append(r2);
            r8 = r8.toString();
            com.android.engineeringmode.Log.e(r7, r8);
        L_0x01b4:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mIsFastCharger;
            if (r7 != 0) goto L_0x01f3;
        L_0x01bc:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.skipNum;
            r8 = 7;
            if (r7 >= r8) goto L_0x01f3;
        L_0x01c5:
            r7 = "BatteryInfoShow";
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r9 = "  skipNum: ";
            r8 = r8.append(r9);
            r9 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r9 = r9.skipNum;
            r8 = r8.append(r9);
            r8 = r8.toString();
            com.android.engineeringmode.Log.e(r7, r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = r7.skipNum;
            r8 = r8 + 1;
            r7.skipNum = r8;
            return;
        L_0x01f1:
            r7 = 1;
            goto L_0x018c;
        L_0x01f3:
            r8 = (double) r3;
            r10 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
            r8 = r8 / r10;
            r10 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
            r7 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
            if (r7 <= 0) goto L_0x0235;
        L_0x0200:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mChargerVolShow;
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r10 = (double) r3;
            r12 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
            r10 = r10 / r12;
            r9 = java.lang.String.valueOf(r10);
            r8 = r8.append(r9);
            r9 = " ";
            r8 = r8.append(r9);
            r9 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r10 = 2131296721; // 0x7f0901d1 float:1.8211367E38 double:1.053000491E-314;
            r9 = r9.getString(r10);
            r8 = r8.append(r9);
            r8 = r8.toString();
            r7.setText(r8);
        L_0x0235:
            r7 = -200; // 0xffffffffffffff38 float:NaN double:NaN;
            if (r5 >= r7) goto L_0x035a;
        L_0x0239:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mChargerElecShow;
            r8 = -1;
            r7.setTextColor(r8);
        L_0x0243:
            r7 = 4500; // 0x1194 float:6.306E-42 double:2.2233E-320;
            if (r3 < r7) goto L_0x0367;
        L_0x0247:
            r7 = 5500; // 0x157c float:7.707E-42 double:2.7174E-320;
            if (r3 > r7) goto L_0x0367;
        L_0x024b:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mChargerVolShow;
            r8 = -1;
            r7.setTextColor(r8);
        L_0x0255:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.plugType;
            r8 = 1;
            if (r7 != r8) goto L_0x001e;
        L_0x025e:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mShowResult;
            if (r7 == 0) goto L_0x001e;
        L_0x0266:
            r7 = "BatteryInfoShow";
            r8 = "plugType is AC";
            com.android.engineeringmode.Log.e(r7, r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mHasShowResult;
            if (r7 != 0) goto L_0x001e;
        L_0x0277:
            r1 = 0;
            r7 = "BatteryInfoShow";
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r9 = "mIsFastCharger:";
            r8 = r8.append(r9);
            r9 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r9 = r9.mIsFastCharger;
            r8 = r8.append(r9);
            r9 = "  elec:";
            r8 = r8.append(r9);
            r8 = r8.append(r5);
            r9 = "  nTemp:";
            r8 = r8.append(r9);
            r9 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r9 = r9.nTemp;
            r8 = r8.append(r9);
            r9 = "  chargerVol:";
            r8 = r8.append(r9);
            r8 = r8.append(r3);
            r8 = r8.toString();
            com.android.engineeringmode.Log.e(r7, r8);
            r7 = "BatteryInfoShow";
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r9 = "curTemp:";
            r8 = r8.append(r9);
            r9 = "/sys/class/power_supply/battery/temp";
            r9 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r9);
            r9 = r9 / 10;
            r8 = r8.append(r9);
            r9 = "  mLastTemp:";
            r8 = r8.append(r9);
            r9 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r9 = r9.mLastTemp;
            r8 = r8.append(r9);
            r9 = "  FASTCHG_START: ";
            r8 = r8.append(r9);
            r9 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r10 = "sys/class/power_supply/battery/fastchg_starting";
            r9 = r9.readStringFromFile(r10);
            r8 = r8.append(r9);
            r8 = r8.toString();
            com.android.engineeringmode.Log.e(r7, r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mIsFastCharger;
            if (r7 == 0) goto L_0x0374;
        L_0x030f:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.charger_mode;
            r8 = "normal_charger";
            r7 = r7.equals(r8);
            if (r7 == 0) goto L_0x0374;
        L_0x031e:
            r7 = "BatteryInfoShow";
            r8 = "insert a wrong type charger";
            com.android.engineeringmode.Log.i(r7, r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mResult;
            r8 = 0;
            r7.setVisibility(r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mResult;
            r8 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = r8.getResources();
            r9 = 2131296749; // 0x7f0901ed float:1.8211423E38 double:1.053000505E-314;
            r8 = r8.getString(r9);
            r7.setText(r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mResult;
            r8 = -65536; // 0xffffffffffff0000 float:NaN double:NaN;
            r7.setTextColor(r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = 0;
            r7.skipNum = r8;
            goto L_0x001e;
        L_0x035a:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mChargerElecShow;
            r8 = -65536; // 0xffffffffffff0000 float:NaN double:NaN;
            r7.setTextColor(r8);
            goto L_0x0243;
        L_0x0367:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mChargerVolShow;
            r8 = -65536; // 0xffffffffffff0000 float:NaN double:NaN;
            r7.setTextColor(r8);
            goto L_0x0255;
        L_0x0374:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mIsFastChargerSupported;
            if (r7 == 0) goto L_0x0489;
        L_0x037c:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.charger_mode;
            r8 = "fast_charger";
            r7 = r7.equals(r8);
            if (r7 == 0) goto L_0x0489;
        L_0x038b:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mIsFastCharger;
            if (r7 == 0) goto L_0x0489;
        L_0x0393:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = "sys/class/power_supply/battery/fastchg_starting";
            r7 = r7.isFileExist(r8);
            if (r7 == 0) goto L_0x001e;
        L_0x039e:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = "sys/class/power_supply/battery/fastchg_starting";
            r7 = r7.readStringFromFile(r8);
            r8 = "1";
            r7 = r7.equals(r8);
            if (r7 == 0) goto L_0x001e;
        L_0x03b0:
            r7 = "/sys/class/power_supply/battery/current_now";
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r7);
            r5 = r7 / 1000;
            r7 = -1500; // 0xfffffffffffffa24 float:NaN double:NaN;
            if (r5 >= r7) goto L_0x0462;
        L_0x03bd:
            r7 = "/sys/class/power_supply/battery/capacity";
            r1 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r7);
            r7 = 70;
            if (r1 < r7) goto L_0x0462;
        L_0x03c8:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.nTemp;
            r8 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
            r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
            if (r7 < 0) goto L_0x001e;
        L_0x03d4:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.nTemp;
            r8 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
            r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
            if (r7 > 0) goto L_0x001e;
        L_0x03e0:
            r7 = "BatteryInfoShow";
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r9 = "  elec:";
            r8 = r8.append(r9);
            r8 = r8.append(r5);
            r9 = " capacity: ";
            r8 = r8.append(r9);
            r8 = r8.append(r1);
            r8 = r8.toString();
            com.android.engineeringmode.Log.e(r7, r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mHander;
            r8 = 2;
            r6 = r7.obtainMessage(r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mIsOppoFind7;
            if (r7 == 0) goto L_0x047c;
        L_0x0418:
            r7 = "/sys/class/power_supply/battery/authenticate";
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r7);
            r6.arg1 = r0;
            r7 = "BatteryInfoShow";
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r9 = "test res:";
            r8 = r8.append(r9);
            r8 = r8.append(r0);
            r8 = r8.toString();
            com.android.engineeringmode.Log.e(r7, r8);
        L_0x043b:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mHander;
            r7.sendMessage(r6);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = 0;
            r7.mShowResult = r8;
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.isInModelTest;
            if (r7 != 0) goto L_0x001e;
        L_0x0452:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mIsFastCharger;
            if (r7 == 0) goto L_0x001e;
        L_0x045a:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = 1;
            r7.mHasShowResult = r8;
            goto L_0x001e;
        L_0x0462:
            r7 = "/sys/class/power_supply/battery/current_now";
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r7);
            r5 = r7 / 1000;
            r7 = -2000; // 0xfffffffffffff830 float:NaN double:NaN;
            if (r5 >= r7) goto L_0x001e;
        L_0x046f:
            r7 = "/sys/class/power_supply/battery/capacity";
            r1 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r7);
            r7 = 70;
            if (r1 >= r7) goto L_0x001e;
        L_0x047a:
            goto L_0x03c8;
        L_0x047c:
            r7 = 1;
            r6.arg1 = r7;
            r7 = "BatteryInfoShow";
            r8 = "test success";
            com.android.engineeringmode.Log.e(r7, r8);
            goto L_0x043b;
        L_0x0489:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mIsFastCharger;
            if (r7 != 0) goto L_0x001e;
        L_0x0491:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.charger_mode;
            r8 = "normal_charger";
            r7 = r7.equals(r8);
            if (r7 == 0) goto L_0x001e;
        L_0x04a0:
            r7 = -500; // 0xfffffffffffffe0c float:NaN double:NaN;
            if (r5 >= r7) goto L_0x001e;
        L_0x04a4:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.nTemp;
            r8 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
            r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
            if (r7 < 0) goto L_0x001e;
        L_0x04b0:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.nTemp;
            r8 = 1110704128; // 0x42340000 float:45.0 double:5.487607523E-315;
            r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
            if (r7 > 0) goto L_0x001e;
        L_0x04bc:
            r7 = 4250; // 0x109a float:5.956E-42 double:2.1E-320;
            if (r3 < r7) goto L_0x001e;
        L_0x04c0:
            r7 = 5500; // 0x157c float:7.707E-42 double:2.7174E-320;
            if (r3 > r7) goto L_0x001e;
        L_0x04c4:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = "/sys/class/power_supply/bms/battery_type";
            r7 = r7.isFileExist(r8);
            if (r7 == 0) goto L_0x001e;
        L_0x04cf:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = "/sys/class/power_supply/bms/battery_type";
            r7 = r7.readStringFromFile(r8);
            r8 = "Unknown Battery";
            r7 = r7.equals(r8);
            if (r7 != 0) goto L_0x001e;
        L_0x04e1:
            r7 = "BatteryInfoShow";
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r9 = "  elec:";
            r8 = r8.append(r9);
            r8 = r8.append(r5);
            r9 = " capacity: ";
            r8 = r8.append(r9);
            r8 = r8.append(r1);
            r8 = r8.toString();
            com.android.engineeringmode.Log.e(r7, r8);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mHander;
            r8 = 2;
            r6 = r7.obtainMessage(r8);
            r7 = "/sys/class/power_supply/battery/temp";
            r4 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r7);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mLastTemp;
            r7 = r4 - r7;
            r8 = -20;
            if (r7 > r8) goto L_0x054f;
        L_0x0524:
            r7 = 0;
            r6.arg1 = r7;
            r7 = "BatteryInfoShow";
            r8 = "test fail";
            com.android.engineeringmode.Log.e(r7, r8);
        L_0x0530:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mHander;
            r7.sendMessage(r6);
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = 0;
            r7.mShowResult = r8;
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.isInModelTest;
            if (r7 != 0) goto L_0x001e;
        L_0x0547:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r8 = 1;
            r7.mHasShowResult = r8;
            goto L_0x001e;
        L_0x054f:
            r7 = com.android.engineeringmode.manualtest.BatteryInfoShow.this;
            r7 = r7.mIsOppoFind7;
            if (r7 == 0) goto L_0x057b;
        L_0x0557:
            r7 = "/sys/class/power_supply/battery/authenticate";
            r0 = com.android.engineeringmode.manualtest.BatteryInfoShow.readFileByLines(r7);
            r6.arg1 = r0;
            r7 = "BatteryInfoShow";
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r9 = "test res:";
            r8 = r8.append(r9);
            r8 = r8.append(r0);
            r8 = r8.toString();
            com.android.engineeringmode.Log.e(r7, r8);
            goto L_0x0530;
        L_0x057b:
            r7 = 1;
            r6.arg1 = r7;
            r7 = "BatteryInfoShow";
            r8 = "test success";
            com.android.engineeringmode.Log.e(r7, r8);
            goto L_0x0530;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.engineeringmode.manualtest.BatteryInfoShow.2.handleMessage(android.os.Message):void");
        }
    };
    private boolean mHasShowResult = false;
    private boolean mIsFastCharger = false;
    private boolean mIsFastChargerSupported = false;
    private boolean mIsOppoFind7 = true;
    private int mLastTemp = 0;
    private TextView mMagicNumber;
    private RelativeLayout mMagicNumberLayout;
    private TextView mResult;
    private boolean mShowResult = false;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private float nTemp = 0.0f;
    private int plugType = 0;
    private int skipNum = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903062);
        if (getIntent().getAction().equals("com.android.engineeringmode.manualtest.BatteryInfoShow.FastCharger")) {
            this.charger_mode = "fast_charger";
        } else if (getIntent().getAction().equals("com.android.engineeringmode.manualtest.BatteryInfoShow.NormalCharger")) {
            this.charger_mode = "normal_charger";
        } else {
            Log.i("BatteryInfoShow", "com.android.engineeringmode.manualtest.BatteryInfoShow");
            if (this.mIsFastChargerSupported) {
                this.charger_mode = "fast_charger";
            } else {
                this.charger_mode = "normal_charger";
            }
        }
        if (this.charger_mode.equals("normal_charger")) {
            setTitle(2131296747);
        } else {
            setTitle(2131296748);
        }
        this.mIsFastChargerSupported = Feature.isVOOCFastchagerSupported(this);
        this.mBatteryCapShow = (TextView) findViewById(2131492940);
        this.mBatteryVolShow = (TextView) findViewById(2131492942);
        this.mBatteryTempShow = (TextView) findViewById(2131492943);
        this.mChargerVolShow = (TextView) findViewById(2131492947);
        this.mChargerElecShow = (TextView) findViewById(2131492951);
        this.mChargerTypeShow = (TextView) findViewById(2131492949);
        this.mChargerStatusShow = (TextView) findViewById(2131492945);
        this.mBatteryTypeInfo = (TextView) findViewById(2131492954);
        this.mBatteryTypeShow = (TextView) findViewById(2131492955);
        this.mFastchargerType = (TextView) findViewById(2131492952);
        this.mFastchargerShow = (TextView) findViewById(2131492953);
        if (!this.mIsFastChargerSupported || this.charger_mode.equals("normal_charger")) {
            this.mFastchargerType.setVisibility(8);
            this.mFastchargerShow.setVisibility(8);
        }
        this.mMagicNumberLayout = (RelativeLayout) findViewById(2131492957);
        this.mMagicNumber = (TextView) findViewById(2131492959);
        this.mBatteryCapShow.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == 1) {
                    Log.d("BatteryInfoShow", "onTouch()");
                    if (BatteryInfoShow.this.isInModelTest || BatteryInfoShow.this.clickCount <= 5) {
                        BatteryInfoShow batteryInfoShow = BatteryInfoShow.this;
                        batteryInfoShow.clickCount = batteryInfoShow.clickCount + 1;
                    }
                }
                return true;
            }
        });
        this.mResult = (TextView) findViewById(2131492956);
        this.mIsOppoFind7 = !Utils.isOnePlus();
        this.mBatteryFilter = new IntentFilter();
        this.mBatteryFilter.addAction("android.intent.action.BATTERY_CHANGED");
        this.mBatteryFilter.addAction("android.intent.action.ADDITIONAL_BATTERY_CHANGED");
        this.mAuthiticate = isFileExist("/sys/class/power_supply/battery/authenticate");
        this.isInModelTest = getIntent().getBooleanExtra("model_test", false);
    }

    private boolean isFileExist(String fileName) {
        return new File(fileName).exists();
    }

    private String readStringFromFile(String fileName) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        String tempString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                tempString = reader.readLine();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("BatteryInfoShow", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("BatteryInfoShow", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("BatteryInfoShow", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    return tempString;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("BatteryInfoShow", "readFileByLines io close exception :" + e122.getMessage());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = reader;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            Log.e("BatteryInfoShow", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            return tempString;
        }
        return tempString;
    }

    public static int readFileByLines(String fileName) {
        IOException e;
        Throwable th;
        BufferedReader bufferedReader = null;
        int result = 0;
        String tempString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            try {
                tempString = reader.readLine();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        Log.e("BatteryInfoShow", "readFileByLines io close exception :" + e1.getMessage());
                    }
                }
                bufferedReader = reader;
            } catch (IOException e2) {
                e = e2;
                bufferedReader = reader;
                try {
                    Log.e("BatteryInfoShow", "readFileByLines io exception:" + e.getMessage());
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            Log.e("BatteryInfoShow", "readFileByLines io close exception :" + e12.getMessage());
                        }
                    }
                    try {
                        result = Integer.valueOf(tempString).intValue();
                    } catch (NumberFormatException e3) {
                        Log.e("BatteryInfoShow", "readFileByLines NumberFormatException:" + e3.getMessage());
                    }
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e122) {
                            Log.e("BatteryInfoShow", "readFileByLines io close exception :" + e122.getMessage());
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = reader;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e = e4;
            Log.e("BatteryInfoShow", "readFileByLines io exception:" + e.getMessage());
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            result = Integer.valueOf(tempString).intValue();
            return result;
        }
        if (!(tempString == null || "".equals(tempString))) {
            result = Integer.valueOf(tempString).intValue();
        }
        return result;
    }

    public void onResume() {
        registerReceiver(this.mBatteryStatusReceiver, this.mBatteryFilter);
        this.mHander.removeMessages(1);
        this.mHander.sendEmptyMessageDelayed(1, 4000);
        this.mResult.setVisibility(4);
        this.mShowResult = false;
        this.mHasShowResult = false;
        if (this.mTimer == null) {
            this.mTimer = new Timer();
        }
        if (this.mTimerTask == null) {
            this.mTimerTask = new TimerTask() {
                public void run() {
                    BatteryInfoShow.this.mHander.sendEmptyMessage(3);
                }
            };
        }
        if (!(this.mTimer == null || this.mTimerTask == null)) {
            this.mTimer.schedule(this.mTimerTask, 0, 500);
        }
        super.onResume();
    }

    public void onPause() {
        this.mHander.removeMessages(1);
        unregisterReceiver(this.mBatteryStatusReceiver);
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
        if (this.mTimerTask != null) {
            this.mTimerTask.cancel();
            this.mTimerTask = null;
        }
        super.onPause();
    }

    public void onStop() {
        super.onStop();
        this.clickCount = 0;
        this.mMagicNumberLayout.setVisibility(8);
    }

    public void onDestroy() {
        Calendar now = Calendar.getInstance();
        String content = (now.get(1) + "-" + (now.get(2) + 1) + "-" + now.get(5) + "-" + now.get(11) + "-" + now.get(12) + "-" + now.get(13)) + "--BatteryInfoShow--" + "has entered it";
        this.mHander.removeMessages(1);
        this.mHander = null;
        super.onDestroy();
    }
}
