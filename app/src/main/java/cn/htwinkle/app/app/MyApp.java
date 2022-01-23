package cn.htwinkle.app.app;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.xutils.x;


public class MyApp extends Application {

    private static final String TAG = "CoreApp";

    /**
     * 全局的Context
     */
    @SuppressLint("StaticFieldLeak")
    private static Context CTX;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        CTX = base;
        Log.i(TAG, "attachBaseContext: 初始化APP");
        x.Ext.init(this);
        x.Ext.setDebug(false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CTX = getApplicationContext();
    }

    /**
     * 获取全局对象的AppContext
     *
     * @return Context
     */
    public static Context getCTX() {
        return CTX;
    }
}
