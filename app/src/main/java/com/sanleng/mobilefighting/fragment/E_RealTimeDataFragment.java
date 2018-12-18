package com.sanleng.mobilefighting.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.activity.AlarmRecordActivity;
import com.sanleng.mobilefighting.adapter.E_RealTimeDataAdapter;
import com.sanleng.mobilefighting.bean.ERealTimeDataBean;
import com.sanleng.mobilefighting.myview.MarqueeViews;

import java.util.ArrayList;
import java.util.List;

/**
 * 智能电气火灾实时数据
 *
 * @author Qiaoshi
 */
public class E_RealTimeDataFragment extends BaseFragment implements OnClickListener {

    private ListView e_realtimedatalslistview;
    private E_RealTimeDataAdapter e_realtimedataAdapter;//(有数据版)

    //private E_RealTimeDatasAdapter e_realtimedatasAdapter;//(无数据版)

    private View view;
    private List<ERealTimeDataBean> list;
    private Receivers receivers;
    private static final String BROADCAST_PERMISSION_DISCS = "com.permissions.MY_BROADCASTS";
    private static final String BROADCAST_ACTION_DISCS = "com.permissions.my_broadcasts";
    private ImageView query_im;
    private boolean state = true;

    private LinearLayout yaout;
    private ImageView imageView_item;
    private MarqueeViews marqueeviews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.e_realtimedatafragment, null);
        initview();
        AlarmMessage();
        return view;
    }

    //初始化
    private void initview() {
        query_im = view.findViewById(R.id.query_im);
        query_im.setOnClickListener(this);
        yaout = view.findViewById(R.id.yaout);
        imageView_item = view.findViewById(R.id.imageView_item);

        receivers = new Receivers();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_DISCS); // 只有持有相同的action的接受者才能接收此广
        getActivity().registerReceiver(receivers, intentFilter, BROADCAST_PERMISSION_DISCS, null);

        list = new ArrayList<>();
        ERealTimeDataBean beana = new ERealTimeDataBean();
        beana.setAddress("南京工程学院A栋一层配电箱");
        beana.setTemperature("当前温度：20℃");
        beana.setTemperaturelimit("限值：0～100℃");
        beana.setResidualcurrent("剩余电流：20MA");
        beana.setCurrentlimit("限值：30～100MA");
        beana.setState("有报警");
        beana.setNumber("10");
        list.add(beana);

        ERealTimeDataBean beanb = new ERealTimeDataBean();
        beanb.setAddress("南京工程学院A栋二层配电箱");
        beanb.setTemperature("当前温度：20℃");
        beanb.setTemperaturelimit("限值：0～100℃");
        beanb.setResidualcurrent("剩余电流：20MA");
        beanb.setCurrentlimit("限值：30～100MA");
        beanb.setState("无报警");
        beanb.setNumber("0");
        list.add(beanb);

        ERealTimeDataBean beanc = new ERealTimeDataBean();
        beanc.setAddress("南京工程学院A栋三层配电箱");
        beanc.setTemperature("当前温度：20℃");
        beanc.setTemperaturelimit("限值：0～100℃");
        beanc.setResidualcurrent("剩余电流：20MA");
        beanc.setCurrentlimit("限值：30～100MA");
        beanc.setState("有报警");
        beanc.setNumber("10");
        list.add(beanc);

        ERealTimeDataBean beand = new ERealTimeDataBean();
        beand.setAddress("南京工程学院A栋四层配电箱");
        beand.setTemperature("当前温度：20℃");
        beand.setTemperaturelimit("限值：0～100℃");
        beand.setResidualcurrent("剩余电流：20MA");
        beand.setCurrentlimit("限值：30～100MA");
        beand.setState("无报警");
        beand.setNumber("0");
        list.add(beand);

        ERealTimeDataBean beane = new ERealTimeDataBean();
        beane.setAddress("南京工程学院A栋五层配电箱");
        beane.setTemperature("当前温度：20℃");
        beane.setTemperaturelimit("限值：0～100℃");
        beane.setResidualcurrent("剩余电流：20MA");
        beane.setCurrentlimit("限值：30～100MA");
        beane.setState("有报警");
        beane.setNumber("10");
        list.add(beane);

//        ERealTimeDataBean beana = new ERealTimeDataBean();
//        beana.setAddress("南京工程学院A栋一层配电箱");
//        beana.setTemperature("设备工作正常，暂无报警");
//        beana.setState("无报警");
//        list.add(beana);
//
//        ERealTimeDataBean beanb = new ERealTimeDataBean();
//        beanb.setAddress("南京工程学院B栋二层配电箱");
//        beanb.setTemperature("报警时间：2018/12/13 00:20");
//        beanb.setState("有报警");
//        list.add(beanb);
//
//        ERealTimeDataBean beanc = new ERealTimeDataBean();
//        beanc.setAddress("南京工程学院C栋三层配电箱");
//        beanc.setTemperature("报警时间：2018/12/13 00:30");
//        beanc.setState("有报警");
//        list.add(beanc);
//
//        ERealTimeDataBean beand = new ERealTimeDataBean();
//        beand.setAddress("南京工程学院D栋四层配电箱");
//        beand.setTemperature("设备工作正常，暂无报警");
//        beand.setState("无报警");
//        list.add(beand);
//
//        ERealTimeDataBean beane = new ERealTimeDataBean();
//        beane.setAddress("南京工程学院E栋五层配电箱");
//        beane.setTemperature("报警时间：2018/12/13 00:50");
//        beane.setState("有报警");
//        list.add(beane);

        e_realtimedatalslistview = view.findViewById(R.id.realtimedatalslistview);
        e_realtimedataAdapter = new E_RealTimeDataAdapter(getActivity(), list, mHandler);

//      e_realtimedatasAdapter = new E_RealTimeDatasAdapter(getActivity(), list);

        e_realtimedatalslistview.setAdapter(e_realtimedataAdapter);
        e_realtimedatalslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AlarmRecordActivity.class);
                startActivity(intent);
            }
        });
    }

    //报警信息
    private void AlarmMessage() {
        marqueeviews = (MarqueeViews) view.findViewById(R.id.marqueeviews);
        List<String> info = new ArrayList<>();
        info.add("南京工程学院A栋一层配电箱温度过高");
        info.add("南京工程学院A栋二层配电箱电流过大");
        info.add("南京工程学院A栋三层配电箱温度过低");
        info.add("南京工程学院A栋四层配电箱电流过小");
        // 在代码里设置自己的动画
        marqueeviews.startWithList(info);

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            final Bundle data = message.getData();
            switch (message.what) {
                // 拍照确认
                case 66660:
                    int selIndex = data.getInt("selIndex");

                    break;
                //待处理
                case 66661:
                    int selIndexs = data.getInt("selIndex");

                    break;
                //历史轨迹
                case 66662:
                    int selIndex_p = data.getInt("selIndex");

                    break;
                default:
                    break;
            }
        }
    };


    // 收到报警广播处理，刷新界面
    public class Receivers extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BROADCAST_ACTION_DISCS)) {
                //刷新数据

            }

        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.query_im:
                if (state) {
                    yaout.setVisibility(View.VISIBLE);
                    imageView_item.setVisibility(View.GONE);
                    state = false;
                } else {
                    yaout.setVisibility(View.GONE);
                    imageView_item.setVisibility(View.VISIBLE);
                    state = true;
                }
                break;
            default:
                break;
        }
    }
}
