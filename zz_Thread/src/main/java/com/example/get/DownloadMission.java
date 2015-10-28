package com.example.get;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.util.Utility;
import com.example.zz_thread.BuildConfig;
import com.google.gson.Gson;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class DownloadMission {
	private static final String TAG = DownloadMission.class.getSimpleName();

	public static interface MissionListener {
		public Handler handler = new Handler(Looper.getMainLooper());

		public void onProgressUpdate(long done, long total);

		public void onFinish();

		public void onError(int errCode);
	}
	
	public static final int ERROR_SERVER_UNSUPPORTED = 206;

	public String name = "";//下载的名称
	public String url = "";//下载的链接地址
	public String location = "";//下载文件存放目录
	public long blocks = 0;//以默认的512kb为单位  计算文件占用的单位数
	public long length = 0;//远程文件大小
	public long done = 0;
	public int threadCount = 3;//线程数
	public int finishCount = 0;
	public ArrayList<Long> threadPositions = new ArrayList<Long>();
	public HashMap<Long, Boolean> blockState = new HashMap<Long, Boolean>();
	public boolean running = false;
	public boolean finished = false;
	public int errCode = -1;
	public long timestamp = 0;

	public transient boolean recovered = false;

	private transient ArrayList<WeakReference<MissionListener>> mListeners = new ArrayList<WeakReference<MissionListener>>();
	private transient boolean mWritingToFile = false;

	/**
	 *线程时候被存储  没有返回false  有的话 获取 线程的状态
	 */
	public boolean isBlockPreserved(long block) {
		return blockState.containsKey(block) ? blockState.get(block) : false;
	}

	public void preserveBlock(long block) {
		synchronized (blockState) {
			blockState.put(block, true);
		}
	}

	public void setPosition(int id, long position) {
		threadPositions.set(id, position);
	}

	public long getPosition(int id) {
		return threadPositions.get(id);
	}

	public synchronized void notifyProgress(long deltaLen) {
		if (recovered) {
			recovered = false;
		}

		done += deltaLen;

		if (done > length) {
			done = length;
		}

		if (done != length) {
			writeThisToFile();
		}

		for (WeakReference<MissionListener> ref : mListeners) {
			final MissionListener listener = ref.get();
			if (listener != null) {
				listener.handler.post(new Runnable() {
					@Override
					public void run() {
						listener.onProgressUpdate(done, length);
					}
				});
			}
		}
	}

	public synchronized void notifyFinished() {
		if (errCode > 0)
			return;

		finishCount++;

		if (finishCount == threadCount) {
			onFinish();
		}
	}

	private void onFinish() {
		if (errCode > 0)
			return;

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onFinish");
		}

		running = false;
		finished = true;

		deleteThisFromFile();

		for (WeakReference<MissionListener> ref : mListeners) {
			final MissionListener listener = ref.get();
			if (listener != null) {
				listener.handler.post(new Runnable() {
					@Override
					public void run() {
						listener.onFinish();
					}
				});
			}
		}
	}

	public synchronized void notifyError(int err) {
		errCode = err;

		writeThisToFile();

		for (WeakReference<MissionListener> ref : mListeners) {
			final MissionListener listener = ref.get();
			listener.handler.post(new Runnable() {
				@Override
				public void run() {
					listener.onError(errCode);
				}
			});
		}
	}

	public synchronized void addListener(final MissionListener listener) {
//		listener.handler = new Handler(Looper.getMainLooper());
		mListeners.add(new WeakReference<MissionListener>(listener));
	}

	public synchronized void removeListener(MissionListener listener) {
		mListeners.remove(listener);
	}

	public void start() {
		//如果这个下载任务没有运行 或者没有完成的话  执行就执行下载
		if (!running && !finished) {
			running = true;

			for (int i = 0; i < threadCount; i++) {

				if (threadPositions.size() <= i && !recovered) {
					threadPositions.add((long) i);
				}

				new Thread(new DownloadRunnable(this, i)).start();
			}
		}
	}

	public void pause() {
		if (running) {
			running = false;

			// TODO: Notify & Write state to info file
			// if (err)
		}
	}

	public void delete() {
		deleteThisFromFile();
		new File(location + "/" + name).delete();
	}

	public void writeThisToFile() {
		if (!mWritingToFile) {
			mWritingToFile = true;
			new Thread() {
				@Override
				public void run() {
					doWriteThisToFile();
					mWritingToFile = false;
				}
			}.start();
		}
	}

	private void doWriteThisToFile() {
		synchronized (blockState) {
			Utility.writeToFile(location + "/" + name + ".giga",
					new Gson().toJson(this));
		}
	}

	private void deleteThisFromFile() {
		new File(location + "/" + name + ".giga").delete();
	}
}
