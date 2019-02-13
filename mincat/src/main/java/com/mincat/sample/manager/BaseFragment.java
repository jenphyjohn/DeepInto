package com.mincat.sample.manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.mincat.sample.custom.CustomProgressDialog;
import com.mincat.sample.manager.inter.InitFra;
import com.mincat.sample.utils.IntentUtils;
import com.mincat.sample.utils.L;

/**
 * @author Michael
 * Fragment基类  基于网络请求
 */
public abstract class BaseFragment extends Fragment implements InitFra {
    public static final String TAG = BaseFragment.class.getName();

    protected IntentUtils intentUtils = IntentUtils.getInstance();

    protected CustomProgressDialog customProgressDialog = CustomProgressDialog.getInstance();
    /**
     * 请求超时时间
     */
    protected static final int REQUEST_TIMEOUT = 15000;

    /**
     * Volley请求队列
     */
    protected RequestQueue mQueue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        L.i(TAG, "BaseFragment onCreateView");

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    /**
     * 查找控件ID
     *
     * @param id   控件id
     * @param view view
     * @param <T>  泛型
     * @return 查找到的空间id值
     */
    protected <T extends View> T getId(int id, View view) {
        try {
            return (T) view.findViewById(id);
        } catch (ClassCastException e) {
            throw e;
        }
    }


    @Override // 当页面销毁的时候回调
    public void onDestroyView() {

        System.gc();
        System.gc();
        System.gc();
        L.i(TAG, "BaseFragment  onDestroyView ");
        super.onDestroyView();
    }

}
