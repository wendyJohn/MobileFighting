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
import com.sanleng.mobilefighting.bean.WaterSystemBean;

import java.util.List;

/**
 * 水位数据适配器
 *
 * @author QiaoShi
 *
 */
@SuppressLint("ResourceAsColor")
public class WatersystemAdapter extends BaseAdapter {

	private List<WaterSystemBean> mList;
	private Context mContext;
	private Handler handler;
	private int mytype;

	/**
	 * bindData用来传递数据给适配器。
	 *
	 * @list
	 */
	public void bindData(Context mContext, List<WaterSystemBean> mList, int mytype, Handler handler) {
		this.mContext = mContext;
		this.mList = mList;
		this.mytype = mytype;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.wastersystem_item, null);

			holder = new Holder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.w_address = (TextView) convertView.findViewById(R.id.w_address);
			holder.safetyvalue = (TextView) convertView.findViewById(R.id.safetyvalue);
			holder.safetyrange = (TextView) convertView.findViewById(R.id.safetyrange);
			holder.historicaldata = (TextView) convertView.findViewById(R.id.historicaldata);

			holder.my_pic = (ImageView) convertView.findViewById(R.id.my_pic);
			holder.w_state = (ImageView) convertView.findViewById(R.id.w_state);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.title.setText(mList.get(position).getTitle());
		holder.w_address.setText(mList.get(position).getW_address());
		holder.safetyvalue.setText(mList.get(position).getSafetyvalue());
		holder.safetyrange.setText(mList.get(position).getSafetyrange());

		String type = mList.get(position).getType();

		if (mytype == 1) {
			if (type.equals("watersystem_level")) {
				holder.my_pic.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.tank));
			}
			if (type.equals("watersystem_hyrant")) {
				holder.my_pic.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.firehydrant));
			}
			if (type.equals("4")) {
				holder.my_pic.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.firepump));
			}
		}
		if (mytype == 2) {
			if (type.equals("watersystem_level")) {
				holder.my_pic.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.tank));
			}
		}

		if (mytype == 3) {
			if (type.equals("watersystem_hyrant")) {
				holder.my_pic.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.firehydrant));
			}
		}
		if (mytype == 4) {
			if (type.equals("4")) {
				holder.my_pic.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.firepump));
			}
		}
		String State = mList.get(position).getState();
		if (State.equals("正常")) {
			holder.w_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.normal));
		}
		if (State.equals("异常")) {
			holder.w_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.abnormal));
		}
		if (State.equals("无状态")) {
			holder.w_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.abnormal));
		}

		holder.historicaldata.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putInt("selIndex", position);
				msg.setData(data);
				msg.what = 22222;
				handler.sendMessage(msg);
			}
		});
		return convertView;
	}

	class Holder {
		TextView title;
		TextView w_address;
		TextView safetyvalue;
		TextView safetyrange;
		TextView historicaldata;

		ImageView my_pic;
		ImageView w_state;

	}
}
