package cn.htwinkle.app.view.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.hjq.permissions.Permission;
import com.pedro.common.ConnectChecker;
import com.pedro.common.VideoCodec;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.htwinkle.app.R;
import cn.htwinkle.app.adapter.OnlineStreamAdapter;
import cn.htwinkle.app.annotation.AppModule;
import cn.htwinkle.app.constants.Constants;
import cn.htwinkle.app.constants.HttpConstant;
import cn.htwinkle.app.entity.OnlineStream;
import cn.htwinkle.app.view.app.screen.share.DisplayService;
import cn.htwinkle.app.view.app.screen.share.ShareViewActivity;
import cn.htwinkle.app.view.base.BaseRefreshActivity;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

@AppModule(value = ScreenShareActivity.TITLE,
        description = "轻量屏幕分享",
        imgResourcesUrl = HttpConstant.FENGJING_RANDOM_PIC,
        permissions = {Permission.RECORD_AUDIO}
)
@ContentView(R.layout.activity_screen_shared)
public class ScreenShareActivity extends BaseRefreshActivity<OnlineStream.StreamsDTO, OnlineStreamAdapter> implements ConnectChecker {

    public static final String TITLE = "屏幕分享";

    private String ENDPOINT = "";

    // 是否已经推送
    private final AtomicBoolean enabled = new AtomicBoolean(false);

    private final int REQUEST_CODE_STREAM = 179;

    @ViewInject(R.id.screen_shared_btn)
    private Button screen_shared_btn;

    @Event(R.id.screen_shared_btn)
    private void onBtnClick(View view) {
        if (!enabled.get()) {
            startRecord();
        } else {
            stopRecord();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ENDPOINT = StrUtil.format("rtmp://htwinkle.cn:1935/live/{}_{}", Constants.MOBILE_DEVICE_BRAND, RandomUtil.randomString(20));

        startService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }


    @Override
    public void finish() {
        super.finish();
        loadAnim(R.anim.no_anim, R.anim.anim_slide_out_bottom);
        stopService();
    }

    @Override
    public void initData() {
        setToolBarTitle(TITLE);
        adapter = new OnlineStreamAdapter(R.layout.item_shared_live_info, this);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(this, ShareViewActivity.class);
            intent.putExtra(Constants.SHARE_VIEW, this.adapter.getData().get(position));
            startActivity(intent);
        });
        loadAnim(R.anim.anim_slide_in_bottom, R.anim.no_anim);
        getData();
    }

    @Override
    public void getData() {
        x.task().run(() -> {
            String auth = HttpUtil.buildBasicAuth("admin", "13038132020", CharsetUtil.CHARSET_UTF_8);
            String response = HttpRequest.get(HttpConstant.ONLINE_STREAM_LIST).auth(auth).execute().body();
            Log.i(TAG, "onFinish: 获取推流信息列表成功");
            if (StrUtil.isEmpty(response)) {
                runOnUiThread(() -> onSuccessGetData(Collections.emptyList()));
                return;
            }
            OnlineStream object = JSONObject.parseObject(response, OnlineStream.class);
            if (object.getCode() == 0 && CollUtil.isNotEmpty(object.getStreams())) {
                runOnUiThread(() -> onSuccessGetData(object.getStreams()));
                return;
            }
            runOnUiThread(() -> onSuccessGetData(Collections.emptyList()));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && (requestCode == REQUEST_CODE_STREAM && resultCode == Activity.RESULT_OK)) {
            DisplayService displayService = DisplayService.COMPANION.getINSTANCE();
            if (displayService != null) {
                displayService.prepareStreamRtp(resultCode, data);
                displayService.startStreamRtp(ENDPOINT);
            }
        } else {
            Toast.makeText(this, "获取数据失败，请验证权限是否正常", Toast.LENGTH_SHORT).show();
            setStartShare();
        }
    }

    @Override
    public void onAuthError() {

    }

    @Override
    public void onAuthSuccess() {

    }

    @Override
    public void onConnectionFailed(@NonNull String s) {
        runOnUiThread(() -> Toast.makeText(this, "连接失败：" + s, Toast.LENGTH_SHORT).show());
        Log.e(TAG, "RTP service connection failed: " + s);
        runOnUiThread(this::stopRecord);
    }

    @Override
    public void onConnectionStarted(@NonNull String s) {
        DisplayService displayService = DisplayService.COMPANION.getINSTANCE();
        if (displayService != null) {
            displayService.showNotification("Stream connection started");
        }
        Log.e(TAG, "RTP service connection started");
        runOnUiThread(() -> Toast.makeText(this, "准备分享", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onConnectionSuccess() {
        DisplayService displayService = DisplayService.COMPANION.getINSTANCE();
        if (displayService != null) {
            displayService.showNotification("RTP service connection success");
        }
        Log.e(TAG, "RTP service connection success");
        runOnUiThread(() -> Toast.makeText(this, "正在分享", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDisconnect() {
        Toast.makeText(this, "分享结束", Toast.LENGTH_SHORT).show();
        stopRecord();
    }

    @Override
    public void onNewBitrate(long l) {

    }

    private void startRecord() {
        DisplayService displayService = DisplayService.COMPANION.getINSTANCE();
        if (displayService != null) {
            displayService.onInit();
            displayService.setVideoCodec(VideoCodec.H264);
            startActivityForResult(displayService.sendIntent(), REQUEST_CODE_STREAM);
        }
        enabled.set(true);
        screen_shared_btn.setText("停止分享");
    }

    private void stopRecord() {
        DisplayService displayService = DisplayService.COMPANION.getINSTANCE();
        if (displayService != null) {
            displayService.stopStream();
        }
        setStartShare();
    }

    private void setStartShare() {
        enabled.set(false);
        screen_shared_btn.setText("开始分享");
    }

    private void startService() {
        DisplayService displayService = DisplayService.COMPANION.getINSTANCE();
        if (displayService == null) {
            DisplayService.COMPANION.setConnectChecker(this);
            startService(new Intent(this, DisplayService.class));
            setDeviceProp();
        }
    }

    private void stopService() {
        DisplayService displayService = DisplayService.COMPANION.getINSTANCE();
        if (displayService != null && displayService.isStreaming()) {
            stopService(new Intent(this, DisplayService.class));
        }
    }

    private void setDeviceProp() {
        setDeviceWH();
        setDpi();
    }

    private void setDeviceWH() {
        Display display = getWindowManager().getDefaultDisplay();
        // 方法一(推荐使用)使用Point来保存屏幕宽、高两个数据
        Point outSize = new Point();
        // 通过Display对象获取屏幕宽、高数据并保存到Point对象中
        display.getSize(outSize);
        // 从Point对象中获取宽、高
        int x = outSize.x;
        int y = outSize.y;

        DisplayService.COMPANION.setWidth(x);
        DisplayService.COMPANION.setHeight(y);
    }

    private void setDpi() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int densityDpi = displayMetrics.densityDpi;

        DisplayService.COMPANION.setDpi(densityDpi);
    }
}
