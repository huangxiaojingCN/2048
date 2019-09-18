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

    private final String number = "2048";

    private TextPaint mTextPaint;

    private Rect bounds;

    private Paint mPaint;

    public CellView(Context context) {
        super(context);

        initPaint();
    }

    private void initPaint() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#E451CD"));
        mTextPaint.setTextSize(30);
        mTextPaint.setFakeBoldText(true);
        bounds = new Rect();
        mTextPaint.getTextBounds(number, 0, number.length(), bounds);

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

        Log.i(TAG, "onDraw: " + getMeasuredWidth() + " height: " + getMeasuredHeight());

        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        canvas.drawText(number,
                (float) (getMeasuredWidth() - bounds.width()) / 2,
                (float) (getMeasuredHeight() / 2 + bounds.height() / 2), mTextPaint);
    }
}
