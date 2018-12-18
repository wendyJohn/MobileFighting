package com.sanleng.mobilefighting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.adapter.PatrolRecordAdapter;
import com.sanleng.mobilefighting.bean.InSpTaskBean;
import com.sanleng.mobilefighting.dialog.PromptDialog;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 巡查总记录
 *
 * @author Qiaoshi
 *
 */
public class PatrolHandleActivity extends Activity implements OnClickListener {

	private RelativeLayout r_back;
	private TextView P_title;
	private TextView p_content;
	private TextView p_company;
	private TextView p_time;

	private ListView patrolrecordlslistview;
	private LinearLayout addrecord;
	private EditText info_editText;
	private Button commit_task;

	private String taskId;
	private String title;
	private String content;
	private String company;
	private String time;

	private int pageNo = 1;// 设置pageNo的初始化值为1，即默认获取的是第一页的数据。
	private int allpage;
	private List<InSpTaskBean> allList;// 存放所有数据AlarmBean的list集合
	private List<InSpTaskBean> onelist;// 存放一页数据实体类的Bean
	// 加载提示
	private PromptDialog promptDialog;
	private boolean is_divPage;// 是否进行分页操作
	private boolean finish = true;// 是否加载完成;
	private PatrolRecordAdapter patrolRecordAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patrolhandleactivity);
		initView();
	}

	private void initView() {
		// 创建对象
		promptDialog = new PromptDialog(this);
		// 设置自定义属性
		promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(2000);

		patrolRecordAdapter = new PatrolRecordAdapter();

		r_back = (RelativeLayout) findViewById(R.id.r_back);
		P_title = (TextView) findViewById(R.id.p_title);
		p_content = (TextView) findViewById(R.id.p_content);
		p_company = (TextView) findViewById(R.id.p_company);
		p_time = (TextView) findViewById(R.id.p_time);
		addrecord = (LinearLayout) findViewById(R.id.addrecord);
		info_editText = (EditText) findViewById(R.id.info_editText);
		commit_task = (Button) findViewById(R.id.commit_task);

		r_back.setOnClickListener(this);
		commit_task.setOnClickListener(this);
		addrecord.setOnClickListener(this);

		Intent intent = getIntent();
		taskId = intent.getStringExtra("taskId");
		title = intent.getStringExtra("title");
		content = intent.getStringExtra("content");
		company = intent.getStringExtra("company");
		time = intent.getStringExtra("time");

		P_title.setText(title);
		p_content.setText(content);
		p_company.setText(company);
		p_time.setText(time);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		pageNo = 1;
		if (pageNo == 1) {
			allList = new ArrayList<InSpTaskBean>();
			onelist = new ArrayList<InSpTaskBean>();
		}
		loadData(pageNo);

		super.onResume();
	}

	// 加载数据
	private void loadData(int page) {
		onelist = new ArrayList<InSpTaskBean>();
		RequestParams params = new RequestParams();
		params.put("pageNum", page + "");
		params.put("pageSize", "10");
		params.put("unitcode", PreferenceUtils.getString(PatrolHandleActivity.this, "unitcode"));
		params.put("username", PreferenceUtils.getString(PatrolHandleActivity.this, "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");

		RequestUtils.ClientPost(URLs.InspectionRecord, params, new NetCallBack() {
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
							InSpTaskBean bean = new InSpTaskBean();
							object = (JSONObject) array.get(i);

							String taskId = object.getString("ids");
							String qrcode_code = object.getString("patrolunitname");
							String equipment_name = object.getString("create_user");
							String inspection_info = object.getString("inspection_time");
							String equipment_status = object.getString("equipment_status");

							bean.setTitle(qrcode_code);
							bean.setContent(equipment_name);
							bean.setCompany(inspection_info);
							bean.setState(equipment_status);
							onelist.add(bean);
						}

						if (SIZE % length == 0) {
							allpage = SIZE / length;
						} else {
							allpage = SIZE / length + 1;
						}

						patrolrecordlslistview = (ListView) findViewById(R.id.patrolrecordlslistview);
						allList.addAll(onelist);
						patrolRecordAdapter.bindData(PatrolHandleActivity.this, allList);
						if (pageNo == 1) {
							patrolrecordlslistview.setEmptyView(findViewById(R.id.nodata));
							patrolrecordlslistview.setAdapter(patrolRecordAdapter);
						}
						patrolRecordAdapter.notifyDataSetChanged();
						pageNo++;

						finish = true;

						patrolrecordlslistview.setOnScrollListener(new OnScrollListener() {
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
									Toast.makeText(PatrolHandleActivity.this, "加载完了！", Toast.LENGTH_SHORT).show();
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// 提交
			case R.id.commit_task:

				break;
			// 添加新的巡查记录
			case R.id.addrecord:

				break;
			// 返回
			case R.id.r_back:
				finish();
				break;
			default:
				break;
		}
	}
}
