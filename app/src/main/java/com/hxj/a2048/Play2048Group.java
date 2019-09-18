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
        MarginLayoutParams layoutParams = (MarginLayoutParams) getChildAt(0).getLayoutParams();
        int leftMargin = layoutParams.leftMargin;
        int topMargin = layoutParams.topMargin;

        for (int i = 0; i < count; i++) {
            CellView cellView = (CellView) getChildAt(i);
            cellView.measure(widthMeasureSpec, heightMeasureSpec);

            int childW = cellView.getMeasuredWidth();
            int childH = cellView.getMeasuredHeight();

            width += childW;
            height += childH;
        }

        setMeasuredDimension(width / 4 + (mRow + 1) * leftMargin,
                height / 4 + (mColumn + 1) * topMargin);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            CellView cellView = (CellView) getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) cellView.getLayoutParams();
            int leftMargin = layoutParams.leftMargin;
            int topMargin = layoutParams.topMargin;

            int width = cellView.getMeasuredWidth();
            int height = cellView.getMeasuredHeight();

            int left = 0, top = 0, right = 0, bottom = 0;

            int temp = mRow * (i / mRow);
            if (temp == 0) {
                temp = mRow;
            }

            if (i - temp < 0) {
                temp = 0;
            }
            if (i == mRow * (i / mRow)) {
                left = leftMargin;
                right = width + leftMargin;
            } else {
                left = leftMargin * (i - temp + 1) + width * (i - temp);
                right = leftMargin * (i - temp + 1) + + width * (i - temp + 1);
            }

            int row = i / mRow;
            if (row == 0) {
                top = topMargin;
                bottom = height + topMargin;
            } else {
                top = height * row + topMargin * row + topMargin;
                bottom = height * (row + 1) + (row + 1) * topMargin;
            }

            //bottom = height * (temp + 1) + topMargin * (i % mRow + i + 1);

            Log.i(TAG, "onLayout left: " + left + " top: " + top
            + " right: " + right + " bottom: " + bottom);
            // -----------
            /*if (i < mRow) {
               if (i == 0) {
                   left = leftMargin;
                   right = leftMargin + width;
               } else {
                   left = leftMargin * (i + 1) + width * i;
                   right = leftMargin * (i + 1) + width * (i + 1);
               }

               top = topMargin;
               bottom = height + topMargin;

            } else if (i < 2 * mRow) {
                if (i == mRow) {
                    left = leftMargin;
                    right = leftMargin + width;
                } else {
                    left = leftMargin * (i - 4 + 1) + width * (i - 4);
                    right = leftMargin * (i - 4 + 1) + width * (i - 4 + 1);
                }

                top = height + topMargin * 2;
                bottom = height * 2 + topMargin * 2;
            } else if (i < 3 * mRow) {
                if (i == 2 * mRow) {
                    left = leftMargin;
                    right = leftMargin + width;
                } else {
                    left = leftMargin * (i - 8 + 1) + width * (i - 8);
                    right = leftMargin * (i - 8 + 1) + width * (i - 8 + 1);
                }

                top = height * 2 + topMargin * 3;
                bottom = height * 3 + topMargin * 3;
            } else {
                if (i == 3 * mRow) {
                    left = leftMargin;
                    right = leftMargin + width;
                } else {
                    left = leftMargin * (i - 12 + 1) + width * (i - 12);
                    right = leftMargin * (i - 12 + 1) + width * (i - 12 + 1);
                }

                top = height * 3 + topMargin * 4;
                bottom = height * 4 + topMargin * 4;
            }*/

            cellView.layout(left, top, right, bottom);
        }
    }
}
