package com.sanleng.mobilefighting.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.bean.StationBean;

import java.util.List;

/**
 * 附近应急站数据适配器
 *
 * @author QiaoShi
 */
public class StationAdapter extends BaseAdapter {

    private List<StationBean> mList;
    private Context mContext;
    private Handler handler;


    public StationAdapter(Context mContext, List<StationBean> mList) {
        super();
        this.mList = mList;
        this.mContext = mContext;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.station_item, null);
            holder = new Holder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.name.setText(mList.get(position).getName());
        holder.address.setText(mList.get(position).getAddress());
        holder.distance.setText(mList.get(position).getDistance());

        return convertView;
    }

    class Holder {
        TextView name;
        TextView address;
        TextView distance;

    }
}
