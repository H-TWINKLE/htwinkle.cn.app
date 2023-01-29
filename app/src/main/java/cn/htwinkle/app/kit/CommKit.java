package cn.htwinkle.app.kit;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import cn.hutool.core.lang.func.VoidFunc0;
import cn.hutool.core.thread.ThreadUtil;

public class CommKit {
    public static final ExecutorService POOL_EXECUTOR = ThreadUtil.newSingleExecutor();

    private static final String TAG = "CommKit";

    public static <R> R safety(Supplier<R> function) {
        return safety(function, true, null);
    }

    public static <R> R safety(Supplier<R> function, boolean ignoreError) {
        return safety(function, ignoreError, null);
    }

    public static <R> R safety(Supplier<R> function, boolean ignoreError, R r) {
        try {
            return function.get();
        } catch (Exception e) {
            if (!ignoreError) {
                e.printStackTrace();
                Log.e(TAG, "safety: " + e.getLocalizedMessage());
            }
        }
        return r;
    }

    public static void safety(VoidFunc0 function) {
        safety(function, false);
    }

    public static void safety(VoidFunc0 function, boolean ignoreError) {
        try {
            function.call();
        } catch (Exception e) {
            if (!ignoreError) {
                Log.e(TAG, "safety: " + e.getLocalizedMessage());
            }
        }
    }
}
