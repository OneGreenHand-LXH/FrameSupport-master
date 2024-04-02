package com.frame.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.frame.R;
import com.frame.view.NotificationDialog;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通知的管理类
 * example:
 * //发系统通知
 * NotificationControlManager.getInstance().notify("文件上传完成", "文件上传完成,请点击查看详情", MainActivity.class);
 * //发应用内通知
 * NotificationControlManager.getInstance().showNotificationDialog("文件上传完成", "文件上传完成,请点击查看详情", new NotificationControlManager.OnNotificationCallback() {
 *
 * @Override public void onCallback() {
 * Toast.makeText(mActivity, "被点击了", Toast.LENGTH_SHORT).show();
 * }
 * });
 */
public class NotificationControlManager {
    private static volatile NotificationControlManager sInstance = null;
    private final AtomicInteger autoIncrement = new AtomicInteger(1001);
    private NotificationDialog dialog;

    public interface OnNotificationCallback {
        void onCallback();
    }

    public static NotificationControlManager getInstance() {
        if (sInstance == null) {
            synchronized (NotificationControlManager.class) {
                if (sInstance == null)
                    sInstance = new NotificationControlManager();
            }
        }
        return sInstance;
    }

    /**
     * 是否打开通知
     */
    public boolean isOpenNotification() {
        Activity currentActivity = ForegroundActivityManager.getInstance().getCurrentActivity();
        if (null == currentActivity)
            return false;
        else
            return NotificationManagerCompat.from(currentActivity).areNotificationsEnabled();
    }

    /**
     * 跳转到系统设置页面去打开通知，注意在这之前应该有个Dialog提醒用户
     */
    @RequiresApi(Build.VERSION_CODES.O)
    public void openNotificationInSys() {
        Activity context = ForegroundActivityManager.getInstance().getCurrentActivity();
        if (null == context)
            return;
        Intent intent = new Intent();
        try {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            //8.0及以后版本使用这两个extra.  >=API 26
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);
            //5.0-7.1 使用这两个extra.  <= API 25, >=API 21
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            //其他低版本或者异常情况，走该节点。进入APP设置界面
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.putExtra("package", context.getPackageName());
            //Uri  uri = Uri.fromParts("package", context.getPackageName(), null);
            //intent.setData(uri) ;
            context.startActivity(intent);
        }
    }

    /**
     * 发通知
     *
     * @param title   标题
     * @param content 内容
     * @param cls     通知点击后跳转的Activity
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    public <A extends Activity> void notify(String title, String content, Class<A> cls) {
        Activity context = ForegroundActivityManager.getInstance().getCurrentActivity();
        if (null == context)
            return;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE);
        Notification.Builder builder;
        PendingIntent pendingIntent = null;
        if (null != cls) {
            Intent intent = new Intent(context, cls);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            } else
                pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "NotificationService";
            String description = "通知消息";
            NotificationChannel notificationChannel = new NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            long[] vibrationPattern = {100, 200, 300, 400, 500, 400, 300, 200, 400};
            notificationChannel.setVibrationPattern(vibrationPattern);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new Notification.Builder(context, channelId);
        } else {
            builder = new Notification.Builder(context);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        }
        if (null != pendingIntent)
            builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(content);
        notificationManager.notify(autoIncrement.incrementAndGet(), builder.build());
    }

    /**
     * 显示应用内通知的Dialog,需要自己处理点击事件。listener默认为null,不处理也可以。dialog会在3000毫秒后自动消失
     *
     * @param title    标题
     * @param content  内容
     * @param listener 点击的回调
     */
    public void showNotificationDialog(String title, String content, OnNotificationCallback listener) {
        Activity activity = ForegroundActivityManager.getInstance().getCurrentActivity();
        if (null == activity)
            return;
        dialog = new NotificationDialog(activity);
        dialog.setInfo(title, content);
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {   //子线程
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showDialog(dialog, listener);
                }
            });
        } else
            showDialog(dialog, listener);
    }

    /**
     * show dialog
     */
    public void showDialog(NotificationDialog dialog, OnNotificationCallback listener) {
        if (null == dialog)
            return;
        dialog.showDialogAutoDismiss();
        if (listener != null) {
            dialog.setOnNotificationClickListener(new NotificationDialog.OnNotificationClick() {
                @Override
                public void onViewClick() {
                    listener.onCallback();
                }
            });
        }
    }

    /**
     * dismiss Dialog
     */
    public void dismissDialog() {
        if (null != dialog && dialog.isShowing())
            dialog.dismiss();
    }

}