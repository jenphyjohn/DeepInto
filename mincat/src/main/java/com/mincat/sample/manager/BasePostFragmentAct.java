package com.mincat.sample.manager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mincat.sample.R;
import com.mincat.sample.manager.inter.InitUi;
import com.mincat.sample.manager.inter.VolleyPostListener;
import com.mincat.sample.manager.post.BaseVolleyPost;
import com.mincat.sample.utils.Constants;
import com.mincat.sample.utils.L;
import com.mincat.sample.utils.NetUtils;
import com.mincat.sample.utils.SpUtils;
import com.mincat.sample.utils.StatusBarUtil;
import com.mincat.sample.utils.T;
import com.mincat.sample.utils.VolleySingle;

/**
 * @author Ming
 * @描述 封装一个类继承自FragmentActivity
 */
public abstract class BasePostFragmentAct extends FragmentActivity implements InitUi,VolleyPostListener {

    public static final String TAG = BaseVolleyPost.class.getName();
    // Volley请求队列
    protected RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    protected void showNetConnectionErrorToast(Context context) {
        T.showLong(context, getString(R.string.http_is_null));
    }

    /**
     * @param context   上下文对象
     * @param sign      标记
     * @param url       请求的URL地址
     * @param param     参数
     * @param hasDialog 是否有进度条
     * @describe 执行网络请求 POST方法
     */
    protected void executeVolleyPostRequest(
            final Context context,
            String url,
            final String param,
            String sign,
            boolean hasDialog
    ) {

        if (NetUtils.checkNet(context)) {
            if (hasDialog) {
                // 此处加载请求进度条

            }
            mQueue = Volley.newRequestQueue(context);

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    requestListenerPost(sign, hasDialog),
                    errorListenerPost(sign, hasDialog)) {


                @Override
                public byte[] getBody() throws AuthFailureError {
                    return param.getBytes();
                }


                public RetryPolicy getRetryPolicy() {
                    return new
                            DefaultRetryPolicy(Constants.REQUEST_TIMEOUT,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                }

            };
            // 此处对Volley请求队列做单利模式处理
            VolleySingle.getVolleySingle(context.getApplicationContext()).addToRequestQueue(request);


        } else {

            showNetConnectionErrorToast(context);
        }
    }

    /**
     * @param sign      请求标记
     * @param hasDialog 是否有进度条
     * @return
     * @describe Volley 请求成功调用此方法
     */
    public Response.Listener<String> requestListenerPost(final String sign, final boolean hasDialog) {
        return new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (hasDialog) {
                    // 在此处关闭进度条

                }
                L.i(TAG, "Volley Post 请求成功,返回参数:" + response);
                onHandleResponsePost(response, sign);
            }
        };
    }

    /**
     * @param sign      请求标记
     * @param hasDialog 是否有进度条
     * @return
     * @描述 Volley 请求失败调用此方法
     */
    public Response.ErrorListener errorListenerPost(final String sign, final boolean hasDialog) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (hasDialog) {
                    // 如果包含请求进度条 在此处关闭进度条

                }
                L.i(TAG, "Volley Post 请求失败,错误信息:" + error);
                errorListener(error, sign);
            }


        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }


    protected Object parseResult(String result, Class<?> cls) {

        return JSONObject.parseObject(result, cls);


    }

    protected boolean isLogin() {

        return SpUtils.getLoginInfo(this);
    }

    /**
     * 设置statusBar 背景色为白色
     */
    @TargetApi(19)
    protected void setBarColorWhite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.white));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置statusBar 背景色为橙色
     */
    @TargetApi(19)
    protected void setBarColorOrange() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.main_orange));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置statusBar 背景色为红色
     */
    @TargetApi(19)
    protected void setBarColorRed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.md_red_500));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置statusBar 背景色为透明
     */
    @TargetApi(19)
    protected void setBarColorTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.transparent));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置 statusBar字体颜色值为黑色
     */
    protected void setBarTitleBlack() {
        // 设置Status字体颜色值
        StatusBarUtil.setStatusTextColor(true, this);
    }
    // 获View 上的文本
    protected String etString(EditText et) {

        String str = et.getText().toString().trim();

        return str;
    }

    // 获取控件ID
    protected <T extends View> T getId(int id) {
        try {
            return (T) findViewById(id);
        } catch (ClassCastException e) {
            throw e;
        }
    }





}
