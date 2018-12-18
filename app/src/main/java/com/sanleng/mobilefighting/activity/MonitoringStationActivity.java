package com.sanleng.mobilefighting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.EmergencystationAdapter;
import com.sanleng.mobilefighting.bean.ArchitectureBean;
import com.sanleng.mobilefighting.dialog.PromptDialog;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;
import com.sanleng.mobilefighting.video.activity.MonitorVideoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 监控站列表
 *
 * @author Qiaoshi
 */
public class MonitoringStationActivity extends Activity {

    private RelativeLayout r_back;
    private ListView eslistview;
    private EmergencystationAdapter emergencystationAdapter;
    private int pageNo = 1;// 设置pageNo的初始化值为1，即默认获取的是第一页的数据。
    private int allpage;
    private List<ArchitectureBean> allList;// 存放所有数据AlarmBean的list集合
    private List<ArchitectureBean> onelist;// 存放一页数据实体类的Bean
    // 加载提示
    private PromptDialog promptDialog;
    private boolean is_divPage;// 是否进行分页操作
    private boolean finish = true;// 是否加载完成;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        this.setContentView(R.layout.emergencystationactivity);
        initview();
        loadData(pageNo);
    }

    private void initview() {
        // 创建对象
        promptDialog = new PromptDialog(this);
        // 设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(2000);
        emergencystationAdapter = new EmergencystationAdapter();
        allList = new ArrayList<ArchitectureBean>();
        r_back = (RelativeLayout) findViewById(R.id.r_back);
        r_back.setOnClickListener(new MyOnClickListener(0));
    }

    // 加载数据
    private void loadData(int page) {
        onelist = new ArrayList<ArchitectureBean>();
        RequestParams params = new RequestParams();
        params.put("pageNum", page + "");
        params.put("pageSize", "10");
        params.put("unit_code", PreferenceUtils.getString(MonitoringStationActivity.this, "unitcode"));
        params.put("username", PreferenceUtils.getString(MonitoringStationActivity.this, "MobileFig_username"));
        params.put("platformkey", "app_firecontrol_owner");

        RequestUtils.ClientPost(URLs.EmergencyStation_URL, params, new NetCallBack() {
            @Override
            public void onStart() {
//				promptDialog.showLoading("正在加载...");
                super.onStart();
            }

            @Override
            public void onMySuccess(String result) {
                if (result == null || result.length() == 0) {
                    return;
                }
                System.out.println("数据请求成功" + result);
//				promptDialog.dismiss();
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
                            ArchitectureBean bean = new ArchitectureBean();
                            object = (JSONObject) array.get(i);
                            String name = object.getString("name");
                            String mac = object.getString("mac");

                            String address = object.getString("address");
                            bean.setName(name);
                            bean.setAddress(address);
                            bean.setMac(mac);
                            onelist.add(bean);
                        }

                        if (SIZE % length == 0) {
                            allpage = SIZE / length;
                        } else {
                            allpage = SIZE / length + 1;
                        }

                        eslistview = (ListView) findViewById(R.id.eslistview);
                        allList.addAll(onelist);
                        emergencystationAdapter.bindData(MonitoringStationActivity.this, allList);
                        if (pageNo == 1) {
                            // 没有数据就提示暂无数据。
                            eslistview.setEmptyView(findViewById(R.id.nodata));
                            eslistview.setAdapter(emergencystationAdapter);
                        }
                        emergencystationAdapter.notifyDataSetChanged();
                        pageNo++;

                        finish = true;

                        eslistview.setOnScrollListener(new OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {
                                /**
                                 * 当分页操作is_divPage为true时、滑动停止时、且pageNo<=allpage（ 这里因为服务端有allpage页数据）时，加载更多数据。
                                 */
                                if (is_divPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE && pageNo <= allpage
                                        && finish) {
                                    finish = false;
                                    loadData(pageNo);
                                } else if (pageNo > allpage && finish) {
                                    finish = false;
                                    // 如果pageNo>allpage则表示，服务端没有更多的数据可供加载了。
                                    Toast.makeText(MonitoringStationActivity.this, "加载完了！", Toast.LENGTH_SHORT).show();
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

                        eslistview.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // TODO Auto-generated method stub
                                Intent intent_Inspection = new Intent(MonitoringStationActivity.this, MonitorVideoActivity.class);
                                intent_Inspection.putExtra("mAddress", "10.101.208.102");
                                intent_Inspection.putExtra("mPort", "51033");
                                intent_Inspection.putExtra("mUsername", "admin");
                                intent_Inspection.putExtra("mPassword", "admin");
                                startActivity(intent_Inspection);
                            }
                        });

                    } else {
                        promptDialog.showError(msg);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(Throwable arg0) {
                promptDialog.showError("加载失败");
            }
        });

    }

    /**
     * 头标点击监听
     */
    private class MyOnClickListener implements OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            switch (index) {
                case 0:
                    finish();
                    break;
            }

        }
    }

}
