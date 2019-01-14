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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.DeviceItemAdapter;
import com.sanleng.mobilefighting.bean.ArchitectureBean;
import com.sanleng.mobilefighting.bean.MyListItem;
import com.sanleng.mobilefighting.dialog.PromptDialog;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.MyConditionDialog;
import com.sanleng.mobilefighting.util.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 电气火灾设备列表
 *
 * @author Qiaoshi
 */
@SuppressLint("ResourceAsColor")
public class DeviceItemActivity extends Activity {

	private RelativeLayout r_back;
	private ListView deviceitemlistview;
	// 加载提示
	private PromptDialog promptDialog;
	private DeviceItemAdapter deviceItemAdapter;

	private String state = null;
	private String buildids = null;
	private String floorids = null;
	private String device_name = null;

	private int pageNo = 1;// 设置pageNo的初始化值为1，即默认获取的是第一页的数据。
	private int allpage;
	private List<ArchitectureBean> allList;// 存放所有数据AlarmBean的list集合
	private List<ArchitectureBean> onelist;// 存放一页数据实体类的Bean
	// 加载提示
	private boolean is_divPage;// 是否进行分页操作
	private boolean finish = true;// 是否加载完成;
	private View spinner_state;
	private View spinner_architecture;
	private View spinner_floor;
	private View spinner_box;

	private TextView mystate;
	private TextView myarchitecture;
	private TextView myfloor;
	private TextView mybox;

	private List<MyListItem> list;
	private MyConditionDialog myConditionDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.deviceitemactivity);
		initview();
	}

	private void initview() {
		// 创建对象
		promptDialog = new PromptDialog(this);

		r_back = (RelativeLayout) findViewById(R.id.r_back);
		r_back.setOnClickListener(new MyOnClickListener(0));
		deviceItemAdapter = new DeviceItemAdapter();

		spinner_state = (View) findViewById(R.id.spinner_state);
		spinner_architecture = (View) findViewById(R.id.spinner_architecture);
		spinner_floor = (View) findViewById(R.id.spinner_floor);
		spinner_box = (View) findViewById(R.id.spinner_box);

		spinner_state.setOnClickListener(new MyOnClickListener(1));
		spinner_architecture.setOnClickListener(new MyOnClickListener(2));
		spinner_floor.setOnClickListener(new MyOnClickListener(3));
		spinner_box.setOnClickListener(new MyOnClickListener(4));

		mystate = (TextView) findViewById(R.id.state);
		myarchitecture = (TextView) findViewById(R.id.architecture);
		myfloor = (TextView) findViewById(R.id.floor);
		mybox = (TextView) findViewById(R.id.box);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		pageNo = 1;
		if (pageNo == 1) {
			allList = new ArrayList<ArchitectureBean>();
			onelist = new ArrayList<ArchitectureBean>();
		}
		loadData(pageNo);
		super.onResume();
	}

	// 加载数据
	private void loadData(int page) {
		onelist = new ArrayList<ArchitectureBean>();
		RequestParams params = new RequestParams();
		params.put("pageNum", page + "");
		params.put("pageSize", "10");
		params.put("unit_id", PreferenceUtils.getString(DeviceItemActivity.this, "unitcode"));
		params.put("username", PreferenceUtils.getString(DeviceItemActivity.this, "MobileFig_username"));
		params.put("device_name", device_name);
		params.put("platformkey", "app_firecontrol_owner");
		params.put("state", state);
		params.put("buildids", buildids);
		params.put("floorids", floorids);
		params.put("device_name", device_name);

		RequestUtils.ClientPost(URLs.DeviceItem_URL, params, new NetCallBack() {
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
							ArchitectureBean bean = new ArchitectureBean();
							object = (JSONObject) array.get(i);
							String device_id = object.getString("device_id");
							String device_name = object.getString("device_name");

							bean.setId(device_id);
							bean.setName(device_name);
							onelist.add(bean);
						}

						if (SIZE % length == 0) {
							allpage = SIZE / length;
						} else {
							allpage = SIZE / length + 1;
						}

						deviceitemlistview = (ListView) findViewById(R.id.deviceitemlistview);
						allList.addAll(onelist);
						deviceItemAdapter.bindData(DeviceItemActivity.this, allList);
						if (pageNo == 1) {
							// 没有数据就提示暂无数据。
							deviceitemlistview.setEmptyView(findViewById(R.id.nodata));
							deviceitemlistview.setAdapter(deviceItemAdapter);
						}
						deviceItemAdapter.notifyDataSetChanged();
						pageNo++;

						finish = true;

						deviceitemlistview.setOnScrollListener(new OnScrollListener() {
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
									Toast.makeText(DeviceItemActivity.this, "加载完了！", Toast.LENGTH_SHORT).show();
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
						deviceitemlistview.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								// TODO Auto-generated method stub
								ArchitectureBean bean = allList.get(position);
								String device_id = bean.getId();
								Intent intent = new Intent(DeviceItemActivity.this, RealTimeDataActivity.class);
								intent.putExtra("device_id", device_id);
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
				promptDialog.showError("加载失败");
			}
		});

	}

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
				// 状态
				case 1:
					loadstate();
					break;
				// 所属建筑
				case 2:
					loadArchitectureData();
					floorids = null;
					device_name = null;
					myarchitecture.setText("所属建筑");
					myfloor.setText("所属楼层");
					mybox.setText("所属配电箱");
					break;
				// 所属楼层
				case 3:
					device_name = null;
					mybox.setText("所属配电箱");
					if (buildids == null || "".equals(buildids)) {
						Toast.makeText(DeviceItemActivity.this, "请先选择建筑", Toast.LENGTH_LONG).show();
					} else {
						loadFloorData();
					}
					break;
				// 所属配电箱
				case 4:
					if (floorids == null || "".equals(floorids)) {
						Toast.makeText(DeviceItemActivity.this, "请先选择楼层", Toast.LENGTH_LONG).show();
					} else {
						loadDeviceData();
					}
					break;
			}

		}
	}

	//加载状态
	private void loadstate() {
		list = new ArrayList<MyListItem>();
		MyListItem myListItema = new MyListItem();
		myListItema.setName("正常");
		myListItema.setPcode("0");
		MyListItem myListItemb = new MyListItem();
		myListItemb.setName("异常");
		myListItemb.setPcode("1");
		list.add(myListItema);
		list.add(myListItemb);

		myConditionDialog = new MyConditionDialog(DeviceItemActivity.this, list, "状态", 1, m_handler);
		myConditionDialog.show();

	}

	// 加载建筑数据
	private void loadArchitectureData() {
		list = new ArrayList<MyListItem>();
		RequestParams params = new RequestParams();
		params.put("unit_id", PreferenceUtils.getString(DeviceItemActivity.this, "unitcode"));
		params.put("username", PreferenceUtils.getString(DeviceItemActivity.this, "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");

		RequestUtils.ClientPost(URLs.Architecture_URL, params, new NetCallBack() {
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
					JSONObject jsonObject = new JSONObject(result);
					String msg = jsonObject.getString("msg");
					if (msg.equals("获取建筑列表成功")) {
						String data = jsonObject.getString("data");
						JSONArray array = new JSONArray(data);
						JSONObject object;
						for (int i = 0; i < array.length(); i++) {
							MyListItem myListItem = new MyListItem();
							object = (JSONObject) array.get(i);
							String id = object.getString("buildids");
							String build_name = object.getString("build_name");
							myListItem.setName(build_name);
							myListItem.setPcode(id);
							list.add(myListItem);
						}
						myConditionDialog = new MyConditionDialog(DeviceItemActivity.this, list, "所属建筑", 2, m_handler);
						myConditionDialog.show();

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

			}
		});
	}

	// 加载楼层数据
	private void loadFloorData() {
		list = new ArrayList<MyListItem>();
		RequestParams params = new RequestParams();
		params.put("unit_id", PreferenceUtils.getString(DeviceItemActivity.this, "unitcode"));
		params.put("build_id", buildids);
		params.put("username", PreferenceUtils.getString(DeviceItemActivity.this, "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");

		RequestUtils.ClientPost(URLs.Floor_URL, params, new NetCallBack() {
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
					JSONObject jsonObject = new JSONObject(result);
					String msg = jsonObject.getString("msg");
					if (msg.equals("获取楼层列表成功")) {
						String data = jsonObject.getString("data");
						JSONArray array = new JSONArray(data);
						JSONObject object;
						for (int i = 0; i < array.length(); i++) {
							MyListItem myListItem = new MyListItem();
							object = (JSONObject) array.get(i);
							String floorids = object.getString("floorids");
							String floor_name = object.getString("floor_name");
							myListItem.setName(floor_name);
							myListItem.setPcode(floorids);
							list.add(myListItem);
						}
						myConditionDialog = new MyConditionDialog(DeviceItemActivity.this, list, "所属楼层", 3, m_handler);
						myConditionDialog.show();

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

			}
		});

	}

	// 加载所属设备数据
	private void loadDeviceData() {
		list = new ArrayList<MyListItem>();
		RequestParams params = new RequestParams();
		params.put("unit_id", PreferenceUtils.getString(DeviceItemActivity.this, "unitcode"));
		params.put("buildids", buildids);
		params.put("floorids", floorids);
		params.put("username", PreferenceUtils.getString(DeviceItemActivity.this, "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");

		RequestUtils.ClientPost(URLs.Device_URL, params, new NetCallBack() {
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
					JSONObject jsonObject = new JSONObject(result);
					String msg = jsonObject.getString("msg");
					if (msg.equals("获取楼设备列表成功")) {
						String data = jsonObject.getString("data");
						JSONArray array = new JSONArray(data);
						for (int i = 0; i < array.length(); i++) {
							MyListItem myListItem = new MyListItem();
							String name = array.getString(i);
							myListItem.setName(name);
							myListItem.setPcode("111");
							list.add(myListItem);
						}
						myConditionDialog = new MyConditionDialog(DeviceItemActivity.this, list, "所属配电箱", 4, m_handler);
						myConditionDialog.show();
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
			}
		});

	}

	private Handler m_handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 111111:
					myConditionDialog.dismiss();
					Bundle a_bundle = msg.getData();
					String statename = a_bundle.getString("state");
					state = a_bundle.getString("id");
					mystate.setText(statename);

					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<ArchitectureBean>();
						onelist = new ArrayList<ArchitectureBean>();
					}
					loadData(pageNo);

					break;
				case 222222:
					myConditionDialog.dismiss();
					Bundle b_bundle = msg.getData();
					String name = b_bundle.getString("buildids");
					buildids = b_bundle.getString("id");
					myarchitecture.setText(name);

					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<ArchitectureBean>();
						onelist = new ArrayList<ArchitectureBean>();
					}
					loadData(pageNo);

					break;
				case 333333:
					myConditionDialog.dismiss();
					Bundle c_bundle = msg.getData();
					String floorname = c_bundle.getString("floorids");
					floorids = c_bundle.getString("id");
					myfloor.setText(floorname);
					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<ArchitectureBean>();
						onelist = new ArrayList<ArchitectureBean>();
					}
					loadData(pageNo);
					break;
				case 444444:
					myConditionDialog.dismiss();
					Bundle d_bundle = msg.getData();
					device_name = d_bundle.getString("device_name");
					mybox.setText(device_name);
					pageNo = 1;
					if (pageNo == 1) {
						allList = new ArrayList<ArchitectureBean>();
						onelist = new ArrayList<ArchitectureBean>();
					}
					loadData(pageNo);
					break;

				default:
					break;
			}

		}
	};
}
