package cn.htwinkle.app.constants;

import android.os.Build;

import cn.htwinkle.app.BuildConfig;

public interface Constants {

    String STATUS_OK = "ok";
    String URL = "url";
    String DATA = "data";
    String KV_NAME = "name";

    long CACHE_MAX_TIME = 365L * 24 * 60 * 60 * 1000;

    /**
     * 手机的品牌
     */
    String MOBILE_DEVICE_BRAND = Build.MODEL;

    String APP_PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    String APP_LIST_PACKAGE = APP_PACKAGE_NAME.replace(".debug", "") + ".view.app";

    /**
     * SharedPreferences KEY
     */
    String SHARE_PREFERENCES_KEY = "HtwinkleCnAppData";

    String GLOBAL_DEVICE_NAME = "GLOBAL_DEVICE_NAME";

    String SHARE_VIEW = "SHARE_VIEW";

    String VIEW_QUIL = "VIEW_QUIL";

    String WEB_VIEW_TITLE = "WEB_VIEW_TITLE";
    String WEB_VIEW_HOME = "WEB_VIEW_HOME";
    String WEB_VIEW_SRS_AUTH = "WEB_VIEW_SRS_AUTH";

    int DEFAULT_QUIL = 75;
}
