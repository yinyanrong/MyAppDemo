package com.example.get;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.zz_thread.BuildConfig;

public class DownloadRunnable implements Runnable {
	private static final String TAG = DownloadRunnable.class.getSimpleName();

	private DownloadMission mMission;//下载任务对象
	private int mId;//线程编号

	public DownloadRunnable(DownloadMission mission, int id) {
		mMission = mission;
		mId = id;
	}


	
	@Override
	public void run() {
		boolean retry = mMission.recovered;
		long position = mMission.getPosition(mId);

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "线程创建位置->"+mId + ":需要下载的模块-->" + position);
			Log.d(TAG, "线程--->"+mId + " 暂停的线程？--> " + mMission.recovered);
		}

		while (mMission.errCode == -1 && mMission.running
				&& position < mMission.blocks) {

			if (Thread.currentThread().isInterrupted()) {
				mMission.pause();
				return;
			}

			if (BuildConfig.DEBUG && retry) {
				Log.d(TAG, mId + "号线程重启成功 ，需要下载的模块 ：" + position);
			}

			//查找未下载的模块
			// Wait for an unblocked position
			while (!retry && position < mMission.blocks
					&& mMission.isBlockPreserved(position)) {

				if (BuildConfig.DEBUG) {
					Log.d(TAG, mId + ":position " + position
							+ " preserved, passing");
				}

				position++;
			}

			retry = false;
			//如果当前模块都已经下完 就跳出循环
			if (position >= mMission.blocks) {
				break;
			}

			if (BuildConfig.DEBUG) {
				Log.d(TAG, mId + ":preserving position " + position);
			}
			//保存下载块的线程标示
			mMission.preserveBlock(position);
			mMission.setPosition(mId, position);
			//定义每个线程需要下载的开始和结束的大小
			long start = position * DownloadManager.BLOCK_SIZE;
			long end = start + DownloadManager.BLOCK_SIZE - 1;

			if (end >= mMission.length) {
				end = mMission.length - 1;
			}

			HttpURLConnection conn = null;

			int total = 0;

			try {
				URL url = new URL(mMission.url);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
				int  code=conn.getResponseCode();
				Log.i("DownloadRunnable","----code:"+code);
				//线程请求写的不完善  忽略很多的请求
				// A server may be ignoring the range requet
				//返回值HTTP_PARTIAL   是服务器处理了部分请求成功的返回值 206
				if (conn.getResponseCode() != HttpURLConnection.HTTP_PARTIAL) {
					mMission.errCode = DownloadMission.ERROR_SERVER_UNSUPPORTED;
					notifyError(DownloadMission.ERROR_SERVER_UNSUPPORTED);

					if (BuildConfig.DEBUG) {
						Log.e(TAG, mId + ":Unsupported");
					}

					break;
				}

				if (BuildConfig.DEBUG) {
					Log.d(TAG, mId + ":  下载的范围 " + conn.getRequestProperty("Range"));
					Log.d(TAG,
							mId + ":下载的内容大小 " + conn.getContentLength()
									+ " Code:" + conn.getResponseCode());
				}

				RandomAccessFile f = new RandomAccessFile(mMission.location
						+ "/" + mMission.name, "rw");
				//设置跳过的字节数
				f.seek(start);
				BufferedInputStream ipt = new BufferedInputStream(
						conn.getInputStream());
				byte[] buf = new byte[512];

				while (start < end && mMission.running) {
					//设置缓冲区
					int len = ipt.read(buf, 0, 512);

					if (len == -1) {
						break;
					} else {
						start += len;
						total += len;
						f.write(buf, 0, len);
						notifyProgress(len);
					}
				}

				if (BuildConfig.DEBUG && mMission.running) {
					Log.d(TAG, mId + ":position " + position
							+ " finished, total length " + total);
				}

				f.close();
				ipt.close();

				// TODO We should save progress for each thread
			} catch (Exception e) {
				// TODO Retry count limit & notify error
				retry = true;

				notifyProgress(-total);

				if (BuildConfig.DEBUG) {
					Log.d(TAG, mId + ":position " + position + " retrying");
				}
			}
		}

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "thread " + mId + " exited main loop");
		}

		if (mMission.errCode == -1 && mMission.running) {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "no error has happened, notifying");
			}
			notifyFinished();
		}

		if (BuildConfig.DEBUG && !mMission.running) {
			Log.d(TAG, "The mission has been paused. Passing.");
		}
	}

	private void notifyProgress(final long len) {
		synchronized (mMission) {
			mMission.notifyProgress(len);
		}
	}

	private void notifyError(final int err) {
		synchronized (mMission) {
			mMission.notifyError(err);
			mMission.pause();
		}
	}

	private void notifyFinished() {
		synchronized (mMission) {
			mMission.notifyFinished();
		}
	}
}
