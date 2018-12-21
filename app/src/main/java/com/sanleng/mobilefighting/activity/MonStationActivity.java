package com.sanleng.mobilefighting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.FunctionAdapter;
import com.sanleng.mobilefighting.myview.MyGridView;

/**
 *应急站
 * @author 作者 : Qiaoshi
 *
 */
public class MonStationActivity extends Activity implements OnClickListener, OnItemClickListener {

	private MyGridView itemGrid;
	private FunctionAdapter adapter;
	private RelativeLayout r_back;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monstationactivity);
		initView();
	}

	// 初始化
	private void initView() {
		r_back = (RelativeLayout) findViewById(R.id.r_back);
		itemGrid = (MyGridView) findViewById(R.id.item_grid);
		adapter = new FunctionAdapter(MonStationActivity.this, R.array.myfunction_name_items,
				R.array.myfunction_icon_items);
		itemGrid.setAdapter(adapter);
		itemGrid.setOnItemClickListener(this);
		r_back.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
		switch (position) {
			case 0:// 物资管理
				Intent intent_materialmanagement = new Intent(MonStationActivity.this, MaterialManagementActivity.class);
				startActivity(intent_materialmanagement);
				break;
			case 1:// 应急开门
				Intent intent_emergencyStation = new Intent(MonStationActivity.this, EmergencyStationActivity.class);
				intent_emergencyStation.putExtra("mode", "应急开门");
				startActivity(intent_emergencyStation);
				break;
			case 2:// 签到巡逻

				break;
			case 3:// 隐患处理

				break;
			case 4:// 我的消息

				break;
			case 5:// 培训活动

				break;

			case 6:// 在线求助

				break;
			case 7:// 个人中心

				break;
			case 8:// 授权中心

				break;
			default:
				break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.r_back:
				finish();
				break;
			default:
				break;
		}
	}
}
