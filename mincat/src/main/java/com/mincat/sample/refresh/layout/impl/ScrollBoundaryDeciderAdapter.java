package com.mincat.sample.refresh.layout.impl;

import android.graphics.PointF;
import android.view.View;

import com.mincat.sample.refresh.layout.api.ScrollBoundaryDecider;
import com.mincat.sample.refresh.layout.util.ScrollBoundaryUtil;

/**
 * 滚动边界
 */

public class ScrollBoundaryDeciderAdapter implements ScrollBoundaryDecider {

    public PointF mActionEvent;
    public ScrollBoundaryDecider boundary;
    public boolean mEnableLoadMoreWhenContentNotFull = true;


    @Override
    public boolean canRefresh(View content) {
        if (boundary != null) {
            return boundary.canRefresh(content);
        }
        //mActionEvent == null 时 canRefresh 不会动态递归搜索
        return ScrollBoundaryUtil.canRefresh(content, mActionEvent);
    }

    @Override
    public boolean canLoadMore(View content) {
        if (boundary != null) {
            return boundary.canLoadMore(content);
        }
        return ScrollBoundaryUtil.canLoadMore(content, mActionEvent, mEnableLoadMoreWhenContentNotFull);
    }
}
