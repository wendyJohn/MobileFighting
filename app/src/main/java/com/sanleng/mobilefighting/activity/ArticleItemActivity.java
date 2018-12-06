package com.sanleng.mobilefighting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;

/**
 * 文章详情
 *
 * @author Qiaoshi
 */
public class ArticleItemActivity extends Activity {
	private RelativeLayout r_back;
	private WebView webView;
	private TextView t_name;
	private TextView t_category;
	private TextView t_frequency;

	private String url;
	private String name;
	private String category;
	private String frequency;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_articleitem);
		intview();
	}

	private void intview() {
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		name = intent.getStringExtra("name");
		category = intent.getStringExtra("category");
		frequency = intent.getStringExtra("frequency");

		t_name = (TextView) findViewById(R.id.t_name);
		t_category = (TextView) findViewById(R.id.t_category);
		t_frequency = (TextView) findViewById(R.id.t_frequency);

		t_name.setText(name);
		t_category.setText(category);
		t_frequency.setText(frequency);

		webView = (WebView) findViewById(R.id.webview);

		webView.loadUrl(url);// 加载url
		r_back = (RelativeLayout) findViewById(R.id.r_back);
		r_back.setOnClickListener(new MyOnClickListener(0));
	}

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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// 释放资源
		webView.destroy();
		webView = null;
		super.onDestroy();
	}
}
