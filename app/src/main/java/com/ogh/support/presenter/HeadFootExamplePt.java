package com.ogh.support.presenter;

import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;
import com.ogh.support.api.API;
import com.ogh.support.bean.WenZhangBean;
import com.ogh.support.view.activity.HeadFootExampleActivity;

public class HeadFootExamplePt extends BasePresenter<HeadFootExampleActivity> {
    public HeadFootExamplePt(HeadFootExampleActivity headFootExampleActivity) {
        super(headFootExampleActivity);
    }

    /**
     * 首页文章列表
     */
    public void getWenZhangList() {
        createRequestBuilder()
                .setLoadStyle(BaseModel.LoadStyle.DIALOG)
                .create()
                .get(API.GET_WEN_ZHANG + "/0/json", WenZhangBean.class);
    }
}