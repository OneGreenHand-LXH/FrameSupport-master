package com.ogh.support.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.ogh.support.greendao.Course;
import com.ogh.support.greendao.Student;

import com.ogh.support.greendao.CourseDao;
import com.ogh.support.greendao.StudentDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig courseDaoConfig;
    private final DaoConfig studentDaoConfig;

    private final CourseDao courseDao;
    private final StudentDao studentDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        courseDaoConfig = daoConfigMap.get(CourseDao.class).clone();
        courseDaoConfig.initIdentityScope(type);

        studentDaoConfig = daoConfigMap.get(StudentDao.class).clone();
        studentDaoConfig.initIdentityScope(type);

        courseDao = new CourseDao(courseDaoConfig, this);
        studentDao = new StudentDao(studentDaoConfig, this);

        registerDao(Course.class, courseDao);
        registerDao(Student.class, studentDao);
    }
    
    public void clear() {
        courseDaoConfig.clearIdentityScope();
        studentDaoConfig.clearIdentityScope();
    }

    public CourseDao getCourseDao() {
        return courseDao;
    }

    public StudentDao getStudentDao() {
        return studentDao;
    }

}
