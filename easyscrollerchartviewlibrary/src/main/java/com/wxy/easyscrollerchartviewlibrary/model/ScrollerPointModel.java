package com.wxy.easyscrollerchartviewlibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class ScrollerPointModel implements Parcelable {
    protected long x;
    protected long y;

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    public ScrollerPointModel(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.x);
        dest.writeLong(this.y);
    }
    protected ScrollerPointModel(Parcel in) {
        this.x = in.readLong();
        this.y = in.readLong();
    }

}
