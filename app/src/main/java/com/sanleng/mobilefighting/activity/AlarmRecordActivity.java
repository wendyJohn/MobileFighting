package com.sanleng.mobilefighting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.AlarmAdapter;
import com.sanleng.mobilefighting.adapter.PatrolRecordAdapter;
import com.sanleng.mobilefighting.bean.InSpTaskBean;
import com.sanleng.mobilefighting.dialog.PromptDialog;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报警记录
 *
 * @author Qiaoshi
 *
 */
public class AlarmRecordActivity extends Activity implements OnClickListener {

	private RelativeLayout r_back;
	private ListView alarmlistview;
	private int pageNo = 1;// 设置pageNo的初始化值为1，即默认获取的是第一页的数据。
	private int allpage;
//	private List<InSpTaskBean> allList;// 存放所有数据AlarmBean的list集合
//	private List<InSpTaskBean> onelist;// 存放一页数据实体类的Bean
	private boolean is_divPage;// 是否进行分页操作
	private boolean finish = true;// 是否加载完成;
	private AlarmAdapter alarmAdapter;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarmrecordactivity);
		initView();
		loadData();
	}

	private void initView() {
//		Intent intent = getIntent();
		r_back = (RelativeLayout) findViewById(R.id.r_back);
		r_back.setOnClickListener(this);
	}


	// 加载数据
	private void loadData(){
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("name", "设备名称："+"电流");
		mapa.put("postion", "设备位置：" + "南京市江宁区悠谷");
		mapa.put("time",  "报警时间：" + "2018/12/13 00:00");

		Map<String, Object> mapb = new HashMap<String, Object>();
		mapb.put("name", "设备名称："+"电压");
		mapb.put("postion", "设备位置：" + "南京市江宁区悠谷");
		mapb.put("time",  "报警时间：" + "2018/12/13 00:00");

		Map<String, Object> mapc = new HashMap<String, Object>();
		mapc.put("name", "设备名称："+"温度");
		mapc.put("postion", "设备位置：" + "南京市江宁区悠谷");
		mapc.put("time",  "报警时间：" + "2018/12/13 00:00");

		list.add(mapa);
		list.add(mapb);
		list.add(mapc);
		alarmlistview=findViewById(R.id.alarmlistview);
		alarmAdapter=new AlarmAdapter(AlarmRecordActivity.this,list);
		alarmlistview.setAdapter(alarmAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// 返回
			case R.id.r_back:
				finish();
				break;
			default:
				break;
		}
	}
}
