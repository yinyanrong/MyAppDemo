package com.example.user.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by user on 2015/11/9.
 */
public class ColorRectBar extends View {

    private  String  TAG="DEMO";
    /**
     * 颜色画笔
     */
    private Paint colorPaint;

    /**
     *灰色的原点的半径
     */
    private int  circleRadius=7;

    /**
     * 控制一行显示的数量
     *
     */
    private int  showNumber=8;

    /**
     *控制绿色的 条
     */
    private int greenLine=2;

    /**
     *控制红色的条
     */
    private  int   redLine=3;

    /**
     *彩条的高度
     */
    private int rectHeight;

    /**
     *彩条对象
     */
    private Rect   colorRect;

    public ColorRectBar(Context context) {
       this(context,null);
    }

    public ColorRectBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorRectBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        colorPaint =new Paint();
        rectHeight=dip2px(context,20);
        colorRect=new Rect();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       int  widthMode= MeasureSpec.getMode(widthMeasureSpec);
       int  width=MeasureSpec.getSize(widthMeasureSpec);
       int  heightMode= MeasureSpec.getMode(widthMeasureSpec);
       int  height=MeasureSpec.getSize(widthMeasureSpec);
        if(widthMode!=MeasureSpec.EXACTLY){
            width=getPaddingLeft()+getPaddingRight();
        }
        if(heightMode!=MeasureSpec.EXACTLY){
            height=getPaddingTop()+getPaddingBottom();
        }

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw");
        colorRect.set(0, 0, getMeasuredWidth(), rectHeight);
        colorPaint.setColor(Color.LTGRAY);
        //绘制底部浅灰色底色
        canvas.drawRect(colorRect, colorPaint);
        int   sumCircleRadius=circleRadius;
        //控件的宽度   -    2*半径  /  (八个点分7份)
        int  ss=(getWidth()-(circleRadius*2))/(showNumber-1);
        int  greenEndwidth= ss*greenLine+sumCircleRadius;
        if(greenLine!=0){
            colorPaint.setColor(Color.GREEN);
            //绘制绿色的条
            canvas.drawRect(0,0, greenEndwidth, colorRect.height(), colorPaint);
        }
        if(redLine!=0){
            colorPaint.setColor(Color.RED);
            //绘制红色的条
            canvas.drawRect(greenEndwidth, 0, greenEndwidth+ss*redLine, colorRect.height(), colorPaint);

        }
        colorPaint.setColor(Color.GRAY);
        for (int i = 0; i <= showNumber; i++) {
            //绘制灰色的小圆点
            canvas.drawCircle(sumCircleRadius, colorRect.height() / 2, circleRadius, colorPaint);
            String   dian=i+"点";
            canvas.drawText(dian,0,dian.length(),sumCircleRadius,colorRect.height()*2,colorPaint);

            sumCircleRadius=sumCircleRadius+ss;
        }

    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
