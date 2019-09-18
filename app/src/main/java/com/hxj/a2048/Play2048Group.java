package com.hxj.a2048;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 *  存储所有单元 cellView.
 */
public class Play2048Group extends ViewGroup {

    public static final String TAG = "Play2048Group";

    private int mColumn;

    private int mRow;

    private Random mRandom;

    /**
     *  单元格矩阵.
     */
    private Model[][] models;

    private List<Model> cells;

    /**
     *  记录空格个数.
     */
    private int mEmptyCells;

    private int mAllCells;

    private int mCanMove;

    private int rand;

    private int oldX;

    private int oldY;

    private int cancelX;

    private int cancelY;

    private int x;

    private int y;

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

        int i = 0;
        for (int x = 0; x < mRow; x++) {
            for (int y = 0; y < mColumn; y++) {
                models[x][y] = cells.get(i);
                i++;
            }
        }

        // 生成一个随机数，初始化数据.
        mRandom = new Random();
        rand = mRandom.nextInt(mRow * mColumn - 1);
        Model model = cells.get(rand);
        model.setNumber(2);
        CellView cellView = model.getCellView();
        cellView.setNumber(2);

        mAllCells = mRow * mColumn - 1;
        mEmptyCells = mAllCells;
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

        setMeasuredDimension(width / mRow + (mRow + 1) * leftMargin,
                height / mRow + (mColumn + 1) * topMargin);
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

            // 每一行的最后一个索引, 0, 4, 8, 12...
            int temp = mRow * (i / mRow);
            // 每一行的开头位置.
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

            cellView.layout(left, top, right, bottom);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                cancelX = (int) event.getX();
                cancelY = (int) event.getY();
               break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int x = (int) event.getX();
                int y = (int) event.getY();

                // 向右滑动.
                if ((x - cancelX) >= 50 && (y - cancelY) < 50 && (y - cancelY) > -50) {
                    right();
                } else if (x - cancelX <= -50 && (y - cancelY) < 50 && (y - cancelY) > -50) { // 向左滑动
                    left();
                }

                // 向下
                if (y - cancelY >= 50 && (x - cancelX) > -50 && (x - cancelX) < 50) {
                    lower();
                } else if (y - cancelY <= -50 && (x - cancelX) > -50 && (x - cancelX) < 50) { // 向上
                    up();
                }

                break;
        }

        return true;
    }

    /**
     *  向上移动
     */
    public void up() {
        int i = 0;

        for (int y = 0; y < mColumn; y++) {
            for (int x = 0; x < mRow; ) {
                if (models[x][y].getNumber() == 0) {
                    x++;
                    continue;
                } else {
                    for(i = x + 1; i < mRow; i++) {
                        if (models[i][y].getNumber() == 0) {
                            continue;
                        } else if (models[i][y].getNumber() == models[x][y].getNumber()) {
                            models[x][y].setNumber(models[i][y].getNumber() + models[x][y].getNumber());
                            models[i][y].setNumber(0);
                            mEmptyCells++;

                            break;
                        } else {
                            break;
                        }
                    }

                    x = i;
                }
            }
        }

        for (int x = 0; x < mRow; x++) {
            for (int y = 0; y < mColumn; y++) {
                if (models[x][y].getNumber() == 0) {
                    continue;
                } else {
                    for (int j = x; j > 0 && models[j - 1][y].getNumber() == 0 ; j--) {
                        models[j-1][y].setNumber(models[j][y].getNumber());
                        models[j][y].setNumber(0);

                        mCanMove = 1;
                    }
                }
            }
        }

        drawAll();
        nextRand();
    }

    /**
     *  向下移动
     */
    public void lower() {
        int i = 0;
        for (int y = 0; y < mColumn; y++) {
            for (int x = mRow - 1; x >= 0;) {
                if (models[x][y].getNumber() == 0) {
                    x--;
                    continue;
                } else {
                    for (i = x - 1;  i >= 0; i--) {
                        if (models[i][y].getNumber() == 0) {
                            continue;
                        } else if (models[i][y].getNumber() == models[x][y].getNumber()) {
                            models[x][y].setNumber(models[i][y].getNumber() + models[x][y].getNumber());
                            models[i][y].setNumber(0);

                            mEmptyCells++;
                            break;
                        } else {
                            break;
                        }
                    }

                    x = i;
                }
            }
        }

        for (int x = 0; x < mRow; x++) {
            for (int y = 0; y < mColumn; y++) {
                if (models[x][y].getNumber() == 0) {
                    continue;
                } else {
                    for (int j = x; j < 3 && models[j + 1][y].getNumber() == 0 ; j++) {
                        models[j+1][y].setNumber(models[j][y].getNumber());
                        models[j][y].setNumber(0);

                        mCanMove = 1;
                    }
                }
            }
        }

        drawAll();
        nextRand();
    }

    /**
     *  向左移动
     */
    public void left() {
        int i;
        for (int x = 0; x < mRow; x++) {
            for (int y = 0; y < mColumn; ) {
                Model model = models[x][y];
                int number = model.getNumber();
                if (number == 0) {
                    y++;
                    continue;
                } else {
                    // 找到不为零的位置. 往后找不为零的数进行运算.
                    for (i = y + 1; i < mColumn; i++) {
                        Model model1 = models[x][i];
                        int number1 = model1.getNumber();
                        if (number1 == 0) {
                            continue;
                        } else if (number == number1){
                            // 如果找到和这个相同的，则进行合并运算（相加）。
                            int temp = number + number1;
                            model.setNumber(temp);
                            model1.setNumber(0);

                            mEmptyCells++;
                            break;
                        } else {
                            break;
                        }
                    }

                    y = i;
                }
            }
        }

        for (int x = 0; x < mRow; x++) {
            for (int y = 0; y < mColumn; y++) {
                Model model = models[x][y];
                int number = model.getNumber();
                if (number == 0) {
                    continue;
                } else {
                    for (int j = y; (j > 0) && models[x][j - 1].getNumber() == 0; j--) {
                        models[x][j - 1].setNumber(models[x][j].getNumber());
                        models[x][j].setNumber(0);

                        mCanMove = 1;
                    }
                }
            }
        }

        drawAll();
        nextRand();
    }

    /**
     *  向右移动
     */
    public void right() {
        int i = 0;
        for (int x = 0; x < mRow; x++) {
            for (int y = mColumn - 1; y >= 0; ) {
                if (models[x][y].getNumber() == 0) {
                    y--;
                    continue;
                } else {
                    for (i = y - 1; i >= 0; i--) {
                        if (models[x][i].getNumber() == 0) {
                            continue;
                        } else if (models[x][i].getNumber() == models[x][y].getNumber()) {
                            models[x][y].setNumber(models[x][i].getNumber() + models[x][y].getNumber());
                            models[x][i].setNumber(0);
                            mEmptyCells++;
                            break;
                        } else {
                            break;
                        }
                    }

                    y = i;
                }
            }
        }

        for (int x = 0; x < mRow; x++) {
            for (int y = mColumn - 1; y >= 0; y--) {
                if (models[x][y].getNumber() == 0) {
                    continue;
                } else {
                    for (int j = y; j < mColumn - 1 && models[x][j + 1].getNumber() == 0; j++) {
                        models[x][j + 1].setNumber(models[x][j].getNumber());
                        models[x][j].setNumber(0);

                        mCanMove = 1;
                    }
                }
            }
        }

        drawAll();
        nextRand();
    }

    private void nextRand() {
        if (mEmptyCells <= 0) {
            gameOver();
            return;
        }

        int newX, newY;

        if (mEmptyCells != mAllCells || mCanMove == 1) {
            do {
                newX = mRandom.nextInt(mRow - 1);
                newY = mRandom.nextInt(mColumn - 1);
            } while (models[newX][newY].getNumber() != 0);

            //calcValue(newX, newY);

            int temp = 0;

            do {
                temp = mRandom.nextInt(mRow - 1);
            } while (temp == 0 || temp == 2);

            Model model = models[newX][newY];
            model.setNumber(temp + 1);
            CellView cellView = model.getCellView();
            cellView.setNumber(model.getNumber());

            mEmptyCells--;
        }
    }

    private void calcValue(int newX, int newY) {
        int i, j, value;
        int max;

        max = getOne(newX, newY);
        for (int x = 0; x < mRow; x++) {
            for (int y = 0; y < mColumn; y++) {
                if (models[x][y].getNumber() != 0) {
                    value = getOne(x, y);
                    if (value > max && oldX != x && oldY != y) {

                    }
                }
            }
        }
    }

    private int getOne(int newX, int newY) {
        int value = 0;
        if (newX -1 > 0) {
            if (models[newX - 1][newY].getNumber() == 0) {
                models[newX - 1][newY].setNumber(0);
            } else {
                models[newX - 1][newY].setNumber(value++);
            }
        }


        return 0;
    }

    private void gameOver() {

    }

    private void drawAll() {
        for (int x = 0; x < mRow; x++) {
            for (int y = 0; y < mColumn; y++) {
                Model model = models[x][y];
                int number = model.getNumber();
                model.getCellView().setNumber(number);
            }
        }
    }
}
