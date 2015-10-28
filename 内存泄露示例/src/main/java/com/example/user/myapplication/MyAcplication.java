package com.example.user.myapplication;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by user on 2015/10/15.
 */
public class MyAcplication  extends Application{

        @Override public void onCreate() {
            super.onCreate();
            LeakCanary.install(this);
        }

}
