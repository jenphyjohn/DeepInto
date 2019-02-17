package com.deepinto.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.VolleyError;
import com.deepinto.R;
import com.deepinto.fra.ClassificationFra;
import com.deepinto.fra.FoundFra;
import com.deepinto.fra.MainFra;
import com.deepinto.fra.MeFra;
import com.deepinto.fra.ShoppingFra;
import com.mincat.sample.manager.post.BaseVolleyPost;
import com.mincat.sample.utils.AppManager;
import com.mincat.sample.utils.Constants;
import com.mincat.sample.utils.L;
import com.mincat.sample.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ming
 * @描述 首页
 */
public class MainHomeAct extends BaseVolleyPost implements RadioGroup.OnCheckedChangeListener {


    private static String homepage = "mMainFra";

    private long exitTimeMillis = System.currentTimeMillis();
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private MainFra mMainFra;
    private ClassificationFra mClassificationFra;
    private FoundFra mFoundFra;
    private ShoppingFra mShoppingFra;
    private MeFra mMeFragment;

    private FragmentManager fm;
    private FragmentTransaction transaction;
    private RadioGroup mRadioButtonRg;
    private FragmentTransaction transaction1;
    private RadioButton mRadioHome, mRadioAround, mRadioInformation, mMy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initView();
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            mMainFra = new MainFra();
            fragmentManager.beginTransaction().replace(R.id.fl, mMainFra, homepage).commit();
        }

        L.i(TAG, "是否已登录:" + isLogin());
    }


    // 初始化控件
    public void initView() {

        mRadioButtonRg = getId(R.id.r_g);
        mRadioButtonRg.setOnCheckedChangeListener(this);
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        mMainFra = (MainFra) fm.findFragmentByTag(homepage);
        mClassificationFra = (ClassificationFra) fm.findFragmentByTag("mClassificationFra");
        mFoundFra = (FoundFra) fm.findFragmentByTag("mFoundFra");
        mShoppingFra = (ShoppingFra) fm.findFragmentByTag("mShoppingFra");
        mMeFragment = (MeFra) fm.findFragmentByTag("mMeFragment");

        mRadioHome = getId(R.id.r_home);
        mRadioAround = getId(R.id.r_around);
        mRadioInformation = getId(R.id.r_information);
        mMy = getId(R.id.r_my);
    }


    /**
     * tab切换事件处理
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        transaction1 = fm.beginTransaction();
        if (mMainFra != null) {
            transaction1.hide(mMainFra);
        }
        if (mClassificationFra != null) {
            transaction1.hide(mClassificationFra);
        }
        if (mFoundFra != null) {
            transaction1.hide(mFoundFra);
        }
        if (mShoppingFra != null) {
            transaction1.hide(mShoppingFra);
        }
        if (mMeFragment != null) {
            transaction1.hide(mMeFragment);
        }


        if (checkedId == R.id.r_home) {
            if (mMainFra == null) {
                mMainFra = new MainFra();
                transaction1.add(R.id.fl, mMainFra, homepage);
            } else {
                transaction1.show(mMainFra);
            }

        } else if (checkedId == R.id.r_around) {
            if (mClassificationFra == null) {
                mClassificationFra = new ClassificationFra();
                transaction1.add(R.id.fl, mClassificationFra, "mClassificationFra");
            } else {
                transaction1.show(mClassificationFra);
            }

        } else if (checkedId == R.id.r_qr_card) {
            if (mFoundFra == null) {
                mFoundFra = new FoundFra();
                transaction1.add(R.id.fl, mFoundFra, "mFoundFra");
            } else {
                transaction1.show(mFoundFra);
            }

        } else if (checkedId == R.id.r_information) {
            if (mShoppingFra == null) {
                mShoppingFra = new ShoppingFra();
                transaction1.add(R.id.fl, mShoppingFra, "mShoppingFra");
            } else {
                transaction1.show(mShoppingFra);
            }

        } else if (checkedId == R.id.r_my) {
            if (mMeFragment == null) {
                mMeFragment = new MeFra();
                transaction1.add(R.id.fl, mMeFragment, "mMeFragment");
            } else {
                transaction1.show(mMeFragment);
            }
        }
        transaction1.commit();
    }

    @Override
    public void onNetRequest() {

    }

    @Override
    public void onClick(View view) {

    }

    // 再按一次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - exitTimeMillis == Constants.ZERO || currentTimeMillis - exitTimeMillis > Constants.EXIT_CONTINUED) {
                exitTimeMillis = System.currentTimeMillis();
                T.showShort(this, Constants.EXIT);
                return false;
            } else {
                finish();
                AppManager.getAppManager().AppExit(this);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override // 网络访问成功回调函数
    public void onHandleResponsePost(String response, String sign) {

    }

    @Override// 网络访问失败回调函数
    public void errorListener(VolleyError error, String sign) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

