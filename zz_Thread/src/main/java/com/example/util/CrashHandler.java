package com.example.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.PrintWriter;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
	public static String CRASH_DIR = Environment.getExternalStorageDirectory()
			.getPath() + "/GigaCrash/";
	public static String CRASH_LOG = CRASH_DIR + "last_crash.log";
	public static String CRASH_TAG = CRASH_DIR + ".crashed";

	private static String ANDROID = Build.VERSION.RELEASE;
	private static String MODEL = Build.MODEL;
	private static String MANUFACTURER = Build.MANUFACTURER;

	public static String VERSION = "Unknown";

	private Thread.UncaughtExceptionHandler mPrevious;

	public static void init(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			VERSION = info.versionName + info.versionCode;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void register() {
		new CrashHandler();
	}

	private CrashHandler() {
		//为一个线程设置一个默认的Handler，在程序不能捕获而退出，并且么有其他的handler时被调用!
		//所以只要通过set方法，为一个线程设置一个handler就可以在程序异常退出时，捕获改异常
		mPrevious = Thread.currentThread().getUncaughtExceptionHandler();
		Thread.currentThread().setUncaughtExceptionHandler(this);
	}
	//把捕获的异常写到 sd卡的文件上
	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		File f = new File(CRASH_LOG);
		if (f.exists()) {
			f.delete();
		} else {
			try {
				new File(CRASH_DIR).mkdirs();
				f.createNewFile();
			} catch (Exception e) {
				return;
			}
		}

		PrintWriter p;
		try {
			p = new PrintWriter(f);
		} catch (Exception e) {
			return;
		}

		p.write("Android Version: " + ANDROID + "\n");
		p.write("Device Model: " + MODEL + "\n");
		p.write("Device Manufacturer: " + MANUFACTURER + "\n");
		p.write("App Version: " + VERSION + "\n");
		p.write("*********************\n");
		throwable.printStackTrace(p);

		p.close();

		try {
			new File(CRASH_TAG).createNewFile();
		} catch (Exception e) {
			return;
		}

		if (mPrevious != null) {
			mPrevious.uncaughtException(thread, throwable);
		}
	}
}
