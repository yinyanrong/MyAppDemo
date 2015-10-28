package com.example.user.myapplication;

import android.app.Activity;

/**
 * Created by user on 2015/10/15.
 */
public class StaticClass {

    private static StaticClass  staticClass=null;
    Activity  activity;
    User  user;
    Test test;
    private StaticClass(){

    }
    public static  StaticClass  getInstance(){
        if (staticClass==null){
            staticClass=new  StaticClass();
        }
        return  staticClass;
    }
    public void  setContent(Activity  activity){
     this.activity=activity;
    }
    public void  setUser(User user){ this.user=user;}
    public void   setTest(Test test){this.test=test;}
}
