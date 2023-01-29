package cn.htwinkle.app.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.alibaba.fastjson.JSONObject;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.loopj.android.image.SmartImageView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Locale;

import cn.htwinkle.app.R;
import cn.htwinkle.app.constants.HttpConstant;
import cn.htwinkle.app.entity.WelPhoto;
import cn.htwinkle.app.kit.CommKit;
import cn.htwinkle.app.kit.HttpKit;
import cn.htwinkle.app.kit.StrKit;
import cn.htwinkle.app.listener.BaseHttpListener;
import cn.htwinkle.app.view.base.BaseActivity;
import okhttp3.FormBody;
import okhttp3.RequestBody;

@ContentView(R.layout.activity_wel)
public class WelActivity extends BaseActivity implements BaseHttpListener.HttpListener {

    public static final int MILLIS_IN_FUTURE = 5000;
    private final Handler handler = new Handler();

    @ViewInject(R.id.wel_cl)
    private CoordinatorLayout wel_cl;

    @ViewInject(R.id.wel_siv_background)
    private SmartImageView wel_siv_background;

    @ViewInject(R.id.wel_button)
    private Button wel_button;

    @ViewInject(R.id.wel_describe)
    private TextView wel_describe;

    @Event(R.id.wel_button)
    private void onButtonClick(View v) {
        c.onFinish();
    }

    @Override
    public void initData() {
        loadStoragesPermission();
    }

    @Override
    public void initView() {
        loadBackgroundImage();
        c.start();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        handler.postDelayed(() -> wel_cl.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION), 500);
    }


    @Override
    public void onFinish(String response) {
        CommKit.safety(() -> {
            WelPhoto welPhoto = JSONObject.parseObject(response, WelPhoto.class);
            if (StrKit.isOk(welPhoto.getState()) && !welPhoto.getList().isEmpty()) {
                runOnUiThread(() ->
                {
                    WelPhoto.ListBean bean = welPhoto.getList().get(0);
                    wel_siv_background.setImageUrl(bean.getPictureUrl());
                    wel_describe.setText(StrKit.safetyText(bean.getPictureName()));
                    if (!TextUtils.isEmpty(bean.getPictureName())) {
                        wel_describe.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, true);
    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, "onError: 获取图片失败" + e.getLocalizedMessage());

    }

    private CountDownTimer c = new CountDownTimer(MILLIS_IN_FUTURE, 1000) {
        @Override
        public void onTick(long l) {
            setButtonText((int) l);
        }

        @Override
        public void onFinish() {
            c.cancel();
            toActivity();
            finish();
        }
    };

    private void setButtonText(int s) {
        runOnUiThread(() -> wel_button
                .setText(String.format(Locale.CHINA, "跳过  %ds", s / 1000)));
    }

    /**
     * 设备动态登录图片
     * 广告页面
     */
    private void loadBackgroundImage() {
        RequestBody requestBody = new FormBody
                .Builder()
                .add("num", "1")
                .add("plate", "1")
                .add("type", "fengjing")
                .build();
        HttpKit.INSTANCE.postAsync(HttpConstant.GET_WEL_PIC, false, requestBody, new BaseHttpListener(this));
    }

    /**
     * 前往主应用
     */
    private void toActivity() {
        startActivity(MainActivity.class);
    }

    @SuppressLint("WrongConstant")
    private void loadStoragesPermission() {
        XXPermissions.with(this)
                .permission(Permission.Group.STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {

                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        setToastString("请授予文件访问权限");
                    }
                });
    }
}
