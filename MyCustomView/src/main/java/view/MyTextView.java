package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.cn.mycustomview.R;

/**
 * Created by user on 2015/10/12.
 */
public class MyTextView   extends View{

    /**
     * 文本内容
     */
    private String  mTextContent;
    /**
     * 文字的大小
     *
     */
    private int     mTextSize;
    /**
     * 自定义的文字
     */
    private Paint  textPaint;
    /**
     * 自定义  文字的框框
     */
    Rect  rect;
    public MyTextView(Context context) {
        this(context,null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray  typedArray= context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyTextView,defStyleAttr,0);
        for (int i=0;i<typedArray.length();i++){
           int type=typedArray.getIndex(i);
            switch (type){
                case R.styleable.MyTextView_myTextContext:
                    mTextContent=typedArray.getString(type);
                break;
                case R.styleable.MyTextView_myTextSize:
                    mTextSize=typedArray.getDimensionPixelSize(type,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                break;
            }
        }
        typedArray.recycle();
        textPaint=new Paint();
        textPaint.setTextSize(mTextSize);
        rect=new Rect();
        textPaint.getTextBounds(mTextContent,0,mTextContent.length(),rect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        textPaint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), textPaint);
        textPaint.setColor(Color.BLUE);
        canvas.drawText(mTextContent,getWidth()/2-rect.width()/2,getHeight()/2-rect.height()/2,textPaint);
    }
}
