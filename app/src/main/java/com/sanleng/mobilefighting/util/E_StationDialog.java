package com.sanleng.mobilefighting.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;

public class E_StationDialog extends Dialog {

	Context context;
	private TextView name;
	private TextView address;
	private TextView time;
	private TextView equipment;

	private TextView dooropening;
	private TextView warehousings;
	private TextView outofstock;
	private TextView reportloss;

	private android.view.View.OnClickListener clickListener2;

	private String alarmEquipment;
	private String alarmUnit;
	private String alarmaddress;
	private String alarmTime;

	private ImageView canle;

	public E_StationDialog(Context context, android.view.View.OnClickListener clickListener2, String alarmEquipment,
						   String alarmUnit, String alarmaddress, String alarmTime) {
		super(context);
		this.context = context;
		this.alarmEquipment = alarmEquipment;
		this.alarmUnit = alarmUnit;
		this.alarmaddress = alarmaddress;
		this.alarmTime = alarmTime;
		this.clickListener2 = clickListener2;
	}

	public E_StationDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.setContentView(R.layout.e_stationdialog);
		// this.setCancelable(false);// 设置点击屏幕Dialog不消失

		name = (TextView) findViewById(R.id.name);
		address = (TextView) findViewById(R.id.address);
		time = (TextView) findViewById(R.id.time);
		equipment = (TextView) findViewById(R.id.equipment);
		name.setText("站点名称：" + alarmUnit);
		address.setText("站点地址：" + alarmaddress);

//		time.setText("报警时间：" + alarmTime);
//		equipment.setText("火警信息：" + alarmEquipment);

		dooropening = (TextView) findViewById(R.id.dooropening);
		warehousings = (TextView) findViewById(R.id.warehousings);
		outofstock = (TextView) findViewById(R.id.outofstock);
		reportloss = (TextView) findViewById(R.id.reportloss);

		canle = (ImageView) findViewById(R.id.canles);

		dooropening.setOnClickListener((android.view.View.OnClickListener) clickListener2);
		warehousings.setOnClickListener((android.view.View.OnClickListener) clickListener2);
		outofstock.setOnClickListener((android.view.View.OnClickListener) clickListener2);
		reportloss.setOnClickListener((android.view.View.OnClickListener) clickListener2);

		canle.setOnClickListener((android.view.View.OnClickListener) clickListener2);
	}

}
