package com.example.user.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

/**
 * Created by user on 2015/10/16.
 */
public class SecondActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent  intent= new Intent();
                intent.setAction("qq");
               sendBroadcast(intent);
            }
        });
    }

    @Override
    public void brocastExit() {
        this.finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("DEMO","SecondActivity--->onDestroy");
    }
}
