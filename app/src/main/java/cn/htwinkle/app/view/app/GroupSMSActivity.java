package cn.htwinkle.app.view.app;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hjq.permissions.Permission;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.htwinkle.app.R;
import cn.htwinkle.app.adapter.SmsPersonAdapter;
import cn.htwinkle.app.annotation.AppModule;
import cn.htwinkle.app.constants.HttpConstant;
import cn.htwinkle.app.entity.SmsPerson;
import cn.htwinkle.app.entity.SmsPreview;
import cn.htwinkle.app.entity.sms.SmsGroupOut;
import cn.htwinkle.app.kit.CommKit;
import cn.htwinkle.app.kit.DbKit;
import cn.htwinkle.app.kit.HttpKit;
import cn.htwinkle.app.kit.PhoneKit;
import cn.htwinkle.app.kit.SharedPrefsKit;
import cn.htwinkle.app.view.base.BaseRefreshActivity;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

@AppModule(value = GroupSMSActivity.TITLE,
        description = "自定义称谓的群发信息",
        imgResourcesId = R.drawable.welcome_default_pic,
        permissions = {Permission.SEND_SMS, Permission.RECEIVE_SMS, Permission.READ_SMS,
                Permission.RECEIVE_WAP_PUSH, Permission.RECEIVE_MMS, Permission.READ_CONTACTS,
                Permission.READ_PHONE_STATE, Permission.MANAGE_EXTERNAL_STORAGE}
)
@ContentView(R.layout.base_recycler_with_toolbar)
public class GroupSMSActivity extends BaseRefreshActivity<SmsPerson, SmsPersonAdapter> {

    public static final String TITLE = "群发短信";
    public static final String EDIT_TEXT = "EDIT_TEXT";

    public static final String PREVIEW_LIST = "PREVIEW_LIST";
    public static final String PREVIEW_TEXT = "PREVIEW_TEXT";

    private static final SortChineseName sortChineseName = new SortChineseName();

    private boolean loadRemoteFirst = false;

    private EditText richSendTextEt;
    private EditText filterTextEt;
    private FloatingActionButton base_rich_fab;

    private String filteredText = "";

    @Override
    public void finish() {
        super.finish();
        loadAnim(R.anim.no_anim, R.anim.anim_slide_out_bottom);
    }

    @Override
    public void initData() {
        setToolBarTitle(TITLE + getDeviceId());
        adapter = new SmsPersonAdapter(R.layout.item_sms_person_main, this);
        adapter.addHeaderView(initHeaderView1());
        adapter.addHeaderView(initHeaderView1_5());
        adapter.addHeaderView(initHeaderView2());
        adapter.addChildClickViewIds(R.id.item_sms_person_send_name_tv,
                R.id.item_sms_person_cb_enable,
                R.id.item_sms_person_cb_back_up);
        loadAnim(R.anim.anim_slide_in_bottom, R.anim.no_anim);
        getData();
    }

    @Override
    public void getData() {
        CommKit.POOL_EXECUTOR.execute(this::combineData);
    }

    private String getDeviceId() {
        String deviceId = PhoneKit.INSTANCE.getDeviceId(this);
        if (StrUtil.isEmpty(deviceId)) {
            return "";
        }
        return "  " + (deviceId.length() > 10 ? deviceId.substring(0, 10) : deviceId);
    }

    /**
     * 初始化富文本框
     *
     * @return View
     */
    private View initHeaderView1() {
        View headerView = View.inflate(this, R.layout.item_sms_edit, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        richSendTextEt = headerView.findViewById(R.id.base_rich_text_et);
        richSendTextEt.setText(loadEditText());
        richSendTextEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                saveEditText(s.toString());
            }
        });
        base_rich_fab = headerView.findViewById(R.id.base_rich_fab);
        base_rich_fab.setOnClickListener(v -> {
            if (TextUtils.isEmpty(richSendTextEt.getText())) {
                setToastString("请填写内容");
                return;
            }
            List<SmsPerson> canSend =
                    this.adapter.getData()
                            .stream()
                            .filter(item -> item.isChecked() && !TextUtils.isEmpty(item.getName()))
                            .sorted(sortChineseName)
                            .collect(Collectors.toList());
            if (canSend.size() > 0) {
                toPreviewSendMessage(canSend);
                return;
            }
            setToastString("请勾选需要发送的联系人");
        });
        return headerView;
    }

    private View initHeaderView1_5() {
        View headerView = View.inflate(this, R.layout.base_dialog_edit, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        filterTextEt = headerView.findViewById(R.id.base_dialog_text_et);
        filterTextEt.setTag(filteredText);
        filterTextEt.setHint("请输入过滤条件 ...");
        filterTextEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filteredText = editable.toString();
                combineData();
                filterTextEt.postDelayed(() -> {
                    filterTextEt.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.showSoftInput(filterTextEt, InputMethodManager.SHOW_IMPLICIT);
                }, 50);
            }
        });
        return headerView;
    }

    private View initHeaderView2() {
        View headerView = View.inflate(this, R.layout.item_sms_person_sample, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return headerView;
    }

    /**
     * 组合数据和缓存数据
     */
    private void combineData() {
        // 获取系统的电话信息
        Map<String, SmsPerson> smsPeople = PhoneKit.INSTANCE.getPhoneTelInfo(this).stream()
                .sorted(sortChineseName)
                .filter(item -> !TextUtils.isEmpty(item.getName()) && item.getName().length() < 4)
                .collect(Collectors.toMap(SmsPerson::getTelPhone, Function.identity(), (key1, key2) -> key2));

        // 获取缓存的人
        Map<String, SmsPerson> cachePeople = getCachePerson();

        // 以缓存的人的信息为主
        smsPeople.putAll(cachePeople);

        // 以远程的人信息为主(仅加载一次)
        Map<String, SmsPerson> remoteData = getRemoteData();
        smsPeople.putAll(remoteData);

        List<SmsPerson> peopleList = new ArrayList<>();

        peopleList.addAll(smsPeople
                .values()
                .stream()
                .filter(SmsPerson::isChecked)
                .sorted(sortChineseName)
                .collect(Collectors.toList()));

        peopleList.addAll(smsPeople
                .values()
                .stream()
                .filter(item -> !item.isChecked())
                .sorted(sortChineseName)
                .collect(Collectors.toList()));

        List<SmsPerson> data = peopleList.stream()
                .filter(item -> item.extendText().contains(filteredText))
                .collect(Collectors.toList());

        runOnUiThread(() -> onSuccessGetData(data));
    }

    private Map<String, SmsPerson> getRemoteData() {
        String sn = PhoneKit.INSTANCE.getDeviceId(this);
        if (!loadRemoteFirst && StrUtil.isNotEmpty(sn)) {
            loadRemoteFirst = true;
            List<SmsGroupOut> safety = CommKit.safety(() -> {
                Map<String, Object> params = new HashMap<>();
                params.put("sn", sn);
                String text = HttpUtil.get(HttpConstant.GET_LIST_BY, params, 3000);
                String baseData = HttpKit.INSTANCE.getBaseData(text);
                if (StrUtil.isEmpty(baseData)) {
                    return null;
                }
                return JSONObject.parseArray(baseData, SmsGroupOut.class);
            }, true);
            if (CollUtil.isEmpty(safety)) {
                return Collections.emptyMap();
            }
            return safety.stream()
                    .map(SmsGroupOut::of)
                    .peek(SmsPerson::saveSelfSafety)
                    .collect(Collectors.toMap(SmsPerson::getTelPhone, Function.identity(), (key1, key2) -> key2));
        }
        return Collections.emptyMap();
    }

    /**
     * 发送短信
     *
     * @param canSend canSend
     */
    private void toPreviewSendMessage(List<SmsPerson> canSend) {
        String sendText = richSendTextEt.getText().toString();
        if (StrUtil.isEmpty(sendText)) {
            setToastString("请填写短信文字");
            return;
        }
        if (CollUtil.isEmpty(canSend)) {
            setToastString("请确认群发短信人员");
            return;
        }
        Intent intent = new Intent(this, PreviewSMSActivity.class);
        intent.putExtra(PREVIEW_TEXT, sendText);
        List<SmsPreview> smsPreviewList = canSend.stream().map(item -> new SmsPreview(sendText, item)).collect(Collectors.toList());
        intent.putExtra(PREVIEW_LIST, JSONObject.toJSONString(smsPreviewList));
        startActivity(intent);
    }

    private Map<String, SmsPerson> getCachePerson() {
        try {
            List<SmsPerson> list = DbKit.INSTANCE.getDb().findAll(SmsPerson.class);
            if (list != null) {
                return list.stream()
                        .filter(item -> !TextUtils.isEmpty(item.getTelPhone()))
                        .collect(Collectors.toMap(SmsPerson::getTelPhone, Function.identity(), (key1, key2) -> key2));
            }
        } catch (DbException e) {
            Log.e(TAG, "getCachePerson: " + e.getLocalizedMessage());
        }
        return Collections.emptyMap();
    }

    private void saveEditText(String text) {
        SharedPrefsKit.INSTANCE.saveValue(this, EDIT_TEXT, text);
    }

    private String loadEditText() {
        return SharedPrefsKit.INSTANCE.getStr(this, EDIT_TEXT, "");
    }

    public static class SortChineseName implements Comparator<SmsPerson> {

        Collator cmp = Collator.getInstance(java.util.Locale.CHINA);

        @Override
        public int compare(SmsPerson o1, SmsPerson o2) {
            if (cmp.compare(o1.getName(), o2.getName()) > 0) {
                return 1;

            } else if (cmp.compare(o1.getName(), o2.getName()) < 0) {
                return -1;
            }
            return 0;
        }
    }
}
