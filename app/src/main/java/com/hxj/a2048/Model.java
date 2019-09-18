package com.hxj.a2048;

import android.graphics.PointF;
import android.view.Display;

public class Model {

    private int number;

    /**
     *  存储单元格的坐标
     */
    private PointF pointF;

    /**
     *  单元格试图.
     */
    private CellView cellView;

    public Model(int number, CellView cellView) {
        this.number = number;
        this.cellView = cellView;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public PointF getPointF() {
        return pointF;
    }

    public void setPointF(PointF pointF) {
        this.pointF = pointF;
    }

    public CellView getCellView() {
        return cellView;
    }

    public void setCellView(CellView cellView) {
        this.cellView = cellView;
    }
}
