package com.ogh.support.greendao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * 课程类
 */
//指定表名名称,不自动生成getter/setter
@Entity(nameInDb = "CourseDb")
public class Course {
    //声明自增长的id
    @Id(autoincrement = true)
    public Long cId;
    public String cName;
    public String cRemark;
    //一对多
    @NotNull
    public Long studentId;//外键,用于与Student类关联,对应Student类的主键
    @ToOne(joinProperty = "studentId")// 注意该参数的值
    public Student student;
    //下面都是自动生成的
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 2063667503)
    private transient CourseDao myDao;

    @Generated(hash = 1081364214)
    public Course(Long cId, String cName, String cRemark, @NotNull Long studentId) {
        this.cId = cId;
        this.cName = cName;
        this.cRemark = cRemark;
        this.studentId = studentId;
    }

    @Generated(hash = 1355838961)
    public Course() {
    }

    public Long getCId() {
        return this.cId;
    }

    public void setCId(Long cId) {
        this.cId = cId;
    }

    public String getCName() {
        return this.cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public String getCRemark() {
        return this.cRemark;
    }

    public void setCRemark(String cRemark) {
        this.cRemark = cRemark;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @Generated(hash = 79695740)
    private transient Long student__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 313494093)
    public Student getStudent() {
        Long __key = this.studentId;
        if (student__resolvedKey == null || !student__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StudentDao targetDao = daoSession.getStudentDao();
            Student studentNew = targetDao.load(__key);
            synchronized (this) {
                student = studentNew;
                student__resolvedKey = __key;
            }
        }
        return student;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 209059263)
    public void setStudent(@NotNull Student student) {
        if (student == null) {
            throw new DaoException(
                    "To-one property 'studentId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.student = student;
            studentId = student.getSId();
            student__resolvedKey = studentId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 94420068)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCourseDao() : null;
    }
}