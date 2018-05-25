package com.xpping.windows10.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;


public class MeCacheBusiness extends BaseDBBusiness<LocalCache> {
    private static MeCacheBusiness instance;

    private MeCacheBusiness(Context context) {
        super(context);
        try {
            dao = helper.getDao(LocalCache.class);//实体操作类
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static MeCacheBusiness getInstance(Context context) {
        if (instance == null) {
            instance = new MeCacheBusiness(context);
        }
        return instance;
    }


    //有就更新 没有就插入
    public void createOrUpdate(LocalCache nc) {
        try {
            Log.i("other", nc.getResult());
            LocalCache cache = queryBykey(nc.getKey());
            if (cache != null) {
                nc.setId(cache.getId());
            }
            dao.createOrUpdate(nc);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dao.clearObjectCache();
            try {
                dao.closeLastIterator();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //重复修改的该条数据，关闭后修改失败
    public void createOrUpdateNoClose(LocalCache nc) {
        try {
            Log.i("other", nc.getResult());
            LocalCache cache = queryBykey(nc.getKey());
            if (cache != null) {
                nc.setId(cache.getId());
            }
            dao.createOrUpdate(nc);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dao.clearObjectCache();
            //            try {
            //                dao.closeLastIterator();
            //            } catch (SQLException e) {
            //                e.printStackTrace();
            //            }
        }
    }

    public LocalCache queryBykey(String key) {
        QueryBuilder<LocalCache, Integer> qb = dao.queryBuilder();
        List<LocalCache> list;
        try {
            qb.where().eq("key", key);
            list = dao.query(qb.prepare());
            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dao.clearObjectCache();
            try {
                dao.closeLastIterator();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
