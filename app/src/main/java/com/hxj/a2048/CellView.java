package com.hxj.a2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

/**
 *  单元格.
 */
public class CellView extends View {

    public static final String TAG = "CellView";

    private String mNumber = "0";

    private TextPaint mTextPaint;

    private Rect bounds = new Rect();

    private Paint mPaint;

    public CellView(Context context) {
        super(context);

        initPaint();
    }

    private void initPaint() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(40);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.getTextBounds(mNumber, 0, mNumber.length(), bounds);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#E4CDCD"));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(130, 130);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        if (!mNumber.equalsIgnoreCase("0")) {
            mTextPaint.setColor(Color.parseColor("#E451CD"));
            canvas.drawText(mNumber,
                    (float) (getMeasuredWidth() - bounds.width()) / 2,
                    (float) (getMeasuredHeight() / 2 + bounds.height() / 2), mTextPaint);
        } else {
            mTextPaint.setColor(Color.parseColor("#E4CDCD"));
            canvas.drawText(mNumber,
                    (float) (getMeasuredWidth() - bounds.width()) / 2,
                    (float) (getMeasuredHeight() / 2 + bounds.height() / 2), mTextPaint);
        }
    }

    public void setNumber(int number) {
        if (number == 0) {
            this.mNumber = "0";
        } else {
            this.mNumber = number + "";
        }

        mTextPaint.getTextBounds(mNumber, 0, mNumber.length(), bounds);
        postInvalidate();
    }

}
