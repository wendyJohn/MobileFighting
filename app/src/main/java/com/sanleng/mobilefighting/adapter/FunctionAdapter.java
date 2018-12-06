package com.sanleng.mobilefighting.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;

/**
 * 应用适配器
 *
 * @author QiaoShi
 *
 */
public class FunctionAdapter extends BaseAdapter {
	private String[] names;
	private TypedArray icons;

	private Context context;

	public FunctionAdapter(Context context, int nameId, int iconsId) {
		// TODO Auto-generated constructor stub
		this.context = context;

		names = context.getResources().getStringArray(nameId);
		icons = context.getResources().obtainTypedArray(iconsId);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return names.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return names[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.myfunction_item, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.myfunc_name);
		tv.setText(names[position]);
		ImageView iv = (ImageView) convertView.findViewById(R.id.myfunc_icon);
		iv.setBackgroundResource(icons.getResourceId(position, -1));

		return convertView;
	}
}
