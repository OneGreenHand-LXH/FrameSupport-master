package com.ogh.support.view.activity;

import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frame.base.BaseQuickHolder;
import com.frame.base.activity.BaseSwipeListActivity;
import com.frame.bean.BaseBean;
import com.ogh.support.bean.WenZhangBean;
import com.ogh.support.databinding.LayoutHeadFootExampleBinding;
import com.ogh.support.presenter.RefreshRequestPt;
import com.ogh.support.view.adapter.ExampleAdapter;

/**
 * 上拉刷新和下拉加载
 */
public class RefreshRequestActivity extends BaseSwipeListActivity<LayoutHeadFootExampleBinding, RefreshRequestPt, BaseBean, WenZhangBean.DataDTO.DatasDTO> {

    @Override
    public BaseQuickAdapter<WenZhangBean.DataDTO.DatasDTO, BaseQuickHolder> setAdapter() {
        return new ExampleAdapter();
    }

    @Override
    protected boolean UserAdapterEmpty() {
        return true;
    }

    @Override
    public void loadMoreListRequest(int page) {
        mPresenter.getWenZhangList(page);
    }

    @Override
    protected void onRefreshRequest() {
        mPresenter.getWenZhangList(0);
    }

    @Override
    protected RefreshRequestPt setPresenter() {
        return new RefreshRequestPt(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        viewBinding.titlebar.setTitle("下拉刷新上拉加载示例");
        mPresenter.getWenZhangList(0);
    }

    @Override
    public void requestSuccess(BaseBean data, Object tag, int pageIndex, int pageCount) {
        WenZhangBean wenZhangBean = (WenZhangBean) data;
        if (null == wenZhangBean || wenZhangBean.data == null)
            return;
        notifyAdapterStatus(wenZhangBean.data.datas, pageIndex + 1, pageCount);//因为这个接口第一页从0开始的,不改变框架原本的逻辑,页数手动+1
    }

}
