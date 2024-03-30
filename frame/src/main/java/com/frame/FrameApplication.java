package com.frame;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.frame.util.AppLifecycleCallback;

/**
 * description: 基础框架Application
 */
public class FrameApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        registerActivityLifecycleCallbacks(new AppLifecycleCallback());//注册Activity生命周期
    }
}