package com.sanleng.mobilefighting.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.EmergencystationAdapter;
import com.sanleng.mobilefighting.bean.ArchitectureBean;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MonStationDialog extends Dialog {

	Context context;
	private TextView message;
	private ListView keylistview;
	private Handler handler;

	private EmergencystationAdapter emergencystationAdapter;
	private int pageNo = 1;// 设置pageNo的初始化值为1，即默认获取的是第一页的数据。
	private int allpage;
	private List<ArchitectureBean> allList;// 存放所有数据AlarmBean的list集合
	private List<ArchitectureBean> onelist;// 存放一页数据实体类的Bean
	private boolean is_divPage;// 是否进行分页操作
	private boolean finish = true;// 是否加载完成;

	public MonStationDialog(Context context, Handler handler) {
		super(context);
		this.context = context;
		this.handler = handler;
		loadData(pageNo);
	}

	public MonStationDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.setContentView(R.layout.monstationdialog);
		this.setCancelable(true);// 设置点击屏幕Dialog不消失
		message = (TextView) findViewById(R.id.con_tilte);
		message.setText("站点列表");
		emergencystationAdapter = new EmergencystationAdapter();
		allList = new ArrayList<ArchitectureBean>();
	}

	// 加载数据
	private void loadData(int page) {
		onelist = new ArrayList<ArchitectureBean>();
		RequestParams params = new RequestParams();
		params.put("pageNum", page + "");
		params.put("pageSize", "10");
		params.put("unit_code", PreferenceUtils.getString(context, "unitcode"));
		params.put("username", PreferenceUtils.getString(context, "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");

		RequestUtils.ClientPost(URLs.EmergencyStation_URL, params, new NetCallBack() {
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
							ArchitectureBean bean = new ArchitectureBean();
							object = (JSONObject) array.get(i);
							String name = object.getString("name");
							String mac = object.getString("mac");
							String ids = object.getString("ids");
							String address = object.getString("address");
							bean.setName(name);
							bean.setAddress(address);
							bean.setMac(mac);
							bean.setId(ids);
							onelist.add(bean);
						}

						if (SIZE % length == 0) {
							allpage = SIZE / length;
						} else {
							allpage = SIZE / length + 1;
						}

						keylistview = (ListView) findViewById(R.id.conlistview);
						allList.addAll(onelist);
						emergencystationAdapter.bindData(context, allList);
						if (pageNo == 1) {
							// 没有数据就提示暂无数据。
							keylistview.setEmptyView(findViewById(R.id.nodata));
							keylistview.setAdapter(emergencystationAdapter);
						}
						emergencystationAdapter.notifyDataSetChanged();
						pageNo++;

						finish = true;

						keylistview.setOnScrollListener(new OnScrollListener() {
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
									Toast.makeText(context, "加载完了！", Toast.LENGTH_SHORT).show();
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

						keylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								ArchitectureBean item = allList.get(position);
								String name = item.getName();
								String myid = item.getId();
								String address = item.getAddress();
								Message msg = new Message();
								Bundle data = new Bundle();
								data.putString("address", address);
								data.putString("id", myid);
								data.putString("name", name);
								msg.setData(data);
								msg.what = 78323;
								handler.sendMessage(msg);
							}
						});

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onMyFailure(Throwable arg0) {
			}
		});

	}
}
