package com.ogh.support.bean;

import com.frame.bean.BaseBean;

import java.util.List;

public class WenZhangBean extends BaseBean {

    public DataDTO data;

    public static class DataDTO {
        public List<DatasDTO> datas;

        public static class DatasDTO {

            public String title;
        }
    }

    @Override
    public boolean isEmpty() {
        return data.datas.isEmpty();
    }
}