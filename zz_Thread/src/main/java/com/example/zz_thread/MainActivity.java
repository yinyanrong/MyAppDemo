package com.example.zz_thread;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment.AllMissionsFragment;
import com.example.fragment.DownloadedMissionsFragment;
import com.example.fragment.DownloadingMissionsFragment;
import com.example.fragment.MissionsFragment;
import com.example.get.DownloadManager;
import com.example.service.DownloadManagerService;
import com.example.util.CrashHandler;
import com.example.util.Utility;

public class MainActivity extends FragmentActivity {
	private Button mAddButton;
	private Button mAllButton;
	private Button mIngButton;
	private Button mOkButton;

	private String mPendingUrl;
	private SharedPreferences mPrefs;

	private MissionsFragment mFragment;
	
	private DownloadManager mManager;
	private DownloadManagerService.DMBinder mBinder;
	
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName p1, IBinder binder) {
			mBinder = (DownloadManagerService.DMBinder) binder;
			mManager = mBinder.getDownloadManager();
		}
		@Override
		public void onServiceDisconnected(ComponentName p1) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//初始化程序异常捕获log日志
		CrashHandler.init(this);
		CrashHandler.register();
		// Service
		Intent i = new Intent();
		i.setClass(this, DownloadManagerService.class);
		startService(i);
		bindService(i, mConnection, Context.BIND_AUTO_CREATE);
		
		setContentView(R.layout.activity_main);
		mPrefs = getSharedPreferences("threads", Context.MODE_PRIVATE);
		mAddButton = (Button) findViewById(R.id.add_button);
		mAddButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showUrlDialog();
			}
		});
		mAllButton = (Button) findViewById(R.id.all_button);
		mAllButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mFragment = new AllMissionsFragment();
				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.frame, mFragment)
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.commit();
			}
		});
		mIngButton = (Button) findViewById(R.id.ing_button);
		mIngButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mFragment = new DownloadingMissionsFragment();
				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.frame, mFragment)
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.commit();
			}
		});
		mOkButton = (Button) findViewById(R.id.ok_button);
		mOkButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mFragment = new DownloadedMissionsFragment();
				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.frame, mFragment)
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.commit();
			}
		});
		
		mFragment = new AllMissionsFragment();
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.frame, mFragment)
				.setTransition(
						FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	private void showUrlDialog() {
		// Create the view
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.dialog_url, null);
		final EditText text = Utility.findViewById(v, R.id.url);
		final EditText name = Utility.findViewById(v, R.id.file_name);
		final TextView tCount = Utility.findViewById(v, R.id.threads_count);
		final SeekBar threads = Utility.findViewById(v, R.id.threads);
		final Button fetch = Utility.findViewById(v, R.id.fetch_name);
		threads.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekbar, int progress,
					boolean fromUser) {
				tCount.setText(String.valueOf(progress + 1));
			}

			@Override
			public void onStartTrackingTouch(SeekBar p1) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar p1) {
			}
		});
		int def = mPrefs.getInt("threads", 4);
		threads.setProgress(def - 1);
		tCount.setText(String.valueOf(def));
		text.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence p1, int p2, int p3,
					int p4) {
			}

			@Override
			public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
				String url = text.getText().toString().trim();
				if (!url.equals("")) {
					int index = url.lastIndexOf("/");
					if (index > 0) {
						int end = url.lastIndexOf("?");
						if (end < 0) {
							end = url.length();
						}
						name.setText(url.substring(index + 1, end));
					}
				}
			}

			@Override
			public void afterTextChanged(Editable txt) {

			}
		});

		if (mPendingUrl != null) {
			text.setText(mPendingUrl);
		}

		// Show the dialog
		final AlertDialog dialog = new AlertDialog.Builder(this).setView(v)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						String url = text.getText().toString().trim();
						String fName = name.getText().toString().trim();
						File f = new File(mManager.getLocation() + "/" + fName);
						if (f.exists()) {
							Toast.makeText(MainActivity.this, R.string.msg_exists, Toast.LENGTH_SHORT).show();
						} else if (!checkURL(url)) {
							Toast.makeText(MainActivity.this, R.string.msg_url_malform, Toast.LENGTH_SHORT).show();
						} else {
							//如果为空就是死循环
							while (mBinder == null);
							
							int res = mManager.startMission(url, fName, threads.getProgress() + 1);
							mBinder.onMissionAdded(mManager.getMission(res));
							mFragment.notifyChange();
							mPrefs.edit().putInt("threads", threads.getProgress() + 1).commit();
							arg0.dismiss();
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
					}
				}).show();
		fetch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new NameFetcherTask().execute(text, name);
			}
		});
	}

	private class NameFetcherTask extends AsyncTask<View, Void, Object[]> {
		@Override
		protected Object[] doInBackground(View[] params) {
			try {
				URL url = new URL(((EditText) params[0]).getText().toString());
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				String header = conn.getHeaderField("Content-Disposition");
				if (header != null && header.indexOf("=") != -1) {
					return new Object[] { params[1],
							header.split("=")[1].replace("\"", "") };
				}
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Object[] result) {
			super.onPostExecute(result);
			if (result != null) {
				((EditText) result[0]).setText(result[1].toString());
			}
		}
	}
	
	private boolean checkURL(String url) {
		try {
			URL u = new URL(url);
			u.openConnection();
			return true;
		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
}
