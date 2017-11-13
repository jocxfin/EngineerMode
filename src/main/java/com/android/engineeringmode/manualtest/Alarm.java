package com.android.engineeringmode.manualtest;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class Alarm implements Parcelable {
    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        public Alarm createFromParcel(Parcel p) {
            return new Alarm(p);
        }

        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };
    public Uri alert;
    public int alertType;
    public DaysOfWeek daysOfWeek;
    public boolean enabled;
    public int hour;
    public int id;
    public String label;
    public int minutes;
    public boolean silent;
    public int soonzeItem;
    public long time;
    public int volume;

    static final class DaysOfWeek {
        private int mDays;

        DaysOfWeek(int days) {
            this.mDays = days;
        }

        public int getCoded() {
            return this.mDays;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel p, int flags) {
        int i;
        int i2 = 1;
        p.writeInt(this.id);
        if (this.enabled) {
            i = 1;
        } else {
            i = 0;
        }
        p.writeInt(i);
        p.writeInt(this.hour);
        p.writeInt(this.minutes);
        p.writeInt(this.daysOfWeek.getCoded());
        p.writeLong(this.time);
        p.writeInt(this.alertType);
        p.writeString(this.label);
        p.writeParcelable(this.alert, flags);
        if (!this.silent) {
            i2 = 0;
        }
        p.writeInt(i2);
        p.writeInt(this.soonzeItem);
        p.writeInt(this.volume);
    }

    public Alarm(Parcel p) {
        boolean z;
        boolean z2 = true;
        this.id = p.readInt();
        if (p.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.enabled = z;
        this.hour = p.readInt();
        this.minutes = p.readInt();
        this.daysOfWeek = new DaysOfWeek(p.readInt());
        this.time = p.readLong();
        this.alertType = p.readInt();
        this.label = p.readString();
        this.alert = (Uri) p.readParcelable(null);
        if (p.readInt() != 1) {
            z2 = false;
        }
        this.silent = z2;
        this.soonzeItem = p.readInt();
        this.volume = p.readInt();
    }
}
