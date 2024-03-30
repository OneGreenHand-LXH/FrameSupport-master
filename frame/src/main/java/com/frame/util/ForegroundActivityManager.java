package com.frame.util;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * 前台Activity管理类
 */
public class ForegroundActivityManager {
    private WeakReference<Activity> currentActivityWeakRef;
    private static volatile ForegroundActivityManager instance = null;

    public static ForegroundActivityManager getInstance() {
        if (instance == null) {
            synchronized (ForegroundActivityManager.class) {
                if (instance == null)
                    instance = new ForegroundActivityManager();
            }
        }
        return instance;
    }

    public Activity getCurrentActivity() {
        if (currentActivityWeakRef != null)
            return currentActivityWeakRef.get();
        else return null;
    }

    public void setCurrentActivity(Activity activity) {
        currentActivityWeakRef = new WeakReference<>(activity);
    }
}