package com.example.user.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by user on 2015/10/15.
 */
public class SecondActivity  extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        Button   button=(Button)findViewById(R.id.button);
        button.setText("activity  销毁");
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                finish();
//                String base64Str=Base64.encodeToString(("123456789").getBytes(), Base64.DEFAULT);
//                Log.i("my", "--有等号-->" + base64Str);
//                String base64Str2=Base64.encodeToString(("123456789").getBytes(), Base64.NO_PADDING);
//                Log.i("my", "--没有等号-->" + base64Str2);
//                String   jiemi1= new String(Base64.decode(base64Str, Base64.DEFAULT));
//                Log.i("my", "--解有等号-->" + jiemi1);
//                String jiemi2=new String(Base64.decode(base64Str2.getBytes(), Base64.NO_PADDING));
//                Log.i("my","--解没有等号-->"+jiemi2);
//            }
//        });
        //内存泄露一
        StaticClass.getInstance().setContent(this);
        //内存泄露二(未证实)
        StaticClass.getInstance().setUser(new User());
        StaticClass.getInstance().setTest(new Test() {

             @Override
             public void test() {

             }
         });
//        15110243174
    }
}
