package com.mincat.sample.refresh.layout.impl;

import android.view.View;

import com.mincat.sample.refresh.layout.api.RefreshHeader;
import com.mincat.sample.refresh.layout.internal.InternalAbstract;

/**
 * 刷新头部包装
 */
public class RefreshHeaderWrapper extends InternalAbstract implements RefreshHeader/*, InvocationHandler*/ {


    public RefreshHeaderWrapper(View wrapper) {
        super(wrapper);
    }

}
