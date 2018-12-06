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
import com.sanleng.mobilefighting.adapter.AlreadRectificationAdapter;
import com.sanleng.mobilefighting.bean.RectificationBean;
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
 * 已整改任务
 *
 * @author Qiaoshi
 */
@SuppressLint("ResourceAsColor")
public class AlreadyRectificationFragment extends BaseFragment {

	private ListView alreadyretifiionlslistview;
	private AlreadRectificationAdapter alreadRectificationAdapter;
	private View view;
	private TextView text_todays, text_thisweeks, text_thismonths;
	private TextView problemdescription;
	private int pageNo = 1;// 设置pageNo的初始化值为1，即默认获取的是第一页的数据。
	private int allpage;
	private List<RectificationBean> allList;// 存放所有数据AlarmBean的list集合
	private List<RectificationBean> onelist;// 存放一页数据实体类的Bean
	// 加载提示
//	private PromptDialog promptDialog;
	private boolean is_divPage;// 是否进行分页操作
	private boolean finish = true;// 是否加载完成
	private String scope;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.alreadrectifionfragment, null);
		initview();
		return view;
	}

	private void initview() {
//		// 创建对象
//		promptDialog = new PromptDialog(getActivity());
//		// 设置自定义属性
//		promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(2000);

		alreadRectificationAdapter = new AlreadRectificationAdapter();

		text_todays = (TextView) view.findViewById(R.id.text_todays);
		text_thisweeks = (TextView) view.findViewById(R.id.text_thisweeks);
		text_thismonths = (TextView) view.findViewById(R.id.text_thismonths);
		problemdescription = (TextView) view.findViewById(R.id.problemdescription);

		text_todays.setOnClickListener(new MyOnClickListener(0));
		text_thisweeks.setOnClickListener(new MyOnClickListener(1));
		text_thismonths.setOnClickListener(new MyOnClickListener(2));
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		pageNo = 1;
		if (pageNo == 1) {
			allList = new ArrayList<RectificationBean>();
			onelist = new ArrayList<RectificationBean>();
		}
		scope = "day";
		loadData(pageNo);
		super.onResume();
	}

	//加载数据
	private void loadData(int page) {
		onelist = new ArrayList<RectificationBean>();
		RequestParams params = new RequestParams();
		params.put("pageNum", page + "");
		params.put("pageSize", "10");
		params.put("scope", scope);
		params.put("unitcode", PreferenceUtils.getString(getActivity(), "unitcode"));
		params.put("username", PreferenceUtils.getString(getActivity(), "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");
		params.put("state", "1");
		params.put("type", "0");

		RequestUtils.ClientPost(URLs.RectificationTask, params, new NetCallBack() {
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
					int length = 10;
					int SIZE = 0;
					JSONObject jsonObject = new JSONObject(result);
					String msg = jsonObject.getString("msg");
					if (msg.equals("获取成功")) {
						String data = jsonObject.getString("data");
						JSONObject objects = new JSONObject(data);
						String listsize = objects.getString("total");
						if (scope.equals("day")) {
							problemdescription.setText("今日整改了" + listsize + "处隐患");
						}
						if (scope.equals("week")) {
							problemdescription.setText("本周整改了" + listsize + "处隐患");
						}
						if (scope.equals("month")) {
							problemdescription.setText("本月整改了" + listsize + "处隐患");
						}
						SIZE = Integer.parseInt(listsize);
						String list = objects.getString("list");

						JSONArray array = new JSONArray(list);
						JSONObject object;
						for (int i = 0; i < array.length(); i++) {
							RectificationBean bean = new RectificationBean();
							object = (JSONObject) array.get(i);
							String taskId = object.getString("ids");
							String labelnumber = object.getString("qrcode");
							String devicename = object.getString("fpEquipmentName");
							String ownedBuildingName = object.getString("ownedBuildingName");
							String floorNumber = object.getString("floorNumber");
							String term = object.getString("processingPeriod");
							String reorganizer = object.getString("finished_user");
							String describe = object.getString("description");

							bean.setTaskId(taskId);
							bean.setLabelnumber(labelnumber);
							bean.setDevicename(devicename);
							bean.setDeviceposition(ownedBuildingName + "-" + floorNumber);
							bean.setTerm(term);
							bean.setReorganizer(reorganizer);
							bean.setDescribe(describe);
							onelist.add(bean);
						}

						if (SIZE % length == 0) {
							allpage = SIZE / length;
						} else {
							allpage = SIZE / length + 1;
						}

						alreadyretifiionlslistview = (ListView) view.findViewById(R.id.alreadyretifiionlslistview);
						allList.addAll(onelist);
						alreadRectificationAdapter.bindData(getActivity(), allList);
						if (pageNo == 1) {
							alreadyretifiionlslistview.setEmptyView(view.findViewById(R.id.nodata));
							alreadyretifiionlslistview.setAdapter(alreadRectificationAdapter);
						}
						alreadRectificationAdapter.notifyDataSetChanged();
						pageNo++;

						finish = true;

						alreadyretifiionlslistview.setOnScrollListener(new OnScrollListener() {
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
//						promptDialog.showError(msg);
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

//		myList = new ArrayList<RectificationBean>();
//		RectificationBean beana = new RectificationBean();
//		beana.setLabelnumber("256234678999");
//		beana.setDevicename("111111111111");
//		beana.setDeviceposition("海亮广场");
//		beana.setTerm("2018-11-11");
//		beana.setReorganizer("安保人员");
//		beana.setDescribe("损坏");
//
//		RectificationBean beanb = new RectificationBean();
//		beanb.setLabelnumber("256234678999");
//		beanb.setDevicename("111111111111");
//		beanb.setDeviceposition("海亮广场");
//		beanb.setTerm("2018-11-11");
//		beanb.setReorganizer("安保人员");
//		beanb.setDescribe("损坏");
//
//		RectificationBean beanc = new RectificationBean();
//		beanc.setLabelnumber("256234678999");
//		beanc.setDevicename("111111111111");
//		beanc.setDeviceposition("海亮广场");
//		beanc.setTerm("2018-11-11");
//		beanc.setReorganizer("安保人员");
//		beanc.setDescribe("损坏");
//
//		myList.add(beana);
//		myList.add(beanb);
//		myList.add(beanc);

//		alreadyretifiionlslistview = (ListView) view.findViewById(R.id.alreadyretifiionlslistview);
//		stayRectificationAdapter = new StayRectificationAdapter(getActivity(), myList);
//		alreadyretifiionlslistview.setAdapter(stayRectificationAdapter);
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

					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<RectificationBean>();
						onelist = new ArrayList<RectificationBean>();
					}
					scope = "day";
					loadData(pageNo);
					break;
				case 1:
					text_todays.setTextColor(getResources().getColor(R.color.blue));
					text_thisweeks.setTextColor(getResources().getColor(R.color.white));
					text_thismonths.setTextColor(getResources().getColor(R.color.blue));

					text_todays.setBackground(getResources().getDrawable(R.xml.text_roundeds));
					text_thisweeks.setBackground(getResources().getDrawable(R.xml.text_rounded));
					text_thismonths.setBackground(getResources().getDrawable(R.xml.text_roundeds));

					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<RectificationBean>();
						onelist = new ArrayList<RectificationBean>();
					}
					scope = "week";
					loadData(pageNo);

					break;
				case 2:
					text_todays.setTextColor(getResources().getColor(R.color.blue));
					text_thisweeks.setTextColor(getResources().getColor(R.color.blue));
					text_thismonths.setTextColor(getResources().getColor(R.color.white));

					text_todays.setBackground(getResources().getDrawable(R.xml.text_roundeds));
					text_thisweeks.setBackground(getResources().getDrawable(R.xml.text_roundeds));
					text_thismonths.setBackground(getResources().getDrawable(R.xml.text_rounded));

					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<RectificationBean>();
						onelist = new ArrayList<RectificationBean>();
					}
					scope = "month";
					loadData(pageNo);
					break;
			}
		}
	}
}
