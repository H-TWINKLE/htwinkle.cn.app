package cn.htwinkle.app.view.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

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
    // 默认的电话卡的id
    private int simCardId = SMSKit.INSTANCE.getDefaultSimCardId();

    @ViewInject(R.id.base_rich_fab)
    private FloatingActionButton base_rich_fab;

    @ViewInject(R.id.send_info_preview_switch)
    private SwitchMaterial send_info_preview_switch;

    @ViewInject(R.id.send_info_preview_sim_iv)
    private ImageView send_info_preview_sim_iv;

    @Event(R.id.base_rich_fab)
    private void onRichFabClick(View view) {
        toSendMessage();
    }

    @Event(R.id.send_info_preview_sim_iv)
    private void onSimIvClick(View view) {
        List<SubscriptionInfo> subscriptionInfoList = SMSKit.INSTANCE.getSubscriptionInfoList(this);
        CharSequence[] displayName = getDisplayName(subscriptionInfoList);

        showDialog(subscriptionInfoList, displayName);
    }

    @Event(value = R.id.send_info_preview_switch, type = CompoundButton.OnCheckedChangeListener.class)
    private void onSwitchClick(CompoundButton compoundButton, boolean b) {
        isDebug = b;
    }

    private void showDialog(List<SubscriptionInfo> subscriptionInfoList, CharSequence[] displayName) {
        new AlertDialog.Builder(this)
                .setTitle("请选择发送短信的SIM卡")
                .setItems(displayName, (dialogInterface, i) -> {
                    simCardId = subscriptionInfoList.get(i).getSubscriptionId();
                    if (i > 0) {
                        send_info_preview_sim_iv.setImageDrawable(getResources().getDrawable(R.drawable.dual_sim_two_line));
                    } else {
                        send_info_preview_sim_iv.setImageDrawable(getResources().getDrawable(R.drawable.dual_sim_one_line));
                    }
                })
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.cancel())
                .show();
    }

    @NonNull
    private CharSequence[] getDisplayName(List<SubscriptionInfo> subscriptionInfoList) {
        return subscriptionInfoList.stream()
                .map(item -> {
                    if (StrUtil.isNotEmpty(item.getDisplayName())) {
                        return item.getDisplayName();
                    }
                    if (StrUtil.isNotEmpty(item.getCarrierName())) {
                        return item.getCarrierName();
                    }
                    if (StrUtil.isNotEmpty(item.getNumber())) {
                        return item.getNumber();
                    }
                    return (item.getSimSlotIndex() + 1) + "";
                }).toArray(CharSequence[]::new);
    }

    private void toSendMessage() {
        if (CollUtil.isEmpty(smsPreviewList)) {
            setToastString("请确认发送人员");
        }
        base_rich_fab.setVisibility(View.GONE);
        for (int i = 0; i < smsPreviewList.size(); i++) {
            SmsPreview smsPreview = smsPreviewList.get(i);
            int finalI = i;
            SMSKit.INSTANCE.sendMessage(simCardId, isDebug, smsPreview.getSmsPerson().getTelPhone(), smsPreview.getSendText(),
                    smsPreviewList.size(), i + 1,
                    new SMSKit.Listener() {
                        @Override
                        public void onPrepare(String tel, String text) {
                            Optional<SmsPreview> finder = getFinder(tel);
                            finder.ifPresent(item -> {
                                item.setStatus(SmsPreview.PREPARE);
                                updateView(finalI);
                            });
                        }

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
        checkSimCardStatus();
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

    /**
     * 检查sim状态
     */
    private void checkSimCardStatus() {
        int phoneSimCardCount = SMSKit.INSTANCE.getPhoneSimCardCount(this);
        send_info_preview_sim_iv.setEnabled(phoneSimCardCount > 1);
    }
}
