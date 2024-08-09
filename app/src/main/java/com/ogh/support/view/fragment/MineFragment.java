package com.ogh.support.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.PermissionUtils;
import com.frame.base.fragment.BaseFragment;
import com.frame.util.CustomClickListener;
import com.frame.util.ToastUtil;
import com.ogh.support.databinding.FragmentMineBinding;
import com.ogh.support.util.IntentUtil;
import com.ogh.support.util.PhotoImagePicker;
import com.ogh.support.util.PhotoPickCallback;
import com.ogh.support.view.activity.HeadFootExampleActivity;
import com.ogh.support.view.activity.NoDataExampleActivity;
import com.ogh.support.view.activity.RefreshRequestActivity;

public class MineFragment extends BaseFragment<FragmentMineBinding> {

    @Override
    protected void init(Bundle savedInstanceState) {
        setViewClicked();
    }

    private void setViewClicked() {
        viewBinding.exampleOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.goActivity(mActivity, NoDataExampleActivity.class);
            }
        });
        viewBinding.exampleTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.goActivity(mActivity, HeadFootExampleActivity.class);
            }
        });
        viewBinding.exampleThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.goActivity(mActivity, RefreshRequestActivity.class);
            }
        });
        viewBinding.systemCamera.setOnClickListener(new CustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)) {
                    ToastUtil.showShortToast("缺少权限！");
                    PermissionUtils.launchAppDetailsSettings();
                    return;
                }
                PhotoImagePicker.getInstance().startCamera(MineFragment.this, new PhotoPickCallback() {
                    @Override
                    public void onCanceled() {
                        ToastUtil.showShortToast("用户取消");
                    }

                    @Override
                    public void onPickImage(@Nullable Uri imageUri) {
                        viewBinding.takeResult.setText("拍照的图片路径为：" + imageUri.getPath());
                    }
                });
            }
        });
        viewBinding.systemPhoto.setOnClickListener(new CustomClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ToastUtil.showShortToast("缺少权限！");
                    PermissionUtils.launchAppDetailsSettings();
                    return;
                }
                PhotoImagePicker.getInstance().startGallery(MineFragment.this, new PhotoPickCallback() {
                    @Override
                    public void onCanceled() {
                        ToastUtil.showShortToast("用户取消");
                    }

                    @Override
                    public void onPickImage(@Nullable Uri imageUri) {
                        viewBinding.takeResult.setText("相册选择的图片路径为：" + imageUri.getPath());
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoImagePicker.getInstance().onActivityResult(MineFragment.this, requestCode, resultCode, data);
    }

}