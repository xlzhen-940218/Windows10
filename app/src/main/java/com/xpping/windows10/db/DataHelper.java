package com.xpping.windows10.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DataHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "Message.db";
    private static final int DATABASE_VERSION = 2;

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
        try {
            TableUtils.createTable(arg1, LocalCache.class);
        } catch (SQLException e) {
            Log.e(DataHelper.class.getName(), "创建数据库失败", e);
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource arg1, int arg2, int arg3) {
//        try {
//            TableUtils.dropTable(arg1, ImageIndex.class, true);
//            TableUtils.createTable(arg1, ImageIndex.class);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void close() {
        super.close();
    }
}
