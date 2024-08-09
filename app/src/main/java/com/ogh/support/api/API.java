package com.ogh.support.api;


import com.frame.config.BaseConfig;

public class API {
    private static final String BASE_URL = BaseConfig.getUrl();//接口统一请求地址
    public final static String GET_WEN_ZHANG = BASE_URL + "article/list";//首页文章列表
}