package com.xpping.windows10.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @ 描述：数据库基础类
 */
class BaseDBBusiness<T extends DBEntity> {
    private final String TAG = "DBBusiness";
    Dao<T, Integer> dao;
    OrmLiteSqliteOpenHelper helper;

    BaseDBBusiness(Context context) {
        helper = OpenHelperManager.getHelper(context, DataHelper.class);
    }

    public void insert(T entity) {
        try {
            dao.create(entity);
        } catch (SQLException ignored) {
        } finally {
            dao.clearObjectCache();
            try {
                dao.closeLastIterator();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void modify(T entity) {
        try {
            dao.update(entity);
        } catch (SQLException e) {
            Log.e(TAG, "修改失败");
        } finally {
            dao.clearObjectCache();
            try {
                dao.closeLastIterator();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public List<T> queryAll() {
        LinkedList<T> list = new LinkedList<>();
        try {
            list = (LinkedList<T>) dao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "查询失败");
        } finally {
            dao.clearObjectCache();
            try {
                dao.closeLastIterator();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public void delete(T entity) {
        try {
            dao.delete(entity);
        } catch (SQLException e) {
            Log.e(TAG, "删除失败");
        } finally {
            dao.clearObjectCache();
            try {
                dao.closeLastIterator();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteById(int id) {
        try {
            dao.deleteById(id);
        } catch (SQLException e) {
            Log.e(TAG, "删除失败");
        } finally {
            dao.clearObjectCache();
            try {
                dao.closeLastIterator();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

//    /**
//     * @ 描述：释放
//     */
//    public void release() {
//        if (helper != null) {
//            helper.close();
//            OpenHelperManager.releaseHelper();
//            helper = null;
//        }
//    }
}
