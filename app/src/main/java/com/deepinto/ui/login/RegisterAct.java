package com.deepinto.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.deepinto.Bean.BaseBean;
import com.deepinto.R;
import com.deepinto.utils.Constant;
import com.mincat.sample.manager.post.BaseVolleyPost;
import com.mincat.sample.utils.L;
import com.mincat.sample.utils.T;
import com.mincat.sample.utils.ToJsonArray;

/**
 * @author Ming
 * @desc 注册页
 */
public class RegisterAct extends BaseVolleyPost {

    public static final int REGISTER_RESULT_CODE = 0x0012;
    private AppCompatImageButton mIconCloseAct;
    private EditText mEtMobile, mVerifyCode, mEtPassword;
    private Button mBtnGetVerifyCode, mBtnRegisterUser;
    private String[] paramKey;
    private String username, verify_code, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);
        initView();
    }

    @Override
    public void initView() {
        mIconCloseAct = getId(R.id.icon_close_act);
        mIconCloseAct.setOnClickListener(this);

        mEtMobile = getId(R.id.et_mobile);
        mVerifyCode = getId(R.id.et_verify_code);

        mBtnGetVerifyCode = getId(R.id.btn_get_verify_code);
        mBtnRegisterUser = getId(R.id.btn_register_user);

        mBtnGetVerifyCode.setOnClickListener(this);
        mBtnRegisterUser.setOnClickListener(this);

        mEtPassword = getId(R.id.et_password);

    }

    @Override
    public void onNetRequest() {

        paramKey = new String[]{"username", "verify_code", "password"};

        username = etString(mEtMobile);
        verify_code = etString(mVerifyCode);
        password = etString(mEtPassword);

        if (TextUtils.isEmpty(username)) {

            T.showShort(RegisterAct.this, "请输入手机号码");

            return;
        } else if (TextUtils.isEmpty(verify_code)) {

            T.showShort(RegisterAct.this, "请输入验证码");
            return;
        } else if (TextUtils.isEmpty(password)) {
            T.showShort(RegisterAct.this, "请输入密码");
            return;

        } else if (password.length() < 6 && password.length() > 12) {
            T.showShort(RegisterAct.this, "密码为6-12位数字字母组成");
            return;
        }


        String[] paramValue = {username, verify_code, password};

        String param = ToJsonArray.toJsonArray(paramKey, paramValue);

        executeVolleyPostRequest(this, Constant.REGISTER_URL, param, Constant.REGISTER_SIGN, false);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_close_act:// 关闭当前页面

                this.finish();
                break;
            case R.id.btn_get_verify_code: // 获取验证码
                mBtnGetVerifyCode.setText("验证码已发送");
                break;

            case R.id.btn_register_user:// 注册用户
                onNetRequest();

                break;
        }

    }

    @Override
    public void onHandleResponsePost(String response, String sign) {
        if (sign.equals(Constant.REGISTER_SIGN)) {


            BaseBean bean = JSONObject.parseObject(response, BaseBean.class);

            if (bean.getCode().equals("1") && bean.getMsg().equals("注册成功")) {

                T.showShort(RegisterAct.this, "注册成功！");
                Intent intent = new Intent();
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                setResult(REGISTER_RESULT_CODE, intent);
                this.finish();

            }
        }

    }

    @Override
    public void errorListener(VolleyError error, String sign) {

    }
}
