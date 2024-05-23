package com.ogh.support.greendao;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * 增删改查语句
 * https://www.jianshu.com/p/8f20fb1ee04a
 * https://www.jianshu.com/p/aa4e172f7d47
 */
public class CommonDaoUtils<T> {
    private final DaoSession mDaoSession;
    private final Class<T> entityClass;

    public CommonDaoUtils(Class<T> pEntityClass) {
        mDaoSession = DaoManager.getInstance().getDaoSession();
        entityClass = pEntityClass;
    }

    /**
     * 插入记录，如果表未创建，先创建表
     */
    public boolean insert(T pEntity) {
        return mDaoSession.insert(pEntity) != -1;
    }

    /**
     * 插入多条数据，在子线程操作
     * 若此实体类存在，则覆盖
     */
    public boolean insertMulti(final List<T> pEntityList) {
        try {
            mDaoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T entity : pEntityList)
                        mDaoSession.insertOrReplace(entity);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改一条数据
     * 直接更新会导致其他字段置空,建议先查询,再更新
     */
    public boolean update(T pEntity) {
        try {
            mDaoSession.update(pEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除单条记录
     */
    public boolean delete(T pEntity) {
        try {
            mDaoSession.delete(pEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除所有记录
     */
    public boolean deleteAll() {
        try {
            mDaoSession.deleteAll(entityClass);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据条件删除所有
     */
    public void deleteAllByWhere(WhereCondition cond, WhereCondition... condMore) {
        QueryBuilder<T> queryBuilder = mDaoSession.queryBuilder(entityClass);
        queryBuilder.where(cond, condMore);
        DeleteQuery<T> deleteQuery = queryBuilder.buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    /**
     * 查询所有记录
     */
    public List<T> queryAll() {
        return mDaoSession.loadAll(entityClass);
    }

    /**
     * 根据主键id查询记录
     */
    public T queryById(long key) {
        return mDaoSession.load(entityClass, key);
    }

    /**
     * 根据条件查询最后一条
     * 降序
     */
    public T queryOrderDesc(Property properties, WhereCondition cond, WhereCondition... condMore) {
        QueryBuilder<T> queryBuilder = mDaoSession.queryBuilder(entityClass);
        queryBuilder.where(cond, condMore);
        queryBuilder.orderDesc(properties);
        queryBuilder.limit(1);
        return queryBuilder.unique();
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<T> queryByNativeSql(String sql, String[] conditions) {
        return mDaoSession.queryRaw(entityClass, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     */
    public List<T> queryByQueryBuilder(WhereCondition cond, WhereCondition... condMore) {
        QueryBuilder<T> queryBuilder = mDaoSession.queryBuilder(entityClass);
        return queryBuilder.where(cond, condMore).list();
    }

    /**
     * 分页加载
     * 一页10条
     */
    public List<T> queryByQueryBuilder(int currentPage, WhereCondition cond, WhereCondition... condMore) {
        QueryBuilder<T> queryBuilder = mDaoSession.queryBuilder(entityClass);
        return queryBuilder.where(cond, condMore).offset(currentPage * 10).limit(10).list();
    }
}