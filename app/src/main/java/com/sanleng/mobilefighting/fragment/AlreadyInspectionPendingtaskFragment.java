package com.sanleng.mobilefighting.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.activity.PatrolHandleTaskActivity;
import com.sanleng.mobilefighting.adapter.AlreadyInspectionPendingAdapter;
import com.sanleng.mobilefighting.bean.InSpTaskBean;
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
 * 已处理巡查任务
 *
 * @author Qiaoshi
 */
@SuppressLint("ResourceAsColor")
public class AlreadyInspectionPendingtaskFragment extends BaseFragment {

	private ListView inspetionpehinglslistview;
	private AlreadyInspectionPendingAdapter inspectionPendingAdapter;
	private View view;
	private int pageNo = 1;// 设置pageNo的初始化值为1，即默认获取的是第一页的数据。
	private int allpage;
	private List<InSpTaskBean> allList;// 存放所有数据AlarmBean的list集合
	private List<InSpTaskBean> onelist;// 存放一页数据实体类的Bean
	// 加载提示
	private PromptDialog promptDialog;
	private boolean is_divPage;// 是否进行分页操作
	private boolean finish = true;// 是否加载完成

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.alreadyinspetionpendingtask, null);
		initview();
		return view;
	}

	private void initview() {
		// 创建对象
		promptDialog = new PromptDialog(getActivity());
		// 设置自定义属性
		promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(2000);

		inspectionPendingAdapter = new AlreadyInspectionPendingAdapter();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		pageNo = 1;
		if (pageNo == 1) {
			allList = new ArrayList<InSpTaskBean>();
			onelist = new ArrayList<InSpTaskBean>();
		}
		loadData(pageNo);
		super.onResume();
	}

	//加载数据
	private void loadData(int page) {
		onelist = new ArrayList<InSpTaskBean>();
		RequestParams params = new RequestParams();
		params.put("page", page + "");
		params.put("pageSize", "10");
		params.put("unitcode", PreferenceUtils.getString(getActivity(), "unitcode"));
		params.put("username", PreferenceUtils.getString(getActivity(), "MobileFig_username"));
		params.put("status", "1,2");
		params.put("platformkey", "app_firecontrol_owner");

		RequestUtils.ClientPost(URLs.InspectionTask, params, new NetCallBack() {
			@Override
			public void onStart() {
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
						SIZE = Integer.parseInt(listsize);
						String list = objects.getString("list");

						JSONArray array = new JSONArray(list);
						JSONObject object;
						for (int i = 0; i < array.length(); i++) {
							InSpTaskBean bean = new InSpTaskBean();
							object = (JSONObject) array.get(i);
							String taskId = object.getString("ttask_id");
							String title = object.getString("task_title");
							String content = object.getString("task_content");
							String company = object.getString("organ_name");
							String time = object.getString("last_time");
							String task_status = object.getString("task_status");

							bean.setTaskId(taskId);
							bean.setTitle(title);
							bean.setContent(content);
							bean.setCompany(company);
							bean.setTime(time);
							bean.setState(task_status);
							onelist.add(bean);
						}

						if (SIZE % length == 0) {
							allpage = SIZE / length;
						} else {
							allpage = SIZE / length + 1;
						}

						inspetionpehinglslistview = (ListView) view.findViewById(R.id.alreadyinspetionpehinglslistview);
						allList.addAll(onelist);
						inspectionPendingAdapter.bindData(getActivity(), allList, null);
						if (pageNo == 1) {
							inspetionpehinglslistview.setAdapter(inspectionPendingAdapter);
						}
						inspectionPendingAdapter.notifyDataSetChanged();
						pageNo++;

						finish = true;

						inspetionpehinglslistview.setOnScrollListener(new OnScrollListener() {
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

						inspetionpehinglslistview.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								// TODO Auto-generated method stub
								InSpTaskBean bean = allList.get(position);
								String taskId = bean.getTaskId();
								String title = bean.getTitle();
								String content = bean.getContent();
								String company = bean.getCompany();
								String time = bean.getTime();
								Intent intent = new Intent(getActivity(), PatrolHandleTaskActivity.class);
								intent.putExtra("taskId", taskId);
								intent.putExtra("title", title);
								intent.putExtra("content", content);
								intent.putExtra("company", company);
								intent.putExtra("time", time);
								startActivity(intent);
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

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		pageNo = 1;
		super.onDestroyView();
	}
}
