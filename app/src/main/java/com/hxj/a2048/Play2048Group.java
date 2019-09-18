package com.hxj.a2048;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 *  存储所有单元 cellView.
 */
public class Play2048Group extends ViewGroup {

    public static final String TAG = "Play2048Group";

    private int mColumn;

    private int mRow;

    /**
     *  单元格矩阵.
     */
    private Model[][] models;

    private List<Model> cells;

    /**
     *  记录空格个数.
     */
    private int mEmptyCells;

    private int oldX;

    private int oldY;

    public Play2048Group(Context context) {
        this(context, null);
    }

    public Play2048Group(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Play2048Group(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Play2048Group);

        try {
            mRow = a.getInteger(R.styleable.Play2048Group_row, 4);
            mColumn = a.getInteger(R.styleable.Play2048Group_column, 4);

            // 保持长宽相等排列, 取传入的最大值
            if (mRow > mColumn) {
                mColumn = mRow;
            } else {
                mRow = mColumn;
            }

            init();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }
    }

    private void init() {
        models = new Model[mRow][mColumn];
        cells = new ArrayList<>(mRow * mColumn);

        for (int i = 0; i < mRow * mColumn; i++) {
            CellView cellView = new CellView(getContext());
            MarginLayoutParams params = new MarginLayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            params.leftMargin = 10;
            params.topMargin = 10;
            cellView.setLayoutParams(params);
            Model model = new Model(0, cellView);
            cells.add(model);
            addView(cellView, i);
        }

        mEmptyCells = mRow * mColumn - 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            CellView cellView = (CellView) getChildAt(i);
            cellView.measure(widthMeasureSpec, heightMeasureSpec);

            int childW = cellView.getMeasuredWidth();
            int childH = cellView.getMeasuredHeight();

            width += childW;
            height += childH;
        }

        Log.i(TAG, "onMeasure width: " + width + " height: " + height);
        setMeasuredDimension(width / 4, height / 4);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        Log.i(TAG, "onLayout count: " + count);
        for (int i = 0; i < count; i++) {
            CellView cellView = (CellView) getChildAt(i);

            int width = cellView.getMeasuredWidth();
            int height = cellView.getMeasuredHeight();

            int left = 0, top = 0, right = 0, bottom = 0;
            if (i < 4) {
               if (i == 0) {
                   left = 0;
                   right = width;
               } else {
                   left = width * i;
                   right = width * (i + 1);
               }

               top = 0;
               bottom = height;

            } else if (i < 8) {
                if (i == 4) {
                    left = 0;
                    right = width;
                } else {
                    left = width * (i - 4);
                    right = width + left;
                }

                top = height;
                bottom = height * 2;
            } else if (i < 12) {
                if (i == 8) {
                    left = 0;
                    right = width;
                } else {
                    left = width * (i - 8);
                    right = width + left;
                }

                top = height * 2;
                bottom = height * 3;
            } else {
                if (i == 12) {
                    left = 0;
                    right = width;
                } else {
                    left = width * (i - 12);
                    right = width + left;
                }

                top = height * 3;
                bottom = height * 4;
            }

            Log.i(TAG, "onLayout 1 - 4:  " + i +  " left:" + left
                    + " top: " + top + " right: " + right + " bottom: " + bottom);
            cellView.layout(left, top, right, bottom);
        }
    }
}
