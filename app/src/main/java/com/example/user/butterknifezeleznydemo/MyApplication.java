package com.example.user.butterknifezeleznydemo;

import android.app.Application;

import com.examlpe.user.User;

/**
 * Created by user on 2015/9/23.
 */
public  class MyApplication  extends Application {

    public User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
