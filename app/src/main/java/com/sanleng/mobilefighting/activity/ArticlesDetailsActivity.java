package com.sanleng.mobilefighting.activity;

import com.sanleng.mobilefighting.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

/**
 * 火灾显示盘
 *
 * @author Qiaoshi
 *
 */
public class ArticlesDetailsActivity extends Activity implements OnClickListener {

	private RelativeLayout r_back;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.articlesdetailsactivity);
		initView();

	}

	private void initView() {
		r_back = (RelativeLayout) findViewById(R.id.r_back);
		r_back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// 返回
			case R.id.r_back:
				finish();
				break;
			default:
				break;
		}
	}
}
