package com.example.user.butterknifezeleznydemo;



import android.app.*;
import android.app.Application;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.examlpe.user.User;

public class  MainActivity extends FragmentActivity {
    String  TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication   application=(MyApplication)getApplication();
        User  u=application.getUser();
        if(u==null){
            User   user=new User();
            user.setName("小名");
            user.setPassword("123456");
            application.setUser(user);
            Log.i(TAG,"未生成 对象");
        }else{

            Log.i(TAG, "用户名：" + u.getName() + "用户密码:" + u.getPassword());
        }
        Log.d(TAG, " Log.d");
        Log.w(TAG, " Log.w");
        Log.i(TAG, " Log.i");
        Log.e(TAG, " Log.e");
        Log.v(TAG, " Log.v");
        final WebView  webView=(WebView)findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSavePassword(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
/**
 * 用WebView显示图片，可使用这个参数 设置网页布局类型：
 * 1、LayoutAlgorithm.NARROW_COLUMNS ：适应内容大小
 * 2、LayoutAlgorithm.SINGLE_COLUMN : 适应屏幕，内容将自动缩放
 */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setHorizontalScrollbarOverlay(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.requestFocus();
        webView.loadUrl("http://www.baidu.com");
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                webView.flingScroll(0, 0);
//                webView.pageUp(true);
//                Log.i("------->>>", "------------------------->>>>>>>>>>");
                MainActivity.this.finish();
            }
        });
//        FragmentManager     fragmentManager=getSupportFragmentManager();
//          FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//        fragmentTransaction.add(null,"ffff");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
