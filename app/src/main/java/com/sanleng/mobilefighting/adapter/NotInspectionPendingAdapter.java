package com.sanleng.mobilefighting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.bean.InSpTaskBean;

import java.util.List;

/**
 * 巡查任务数据适配器
 *
 * @author QiaoShi
 *
 */
@SuppressLint("ResourceAsColor")
public class NotInspectionPendingAdapter extends BaseAdapter {

	private List<InSpTaskBean> mList;
	private Context mContext;
	private Handler handler;

	/**
	 * bindData用来传递数据给适配器。
	 *
	 * @list
	 */
	public void bindData(Context mContext, List<InSpTaskBean> mList, Handler handler) {
		this.mList = mList;
		this.mContext = mContext;
		this.handler = handler;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.notinspetionptask_item, null);

			holder = new Holder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.company = (TextView) convertView.findViewById(R.id.company);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.immediatetreatment = (TextView) convertView.findViewById(R.id.immediatetreatment);

			holder.i_state = (ImageView) convertView.findViewById(R.id.i_state);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.title.setText(mList.get(position).getTitle());
		holder.content.setText(mList.get(position).getContent());
		holder.company.setText(mList.get(position).getCompany());
		holder.time.setText(mList.get(position).getTime());

		String State = mList.get(position).getState();
		if (State.equals("0")) {
			holder.i_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.notcomplete));
		}
		if (State.equals("1")) {
			holder.i_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.alreadycomplete));
		}
		if (State.equals("2")) {
			holder.i_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.overtimecomplete));
		}

		holder.immediatetreatment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putInt("selIndex", position);
				msg.setData(data);
				msg.what = 33333;
				handler.sendMessage(msg);
			}
		});
		return convertView;
	}

	class Holder {
		TextView title;
		TextView content;
		TextView company;
		TextView time;
		TextView immediatetreatment;

		ImageView i_state;
	}
}
