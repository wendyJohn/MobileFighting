package com.sanleng.mobilefighting.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.myview.CustomCurveChart;

import java.util.ArrayList;
import java.util.List;

/**
 * 水位历史数据
 *
 * @author Qiaoshi
 */
@SuppressLint("ResourceAsColor")
public class WaterleveldataActivity extends Activity {

	private RelativeLayout r_back;
	private TextView position;
	private LinearLayout customCurveChart;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.waterlevedataactivity);
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
		r_back = (RelativeLayout) findViewById(R.id.r_back);
		position = (TextView) findViewById(R.id.position);
		customCurveChart = (LinearLayout) findViewById(R.id.customCurveChart);

		r_back.setOnClickListener(new MyOnClickListener(0));
	}

	// 加载数据
	private void loadData() {
		String[] xLabel = { "0", "2", "4", "6", "8", "10", "12", "14", "16", "18", "20", "22", "24(h)" };
		String[] yLabel = { "0", "1", "2", "3", "4", "5(m)" };
		int[] mydata = { 1, 2, 3, 4, 3, 2, 3, 2, 3, 2, 3, 2, 1 };
		List<int[]> data = new ArrayList<int[]>();
		List<Integer> color = new ArrayList<Integer>();
		data.add(mydata);
		color.add(R.color.color14);
		customCurveChart.addView(new CustomCurveChart(this, xLabel, yLabel, data, color, false));

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
