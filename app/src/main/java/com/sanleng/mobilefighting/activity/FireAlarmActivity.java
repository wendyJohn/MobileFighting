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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.FireAlarmAdapter;
import com.sanleng.mobilefighting.bean.FireAlarmBean;
import com.sanleng.mobilefighting.dialog.ImmediateTreatmentDialog;
import com.sanleng.mobilefighting.dialog.PromptDialog;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;
import com.sanleng.mobilefighting.util.SVProgressHUD;
import com.sanleng.mobilefighting.video.activity.MonitorVideoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 火警信息
 *
 * @author Qiaoshi
 */
@SuppressLint("ResourceAsColor")
public class FireAlarmActivity extends Activity {

	private TextView tab_1, tab_2;// 选项名称
	private LinearLayout l_opa, l_opb;// 选项名称
	private TextView text_today, text_thisweek, text_todays, text_thisweeks, text_thismonths;
	private LinearLayout l_date1, l_date2;
	private ListView firealarmlistview;
	private FireAlarmAdapter fireAlarmAdapter;
	private ImmediateTreatmentDialog immediateTreatmentDialog;
	private RelativeLayout r_back;

	private int pageNo = 1;// 设置pageNo的初始化值为1，即默认获取的是第一页的数据。
	private int allpage;
	private List<FireAlarmBean> allList;// 存放所有数据AlarmBean的list集合
	private List<FireAlarmBean> onelist;// 存放一页数据实体类的Bean
	// 加载提示
	private PromptDialog promptDialog;
	private boolean is_divPage;// 是否进行分页操作
	private boolean finish = true;// 是否加载完成;
	private String scope = "oneday";// 日期
	private String status = "pending";// 状态
	private String type = "待处理";// 状态

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.firealarmactivity);
		initview();
	}

	private void initview() {
		InitTextView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		pageNo = 1;
		if (pageNo == 1) {
			allList = new ArrayList<FireAlarmBean>();
			onelist = new ArrayList<FireAlarmBean>();
		}
		loadData(pageNo);
		super.onResume();
	}

	/**
	 * 初始化头标
	 */
	private void InitTextView() {
		// 创建对象
		promptDialog = new PromptDialog(this);
		// 设置自定义属性
		promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(2000);

		fireAlarmAdapter = new FireAlarmAdapter();

		tab_1 = (TextView) findViewById(R.id.tab_1);
		tab_2 = (TextView) findViewById(R.id.tab_2);

		text_todays = (TextView) findViewById(R.id.text_todays);
		text_thisweeks = (TextView) findViewById(R.id.text_thisweeks);
		text_thismonths = (TextView) findViewById(R.id.text_thismonths);

		text_today = (TextView) findViewById(R.id.text_today);
		text_thisweek = (TextView) findViewById(R.id.text_thisweek);

		l_opa = (LinearLayout) findViewById(R.id.l_opa);
		l_opb = (LinearLayout) findViewById(R.id.l_opb);

		l_date1 = (LinearLayout) findViewById(R.id.l_date1);
		l_date2 = (LinearLayout) findViewById(R.id.l_date2);

		r_back = (RelativeLayout) findViewById(R.id.r_back);
		r_back.setOnClickListener(new MyOnClickListener(99));

		tab_1.setText("待处理");
		tab_2.setText("已处理");

		l_opa.setOnClickListener(new MyOnClickListener(0));
		l_opb.setOnClickListener(new MyOnClickListener(1));

		text_todays.setOnClickListener(new MyOnClickListener(2));
		text_thisweeks.setOnClickListener(new MyOnClickListener(3));
		text_thismonths.setOnClickListener(new MyOnClickListener(4));

		text_today.setOnClickListener(new MyOnClickListener(5));
		text_thisweek.setOnClickListener(new MyOnClickListener(6));

	}

	/**
	 * 头标点击监听
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@SuppressLint("ResourceType")
		public void onClick(View v) {
			switch (index) {
				case 0:
					tab_1.setTextColor(getResources().getColor(R.color.text_blue));
					tab_2.setTextColor(getResources().getColor(R.color.black));

					text_today.setTextColor(getResources().getColor(R.color.white));
					text_thisweek.setTextColor(getResources().getColor(R.color.blue));

					text_today.setBackground(getResources().getDrawable(R.xml.text_rounded));
					text_thisweek.setBackground(getResources().getDrawable(R.xml.text_roundeds));

					l_date1.setVisibility(View.VISIBLE);
					l_date2.setVisibility(View.GONE);

					scope = "oneday";// 日期
					status = "pending";// 状态
					type = "待处理";
					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<FireAlarmBean>();
						onelist = new ArrayList<FireAlarmBean>();
					}
					loadData(pageNo);

					break;
				case 1:
					tab_1.setTextColor(getResources().getColor(R.color.black));
					tab_2.setTextColor(getResources().getColor(R.color.text_blue));
					text_todays.setTextColor(getResources().getColor(R.color.white));
					text_thisweeks.setTextColor(getResources().getColor(R.color.blue));
					text_thismonths.setTextColor(getResources().getColor(R.color.blue));

					text_todays.setBackground(getResources().getDrawable(R.xml.text_rounded));
					text_thisweeks.setBackground(getResources().getDrawable(R.xml.text_roundeds));
					text_thismonths.setBackground(getResources().getDrawable(R.xml.text_roundeds));

					l_date1.setVisibility(View.GONE);
					l_date2.setVisibility(View.VISIBLE);

					scope = "oneday";// 日期
					status = "processed";// 状态
					type = "已处理";
					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<FireAlarmBean>();
						onelist = new ArrayList<FireAlarmBean>();
					}
					loadData(pageNo);

					break;
				case 2:
					text_todays.setTextColor(getResources().getColor(R.color.white));
					text_thisweeks.setTextColor(getResources().getColor(R.color.blue));
					text_thismonths.setTextColor(getResources().getColor(R.color.blue));

					text_todays.setBackground(getResources().getDrawable(R.xml.text_rounded));
					text_thisweeks.setBackground(getResources().getDrawable(R.xml.text_roundeds));
					text_thismonths.setBackground(getResources().getDrawable(R.xml.text_roundeds));

					scope = "oneday";// 日期
					status = "processed";// 状态

					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<FireAlarmBean>();
						onelist = new ArrayList<FireAlarmBean>();
					}
					loadData(pageNo);

					break;
				case 3:
					text_todays.setTextColor(getResources().getColor(R.color.blue));
					text_thisweeks.setTextColor(getResources().getColor(R.color.white));
					text_thismonths.setTextColor(getResources().getColor(R.color.blue));

					text_todays.setBackground(getResources().getDrawable(R.xml.text_roundeds));
					text_thisweeks.setBackground(getResources().getDrawable(R.xml.text_rounded));
					text_thismonths.setBackground(getResources().getDrawable(R.xml.text_roundeds));

					scope = "sevendays";// 日期
					status = "processed";// 状态

					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<FireAlarmBean>();
						onelist = new ArrayList<FireAlarmBean>();
					}
					loadData(pageNo);
					break;
				case 4:
					text_todays.setTextColor(getResources().getColor(R.color.blue));
					text_thisweeks.setTextColor(getResources().getColor(R.color.blue));
					text_thismonths.setTextColor(getResources().getColor(R.color.white));

					text_todays.setBackground(getResources().getDrawable(R.xml.text_roundeds));
					text_thisweeks.setBackground(getResources().getDrawable(R.xml.text_roundeds));
					text_thismonths.setBackground(getResources().getDrawable(R.xml.text_rounded));

					scope = "thirtydays";// 日期
					status = "processed";// 状态

					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<FireAlarmBean>();
						onelist = new ArrayList<FireAlarmBean>();
					}
					loadData(pageNo);

					break;
				case 5:
					text_today.setTextColor(getResources().getColor(R.color.white));
					text_thisweek.setTextColor(getResources().getColor(R.color.blue));

					text_today.setBackground(getResources().getDrawable(R.xml.text_rounded));
					text_thisweek.setBackground(getResources().getDrawable(R.xml.text_roundeds));

					scope = "oneday";// 日期
					status = "pending";// 状态

					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<FireAlarmBean>();
						onelist = new ArrayList<FireAlarmBean>();
					}
					loadData(pageNo);

					break;
				case 6:
					text_today.setTextColor(getResources().getColor(R.color.blue));
					text_thisweek.setTextColor(getResources().getColor(R.color.white));

					text_today.setBackground(getResources().getDrawable(R.xml.text_roundeds));
					text_thisweek.setBackground(getResources().getDrawable(R.xml.text_rounded));

					scope = "sevendays";// 日期
					status = "pending";// 状态

					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<FireAlarmBean>();
						onelist = new ArrayList<FireAlarmBean>();
					}
					loadData(pageNo);
					break;
				case 99:
					finish();
					break;

			}

		}
	}

	// 加载数据
	private void loadData(int page) {
		onelist = new ArrayList<FireAlarmBean>();
		RequestParams params = new RequestParams();
		params.put("page", page + "");
		params.put("pageSize", "10");
		params.put("unitcode", PreferenceUtils.getString(FireAlarmActivity.this, "unitcode"));
		params.put("username", PreferenceUtils.getString(FireAlarmActivity.this, "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");
		params.put("status", status);
		params.put("scope", scope);

		RequestUtils.ClientPost(URLs.FireAlarm_URL, params, new NetCallBack() {
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
							FireAlarmBean bean = new FireAlarmBean();
							object = (JSONObject) array.get(i);

							String taskId = object.getString("ids");
							String receive_time = object.getString("receive_time");
							String device_name = object.getString("device_name");
							String position = object.getString("position");
							String unit_name = object.getString("unit_name");

							bean.setAlarmTime(receive_time);
							bean.setAlarmEquipment(device_name);
							bean.setAlarmPosition(position);
							bean.setAlarmUnit(unit_name);
							bean.setTaskId(taskId);
							onelist.add(bean);
						}

						if (SIZE % length == 0) {
							allpage = SIZE / length;
						} else {
							allpage = SIZE / length + 1;
						}

						firealarmlistview = (ListView) findViewById(R.id.firealarmlistview);
						allList.addAll(onelist);
						fireAlarmAdapter.bindData(FireAlarmActivity.this, allList, type, mHandler);
						if (pageNo == 1) {
							// 没有数据就提示暂无数据。
							firealarmlistview.setEmptyView(findViewById(R.id.nodata));
							firealarmlistview.setAdapter(fireAlarmAdapter);
						}
						fireAlarmAdapter.notifyDataSetChanged();
						pageNo++;

						finish = true;

						firealarmlistview.setOnScrollListener(new OnScrollListener() {
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
									Toast.makeText(FireAlarmActivity.this, "加载完了！", Toast.LENGTH_SHORT).show();
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
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message message) {
			final Bundle data = message.getData();
			switch (message.what) {
				// 立即处理
				case 111111:
					int selIndex = data.getInt("selIndex");
					FireAlarmBean bean = allList.get(selIndex);
					String taskId = bean.getTaskId();
					immediateTreatmentDialog = new ImmediateTreatmentDialog(FireAlarmActivity.this, taskId, mHandler);
					immediateTreatmentDialog.show();
					break;
				// 查看监控
				case 565433:
					int selIndexs = data.getInt("selIndex");
					Intent intent_Inspection = new Intent(FireAlarmActivity.this, MonitorVideoActivity.class);
					startActivity(intent_Inspection);
					break;
				// 位置查看
				case 333333:
					int selIndex_p = data.getInt("selIndex");
					Intent intent_mapfunction = new Intent(FireAlarmActivity.this, MapFunctionActivity.class);
					startActivity(intent_mapfunction);
					break;
				// 火警确认成功
				case 234233:
					SVProgressHUD.showSuccessWithStatus(FireAlarmActivity.this, "火警确认成功");
					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<FireAlarmBean>();
						onelist = new ArrayList<FireAlarmBean>();
					}
					loadData(pageNo);
					break;
				// 火警确认失败
				case 234232:
					SVProgressHUD.showErrorWithStatus(FireAlarmActivity.this, "火警确认失败");
					break;

				default:
					break;
			}
		}
	};

}
