package cn.htwinkle.app.kit;

import android.text.TextUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import cn.htwinkle.app.constants.Constants;

public enum DbKit {
    INSTANCE;

    private static final DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName(getDbName())
            //设置数据库路径，默认存储在app的私有目录
            .setDbVersion(1)
            .setAllowTransaction(true)    //设置是否允许事务，默认true
            //设置数据库打开的监听
            .setDbOpenListener(db -> db.getDatabase().enableWriteAheadLogging())
            //设置数据库更新的监听
            .setDbUpgradeListener((db, oldVersion, newVersion) -> {
            })
            //设置表创建的监听
            .setTableCreateListener((db, table) -> {
            });

    public DbManager getDb() throws DbException {
        return x.getDb(DbKit.daoConfig);
    }

    public static String getDbName() {
        return Constants.APP_PACKAGE_NAME + ".db";
    }
}
