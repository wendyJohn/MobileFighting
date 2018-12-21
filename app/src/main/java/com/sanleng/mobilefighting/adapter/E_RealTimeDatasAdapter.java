package com.sanleng.mobilefighting.adapter;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.bean.ERealTimeDataBean;

import java.util.List;
import java.util.Map;

/**
 * 智能电气火灾实时数据适配器
 *
 * @author QiaoShi
 */
public class E_RealTimeDatasAdapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, Object>> list;

    /**
     * bindData用来传递数据给适配器。
     *
     * @param list
     */
    public void bindData(Context mContext, List<Map<String, Object>> list) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.e_realtimedatas_item, parent, false);
            holder = new Holder();
            holder.address = (TextView) convertView.findViewById(R.id.w_address);
            holder.temperature = (TextView) convertView.findViewById(R.id.temperature);
            holder.temperaturealarm = (ImageView) convertView.findViewById(R.id.temperaturealarm);
            holder.temperaturealarms = (ImageView) convertView.findViewById(R.id.temperaturealarms);
            holder.dress_yout = (LinearLayout) convertView.findViewById(R.id.dress_yout);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext).load(R.drawable.ealarm).apply(options).into(holder.temperaturealarm);

        String state = list.get(position).get("state").toString();
        holder.address.setText(list.get(position).get("equipmentName").toString());
        holder.temperature.setText(list.get(position).get("position").toString());

        if (state.equals("0")) {
            holder.temperaturealarm.setVisibility(View.GONE);
            holder.temperature.setTextColor(mContext.getResources().getColor(R.color.text_blue));
            holder.temperaturealarms.setVisibility(View.VISIBLE);
            holder.temperaturealarms.setBackground(mContext.getResources().getDrawable(R.drawable.ealarms));
        }
        if (state.equals("1")) {
            holder.temperaturealarm.setVisibility(View.VISIBLE);
            holder.temperaturealarms.setVisibility(View.GONE);
            holder.temperature.setTextColor(mContext.getResources().getColor(R.color.red));
        }

        return convertView;
    }

    class Holder {
        TextView address;
        TextView temperature;

        ImageView temperaturealarm;
        ImageView temperaturealarms;

        LinearLayout dress_yout;
    }
}
