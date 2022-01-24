package cn.htwinkle.app.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.yanzhenjie.permission.AndPermission;

import org.xutils.view.annotation.ContentView;

import java.util.List;
import java.util.stream.Collectors;

import cn.htwinkle.app.R;
import cn.htwinkle.app.adapter.AppInfoAdapter;
import cn.htwinkle.app.annotation.AppModule;
import cn.htwinkle.app.constants.Constants;
import cn.htwinkle.app.entity.AppInfo;
import cn.htwinkle.app.kit.ClassesReader;
import cn.htwinkle.app.view.base.BaseRefreshActivity;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseRefreshActivity<AppInfo, AppInfoAdapter> {

    @Override
    public void setDefaultView() {
        getBase_swipe_refresh_layout().setColorSchemeColors(Color.rgb(47, 223, 189));
        getBase_swipe_refresh_layout().setOnRefreshListener(this::refreshData);

        if (layoutManager == null) {
            layoutManager = new GridLayoutManager(MainActivity.this, 2);
        }
        getBase_recycler_view().setLayoutManager(layoutManager);
        getBase_recycler_view().setAdapter(adapter);
    }

    @Override
    public void initData() {
        adapter = new AppInfoAdapter(R.layout.item_app_info);
        adapter.setOnItemClickListener((adapter, view, position) ->
        {
            preStartApp(position);
        });
        loadAnim(R.anim.anim_slide_in_bottom, R.anim.no_anim);
        getData();
    }

    @Override
    public void finish() {
        super.finish();
        loadAnim(R.anim.no_anim, R.anim.anim_slide_out_bottom);
    }

    @Override
    public void getData() {
        List<Class<?>> list = ClassesReader.reader(Constants.APP_LIST_PACKAGE, this);
        onSuccessGetData(list
                .stream()
                .filter(item -> item.isAnnotationPresent(AppModule.class))
                .map(this::getAppInfo).collect(Collectors.toList()));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backToHome();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backToHome() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    /**
     * 获取注册的app信息
     *
     * @param item item
     * @return AppInfo
     */
    @NonNull
    private AppInfo getAppInfo(Class<?> item) {
        AppModule module = item.getDeclaredAnnotation(AppModule.class);
        AppInfo appInfo = new AppInfo();
        if (module != null) {
            appInfo.setName(module.value());
            appInfo.setDescription(module.description());
            appInfo.settClass(item);
            appInfo.setPermission(module.permissions());
            if (module.imgResourcesId() != 0) {
                appInfo.setResourcesId(module.imgResourcesId());
            }
            if (!TextUtils.isEmpty(module.imgResourcesUrl())) {
                appInfo.setImgUrl(module.imgResourcesUrl());
            }
        }
        return appInfo;
    }

    @SuppressLint("WrongConstant")
    private void preStartApp(int position) {
        AppInfo info = this.adapter.getItem(position);
        if (info.getPermission().length > 0) {
            AndPermission.with(this)
                    .runtime()
                    .permission(info.getPermission())
                    .onGranted(permissions -> startApp(info))
                    .onDenied(permissions -> setToastString("获取权限失败，请检查"))
                    .start();
            return;
        }
        startApp(info);
    }

    private void startApp(AppInfo info) {
        startActivity(info.gettClass());
    }
}
