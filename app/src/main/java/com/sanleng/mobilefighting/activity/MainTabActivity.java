package com.sanleng.mobilefighting.activity;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.dialog.FireTipsDialog;
import com.sanleng.mobilefighting.dialog.NoticeDialog;
import com.sanleng.mobilefighting.fragment.HomeFragment;
import com.sanleng.mobilefighting.fragment.MapFunctionFragment;
import com.sanleng.mobilefighting.fragment.MonitorCenterFragment;
import com.sanleng.mobilefighting.fragment.NewMineFragment;
import com.sanleng.mobilefighting.util.DrawableUtil;

import org.xclcharts.common.DensityUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 首页
 */

public class MainTabActivity extends FragmentActivity implements View.OnClickListener {

	private static String[] stringArr;

	private static final int[] RADIO_IDS = new int[] { R.id.tab_life, R.id.tab_profit, R.id.tab_invitation,
			R.id.tab_personal };
	// 默认图片
	private static final int[] TAB_ICON_NORMAL_IDS = new int[] { R.drawable.hp_on, R.drawable.monitor_on,
			R.drawable.journal_on, R.drawable.personal_on };
	// 点击图片
	private static final int[] TAB_ICON_ACTIVE_IDS = new int[] { R.drawable.hp_in, R.drawable.monitor_in,
			R.drawable.journal_in, R.drawable.personal_in };
	private FragmentManager mFragmentManager;
	private HomeFragment f1;
	private MonitorCenterFragment f2;
	private MapFunctionFragment f3;
	private NewMineFragment f4;

	private RadioGroup mRadioGroup;
	private int mBottomDrawableSize;
	private RadioButton btn1, btn2, btn3, btn4;
	public static int WIDTH = 0;
	public static int HEIGHT = 0;
	static LinearLayout linearLayout;
	public static final String SETTINGS_ACTION = "android.settings.APPLICATION_DETAILS_SETTINGS";
	private NoticeDialog noticeDialog;
	private LineStatusReceiver linestatusReceiver;
	//	public static final String LINE_ACTION = "LINE_ACTION";
	private FireTipsDialog fireTipsDialog;

	private static final String BROADCAST_PERMISSION_DISC = "com.permissions.MY_BROADCAST";
	private static final String BROADCAST_ACTION_DISC = "com.permissions.my_broadcast";

	private static final String BROADCAST_PERMISSION_DISCS = "com.permissions.MY_BROADCASTS";
	private static final String BROADCAST_ACTION_DISCS = "com.permissions.my_broadcasts";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}

		// 消息通知栏是否打开
		if (isNotificationEnabled(MainTabActivity.this) == false) {
			noticeDialog = new NoticeDialog(MainTabActivity.this, m_handler);
			noticeDialog.show();
		}

		setContentView(R.layout.activity_miantab);
		WindowManager wm = this.getWindowManager();
		WIDTH = wm.getDefaultDisplay().getWidth();
		HEIGHT = wm.getDefaultDisplay().getHeight();
		float scale = this.getResources().getDisplayMetrics().density;
		initView();
	}

	private void initView() {
		linestatusReceiver = new LineStatusReceiver();
		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(LINE_ACTION);
//		registerReceiver(linestatusReceiver, intentFilter);
		intentFilter.addAction(BROADCAST_ACTION_DISC); // 只有持有相同的action的接受者才能接收此广
		registerReceiver(linestatusReceiver, intentFilter, BROADCAST_PERMISSION_DISC, null);

		mRadioGroup = (RadioGroup) findViewById(R.id.bottom_tab_group);
		mFragmentManager = getSupportFragmentManager();
		mBottomDrawableSize = DensityUtil.dip2px(this, 30);
		mRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
		btn1 = (RadioButton) findViewById(R.id.tab_life);
		btn2 = (RadioButton) findViewById(R.id.tab_profit);
		btn3 = (RadioButton) findViewById(R.id.tab_invitation);
		btn4 = (RadioButton) findViewById(R.id.tab_personal);
		linearLayout = (LinearLayout) findViewById(R.id.login_delete);
		findViewById(R.id.home_close).setOnClickListener(this);
		findViewById(R.id.home_delete).setOnClickListener(this);
		showFragment(0);
		setBottomTextColor(0);
	}

	private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			switch (checkedId) {
				case R.id.tab_life:
					showFragment(0);
					setBottomTextColor(0);
					break;
				case R.id.tab_profit:
					showFragment(1);
					setBottomTextColor(1);
					break;
				case R.id.tab_invitation:
					showFragment(2);
					setBottomTextColor(2);
					break;
				case R.id.tab_personal:
					showFragment(3);
					setBottomTextColor(3);
					break;

				default:
					break;
			}
		}
	};

	public static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			linearLayout.setVisibility(View.VISIBLE);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.home_close:
				Message msg1 = new Message();
				msg1.what = 1;// qux
				new HomeFragment().handler.sendMessage(msg1);
				linearLayout.setVisibility(View.GONE);
				break;
			case R.id.home_delete:
				Message msg2 = new Message();
				msg2.what = 2;// 删除
				new HomeFragment().handler.sendMessage(msg2);
				linearLayout.setVisibility(View.GONE);
				break;
		}
	}

	private void showFragment(int page) {
		FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
		if (f1 != null)
			mFragmentTransaction.hide(f1);
		if (f2 != null)
			mFragmentTransaction.hide(f2);
		if (f3 != null)
			mFragmentTransaction.hide(f3);
		if (f4 != null)
			mFragmentTransaction.hide(f4);
		switch (page) {
			case 0:// 首页
				if (f1 == null) {
					f1 = new HomeFragment();

					mFragmentTransaction.add(R.id.main_content, f1);
				} else {
					mFragmentTransaction.show(f1);
				}
				break;
			case 1://

				if (f2 != null) {
					mFragmentTransaction.remove(f2);
				}
				f2 = new MonitorCenterFragment();
				mFragmentTransaction.add(R.id.main_content, f2);
				break;
			case 2://
				if (f3 == null) {
					f3 = new MapFunctionFragment();
					Bundle bundle = new Bundle();
					bundle.putStringArray("aMap", stringArr);
					f3.setArguments(bundle);
					mFragmentTransaction.add(R.id.main_content, f3);
				} else {
					mFragmentTransaction.show(f3);
				}
				break;
			case 3:// 我的
				if (f4 == null) {
					f4 = new NewMineFragment();
					mFragmentTransaction.add(R.id.main_content, f4);
				} else {
					mFragmentTransaction.show(f4);
				}
				break;
			default:
				break;
		}
		mFragmentTransaction.commitAllowingStateLoss();

	}

	private void setBottomTextColor(int position) {
		btn1.setTextColor(getResources().getColor(R.color.gray_bold));
		btn2.setTextColor(getResources().getColor(R.color.gray_bold));
		btn3.setTextColor(getResources().getColor(R.color.gray_bold));
		btn4.setTextColor(getResources().getColor(R.color.gray_bold));
		Drawable d1 = DrawableUtil.getDrawable(MainTabActivity.this, TAB_ICON_NORMAL_IDS[0]);

		d1.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
		btn1.setCompoundDrawables(null, d1, null, null);
		Drawable d2 = DrawableUtil.getDrawable(MainTabActivity.this, TAB_ICON_NORMAL_IDS[1]);
		d2.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
		btn2.setCompoundDrawables(null, d2, null, null);
		Drawable d3 = DrawableUtil.getDrawable(MainTabActivity.this, TAB_ICON_NORMAL_IDS[2]);
		d3.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
		btn3.setCompoundDrawables(null, d3, null, null);
		Drawable d4 = DrawableUtil.getDrawable(MainTabActivity.this, TAB_ICON_NORMAL_IDS[3]);
		d4.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
		btn4.setCompoundDrawables(null, d4, null, null);
		switch (position) {
			case 0:
				Drawable dz = DrawableUtil.getDrawable(MainTabActivity.this, TAB_ICON_ACTIVE_IDS[0]);
				dz.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
				btn1.setTextColor(getResources().getColor(R.color.text_blue));
				btn1.setCompoundDrawables(null, dz, null, null);
				break;
			case 1:
				Drawable dz1 = DrawableUtil.getDrawable(MainTabActivity.this, TAB_ICON_ACTIVE_IDS[1]);
				dz1.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
				btn2.setTextColor(getResources().getColor(R.color.text_blue));
				btn2.setCompoundDrawables(null, dz1, null, null);
				break;
			case 2:
				Drawable dz2 = DrawableUtil.getDrawable(MainTabActivity.this, TAB_ICON_ACTIVE_IDS[2]);
				dz2.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
				btn3.setTextColor(getResources().getColor(R.color.text_blue));
				btn3.setCompoundDrawables(null, dz2, null, null);
				break;
			case 3:
				Drawable dz3 = DrawableUtil.getDrawable(MainTabActivity.this, TAB_ICON_ACTIVE_IDS[3]);
				dz3.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
				btn4.setTextColor(getResources().getColor(R.color.text_blue));
				btn4.setCompoundDrawables(null, dz3, null, null);
				break;

			default:
				break;
		}

	}

	private void setTabChecked(int position) {
		int count = RADIO_IDS.length;
		for (int index = 0; index < count; index++) {
			RadioButton radioButton = (RadioButton) findViewById(RADIO_IDS[index]);
			if (index == position) {
				radioButton.setTextColor(getResources().getColor(R.color.text_blue));
				Drawable d = DrawableUtil.getDrawable(MainTabActivity.this, TAB_ICON_ACTIVE_IDS[index]);
				d.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
				radioButton.setCompoundDrawables(null, d, null, null);
			} else {
				radioButton.setTextColor(MainTabActivity.this.getResources().getColor(R.color.text_blue));
				Drawable d = DrawableUtil.getDrawable(MainTabActivity.this, TAB_ICON_NORMAL_IDS[index]);
				d.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
				radioButton.setCompoundDrawables(null, d, null, null);
			}
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(linestatusReceiver);
		super.onDestroy();
	}

	//判断消息通知栏是否打开
	private boolean isNotificationEnabled(Context context) {
		String CHECK_OP_NO_THROW = "checkOpNoThrow";
		String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

		AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
		ApplicationInfo appInfo = context.getApplicationInfo();
		String pkg = context.getApplicationContext().getPackageName();
		int uid = appInfo.uid;

		Class appOpsClass = null;
		/* Context.APP_OPS_MANAGER */
		try {
			appOpsClass = Class.forName(AppOpsManager.class.getName());
			Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
					String.class);
			Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

			int value = (Integer) opPostNotificationValue.get(Integer.class);
			return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

	private Handler m_handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 76565:
					goToNotificationSetting(MainTabActivity.this);
					break;
				default:
					break;
			}

		}
	};

	/**
	 * 打开允许通知的设置页
	 */
	private void goToNotificationSetting(Context context) {
		Intent intent = new Intent();
		if (Build.VERSION.SDK_INT >= 26) {
			// android 8.0引导
			intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
			intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
		} else if (Build.VERSION.SDK_INT >= 21) {
			// android 5.0-7.0
			intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
			intent.putExtra("app_package", context.getPackageName());
			intent.putExtra("app_uid", context.getApplicationInfo().uid);
		} else {
			// 其他
			intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			intent.setData(Uri.fromParts("package", context.getPackageName(), null));
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	// 火警广播处理
	public class LineStatusReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BROADCAST_ACTION_DISC)) {

				Intent myintent = new Intent(BROADCAST_ACTION_DISCS);
				context.sendBroadcast(myintent, BROADCAST_PERMISSION_DISCS);
//				Intent myintent = new Intent(BROADCAST_ACTION_DISC);
//				context.sendBroadcast(myintent);
				String str = intent.getStringExtra("str_test");
				fireTipsDialog = new FireTipsDialog(context, str, mHandler);
				Window window = fireTipsDialog.getWindow();
				WindowManager.LayoutParams layoutParams = window.getAttributes();
				layoutParams.gravity = Gravity.TOP;
				layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
				window.setAttributes(layoutParams);
				fireTipsDialog.show();
			}
		}
	}

	private Handler mHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
				case 32323:
					Intent intent_fireAlarm = new Intent(MainTabActivity.this, FireAlarmActivity.class);
					startActivity(intent_fireAlarm);
					break;

				default:
					break;
			}
		}
	};
}
