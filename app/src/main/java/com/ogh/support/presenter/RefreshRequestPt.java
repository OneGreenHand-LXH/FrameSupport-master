package com.ogh.support.presenter;

import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;
import com.ogh.support.api.API;
import com.ogh.support.bean.WenZhangBean;
import com.ogh.support.view.activity.RefreshRequestActivity;

public class RefreshRequestPt extends BasePresenter<RefreshRequestActivity> {
    public RefreshRequestPt(RefreshRequestActivity refreshRequestActivity) {
        super(refreshRequestActivity);
    }

    /**
     * 首页文章列表
     */
    public void getWenZhangList(int page) {
        createRequestBuilder()
                .setLoadStyle(page == 0 ? BaseModel.LoadStyle.DIALOG : BaseModel.LoadStyle.NONE)
                .setPageIndex(page)
                .setPageCount(10)//改为每页10条，发现有时候没那么多数据,导致无法分页
                .create()
                .get(API.GET_WEN_ZHANG + "/" + page + "/json?page_size=10", WenZhangBean.class);
    }
}