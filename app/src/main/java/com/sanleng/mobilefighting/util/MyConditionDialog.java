package com.sanleng.mobilefighting.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.HomeTopAdapter;
import com.sanleng.mobilefighting.bean.MyListItem;

import java.util.List;

public class MyConditionDialog extends Dialog {

	Context context;
	private TextView message;
	private HomeTopAdapter homeTopAdapter;
	private ListView keylistview;
	private Handler handler;
	private List<MyListItem> list;
	private String mtitle;
	private int type;

	public MyConditionDialog(Context context, List<MyListItem> list, String mtitle, int type, Handler handler) {
		super(context);
		this.context = context;
		this.list = list;
		this.mtitle = mtitle;
		this.type = type;
		this.handler = handler;
	}

	public MyConditionDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.setContentView(R.layout.conditiondialog);
		this.setCancelable(true);// 设置点击屏幕Dialog不消失
		message = (TextView) findViewById(R.id.con_tilte);
		message.setText(mtitle);
		keylistview = (ListView) findViewById(R.id.conlistview);
		homeTopAdapter = new HomeTopAdapter(context, list);
		keylistview.setAdapter(homeTopAdapter);
		homeTopAdapter.notifyDataSetChanged();
		keylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MyListItem item = list.get(position);
				String province = item.getName();
				String myid = item.getPcode();

				if (type == 1) {
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putString("state", province);
					data.putString("id", myid);
					msg.setData(data);
					msg.what = 111111;
					handler.sendMessage(msg);
				}
				if (type == 2) {
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putString("buildids", province);
					data.putString("id", myid);

					msg.setData(data);
					msg.what = 222222;
					handler.sendMessage(msg);
				}
				if (type == 3) {
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putString("floorids", province);
					data.putString("id", myid);
					msg.setData(data);
					msg.what = 333333;
					handler.sendMessage(msg);
				}
				if (type == 4) {
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putString("device_name", province);
					data.putString("id", myid);
					msg.setData(data);
					msg.what = 444444;
					handler.sendMessage(msg);
				}

			}
		});
	}

}
