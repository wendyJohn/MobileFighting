package com.sanleng.mobilefighting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.dialog.PromptDialog;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 首页单元
 *
 * @author Qiaoshi
 *
 */
public class ManagementCenterActivity extends Activity implements OnClickListener {

	private TextView monitor;
	private TextView inspection_management;
	private TextView productionlinetours;
	private TextView lookuprecord;
	private TextView onebuttonalarm;
	private TextView gridmanagement;
	private TextView whole;
	private TextView article;
	private TextView video;
	private TextView livebroadcast;
	private PromptDialog promptDialog;

//	private AffairAndWork_Dialog affairAndWork_Dialog;
//	// 动画效果
//	private List<ImageView> mImageList;
//	/** 广告条正下方的标语 */
//	private String[] imageDescriptionArray = { //
//			"设备信息的通讯协议", //
//			"及时发现数据异常情况", //
//			"快速统计告警数据信息" };
//	/** 记录上一次点的位置，默认为0 */
//	private int previousPointEnale = 0;
//	private ViewPager mViewPager;
//	private LinearLayout llPointGroup;
//	private TextView tvDescription;
//	/** 记录是否停止循环播放 */
//	private boolean isStop = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.managementcenter_activity);
		initView();
//		获取问题统计
//		ProblemStatistics();

//		initAnimation();
//		try {
//
//			// 开启子线程，让广告条以3秒的频率循环播放
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//
//					while (!isStop) {
//						SystemClock.sleep(3000);
//						if (ManagementCenterActivity.this != null) {
//							ManagementCenterActivity.this.runOnUiThread(new Runnable() {
//								public void run() {
//									mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
//								}
//							});
//						}
//					}
//				}
//			}).start();
//		} catch (Exception e) {
		// TODO: handle exception
//			e.printStackTrace();
//		}

	}

	private void initView() {
		// 创建对象
		promptDialog = new PromptDialog(this);
		// 设置自定义属性
		promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(2000);
		monitor = (TextView) findViewById(R.id.monitor);
		inspection_management = (TextView) findViewById(R.id.inspection_management);
		productionlinetours = (TextView) findViewById(R.id.productionlinetours);
		lookuprecord = (TextView) findViewById(R.id.lookuprecord);
		onebuttonalarm = (TextView) findViewById(R.id.onebuttonalarm);
		gridmanagement = (TextView) findViewById(R.id.gridmanagement);
		whole = (TextView) findViewById(R.id.whole);
		article = (TextView) findViewById(R.id.article);
		video = (TextView) findViewById(R.id.video);
		livebroadcast = (TextView) findViewById(R.id.livebroadcast);

		monitor.setOnClickListener(this);
		inspection_management.setOnClickListener(this);
		productionlinetours.setOnClickListener(this);
		lookuprecord.setOnClickListener(this);
		onebuttonalarm.setOnClickListener(this);
		gridmanagement.setOnClickListener(this);
		whole.setOnClickListener(this);
		article.setOnClickListener(this);
		video.setOnClickListener(this);
		livebroadcast.setOnClickListener(this);

//		mViewPager = (ViewPager) findViewById(R.id.viewpager);
//		mImageList = new ArrayList<ImageView>();
//		int[] imageIds = new int[] { R.drawable.mc_in, R.drawable.mc_in };
//		mViewPager.setAdapter(new MyAdapter());
//		mImageList = new ArrayList<ImageView>();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// 隐患上报
			case R.id.monitor:
				Intent intent_rectification = new Intent(ManagementCenterActivity.this, RectificationActivity.class);
				startActivity(intent_rectification);
				break;
			// 巡查管理
			case R.id.inspection_management:
				Intent intent_Inspection = new Intent(ManagementCenterActivity.this, InspectionManagementActivity.class);
				startActivity(intent_Inspection);
				break;
			// 巡检记录
			case R.id.productionlinetours:

				break;
			// 查岗记录
			case R.id.lookuprecord:

				break;
			// 一键报警
			case R.id.onebuttonalarm:

				break;
			// 网格管理
			case R.id.gridmanagement:

				break;
			// 全部
			case R.id.whole:

				break;
			// 文章
			case R.id.article:

				break;
			// 视频
			case R.id.video:

				break;
			// 直播
			case R.id.livebroadcast:

				break;
			default:
				break;
		}
	}

	private void ProblemStatistics() {
		RequestParams params = new RequestParams();
		params.put("username", PreferenceUtils.getString(ManagementCenterActivity.this, "MobileFig_username"));
		params.put("unitcode", PreferenceUtils.getString(ManagementCenterActivity.this, "unitcode"));
		params.put("platformkey", "app_firecontrol_owner");
		RequestUtils.ClientPost(URLs.ProblemStatistics, params, new NetCallBack() {
			@Override
			public void onStart() {
//				promptDialog.showLoading("正在加载...");
				super.onStart();
			}

			@Override
			public void onMySuccess(String result) {
				if (result == null || result.length() == 0) {
					return;
				}
				System.out.println("数据请求成功" + result);
				try {
					JSONObject jsonObject = new JSONObject(result);
					String msg = jsonObject.getString("msg");
//					promptDialog.dismiss();
					if (msg.equals("获取成功")) {
						JSONObject object = new JSONObject("data");
						String gz = object.getString("gz");
						String fireEngine = object.getString("fireEngine");
						String rectificationSum = object.getString("rectificationSum");

//						String gz=object.getString("gz");
					} else {
						promptDialog.showError(msg);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onMyFailure(Throwable arg0) {
				promptDialog.showError("获取失败");
			}
		});

	}

//	private void initAnimation() {
//		llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
//		tvDescription = (TextView) findViewById(R.id.tv_image_description);
//		mImageList = new ArrayList<ImageView>();
//		int[] imageIds = new int[] { R.drawable.a, R.drawable.c, R.drawable.d };
//		ImageView mImageView;
//		LayoutParams params;
//		// 初始化广告条资源
//		for (int id : imageIds) {
//			mImageView = new ImageView(ManagementCenterActivity.this);
//			mImageView.setBackgroundResource(id);
//			mImageList.add(mImageView);
//
//			// 初始化广告条正下方的"点"
//			View dot = new View(ManagementCenterActivity.this);
//			dot.setBackgroundResource(R.drawable.point_background);
//			params = new LayoutParams(10, 10);
//			params.leftMargin = 10;
//			dot.setLayoutParams(params);
//			dot.setEnabled(false);
//			llPointGroup.addView(dot);
//		}
//		mViewPager = (ViewPager) findViewById(R.id.viewpager);
//		mViewPager.setAdapter(new MyAdapter());
//
//		// 设置广告标语和底部“点”选择状态
//		tvDescription.setText(imageDescriptionArray[0]);
//		llPointGroup.getChildAt(0).setEnabled(true);
//
//		// 设置广告条跳转时，广告语和状态语的变化
//		mViewPager.setOnPageChangeListener(new MyListener());
//
//		// 初始化广告条，当前索引Integer.MAX_VALUE的一半
//		int index = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % mImageList.size());
//		mViewPager.setCurrentItem(index); // 设置当前选中的Page，会触发onPageChangListener.onPageSelected方法
//	}

	/**
	 * ViewPager数据适配器
	 */
//	private class MyAdapter extends PagerAdapter {
//		@Override
//		public int getCount() {
//			// 将viewpager页数设置成Integer.MAX_VALUE，可以模拟无限循环
//			return Integer.MAX_VALUE;
//		}
//
//		/**
//		 * 复用对象 true 复用view false 复用的是Object
//		 */
//		@Override
//		public boolean isViewFromObject(View arg0, Object arg1) {
//			// TODO Auto-generated method stub
//			return arg0 == arg1;
//		}
//
//		/**
//		 * 销毁对象
//		 *
//		 * @param position
//		 *            被销毁对象的索引位置
//		 */
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object) {
//			container.removeView(mImageList.get(position % mImageList.size()));
//		}
//
//		/**
//		 * 初始化一个对象
//		 *
//		 * @param position
//		 *            初始化对象的索引位置
//		 */
//		@Override
//		public Object instantiateItem(ViewGroup container, int position) {
//			container.addView(mImageList.get(position % mImageList.size()));
//			return mImageList.get(position % mImageList.size());
//		}
//	}

//	private class MyListener implements OnPageChangeListener {
//		@Override
//		public void onPageScrollStateChanged(int arg0) {
//			// TODO Auto-generated method stub
//		}
//
//		@Override
//		public void onPageScrolled(int arg0, float arg1, int arg2) {
//			// TODO Auto-generated method stub
//		}
//
//		@Override
//		public void onPageSelected(int arg0) {
//			// 获取新的位置
//			int newPosition = arg0 % imageDescriptionArray.length;
//			// 设置广告标语
//			tvDescription.setText(imageDescriptionArray[newPosition]);
//			// 消除上一次的状态点
//			llPointGroup.getChildAt(previousPointEnale).setEnabled(false);
//			// 设置当前的状态点“点”
//			llPointGroup.getChildAt(newPosition).setEnabled(true);
//			// 记录位置
//			previousPointEnale = newPosition;
//		}
//	}

}
