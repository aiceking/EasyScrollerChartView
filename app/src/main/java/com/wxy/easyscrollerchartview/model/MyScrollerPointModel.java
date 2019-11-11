package com.wxy.easyscrollerchartview.model;

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
}
