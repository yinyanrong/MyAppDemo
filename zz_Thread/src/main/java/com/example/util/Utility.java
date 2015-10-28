package com.example.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.example.get.DownloadMission;
import com.example.zz_thread.R;

public class Utility {

	public static enum FileType {
		APP, VIDEO, EXCEL, WORD, POWERPOINT, MUSIC, ARCHIVE, UNKNOWN
	}

	public static String formatBytes(long bytes) {
		if (bytes < 1024) {
			return String.format("%d B", bytes);
		} else if (bytes < 1024 * 1024) {
			return String.format("%.2f kB", (float) bytes / 1024);
		} else if (bytes < 1024 * 1024 * 1024) {
			return String.format("%.2f MB", (float) bytes / 1024 / 1024);
		} else {
			return String.format("%.2f GB", (float) bytes / 1024 / 1024 / 1024);
		}
	}

	public static String formatSpeed(float speed) {
		if (speed < 1024) {
			return String.format("%.2f B/s", speed);
		} else if (speed < 1024 * 1024) {
			return String.format("%.2f kB/s", speed / 1024);
		} else if (speed < 1024 * 1024 * 1024) {
			return String.format("%.2f MB/s", speed / 1024 / 1024);
		} else {
			return String.format("%.2f GB/s", speed / 1024 / 1024 / 1024);
		}
	}

	public static void writeToFile(String fileName, String content) {
		try {
			writeToFile(fileName, content.getBytes("UTF-8"));
		} catch (Exception e) {

		}
	}

	public static void writeToFile(String fileName, byte[] content) {
		File f = new File(fileName);

		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (Exception e) {

			}
		}

		try {
			FileOutputStream opt = new FileOutputStream(f, false);
			opt.write(content, 0, content.length);
			opt.close();
		} catch (Exception e) {

		}
	}

	public static String readFromFile(String file) {
		try {
			File f = new File(file);

			if (!f.exists() || !f.canRead()) {
				return null;
			}

			BufferedInputStream ipt = new BufferedInputStream(
					new FileInputStream(f));

			byte[] buf = new byte[512];
			StringBuilder sb = new StringBuilder();

			while (ipt.available() > 0) {
				int len = ipt.read(buf, 0, 512);
				sb.append(new String(buf, 0, len, "UTF-8"));
			}

			ipt.close();
			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> T findViewById(View v, int id) {
		return (T) v.findViewById(id);
	}

	public static <T> T findViewById(Activity activity, int id) {
		return (T) activity.findViewById(id);
	}

	public static String getFileExt(String url) {
		if (url.indexOf("?") > -1) {
			url = url.substring(0, url.indexOf("?"));
		}
		if (url.lastIndexOf(".") == -1) {
			return null;
		} else {
			String ext = url.substring(url.lastIndexOf("."));
			if (ext.indexOf("%") > -1) {
				ext = ext.substring(0, ext.indexOf("%"));
			}
			if (ext.indexOf("/") > -1) {
				ext = ext.substring(0, ext.indexOf("/"));
			}
			return ext.toLowerCase();

		}
	}

	public static String getErrorString(Context context, int code) {
		switch (code) {
		case DownloadMission.ERROR_SERVER_UNSUPPORTED:
			return "��������֧��";
		default:
			return "";
		}
	}

	public static FileType getFileType(String file) {
		if (file.endsWith(".apk")) {
			return FileType.APP;
		} else if (file.endsWith(".mp3") || file.endsWith(".wav")
				|| file.endsWith(".flac")) {
			return FileType.MUSIC;
		} else if (file.endsWith(".mp4") || file.endsWith(".mpeg")
				|| file.endsWith(".rm") || file.endsWith(".rmvb")
				|| file.endsWith(".flv") || file.endsWith(".webp")) {
			return FileType.VIDEO;
		} else if (file.endsWith(".doc") || file.endsWith(".docx")) {
			return FileType.WORD;
		} else if (file.endsWith(".xls") || file.endsWith(".xlsx")) {
			return FileType.EXCEL;
		} else if (file.endsWith(".ppt") || file.endsWith(".pptx")) {
			return FileType.POWERPOINT;
		} else if (file.endsWith(".zip") || file.endsWith(".rar")
				|| file.endsWith(".7z") || file.endsWith(".gz")
				|| file.endsWith("tar") || file.endsWith(".bz")) {
			return FileType.ARCHIVE;
		} else {
			return FileType.UNKNOWN;
		}
	}

	public static int getBackgroundForFileType(FileType type) {
		switch (type) {
		case APP:
			return R.color.orange;
		case MUSIC:
			return R.color.cyan;
		case ARCHIVE:
			return R.color.blue;
		case VIDEO:
			return R.color.green;
		case WORD:
		case EXCEL:
		case POWERPOINT:
			return R.color.brown;
		case UNKNOWN:
		default:
			return R.color.bluegray;
		}
	}

	public static int getForegroundForFileType(FileType type) {
		switch (type) {
		case APP:
			return R.color.orange_dark;
		case MUSIC:
			return R.color.cyan_dark;
		case ARCHIVE:
			return R.color.blue_dark;
		case VIDEO:
			return R.color.green_dark;
		case WORD:
		case EXCEL:
		case POWERPOINT:
			return R.color.brown_dark;
		case UNKNOWN:
		default:
			return R.color.bluegray_dark;
		}
	}

	public static int getThemeForFileType(FileType type) {
		switch (type) {
		case APP:
			return R.style.AppTheme;
		case MUSIC:
			return R.style.AppTheme;
		case ARCHIVE:
			return R.style.AppTheme;
		case VIDEO:
			return R.style.AppTheme;
		case WORD:
		case EXCEL:
		case POWERPOINT:
			return R.style.AppTheme;
		case UNKNOWN:
		default:
			return R.style.AppTheme;
		}
	}

	public static int getIconForFileType(FileType type) {
		switch (type) {
		case APP:
			return R.drawable.apps;
		case MUSIC:
			return R.drawable.music;
		case ARCHIVE:
			return R.drawable.archive;
		case VIDEO:
			return R.drawable.video;
		case WORD:
			return R.drawable.word;
		case EXCEL:
			return R.drawable.excel;
		case POWERPOINT:
			return R.drawable.powerpoint;
		case UNKNOWN:
		default:
			return R.drawable.unknown;
		}
	}
}
