package com.sanleng.mobilefighting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.sanleng.mobilefighting.R;

/**
 * 巡查管理
 *
 * @author Qiaoshi
 *
 */
public class InspectionManagementActivity extends Activity implements OnClickListener {

	private RelativeLayout r_firepatrol;
	private RelativeLayout r_inspecting;
	private RelativeLayout r_inspectiontask;
	private RelativeLayout r_hiddentrouble;
	private RelativeLayout r_sticker;
	private RelativeLayout r_phoneticlookup;
	private RelativeLayout r_back;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inspectionmanagement_activity);
		initView();

	}

	private void initView() {
		r_firepatrol = (RelativeLayout) findViewById(R.id.r_firepatrol);
		r_inspecting = (RelativeLayout) findViewById(R.id.r_inspecting);
		r_inspectiontask = (RelativeLayout) findViewById(R.id.r_inspectiontask);
		r_hiddentrouble = (RelativeLayout) findViewById(R.id.r_hiddentrouble);
		r_sticker = (RelativeLayout) findViewById(R.id.r_sticker);
		r_phoneticlookup = (RelativeLayout) findViewById(R.id.r_phoneticlookup);
		r_back = (RelativeLayout) findViewById(R.id.r_back);

		r_firepatrol.setOnClickListener(this);
		r_inspecting.setOnClickListener(this);
		r_inspectiontask.setOnClickListener(this);
		r_hiddentrouble.setOnClickListener(this);
		r_sticker.setOnClickListener(this);
		r_phoneticlookup.setOnClickListener(this);
		r_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

			// 巡检点扫描
			case R.id.r_firepatrol:
				Intent intent_patrolpointscanning = new Intent(InspectionManagementActivity.this,
						PatrolPointScanningActivity.class);
				intent_patrolpointscanning.putExtra("type", 1);
				startActivity(intent_patrolpointscanning);
				break;
			// 巡查助手
			case R.id.r_inspecting:
				Intent intent_inspectingAssistant = new Intent(InspectionManagementActivity.this,
						InspectingAssistantActivity.class);
				startActivity(intent_inspectingAssistant);
				break;
			// 巡查任务
			case R.id.r_inspectiontask:
				Intent intent_inspectionMission = new Intent(InspectionManagementActivity.this,
						InspectionMissionActivity.class);
				startActivity(intent_inspectionMission);
				break;
			// 隐患整改
			case R.id.r_hiddentrouble:
				Intent intent_rectification = new Intent(InspectionManagementActivity.this, RectificationActivity.class);
				startActivity(intent_rectification);

				break;
			// 贴贴
			case R.id.r_sticker:

				break;
			// 语音查岗
			case R.id.r_phoneticlookup:

				break;
			// 返回
			case R.id.r_back:
				finish();
				break;
			default:
				break;
		}
	}
}
