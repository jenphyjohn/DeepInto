package com.deepinto.fra;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.deepinto.R;
import com.mincat.sample.manager.BaseFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Ming
 * @Desc 首页
 */
public class MainFra extends BaseFragment {

    private View view;
    private AppCompatImageButton mImageBtn;
    private static final int REQUEST_CODE_SCAN = 0x0088;// 扫描二维码
    private TextView mShi, mFen, mMiao;


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            countDown();
            sendEmptyMessageDelayed(0, 1000);
        }
    };


    private void countDown() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String format = df.format(curDate);

        StringBuffer buffer = new StringBuffer();
        String substring = format.substring(0, 11);
        buffer.append(substring);


        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour % 2 == 0) {
            buffer.append((hour + 2));
            buffer.append(":00:00");
        } else {
            buffer.append((hour + 1));
            buffer.append(":00:00");
        }

        String totalTime = buffer.toString();
        try {
            java.util.Date date = df.parse(totalTime);
            java.util.Date date1 = df.parse(format);
            long defferenttime = date.getTime() - date1.getTime();
            long days = defferenttime / (1000 * 60 * 60 * 24);
            long hours = (defferenttime - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minute = (defferenttime - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            long seconds = defferenttime % 60000;
            long second = Math.round((float) seconds / 1000);
            mShi.setText("0" + hours + "");
            if (minute >= 10) {
                mFen.setText(minute + "");
            } else {
                mFen.setText("0" + minute + "");
            }
            if (second >= 10) {
                mMiao.setText(second + "");
            } else {
                mMiao.setText("0" + second + "");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fra_main, container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {

        mShi = getId(R.id.tv_miao_sha_shi, view);
        mFen = getId(R.id.tv_miao_sha_minter, view);
        mMiao = getId(R.id.tv_miao_sha_second, view);

        handler.sendEmptyMessage(0);

    }

    @Override// 点击事件
    public void onClick(View v) {

        switch (v.getId()) {

        }

    }

    @Override// 网络请求,请求成功回调函数
    public void onHandleResponse(String response, String sign) {

    }

    @Override// 网络请求,请求失败回调函数
    public void errorListener(VolleyError error, String sign) {

    }

}
