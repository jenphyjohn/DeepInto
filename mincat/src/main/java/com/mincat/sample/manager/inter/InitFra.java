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

    /**
     * 请求成功回调
     *
     * @param response 返回结果
     * @param sign     请求标记
     */
    void onHandleResponse(String response, String sign);

    /**
     * 请求失败回调
     *
     * @param error 错误信息
     * @param sign  请求标记
     */
    void errorListener(VolleyError error, String sign);
}
