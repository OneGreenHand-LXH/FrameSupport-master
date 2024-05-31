package com.ogh.support.util;

import android.net.Uri;

import androidx.annotation.Nullable;


public interface PhotoPickCallback {

    /**
     * 用户取消回调
     */
    void onCanceled();

    /**
     * 图片返回回调
     */
    void onPickImage(@Nullable Uri imageUri);
}