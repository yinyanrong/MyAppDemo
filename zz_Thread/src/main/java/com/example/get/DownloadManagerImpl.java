package com.example.get;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.example.util.Utility;
import com.example.zz_thread.BuildConfig;
import com.google.gson.Gson;

public class DownloadManagerImpl implements DownloadManager {
	private static final String TAG = DownloadManagerImpl.class.getSimpleName();

	private Context mContext;
	private String mLocation;
	protected ArrayList<DownloadMission> mMissions = new ArrayList<DownloadMission>();

	public DownloadManagerImpl(Context context, String location) {
		mContext = context;
		mLocation = location;
		loadMissions();
	}

	@Override
	public int startMission(String url, String name, int threads) {
		DownloadMission mission = new DownloadMission();
		mission.url = url;
		mission.name = name;
		mission.location = mLocation;
		mission.timestamp = System.currentTimeMillis();
		mission.threadCount = threads;
		new Initializer(mContext, mission).start();
		return insertMission(mission);
	}

	@Override
	public void resumeMission(int i) {
		DownloadMission d = getMission(i);
		if (!d.running && d.errCode == -1) {
			d.start();
		}
	}

	@Override
	public void pauseMission(int i) {
		DownloadMission d = getMission(i);
		if (d.running) {
			d.pause();
		}
	}

	@Override
	public void deleteMission(int i) {
		getMission(i).delete();
		mMissions.remove(i);
	}

	private void loadMissions() {
		File f = new File(mLocation);

		if (f.exists() && f.isDirectory()) {
			File[] subs = f.listFiles();

			for (File sub : subs) {
				if (sub.isDirectory()) {
					continue;
				}

				if (sub.getName().endsWith(".giga")) {
					String str = Utility.readFromFile(sub.getAbsolutePath());
					if (str != null && !str.trim().equals("")) {

						if (BuildConfig.DEBUG) {
							Log.d(TAG, "loading mission " + sub.getName());
							Log.d(TAG, str);
						}

						DownloadMission mis = new Gson().fromJson(str,
								DownloadMission.class);

						if (mis.finished) {
							sub.delete();
							continue;
						}

						mis.running = false;
						mis.recovered = true;
						insertMission(mis);
					}
				} else if (!sub.getName().startsWith(".")
						&& !new File(sub.getPath() + ".giga").exists()) {
					// Add a dummy mission for downloaded files
					DownloadMission mis = new DownloadMission();
					mis.length = sub.length();
					mis.done = mis.length;
					mis.finished = true;
					mis.running = false;
					mis.name = sub.getName();
					mis.location = mLocation;
					mis.timestamp = sub.lastModified();
					insertMission(mis);
				}
			}
		}
	}

	@Override
	public DownloadMission getMission(int i) {
		return mMissions.get(i);
	}

	@Override
	public int getCount() {
		return mMissions.size();
	}

	private int insertMission(DownloadMission mission) {
		int i = -1;

		DownloadMission m = null;

		if (mMissions.size() > 0) {
			do {
				m = mMissions.get(++i);
			} while (m.timestamp > mission.timestamp
					&& i < mMissions.size() - 1);

			// if (i > 0) i--;
		} else {
			i = 0;
		}

		mMissions.add(i, mission);

		return i;
	}

	@Override
	public String getLocation() {
		return mLocation;
	}

	private class Initializer extends Thread {
		private Context context;
		private DownloadMission mission;

		public Initializer(Context context, DownloadMission mission) {
			this.context = context;
			this.mission = mission;
		}

		@Override
		public void run() {
			try {
				URL url = new URL(mission.url);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				//获取远程文件的大小
				mission.length = conn.getContentLength();
				//获取不到文件大小 下载暂停
				if (mission.length <= 0) {
					mission.errCode = DownloadMission.ERROR_SERVER_UNSUPPORTED;
					// mission.notifyError(DownloadMission.ERROR_SERVER_UNSUPPORTED);
					return;
				}
				/**
				 * 设定512kb为一个硬盘单位,   分模块下载 （有多少模块就有多少次下载）
				 */
				mission.blocks = mission.length / BLOCK_SIZE;
				//如果线程的设定超过了 模块数 则以模块为主
				if (mission.threadCount > mission.blocks) {
					mission.threadCount = (int) mission.blocks;
				}
				//如果线程数为0   则默认为1条线程
				if (mission.threadCount <= 0) {
					mission.threadCount = 1;
				}
				//万一  计算出的 模块数* 每块的大小  < 远程的文件 则进行d多一次下载
				if (mission.blocks * BLOCK_SIZE < mission.length) {
					mission.blocks++;
				}

				new File(mission.location).mkdirs();
				new File(mission.location + "/" + mission.name).createNewFile();
				RandomAccessFile af = new RandomAccessFile(mission.location
						+ "/" + mission.name, "rw");
				af.setLength(mission.length);
				af.close();

				mission.start();
			} catch (Exception e) {
				// TODO Notify
				throw new RuntimeException(e);
			}
		}
	}
}
