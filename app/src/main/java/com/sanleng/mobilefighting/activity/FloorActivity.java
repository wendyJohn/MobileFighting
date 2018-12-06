package com.sanleng.mobilefighting.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.FloorAdapter;
import com.sanleng.mobilefighting.bean.ArchitectureBean;
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
 * 电气火灾楼层列表
 *
 * @author Qiaoshi
 */
@SuppressLint("ResourceAsColor")
public class FloorActivity extends Activity {

	private RelativeLayout r_back;
	private ListView floorlistview;
	private List<ArchitectureBean> mylist;
	// 加载提示
	private PromptDialog promptDialog;
	private FloorAdapter floorAdapter;
	private String build_id;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.flooractivity);
		initview();
		loadData();
	}

	private void initview() {
		Intent intent=getIntent();
		build_id=intent.getStringExtra("build_id");
		// 创建对象
		promptDialog = new PromptDialog(this);
		// 设置自定义属性
		promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(2000);
		r_back = (RelativeLayout) findViewById(R.id.r_back);
		r_back.setOnClickListener(new MyOnClickListener(0));
	}

	// 加载建筑数据
	private void loadData() {
		mylist = new ArrayList<ArchitectureBean>();
		RequestParams params = new RequestParams();
		params.put("unit_id", PreferenceUtils.getString(FloorActivity.this, "unitcode"));
		params.put("build_id", build_id);
		params.put("username", PreferenceUtils.getString(FloorActivity.this, "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");

		RequestUtils.ClientPost(URLs.Floor_URL, params, new NetCallBack() {
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
					JSONObject jsonObject = new JSONObject(result);
					String msg = jsonObject.getString("msg");
					if (msg.equals("获取楼层列表成功")) {
						String data = jsonObject.getString("data");
						JSONArray array = new JSONArray(data);
						JSONObject object;
						for (int i = 0; i < array.length(); i++) {
							ArchitectureBean bean = new ArchitectureBean();
							object = (JSONObject) array.get(i);
							String floorids = object.getString("floorids");
							String floor_name = object.getString("floor_name");
							bean.setId(floorids);
							bean.setName(floor_name);
							mylist.add(bean);
						}

						floorlistview = (ListView) findViewById(R.id.floorlistview);
						floorAdapter = new FloorAdapter(FloorActivity.this, mylist);
						floorlistview.setAdapter(floorAdapter);
						floorlistview.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								// TODO Auto-generated method stub
								ArchitectureBean bean = mylist.get(position);
								String floor_id = bean.getId();
								Intent intent = new Intent(FloorActivity.this, DeviceActivity.class);
								intent.putExtra("build_id", build_id);
								intent.putExtra("floor_id", floor_id);
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
			}

		}
	}

}
