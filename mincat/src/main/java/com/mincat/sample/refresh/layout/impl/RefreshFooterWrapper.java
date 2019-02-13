package com.mincat.sample.refresh.layout.impl;

import android.view.View;

import com.mincat.sample.refresh.layout.api.RefreshFooter;
import com.mincat.sample.refresh.layout.internal.InternalAbstract;

/**
 * 刷新底部包装
 */
public class RefreshFooterWrapper extends InternalAbstract implements RefreshFooter/*, InvocationHandler */{


    public RefreshFooterWrapper(View wrapper) {
        super(wrapper);
    }


    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return mWrappedInternal instanceof RefreshFooter && ((RefreshFooter) mWrappedInternal).setNoMoreData(noMoreData);
    }

}
