package com.sanleng.mobilefighting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 监控单元
 *
 * @author Qiaoshi
 *
 */
public class MonitorCenterActivity extends Activity implements OnClickListener {

	private TextView hostmonitoring;
	private TextView watersystem;
	private TextView electricfire;
	private TextView smokecontrol;
	private TextView fireproofdoor;
	private TextView videoapplication;
	private TextView hostselfinspection;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monitorcenter_activity);
		initView();
		loadData();
		WaterSystemloadData();

	}

	private void initView() {

		hostmonitoring = (TextView) findViewById(R.id.hostmonitoring);
		watersystem = (TextView) findViewById(R.id.watersystem);
		electricfire = (TextView) findViewById(R.id.electricfire);
		smokecontrol = (TextView) findViewById(R.id.smokecontrol);
		fireproofdoor = (TextView) findViewById(R.id.fireproofdoor);
		videoapplication = (TextView) findViewById(R.id.videoapplication);
		hostselfinspection = (TextView) findViewById(R.id.hostselfinspection);

		hostmonitoring.setOnClickListener(this);
		watersystem.setOnClickListener(this);
		electricfire.setOnClickListener(this);
		smokecontrol.setOnClickListener(this);
		fireproofdoor.setOnClickListener(this);
		videoapplication.setOnClickListener(this);
		hostselfinspection.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

			// 主机监测
			case R.id.hostmonitoring:
				Intent intent_hostMonitoring = new Intent(MonitorCenterActivity.this, HostMonitoringActivity.class);
				startActivity(intent_hostMonitoring);
				break;
			// 水系统
			case R.id.watersystem:
				WaterSystemloadData();
				Intent intent_watersystem = new Intent(MonitorCenterActivity.this, WaterSystemActivity.class);
				startActivity(intent_watersystem);
				break;
			// 电气火灾
			case R.id.electricfire:
				Intent intent_architecture = new Intent(MonitorCenterActivity.this, ArchitectureActivity.class);
				startActivity(intent_architecture);
				break;
			// 防排烟
			case R.id.smokecontrol:

				break;
			// 防火门
			case R.id.fireproofdoor:

				break;
			// 视频应用
			case R.id.videoapplication:

				break;
			// 主机自检
			case R.id.hostselfinspection:

				break;
			default:
				break;
		}
	}

	// 缓存火警统计数据
	private void loadData() {
		RequestParams params = new RequestParams();
		params.put("unitcode", PreferenceUtils.getString(MonitorCenterActivity.this, "unitcode"));
		params.put("username", PreferenceUtils.getString(MonitorCenterActivity.this, "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");

		RequestUtils.ClientPost(URLs.FireAlarmStatistics, params, new NetCallBack() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onMySuccess(String result) {
				if (result == null || result.length() == 0) {
					return;
				}

				try {
					JSONObject jsonObject = new JSONObject(result);
					String msg = jsonObject.getString("msg");
					int unhandlefire = jsonObject.getInt("unhandlefire");
					int todayfire = jsonObject.getInt("todayfire");
					int truefire = jsonObject.getInt("truefire");
					int missfire = jsonObject.getInt("missfire");
					int weekfire = jsonObject.getInt("weekfire");

					PreferenceUtils.setInt(MonitorCenterActivity.this, "unhandlefire", unhandlefire);
					PreferenceUtils.setInt(MonitorCenterActivity.this, "todayfire", todayfire);
					PreferenceUtils.setInt(MonitorCenterActivity.this, "truefire", truefire);
					PreferenceUtils.setInt(MonitorCenterActivity.this, "missfire", missfire);
					PreferenceUtils.setInt(MonitorCenterActivity.this, "weekfire", weekfire);

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

	// 提前缓存水系統统计数据
	private void WaterSystemloadData() {
		RequestParams params = new RequestParams();
		params.put("unitcode", PreferenceUtils.getString(MonitorCenterActivity.this, "unitcode"));
		params.put("username", PreferenceUtils.getString(MonitorCenterActivity.this, "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");

		RequestUtils.ClientPost(URLs.WaterSystemStatistics_URL, params, new NetCallBack() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onMySuccess(String result) {
				if (result == null || result.length() == 0) {
					return;
				}

				try {
					JSONObject jsonObject = new JSONObject(result);
					String msg = jsonObject.getString("msg");
					int hyrant = jsonObject.getInt("hyrant");
					int eqt = jsonObject.getInt("eqt");
					int water = jsonObject.getInt("water");

					PreferenceUtils.setInt(MonitorCenterActivity.this, "hyrant", hyrant);
					PreferenceUtils.setInt(MonitorCenterActivity.this, "eqt", eqt);
					PreferenceUtils.setInt(MonitorCenterActivity.this, "water", water);

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
