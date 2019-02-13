package com.mincat.sample.manager.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONObject;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mincat.sample.R;
import com.mincat.sample.custom.CustomProgressDialog;
import com.mincat.sample.utils.SpUtils;
import com.mincat.sample.utils.StatusBarUtil;
import com.mincat.sample.utils.T;


/**
 * @author Mings
 */

public abstract class AppCompat extends AppCompatUnifiedManager {
    public static final String TAG = AppCompat.class.getName();

    protected CustomProgressDialog customProgressDialog = CustomProgressDialog.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {


        super.onCreate(savedInstanceState, persistentState);
    }


    //封装 toolbar 到基类中
    protected void initToolBar(int id) {
        toolbar = (Toolbar) findViewById(id);
        //toolbar.setClickable(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //toolbar返回键按钮
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            activitySlideRight();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //初始化3D效果按钮
    protected void initFabButton(int id) {
        mFabButton = (FloatingActionButton) findViewById(id);
        mFabButton.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_file_upload).color(Color.WHITE).actionBar());
        mFabButton.setOnClickListener(this);
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }


    protected Object parseResult(String result, Class<?> cls) {

        return JSONObject.parseObject(result, cls);


    }

    protected void showNetConnectionErrorToast(Context context) {
        T.showLong(context, getString(R.string.http_is_null));
    }

    protected boolean isLogin() {

        return SpUtils.getLoginInfo(this);
    }

    /**
     * 设置statusBar 背景色为白色
     */
    @TargetApi(19)
    protected void setBarColorWhite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.white));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置statusBar 背景色为橙色
     */
    @TargetApi(19)
    protected void setBarColorOrange() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.main_orange));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置statusBar 背景色为红色
     */
    @TargetApi(19)
    protected void setBarColorRed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.md_red_500));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置statusBar 背景色为透明
     */
    @TargetApi(19)
    protected void setBarColorTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.transparent));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


}
