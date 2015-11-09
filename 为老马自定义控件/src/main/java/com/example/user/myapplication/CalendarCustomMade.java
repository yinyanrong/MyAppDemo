package com.example.user.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by user on 2015/11/9.
 */
public class CalendarCustomMade extends View {

    private  String  TAG="DEMO";
    /**
     *左边的文字颜色
     */
    private  int mLeftTextColor;
    /**
     * 左边的文字的大小
     */
    private  int mLeftTextSize;

    /**
     *左边的文字画笔
     */
    private Paint  mLeftPaint;

    /**
     *服务开始时间
     */
    private int startTime=6;
    /**
     * 服务结束时间
     */
    private int  endTime=8;

    /**
     *月份显示文本
     */
    private String  monthStr="月份";
    /**
     * 月份的Rect
     */
   private   Rect mRectM;

    /**
     * 横线的Rect
     */
    private   Rect mRectL;

    /**
     *灰色的原点的半径
     */
    private int  circleRadius=10;

    /**
     * 控制一行显示的数量
     *
     */
    private int  showNumber=8;
    public CalendarCustomMade(Context context) {
       this(context,null);
    }

    public CalendarCustomMade(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarCustomMade(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       TypedArray array= context.getTheme().obtainStyledAttributes(attrs,R.styleable.CalendarCustomMade,defStyleAttr,0);
       int count= array.getIndexCount();
        for (int i=0;i<count ;i++){
          int  dex=array.getIndex(i);
            switch (dex){
                case  R.styleable.CalendarCustomMade_left_text_clolr:
                    mLeftTextColor= array.getColor(i, Color.BLACK);
                break;
                case R.styleable.CalendarCustomMade_left_text_size:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mLeftTextSize = array.getDimensionPixelSize(dex, (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    float  x=getResources().getDisplayMetrics().widthPixels;
                    float  y=getResources().getDisplayMetrics().heightPixels;

                    break;
            }
        }
        array.recycle();
        mLeftPaint=new Paint();
        mLeftPaint.setTextSize(mLeftTextSize);
        mRectL=new Rect();
        mRectM =new Rect();
        mLeftPaint.getTextBounds(monthStr,0,monthStr.length(), mRectM);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       int  widthMode= MeasureSpec.getMode(widthMeasureSpec);
       int  width=MeasureSpec.getSize(widthMeasureSpec);
       int  heightMode= MeasureSpec.getMode(widthMeasureSpec);
       int  height=MeasureSpec.getSize(widthMeasureSpec);
        if(widthMode!=MeasureSpec.EXACTLY){
            width=getPaddingLeft()+getPaddingRight()+ mRectM.width();
        }
        if(heightMode!=MeasureSpec.EXACTLY){
            height=getPaddingTop()+getPaddingBottom()+ mRectM.height();
        }
        setMeasuredDimension(width,50);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mLeftPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mLeftPaint);
        mLeftPaint.setColor(Color.RED);
        Log.i(TAG, "getWidth():" + getWidth() + "  getHeight():" + getHeight() + "getMeasuredWidth():" + getMeasuredWidth() + "getMeasuredHeight():" + getMeasuredHeight());
        canvas.drawText(monthStr, getWidth() / 2 - mRectM.width() / 2, getHeight() / 2 + mRectM.height() / 2, mLeftPaint);
        mLeftPaint.setColor(Color.GRAY);
        int   sunCircleRadius=circleRadius;
        int  ss=getWidth()/showNumber;//
        for (int  i=0;i<showNumber;i++){
            canvas.drawCircle(sunCircleRadius,getHeight()/2,circleRadius,mLeftPaint);
            Log.i(TAG, "----i--" + sunCircleRadius);
            sunCircleRadius=+ss;
        }
    }
}
