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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.bean.FireAlarmBean;

import java.util.List;

/**
 * 火警数据适配器
 *
 * @author QiaoShi
 *
 */
@SuppressLint("ResourceAsColor")
public class FireAlarmAdapter extends BaseAdapter {

	private List<FireAlarmBean> mList;
	private Context mContext;
	private Handler handler;
	private String type;
	private LinearLayout type_yout;
	private LinearLayout type_youts;

	/**
	 * bindData用来传递数据给适配器。
	 *
	 * @list
	 */
	public void bindData(Context mContext, List<FireAlarmBean> mList, String type, Handler handler) {
		this.mList = mList;
		this.mContext = mContext;
		this.type = type;
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.firealarm_item, null);

			holder = new Holder();
			holder.alarmTime = (TextView) convertView.findViewById(R.id.alarmtime);
			holder.alarmEquipment = (TextView) convertView.findViewById(R.id.alarmequipment);
			holder.alarmPosition = (TextView) convertView.findViewById(R.id.alarmposition);
			holder.alarmUnit = (TextView) convertView.findViewById(R.id.alarmunit);
			holder.cancle = (TextView) convertView.findViewById(R.id.cancle);
			holder.viewlocation = (TextView) convertView.findViewById(R.id.viewlocation);
			holder.immediatetreatment = (TextView) convertView.findViewById(R.id.immediatetreatment);
			type_yout = (LinearLayout) convertView.findViewById(R.id.type_yout);
			holder.cancles = (TextView) convertView.findViewById(R.id.cancles);
			holder.viewlocations = (TextView) convertView.findViewById(R.id.viewlocations);

			type_youts = (LinearLayout) convertView.findViewById(R.id.type_youts);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		if (type.equals("已处理")) {
			type_youts.setVisibility(View.VISIBLE);
			type_yout.setVisibility(View.GONE);
		}
		if (type.equals("待处理")) {
			type_youts.setVisibility(View.GONE);
			type_yout.setVisibility(View.VISIBLE);
		}

		holder.alarmTime.setText(mList.get(position).getAlarmTime());
		holder.alarmEquipment.setText(mList.get(position).getAlarmEquipment());
		holder.alarmPosition.setText(mList.get(position).getAlarmPosition());
		holder.alarmUnit.setText(mList.get(position).getAlarmUnit());
		holder.cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putInt("selIndex", position);
				msg.setData(data);
				msg.what = 565433;
				handler.sendMessage(msg);
			}
		});
		holder.cancles.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putInt("selIndex", position);
				msg.setData(data);
				msg.what = 565433;
				handler.sendMessage(msg);
			}
		});
		holder.immediatetreatment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putInt("selIndex", position);
				msg.setData(data);
				msg.what = 111111;
				handler.sendMessage(msg);
			}
		});

		holder.viewlocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putInt("selIndex", position);
				msg.setData(data);
				msg.what = 333333;
				handler.sendMessage(msg);
			}
		});
		holder.viewlocations.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putInt("selIndex", position);
				msg.setData(data);
				msg.what = 333333;
				handler.sendMessage(msg);
			}
		});
		return convertView;
	}

	class Holder {
		TextView alarmTime;
		TextView alarmEquipment;
		TextView alarmPosition;
		TextView alarmUnit;
		TextView cancle;
		TextView viewlocation;
		TextView cancles;
		TextView viewlocations;
		TextView immediatetreatment;
	}
}
