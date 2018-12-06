package com.sanleng.mobilefighting.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.myview.DataItem;
import com.sanleng.mobilefighting.myview.DiscView;
import com.sanleng.mobilefighting.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 水系统
 *
 * @author Qiaoshi
 */
@SuppressLint("ResourceAsColor")
public class WaterSystemActivity extends Activity {

	private TextView viewthedetails;
	private RelativeLayout r_back;
	private TextView tank;
	private TextView firehydrant;
	private TextView firepump;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.watersystemactivity);
		initview();
		loadData();
	}

	private void initview() {
		InitTextView();
	}

	/**
	 * 初始化头标
	 */
	private void InitTextView() {
		viewthedetails = (TextView) findViewById(R.id.viewthedetails);
		r_back = (RelativeLayout) findViewById(R.id.r_back);

		tank = (TextView) findViewById(R.id.tank);
		firehydrant = (TextView) findViewById(R.id.firehydrant);
		firepump = (TextView) findViewById(R.id.firepump);

		viewthedetails.setOnClickListener(new MyOnClickListener(0));
		r_back.setOnClickListener(new MyOnClickListener(1));

		tank.setOnClickListener(new MyOnClickListener(2));
		firehydrant.setOnClickListener(new MyOnClickListener(3));
		firepump.setOnClickListener(new MyOnClickListener(4));

	}

	//加载数据
	private void loadData() {

		int hyrant = PreferenceUtils.getInt(WaterSystemActivity.this, "hyrant");
		int eqt = PreferenceUtils.getInt(WaterSystemActivity.this, "eqt");
		int water = PreferenceUtils.getInt(WaterSystemActivity.this, "water");

		DiscView discView = (DiscView) findViewById(R.id.disc_waster);
		List<DataItem> items = new ArrayList<DataItem>();
		items.add(new DataItem(hyrant, "水压异常", hyrant + "", getResources().getColor(R.color.copa)));
		items.add(new DataItem(eqt, "水位异常", eqt + "", getResources().getColor(R.color.copc)));
		items.add(new DataItem(water, "设备异常", water + "", getResources().getColor(R.color.copb)));
		discView.setItems(items);

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
					Intent intent_waterSystema = new Intent(WaterSystemActivity.this, WaterSystemDetailsActivity.class);
					intent_waterSystema.putExtra("type", 1);
					startActivity(intent_waterSystema);
					break;
				case 1:
					finish();
					break;
				case 2:
					Intent intent_waterSystemb = new Intent(WaterSystemActivity.this, WaterSystemDetailsActivity.class);
					intent_waterSystemb.putExtra("type", 2);
					startActivity(intent_waterSystemb);
					break;
				case 3:
					Intent intent_waterSystemc = new Intent(WaterSystemActivity.this, WaterSystemDetailsActivity.class);
					intent_waterSystemc.putExtra("type", 3);
					startActivity(intent_waterSystemc);
					break;
				case 4:
					Intent intent_waterSystemd = new Intent(WaterSystemActivity.this, WaterSystemDetailsActivity.class);
					intent_waterSystemd.putExtra("type", 4);
					startActivity(intent_waterSystemd);
					break;
			}

		}
	}

}
