package cn.htwinkle.app.constants;

import cn.htwinkle.app.BuildConfig;

public interface Constants {

    String STATUS_OK = "ok";
    String URL = "url";
    String DATA = "data";
    String KV_NAME = "name";

    long CACHE_MAX_TIME = 365L * 24 * 60 * 60 * 1000;

    String APP_PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    String APP_LIST_PACKAGE = APP_PACKAGE_NAME.replace(".debug", "") + ".view.app";

    /**
     * SharedPreferences KEY
     */
    String SHARE_PREFERENCES_KEY = "HtwinkleCnAppData";

    String GLOBAL_DEVICE_NAME = "GLOBAL_DEVICE_NAME";
}
