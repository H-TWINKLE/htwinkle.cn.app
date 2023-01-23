package cn.htwinkle.app.view.app;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import cn.htwinkle.app.R;
import cn.htwinkle.app.adapter.SmsPreviewAdapter;
import cn.htwinkle.app.entity.SmsPreview;
import cn.htwinkle.app.kit.SMSKit;
import cn.htwinkle.app.view.base.BaseRefreshActivity;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;


@ContentView(R.layout.activity_send_info_preview)
public class PreviewSMSActivity extends BaseRefreshActivity<SmsPreview, SmsPreviewAdapter> {

    public static final String TITLE = "群发短信预览";

    private String originText = "";
    private List<SmsPreview> smsPreviewList = null;
    private boolean isDebug = false;

    @ViewInject(R.id.base_rich_fab)
    private FloatingActionButton base_rich_fab;

    @ViewInject(R.id.send_info_preview_switch)
    private SwitchMaterial send_info_preview_switch;

    @Event(R.id.base_rich_fab)
    private void onRichFabClick(View view) {
        toSendMessage();
    }

    @Event(value = R.id.send_info_preview_switch, type = CompoundButton.OnCheckedChangeListener.class)
    private void onSwitchClick(CompoundButton compoundButton, boolean b) {
        isDebug = b;
    }

    private void toSendMessage() {
        if (CollUtil.isEmpty(smsPreviewList)) {
            setToastString("请确认发送人员");
        }
        base_rich_fab.setVisibility(View.GONE);
        for (int i = 0; i < smsPreviewList.size(); i++) {
            SmsPreview smsPreview = smsPreviewList.get(i);
            int finalI = i;
            SMSKit.INSTANCE.sendMessage(isDebug, smsPreview.getSmsPerson().getTelPhone(), smsPreview.getSendText(),
                    smsPreviewList.size(), i + 1,
                    new SMSKit.Listener() {
                        @Override
                        public void onSuccess(String tel, String text) {
                            Optional<SmsPreview> finder = getFinder(tel);
                            finder.ifPresent(item -> {
                                item.setStatus(SmsPreview.SUCCESS);
                                updateView(finalI);
                            });
                        }

                        @Override
                        public void onFail(String tel, String text) {
                            Optional<SmsPreview> first = getFinder(tel);
                            first.ifPresent(item -> {
                                item.setStatus(SmsPreview.FAILURE);
                                updateView(finalI);
                            });
                        }

                        @Override
                        public void onError(String tel, String text, Exception e) {
                            Optional<SmsPreview> first = getFinder(tel);
                            first.ifPresent(item -> {
                                item.setStatus(SmsPreview.ERROR);
                                updateView(finalI);
                            });
                        }

                        @Override
                        public void onFinish() {
                            runOnUiThread(() -> base_rich_fab.setVisibility(View.VISIBLE));
                        }
                    });
        }
    }

    private void updateView(int count) {
        runOnUiThread(() -> adapter.notifyItemChanged(count));
    }

    @NonNull
    private Optional<SmsPreview> getFinder(String tel) {
        return adapter.getData().stream()
                .filter(Objects::nonNull)
                .filter(item -> StrUtil.isNotEmpty(item.getSmsPerson().getTelPhone()))
                .filter(item -> item.getSmsPerson().getTelPhone().equals(tel)).findFirst();
    }


    @Override
    public void finish() {
        super.finish();
        loadAnim(R.anim.no_anim, R.anim.anim_slide_out_bottom);
    }

    @Override
    public void initData() {
        setToolBarTitle(TITLE);
        adapter = new SmsPreviewAdapter(R.layout.item_send_info_preview, this);
        loadAnim(R.anim.anim_slide_in_bottom, R.anim.no_anim);
        getData();
    }

    @Override
    public void getData() {
        Intent intent = getIntent();
        originText = intent.getStringExtra(GroupSMSActivity.PREVIEW_TEXT);
        String previewList = intent.getStringExtra(GroupSMSActivity.PREVIEW_LIST);
        if (StrUtil.isEmpty(previewList)) {
            finish();
        }
        smsPreviewList = JSONObject.parseArray(previewList, SmsPreview.class);
        onSuccessGetData(smsPreviewList);
    }
}
