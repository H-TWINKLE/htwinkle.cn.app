package cn.htwinkle.app.wrapper;

import androidx.annotation.NonNull;

import com.vector.update_app.HttpManager;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Map;

public class UpdateAppHttpUtil implements HttpManager {
    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {
        callBack.onResponse("");
    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {

    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull FileCallback callback) {
        x.http().get(new RequestParams(url), new org.xutils.common.Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
                callback.onBefore();
            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                callback.onProgress((float) current / total, total);
            }

            @Override
            public void onSuccess(File result) {
                callback.onResponse(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {

            }
        });
    }
}
