package com.ogh.support.greendao;

import android.annotation.SuppressLint;

import com.frame.config.BaseConfig;
import com.ogh.support.AppContext;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * 创建数据库、创建数据库表、包含增删改查的操作以及数据库的升级
 * https://www.jianshu.com/p/39db996be365
 * https://www.jianshu.com/p/a370be2ace1d
 * 相关升级教程
 * https://www.jianshu.com/p/aa3b27c0949c
 * https://www.jianshu.com/p/ae9c97ca6afd
 */
public class DaoManager {
    //数据库名字
    private static final String DB_NAME = "FrameSupport.db";
    //多线程中要被共享的使用volatile关键字修饰
    @SuppressLint("StaticFieldLeak")
    private static volatile DaoManager manager = null;
    @SuppressLint("StaticFieldLeak")
    private static DaoMaster sDaoMaster;
    @SuppressLint("StaticFieldLeak")
    private static DaoMaster.DevOpenHelper sHelper;
    private static DaoSession sDaoSession;

    public static DaoManager getInstance() {
        if (manager == null) {
            synchronized (DaoManager.class) {
                if (manager == null)
                    manager = new DaoManager();
            }
        }
        return manager;
    }

    private DaoManager() {
        //输出日志，默认关闭
        QueryBuilder.LOG_SQL = BaseConfig.DEBUG;
        QueryBuilder.LOG_VALUES = BaseConfig.DEBUG;
    }

    /**
     * 判断是否有存在数据库，如果没有则创建
     */
    public DaoMaster getDaoMaster() {
        if (sDaoMaster == null) {
            sHelper = new DaoMaster.DevOpenHelper(AppContext.getContext(), DB_NAME, null);
            sDaoMaster = new DaoMaster(sHelper.getWritableDatabase());
        }
        return sDaoMaster;
    }

    /**
     * 完成对数据库的添加、删除、修改、查询操作，仅仅是一个接口
     */
    public DaoSession getDaoSession() {
        if (sDaoSession == null) {
            synchronized (DaoSession.class) {
                if (sDaoMaster == null)
                    sDaoMaster = getDaoMaster();
                sDaoSession = sDaoMaster.newSession();
            }
        }
        return sDaoSession;
    }

    /**
     * 关闭所有的操作，数据库开启后，使用完毕要关闭
     */
    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }

    public void closeHelper() {
        if (sHelper != null) {
            sHelper.close();
            sHelper = null;
        }
    }

    public void closeDaoSession() {
        if (sDaoSession != null) {
            sDaoSession.clear();
            sDaoSession = null;
        }
    }
}
