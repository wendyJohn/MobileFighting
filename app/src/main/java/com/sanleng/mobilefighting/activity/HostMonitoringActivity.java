package com.sanleng.mobilefighting.activity;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.fragment.FaultFragment;
import com.sanleng.mobilefighting.fragment.FireAlarmFragment;
import com.sanleng.mobilefighting.fragment.MisreportFragment;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 主机监控
 *
 * @author Qaioshi
 */
public class HostMonitoringActivity extends FragmentActivity implements OnClickListener {

    private RelativeLayout tasklist_back;
    public List<Integer> myIdList;
    private ViewPager viewPager;// 页卡内容
    private ImageView imageView;// 动画图片
    private TextView voiceAnswer, healthPedia, pDected;// 选项名称

    private List<Fragment> fragments;// Tab页面列表
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private int selectedColor, unSelectedColor;
    private int c_opa, c_opb, c_opc;
    /** 页卡总数 **/
    private static final int pageSize = 3;
    private LinearLayout opa, opb, opc;
    private ImageView im_opa, im_opb, im_opc;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        this.setContentView(R.layout.hostmonitoringactivity);
        initview();

    }

    private void initview() {
        tasklist_back = (RelativeLayout) findViewById(R.id.tasklist_back);
        tasklist_back.setOnClickListener(this);
        selectedColor = getResources().getColor(R.color.white);
        unSelectedColor = getResources().getColor(R.color.black);

        c_opa = getResources().getColor(R.color.copa);
        c_opb = getResources().getColor(R.color.copb);
        c_opc = getResources().getColor(R.color.copc);

        opa = (LinearLayout) findViewById(R.id.opa);
        opb = (LinearLayout) findViewById(R.id.opb);
        opc = (LinearLayout) findViewById(R.id.opc);

        im_opa = (ImageView) findViewById(R.id.im_opa);
        im_opb = (ImageView) findViewById(R.id.im_opb);
        im_opc = (ImageView) findViewById(R.id.im_opc);

        InitImageView();
        InitTextView();
        InitViewPager();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    /**
     * 初始化Viewpager页
     */
    private void InitViewPager() {
        viewPager = (ViewPager) findViewById(R.id.vPager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new FireAlarmFragment());
        fragments.add(new FaultFragment());
        fragments.add(new MisreportFragment());
        viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {
        voiceAnswer = (TextView) findViewById(R.id.tab_1);
        healthPedia = (TextView) findViewById(R.id.tab_2);
        pDected = (TextView) findViewById(R.id.tab_3);

        voiceAnswer.setTextColor(selectedColor);
        healthPedia.setTextColor(unSelectedColor);
        pDected.setTextColor(unSelectedColor);

        voiceAnswer.setText("火警");
        healthPedia.setText("故障");
        pDected.setText("误报");

        opa.setOnClickListener(new MyOnClickListener(0));
        opb.setOnClickListener(new MyOnClickListener(1));
        opc.setOnClickListener(new MyOnClickListener(2));
    }

    /**
     * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
     */

    private void InitImageView() {
        imageView = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.tab_selected_bg).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / pageSize - bmpW) / 2;// 计算偏移量--(屏幕宽度/页卡总数-图片实际宽度)/2//
        // = 偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        imageView.setImageMatrix(matrix);// 设置动画初始位置
    }

    /**
     * 头标点击监听
     */
    private class MyOnClickListener implements OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            switch (index) {
                case 0:
                    voiceAnswer.setTextColor(selectedColor);
                    healthPedia.setTextColor(unSelectedColor);
                    pDected.setTextColor(unSelectedColor);

                    opa.setBackgroundColor(c_opa);
                    opb.setBackgroundColor(selectedColor);
                    opc.setBackgroundColor(selectedColor);

                    im_opa.setImageDrawable(getResources().getDrawable(R.drawable.hj2));
                    im_opb.setImageDrawable(getResources().getDrawable(R.drawable.gz1));
                    im_opc.setImageDrawable(getResources().getDrawable(R.drawable.wb1));
                    FireAlarmloadData();
                    break;
                case 1:
                    healthPedia.setTextColor(selectedColor);
                    voiceAnswer.setTextColor(unSelectedColor);
                    pDected.setTextColor(unSelectedColor);

                    opa.setBackgroundColor(selectedColor);
                    opb.setBackgroundColor(c_opb);
                    opc.setBackgroundColor(selectedColor);

                    im_opa.setImageDrawable(getResources().getDrawable(R.drawable.hj));
                    im_opb.setImageDrawable(getResources().getDrawable(R.drawable.gz2));
                    im_opc.setImageDrawable(getResources().getDrawable(R.drawable.wb1));
                    break;
                case 2:
                    pDected.setTextColor(selectedColor);
                    voiceAnswer.setTextColor(unSelectedColor);
                    healthPedia.setTextColor(unSelectedColor);

                    opa.setBackgroundColor(selectedColor);
                    opb.setBackgroundColor(selectedColor);
                    opc.setBackgroundColor(c_opc);

                    im_opa.setImageDrawable(getResources().getDrawable(R.drawable.hj));
                    im_opb.setImageDrawable(getResources().getDrawable(R.drawable.gz1));
                    im_opc.setImageDrawable(getResources().getDrawable(R.drawable.wb2));
                    break;
            }
            viewPager.setCurrentItem(index);
        }
    }

    /**
     * 为选项卡绑定监听器
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {
        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        public void onPageScrollStateChanged(int index) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageSelected(int index) {
            Animation animation = new TranslateAnimation(one * currIndex, one * index, 0, 0);// 显然这个比较简洁，只有一行代码。
            currIndex = index;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            imageView.startAnimation(animation);

            switch (index) {
                case 0:
                    voiceAnswer.setTextColor(selectedColor);
                    healthPedia.setTextColor(unSelectedColor);
                    pDected.setTextColor(unSelectedColor);

                    opa.setBackgroundColor(c_opa);
                    opb.setBackgroundColor(selectedColor);
                    opc.setBackgroundColor(selectedColor);

                    im_opa.setImageDrawable(getResources().getDrawable(R.drawable.hj2));
                    im_opb.setImageDrawable(getResources().getDrawable(R.drawable.gz1));
                    im_opc.setImageDrawable(getResources().getDrawable(R.drawable.wb1));
                    break;
                case 1:
                    healthPedia.setTextColor(selectedColor);
                    voiceAnswer.setTextColor(unSelectedColor);
                    pDected.setTextColor(unSelectedColor);

                    opa.setBackgroundColor(selectedColor);
                    opb.setBackgroundColor(c_opb);
                    opc.setBackgroundColor(selectedColor);

                    im_opa.setImageDrawable(getResources().getDrawable(R.drawable.hj));
                    im_opb.setImageDrawable(getResources().getDrawable(R.drawable.gz2));
                    im_opc.setImageDrawable(getResources().getDrawable(R.drawable.wb1));
                    break;
                case 2:
                    pDected.setTextColor(selectedColor);
                    voiceAnswer.setTextColor(unSelectedColor);
                    healthPedia.setTextColor(unSelectedColor);

                    opa.setBackgroundColor(selectedColor);
                    opb.setBackgroundColor(selectedColor);
                    opc.setBackgroundColor(c_opc);

                    im_opa.setImageDrawable(getResources().getDrawable(R.drawable.hj));
                    im_opb.setImageDrawable(getResources().getDrawable(R.drawable.gz1));
                    im_opc.setImageDrawable(getResources().getDrawable(R.drawable.wb2));
                    break;
            }
        }
    }

    /**
     * 定义适配器
     */
    class myPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        /**
         * 得到每个页面
         */
        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        /**
         * 每个页面的title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        /**
         * 页面的总个数
         */
        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tasklist_back:
                finish();
                break;
            default:
                break;
        }
    }

    // 提前缓存火警统计数据
    private void FireAlarmloadData() {
        RequestParams params = new RequestParams();
        params.put("unitcode", PreferenceUtils.getString(HostMonitoringActivity.this, "unitcode"));
        params.put("username", PreferenceUtils.getString(HostMonitoringActivity.this, "MobileFig_username"));
        params.put("platformkey", "app_firecontrol_owner");

        RequestUtils.ClientPost(URLs.FireAlarmStatistics, params, new NetCallBack() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onMySuccess(String result) {
                if (result == null || result.length() == 0) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String msg = jsonObject.getString("msg");
                    int unhandlefire = jsonObject.getInt("unhandlefire");
                    int todayfire = jsonObject.getInt("todayfire");
                    int truefire = jsonObject.getInt("truefire");
                    int missfire = jsonObject.getInt("missfire");
                    int weekfire = jsonObject.getInt("weekfire");

                    PreferenceUtils.setInt(HostMonitoringActivity.this, "unhandlefire", unhandlefire);
                    PreferenceUtils.setInt(HostMonitoringActivity.this, "todayfire", todayfire);
                    PreferenceUtils.setInt(HostMonitoringActivity.this, "truefire", truefire);
                    PreferenceUtils.setInt(HostMonitoringActivity.this, "missfire", missfire);
                    PreferenceUtils.setInt(HostMonitoringActivity.this, "weekfire", weekfire);

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
