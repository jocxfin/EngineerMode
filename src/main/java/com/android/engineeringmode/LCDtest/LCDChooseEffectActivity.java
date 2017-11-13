package com.android.engineeringmode.LCDtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;

import com.android.engineeringmode.functions.Light;

public class LCDChooseEffectActivity extends Activity {
    private String file_name = "/persist/gamma_node";
    private String node_path = "/sys/devices/virtual/graphics/fb0/gamma";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903135);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 2, 1, getString(2131296782));
        menu.add(0, 3, 2, getString(2131296783));
        menu.add(0, 4, 3, getString(2131296784));
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int value = 0;
        switch (item.getItemId()) {
            case Light.CHARGE_RED_LIGHT /*2*/:
                value = 1;
                break;
            case Light.CHARGE_GREEN_LIGHT /*3*/:
                value = 2;
                break;
            case 4:
                value = 3;
                break;
        }
        try {
            Object oRemoteService = Class.forName("android.os.ServiceManager").getMethod("getService", new Class[]{String.class}).invoke(null, new Object[]{"OEMExService"});
            Object oIOemExService = Class.forName("com.oem.os.IOemExService$Stub").getMethod("asInterface", new Class[]{IBinder.class}).invoke(null, new Object[]{oRemoteService});
            oIOemExService.getClass().getMethod("setGammaData", new Class[]{Integer.TYPE}).invoke(oIOemExService, new Object[]{Integer.valueOf(value)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
