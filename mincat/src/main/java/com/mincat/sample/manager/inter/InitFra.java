package com.mincat.sample.manager.inter;

import android.view.View;

import com.android.volley.VolleyError;

/**
 * 初始化Fragment
 *
 * @author Ming
 */
public interface InitFra extends View.OnClickListener {

    /**
     * 初始化方法
     *
     * @param view
     */
    void initView(View view);

    /**
     * 点击事件
     *
     * @param v view对象
     */
    @Override
    void onClick(View v);

}
