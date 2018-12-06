package com.sanleng.mobilefighting.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;

public class FireFormationDialog extends Dialog {

	Context context;
	private TextView name;
	private TextView address;
	private TextView time;
	private TextView equipment;

	private LinearLayout monitor;

	private android.view.View.OnClickListener clickListener2;

	private String alarmEquipment;
	private String alarmUnit;
	private String alarmaddress;
	private String alarmTime;

	private ImageView canle;

	public FireFormationDialog(Context context, android.view.View.OnClickListener clickListener2, String alarmEquipment,
							   String alarmUnit, String alarmaddress, String alarmTime) {
		super(context);
		this.context = context;
		this.alarmEquipment = alarmEquipment;
		this.alarmUnit = alarmUnit;
		this.alarmaddress = alarmaddress;
		this.alarmTime = alarmTime;
		this.clickListener2 = clickListener2;
	}

	public FireFormationDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.setContentView(R.layout.fireinformationdialog);
		// this.setCancelable(false);// 设置点击屏幕Dialog不消失

		name = (TextView) findViewById(R.id.name);
		address = (TextView) findViewById(R.id.address);
		time = (TextView) findViewById(R.id.time);
		equipment = (TextView) findViewById(R.id.equipment);
		name.setText("报警单位：" + alarmUnit);
		address.setText("报警地址：" + alarmaddress);
		time.setText("报警时间：" + alarmTime);
		equipment.setText("火警信息：" + alarmEquipment);

		monitor = (LinearLayout) findViewById(R.id.monitor);
		canle = (ImageView) findViewById(R.id.canle);
		monitor.setOnClickListener((android.view.View.OnClickListener) clickListener2);
		canle.setOnClickListener((android.view.View.OnClickListener) clickListener2);
	}

}
