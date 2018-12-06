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
 * 物质管理操作功能模块界面
 *
 * @author 作者 : Qiaoshi
 *
 */
public class MaterialManagementActivity extends Activity implements OnClickListener, OnItemClickListener {

	private MyGridView itemGrid;
	private FunctionAdapter adapter;
	private RelativeLayout r_back;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.materialmanagementactivity);
		initView();
	}

	// 初始化
	private void initView() {
		r_back = (RelativeLayout) findViewById(R.id.r_back);
		itemGrid = (MyGridView) findViewById(R.id.item_grid);
		adapter = new FunctionAdapter(MaterialManagementActivity.this, R.array.material_management_item,
				R.array.material_management_icon_item);
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
			case 0:// 入库
				Intent intent_Warehousing = new Intent(MaterialManagementActivity.this, MaterialManagementCapture.class);
				intent_Warehousing.putExtra("mode", "Warehousing");
				startActivity(intent_Warehousing);
				break;
			case 1:// 出库
				Intent intent_OutOfStock = new Intent(MaterialManagementActivity.this, MaterialManagementCapture.class);
				intent_OutOfStock.putExtra("mode", "OutOfStock");
				startActivity(intent_OutOfStock);
				break;

			case 2:// 物资查询
				Intent intent_emergencystation = new Intent(MaterialManagementActivity.this, EmergencyStationActivity.class);
				intent_emergencystation.putExtra("mode", "物资查询");
				startActivity(intent_emergencystation);
				break;
			case 3:// 报损
				Intent intent_Reportloss = new Intent(MaterialManagementActivity.this, MaterialManagementCapture.class);
				intent_Reportloss.putExtra("mode", "Reportloss");
				startActivity(intent_Reportloss);
				break;
			case 4:// 盘点

				break;
			case 5:// 站点详情
				Intent intent_emergencystations = new Intent(MaterialManagementActivity.this, EmergencyStationActivity.class);
				intent_emergencystations.putExtra("mode", "站点详情");
				startActivity(intent_emergencystations);
				break;

			case 6:// 维护

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
