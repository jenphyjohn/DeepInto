package com.deepinto.ui.mall.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.deepinto.R;
import com.deepinto.entity.BaseBean;
import com.deepinto.entity.mall.RegisterEntity;
import com.deepinto.entity.mall.VerifyCodeEntity;
import com.deepinto.ui.mall.login.LoginAct;
import com.deepinto.utils.Constant;
import com.mincat.sample.manager.post.BaseVolleyPost;
import com.mincat.sample.utils.FilterEtLength;
import com.mincat.sample.utils.GetDeviceId;
import com.mincat.sample.utils.L;
import com.mincat.sample.utils.MD5;
import com.mincat.sample.utils.T;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * @author Ming
 * @desc 注册页
 */
public class RegisterAct extends BaseVolleyPost {

    public static final int REGISTER_RESULT_CODE = 0x0012;
    private AppCompatImageButton mIconCloseAct;
    private EditText mEtMobile, mVerifyCode, mEtPassword;
    private Button mBtnGetVerifyCode, mBtnRegisterUser;
    private RegisterEntity mRegisterEntity = new RegisterEntity();
    private String username, password, verifyCode, device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);
        initView();
    }

    @Override
    public void initView() {
        setBarColorOrange();
        mIconCloseAct = getId(R.id.icon_close_act);
        mIconCloseAct.setOnClickListener(this);

        mEtMobile = getId(R.id.et_mobile);
        mVerifyCode = getId(R.id.et_verify_code);

        mBtnGetVerifyCode = getId(R.id.btn_get_verify_code);
        mBtnRegisterUser = getId(R.id.btn_register_user);

        mBtnGetVerifyCode.setOnClickListener(this);
        mBtnRegisterUser.setOnClickListener(this);

        mEtPassword = getId(R.id.et_password);

        FilterEtLength.filter(mEtMobile, 11);
        FilterEtLength.filter(mEtPassword, 12);
        FilterEtLength.filter(mVerifyCode, 6);

    }


    // 验证验证码
    public void onNetRequestVerifyCode() {


        verifyCode = etString(mVerifyCode);

        VerifyCodeEntity entity = new VerifyCodeEntity();

        entity.setVerifyCode(verifyCode);

        String param = JSONObject.toJSONString(entity);

        executeVolleyPostRequest(this, Constant.VERIFY_CODE_URL, param, Constant.VERIFY_CODE, false);

    }

    @Override// 注册
    public void onNetRequest() {


        username = etString(mEtMobile);
        password = etString(mEtPassword);

        device_id = GetDeviceId.getDeviceId(this);
        if (TextUtils.isEmpty(username)) {

            T.showShort(RegisterAct.this, "请输入手机号码");

            return;
        } else if (TextUtils.isEmpty(password)) {
            T.showShort(RegisterAct.this, "请输入密码");
            return;

        } else if (password.length() < 6 && password.length() > 12) {
            T.showShort(RegisterAct.this, "密码为6-12位数字字母组成");
            return;
        }

        try {
            mRegisterEntity.setUsername(username);
            mRegisterEntity.setPassword(MD5.getMd5(password));
            mRegisterEntity.setCtime(new Date());
            mRegisterEntity.setPhonet(Constant.PHONE_T);
            mRegisterEntity.setPhoneid(device_id);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String param = JSONObject.toJSONString(mRegisterEntity);

        L.i(TAG, "传给服务器的json参数:" + param);

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
                onNetRequestVerifyCode();

                break;
        }

    }

    @Override
    public void onHandleResponsePost(String response, String sign) {
        BaseBean bean = JSONObject.parseObject(response, BaseBean.class);

        if (sign.equals(Constant.REGISTER_SIGN)) {

            if (bean.getStatus() == Constant.SUCCESS_CODE) {

                Message message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);

                Intent intent = new Intent(RegisterAct.this, LoginAct.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                setResult(REGISTER_RESULT_CODE, intent);
                RegisterAct.this.finish();

            } else if (bean.getStatus() ==Constant.FIALED_CODE_205){

                Message message = Message.obtain();
                message.what = 4;
                handler.sendMessage(message);
            }else {
                Message message = Message.obtain();
                message.what = 2;
                handler.sendMessage(message);

            }
        } else if (sign.equals(Constant.VERIFY_CODE)) {

            if (bean.getStatus() == Constant.SUCCESS_CODE) {

                onNetRequest();
            } else {

                Message message = Message.obtain();
                message.what = 3;
                handler.sendMessage(message);
            }

        }

    }

    @Override
    public void errorListener(VolleyError error, String sign) {

    }

    Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 1:
                    T.showShort(RegisterAct.this, "注册成功");
                    break;
                case 2:
                    T.showShort(RegisterAct.this, "注册失败,请稍后再试");
                    break;
                case 3:
                    T.showShort(RegisterAct.this, "验证码错误,请重新获取");
                    break;
                case 4:
                    T.showShort(RegisterAct.this, "该用户已存在,请登录");
                    break;
            }

            super.handleMessage(msg);

        }
    };
}
