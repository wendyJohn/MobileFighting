package com.sanleng.mobilefighting.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.activity.AlarmRecordActivity;
import com.sanleng.mobilefighting.adapter.AlarmAdapter;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史报警记录
 *
 * @author Qiaoshi
 */
public class AlarmRecordFragment extends Fragment {

    private ListView alarmlistview;
    private int pageNo = 1;// 设置pageNo的初始化值为1，即默认获取的是第一页的数据。
    private int allpage;
    private boolean is_divPage;// 是否进行分页操作
    private boolean finish = true;// 是否加载完成;
    private AlarmAdapter alarmAdapter;
    private List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.alarmrecordfragment, null);
        initView();
        return view;
    }


    private void initView() {
        alarmAdapter = new AlarmAdapter();
        AddPolice(1);
    }


    //获取历史报警信息
    private void AddPolice(int page) {
        RequestParams params = new RequestParams();
        params.put("event_no", "142");
        params.put("pageNum", page + "");
        params.put("pageSize", "10");
        params.put("unit_code", PreferenceUtils.getString(getActivity(), "unitcode"));
        params.put("username", PreferenceUtils.getString(getActivity(), "MobileFig_username"));
        params.put("platformkey", "app_firecontrol_owner");

        RequestUtils.ClientPost(URLs.Police_URL, params, new NetCallBack() {
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
                    int length = 10;
                    int SIZE = 0;
                    JSONObject jsonObject = new JSONObject(result);
                    String msg = jsonObject.getString("msg");
                    if (msg.equals("获取成功")) {
                        String data = jsonObject.getString("data");
                        JSONObject objects = new JSONObject(data);
                        String listsize = objects.getString("total");
                        SIZE = Integer.parseInt(listsize);
                        String list = objects.getString("list");
                        JSONArray array = new JSONArray(list);
                        JSONObject object;
                        for (int i = 0; i < array.length(); i++) {
                            object = (JSONObject) array.get(i);
                            //获取数据
                            String unit_name = object.getString("unit_name");
                            String build_name = object.getString("build_name");
                            String equipment_name = object.getString("equipment_name");
                            String position = object.getString("position");
                            String receive_time = object.getString("receive_time");

                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("name", "设备名称：" + equipment_name);
                            map.put("postion", "设备位置：" + unit_name + build_name + position);
                            map.put("time", "报警时间：" + receive_time);
                            lists.add(map);
                        }
                        if (SIZE % length == 0) {
                            allpage = SIZE / length;
                        } else {
                            allpage = SIZE / length + 1;
                        }

                        alarmlistview = view.findViewById(R.id.alarmlistview);
                        allList.addAll(lists);
                        alarmAdapter.bindData(getActivity(), allList);
                        if (pageNo == 1) {
                            // 没有数据就提示暂无数据。
                            alarmlistview.setEmptyView(view.findViewById(R.id.nodata));
                            alarmlistview.setAdapter(alarmAdapter);
                        }
                        alarmAdapter.notifyDataSetChanged();
                        pageNo++;
                        finish = true;
                        alarmlistview.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {
                                /**
                                 * 当分页操作is_divPage为true时、滑动停止时、且pageNo<=allpage（ 这里因为服务端有allpage页数据）时，加载更多数据。
                                 */
                                if (is_divPage && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && pageNo <= allpage
                                        && finish) {
                                    finish = false;
                                    AddPolice(pageNo);
                                } else if (pageNo > allpage && finish) {
                                    finish = false;
                                    // 如果pageNo>allpage则表示，服务端没有更多的数据可供加载了。
                                    Toast.makeText(getActivity(), "加载完了！", Toast.LENGTH_SHORT).show();
                                }
                            }

                            // 当：第一个可见的item（firstVisibleItem）+可见的item的个数（visibleItemCount）=
                            // 所有的item总数的时候， is_divPage变为TRUE，这个时候才会加载数据。
                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                                 int totalItemCount) {
                                is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
                            }
                        });
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

}
