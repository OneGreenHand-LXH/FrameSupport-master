package com.ogh.support.util;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

/**
 * 调用系统拍照、系统相册
 * 使用前请自行进行权限申请
 * 拍照图片会保存在/storage/emulated/0/Android/data/<应用包名>/files/Pictures
 */
public class PhotoImagePicker {
    private static volatile PhotoImagePicker instance = null;
    private PhotoPickCallback callback;
    private static final int PICK_IMAGE_CHOOSER_REQUEST_CODE = 200;
    private Uri outputFileUri;//拍照输出的uri

    public static PhotoImagePicker getInstance() {
        if (instance == null) {
            synchronized (PhotoImagePicker.class) {
                if (instance == null)
                    instance = new PhotoImagePicker();
            }
        }
        return instance;
    }

    /**
     * 启动照相机(Activity)
     */
    public void startCamera(Activity activity, PhotoPickCallback callback) {
        this.callback = callback;
        outputFileUri = Uri.fromFile(new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg"));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getIntentUri(activity, outputFileUri));
        activity.startActivityForResult(intent, PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    /**
     * 启动照相机(Fragment)
     */
    public void startCamera(Fragment fragment, PhotoPickCallback callback) {
        this.callback = callback;
        outputFileUri = Uri.fromFile(new File(fragment.requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg"));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getIntentUri(fragment.requireContext(), outputFileUri));
        fragment.startActivityForResult(intent, PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    /**
     * 启动图库选择器(Activity)
     */
    public void startGallery(Activity activity, PhotoPickCallback callback) {
        this.callback = callback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(intent, PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    /**
     * 启动图库选择器(Fragment)
     */
    public void startGallery(Fragment fragment, PhotoPickCallback callback) {
        this.callback = callback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        fragment.startActivityForResult(intent, PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    /**
     * 启动文件选择器(Activity)
     */
    public void startChooser(Activity activity, String mime, PhotoPickCallback callback) {
        this.callback = callback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mime);
        activity.startActivityForResult(intent, PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    /**
     * 启动文件选择器(Fragment)
     */
    public void startChooser(Fragment fragment, String mime, PhotoPickCallback callback) {
        this.callback = callback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mime);
        fragment.startActivityForResult(intent, PICK_IMAGE_CHOOSER_REQUEST_CODE);
    }

    /**
     * 图片选择结果回调，在 {@link Activity#onActivityResult(int, int, Intent)} 中调用
     */
    @SuppressWarnings("JavadocReference")
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        onActivityResultInner(activity, null, requestCode, resultCode, data);
    }

    /**
     * 图片选择结果回调，在 {@link Fragment#onActivityResult(int, int, Intent)} 中调用
     */
    public void onActivityResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
        onActivityResultInner(null, fragment, requestCode, resultCode, data);
    }

    private void onActivityResultInner(Activity activity, Fragment fragment, int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            if (callback != null)
                callback.onCanceled();
            return;
        }
        Context context;
        if (activity != null) {
            context = activity;
        } else
            context = fragment.getContext();
        if (context != null && requestCode == PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            boolean isCamera = true;
            if (data != null && data.getData() != null) {
                String action = data.getAction();
                isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
            }
            Uri pickImageUri = isCamera || data.getData() == null ? outputFileUri : data.getData();
            handlePickImage(context, pickImageUri);
        }
    }

    /**
     * 选择图片结果回调
     */
    private void handlePickImage(Context context, Uri imageUri) {
        if (callback != null)
            callback.onPickImage(handleUri(context, imageUri));
        outputFileUri = null;
    }

    /**
     * 兼容 Android N，Intent中不能使用 file:///*
     */
    private Uri getIntentUri(Context context, @NonNull Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileProvider", new File(Objects.requireNonNull(uri.getPath())));
        } else
            return uri;
    }

    /**
     * 处理返回图片的 uri，content 协议自动转换 file 协议，避免 {@link FileNotFoundException}
     */
    private Uri handleUri(Context context, Uri uri) {
        String realPath = "";
        if (DocumentsContract.isDocumentUri(context, uri)) {//如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                realPath = getRealPathFromUri(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                realPath = getRealPathFromUri(context, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {//如果是content类型的Uri，则使用普通方式处理
            realPath = getRealPathFromUri(context, uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme()))  //如果是file类型的Uri，直接获取图片路径即可
            realPath = uri.getPath();
        if (!TextUtils.isEmpty(realPath))
            return Uri.fromFile(new File(realPath));
        else
            return uri;
    }

    /**
     * 获取文件的真实路径，比如：content://media/external/images/media/74275 的真实路径 file:///storage/sdcard0/Pictures/X.jpg
     * http://stackoverflow.com/questions/20028319/how-to-convert-content-media-external-images-media-y-to-file-storage-sdc
     */
    private String getRealPathFromUri(Context context, Uri uri, String selection) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, selection, null, null);
            if (cursor == null)
                return "";
            if (cursor.moveToFirst())
                return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            return "";
        } catch (IllegalStateException e) {
            return e.getMessage();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }
}