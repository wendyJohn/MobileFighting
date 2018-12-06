package com.sanleng.mobilefighting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.bean.RectificationBean;

import java.util.List;

/**
 * 待整改数据适配器
 *
 * @author QiaoShi
 *
 */
@SuppressLint("ResourceAsColor")
public class StayRectificationAdapter extends BaseAdapter {

	private List<RectificationBean> mList;
	private Context mContext;

	/**
	 * bindData用来传递数据给适配器。
	 *
	 * @list
	 */
	public void bindData(Context mContext, List<RectificationBean> mList) {
		this.mList = mList;
		this.mContext = mContext;

	}

	@Override
	public int getCount() {
		return mList.size();

	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.stayrrectification_item, null);
			holder = new Holder();
			holder.labelnumber = (TextView) convertView.findViewById(R.id.labelnumber);
			holder.devicename = (TextView) convertView.findViewById(R.id.devicename);
			holder.deviceposition = (TextView) convertView.findViewById(R.id.deviceposition);
			holder.term = (TextView) convertView.findViewById(R.id.term);
			holder.reorganizer = (TextView) convertView.findViewById(R.id.reorganizer);
			holder.describe = (TextView) convertView.findViewById(R.id.describe);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.labelnumber.setText(mList.get(position).getLabelnumber());
		holder.devicename.setText(mList.get(position).getDevicename());
		holder.deviceposition.setText(mList.get(position).getDeviceposition());
		holder.term.setText(mList.get(position).getTerm());
		holder.reorganizer.setText(mList.get(position).getReorganizer());
		holder.describe.setText(mList.get(position).getDescribe());

		return convertView;
	}

	class Holder {
		TextView labelnumber;
		TextView devicename;
		TextView deviceposition;
		TextView term;
		TextView reorganizer;
		TextView describe;
	}
}
