package com.sanleng.mobilefighting.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.sanleng.mobilefighting.R;
import com.sanleng.mobilefighting.dialog.PromptDialog;
import com.sanleng.mobilefighting.jpush.TagAliasOperatorHelper;
import com.sanleng.mobilefighting.jpush.TagAliasOperatorHelper.TagAliasBean;
import com.sanleng.mobilefighting.net.NetCallBack;
import com.sanleng.mobilefighting.net.RequestUtils;
import com.sanleng.mobilefighting.net.URLs;
import com.sanleng.mobilefighting.util.PreferenceUtils;
import com.sanleng.mobilefighting.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登陆界面
 *
 * @author qiaoshi
 */
public class LoginActivity extends Activity implements OnClickListener, TextWatcher {
    private EditText login_number;
    private EditText login_password;
    private Button login_btn;
    private RelativeLayout scrollviewRootLayout;
    private TextView login_questions;
    private String userName;
    private String password;
    private String lastAccount;
    private String lastPwd;
    private PromptDialog promptDialog;
    private CheckBox whether_contact;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.login_activity);
        initView();
    }

    private void initView() {
        // 创建对象
        promptDialog = new PromptDialog(this);
        // 设置自定义属性
        promptDialog.getDefaultBuilder().touchAble(true).round(3).loadingDuration(2000);
        login_btn = (Button) findViewById(R.id.login_btn);
        login_number = (EditText) findViewById(R.id.login_number);
        login_password = (EditText) findViewById(R.id.login_password);
        //	scrollviewRootLayout = (RelativeLayout) findViewById(R.id.scrollviewRootLayout);
        login_questions = (TextView) findViewById(R.id.login_questions);
        login_btn.setOnClickListener(this);
        login_password.setOnClickListener(this);
        login_number.addTextChangedListener(this);
        login_password.addTextChangedListener(this);
        //	controlKeyboardLayout(scrollviewRootLayout, login_questions);

        whether_contact = (CheckBox) findViewById(R.id.whether_contact);
        whether_contact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    PreferenceUtils.setInt(LoginActivity.this, "state", 1);
                } else {
                    PreferenceUtils.setInt(LoginActivity.this, "state", 0);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int state = PreferenceUtils.getInt(LoginActivity.this, "state");
        if (state == 1) {
            // 记住上次登录的信息
            lastAccount = PreferenceUtils.getString(this, "MobileFig_username");
            lastPwd = PreferenceUtils.getString(this, "MobileFig_password");
            if (!StringUtils.isEmpty(lastAccount) && !StringUtils.isEmpty(lastPwd)) {
                login_number.setText(lastAccount);
                login_password.setText(lastPwd);

//                Intent intent_pwdchange = new Intent(LoginActivity.this, MainFireAlarmActivity.class);
//                startActivity(intent_pwdchange);

                Intent intent_pwdchange = new Intent(LoginActivity.this, MainTabActivity.class);
                startActivity(intent_pwdchange);

                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                //测试
//                Intent intent_pwdchange = new Intent(LoginActivity.this, MainTabActivity.class);
//                startActivity(intent_pwdchange);
//                finish();

                userName = login_number.getText().toString().trim();
                password = login_password.getText().toString().trim();
                RequestParams params = new RequestParams();
                params.put("username", userName);
                params.put("password", password);
                params.put("platformkey", "app_firecontrol_owner");
                RequestUtils.ClientPost(URLs.BULOGIN_URL, params, new NetCallBack() {
                    @Override
                    public void onStart() {
                        promptDialog.showLoading("正在登录...");
                        super.onStart();
                    }

                    @Override
                    public void onMySuccess(String result) {
                        if (result == null || result.length() == 0) {
                            return;
                        }
                        System.out.println("数据请求成功" + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String msg = jsonObject.getString("msg");

                            if (msg.equals("登录成功")) {
                                promptDialog.showSuccess("登录成功");
                                String data = jsonObject.getString("data");
                                JSONObject object = new JSONObject(data);
                                String unitcode = object.getString("unitcode");
                                String agentName = object.getString("name");

                                TagAliasBean tagAliasBean = new TagAliasBean();
                                tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET;
                                tagAliasBean.alias = unitcode;
                                tagAliasBean.isAliasAction = true;

                                TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), 1, tagAliasBean);

                                // 存入数据库中（登录名称和密码）
                                PreferenceUtils.setString(LoginActivity.this, "MobileFig_username", userName);
                                PreferenceUtils.setString(LoginActivity.this, "MobileFig_password", password);
                                // 单位ID
                                PreferenceUtils.setString(LoginActivity.this, "unitcode", unitcode);
                                // 人员名称
                                PreferenceUtils.setString(LoginActivity.this, "agentName", agentName);

                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        // 等待2000毫秒后销毁此页面，并提示登陆成功
//                                        Intent intent_pwdchange = new Intent(LoginActivity.this, MainFireAlarmActivity.class);
//                                        startActivity(intent_pwdchange);

                                        Intent intent_pwdchange = new Intent(LoginActivity.this, MainTabActivity.class);
                                        startActivity(intent_pwdchange);

                                        finish();
                                    }
                                }, 1000);
                            } else {
                                promptDialog.showError(msg);
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onMyFailure(Throwable arg0) {
                        promptDialog.showError("登录失败");
                    }
                });

                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }

    @SuppressLint("ResourceType")
    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
        boolean Sign1 = login_number.getText().length() > 0;

        if (Sign1) {
            login_btn.setBackgroundDrawable(getResources().getDrawable(R.xml.login_btn_color));
            login_btn.setEnabled(true);
        } else {
            login_btn.setBackgroundDrawable(getResources().getDrawable(R.xml.btn_enable_blue));
            login_btn.setEnabled(false);
        }
    }

    /**
     * //	 * @param              键盘出现时，移动布局，是scrollToView出现在键盘上方。
     * //	 * @param root         最外层布局，需要调整的布局，注：若界面中有ScrollView布局，则为ScrollView下的LinearLayout，
     * 其他特殊布局，请根据需要自行传入外层布局。
     *
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    public void controlKeyboardLayout(final View root, final View scrollToView) {
        try {

            root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Rect rect = new Rect();
                    // 获取root在窗体的可视区域
                    root.getWindowVisibleDisplayFrame(rect);
                    // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                    int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                    // 若不可视区域高度大于100，则键盘显示
                    if (rootInvisibleHeight > 100) {
                        int[] location = new int[2];
                        // 获取scrollToView在窗体的坐标
                        scrollToView.getLocationInWindow(location);
                        // 计算root滚动高度，使scrollToView在可见区域
                        int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                        root.scrollTo(0, srollHeight);
                    } else {
                        // 键盘隐藏
                        root.scrollTo(0, 0);
                    }
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
