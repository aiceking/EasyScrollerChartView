package com.wxy.easyscrollerchartview.model;

import android.os.Parcel;
import android.os.Parcelable;

public  class ScrollerPointModel implements Parcelable {
    protected float y;
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public ScrollerPointModel(long y) {
        this.y = y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.y);
    }

    protected ScrollerPointModel(Parcel in) {
        this.y = in.readFloat();
    }

    public static final Parcelable.Creator<ScrollerPointModel> CREATOR = new Parcelable.Creator<ScrollerPointModel>() {
        @Override
        public ScrollerPointModel createFromParcel(Parcel source) {
            return new ScrollerPointModel(source);
        }

        @Override
        public ScrollerPointModel[] newArray(int size) {
            return new ScrollerPointModel[size];
        }
    };
}
