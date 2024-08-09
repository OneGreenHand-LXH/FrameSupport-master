package com.frame.bean;

public class BaseBean {
    public int errorCode;
    public String errorMsg;

    //判断数据是否为空(详情请看BaseModel)
    public boolean isEmpty() {
        return false;
    }
}
