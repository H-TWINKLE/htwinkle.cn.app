package cn.htwinkle.app.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.loopj.android.image.SmartImageView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Locale;

import cn.htwinkle.app.R;
import cn.htwinkle.app.componet.NotCacheWebImage;
import cn.htwinkle.app.view.base.BaseActivity;

@ContentView(R.layout.activity_wel)
public class WelActivity extends BaseActivity {

    public static final int MILLIS_IN_FUTURE = 5000;

    @ViewInject(R.id.wel_cl)
    private CoordinatorLayout wel_cl;

    @ViewInject(R.id.wel_siv_background)
    private SmartImageView wel_siv_background;

    @ViewInject(R.id.wel_button)
    private Button wel_button;

    private final Handler handler = new Handler();

    @Event(R.id.wel_button)
    private void onButtonClick(View v) {
        c.onFinish();
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


    @Override
    public void initData() {
        loadStoragesPermission();
    }

    @Override
    public void initView() {
        buildBackgroundImage();
        c.start();
    }

    /**
     * 设备动态登录图片
     * 广告页面
     */
    private void buildBackgroundImage() {
        wel_siv_background.setImage(new NotCacheWebImage(""));
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        handler.postDelayed(() -> wel_cl.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION), 500);
    }


    /**
     * 前往主应用
     */
    private void toActivity() {

    }

    @SuppressLint("WrongConstant")
    private void loadStoragesPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onDenied(permissions -> {
                    setToastString("请授予文件访问权限");
                })
                .start();
    }
}
