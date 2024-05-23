package com.ogh.support.greendao;

/**
 * 存放DaoUtils
 * 操作数据库用这个
 */
public class DaoUtilsStore {
    private volatile static DaoUtilsStore instance = null;
    private final CommonDaoUtils<Student> studentDaoUtils;
    private final CommonDaoUtils<Course> courseDaoUtils;

    public static DaoUtilsStore getInstance() {
        if (instance == null) {
            synchronized (DaoUtilsStore.class) {
                instance = new DaoUtilsStore();
            }
        }
        return instance;
    }

    private DaoUtilsStore() {
        studentDaoUtils = new CommonDaoUtils<>(Student.class);
        courseDaoUtils = new CommonDaoUtils<>(Course.class);
    }

    public CommonDaoUtils<Student> getStudentDaoUtils() {
        return studentDaoUtils;
    }

    public CommonDaoUtils<Course> getCourseDaoUtils() {
        return courseDaoUtils;
    }
}
