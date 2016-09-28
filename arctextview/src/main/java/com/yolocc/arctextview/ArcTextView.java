package com.yolocc.arctextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 */

public class ArcTextView extends TextView {

    private final int default_background_color = Color.WHITE;
    private final int default_border_color = Color.WHITE;
    private final float default_border_stroke_width;

    private RectF mLeftCornerRectF = new RectF();

    private RectF mRightCornerRectF = new RectF();

    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Path borderPath = new Path();

    private RectF mHorizontalBlankFillRectF = new RectF();

    private RectF mVerticalBlankFillRectF = new RectF();

    // 是否有边框
    private boolean isBorder = true;

    private int borderStrokeWidth;

    // 垂直 水平内边距
    private int horizontalPadding, verticalPadding;

    // 背景颜色
    private int backgroundColor;
    // 边框背景颜色
    private int borderColor;

    public ArcTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        horizontalPadding = (int) dp2px(12.0f);
        verticalPadding = (int) dp2px(3.0f);
        default_border_stroke_width = dp2px(1.0f);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcTextView);
        backgroundColor = typedArray.getColor(R.styleable.ArcTextView_arc_background_color, default_background_color);
        borderColor = typedArray.getColor(R.styleable.ArcTextView_arc_border_color, default_border_color);
        borderStrokeWidth = (int) typedArray.getDimension(R.styleable.ArcTextView_arc_border_width, default_border_stroke_width);
        borderStrokeWidth = borderStrokeWidth > verticalPadding ? verticalPadding : borderStrokeWidth;
        isBorder = typedArray.getBoolean(R.styleable.ArcTextView_border, true);
        typedArray.recycle();

        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER);
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);

        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderStrokeWidth);
        borderPaint.setStyle(Paint.Style.STROKE);

        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(mLeftCornerRectF, -180, 90, true, backgroundPaint);
        canvas.drawArc(mLeftCornerRectF, 90, 180, true, backgroundPaint);
        canvas.drawArc(mRightCornerRectF, 0, 90, true, backgroundPaint);
        canvas.drawArc(mRightCornerRectF, 0, -90, true, backgroundPaint);
        canvas.drawRect(mHorizontalBlankFillRectF, backgroundPaint);
        canvas.drawRect(mVerticalBlankFillRectF, backgroundPaint);
        if (isBorder) {
            canvas.drawPath(borderPath, borderPaint);
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int left, top, right, bottom;
        if (isBorder) {
            left = borderStrokeWidth;
            top = borderStrokeWidth;
            right = (left + w - borderStrokeWidth * 2);
            bottom = (top + h - borderStrokeWidth * 2);
        } else {
            left = 0;
            top = 0;
            right = (left + w);
            bottom = (top + h);
        }

        int rectFWidth = bottom - top;
        int l = (int) (rectFWidth / 2.0f);
        mLeftCornerRectF.set(left, top, left + rectFWidth, top + rectFWidth);
        mRightCornerRectF.set(right - rectFWidth, top, right, top + rectFWidth);

        if (isBorder) {
            borderPath.reset();
            borderPath.addArc(mLeftCornerRectF, -180, 90);
            borderPath.addArc(mLeftCornerRectF, 90, 180);
            borderPath.addArc(mRightCornerRectF, 0, 90);
            borderPath.addArc(mRightCornerRectF, 0, -90);

            borderPath.moveTo(left + l, top);
            borderPath.lineTo(right - l, top);
            borderPath.moveTo(left + l, bottom);
            borderPath.lineTo(right - l, bottom);
        }

        mHorizontalBlankFillRectF.set(left, top + l, right, bottom - l);
        mVerticalBlankFillRectF.set(left + l, top, right - l, bottom);

    }

    /**
     * 设置边框的颜色
     *
     * @param color 颜色值
     */
    public void setBorderColor(@ColorInt int color) {
        if(color == borderColor) {
            return;
        }
        borderColor = color;
        borderPaint.setColor(borderColor);
        invalidate();
    }

    /**
     * 设置背景颜色
     *
     * @param color 颜色值
     */
    public void setArcBackgroundColor(@ColorInt int color) {
        if(color == backgroundColor) {
            return;
        }
        backgroundColor = color;
        backgroundPaint.setColor(backgroundColor);
        invalidate();
    }

    /**
     * 设置是否要边框
     *
     * @param isBorder boolean
     */
    public void setBorder(boolean isBorder) {
        if(this.isBorder == isBorder) {
            return;
        } else {
            this.isBorder = isBorder;
            invalidate();
        }
    }

    public float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
