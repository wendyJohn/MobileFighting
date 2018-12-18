package com.sanleng.mobilefighting.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.bean.ArchitectureBean;
import com.sanleng.mobilefighting.util.ImageDown;
import com.sanleng.mobilefighting.util.ImageDown.ImageCallBack;

import java.util.List;
import java.util.Map;

/**
 * 报警数据适配器
 *
 * @author QiaoShi
 */
public class AlarmAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> list;

    public AlarmAdapter(Context context, List<Map<String, Object>> list) {
        super();
        this.list = list;
        this.context = context;
    }

//    /**
//     * bindData用来传递数据给适配器。
//     *
//     * @param list
//     */
//    public void bindData(Context context, List<Map<String, Object>> list) {
//        this.list = list;
//        this.context = context;
//    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.alarm_item, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.postion = (TextView) convertView.findViewById(R.id.postion);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).get("name").toString());
        holder.postion.setText(list.get(position).get("postion").toString());
        holder.time.setText(list.get(position).get("time").toString());

        return convertView;
    }

    class ViewHolder {
        TextView name, postion, time;
    }
}
