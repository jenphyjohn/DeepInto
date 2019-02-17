package com.mincat.sample.manager.post;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mincat.sample.imagecache.utils.Constant;
import com.mincat.sample.manager.base.BaseVolleyRequest;
import com.mincat.sample.manager.inter.VolleyPostListener;
import com.mincat.sample.utils.Constants;
import com.mincat.sample.utils.L;
import com.mincat.sample.utils.NetUtils;
import com.mincat.sample.utils.VolleySingle;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael
 * @描述 Volley 执行Post请求 当Activity仅需要Post请求时, 继承此类即可
 */
public abstract class BaseVolleyPost extends BaseVolleyRequest implements VolleyPostListener {

    public static final String TAG = BaseVolleyPost.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setBarColorOrange();
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

                /**
                 * 后台使用Springmvc的关系 此处请求头必须这样设置
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json;charset=utf-8");
                    return headers;

                }

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


}
