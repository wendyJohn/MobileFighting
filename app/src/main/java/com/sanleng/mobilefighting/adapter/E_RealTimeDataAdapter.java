package com.sanleng.mobilefighting.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.bean.ERealTimeDataBean;

import java.util.List;

/**
 * 智能电气火灾实时数据适配器
 *
 * @author QiaoShi
 */
public class E_RealTimeDataAdapter extends BaseAdapter {

    private List<ERealTimeDataBean> mList;
    private Context mContext;
    private Handler handler;

    public E_RealTimeDataAdapter(Context mContext, List<ERealTimeDataBean> mList, Handler handler) {
        super();
        this.mList = mList;
        this.mContext = mContext;
        this.handler = handler;
    }
//    /**
//     * bindData用来传递数据给适配器。
//     *
//     * @list
//     */
//    public void bindData(Context mContext, List<ERealTimeDataBean> mList, Handler handler) {
//        this.mContext = mContext;
//        this.mList = mList;
//        this.handler = handler;
//    }

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.e_realtimedata_item, null);
            holder = new Holder();
            holder.address = (TextView) convertView.findViewById(R.id.w_address);
            holder.temperature = (TextView) convertView.findViewById(R.id.temperature);
            holder.temperaturelimit = (TextView) convertView.findViewById(R.id.temperaturelimit);
            holder.residualcurrent = (TextView) convertView.findViewById(R.id.residualcurrent);
            holder.currentlimit = (TextView) convertView.findViewById(R.id.currentlimit);
            holder.temperaturealarm = (ImageView) convertView.findViewById(R.id.temperaturealarm);
            holder.temperaturealarms = (ImageView) convertView.findViewById(R.id.temperaturealarms);
            holder.contactnumber = (TextView) convertView.findViewById(R.id.contactnumber);

            holder.electricalmaintenance = (TextView) convertView.findViewById(R.id.electricalmaintenance);
            holder.pendingdisposal = (TextView) convertView.findViewById(R.id.pendingdisposal);

            holder.confirmphoto = (TextView) convertView.findViewById(R.id.confirmphoto);
            holder.historicaltrack = (TextView) convertView.findViewById(R.id.historicaltrack);
            holder.message_item_unread = (TextView) convertView.findViewById(R.id.message_item_unread);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.address.setText(mList.get(position).getAddress());
        holder.temperature.setText(mList.get(position).getTemperature());
        holder.temperaturelimit.setText(mList.get(position).getTemperaturelimit());
        holder.residualcurrent.setText(mList.get(position).getResidualcurrent());
        holder.currentlimit.setText(mList.get(position).getCurrentlimit());

        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext).load(R.drawable.ealarm).apply(options).into(holder.temperaturealarm);
        Linkify.addLinks(holder.contactnumber, Linkify.PHONE_NUMBERS);
        String state = mList.get(position).getState();
        String number = mList.get(position).getNumber();
        int str = Integer.parseInt(number);
        if (str == 0) {
            holder.message_item_unread.setVisibility(View.GONE);
        }
        if (str > 0) {
            holder.message_item_unread.setVisibility(View.VISIBLE);
        }

        if (state.equals("无报警")) {
            holder.temperaturealarm.setVisibility(View.GONE);
            holder.temperaturealarms.setVisibility(View.VISIBLE);
//            holder.address.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.temperaturealarms.setBackground(mContext.getResources().getDrawable(R.drawable.ealarms));
        }
        if (state.equals("有报警")) {
            holder.temperaturealarm.setVisibility(View.VISIBLE);
            holder.temperaturealarms.setVisibility(View.GONE);
//            holder.address.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        //确认拍照
        holder.confirmphoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putInt("selIndex", position);
                msg.setData(data);
                msg.what = 66660;
                handler.sendMessage(msg);
            }
        });
        //待处理
        holder.pendingdisposal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putInt("selIndex", position);
                msg.setData(data);
                msg.what = 66661;
                handler.sendMessage(msg);
            }
        });
        //历史轨迹
        holder.historicaltrack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putInt("selIndex", position);
                msg.setData(data);
                msg.what = 66662;
                handler.sendMessage(msg);
            }
        });


        return convertView;
    }

    class Holder {
        TextView address;
        TextView temperature;
        TextView temperaturelimit;
        TextView residualcurrent;
        TextView electricalmaintenance;
        TextView currentlimit;
        TextView contactnumber;

        TextView confirmphoto;
        TextView pendingdisposal;
        TextView historicaltrack;
        TextView message_item_unread;

        ImageView temperaturealarm;
        ImageView temperaturealarms;
    }
}
