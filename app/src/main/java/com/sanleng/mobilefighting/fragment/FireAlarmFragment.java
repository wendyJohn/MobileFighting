package com.sanleng.mobilefighting.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.activity.FireAlarmActivity;
import com.sanleng.mobilefighting.dialog.PromptDialog;
import com.sanleng.mobilefighting.myview.DataItem;
import com.sanleng.mobilefighting.myview.DiscView;
import com.sanleng.mobilefighting.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 火警信息
 *
 * @author Qiaoshi
 */
@SuppressLint("ResourceAsColor")
public class FireAlarmFragment extends BaseFragment {

	private TextView tab_1, tab_2;// 选项名称
	private View view;
	private TextView firealarminformation;
	// 加载提示
	private PromptDialog promptDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.firealarmfragment, null);
		initview();
		loadData();
		return view;
	}

	private void initview() {
		// 创建对象
		promptDialog = new PromptDialog(getActivity());
		// 设置自定义属性
		promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(2000);

		tab_1 = (TextView) view.findViewById(R.id.tab_1);
		tab_2 = (TextView) view.findViewById(R.id.tab_2);
		firealarminformation = (TextView) view.findViewById(R.id.firealarminformation);
		firealarminformation.setOnClickListener(new MyOnClickListener(0));
	}

	// 加载数据
	private void loadData() {
		int unhandlefire = PreferenceUtils.getInt(getActivity(), "unhandlefire");
		int todayfire = PreferenceUtils.getInt(getActivity(), "todayfire");
		int truefire = PreferenceUtils.getInt(getActivity(), "truefire");
		int missfire = PreferenceUtils.getInt(getActivity(), "missfire");
		int weekfire = PreferenceUtils.getInt(getActivity(), "weekfire");

		tab_1.setText(unhandlefire + "");
		tab_2.setText(todayfire + "");

		DiscView discView = (DiscView) view.findViewById(R.id.disc);
		List<DataItem> items = new ArrayList<DataItem>();
		items.add(new DataItem(weekfire, "未处理火警", weekfire + "", getResources().getColor(R.color.copa)));
		items.add(new DataItem(truefire, "真实火警", truefire + "", getResources().getColor(R.color.copc)));
		items.add(new DataItem(missfire, "误报火警", missfire + "", getResources().getColor(R.color.copb)));
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
					Intent intent_fireAlarm = new Intent(getActivity(), FireAlarmActivity.class);
					startActivity(intent_fireAlarm);
					break;

			}

		}
	}

}
