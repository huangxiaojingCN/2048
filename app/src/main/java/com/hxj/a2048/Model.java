package com.hxj.a2048;


public class Model {

    private int number;
    /**
     *  单元格视图.
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

    public CellView getCellView() {
        return cellView;
    }

    public void setCellView(CellView cellView) {
        this.cellView = cellView;
    }
}
