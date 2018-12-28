package com.sanleng.mobilefighting.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.activity.ArticleActivity;
import com.sanleng.mobilefighting.activity.EmergencyRescueActivity;
import com.sanleng.mobilefighting.activity.FireAlarmActivity;
import com.sanleng.mobilefighting.activity.HostMonitoringActivity;
import com.sanleng.mobilefighting.activity.InspectingAssistantActivity;
import com.sanleng.mobilefighting.activity.InspectionMissionActivity;
import com.sanleng.mobilefighting.activity.MainTabActivity;
import com.sanleng.mobilefighting.activity.MonStationActivity;
import com.sanleng.mobilefighting.activity.MonitoringStationActivity;
import com.sanleng.mobilefighting.activity.PatrolHandleActivity;
import com.sanleng.mobilefighting.activity.PatrolPointScanningActivity;
import com.sanleng.mobilefighting.activity.RectificationActivity;
import com.sanleng.mobilefighting.activity.RecyclerViewActivity;
import com.sanleng.mobilefighting.activity.SearchActivity;
import com.sanleng.mobilefighting.adapter.HomeAdapter;
import com.sanleng.mobilefighting.bean.UserBean;
import com.sanleng.mobilefighting.data.HomeData;
import com.sanleng.mobilefighting.myview.BannerView;
import com.sanleng.mobilefighting.myview.ZQScrollGridView;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.ACache;
import com.sanleng.mobilefighting.util.PreferenceUtils;
import com.sanleng.mobilefighting.util.UtilFileDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */
@SuppressLint("InflateParams")
public class HomeFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Intent intent;
    private ZQScrollGridView gridView;
    private static HomeAdapter adapter;
    public static List<Integer> listPosition;
    public static List<UserBean> list;
    private static ACache aCache;
    private LinearLayout article;
    private LinearLayout video;
    private LinearLayout livebroadcast;
    private List<String> urls;

    private LinearLayout mag_a;
    private LinearLayout mag_b;
    private LinearLayout mag_c;
    private LinearLayout mag_d;
    private Receiver Receiver;

    private static final String BROADCAST_PERMISSION_DISCS = "com.permissions.MY_BROADCASTS";
    private static final String BROADCAST_ACTION_DISCS = "com.permissions.my_broadcasts";

    private TextView gza;
    private TextView gzb;
    private BannerView banner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        view = getView();
        aCache = ACache.get(getActivity());
        initView();
        requestData();
    }

    public void initView() {
        Receiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_DISCS); // 只有持有相同的action的接受者才能接收此广
        getActivity().registerReceiver(Receiver, intentFilter, BROADCAST_PERMISSION_DISCS, null);

        gridView = (ZQScrollGridView) view.findViewById(R.id.home_gridview);
        article = (LinearLayout) view.findViewById(R.id.article);
        video = (LinearLayout) view.findViewById(R.id.video);
        livebroadcast = (LinearLayout) view.findViewById(R.id.livebroadcast);

        gza = (TextView) view.findViewById(R.id.gza);
        gzb = (TextView) view.findViewById(R.id.gzb);

        mag_a = (LinearLayout) view.findViewById(R.id.mag_a);
        mag_b = (LinearLayout) view.findViewById(R.id.mag_b);
        mag_c = (LinearLayout) view.findViewById(R.id.mag_c);
        mag_d = (LinearLayout) view.findViewById(R.id.mag_d);

        mag_a.setOnClickListener(this);
        mag_b.setOnClickListener(this);
        mag_c.setOnClickListener(this);
        mag_d.setOnClickListener(this);

        article.setOnClickListener(this);
        video.setOnClickListener(this);
        livebroadcast.setOnClickListener(this);
        onLoad();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        FireQuantity();
        super.onResume();
    }

    public void onLoad() {
        adapter = new HomeAdapter(getActivity());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(onItemClickListener);
        gridView.setOnItemLongClickListener(onItemLongClickListener);
        showData();
    }

    /*****
     * 刷新数据
     */
    private void notifyData() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 文章
            case R.id.article:
                Intent intent_article = new Intent(getActivity(), ArticleActivity.class);
                startActivity(intent_article);
                break;
            // 视频
            case R.id.video:
                Intent intent_recyclerView = new Intent(getActivity(), RecyclerViewActivity.class);
                startActivity(intent_recyclerView);
                break;
            // 直播
            case R.id.livebroadcast:

                break;
            // 待处理火警
            case R.id.mag_a:
                Intent intent_fireAlarm = new Intent(getActivity(), FireAlarmActivity.class);
                startActivity(intent_fireAlarm);
                break;
            // 今日故障设备
            case R.id.mag_b:
                Intent intent_hostMonitoring = new Intent(getActivity(), HostMonitoringActivity.class);
                startActivity(intent_hostMonitoring);
                break;
            // 待整改隐患
            case R.id.mag_c:
                Intent intent_rectification = new Intent(getActivity(), RectificationActivity.class);
                startActivity(intent_rectification);
                break;
            // 电气火灾
            case R.id.mag_d:

                break;

        }
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                showLongClick(false);
            } else if (msg.what == 2)// 删除
            {
                showDelete();
            }
        }
    };

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == (listPosition.size() - 1)) {
                intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("list", (Serializable) listPosition);
                startActivityForResult(intent, 1);
            } else {
                UserBean bean = list.get(listPosition.get(position));
                String name = bean.getTitle();
                if (name.equals("隐患整改")) {
                    Intent intent_rectification = new Intent(getActivity(), RectificationActivity.class);
                    startActivity(intent_rectification);
                }
                if (name.equals("视频监控")) {
                    Intent intent_monitoringstation = new Intent(getActivity(), MonitoringStationActivity.class);
                    startActivity(intent_monitoringstation);

//					Intent intent_Inspection = new Intent(getActivity(), MonitorVideoActivity.class);
//					startActivity(intent_Inspection);
                }
                if (name.equals("巡查记录")) {
                    Intent intent_PatrolHandle = new Intent(getActivity(), PatrolHandleActivity.class);
                    startActivity(intent_PatrolHandle);
                }
                if (name.equals("查岗记录")) {
//
                }
                if (name.equals("应急站")) {
                    //EmergencyRescueActivity  MonStationActivity
                    Intent intent_monstation = new Intent(getActivity(), EmergencyRescueActivity.class);
                    startActivity(intent_monstation);
                }
                if (name.equals("网格管理")) {

                }
                if (name.equals("巡查点扫描")) {
                    Intent intent_patrolpointscanning = new Intent(getActivity(), PatrolPointScanningActivity.class);
                    intent_patrolpointscanning.putExtra("type", 1);
                    startActivity(intent_patrolpointscanning);
                }
                if (name.equals("巡查助手")) {
                    Intent intent_inspectingAssistant = new Intent(getActivity(), InspectingAssistantActivity.class);
                    startActivity(intent_inspectingAssistant);
                }
                if (name.equals("巡查任务")) {
                    Intent intent_inspectionMission = new Intent(getActivity(), InspectionMissionActivity.class);
                    startActivity(intent_inspectionMission);
                }
                if (name.equals("初始化绑定")) {

                }
                if (name.equals("主机监测")) {
                    Intent intent_hostMonitoring = new Intent(getActivity(), HostMonitoringActivity.class);
                    startActivity(intent_hostMonitoring);
                }
            }
        }
    };
    AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            if (position == (listPosition.size() - 1)) {
                return false;
            }
            showLongClick(true);
            Message msg = new Message();
            msg.what = 1;// 删除
            msg.obj = 1;
            MainTabActivity.handler.sendMessage(msg);
            return false;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                if (data.getStringExtra("key").equals("2")) {
                    listPosition.clear();
                    listPosition = HomeData.POSITION(aCache);
                    notifyData();
                }
            }
        }
    }

    private void showData() {
        listPosition = HomeData.POSITION(aCache);
        list = new ArrayList<UserBean>();
        for (int i = 0; i < 12; i++) {
            UserBean user = new UserBean(HomeData.IMG[i], HomeData.TITLE[i], false, false);
            list.add(user);
        }
        notifyData();
    }

    /***
     * 删除
     */
    private void showDelete() {
        // 删除缓存
        String home = "";
        for (int i = 0; i < listPosition.size() - 1; i++) {
            UserBean userBean = list.get(listPosition.get(i));
            if (!userBean.isCheck()) {
                home += listPosition.get(i) + ",";
            }
        }
        aCache.remove("home");
        listPosition.clear();
        try {
            UtilFileDB.ADDFile(aCache, "home", home.substring(0, (home.length() - 1)));
            if (listPosition == null || listPosition.size() <= 1) {
                listPosition = HomeData.POSITION(aCache);
            }
        } catch (Exception e) {
            listPosition.add((list.size() - 1));
        }
        showLongClick(false);
    }

    /****
     * 重新刷新数据
     *
     * @param isvisibility
     */
    private void showLongClick(boolean isvisibility) {
        list.clear();
        for (int i = 0; i < 12; i++) {
            UserBean user = new UserBean(HomeData.IMG[i], HomeData.TITLE[i], false, isvisibility);
            list.add(user);
        }

        HomeFragment.list.get(11).setIsvisibility(false);
        notifyData();
    }

    private void requestData() {
        urls = new ArrayList<>();
        RequestParams params = new RequestParams();
        params.put("username", PreferenceUtils.getString(getActivity(), "MobileFig_username"));
        RequestUtils.ClientPost(
                URLs.HOST + "/kspf/app/publicityedu/banner?platformkey=app_firecontrol_owner", params,
                new NetCallBack() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onMySuccess(String result) {
                        if (result == null || result.length() == 0) {
                            return;
                        }
                        System.out.println("数据请求成功" + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String data = jsonObject.getString("data");
                            JSONArray array = new JSONArray(data);
                            JSONObject object;
                            for (int i = 0; i < array.length(); i++) {
                                object = (JSONObject) array.get(i);
                                String url = object.getString("url");
                                urls.add(URLs.HOST + url);
                            }

                            banner = (BannerView) view.findViewById(R.id.bannerview);
                            // 解耦
                            banner.loadData(urls).display();// 构建者模式返回对象本身
                            banner.setBannerClicklistener(new BannerView.BannerClicklistener() {
                                @Override
                                public void onClickListener(int pos) {
                                    Toast.makeText(getActivity(), "pos====" + pos, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyFailure(Throwable arg0) {
                    }
                });

    }

    // 火警广播处理
    public class Receiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BROADCAST_ACTION_DISCS)) {
                FireQuantity();
            }

        }
    }

    // 获取提示数量
    private void FireQuantity() {
        RequestParams params = new RequestParams();
        params.put("unitcode", PreferenceUtils.getString(getActivity(), "unitcode"));
        params.put("username", PreferenceUtils.getString(getActivity(), "MobileFig_username"));
        params.put("platformkey", "app_firecontrol_owner");

        RequestUtils.ClientPost(URLs.ProblemStatistics, params, new NetCallBack() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onMySuccess(String result) {
                if (result == null || result.length() == 0) {
                    return;
                }
                System.out.println("数据请求成功" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String data = jsonObject.getString("data");
                    JSONObject object = new JSONObject(data);
                    int fireEngine = object.getInt("fireEngine");

                    int gz = object.getInt("gz");
                    int rectificationSum = object.getInt("rectificationSum");

                    if (fireEngine == 0) {
                        gza.setVisibility(View.VISIBLE);
                        gzb.setVisibility(View.GONE);
                    } else {
                        gza.setVisibility(View.GONE);
                        gzb.setVisibility(View.VISIBLE);
                        gzb.setText(fireEngine + "");
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(Throwable arg0) {
            }
        });
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        getActivity().unregisterReceiver(Receiver);
        super.onDestroy();
    }

}
