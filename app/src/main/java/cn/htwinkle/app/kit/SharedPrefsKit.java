package cn.htwinkle.app.kit;

import android.content.Context;
import android.content.SharedPreferences;

import cn.htwinkle.app.constants.Constants;

public enum SharedPrefsKit {

    INSTANCE;

    /**
     * 获取SharedPreferences对象
     *
     * @param context context
     * @return SharedPreferences
     */
    public SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Constants.SHARE_PREFERENCES_KEY,
                Context.MODE_PRIVATE);
    }

    /**
     * 获取一个key
     *
     * @param context context
     * @param key     key
     * @return String
     */
    public Integer getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * 获取一个key
     *
     * @param context context
     * @param key     key
     * @return String
     */
    public String getStr(Context context, String key) {
        return getStr(context, key, "");
    }

    public String getStr(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(key, defaultValue);
    }


    /**
     * 保存一个key到SharedPreferences
     *
     * @param context context
     * @param key     key
     * @param value   value
     */
    public void saveValue(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 保存一个key到SharedPreferences
     *
     * @param context context
     * @param key     key
     * @param value   value
     */
    public void saveValue(Context context, String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 移除一个key到SharedPreferences
     *
     * @param context context
     * @param key     key
     */
    public void removeKey(Context context, String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 获取默认的SharedPreferences对象
     *
     * @param context context
     * @return SharedPreferences.Editor
     */
    public SharedPreferences.Editor getSharePreferencesEdit(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.edit();
    }


}
