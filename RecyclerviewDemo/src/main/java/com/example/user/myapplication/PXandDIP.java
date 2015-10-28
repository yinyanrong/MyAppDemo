package com.example.user.myapplication;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

/**
 * Created by user on 2015/9/29.
 */
public class PXandDIP {
   private static  String  Tag="PXandDIP";
    public  static  int  dpValue(Context  context,int   value){
       int  result=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,context.getResources().getDisplayMetrics());
        return result;
    }
    public  static  int  spValue(Context  context,int   value){
        int  result=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,value,context.getResources().getDisplayMetrics());
        return result;
    }

}
