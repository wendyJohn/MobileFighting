package com.sanleng.mobilefighting.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;

public class FalseHintsDialog extends Dialog implements android.view.View.OnClickListener {

	Context context;
	private TextView message;
	private TextView tv_dialog_retry;

	public FalseHintsDialog(Context context) {
		super(context);
		this.context = context;
	}

	public FalseHintsDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.setContentView(R.layout.falsehintsdialog);
		this.setCancelable(false);// 设置点击屏幕Dialog不消失
		message = (TextView) findViewById(R.id.login_message);
		tv_dialog_retry = (TextView) findViewById(R.id.signout);
		tv_dialog_retry.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.signout:
				System.exit(0);
				break;
			default:
				break;
		}
	}

}
