package cn.htwinkle.app.view.app;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.hjq.permissions.Permission;
import com.nanchen.screenrecordhelper.ScreenRecordHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.htwinkle.app.R;
import cn.htwinkle.app.adapter.SharedLiveAdapter;
import cn.htwinkle.app.annotation.AppModule;
import cn.htwinkle.app.constants.HttpConstant;
import cn.htwinkle.app.entity.ShareLive;
import cn.htwinkle.app.kit.PhoneKit;
import cn.htwinkle.app.view.base.BaseRefreshActivity;

@AppModule(value = ScreenShareActivity.TITLE,
        description = "手机屏幕分享工具",
        imgResourcesUrl = HttpConstant.FENGJING_RANDOM_PIC,
        permissions = {Permission.SEND_SMS, Permission.RECEIVE_SMS, Permission.READ_SMS,
                Permission.RECEIVE_WAP_PUSH, Permission.RECEIVE_MMS, Permission.READ_CONTACTS,
                Permission.READ_PHONE_STATE}
)
@ContentView(R.layout.activity_screen_shared)
public class ScreenShareActivity extends BaseRefreshActivity<ShareLive, SharedLiveAdapter> {

    public static final String TITLE = "屏幕分享";


    // 是否已经推送
    private final AtomicBoolean enabled = new AtomicBoolean();

    private ScreenRecordHelper screenRecordHelper = null;


    @ViewInject(R.id.screen_shared_btn)
    private Button screen_shared_btn;

    @Event(R.id.screen_shared_btn)
    private void onBtnClick(View view) {
        if (enabled.get() && getScreenRecordHelper().isRecording()) {
            getScreenRecordHelper().cancelRecord();
        } else {
            getScreenRecordHelper().startRecord();
        }
    }


    @Override
    public void finish() {
        super.finish();
        loadAnim(R.anim.no_anim, R.anim.anim_slide_out_bottom);
    }

    @Override
    public void initData() {
        setToolBarTitle(TITLE);
        adapter = new SharedLiveAdapter(R.layout.item_shared_live_info);
        loadAnim(R.anim.anim_slide_in_bottom, R.anim.no_anim);
        getData();
    }

    @Override
    public void getData() {

    }

    private ScreenRecordHelper getScreenRecordHelper() {
        if (screenRecordHelper == null) {
            File recordDir = PhoneKit.getChildDir("record");
            screenRecordHelper = new ScreenRecordHelper(this, new ScreenRecordHelper.OnVideoRecordListener() {
                @Override
                public void onBeforeRecord() {

                }

                @Override
                public void onStartRecord() {
                    screen_shared_btn.setText("正在分享");
                    enabled.set(true);
                }

                @Override
                public void onCancelRecord() {
                    screen_shared_btn.setText("开始分享");
                    enabled.set(false);
                }

                @Override
                public void onEndRecord() {
                    screen_shared_btn.setText("开始分享");
                    enabled.set(false);
                }
            }, recordDir.getAbsolutePath());
        }
        return screenRecordHelper;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && data != null) {
            getScreenRecordHelper().onActivityResult(requestCode, resultCode, data);
        }
    }
}
