package com.deepinto.ui.mall.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.deepinto.R;
import com.deepinto.entity.BaseBean;
import com.deepinto.entity.mall.LoginEntity;
import com.deepinto.ui.MainHomeAct;
import com.deepinto.ui.mall.register.RegisterAct;
import com.deepinto.utils.Constant;
import com.mincat.sample.manager.post.BaseVolleyPost;
import com.mincat.sample.utils.FilterEtLength;
import com.mincat.sample.utils.L;
import com.mincat.sample.utils.MD5;
import com.mincat.sample.utils.SpUtils;
import com.mincat.sample.utils.T;

import java.security.NoSuchAlgorithmException;

/**
 * @author Ming
 * @desc 登录页
 */
public class LoginAct extends BaseVolleyPost {
    private static final int REGISTER_REQUEST_CODE = 0x0010;
    private EditText mEtUserName;
    private EditText mEtPassword;
    private String textUserName;
    private String textPassWord;
    private Button mBtnLogin;
    private TextView mTvRegister;
    private LoginEntity entity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        initView();
    }

    @Override
    public void initView() {
        setBarColorOrange();

        mEtUserName = getId(R.id.et_username);
        mEtPassword = getId(R.id.et_password);
        mBtnLogin = getId(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mTvRegister = getId(R.id.tv_register);
        mTvRegister.setOnClickListener(this);

        FilterEtLength.filter(mEtUserName, 11);
        FilterEtLength.filter(mEtPassword, 12);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_login:
                onNetRequest();
                break;

            case R.id.tv_register:

                intentUtils.openActivityFromRightForResult(this, RegisterAct.class, REGISTER_REQUEST_CODE);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RegisterAct.REGISTER_RESULT_CODE) {

            String dataUsername = data.getStringExtra("username");
            String dataPassword = data.getStringExtra("password");

            mEtUserName.setText(dataUsername);
            mEtPassword.setText(dataPassword);
        }


    }

    @Override // 执行网络请求
    public void onNetRequest() {

        textUserName = etString(mEtUserName);
        textPassWord = etString(mEtPassword);

        try {
            entity = new LoginEntity();
            entity.setUsername(textUserName);
            entity.setPassword(MD5.getMd5(textPassWord));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String mLoginParam = JSONObject.toJSONString(entity);

        executeVolleyPostRequest(this, Constant.LOGIN_URL, mLoginParam, Constant.LOGIN_SIGN, false);

    }

    @Override
    public void onHandleResponsePost(String response, String sign) {

        if (sign.equals(Constant.LOGIN_SIGN)) {

            BaseBean baseBean = com.alibaba.fastjson.JSONObject.parseObject(response, BaseBean.class);

            if (baseBean.getStatus()==Constant.SUCCESS_CODE) {

                T.showShort(LoginAct.this, "登录成功");
                intentUtils.openActivityFromRight(LoginAct.this, MainHomeAct.class);

                SpUtils.setLoginInfo(textUserName, textPassWord, "", this);

            } else {
                T.showShort(LoginAct.this, "登录失败,请稍后再试");
            }
        }

    }

    @Override
    public void errorListener(VolleyError error, String sign) {

        L.i(TAG, "请求失败:" + error);

    }
}
