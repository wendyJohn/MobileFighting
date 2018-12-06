package com.sanleng.mobilefighting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;

import java.util.ArrayList;
import java.util.List;

/**
 * 应急开锁
 *
 * @author qiaoshi
 *
 */
public class EmergencyUnlockingActivity extends Activity implements OnClickListener {
	private String mac;
	private Button btn_one;
	private Button btn_two;
	private Button btn_three;
	private Button btn_four;
	private Button btn_onekey;
	private RelativeLayout r_back;
	private List<String> list = new ArrayList<>();
	private String str;
	private int i = 0;// 次数

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.open_door);
		initview();

		list.add("A");
		list.add("B");
		list.add("C");
		list.add("D");
	}

	private void initview() {
		Intent intent = getIntent();
		mac = intent.getStringExtra("mac");
		btn_one = (Button) findViewById(R.id.btn_one);
		btn_two = (Button) findViewById(R.id.btn_two);
		btn_three = (Button) findViewById(R.id.btn_three);
		btn_four = (Button) findViewById(R.id.btn_four);
		btn_onekey = (Button) findViewById(R.id.btn_onekey);
		btn_one.setOnClickListener(this);
		btn_two.setOnClickListener(this);
		btn_three.setOnClickListener(this);
		btn_four.setOnClickListener(this);
		btn_onekey.setOnClickListener(this);
		r_back = (RelativeLayout) findViewById(R.id.r_back);
		r_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// 一号开锁
			case R.id.btn_one:
				Unlock("A", mac);
				break;
			// 二号开锁
			case R.id.btn_two:
				Unlock("B", mac);
				break;
			// 三号开锁
			case R.id.btn_three:
				Unlock("C", mac);
				break;
			// 四号开锁
			case R.id.btn_four:
				Unlock("D", mac);
				break;
			// 一键开启
			case R.id.btn_onekey:
				i = 0;
				handler.postDelayed(runnable, 2000);// 每两秒执行一次runnable.
				break;
			// 返回
			case R.id.r_back:
				finish();
				break;
			default:
				break;
		}
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (i == 4) {
				handler.removeCallbacks(runnable);
			} else {
				str = list.get(i).toString();
				i++;
				Unlock(str, mac);
				// 要做的事情
				handler.postDelayed(this, 2000);
			}

		}
	};

	// 开锁方式
	private void Unlock(String position, final String mac) {
		RequestUtils.ClientPost(URLs.ORDER_BASE_URL + "/" + position + "/" + mac, null, new NetCallBack() {
			@Override
			public void onStart() {
				Log.d("qs", URLs.ORDER_BASE_URL);
				super.onStart();
			}

			@Override
			public void onMySuccess(String result) {
				if (result == null || result.length() == 0) {
					return;
				}
				System.out.println("数据请求成功" + result);
			}

			@Override
			public void onMyFailure(Throwable arg0) {

			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		handler.removeCallbacks(runnable);
		super.onDestroy();
	}

}
