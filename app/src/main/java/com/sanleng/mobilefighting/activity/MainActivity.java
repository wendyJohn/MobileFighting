package com.sanleng.mobilefighting.activity;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.util.Utils;

public class MainActivity extends TabActivity {
    private TabHost tabHost;
    private FrameLayout frame_home, frame_forum, frame_forums, frame_forumps;
    private ImageView tab_home, tab_bang, tab_bangs, tab_peresons;
    private TextView tab_home_text_clicktab_bang_text, tab_bang_text_click, tab_bang_text_clicks,
            tab_person_text_clicks;
    private TextView main_title_txt;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        tabHost = getTabHost();
        Intent intent_one = new Intent();
        intent_one.setClass(MainActivity.this, ManagementCenterActivity.class);
        tabHost.addTab(tabHost.newTabSpec("one").setIndicator("one").setContent(intent_one));

        Intent intent_two = new Intent();
        intent_two.setClass(MainActivity.this, MonitorCenterActivity.class);
        tabHost.addTab(tabHost.newTabSpec("two").setIndicator("two").setContent(intent_two));

        Intent intent_three = new Intent();
        intent_three.setClass(MainActivity.this, ManagementCenterActivity.class);
        tabHost.addTab(tabHost.newTabSpec("three").setIndicator("three").setContent(intent_three));

        Intent intent_four = new Intent();
        intent_four.setClass(MainActivity.this, ManagementCenterActivity.class);
        tabHost.addTab(tabHost.newTabSpec("four").setIndicator("four").setContent(intent_three));

        frame_home = (FrameLayout) findViewById(R.id.frame_home);
        frame_forum = (FrameLayout) findViewById(R.id.frame_forum);
        frame_forums = (FrameLayout) findViewById(R.id.frame_forums);
        frame_forumps = (FrameLayout) findViewById(R.id.frame_forumps);

        frame_home.setOnClickListener(clickListener);
        frame_forum.setOnClickListener(clickListener);
        frame_forums.setOnClickListener(clickListener);
        frame_forumps.setOnClickListener(clickListener);

        tab_home = (ImageView) findViewById(R.id.tab_home_click);
        tab_home_text_clicktab_bang_text = (TextView) findViewById(R.id.tab_home_text);

        tab_bang = (ImageView) findViewById(R.id.tab_bang);
        tab_bang_text_click = (TextView) findViewById(R.id.tab_bang_text_click);

        tab_bangs = (ImageView) findViewById(R.id.tab_bangs);
        tab_bang_text_clicks = (TextView) findViewById(R.id.tab_bang_text_clicks);

        tab_peresons = (ImageView) findViewById(R.id.tab_bangps);
        tab_person_text_clicks = (TextView) findViewById(R.id.tab_person_text_clicks);

        main_title_txt = (TextView) findViewById(R.id.main_title_txt);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            // 版本更新升级
            // getServiceVersion();
            // wifiCode = 1;
        } else {
            // 判断有无网络，如果为true可以下载，如果为false要设置当前网络
            Boolean network = Utils.isConnectInternet(MainActivity.this);
            if (!network) {
                Toast.makeText(MainActivity.this, "当前无网络，请设置网络！", Toast.LENGTH_LONG).show();
            } else {
                // 版本更新升级
                // getServiceVersion();
            }
        }
    }

    OnClickListener clickListener = new OnClickListener() {

        @SuppressLint("ResourceAsColor")
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if (arg0 == frame_home) {
                tabHost.setCurrentTabByTag("one");
                tab_home.setImageResource(R.drawable.hp_in);
                tab_bangs.setImageResource(R.drawable.monitor_on);
                tab_bang.setImageResource(R.drawable.journal_on);
                tab_peresons.setImageResource(R.drawable.personal_on);

                tab_home_text_clicktab_bang_text
                        .setTextColor(MainActivity.this.getResources().getColor(R.color.text_blue));
                tab_bang_text_click.setTextColor(MainActivity.this.getResources().getColor(R.color.gray));
                tab_bang_text_clicks.setTextColor(MainActivity.this.getResources().getColor(R.color.gray));
                tab_person_text_clicks.setTextColor(MainActivity.this.getResources().getColor(R.color.gray));
                tab_home_text_clicktab_bang_text.setText("首页");
                main_title_txt.setText("首页");
            } else if (arg0 == frame_forums) {
                tabHost.setCurrentTabByTag("two");
                tab_home.setImageResource(R.drawable.hp_on);
                tab_bangs.setImageResource(R.drawable.monitor_in);
                tab_bang.setImageResource(R.drawable.journal_on);
                tab_peresons.setImageResource(R.drawable.personal_on);

                tab_home_text_clicktab_bang_text.setTextColor(MainActivity.this.getResources().getColor(R.color.gray));
                tab_bang_text_click.setTextColor(MainActivity.this.getResources().getColor(R.color.gray));
                tab_bang_text_clicks.setTextColor(MainActivity.this.getResources().getColor(R.color.text_blue));
                tab_person_text_clicks.setTextColor(MainActivity.this.getResources().getColor(R.color.gray));
                tab_bang_text_clicks.setText("监控");
                main_title_txt.setText("监控");
            } else if (arg0 == frame_forum) {
                tabHost.setCurrentTabByTag("three");
                tab_home.setImageResource(R.drawable.hp_on);
                tab_bangs.setImageResource(R.drawable.monitor_on);
                tab_bang.setImageResource(R.drawable.journal_in);
                tab_peresons.setImageResource(R.drawable.personal_on);

                tab_home_text_clicktab_bang_text.setTextColor(MainActivity.this.getResources().getColor(R.color.gray));
                tab_bang_text_click.setTextColor(MainActivity.this.getResources().getColor(R.color.text_blue));
                tab_bang_text_clicks.setTextColor(MainActivity.this.getResources().getColor(R.color.gray));
                tab_person_text_clicks.setTextColor(MainActivity.this.getResources().getColor(R.color.gray));
                tab_bang_text_click.setText("日志");
                main_title_txt.setText("日志");
            } else if (arg0 == frame_forumps) {
                tabHost.setCurrentTabByTag("four");
                tab_home.setImageResource(R.drawable.hp_on);
                tab_bangs.setImageResource(R.drawable.monitor_on);
                tab_bang.setImageResource(R.drawable.journal_on);
                tab_peresons.setImageResource(R.drawable.personal_in);

                tab_home_text_clicktab_bang_text.setTextColor(MainActivity.this.getResources().getColor(R.color.gray));
                tab_person_text_clicks.setTextColor(MainActivity.this.getResources().getColor(R.color.text_blue));
                tab_bang_text_clicks.setTextColor(MainActivity.this.getResources().getColor(R.color.gray));
                tab_bang_text_click.setTextColor(MainActivity.this.getResources().getColor(R.color.gray));
                tab_person_text_clicks.setText("个人");
                main_title_txt.setText("个人");
            }
        }
    };
}
