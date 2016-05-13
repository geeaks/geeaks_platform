package com.mljr.carfinance.common.constants;

public class Consts {
    //成功状态
    public final static int STATUS_SUCCESS = 0;
    //失败状态
    public final static int STATUS_FAIL = 1;
    //异常状态
    public final static int STATUS_ERROR = 500;
    //需要登录状态
    public final static int STATUS_NEED_LOGIN = 600;

    public final static int DEFAULT_PAGE_SIZE = 10;

    public final static String SYS_USER_TOKEN_PREFIX = "SYS-USER-TOKEN";
    
    /**
     * 缓存在redis中地区信息
     */
    public static final String AREA_KEY_PREFIX = "Area";
    /**
     * 缓存在redis中的城市前缀
     */
    public static final String CITY_KEY_PREFIX = "City";
    /**
     * 缓存在redis中的市场前缀
     */
    public static final String MARKET_KEY_PREFIX = "Market";

    public final static String SUCCESS_MSG = "操作成功";

    public final static String FAIL_MSG = "操作失败";

    public final static String SERVER_ERROR_MSG = "服务器开小差啦";
    public final static String SERVER_PAGE_NOT_FOUND_MSG = "您访问该的页面不存在";
    

}
