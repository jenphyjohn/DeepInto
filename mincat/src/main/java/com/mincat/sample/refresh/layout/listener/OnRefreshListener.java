package com.mincat.sample.refresh.layout.listener;

import android.support.annotation.NonNull;

import com.mincat.sample.refresh.layout.api.RefreshLayout;

/**
 * 刷新监听器
 */

public interface OnRefreshListener {
    void onRefresh(@NonNull RefreshLayout refreshLayout);
}
