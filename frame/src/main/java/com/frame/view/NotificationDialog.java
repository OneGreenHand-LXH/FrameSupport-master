package com.frame.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.frame.R;

/**
 * 通知的自定义Dialog
 */
public class NotificationDialog extends Dialog implements LifecycleObserver {
    private Context mContext;
    private float mStartY;
    private View mView;
    private int mHeight;
    //控件
    private TextView tvTitle;
    private TextView tvContent;
    //
    private OnNotificationClick mListener;

    public void setOnNotificationClickListener(OnNotificationClick listener) {
        mListener = listener;
    }

    public interface OnNotificationClick {
        void onViewClick();
    }

    public NotificationDialog(@NonNull Context context) {
        super(context, R.style.DialogNotificationTop);
        initCommon(context);
    }

    @SuppressLint("InflateParams")
    private void initCommon(Context context) {
        mContext = context;
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            if (!activity.isFinishing() && !activity.isDestroyed())//注册绑定生命周期
                activity.getLifecycle().addObserver(this);
        }
        mView = LayoutInflater.from(context).inflate(R.layout.common_layout_notifacation, null);
        setCanceledOnTouchOutside(false);
        setContentView(mView);
        Window window = getWindow(); //必须在setContentView()之后
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            attributes.gravity = Gravity.TOP;
            attributes.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            window.setAttributes(attributes);
            window.setWindowAnimations(R.style.DialogNotificationAnimation);
        }
        initView();
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setContent(String content) {
        tvContent.setText(content);
    }

    public void setInfo(String title, String content) {
        tvTitle.setText(title);
        tvContent.setText(content);
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isOutOfBounds(event))
                    mStartY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (mStartY > 0) {
                    float moveY = event.getY();
                    if (mStartY - moveY >= getViewHalfHeight()) {//滑动超过控件的一半认定为滑动事件
                        dismiss();
                    } else if (mStartY == moveY && isOutOfBounds(event)) {//认定为点击事件
                        if (null != mListener)
                            mListener.onViewClick();
                        dismiss();
                    }
                }
                break;
        }
        return false;
    }

    /**
     * 点击是否在范围外
     */
    private boolean isOutOfBounds(MotionEvent event) {
        float yValue = event.getY();
        return yValue > 0 && yValue <= (mHeight == 0 ? 50 : mHeight);
    }

    /**
     * 获取控件一半的高度
     * 没有就默认50
     */
    private int getViewHalfHeight() {
        return mHeight == 0 ? 50 : (mHeight / 2);
    }

    private void setDialogSize() {
        mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mHeight = null == v ? 50 : v.getHeight();
            }
        });
    }

    /**
     * 显示Dialog但是不会自动退出
     */
    public void showDialog() {
        if (!isShowing()) {
            show();
            setDialogSize();
        }
    }

    /**
     * 显示Dialog,3000毫秒后自动退出
     */
    public void showDialogAutoDismiss() {
        if (!isShowing()) {
            show();
            setDialogSize();
            //延迟3000毫秒后自动消失
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isShowing())
                        dismiss();
                }
            }, 3000);
        }
    }

    @Override
    public void show() {
        if (mContext instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) mContext;
            if (activity.isFinishing() || activity.isDestroyed())
                return;
        }
        super.show();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (isShowing())
            dismiss();
    }

}