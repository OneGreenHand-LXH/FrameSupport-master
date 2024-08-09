package com.ogh.support.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.frame.base.BaseQuickHolder;
import com.ogh.support.R;
import com.ogh.support.bean.WenZhangBean;

public class ExampleAdapter extends BaseQuickAdapter<WenZhangBean.DataDTO.DatasDTO, BaseQuickHolder> implements LoadMoreModule {

    public ExampleAdapter() {
        super(R.layout.item_common);
    }

    @Override
    protected void convert(BaseQuickHolder helper, WenZhangBean.DataDTO.DatasDTO item) {
        helper.setText(R.id.item_content, item.title);
    }
}