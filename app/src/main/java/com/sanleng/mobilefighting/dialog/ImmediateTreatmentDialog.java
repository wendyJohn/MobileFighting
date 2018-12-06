package com.sanleng.mobilefighting.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ImmediateTreatmentDialog extends Dialog implements OnClickListener {
	Context context;
	private ImageView bt_cancel;
	private TextView bt_confire;
	private String staus = "101";
	private String taskId;
	private Handler mHandler;

	public ImmediateTreatmentDialog(Context context, String taskId, Handler mHandler) {
		super(context);
		this.context = context;
		this.taskId = taskId;
		this.mHandler = mHandler;
	}

	public ImmediateTreatmentDialog(Context context, int taskID) {
		super(context, taskID);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.setContentView(R.layout.immediatetreatmentdialog);
		initview();
	}

	private void initview() {
		bt_cancel = (ImageView) findViewById(R.id.bt_cancel);
		bt_confire = (TextView) findViewById(R.id.bt_confire);
		bt_cancel.setOnClickListener(this);
		bt_confire.setOnClickListener(this);
		// 有效
		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup1);
		// 绑定一个匿名监听器
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) findViewById(radioButtonId);
				String s = rb.getText().toString();
				if (s.equals("真实火警")) {
					staus = "101";
				}
				if (s.equals("误报")) {
					staus = "102";
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.bt_cancel) {
			dismiss();
		} else if (v.getId() == R.id.bt_confire) {
			upload();
			dismiss();
		}
	}

	private void upload() {
		RequestParams params = new RequestParams();
		params.put("ids", taskId);
		params.put("alarmtype", staus);
		params.put("username", PreferenceUtils.getString(context, "MobileFig_username"));
		params.put("platformkey", "app_firecontrol_owner");
		RequestUtils.ClientPost(URLs.ConfirmFireAlarm_URL, params, new NetCallBack() {
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
					String msg = jsonObject.getString("msg");
					if(msg.equals("警情处理成功")) {
						Message mymsg = new Message();
						mymsg.what = 234233;
						mHandler.sendMessage(mymsg);
					}else {
						Message mymsg = new Message();
						mymsg.what = 234232;
						mHandler.sendMessage(mymsg);
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
