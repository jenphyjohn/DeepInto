package com.deepinto.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.deepinto.R;
import com.deepinto.ui.login.LoginAct;
import com.mincat.sample.manager.base.AppCompat;
import com.mincat.sample.utils.GetAppSha1;
import com.mincat.sample.utils.L;
import com.mincat.sample.utils.WindowTitle;

/**
 * @author Ming
 * @描述 启动页
 */
public class SplashAct extends AppCompat {

    public static SplashAct instance = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_splash);
        instance = this;
        initView();
    }


    @Override
    public void initView() {

        WindowTitle.fullScreen(this);
        String sha1 = GetAppSha1.getSha1(this);
        L.i(TAG, "SHA1:" + sha1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashAct.this, MainHomeAct.class));
            }
        }, 3000);
    }

    @Override
    public void onNetRequest() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {
    }
}
