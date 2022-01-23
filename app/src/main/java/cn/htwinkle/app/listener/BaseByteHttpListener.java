package cn.htwinkle.app.listener;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BaseByteHttpListener implements Callback {
    private ByteHttpListener listener;


    public BaseByteHttpListener(ByteHttpListener listener) {
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
            listener.onFinish(response.body().bytes());
            return;
        }
        if (listener != null) {
            listener.onError(new Exception("网络异常 - 获取消息失败"));
        }
    }

    public ByteHttpListener getListener() {
        return listener;
    }

    public void setListener(ByteHttpListener listener) {
        this.listener = listener;
    }

    public interface ByteHttpListener {
        void onFinish(byte[] response);

        void onError(Exception e);
    }
}
