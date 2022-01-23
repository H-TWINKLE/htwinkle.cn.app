package cn.htwinkle.app.listener;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BaseHttpListener implements Callback {
    private HttpListener listener;


    public BaseHttpListener(HttpListener listener) {
        this.listener = listener;
    }

    @Override
    public void onFailure(@NonNull Call call, IOException e) {
        if (listener != null) {
            listener.onError(e);
        }
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        if (listener != null && response.body() != null) {
            String resp = response.body().string();
            if (vailUserTokenExpire(resp)) {
                return;
            }
            listener.onFinish(resp);
            return;
        }
        if (listener != null) {
            listener.onError(new Exception("网络异常 - 获取消息失败"));
        }
    }

    /**
     * 先解析一次是否token已经过期
     */
    private boolean vailUserTokenExpire(String resp) {
        return false;
    }

    public HttpListener getListener() {
        return listener;
    }

    public void setListener(HttpListener listener) {
        this.listener = listener;
    }

    public interface HttpListener {
        void onFinish(String response);

        void onError(Exception e);
    }

}
