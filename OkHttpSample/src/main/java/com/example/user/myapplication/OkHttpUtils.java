package com.example.user.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by user on 2015/10/19.
 */
public class OkHttpUtils {
    OkHttpClient   httpClient=new OkHttpClient();
   static OkHttpUtils  okHttpUtils=null;


    public static OkHttpUtils   getInstance(){
        if (okHttpUtils==null){
            synchronized (OkHttpUtils.class){
                if(okHttpUtils==null){}{
                    okHttpUtils=new  OkHttpUtils();
                }
            }
        }
        return okHttpUtils;
    }
    public  void requestGet(String URL, final Handler handler,final long   time){
        Request    request=new  Request.Builder().url(URL).build();
        Call call =httpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                Message  message=Message.obtain();
                Bundle bundle =new Bundle();
                bundle.putString("value",request.body().toString());
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Message  message=Message.obtain();
                Bundle bundle =new Bundle();
                bundle.putString("value",response.body().toString());
                handler.sendMessage(message);
            }
        });
    }


}
