package com.example.user.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String  TAG="MainActivity";
    RecyclerView    mRecyclerView;
    private List<String> mDatas;
    PopupWindow  mPopupWindow;
    Button   but;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecylerViewAdapter());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST));

        but=(Button)findViewById(R.id.but);
        but .setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
//                       showShareDialogP2PBuy();
                       showDialog(0);
                       Log.i(TAG, "--click---" );
                       Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                       startActivity(intent);
                   }
               });
        initData();

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Log.i(TAG,"-----"+id);
        return super.onCreateDialog(id);
    }

    protected void initData()
    {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++)
        {
            mDatas.add("" + (char) i);
        }
    }
    public void showShareDialogP2PBuy() {

            View view = View
                    .inflate(this, R.layout.recycler_item, null);
            mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT);

            mPopupWindow.setFocusable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//            mPopupWindow.s
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Log.e("", "----------------mPopupWindow  dismiss------------->>>");
                }
            });
        mPopupWindow.showAsDropDown(but);

    }
    private  class  RecylerViewAdapter  extends  RecyclerView.Adapter<RecylerViewAdapter.RecyclerViewViewHolder>{

        @Override
        public void onBindViewHolder(RecyclerViewViewHolder holder, int position) {
            holder.textView2.setText(mDatas.get(position));
        }

        @Override
        public RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View   view=LayoutInflater.from(MainActivity.this).inflate(R.layout.recycler_item, parent, false);
            RecyclerViewViewHolder rv  =new  RecyclerViewViewHolder(view);
            return rv;
        }
        @Override
        public int getItemCount() {
            return mDatas.size();
        }
       class  RecyclerViewViewHolder  extends RecyclerView.ViewHolder{
           TextView  textView2;
           public RecyclerViewViewHolder(View itemView) {
               super(itemView);
               textView2=  (TextView)itemView.findViewById(R.id.textView2);
           }
       }

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
