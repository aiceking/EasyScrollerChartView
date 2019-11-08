package com.wxy.easyscrollerchartviewlibrary.model;

public abstract class ScrollerPointModel {
    private long x;
    private long y;

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
}
