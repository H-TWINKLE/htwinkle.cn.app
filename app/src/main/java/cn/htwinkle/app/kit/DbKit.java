package cn.htwinkle.app.kit;

import android.content.Context;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import cn.htwinkle.app.app.MyApp;
import cn.htwinkle.app.constants.Constants;

public enum DbKit {
    INSTANCE;
    private static final String TAG = "DbKit";

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
        DbManager db = x.getDb(DbKit.daoConfig);
        db.getDaoConfig().setDbName(getDbName());
        return db;
    }

    public static String getDbName() {
        String name = Constants.APP_PACKAGE_NAME + "_" + getKey() + ".db";
        Log.i(TAG, "getDbName: " + name);
        return name;
    }

    private static String getKey() {
        Context ctx = MyApp.getCTX();
        if (ctx == null) {
            return "";
        }
        return PhoneKit.INSTANCE.getDeviceId(ctx);
    }
}
