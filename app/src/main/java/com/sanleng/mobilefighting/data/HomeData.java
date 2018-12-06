package com.sanleng.mobilefighting.data;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.util.ACache;
import com.sanleng.mobilefighting.util.UtilFileDB;

import java.util.ArrayList;
import java.util.List;

public class HomeData {

	public static String[] TITLE = { "巡查任务", "巡查点扫描", "隐患整改", "巡查记录", "视频监控", "主机监测", "应急站", "网格管理", "巡查助手", "查岗记录",
			"初始化绑定", "全部" };

	public static int[] IMG = { R.drawable.inspectiontask, R.drawable.hiddentrouble, R.drawable.firepatrol,
			R.drawable.productionlinetours, R.drawable.videoapplication, R.drawable.hostmonitoring,
			R.drawable.onebuttonalarm, R.drawable.gridmanagement, R.drawable.inspecting, R.drawable.lookuprecord,
			R.drawable.sticker, R.drawable.whole };

	public static int[] IMGSEARCH = { R.drawable.inspectiontask, R.drawable.firepatrol, R.drawable.hiddentrouble,
			R.drawable.productionlinetours, R.drawable.videoapplication, R.drawable.hostmonitoring,
			R.drawable.onebuttonalarm, R.drawable.gridmanagement, R.drawable.inspecting, R.drawable.lookuprecord,
			R.drawable.sticker, R.drawable.whole };

	/****
	 *
	 * 获取缓存数据
	 *
	 * @param aCache
	 * @return
	 */
	public static final List<Integer> POSITION(ACache aCache) {
		String string = UtilFileDB.SELETEFile(aCache, "home");
		List<Integer> list = new ArrayList<Integer>();
		if (string == null)// 默认
		{
			for (int i = 0; i < 7; i++) {
				list.add(i);
			}
			list.add(11);
			UtilFileDB.ADDFile(aCache, "home", "0,1,2,3,4,5,6");
			return list;
		} else {
			String[] stringArr = string.split(",");
			for (int i = 0; i < stringArr.length; i++) {
				list.add(Integer.valueOf(stringArr[i]));
			}
			list.add(11);
			return list;
		}

	}

}
