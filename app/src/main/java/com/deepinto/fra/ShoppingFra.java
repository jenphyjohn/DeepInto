package com.deepinto.fra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.deepinto.R;
import com.mincat.sample.manager.BaseFragment;

/**
 * @author Ming
 * @描述 购物车
 */
public class ShoppingFra extends BaseFragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fra_shopping, container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onHandleResponsePost(String response, String sign) {

    }

    @Override
    public void errorListener(VolleyError error, String sign) {

    }
}
