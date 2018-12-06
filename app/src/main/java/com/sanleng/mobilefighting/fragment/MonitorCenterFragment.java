package com.sanleng.mobilefighting.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.activity.DeviceItemActivity;
import com.sanleng.mobilefighting.activity.HostMonitoringActivity;
import com.sanleng.mobilefighting.activity.WaterSystemActivity;
import com.sanleng.mobilefighting.adapter.FunctionAdapter;
import com.sanleng.mobilefighting.myview.MyGridView;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;
import com.sanleng.mobilefighting.video.activity.MonitorVideoActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 监控单元
 *
 * @author Qiaoshi
 */
public class MonitorCenterFragment extends Fragment implements OnItemClickListener {

    private MyGridView itemGrid;
    private FunctionAdapter adapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.monitorcenter_fragment, null);
        initView();
        loadData();
        WaterSystemloadData();
        return view;
    }

    private void initView() {
        itemGrid = (MyGridView) view.findViewById(R.id.item_grid);
        adapter = new FunctionAdapter(getActivity(), R.array.myfunction_name_itemjs,
                R.array.myfunction_icon_itemjs);
        itemGrid.setAdapter(adapter);
        itemGrid.setOnItemClickListener(this);
    }


    // 缓存火警统计数据
    private void loadData() {
        RequestParams params = new RequestParams();
        params.put("unitcode", PreferenceUtils.getString(getActivity(), "unitcode"));
        params.put("username", PreferenceUtils.getString(getActivity(), "MobileFig_username"));
        params.put("platformkey", "app_firecontrol_owner");

        RequestUtils.ClientPost(URLs.FireAlarmStatistics, params, new NetCallBack() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onMySuccess(String result) {
                if (result == null || result.length() == 0) {
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String msg = jsonObject.getString("msg");
                    int unhandlefire = jsonObject.getInt("unhandlefire");
                    int todayfire = jsonObject.getInt("todayfire");
                    int truefire = jsonObject.getInt("truefire");
                    int missfire = jsonObject.getInt("missfire");
                    int weekfire = jsonObject.getInt("weekfire");

                    PreferenceUtils.setInt(getActivity(), "unhandlefire", unhandlefire);
                    PreferenceUtils.setInt(getActivity(), "todayfire", todayfire);
                    PreferenceUtils.setInt(getActivity(), "truefire", truefire);
                    PreferenceUtils.setInt(getActivity(), "missfire", missfire);
                    PreferenceUtils.setInt(getActivity(), "weekfire", weekfire);

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

    // 提前缓存水系統统计数据
    private void WaterSystemloadData() {
        RequestParams params = new RequestParams();
        params.put("unitcode", PreferenceUtils.getString(getActivity(), "unitcode"));
        params.put("username", PreferenceUtils.getString(getActivity(), "MobileFig_username"));
        params.put("platformkey", "app_firecontrol_owner");

        RequestUtils.ClientPost(URLs.WaterSystemStatistics_URL, params, new NetCallBack() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onMySuccess(String result) {
                if (result == null || result.length() == 0) {
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String msg = jsonObject.getString("msg");
                    int hyrant = jsonObject.getInt("hyrant");
                    int eqt = jsonObject.getInt("eqt");
                    int water = jsonObject.getInt("water");

                    PreferenceUtils.setInt(getActivity(), "hyrant", hyrant);
                    PreferenceUtils.setInt(getActivity(), "eqt", eqt);
                    PreferenceUtils.setInt(getActivity(), "water", water);

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            // 主机监测
            case 0:
                Intent intent_hostMonitoring = new Intent(getActivity(), HostMonitoringActivity.class);
                startActivity(intent_hostMonitoring);
                break;
            // 水系统
            case 1:
                WaterSystemloadData();
                Intent intent_watersystem = new Intent(getActivity(), WaterSystemActivity.class);
                startActivity(intent_watersystem);
                break;
            // 电气火灾
            case 2:
                Intent intent_architecture = new Intent(getActivity(), DeviceItemActivity.class);
                startActivity(intent_architecture);
                break;
            // 防排烟
            case 3:

                break;
            // 防火门
            case 4:

                break;
            // 视频应用
            case 5:
                Intent intent_ipLogin = new Intent(getActivity(), MonitorVideoActivity.class);
                startActivity(intent_ipLogin);

                break;
            // 主机自检
            case 6:

                break;
            default:
                break;
        }


    }
}
