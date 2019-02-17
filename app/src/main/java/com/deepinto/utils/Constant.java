package com.deepinto.utils;

public class Constant {

    public static final String BASE_URL = "http://10.0.2.2:8099";

    public static final String PHONE_T = "android";

    public static final int SUCCESS_CODE = 200;
    public static final int FIALED_CODE_205 = 205;

    /**
     * 用户注册
     */
    public static final String REGISTER_SIGN = "/mall/register";

    public static final String REGISTER_URL = BASE_URL + REGISTER_SIGN;

    /**
     * 用户登录
     */
    public static final String LOGIN_SIGN = "/mall/login";

    public static final String LOGIN_URL = BASE_URL + LOGIN_SIGN;

    /**
     * 验证验证码
     */
    public static final String VERIFY_CODE = "/mall/verifyCode";
    public static final String VERIFY_CODE_URL = BASE_URL + VERIFY_CODE;

    /**
     * 获取轮播图图片
     */
    public static final String BANNER_IMAGE = "/mall/getBannerImage";

    public static final String GET_BANNER_IMAGE = BASE_URL + BANNER_IMAGE;


}
