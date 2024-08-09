package com.ogh.support.presenter;

import com.frame.base.BaseModel;
import com.frame.base.BasePresenter;
import com.ogh.support.api.API;
import com.ogh.support.bean.WenZhangBean;
import com.ogh.support.view.activity.NoDataExampleActivity;

public class RequestErrorExamplePt extends BasePresenter<NoDataExampleActivity> {
    public RequestErrorExamplePt(NoDataExampleActivity requestErrorExampleActivity) {
        super(requestErrorExampleActivity);
    }

    /**
     * 首页文章列表
     */
    public void getWenZhangList() {
        createRequestBuilder()
                .setLoadStyle(BaseModel.LoadStyle.DIALOG_VIEW)
                .create()
                .get(API.GET_WEN_ZHANG + "/99999/json", WenZhangBean.class);
    }
}
