package cn.htwinkle.app.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.stream.Collectors;

import cn.htwinkle.app.R;
import cn.htwinkle.app.adapter.AppInfoAdapter;
import cn.htwinkle.app.annotation.AppModule;
import cn.htwinkle.app.constants.Constants;
import cn.htwinkle.app.entity.AppInfo;
import cn.htwinkle.app.kit.ClassesReader;
import cn.htwinkle.app.kit.CommKit;
import cn.htwinkle.app.kit.DbKit;
import cn.htwinkle.app.kit.PhoneKit;
import cn.htwinkle.app.kit.SharedPrefsKit;
import cn.htwinkle.app.view.base.BaseRefreshActivity;
import cn.hutool.core.util.StrUtil;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseRefreshActivity<AppInfo, AppInfoAdapter> {

    private final Handler handler = new Handler();

    @ViewInject(R.id.base_tv_center_text)
    private TextView base_tv_center_text;

    @Event(R.id.base_tv_center_text)
    private void onDeviceIdClick(View view) {
        String deviceId = PhoneKit.INSTANCE.getDeviceId(MainActivity.this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.base_dialog_edit, null);
        EditText sendNameEt = dialogView.findViewById(R.id.base_dialog_text_et);
        sendNameEt.setText(deviceId);
        sendNameEt.setSelection(deviceId.length());
        sendNameEt.postDelayed(() -> {
            sendNameEt.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(sendNameEt, InputMethodManager.SHOW_IMPLICIT);
        }, 100);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("请输入身份标志")
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    runOnUiThread(() -> {
                        Editable text = sendNameEt.getText();
                        if (StrUtil.isEmpty(text.toString())) {
                            setToastString("输入的身份标志为空");
                            return;
                        }
                        base_tv_center_text.setText(text);
                        SharedPrefsKit.INSTANCE.saveValue(this, Constants.GLOBAL_DEVICE_NAME, text.toString());
                        CommKit.safety(() -> DbKit.INSTANCE.getDb().close());
                    });
                    dialogInterface.cancel();
                })
                .setNeutralButton("复原", (dialogInterface, i) -> {
                    runOnUiThread(() -> {
                        SharedPrefsKit.INSTANCE.removeKey(this, Constants.GLOBAL_DEVICE_NAME);
                        CommKit.safety(() -> DbKit.INSTANCE.getDb().close());
                        setDefaultDeviceId();
                    });
                    dialogInterface.cancel();
                })
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.cancel())
                .create();

        alertDialog.show();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        handler.postDelayed(() -> {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }, 100);
    }

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
        adapter.setOnItemClickListener((adapter, view, position) -> preStartApp(position));
        loadAnim(R.anim.anim_slide_in_bottom, R.anim.no_anim);
        initDeviceId();
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
            appInfo.setImgUrl(module.imgResourcesUrl());
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
            XXPermissions.with(this)
                    .permission(info.getPermission())
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            startApp(info);
                        }

                        @Override
                        public void onDenied(List<String> permissions, boolean never) {
                            setToastString("获取权限失败，请检查");
                        }
                    });
            return;
        }
        startApp(info);
    }

    private void startApp(AppInfo info) {
        startActivity(info.gettClass());
    }

    private void initDeviceId() {
        XXPermissions.with(this)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        setDefaultDeviceId();
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        setToastString("获取文件权限失败，请检查");
                    }
                });
    }

    private void setDefaultDeviceId() {
        base_tv_center_text.setText(PhoneKit.INSTANCE.getDeviceId(MainActivity.this));
    }
}
