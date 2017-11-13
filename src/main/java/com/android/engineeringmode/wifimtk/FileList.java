package com.android.engineeringmode.wifimtk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.engineeringmode.functions.Light;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileList extends Activity {
    private final int DELETE_FILE_MENU = 1;
    private final String TAG = "EM_WIFI_FileList";
    private final String filePath = "/data/data/com.mediatek.engineermode/myData";
    private List<String> items = null;
    private ListView mFileList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903094);
        this.mFileList = (ListView) findViewById(2131493082);
        registerForContextMenu(this.mFileList);
        this.items = new ArrayList();
        File fileName = new File("/data/data/com.mediatek.engineermode/myData");
        if (fileName.exists()) {
            File[] files = fileName.listFiles();
            for (File name : files) {
                this.items.add(name.getName());
            }
            this.mFileList.setAdapter(new ArrayAdapter(this, 2130903203, this.items));
            this.mFileList.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                    String name = FileList.this.mFileList.getItemAtPosition(arg2).toString();
                    Bundle b = new Bundle();
                    b.putString("FILENAME", name);
                    Intent intent = new Intent();
                    intent.putExtras(b);
                    FileList.this.setResult(-1, intent);
                    FileList.this.finish();
                }
            });
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Delete");
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Light.MAIN_KEY_LIGHT /*1*/:
                new File("/data/data/com.mediatek.engineermode/myData/" + ((String) this.items.get(((AdapterContextMenuInfo) item.getMenuInfo()).position))).delete();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
