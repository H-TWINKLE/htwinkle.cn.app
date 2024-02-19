package cn.htwinkle.app.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.fastjson.JSONObject;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.htwinkle.app.BuildConfig;
import cn.htwinkle.app.R;
import cn.htwinkle.app.adapter.AppInfoAdapter;
import cn.htwinkle.app.annotation.AppModule;
import cn.htwinkle.app.constants.Constants;
import cn.htwinkle.app.constants.HttpConstant;
import cn.htwinkle.app.entity.AppInfo;
import cn.htwinkle.app.entity.ReleaseLatest;
import cn.htwinkle.app.kit.ClassesReader;
import cn.htwinkle.app.kit.CommKit;
import cn.htwinkle.app.kit.DbKit;
import cn.htwinkle.app.kit.PhoneKit;
import cn.htwinkle.app.kit.SharedPrefsKit;
import cn.htwinkle.app.kit.VersionUpdateKit;
import cn.htwinkle.app.view.base.BaseRefreshActivity;
import cn.htwinkle.app.wrapper.UpdateAppHttpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.BooleanUtil;
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

        idDialog(dialogView, sendNameEt);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        handler.postDelayed(this::fullscreen, 100);
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
        x.task().run(this::checkUpdate);

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

    private void idDialog(View dialogView, EditText sendNameEt) {
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
            if (module.defaultResourcesId() != 0) {
                appInfo.setResourcesId(module.defaultResourcesId());
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
                .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE)
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

    private void checkUpdate() {
        Pair<ReleaseLatest, ReleaseLatest.AssetsDTO> info = getLatestApk();
        if (info == null) {
            return;
        }

        ReleaseLatest.AssetsDTO latestApk = info.getValue();
        if (latestApk == null || StrUtil.isEmpty(latestApk.getName())) {
            return;
        }
        String[] name = latestApk.getName().split("_");

        Optional<String> versionName = Arrays.stream(name).filter(item -> item.toUpperCase(Locale.ROOT).startsWith("V")).findFirst();
        if (!versionName.isPresent()) {
            return;
        }
        String versionStr = versionName.get();
        String latestVersion = versionStr.replaceAll("[V.]", "");
        Double latestVersionD = Convert.toDouble(latestVersion);

        String thisVersion = BuildConfig.VERSION_NAME.replaceAll("[V.]", "");
        Double thisVersionD = Convert.toDouble(thisVersion);

        // boolean update = true;

        boolean update = latestVersionD > thisVersionD;

        // 远程版本低于现在版本
        if (!update) {
            return;
        }

        UpdateAppManager build = new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(this)
                //更新地址
                .setUpdateUrl(latestApk.getBrowserDownloadUrl())
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                .build();

        build.checkNewApp(new UpdateCallback() {
            @Override
            protected UpdateAppBean parseJson(String json) {
                UpdateAppBean appBean = new UpdateAppBean();

                appBean.setUpdate(StrUtil.upperFirst(BooleanUtil.toStringYesNo(update)))
                        .setOriginRes(JSONObject.toJSONString(info))
                        .setNewVersion(StrUtil.format(" {} ", versionStr))
                        .setApkFileUrl(latestApk.getBrowserDownloadUrl())
                        .setTargetSize(DataSizeUtil.format(latestApk.getSize()))
                        .setUpdateLog(StrUtil.format("更新时间：{} \n {}",
                                DateUtil.parse(info.getKey().getPublishedAt(), DatePattern.UTC_PATTERN), info.getKey().getBody()))
                        .setConstraint(false);
                return appBean;
            }
        });
    }

    private void setDefaultDeviceId() {
        base_tv_center_text.setText(PhoneKit.INSTANCE.getDeviceId(MainActivity.this));
    }

    private Pair<ReleaseLatest, ReleaseLatest.AssetsDTO> getLatestApk() {
        ReleaseLatest releaseLatest = VersionUpdateKit.INSTANCE.getGithubAssertsList(HttpConstant.GIT_HUB_RELEASE_LATEST);
        if (releaseLatest == null) {
            return null;
        }

        List<ReleaseLatest.AssetsDTO> assertsList = releaseLatest.getAssets();
        if (CollUtil.isEmpty(assertsList)) {
            return null;
        }

        Optional<ReleaseLatest.AssetsDTO> signed = assertsList.stream()
                .filter(item -> StrUtil.isNotEmpty(item.getName()))
                .filter(item -> item.getName().contains("signed"))
                .findFirst();
        return new Pair<>(releaseLatest, signed.orElse(null));
    }
}
