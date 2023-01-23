package cn.htwinkle.app.kit;

import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import cn.hutool.core.thread.ThreadUtil;

public enum SMSKit {
    INSTANCE;

    public static final ExecutorService POOL_EXECUTOR = ThreadUtil.newSingleExecutor();

    public void sendMessage(boolean debug, String tel, String text, int allCount, int nowCount, Listener listener) {
        POOL_EXECUTOR.submit(() -> {
            if (listener != null) {
                listener.onPrepare(tel, text);
            }

            if (sendSms(debug, tel, text)) {
                if (listener != null) {
                    listener.onSuccess(tel, text);
                }
            } else {
                if (listener != null) {
                    listener.onFail(tel, text);
                }
            }

            if (allCount == nowCount && listener != null) {
                listener.onFinish();
            }
            ThreadUtil.sleep(2000);
        });
    }

    /**
     * 发送短信服务
     *
     * @param tel  tel
     * @param text text
     */
    private boolean sendSms(boolean debug, String tel, String text) {
        try {
            if (!debug) {
                SmsManager manager = SmsManager.getDefault();
                ArrayList<String> smsMessageList = manager.divideMessage(text);
                smsMessageList.forEach(item ->
                        manager.sendTextMessage(tel, null, item, null, null));
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public interface Listener {
        void onPrepare(String tel, String text);

        void onSuccess(String tel, String text);

        void onFail(String tel, String text);

        void onError(String tel, String text, Exception e);

        default void onFinish() {
        }
    }
}
