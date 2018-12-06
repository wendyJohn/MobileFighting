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
 * 实时数据
 *
 * @author QiaoShi
 *
 */
@SuppressLint("ResourceAsColor")
public class RealtimedataAdapter extends BaseAdapter {

	private List<InSpTaskBean> mList;
	private Context mContext;

	public RealtimedataAdapter(Context mContext, List<InSpTaskBean> mList) {
		super();
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

	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.realtimedata_item, null);

			holder = new Holder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.value = (TextView) convertView.findViewById(R.id.value);
			holder.reasonablescope = (TextView) convertView.findViewById(R.id.reasonablescope);
			holder.immediatetreatment = (TextView) convertView.findViewById(R.id.immediatetreatment);
			holder.r_state = (ImageView) convertView.findViewById(R.id.r_state);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.title.setText(mList.get(position).getTitle());
		holder.value.setText(mList.get(position).getContent());
		holder.reasonablescope.setText(mList.get(position).getCompany());
		String State = mList.get(position).getState();
		if (State.equals("0")) {
			holder.r_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.normal));
		}
		if (State.equals("1")) {
			holder.r_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.abnormal));
		}
		if (State.equals("null")) {
			holder.r_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.abnormal));
		}
		return convertView;
	}

	class Holder {
		TextView title;
		TextView value;
		TextView reasonablescope;
		TextView immediatetreatment;

		ImageView r_state;
	}
}
