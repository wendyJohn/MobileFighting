package com.sanleng.mobilefighting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PwdChangeActivity extends Activity implements OnClickListener {

    private EditText originalpassword;// 原密码
    private EditText newpassword;// 新密码
    private EditText confirmnewpassword;// 重复新密码
    private Button btn_passwordmodification;// 确定
    private RelativeLayout task_ac_back;
    private SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passwordmodificationactivity);
        initView();
        initListener();
    }

    private void initView() {
        originalpassword = (EditText) findViewById(R.id.originalpassword);
        newpassword = (EditText) findViewById(R.id.newpassword);
        confirmnewpassword = (EditText) findViewById(R.id.confirmnewpassword);
        btn_passwordmodification = (Button) findViewById(R.id.btn_passwordmodification);
        task_ac_back = (RelativeLayout) findViewById(R.id.task_ac_back);

    }

    private void initListener() {
        btn_passwordmodification.setOnClickListener(this);
        task_ac_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_passwordmodification:
                doChangePwd();
                break;
            case R.id.task_ac_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 发送修改密码请求
     */
    private void doChangePwd() {
        String pwd = originalpassword.getText().toString().trim();
        String newpwd = newpassword.getText().toString().trim();
        String renewpwd = confirmnewpassword.getText().toString().trim();
        if (isEquale(pwd, newpwd, renewpwd)) {
            return;
        }
        RequestParams params = new RequestParams();
        params.put("username", PreferenceUtils.getString(this, "MobileFig_username"));
        params.put("oldpassword", pwd);
        params.put("newpassword", newpwd);
        params.put("platformkey", "app_firecontrol_owner");

        RequestUtils.ClientPost(URLs.PasswordModification, params, new NetCallBack() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onMySuccess(String result) {
                if (result == null || result.length() == 0) {
                    return;
                }
                System.out.println("数据请求成功" + result);
                try {
                    JSONObject jsonobject = new JSONObject(result);
                    String msg = jsonobject.getString("msg");
                    if (msg.equals("密码修改成功,请重新登录")) {
                        sweetAlertDialog = new SweetAlertDialog(PwdChangeActivity.this, SweetAlertDialog.WARNING_TYPE);
                        sweetAlertDialog.setContentText("密码修改成功,请重新登录！");
                        sweetAlertDialog.setConfirmText("重新登录");
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                // 清空sharepre中的用户名和密码
                                PreferenceUtils.setString(PwdChangeActivity.this, "MobileFig_username", "");
                                PreferenceUtils.setString(PwdChangeActivity.this, "MobileFig_password", "");
                                Intent loginOutIntent = new Intent(PwdChangeActivity.this, LoginActivity.class);
                                loginOutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(loginOutIntent);
                                finish();
                            }
                        });
                        sweetAlertDialog.show();
                    }
                    if (msg.equals("旧密码不正确")) {
                        new SweetAlertDialog(PwdChangeActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("旧密码不正确")
                                .show();
                    }
                    if (msg.equals("用户不存在")) {
                        new SweetAlertDialog(PwdChangeActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("用户不存在")
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(Throwable arg0) {
                new SweetAlertDialog(PwdChangeActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("温馨提示")
                        .setContentText("服务器访问异常!")
                        .show();
            }
        });


    }

    /**
     * 检测密码是否一致，是否为空
     *
     * @param pwd
     * @param newpwd
     * @param renewpwd
     * @return true 不合法
     */
    private boolean isEquale(String pwd, String newpwd, String renewpwd) {
        boolean flag = false;
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(newpwd) || TextUtils.isEmpty(renewpwd)) {
            Toast.makeText(PwdChangeActivity.this, "信息输入不完整", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!newpwd.equals(renewpwd)) {
            Toast.makeText(PwdChangeActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return true;
        }
        return flag;
    }
}
