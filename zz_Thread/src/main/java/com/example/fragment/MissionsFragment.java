package com.example.fragment;
import com.example.adapter.MyAdapter;
import com.example.get.DownloadManager;
import com.example.service.DownloadManagerService;
import com.example.util.Utility;
import com.example.zz_thread.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.v4.app.Fragment;

public abstract class MissionsFragment extends Fragment {
	private DownloadManager mManager;
	private DownloadManagerService.DMBinder mBinder;
	private ListView mList;
	private MyAdapter mAdapter;
	private Activity mActivity;
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			mBinder = (DownloadManagerService.DMBinder) binder;
			mManager = setupDownloadManager(mBinder);
			updateList();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// What to do?
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.missions, container, false);
		// Views
		mList = Utility.findViewById(v, R.id.listView);
		// Bind the service
		Intent i = new Intent();
		i.setClass(getActivity(), DownloadManagerService.class);
		getActivity().bindService(i, mConnection, Context.BIND_AUTO_CREATE);
		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	public void notifyChange() {
		mAdapter.notifyDataSetChanged();
	}

	private void updateList() {
		mAdapter = new MyAdapter(mActivity, mBinder, mManager);
		mList.setAdapter(mAdapter);
	}

	protected abstract DownloadManager setupDownloadManager(
			DownloadManagerService.DMBinder binder);
}
