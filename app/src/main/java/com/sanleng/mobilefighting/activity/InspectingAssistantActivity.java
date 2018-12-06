package com.sanleng.mobilefighting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;

/**
 * 巡查助手
 *
 * @author Qiaoshi
 *
 */
public class InspectingAssistantActivity extends Activity implements OnClickListener {

	private RelativeLayout r_back;
	private TextView articles;
	private TextView firewatersupply;
	private TextView fireartillery;
	private TextView sprinklerfireextinguishing;
	private TextView mechanicalpressurized;
	private TextView emergencylighting;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inspectingassistant_list);
		initView();
//

	}

	private void initView() {

		r_back = (RelativeLayout) findViewById(R.id.r_back);
		articles = (TextView) findViewById(R.id.articles);
		firewatersupply = (TextView) findViewById(R.id.firewatersupply);
		fireartillery = (TextView) findViewById(R.id.fireartillery);
		sprinklerfireextinguishing = (TextView) findViewById(R.id.sprinklerfireextinguishing);
		mechanicalpressurized = (TextView) findViewById(R.id.mechanicalpressurized);
		emergencylighting = (TextView) findViewById(R.id.emergencylighting);

		r_back.setOnClickListener(this);
		articles.setOnClickListener(this);
		firewatersupply.setOnClickListener(this);
		fireartillery.setOnClickListener(this);
		sprinklerfireextinguishing.setOnClickListener(this);
		mechanicalpressurized.setOnClickListener(this);
		emergencylighting.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// 火灾自动\n报警系统
			case R.id.articles:
				Intent intent_articles = new Intent(InspectingAssistantActivity.this, ArticlesActivity.class);
				startActivity(intent_articles);
				break;
			// 消防供水设施
			case R.id.firewatersupply:

				break;
			// 消防栓（消防炮）\n灭火系统
			case R.id.fireartillery:

				break;
			// 自动喷水灭火\n系统
			case R.id.sprinklerfireextinguishing:

				break;
			// 机械加压送风\n排风系统
			case R.id.mechanicalpressurized:

				break;
			// 应急照明
			case R.id.emergencylighting:

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
