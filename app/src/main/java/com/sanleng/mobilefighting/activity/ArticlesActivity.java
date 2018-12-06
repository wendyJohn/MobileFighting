package com.sanleng.mobilefighting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.sanleng.mobilefighting.R;

public class ArticlesActivity extends Activity implements OnClickListener {

    private RelativeLayout r_back;
    private RelativeLayout firedisplaypanel;
    private RelativeLayout firefightinglinkage;
    private RelativeLayout firealarmcontrol;
    private RelativeLayout pointtypesmoke;
    private RelativeLayout manualalarmbutton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articlesactivity);
        initView();
//

    }

    private void initView() {

        r_back = (RelativeLayout) findViewById(R.id.r_back);

        firedisplaypanel = (RelativeLayout) findViewById(R.id.firedisplaypanel);
        firefightinglinkage = (RelativeLayout) findViewById(R.id.firefightinglinkage);
        firealarmcontrol = (RelativeLayout) findViewById(R.id.firealarmcontrol);
        pointtypesmoke = (RelativeLayout) findViewById(R.id.pointtypesmoke);
        manualalarmbutton = (RelativeLayout) findViewById(R.id.manualalarmbutton);

        r_back.setOnClickListener(this);
        firedisplaypanel.setOnClickListener(this);
        firefightinglinkage.setOnClickListener(this);
        firealarmcontrol.setOnClickListener(this);
        pointtypesmoke.setOnClickListener(this);
        manualalarmbutton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            // 火灾显示盘
            case R.id.firedisplaypanel:
                Intent intent_articlesdetails = new Intent(ArticlesActivity.this, ArticlesDetailsActivity.class);
                startActivity(intent_articlesdetails);
                break;
            // 消防联动控制器
            case R.id.firefightinglinkage:

                break;
            // 火灾报警控制器
            case R.id.fireartillery:

                break;
            // 点型感烟探测器
            case R.id.firealarmcontrol:

                break;
            // 手动报警按钮
            case R.id.manualalarmbutton:

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
