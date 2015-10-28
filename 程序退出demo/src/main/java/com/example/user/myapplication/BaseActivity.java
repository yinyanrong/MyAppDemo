package com.example.user.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by user on 2015/10/16.
 */
public abstract class BaseActivity  extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter   intentFilter=new IntentFilter("qq");
        registerReceiver(receiver, intentFilter);

    }
    public abstract void brocastExit();
    public BroadcastReceiver  receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals("qq")){
                brocastExit();
                Log.i("DEMO", "---->BaseActivity  receiver");
            }
        }
    };
    @Override
    protected void onDestroy() {

        unregisterReceiver(receiver);
        Log.i("DEMO", "BaseActivity--->onDestroy()");
        super.onDestroy();
    }

}
