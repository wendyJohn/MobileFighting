package com.sanleng.mobilefighting.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.AlarmAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史报警记录
 *
 * @author Qiaoshi
 *
 */
public class AlarmRecordFragment extends Fragment{

	private ListView alarmlistview;
	private int pageNo = 1;// 设置pageNo的初始化值为1，即默认获取的是第一页的数据。
	private int allpage;
//	private List<InSpTaskBean> allList;// 存放所有数据AlarmBean的list集合
//	private List<InSpTaskBean> onelist;// 存放一页数据实体类的Bean
	private boolean is_divPage;// 是否进行分页操作
	private boolean finish = true;// 是否加载完成;
	private AlarmAdapter alarmAdapter;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.alarmrecordfragment, null);
		initView();
		loadData();
		return view;
	}


	private void initView() {
//		Intent intent = getIntent();
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
		alarmlistview=view.findViewById(R.id.alarmlistview);
		alarmAdapter=new AlarmAdapter(getActivity(),list);
		alarmlistview.setAdapter(alarmAdapter);
	}

}
