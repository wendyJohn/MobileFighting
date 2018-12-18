package com.sanleng.mobilefighting.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.activity.LoginActivity;
import com.sanleng.mobilefighting.util.PreferenceUtils;

//我
public class Fragment_Profile extends Fragment implements OnClickListener {
    private Activity ctx;
    private View layout;
    private TextView tvname, tv_accout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (layout == null) {
            ctx = this.getActivity();
            layout = ctx.getLayoutInflater().inflate(R.layout.fragment_profile,
                    null);
            initViews();
            initData();
            setOnListener();
        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        return layout;
    }

    private void initViews() {
        tvname = (TextView) layout.findViewById(R.id.tvname);
        tv_accout = (TextView) layout.findViewById(R.id.tvmsg);
        tvname.setText(PreferenceUtils.getString(getActivity(), "MobileFig_username"));

    }

    private void setOnListener() {
        layout.findViewById(R.id.view_user).setOnClickListener(this);
        layout.findViewById(R.id.changepassword).setOnClickListener(this);
        layout.findViewById(R.id.scavengingcaching).setOnClickListener(this);
        layout.findViewById(R.id.dataupdate).setOnClickListener(this);
        layout.findViewById(R.id.versionupdate).setOnClickListener(this);
        layout.findViewById(R.id.aboutus).setOnClickListener(this);
        layout.findViewById(R.id.login_out).setOnClickListener(this);
    }

    private void initData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_user:

                break;
            case R.id.changepassword://修改密码

                break;
            case R.id.scavengingcaching://清除缓存

                break;
            case R.id.dataupdate:// 数据更新

                break;
            case R.id.versionupdate://版本更新

                break;
            case R.id.aboutus:// 关于我们

                break;
            case R.id.login_out:// 退出登录
                // 清空sharepre中的用户名和密码
                PreferenceUtils.setString(getActivity().getApplicationContext(), "MobileFig_username", "");
                PreferenceUtils.setString(getActivity().getApplicationContext(), "MobileFig_password", "");
                Intent loginOutIntent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                loginOutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginOutIntent);
                getActivity().finish();
                break;
            default:
                break;
        }
    }

}