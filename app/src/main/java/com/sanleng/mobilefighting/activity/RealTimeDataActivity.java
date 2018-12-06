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
import com.sanleng.mobilefighting.adapter.RealtimedataAdapter;
import com.sanleng.mobilefighting.bean.InSpTaskBean;
import com.sanleng.mobilefighting.dialog.PromptDialog;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 电气火灾设备实时数据
 *
 * @author Qiaoshi
 */
@SuppressLint("ResourceAsColor")
public class RealTimeDataActivity extends Activity {

	private RelativeLayout r_back;
	private ListView realtimedatalistview;
	private List<InSpTaskBean> mylist;
	// 加载提示
	private PromptDialog promptDialog;
	private RealtimedataAdapter realtimedataAdapter;
	private String device_id;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.realtimedataactivity);
		initview();
		loadData();
	}

	private void initview() {
		Intent intent = getIntent();
		device_id = intent.getStringExtra("device_id");
		promptDialog = new PromptDialog(this);
		r_back = (RelativeLayout) findViewById(R.id.r_back);
		r_back.setOnClickListener(new MyOnClickListener(0));
	}

	// 加载建筑数据
	private void loadData() {
		mylist = new ArrayList<InSpTaskBean>();
		RequestParams params = new RequestParams();
		params.put("device_id", device_id);
		params.put("username", "admin");
		params.put("platformkey", "app_firecontrol_owner");

		RequestUtils.ClientPost(URLs.RealTimeData_URL, params, new NetCallBack() {
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
					if (msg.equals("获取数据成功")) {
						String data = jsonObject.getString("data");
						JSONObject Object = new JSONObject(data);
						String electricalDetectorInfos = Object.getString("electricalDetectorInfos");
						JSONArray array = new JSONArray(electricalDetectorInfos);
						JSONObject object;
						for (int i = 0; i < array.length(); i++) {
							InSpTaskBean bean = new InSpTaskBean();
							object = (JSONObject) array.get(i);

							String detector_id = object.getString("detector_id");
							String detector_portVal = object.getString("detector_portVal");

							String realtime_data = object.getString("realtime_data");
							String measurement_unit = object.getString("measurement_unit");

							String lower_limit = object.getString("lower_limit");
							String upper_limit = object.getString("upper_limit");

							String state = object.getString("state");

							bean.setTaskId(detector_id);
							bean.setTitle(detector_portVal);
							bean.setContent(realtime_data + " " + measurement_unit);
							bean.setCompany(lower_limit + "～" + upper_limit + " " + measurement_unit);
							bean.setState(state);
							mylist.add(bean);
						}

						realtimedatalistview = (ListView) findViewById(R.id.realtimedatalistview);
						realtimedataAdapter = new RealtimedataAdapter(RealTimeDataActivity.this, mylist);
						realtimedatalistview.setAdapter(realtimedataAdapter);
						realtimedatalistview.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								// TODO Auto-generated method stub
								InSpTaskBean bean = mylist.get(position);
								String device_id = bean.getTaskId();
								Intent intent = new Intent(RealTimeDataActivity.this, RealtimeleveldataActivity.class);
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
			}

		}
	}

}
