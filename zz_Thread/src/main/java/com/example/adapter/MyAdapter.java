package com.example.adapter;
import java.io.File;
import com.example.get.DownloadManager;
import com.example.get.DownloadMission;
import com.example.service.DownloadManagerService;
import com.example.util.Utility;
import com.example.zz_thread.DetailActivity;
import com.example.zz_thread.ProgressDrawable;
import com.example.zz_thread.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private DownloadManager mManager;
	private DownloadManagerService.DMBinder mBinder;
	private int mLayout;
	
	public MyAdapter(Context context, DownloadManagerService.DMBinder binder, DownloadManager manager) {
		super();
		mContext = context;
		mManager = manager;
		mBinder = binder;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLayout = R.layout.mission_item_linear;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mManager.getCount();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mManager.getMission(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		view = mInflater.inflate(mLayout, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		DownloadMission ms = mManager.getMission(pos);
		viewHolder.mission = ms;
		viewHolder.position = pos;
		Utility.FileType type = Utility.getFileType(ms.name);
		viewHolder.icon.setImageResource(Utility.getIconForFileType(type));
		viewHolder.name.setText(ms.name);
		viewHolder.size.setText(Utility.formatBytes(ms.length));
		viewHolder.progress = new ProgressDrawable(mContext, Utility.getBackgroundForFileType(type), Utility.getForegroundForFileType(type));
		viewHolder.bkg.setBackgroundDrawable(viewHolder.progress);
		viewHolder.observer = new MissionObserver(this, viewHolder);
		ms.addListener(viewHolder.observer);
		updateProgress(viewHolder);
		viewHolder.menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				buildPopup(viewHolder);
			}
		});
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDetail(viewHolder);
			}
		});
		return view;
	}
	
	
	private void updateProgress(ViewHolder h) {
		updateProgress(h, false);
	}
	
	private void updateProgress(ViewHolder h, boolean finished) {
		if (h.mission == null) return;
		long now = System.currentTimeMillis();
		if (h.lastTimeStamp == -1) {
			h.lastTimeStamp = now;
		}
		if (h.lastDone == -1) {
			h.lastDone = h.mission.done;
		}
		long deltaTime = now - h.lastTimeStamp;
		long deltaDone = h.mission.done - h.lastDone;
		if (deltaTime == 0 || deltaTime > 1000 || finished) {
			if (h.mission.errCode > 0) {
				h.status.setText(R.string.msg_error);
			} else {
				float progress = (float) h.mission.done / h.mission.length;
				h.status.setText(String.format("%.2f%%", progress * 100));
				h.progress.setProgress(progress);
			}
		}
		if (deltaTime > 1000 && deltaDone > 0) {
			float speed = (float) deltaDone / deltaTime;
			String speedStr = Utility.formatSpeed(speed * 1000);
			String sizeStr = Utility.formatBytes(h.mission.length);
			h.size.setText(sizeStr + " " + speedStr);
			h.lastTimeStamp = now;
			h.lastDone = h.mission.done;
		}
	}
	
	private void showDetail(ViewHolder h) {
		if (h.mission.finished) return;
		// Pass the manager
		DetailActivity.sManager = mManager;
		Intent i = new Intent();
		i.setAction(Intent.ACTION_MAIN);
		i.setClass(mContext, DetailActivity.class);
		i.putExtra("colorId", h.colorId);
		i.putExtra("id", h.position);
		mContext.startActivity(i);
	}
	
	private void buildPopup(final ViewHolder h) {
		PopupMenu popup = new PopupMenu(mContext, h.menu);
		popup.inflate(R.menu.mission);
		Menu menu = popup.getMenu();
		MenuItem start = menu.findItem(R.id.start);
		MenuItem pause = menu.findItem(R.id.pause);
		MenuItem view = menu.findItem(R.id.view);
		MenuItem delete = menu.findItem(R.id.delete);
		// Set to false first
		start.setVisible(false);
		pause.setVisible(false);
		view.setVisible(false);
		delete.setVisible(false);
		if (!h.mission.finished) {
			if (!h.mission.running) {
				if (h.mission.errCode == -1) {
					start.setVisible(true);
				}
				delete.setVisible(true);
			} else {
				pause.setVisible(true);
			}
		} else {
			view.setVisible(true);
			delete.setVisible(true);
		}
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
					case R.id.start:
						mManager.resumeMission(h.position);
						mBinder.onMissionAdded(mManager.getMission(h.position));
						return true;
					case R.id.pause:
						mManager.pauseMission(h.position);
						mBinder.onMissionRemoved(mManager.getMission(h.position));
						h.lastTimeStamp = -1;
						h.lastDone = -1;
						return true;
					case R.id.view:
						Intent i = new Intent();
						i.setAction(Intent.ACTION_VIEW);
						File f = new File(h.mission.location + "/" + h.mission.name);
						String ext = Utility.getFileExt(h.mission.name);
						if (ext == null) return false;
						String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.substring(1));
						if (f.exists()) {
							i.setDataAndType(Uri.fromFile(f), mime);
							mContext.startActivity(i);
						}
						return true;
					case R.id.delete:
						mManager.deleteMission(h.position);
						notifyDataSetChanged();
						return true;
					default:
						return false;
				}
			}
		});
		popup.show();
	}
	
	
	static class ViewHolder{
		public DownloadMission mission;
		public int position;
		public TextView status;
		public ImageView icon;
		public TextView name;
		public TextView size;
		public View bkg;
		public ImageView menu;
		public ProgressDrawable progress;
		public MissionObserver observer;
		public long lastTimeStamp = -1;
		public long lastDone = -1;
		public int colorId = 0;
		public ViewHolder(View v) {
			status = Utility.findViewById(v, R.id.item_status);
			icon = Utility.findViewById(v, R.id.item_icon);
			name = Utility.findViewById(v, R.id.item_name);
			size = Utility.findViewById(v, R.id.item_size);
			bkg = Utility.findViewById(v, R.id.item_bkg);
			menu = Utility.findViewById(v, R.id.item_more);
		}
	}
	
	static class MissionObserver implements DownloadMission.MissionListener {
		private MyAdapter mAdapter;
		private ViewHolder mHolder;
		public MissionObserver(MyAdapter adapter, ViewHolder holder) {
			mAdapter = adapter;
			mHolder = holder;
		}
		@Override
		public void onProgressUpdate(long done, long total) {
			mAdapter.updateProgress(mHolder);
		}
		@Override
		public void onFinish() {
			//mAdapter.mManager.deleteMission(mHolder.position);
			// TODO Notification
			//mAdapter.notifyDataSetChanged();
			mHolder.size.setText(Utility.formatBytes(mHolder.mission.length));
			mAdapter.updateProgress(mHolder, true);
		}
		@Override
		public void onError(int errCode) {
			mAdapter.updateProgress(mHolder);
		}
	}
	
	

}
