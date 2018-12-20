package com.sanleng.mobilefighting.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RadioButton;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.fragment.AlarmRecordFragment;
import com.sanleng.mobilefighting.fragment.E_RealTimeDataFragment;
import com.sanleng.mobilefighting.fragment.NewMineFragment;
import com.sanleng.mobilefighting.util.BuilderManager;
import com.sanleng.mobilefighting.util.DrawableUtil;

import org.xclcharts.common.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 智能电气火灾报警系统
 *
 * @author Qaioshi
 */
public class MainFireAlarmActivity extends FragmentActivity {

    private ViewPager viewPager;// 页卡内容
//    private ImageView imageView;// 动画图片

    private List<Fragment> fragments;// Tab页面列表
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private static final int pageSize = 3;
    private static final String BROADCAST_PERMISSION_DISC = "com.permissions.MY_BROADCAST";
    private static final String BROADCAST_ACTION_DISC = "com.permissions.my_broadcast";

    private static final String BROADCAST_PERMISSION_DISCS = "com.permissions.MY_BROADCASTS";
    private static final String BROADCAST_ACTION_DISCS = "com.permissions.my_broadcasts";

    private Receiver receiver;
    private SweetAlertDialog sweetAlertDialog;
    private BoomMenuButton bmb;
    private RadioButton opa, opb, opc;
    private int mBottomDrawableSize;
    // 默认图片
    private static final int[] TAB_ICON_NORMAL_IDS = new int[] { R.drawable.emergencya_on,
            R.drawable.emergencyb_on, R.drawable.emergencyc_on };
    // 点击图片
    private static final int[] TAB_ICON_ACTIVE_IDS = new int[] { R.drawable.emergencya_in,
            R.drawable.emergencyb_in, R.drawable.emergencyc_in };
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        this.setContentView(R.layout.mainfirealarmactivity);
        initview();
    }

    private void initview() {
        receiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_DISC); // 只有持有相同的action的接受者才能接收此广
        registerReceiver(receiver, intentFilter, BROADCAST_PERMISSION_DISC, null);
        mBottomDrawableSize = DensityUtil.dip2px(this, 30);
        opa = (RadioButton) findViewById(R.id.opa);
        opb = (RadioButton) findViewById(R.id.opb);
        opc = (RadioButton) findViewById(R.id.opc);
        InitTextView();
        InitViewPager();
        setBottomTextColor(0);
        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.SimpleCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_9_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_9_1);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++)
            bmb.addBuilder(BuilderManager.getSimpleCircleButtonBuilder()
            .listener(new OnBMClickListener() {
                @Override
                public void onBoomButtonClick(int index) {
                    Toast.makeText(MainFireAlarmActivity.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
                }
            })
            );
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
        fragments.add(new E_RealTimeDataFragment());
        fragments.add(new AlarmRecordFragment());
        fragments.add(new NewMineFragment());
        viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {
        opa.setOnClickListener(new MyOnClickListener(0));
        opb.setOnClickListener(new MyOnClickListener(1));
        opc.setOnClickListener(new MyOnClickListener(2));
    }

    /**
     * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
     */

//    private void InitImageView() {
//        imageView = (ImageView) findViewById(R.id.cursor);
//        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.tab_selected_bg).getWidth();// 获取图片宽度
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenW = dm.widthPixels;// 获取分辨率宽度
//        offset = (screenW / pageSize - bmpW) / 2;// 计算偏移量--(屏幕宽度/页卡总数-图片实际宽度)/2//
//        // = 偏移量
//        Matrix matrix = new Matrix();
//        matrix.postTranslate(offset, 0);
//        imageView.setImageMatrix(matrix);// 设置动画初始位置
//    }

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
                    setBottomTextColor(0);
                    break;
                case 1:
                    setBottomTextColor(1);
                    break;
                case 2:
                    setBottomTextColor(2);
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

            switch (index) {
                case 0:
                    setBottomTextColor(0);
                    break;
                case 1:
                    setBottomTextColor(1);
                    break;
                case 2:
                    setBottomTextColor(2);
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

    // 报警广播处理
    public class Receiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BROADCAST_ACTION_DISC)) {
                Intent myintent = new Intent(BROADCAST_ACTION_DISCS);
                context.sendBroadcast(myintent, BROADCAST_PERMISSION_DISCS);

                String str = intent.getStringExtra("str_test");

//                sweetAlertDialog = new SweetAlertDialog(MainFireAlarmActivity.this, SweetAlertDialog.WARNING_TYPE);
//                sweetAlertDialog.setCancelable(false);
//                sweetAlertDialog.setTitleText("报警提醒!");
//                sweetAlertDialog.setContentText(str);
//                sweetAlertDialog.setConfirmText("确认信息");
//                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.setTitleText("报警处理!");
//                        sDialog.setContentText("请尽快安排相应人员处理报警");
//                        sDialog.setConfirmText("确认");
//                        sDialog.setConfirmClickListener(null);
//                        sDialog.setCancelable(false);
//                        sDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                    }
//                })
//                        .show();

            }
        }
    }


    private void setBottomTextColor(int position) {
        opa.setTextColor(getResources().getColor(R.color.gray_bold));
        opb.setTextColor(getResources().getColor(R.color.gray_bold));
        opc.setTextColor(getResources().getColor(R.color.gray_bold));
        Drawable d1 = DrawableUtil.getDrawable(MainFireAlarmActivity.this, TAB_ICON_NORMAL_IDS[0]);

        d1.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
        opa.setCompoundDrawables(null, d1, null, null);
        Drawable d2 = DrawableUtil.getDrawable(MainFireAlarmActivity.this, TAB_ICON_NORMAL_IDS[1]);
        d2.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
        opb.setCompoundDrawables(null, d2, null, null);
        Drawable d3 = DrawableUtil.getDrawable(MainFireAlarmActivity.this, TAB_ICON_NORMAL_IDS[2]);
        d3.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
        opc.setCompoundDrawables(null, d3, null, null);
        switch (position) {
            case 0:
                Drawable dz = DrawableUtil.getDrawable(MainFireAlarmActivity.this, TAB_ICON_ACTIVE_IDS[0]);
                dz.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
                opa.setTextColor(getResources().getColor(R.color.text_blue));
                opa.setCompoundDrawables(null, dz, null, null);
                break;
            case 1:
                Drawable dz1 = DrawableUtil.getDrawable(MainFireAlarmActivity.this, TAB_ICON_ACTIVE_IDS[1]);
                dz1.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
                opb.setTextColor(getResources().getColor(R.color.text_blue));
                opb.setCompoundDrawables(null, dz1, null, null);
                break;
            case 2:
                Drawable dz2 = DrawableUtil.getDrawable(MainFireAlarmActivity.this, TAB_ICON_ACTIVE_IDS[2]);
                dz2.setBounds(0, 0, mBottomDrawableSize, mBottomDrawableSize);
                opc.setTextColor(getResources().getColor(R.color.text_blue));
                opc.setCompoundDrawables(null, dz2, null, null);
                break;
            default:
                break;
        }

    }
}
