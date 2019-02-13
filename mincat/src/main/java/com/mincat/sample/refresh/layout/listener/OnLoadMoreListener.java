package com.mincat.sample.refresh.layout.listener;

import android.support.annotation.NonNull;

import com.mincat.sample.refresh.layout.api.RefreshLayout;

/**
 * 加载更多监听器
 */

public interface OnLoadMoreListener {
    void onLoadMore(@NonNull RefreshLayout refreshLayout);
}
