package com.deepinto.fra;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.deepinto.R;
import com.deepinto.adapter.FraMainListAdapter;
import com.deepinto.entity.GetBannerImageInfo;
import com.deepinto.entity.ImageInfo;
import com.deepinto.ui.ScanQrCodeAct;
import com.deepinto.utils.Constant;
import com.mincat.sample.manager.BaseFragment;
import com.mincat.sample.refresh.layout.api.OnTwoLevelListener;
import com.mincat.sample.refresh.layout.api.RefreshFooter;
import com.mincat.sample.refresh.layout.api.RefreshHeader;
import com.mincat.sample.refresh.layout.api.RefreshLayout;
import com.mincat.sample.refresh.layout.header.TwoLevelHeader;
import com.mincat.sample.refresh.layout.listener.SimpleMultiPurposeListener;
import com.mincat.sample.utils.HiddenStatusBar;
import com.mincat.sample.utils.L;
import com.mincat.sample.utils.TimeUtils;

import java.util.List;

/**
 * @author Ming
 * @Desc 首页
 */
public class MainFra extends BaseFragment {


    private static final String SIGN_HEADER = "header";
    private static final String SING_FOOTER = "footer";

    private View view;
    private AppCompatImageButton mImageBtn;
    private static final int REQUEST_CODE_SCAN = 0x0088;// 扫描二维码
    private TextView mShi, mFen, mMiao;
    private RecyclerView mRecycleView;
    private FraMainListAdapter adapter;

    private TwoLevelHeader header;
    private View floor;
    private RefreshLayout refreshLayout;
    private RelativeLayout mRlTitleBar;
    private ImageView mSecondFloor;
    private ImageButton mBtnCloseTwoFloor;
    private JSONObject mJson;
    private AppCompatImageView mBannerImage;


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            countDown();
            sendEmptyMessageDelayed(0, 1000);
        }
    };


    private void countDown() {

        TimeUtils.countTime(mShi, mFen, mMiao);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fra_main, container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {

        // 隐藏statusBar
        HiddenStatusBar.hiddenStatusBarFragment(this);


        mShi = getId(R.id.tv_miao_sha_shi, view);
        mFen = getId(R.id.tv_miao_sha_minter, view);
        mMiao = getId(R.id.tv_miao_sha_second, view);
        mRecycleView = getId(R.id.list, view);
        mImageBtn = getId(R.id.image_scan, view);
        mSecondFloor = getId(R.id.second_floor_content, view);

        // 关闭七宝二楼
        mBtnCloseTwoFloor = getId(R.id.btn_close_two_floor, view);
        mBtnCloseTwoFloor.setOnClickListener(this);

        header = getId(R.id.header, view);
        floor = getId(R.id.second_floor, view);
        mRlTitleBar = getId(R.id.rl_title_bar, view);
        refreshLayout = getId(R.id.refreshLayout, view);

        mBannerImage = getId(R.id.banner_image, view);

        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {

            /*header*/
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);


            }

            @Override// 下拉刷新,拖拽下拉回调函数
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                super.onHeaderMoving(header, isDragging, percent, offset, headerHeight, maxDragHeight);
                mRlTitleBar.setAlpha(1 - Math.min(percent, 1));
                L.i(TAG, "onHeaderMoving...");
                if (isDragging) {
                    mRlTitleBar.setVisibility(View.VISIBLE);
                }
            }

            @Override// 下拉刷新 刷新完成回调函数
            public void onHeaderFinish(RefreshHeader header, boolean success) {
                super.onHeaderFinish(header, success);

                mRlTitleBar.setVisibility(View.VISIBLE);
            }

            @Override// 下拉刷新,正在刷新回调函数
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                L.i(TAG, "onRefresh...");
                onNetRequest(SIGN_HEADER);


            }

            /*Footer*/
            @Override // 上拉加载更多,开始加载回调函数
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {
                super.onFooterMoving(footer, isDragging, percent, offset, footerHeight, maxDragHeight);

                L.i(TAG, "onFooterMoving...");

                onNetRequest(SING_FOOTER);
            }


            @Override// 上拉加载更多结束加载之后回调函数
            public void onFooterFinish(RefreshFooter footer, boolean success) {
                super.onFooterFinish(footer, success);

                L.i(TAG, "onFooterFinish...");
            }
        });


        handler.sendEmptyMessage(0);
        initAdapter();


        /**
         * 打开七宝二楼
         */
        header.setOnTwoLevelListener(new OnTwoLevelListener() {
            @Override
            public boolean onTwoLevel(@NonNull RefreshLayout refreshLayout) {
                mSecondFloor.animate().alpha(1).setDuration(2000);
                mRlTitleBar.setVisibility(View.GONE);

                return true;//true 将会展开二楼状态 false 关闭刷新
            }
        });


        onNetRequest();


    }

    /**
     * 关闭七宝二楼
     */
    public void closeTwoLevel() {
        mRlTitleBar.setVisibility(View.VISIBLE);
        header.finishTwoLevel();
        mSecondFloor.animate().alpha(0).setDuration(1000);
    }


    /**
     *
     */
    private void onNetRequest() {

        mJson = new JSONObject();

        mJson.put("flag", "true");

        String param = mJson.toJSONString();

        executeVolleyPostRequest(getActivity(), Constant.GET_BANNER_IMAGE, param, Constant.BANNER_IMAGE, false);


    }

    /**
     * 网络请求
     */
    private void onNetRequest(String sign) {

        try {
            refreshLayout.closeHeaderOrFooter();
            if (sign.equals(SIGN_HEADER)) {

                L.i(TAG, "停止刷新:来自下拉刷新");
            } else if (sign.equals(SING_FOOTER)) {


                L.i(TAG, "停止刷新:来自上拉加载更多");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 填充adapter
     */
    private void initAdapter() {
        adapter = new FraMainListAdapter(getActivity());

        mRecycleView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        mRecycleView.setAdapter(adapter);

        mImageBtn.setOnClickListener(this);

//        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()) {
//            @Override
//            public boolean canScrollVertically() {
//                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
//                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
//                return false;
//            }
//        });
        //解决数据加载不完的问题
        mRecycleView.setNestedScrollingEnabled(false);
        mRecycleView.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        mRecycleView.setFocusable(false);
    }

    @Override// 点击事件
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.image_scan:

                intentUtils.openActivityFromRightForResult(getActivity(), ScanQrCodeAct.class, REQUEST_CODE_SCAN);

                break;

            case R.id.btn_close_two_floor:

                closeTwoLevel();
                break;

        }

    }


    @Override
    public void onHandleResponsePost(String response, String sign) {

        if (sign.equals(Constant.BANNER_IMAGE)) {

            GetBannerImageInfo getBannerImageInfo = JSONObject.parseObject(response, GetBannerImageInfo.class);

            if (getBannerImageInfo.getCode().equals("1")) {

                List<ImageInfo> list = getBannerImageInfo.getImageInfo();

                for (ImageInfo info : list) {

                    loadImage(getActivity(), info.getImage(), mBannerImage);


                }
            }

        }

    }

    @Override
    public void errorListener(VolleyError error, String sign) {

    }
}
