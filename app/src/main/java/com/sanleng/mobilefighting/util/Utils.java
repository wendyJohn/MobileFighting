package com.sanleng.mobilefighting.util;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	private static Toast toast = null;
	/**
	 * log�?�?
	 */
	static private boolean i = true;

	public static void toast(Context context, String str) {
		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 * @param obj
	 * @param str
	 */
	public static void d(Object obj, String str) {
		if (i) {
		}
	}

	public static void i(String str) {
		if (i) {
			if (str != null) {
				Log.i("测试+", str);
			}

		}
	}

	/**
	 * 输出流→字符�?
	 */
	public static String getStreamString(InputStream tInputStream) {
		if (tInputStream != null) {
			try {
				BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));
				StringBuffer tStringBuffer = new StringBuffer();
				String sTempOneLine = new String("");
				while ((sTempOneLine = tBufferedReader.readLine()) != null) {
					tStringBuffer.append(sTempOneLine);
				}
				return tStringBuffer.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将一个字符串转化为输入流
	 */
	public static InputStream getStringStream(String sInputString) {
		if (sInputString != null && !sInputString.trim().equals("")) {
			try {
				ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
				return tInputStringStream;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取版本信息
	 *
	 * @param activity
	 * @return
	 */
	public static int getVersionCode(Activity activity) {
		// 获取packagemanager的实�?
		PackageManager packageManager = activity.getPackageManager();
		// getPackageName()是你当前类的包名�?0代表是获取版本信�?
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packInfo.versionCode;
	}

	/**
	 * 获取屏幕宽高 返回int型数�? 第一个是�? 第二个是�?
	 *
	 * @param act
	 * @return
	 */
	public static int[] getPhoneWidthAndHeight(Activity act) {
		WindowManager wm = act.getWindowManager();

		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();

		return new int[] { width, height };
	}

	/**
	 * 将流转化成字符串 自动编码转换utf-8
	 * <p/>
	 * 有转义字符时-失效
	 *
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String getString(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = in.read(b)) != -1) {
			out.write(b, 0, len);
		}
		String str = new String(out.toByteArray(), "utf-8");
		out.close();
		return str;
	}

	/**
	 * @param in
	 *            输出�?
	 * @param str
	 *            编码
	 * @return
	 * @throws IOException
	 */
	public static String getString(InputStream in, String str) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		byte[] b = new byte[1024];
		int len = 0;
		while ((len = in.read(b)) != -1) {
			out.write(b, 0, len);
		}
		String str1 = new String(out.toByteArray(), str);
		out.close();
		return str1;
	}

	/**
	 * 保存图片到手机内�?
	 *
	 * @param context
	 * @param filename
	 * @param bitmap
	 * @return
	 */
	public static boolean setSaveToPhone(Context context, String filename, Bitmap bitmap, String path) {
		if (TextUtils.isEmpty(path)) {
			path = "/data/data/" + context.getPackageName() + "/" + filename;
		} else {
			path = context.getFilesDir().getAbsolutePath();
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] bytes = baos.toByteArray();

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(path);
			out.write(bytes);

			out.flush();
			out.close();
			baos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 保存图片
	 */
	public static void BaoCunTuPian(Bitmap bitmap, String name) throws IOException {
		FileOutputStream fileOutputStream = null;
		File file = new File(Environment.getExternalStorageDirectory(), name);
		fileOutputStream = new FileOutputStream(file);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] bytes = baos.toByteArray();
		fileOutputStream.write(bytes);

		fileOutputStream.flush();
		fileOutputStream.close();
		baos.close();
	}

	/**
	 * 保存数据到手机内�?
	 */
	public static void setSaveToPhone(Context context, String filename, String content) {
		String path = "/data/data/" + context.getPackageName() + "/" + filename;
		try {
			FileOutputStream out = new FileOutputStream(path);
			out.write(content.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 从本地读取数�?
	 */
	public static String getSavePhoneText(Context context, String filename) {
		try {
			String path = "/data/data/" + context.getPackageName() + "/" + filename;
			FileInputStream in = new FileInputStream(path);
			String str = getString(in);
			in.close();
			return str;
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 从本地读取图�?
	 */
	public static Bitmap getSavePhone(Context context, String filename) {
		String path = "/data/data/" + context.getPackageName() + "/" + filename;
		try {
			FileInputStream in = new FileInputStream(path);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			in.close();
			return bitmap;

		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 通过图片路径获取图片
	 */
	public static Bitmap getBitmapformPhone(File file) {

		try {
			FileInputStream in = new FileInputStream(file);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			in.close();
			return bitmap;
		} catch (Exception e) {
		}
		return null;

	}

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	/**
	 * 得到字符串方式的时间
	 *
	 * @param time
	 * @return 2015-01-01 11:11:11
	 */
	public static String getFormatTime(long time) {
		Date date = new Date(time);
		return dateFormat.format(date);
	}

	private static DecimalFormat df = new DecimalFormat("#.00");

	/**
	 * 得到字符串方式的文件大小
	 *
	 * @param filesize
	 * @return B? KB? MB? GB?
	 */
	public static String getFileSize(long filesize) {
		StringBuffer mstrbuf = new StringBuffer();
		if (filesize < 1024) {
			mstrbuf.append(filesize);
			mstrbuf.append(" B");
		} else if (filesize < 1048576) {
			mstrbuf.append(df.format((double) filesize / 1024));
			mstrbuf.append(" K");
		} else if (filesize < 1073741824) {
			mstrbuf.append(df.format((double) filesize / 1048576));
			mstrbuf.append(" M");
		} else {
			mstrbuf.append(df.format((double) filesize / 1073741824));
			mstrbuf.append(" G");
		}
		return mstrbuf.toString();
	}

	/**
	 * 文件及文件夹的删�?
	 *
	 * @param file
	 */
	public static void delete(File file) {

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files.length != 0) {
				for (File f : files) {
					delete(f);
				}
			}
		}
		file.delete();
	}

	/**
	 * 判断网络状�?�是否可�?
	 *
	 * @return true: 网络可用 ; false: 网络不可�?
	 */
	public static boolean isConnectInternet(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) { // 注意，这个判断一定要的哦，要不然会出�?
			return networkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * 将信息复制到粘贴板上�?
	 */
	public static void zhantieban(Context context, String text) {
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
		cmb.setText(text);
	}

	private final static int kSystemRootStateUnknow = -1;
	private final static int kSystemRootStateDisable = 0;
	private final static int kSystemRootStateEnable = 1;
	private static int systemRootState = kSystemRootStateUnknow;

	/**
	 * 判断手机是否Root
	 */
	public static boolean isRootSystem() {
		if (systemRootState == kSystemRootStateEnable) {
			return true;
		} else if (systemRootState == kSystemRootStateDisable) {

			return false;
		}
		File f = null;
		final String kSuSearchPaths[] = { "/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/" };
		try {
			for (int i = 0; i < kSuSearchPaths.length; i++) {
				f = new File(kSuSearchPaths[i] + "su");
				if (f != null && f.exists()) {
					systemRootState = kSystemRootStateEnable;
					return true;
				}
			}
		} catch (Exception e) {
		}
		systemRootState = kSystemRootStateDisable;
		return false;
	}

	/**
	 * unicode 转字符串
	 */
	public static String unicode2String(String unicode) {
		StringBuffer string = new StringBuffer();
		String[] hex = unicode.split("\\\\u");
		for (int i = 1; i < hex.length; i++) {
			// 转换出每�?个代码点
			int data = Integer.parseInt(hex[i], 16);
			// 追加成string
			string.append((char) data);
		}
		return string.toString();
	}

	/**
	 * 字符串转换unicode
	 */
	public static String string2Unicode(String string) {
		StringBuffer unicode = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			// 取出每一个字�?
			char c = string.charAt(i);
			// 转换为unicode
			unicode.append("\\u" + Integer.toHexString(c));
		}
		return unicode.toString();
	}

	/**
	 *
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	// 判断有无网络
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			// 如果仅仅是用来判断网络连�?
			// 则可以使�? cm.getActiveNetworkInfo().isAvailable();
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
