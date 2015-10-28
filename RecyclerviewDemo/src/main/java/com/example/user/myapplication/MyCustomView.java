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
 * Created by user on 2015/9/21.
 */
public class MyCustomView extends View {
    String  TAG="MyCustomView";
    private String textContent;
    private int  orientations;
    private Paint   mPaint;
    private  Rect   mRect;
    private int  textSize=30;
    public MyCustomView(Context context) {
        this(context, null);
    }

    public MyCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

      TypedArray    typedArray=context.obtainStyledAttributes(attrs, R.styleable.customView);
        textSize =px2dip(context,textSize);
        int   count=typedArray.getIndexCount();
        Log.i(TAG,"--->"+textContent);
        for (int   i=0 ; i<count ; i++){
           int  typeIndex= typedArray.getIndex(i);
            switch (typeIndex){
                case R.styleable.customView_textContent:
                    textContent=typedArray.getString(typeIndex);
                    Log.i(TAG,"--->"+textContent);
                break;
                case R.styleable.customView_orientation:
                    orientations=typedArray.getIndex(typeIndex);
                break;
                case R.styleable.customView_textSize:
                    textSize= typedArray.getDimensionPixelSize(typeIndex, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                break;
            }

        }
        typedArray.recycle();
        mPaint=new Paint();
        mRect =new  Rect();
        mPaint.getTextBounds(textContent, 0, textContent.length(), mRect);

    }
    public  void  pxTOdp(Context context ,int   value){
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,context.getResources().getDisplayMetrics());
    }
    public  int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setTextSize(textSize);
        mPaint.setColor(Color.BLACK);
        canvas.drawText(textContent, getMeasuredWidth()/2, getMeasuredHeight()/2, mPaint);
//        canvas.drawRect(mRect,mPaint);
    }
}
