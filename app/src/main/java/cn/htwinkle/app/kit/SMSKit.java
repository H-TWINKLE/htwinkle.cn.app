package cn.htwinkle.app.kit;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hutool.core.thread.ThreadUtil;

public enum SMSKit {
    INSTANCE;

    public void sendMessage(int simCardId, boolean debug, String tel, String text, int allCount, int nowCount, Listener listener) {
        CommKit.POOL_EXECUTOR.submit(() -> {
            if (listener != null) {
                listener.onPrepare(tel, text);
            }

            if (sendSms(simCardId, debug, tel, text)) {
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
    private boolean sendSms(int simCardId, boolean debug, String tel, String text) {
        try {
            if (!debug) {
                SmsManager manager = SmsManager.getSmsManagerForSubscriptionId(simCardId);
                ArrayList<String> smsMessageList = manager.divideMessage(text);
                smsMessageList.forEach(item ->
                        manager.sendTextMessage(tel, null, item, null, null));
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public int getDefaultSimCardId() {
        return SmsManager.getDefault().getSubscriptionId();
    }

    public List<SubscriptionInfo> getSubscriptionInfoList(Activity activity) {
        return CommKit.safety(() -> {
            SubscriptionManager localSubscriptionManager = SubscriptionManager.from(activity);
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED) {
                return localSubscriptionManager.getActiveSubscriptionInfoList();
            }
            return Collections.emptyList();
        }, true, Collections.emptyList());
    }

    /**
     * 获取手机卡信息
     *
     * @param activity activity
     * @return int
     */
    public int getPhoneSimCardCount(Activity activity) {
        return CommKit.safety(() -> {
            SubscriptionManager localSubscriptionManager = SubscriptionManager.from(activity);
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED) {
                return localSubscriptionManager.getActiveSubscriptionInfoCount();
            }
            return 1;
        }, true, 1);
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
