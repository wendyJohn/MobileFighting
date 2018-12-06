package com.sanleng.mobilefighting.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.WatersystemAdapter;
import com.sanleng.mobilefighting.bean.WaterSystemBean;
import com.sanleng.mobilefighting.dialog.PromptDialog;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 水系统详情
 *
 * @author Qiaoshi
 */
@SuppressLint("ResourceAsColor")
public class WaterSystemDetailsActivity extends Activity {

	private ListView wasterdetailslistview;
	private WatersystemAdapter watersystemAdapter;
	private RelativeLayout r_back;
	private int pageNo = 1;// 设置pageNo的初始化值为1，即默认获取的是第一页的数据。
	private int allpage;
	private List<WaterSystemBean> allList;// 存放所有数据AlarmBean的list集合
	private List<WaterSystemBean> onelist;// 存放一页数据实体类的Bean
	// 加载提示
	private PromptDialog promptDialog;
	private boolean is_divPage;// 是否进行分页操作
	private boolean finish = true;// 是否加载完成;
	private int mytype;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.wsdetailsactivity);
		initview();
		loadData(pageNo);
	}

	private void initview() {
		Intent intent = getIntent();
		mytype = intent.getExtras().getInt("type");

		// 创建对象
		promptDialog = new PromptDialog(this);
		// 设置自定义属性
		promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(2000);
		allList = new ArrayList<WaterSystemBean>();
		watersystemAdapter = new WatersystemAdapter();
		r_back = (RelativeLayout) findViewById(R.id.r_back);
		r_back.setOnClickListener(new MyOnClickListener(0));
	}

	// 加载数据
	private void loadData(int page) {
		onelist = new ArrayList<WaterSystemBean>();
		RequestParams params = new RequestParams();
		params.put("page", page + "");
		params.put("pageSize", "10");
		params.put("unitcode", PreferenceUtils.getString(WaterSystemDetailsActivity.this, "unitcode"));
		params.put("username", PreferenceUtils.getString(WaterSystemDetailsActivity.this, "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");
		params.put("fireSysName", "system_name_water");

		RequestUtils.ClientPost(URLs.WaterSystem_URL, params, new NetCallBack() {
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
//				promptDialog.dismiss();
				try {
					int length = 10;
					int SIZE = 0;
					JSONObject jsonObject = new JSONObject(result);
					String msg = jsonObject.getString("msg");
					if (msg.equals("获取成功")) {
						String data = jsonObject.getString("data");
						JSONObject objects = new JSONObject(data);
						String listsize = objects.getString("total");
						SIZE = Integer.parseInt(listsize);
						String list = objects.getString("list");

						JSONArray array = new JSONArray(list);
						JSONObject object;
						for (int i = 0; i < array.length(); i++) {
							WaterSystemBean bean = new WaterSystemBean();
							object = (JSONObject) array.get(i);
							String taskId = object.getString("ids");

							String title = object.getString("device_name");

							String build_name = object.getString("build_name");
							String device_address = object.getString("device_address");

							String current_state = object.getString("current_state");

							String range_min = object.getString("range_min");
							String range_max = object.getString("range_max");
							String state = object.getString("state");
							String device_type = object.getString("device_type");

							if (mytype == 1) {
								bean.setTaskId(taskId);
								bean.setTitle(title);
								bean.setW_address("地址：" + build_name + "-" + device_address);
								bean.setSafetyvalue("当前值：" + current_state);
								bean.setSafetyrange("安全范围：" + range_min + "～" + range_max);
								bean.setState(state);
								bean.setType(device_type);
								onelist.add(bean);
							}
							if (mytype == 2 && device_type.equals("watersystem_level")) {
								bean.setTaskId(taskId);
								bean.setTitle(title);
								bean.setW_address("地址：" + build_name + "-" + device_address);
								bean.setSafetyvalue("当前值：" + current_state);
								bean.setSafetyrange("安全范围：" + range_min + "～" + range_max);
								bean.setState(state);
								bean.setType(device_type);
								onelist.add(bean);
							}

							if (mytype == 3 && device_type.equals("watersystem_hyrant")) {
								bean.setTaskId(taskId);
								bean.setTitle(title);
								bean.setW_address("地址：" + build_name + "-" + device_address);
								bean.setSafetyvalue("当前值：" + current_state);
								bean.setSafetyrange("安全范围：" + range_min + "～" + range_max);
								bean.setState(state);
								bean.setType(device_type);
								onelist.add(bean);
							}

							if (mytype == 4 && device_type.equals("4")) {
								bean.setTaskId(taskId);
								bean.setTitle(title);
								bean.setW_address("地址：" + build_name + "-" + device_address);
								bean.setSafetyvalue("当前值：" + current_state);
								bean.setSafetyrange("安全范围：" + range_min + "～" + range_max);
								bean.setState(state);
								bean.setType(device_type);
								onelist.add(bean);
							}
						}

						if (SIZE % length == 0) {
							allpage = SIZE / length;
						} else {
							allpage = SIZE / length + 1;
						}

						wasterdetailslistview = (ListView) findViewById(R.id.wasterdetailslistview);
						allList.addAll(onelist);
						watersystemAdapter.bindData(WaterSystemDetailsActivity.this, allList, mytype, mHandler);
						if (pageNo == 1) {
							// 没有数据就提示暂无数据。
							wasterdetailslistview.setEmptyView(findViewById(R.id.nodata));
							wasterdetailslistview.setAdapter(watersystemAdapter);
						}
						watersystemAdapter.notifyDataSetChanged();
						pageNo++;

						finish = true;

						wasterdetailslistview.setOnScrollListener(new OnScrollListener() {
							@Override
							public void onScrollStateChanged(AbsListView view, int scrollState) {
								/**
								 * 当分页操作is_divPage为true时、滑动停止时、且pageNo<=allpage（ 这里因为服务端有allpage页数据）时，加载更多数据。
								 */
								if (is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE && pageNo <= allpage
										&& finish) {
									finish = false;
									loadData(pageNo);
								} else if (pageNo > allpage && finish) {
									finish = false;
									// 如果pageNo>allpage则表示，服务端没有更多的数据可供加载了。
									Toast.makeText(WaterSystemDetailsActivity.this, "加载完了！", Toast.LENGTH_SHORT).show();
								}
							}

							// 当：第一个可见的item（firstVisibleItem）+可见的item的个数（visibleItemCount）=
							// 所有的item总数的时候， is_divPage变为TRUE，这个时候才会加载数据。
							@Override
							public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
												 int totalItemCount) {
								is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
							}
						});

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
				promptDialog.showError("加载失败");
			}
		});

	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			final Bundle data = message.getData();
			switch (message.what) {
				// 查看历史数据
				case 22222:
					int selIndex = data.getInt("selIndex");

					Intent intent_waterleveldata = new Intent(WaterSystemDetailsActivity.this,
							WaterleveldataActivity.class);
					startActivity(intent_waterleveldata);

					break;

				default:
					break;
			}
		}
	};

	/**
	 * 头标点击监听
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			switch (index) {
				case 0:
					finish();
					break;
			}

		}
	}

}
