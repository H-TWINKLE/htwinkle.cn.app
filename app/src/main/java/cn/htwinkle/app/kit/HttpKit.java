package cn.htwinkle.app.kit;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public enum HttpKit {
    /**
     * 单例对象
     */
    INSTANCE;

    private static final String TAG = "HttpKit";

    public static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
            .build();

    /**
     * 同步的调用方法
     *
     * @param <T>         <T>
     * @param url         url
     * @param isNeedToken isNeedToken
     * @param requestBody requestBody
     * @param tClass      tClass
     */
    public <T> T postSync(String url, boolean isNeedToken, RequestBody requestBody, Class<T> tClass) {
        Request.Builder request = new Request.Builder()
                .url(url)
                .post(requestBody);
        if (isNeedToken) {
            // request.addHeader(FROM_MOBILE, UserKit.INSTANCE.getToken());
        }
        try {
            Response response = HttpKit.OK_HTTP_CLIENT.newCall(request.build()).execute();
            String resp = Objects.requireNonNull(response.body()).toString();
            return JSONObject.parseObject(resp, tClass);
        } catch (IOException e) {
            Log.e(TAG, "postSync: " + e.getLocalizedMessage());
        } catch (NullPointerException e) {
            return null;
        }
        return null;
    }

    /**
     * 异步的调用一个方法
     *
     * @param url         url
     * @param isNeedToken isNeedToken
     * @param requestBody requestBody
     * @param callback    callback
     */
    public void postAsync(String url, boolean isNeedToken, RequestBody requestBody, Callback callback) {
        Request.Builder request = new Request.Builder()
                .url(url)
                .post(requestBody);
        if (isNeedToken) {
            // request.addHeader(FROM_MOBILE, UserKit.INSTANCE.getToken());
        }
        HttpKit.OK_HTTP_CLIENT.newCall(request.build()).enqueue(callback);
    }

    /**
     * 异步的调用一个方法
     *
     * @param url         url
     * @param isNeedToken isNeedToken
     * @param requestBody requestBody
     * @param callback    callback
     */
    public void postImageAsync(String url, boolean isNeedToken, RequestBody requestBody, Callback callback) {
        Request.Builder request = new Request.Builder()
                .url(url)
                .header("Content-Type", "multipart/form-data")
                .post(requestBody);
        if (isNeedToken) {
            // request.header("Cookie", "MYZX=" + UserKit.INSTANCE.getToken());
        }
        HttpKit.OK_HTTP_CLIENT.newCall(request.build()).enqueue(callback);
    }

    /**
     * 同步的调用一个方法
     *
     * @param url         url
     * @param isNeedToken isNeedToken
     */
    public <T> T getSync(String url, boolean isNeedToken, Class<T> tClass) {
        Request.Builder request = new Request.Builder()
                .url(url)
                .get();
        if (isNeedToken) {
            // request.addHeader(FROM_MOBILE, UserKit.INSTANCE.getToken());
        }
        try {
            Response response = HttpKit.OK_HTTP_CLIENT.newCall(request.build()).execute();
            String resp = Objects.requireNonNull(response.body()).toString();
            return JSONObject.parseObject(resp, tClass);
        } catch (IOException e) {
            Log.e(TAG, "getSync: " + e.getLocalizedMessage());
        } catch (NullPointerException e) {
            return null;
        }
        return null;
    }

    /**
     * 异步的调用一个方法
     *
     * @param url         url
     * @param isNeedToken isNeedToken
     * @param callback    callback
     */
    public void getAsync(String url, boolean isNeedToken, Callback callback) {
        Request.Builder request = new Request.Builder()
                .url(url)
                .get();
        if (isNeedToken) {
            // request.addHeader(FROM_MOBILE, UserKit.INSTANCE.getToken());
        }
        HttpKit.OK_HTTP_CLIENT.newCall(request.build()).enqueue(callback);
    }

}

