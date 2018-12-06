package com.sanleng.mobilefighting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.bean.InSpTaskBean;

import java.util.List;

/**
 * 巡查任务记录数据适配器
 *
 * @author QiaoShi
 *
 */
@SuppressLint("ResourceAsColor")
public class PatrolRecordAdapter extends BaseAdapter {

	private List<InSpTaskBean> mList;
	private Context mContext;

	/**
	 * bindData用来传递数据给适配器。
	 *
	 * @list
	 */
	public void bindData(Context mContext, List<InSpTaskBean> mList) {
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.patrolrecod_item, null);

			holder = new Holder();
			holder.labelnumber = (TextView) convertView.findViewById(R.id.labelnumber);
			holder.devicename = (TextView) convertView.findViewById(R.id.devicename);

			holder.devicepositions = (TextView) convertView.findViewById(R.id.devicep);
			holder.p_state = (ImageView) convertView.findViewById(R.id.p_state);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.labelnumber.setText(mList.get(position).getTitle());
		holder.devicename.setText(mList.get(position).getContent());
		holder.devicepositions.setText(mList.get(position).getCompany());

		String State = mList.get(position).getState();
		if (State.equals("0")) {
			holder.p_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.p_normal));
		}
		if (State.equals("2")) {
			holder.p_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.p_lose));
		}
		if (State.equals("1")) {
			holder.p_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.p_fault));
		}

		return convertView;
	}

	class Holder {
		TextView labelnumber;
		TextView devicename;
		TextView devicepositions;
		ImageView p_state;

	}
}
