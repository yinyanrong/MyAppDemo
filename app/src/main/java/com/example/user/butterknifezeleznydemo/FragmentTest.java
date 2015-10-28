package com.example.user.butterknifezeleznydemo;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 2015/9/22.
 */
public class FragmentTest  extends FragmentActivity {


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        getFragmentManager();
        return super.onCreateView(name, context, attrs);
    }
}
