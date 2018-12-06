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
import com.sanleng.mobilefighting.adapter.EmergencystationAdapter;
import com.sanleng.mobilefighting.bean.ArchitectureBean;

import java.util.ArrayList;
import java.util.List;

public class StorageDialog extends Dialog {

	Context context;
	private TextView message;
	private ListView keylistview;
	private Handler handler;

	private EmergencystationAdapter emergencystationAdapter;
	private List<ArchitectureBean> allList;// 存放所有数据AlarmBean的list集合
	private List<ArchitectureBean> onelist;// 存放一页数据实体类的Bean

	public StorageDialog(Context context, Handler handler) {
		super(context);
		this.context = context;
		this.handler = handler;
	}

	public StorageDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.setContentView(R.layout.monstationdialog);
		this.setCancelable(true);// 设置点击屏幕Dialog不消失
		message = (TextView) findViewById(R.id.con_tilte);
		message.setText("存储位置");
		emergencystationAdapter = new EmergencystationAdapter();
		allList = new ArrayList<ArchitectureBean>();
		onelist = new ArrayList<ArchitectureBean>();
		keylistview = (ListView) findViewById(R.id.conlistview);
		loadData();
	}

	// 加载数据
	private void loadData() {

		ArchitectureBean beana = new ArchitectureBean();
		beana.setName("一号箱");
		beana.setId("A");
		onelist.add(beana);

		ArchitectureBean beanb = new ArchitectureBean();
		beanb.setName("二号箱");
		beanb.setId("B");
		onelist.add(beanb);

		ArchitectureBean beanc = new ArchitectureBean();
		beanc.setName("三号箱");
		beanc.setId("C");
		onelist.add(beanc);

		ArchitectureBean beand = new ArchitectureBean();
		beand.setName("四号箱");
		beand.setId("D");
		onelist.add(beand);

		allList.addAll(onelist);
		emergencystationAdapter.bindData(context, allList);

		// 没有数据就提示暂无数据。
		keylistview.setEmptyView(findViewById(R.id.nodata));
		keylistview.setAdapter(emergencystationAdapter);
		emergencystationAdapter.notifyDataSetChanged();

		keylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ArchitectureBean item = allList.get(position);
				String name = item.getName();
				String myid = item.getId();
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putString("id", myid);
				data.putString("name", name);
				msg.setData(data);
				msg.what = 78326;
				handler.sendMessage(msg);
			}
		});
	}
}
