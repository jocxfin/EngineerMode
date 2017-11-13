package com.android.engineeringmode.manualtest.modeltest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

public class ModelTestManager {
    private int index = -1;
    private List<Intent> intents;
    private ModelTestParser parser;
    private List<String> titles;

    public ModelTestManager(Context context, String intentListFile) {
        this.parser = new ModelTestParser(context, intentListFile);
        this.parser.parse();
        this.intents = this.parser.getIntentList();
        this.titles = this.parser.getTitleList();
        Log.d("ModelTestManager", "SIZE : " + this.intents.size());
    }

    public Intent getCurrentIntent() {
        Log.d("ModelTestManager", "getCurrentIntent index: " + this.index);
        if (this.index < 0 || this.index >= this.intents.size()) {
            return null;
        }
        return (Intent) this.intents.get(this.index);
    }

    public int getCurrentIndex() {
        return this.index;
    }

    public void setCurrentIndex(int num) {
        this.index = num;
    }

    public Intent getNextIntent() {
        Log.d("ModelTestManager", "getNextIntent index: " + this.index);
        if (!hasNextIntent()) {
            return null;
        }
        this.index++;
        return getCurrentIntent();
    }

    public String getCurrentTitle() {
        Log.d("ModelTestManager", "getCurrentTitle index: " + this.index);
        if (this.index < 0 || this.index >= this.intents.size()) {
            return null;
        }
        return (String) this.titles.get(this.index);
    }

    public boolean hasNextIntent() {
        Log.d("ModelTestManager", "hasNextIntent index: " + this.index);
        return this.index < this.intents.size() + -1;
    }

    public void remove(String action) {
        for (int i = 0; i < this.intents.size(); i++) {
            if (((Intent) this.intents.get(i)).getAction().equals(action)) {
                this.intents.remove(i);
                this.titles.remove(i);
                return;
            }
        }
    }
}
