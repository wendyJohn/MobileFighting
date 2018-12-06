package com.sanleng.mobilefighting.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;
import com.sanleng.mobilefighting.util.SVProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 物资详情
 *
 * @author qiaoshi
 *
 */
public class MaterialDetailsDialog extends Dialog implements android.view.View.OnClickListener {

	private Context context;
	private TextView notice;
	private String ids;
	private TextView name;
	private TextView number;
	private TextView specification;
	private TextView model;

	private TextView stationName;
	private TextView effective;
	private TextView stationAddress;
	private TextView storageLocation;

	public MaterialDetailsDialog(Context context, String ids) {
		super(context);
		this.context = context;
		this.ids = ids;
	}

	public MaterialDetailsDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.setContentView(R.layout.materialdetailsdialog);
		this.setCancelable(false);// 设置点击屏幕Dialog不消失
		notice = (TextView) findViewById(R.id.notice);

		name = (TextView) findViewById(R.id.name);
		number = (TextView) findViewById(R.id.number);
		specification = (TextView) findViewById(R.id.specification);
		model = (TextView) findViewById(R.id.model);

		stationName = (TextView) findViewById(R.id.stationName);
		effective = (TextView) findViewById(R.id.effective);
		stationAddress = (TextView) findViewById(R.id.stationAddress);
		storageLocation = (TextView) findViewById(R.id.storageLocation);

		notice.setOnClickListener(this);
		MaterialDetails();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// 知道了
			case R.id.notice:
				dismiss();
				break;

			default:
				break;
		}
	}

	// 获取物资详情
	private void MaterialDetails() {
		RequestParams params = new RequestParams();
		params.put("ids", ids);
		params.put("username", PreferenceUtils.getString(context, "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");
		RequestUtils.ClientPost(URLs.MaterialDetails_URL, params, new NetCallBack() {
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
					String myname = object.getString("name");
					String mynumber = object.getString("number");
					String myspecification = object.getString("specification");
					String mymodel = object.getString("model");

					String mystationName = object.getString("stationName");
					String myeffective = object.getString("effective");
					String mystationAddress = object.getString("stationAddress");
					String mystorageLocation = object.getString("storageLocation");

					name.setText("物资名称：" + myname);
					number.setText("物资编号：" + mynumber);
					specification.setText("物资数量：" + myspecification);
					model.setText("物资型号：" + mymodel);

					stationName.setText("站点名称：" + "南京站点一");
					effective.setText("有效期：" + myeffective);
					stationAddress.setText("站点地址：" + "江苏省南京市江宁区秣陵街道三棱科技（集团）有限公司");
					storageLocation.setText("存储位置：" + "一号箱");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onMyFailure(Throwable arg0) {
				SVProgressHUD.showErrorWithStatus(context, "物资信息加载失败");
			}
		});

	}
}
