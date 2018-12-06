package com.sanleng.mobilefighting.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.MisreportAdapter;
import com.sanleng.mobilefighting.bean.FireAlarmBean;
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
 * 误报信息
 *
 * @author Qiaoshi
 */
@SuppressLint("ResourceAsColor")
public class MisreportFragment extends BaseFragment {

	private View view;
	private TextView text_todays, text_thisweeks, text_thismonths;
	private ListView firealarmlistview;
	private MisreportAdapter misreportAdapter;

	private int pageNo = 1;// 设置pageNo的初始化值为1，即默认获取的是第一页的数据。
	private int allpage;
	private List<FireAlarmBean> allList;// 存放所有数据AlarmBean的list集合
	private List<FireAlarmBean> onelist;// 存放一页数据实体类的Bean
	// 加载提示
	private PromptDialog promptDialog;
	private boolean is_divPage;// 是否进行分页操作
	private boolean finish = true;// 是否加载完成;

	private String scope = "oneday";// 日期
	private String status = "processed";// 状态
	private TextView data_text;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.faultfragment, null);
		initview();
		return view;
	}

	private void initview() {
		InitTextView();
	}

	@Override
	public void onResume() {
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
//		// 创建对象
		promptDialog = new PromptDialog(getActivity());
//		// 设置自定义属性
		promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(2000);

		misreportAdapter = new MisreportAdapter();

		text_todays = (TextView) view.findViewById(R.id.text_todays);
		text_thisweeks = (TextView) view.findViewById(R.id.text_thisweeks);
		text_thismonths = (TextView) view.findViewById(R.id.text_thismonths);

		text_todays.setOnClickListener(new MyOnClickListener(0));
		text_thisweeks.setOnClickListener(new MyOnClickListener(1));
		text_thismonths.setOnClickListener(new MyOnClickListener(2));
		data_text = (TextView) view.findViewById(R.id.data_text);
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
				case 1:
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
				case 2:
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
			}
		}
	}

	// 加载数据
	private void loadData(int page) {
		onelist = new ArrayList<FireAlarmBean>();
		RequestParams params = new RequestParams();
		params.put("page", page + "");
		params.put("pageSize", "10");
		params.put("unitcode", PreferenceUtils.getString(getActivity(), "unitcode"));
		params.put("username", PreferenceUtils.getString(getActivity(), "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");
		params.put("status", status);
		params.put("scope", scope);

		RequestUtils.ClientPost(URLs.FireAlarm_URL, params, new NetCallBack() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onMySuccess(String result) {
				if (result == null || result.length() == 0) {
					return;
				}
				System.out.println("误报数据请求成功" + result);
				try {
					int length = 10;
					int SIZE = 0;
					JSONObject jsonObject = new JSONObject(result);
					String msg = jsonObject.getString("msg");
					if (msg.equals("获取成功")) {
						String data = jsonObject.getString("data");
						JSONObject objects = new JSONObject(data);
						String listsize = objects.getString("total");
						if (scope.equals("oneday")) {
							data_text.setText("今日共有" + listsize + "起误报");
						}

						if (scope.equals("sevendays")) {
							data_text.setText("本周共有" + listsize + "起误报");
						}
						if (scope.equals("thirtydays")) {
							data_text.setText("本月共有" + listsize + "起误报");
						}
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
							String alarm_type = object.getString("alarm_type");
							if (alarm_type.equals("102")) {
								bean.setAlarmTime(receive_time);
								bean.setAlarmEquipment(device_name);
								bean.setAlarmPosition(position);
								bean.setAlarmUnit(unit_name);
								bean.setTaskId(taskId);
								onelist.add(bean);
							}
						}

						if (SIZE % length == 0) {
							allpage = SIZE / length;
						} else {
							allpage = SIZE / length + 1;
						}

						firealarmlistview = (ListView) view.findViewById(R.id.firealarmlistview);
						allList.addAll(onelist);
						misreportAdapter.bindData(getActivity(), allList);
						if (pageNo == 1) {
							// 没有数据就提示暂无数据。
							firealarmlistview.setEmptyView(view.findViewById(R.id.nodata));
							firealarmlistview.setAdapter(misreportAdapter);
						}
						misreportAdapter.notifyDataSetChanged();
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
									Toast.makeText(getActivity(), "加载完了！", Toast.LENGTH_SHORT).show();
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
//				promptDialog.showError("加载失败");
			}
		});

	}
}
