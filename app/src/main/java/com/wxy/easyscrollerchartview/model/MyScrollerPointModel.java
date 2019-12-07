package com.wxy.easyscrollerchartview.model;


import android.os.Parcel;

import com.wxy.easyscrollerchartviewlibrary.model.ScrollerPointModel;

public class MyScrollerPointModel extends ScrollerPointModel {
    public MyScrollerPointModel(long x, long y) {
        super(x, y);
    }
    private String name;

    public MyScrollerPointModel(long x, long y, String name) {
        super(x, y);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
    }

    protected MyScrollerPointModel(Parcel in) {
        super(in);
        this.name = in.readString();
    }

    public static final Creator<MyScrollerPointModel> CREATOR = new Creator<MyScrollerPointModel>() {
        @Override
        public MyScrollerPointModel createFromParcel(Parcel source) {
            return new MyScrollerPointModel(source);
        }

        @Override
        public MyScrollerPointModel[] newArray(int size) {
            return new MyScrollerPointModel[size];
        }
    };
}
