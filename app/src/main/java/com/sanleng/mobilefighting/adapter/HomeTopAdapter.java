package com.sanleng.mobilefighting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.bean.MyListItem;

import java.util.List;


public class HomeTopAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<MyListItem> listPro;
	private Context context;
	public HomeTopAdapter(Context context, List<MyListItem> listPro) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.listPro = listPro;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listPro.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listPro.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HotViewHolder holder = null;
		if (convertView == null) {
			holder = new HotViewHolder();
			convertView = (View) mInflater.inflate(R.layout.hometop_listinfo, null);

			holder.textView = (TextView) convertView.findViewById(R.id.mylisttextView);
			convertView.setTag(holder);
		} else {
			holder = (HotViewHolder) convertView.getTag();
		}

		holder.textView.setText(listPro.get(position).getName());

		return convertView;
	}

	class HotViewHolder {
		TextView textView;
	}
}
