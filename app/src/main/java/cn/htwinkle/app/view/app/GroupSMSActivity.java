package cn.htwinkle.app.view.app;

import android.os.Handler;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yanzhenjie.permission.runtime.Permission;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import cn.htwinkle.app.R;
import cn.htwinkle.app.adapter.SmsPersonAdapter;
import cn.htwinkle.app.annotation.AppModule;
import cn.htwinkle.app.entity.SmsPerson;
import cn.htwinkle.app.kit.DbKit;
import cn.htwinkle.app.kit.PhoneKit;
import cn.htwinkle.app.kit.SharedPrefsKit;
import cn.htwinkle.app.view.base.BaseRefreshActivity;

@AppModule(value = GroupSMSActivity.TITLE,
        description = "自定义称谓的群发信息",
        imgResourcesId = R.drawable.welcome_default_pic,
        permissions = {Permission.SEND_SMS, Permission.RECEIVE_SMS, Permission.READ_SMS,
                Permission.RECEIVE_WAP_PUSH, Permission.RECEIVE_MMS, Permission.READ_CONTACTS}
)
@ContentView(R.layout.base_recycler_with_toolbar)
public class GroupSMSActivity extends BaseRefreshActivity<SmsPerson, SmsPersonAdapter> {

    public static final String TITLE = "群发短信";
    public static final String EDIT_TEXT = "EDIT_TEXT";

    private EditText base_rich_text_et;
    private FloatingActionButton base_rich_fab;
    private final Handler handler = new android.os.Handler();

    @Override
    public void finish() {
        super.finish();
        loadAnim(R.anim.no_anim, R.anim.anim_slide_out_bottom);
    }

    @Override
    public void initData() {
        setToolBarTitle(TITLE);
        adapter = new SmsPersonAdapter(R.layout.item_sms_person);
        adapter.addHeaderView(initHeaderView1());
        adapter.addChildClickViewIds(R.id.item_sms_person_cb);
        loadAnim(R.anim.anim_slide_in_bottom, R.anim.no_anim);
        getData();
    }

    @Override
    public void getData() {
        combineData();
    }

    /**
     * 邀请好友
     *
     * @return View
     */
    private View initHeaderView1() {
        View headerView = View.inflate(this, R.layout.item_sms_edit, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        base_rich_text_et = headerView.findViewById(R.id.base_rich_text_et);
        base_rich_text_et.setText(loadEditText());
        base_rich_text_et.addTextChangedListener(new TextWatcher() {
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
            if (TextUtils.isEmpty(base_rich_text_et.getText())) {
                setToastString("请填写内容");
                return;
            }
            List<SmsPerson> canSend =
                    this.adapter.getData()
                            .stream()
                            .filter(item -> item.isChecked() && !TextUtils.isEmpty(item.getName()))
                            .collect(Collectors.toList());
            if (canSend.size() > 0) {
                toSendMessage(canSend);
                return;
            }
            setToastString("请勾选需要发送的联系人");
        });
        return headerView;
    }

    private void toSendMessage(List<SmsPerson> canSend) {
        base_rich_fab.setEnabled(false);
        String sendText = base_rich_text_et.getText().toString();
        for (int x = 0; x < canSend.size(); x++) {
            SmsPerson smsPerson = canSend.get(x);
            sendSms(x + 1, canSend.size(), smsPerson.getName(), smsPerson.getTelPhone(),
                    smsPerson.getName() + sendText);
        }
    }

    /**
     * 组合数据和缓存数据
     */
    private void combineData() {
        List<SmsPerson> smsPeople = PhoneKit.INSTANCE.getPhone(this).stream()
                .sorted((t0, t1) -> {
                    if (TextUtils.isEmpty(t0.getName()) || TextUtils.isEmpty(t1.getName())) {
                        return -1;
                    }
                    return getPinYinName(t0.getName()).compareTo(getPinYinName(t1.getName()));
                }).collect(Collectors.toList());
        smsPeople = smsPeople.stream().filter(item ->
                !TextUtils.isEmpty(item.getName()) && item.getName().length() < 4
        ).collect(Collectors.toList());
        List<SmsPerson> cachePeople = getCachePerson();
        for (int smsIndex = 0; smsIndex < smsPeople.size(); smsIndex++) {
            SmsPerson smsPerson = smsPeople.get(smsIndex);
            for (int cacheIndex = 0; cacheIndex < cachePeople.size(); cacheIndex++) {
                SmsPerson cachePerson = cachePeople.get(cacheIndex);
                if (!TextUtils.isEmpty(smsPerson.getTelPhone()) &&
                        smsPerson.getTelPhone().equals(cachePerson.getTelPhone())) {
                    smsPeople.set(smsIndex, cachePerson);
                }
            }
        }
        smsPeople = smsPeople
                .stream()
                .sorted(Comparator.comparing(SmsPerson::isChecked).reversed())
                .collect(Collectors.toList());
        onSuccessGetData(smsPeople);
    }

    private List<SmsPerson> getCachePerson() {
        try {
            List<SmsPerson> list = DbKit.INSTANCE.getDb().findAll(SmsPerson.class);
            if (list != null) {
                return list;
            }
        } catch (DbException e) {
            Log.e(TAG, "getCachePerson: " + e.getLocalizedMessage());
        }
        return new ArrayList<>();
    }

    private void saveEditText(String text) {
        SharedPrefsKit.INSTANCE.saveValue(this, EDIT_TEXT, text);
    }

    private String loadEditText() {
        return SharedPrefsKit.INSTANCE.getStr(this, EDIT_TEXT, "");
    }

    /**
     * 发送短信服务
     *
     * @param name name
     * @param tel  tel
     * @param text text
     */
    private void sendSms(int index, int allCount, String name, String tel, String text) {
        try {
            SmsManager manager = SmsManager.getDefault();
            ArrayList<String> strings = manager.divideMessage(text);
            handler.postDelayed(() -> {
                for (int i = 0; i < strings.size(); i++) {
                    manager.sendTextMessage(tel, null, strings.get(i), null, null);
                }
                setToastString(getSenderToast(index, allCount, name) + "成功 " + tel);
                if (index == allCount) {
                    setToastString(String.format("发送完成，合计发送 %s 条", allCount));
                    base_rich_fab.setEnabled(true);
                }
            }, 2000);
        } catch (Exception e) {
            setToastString(getSenderToast(index, allCount, name) + "失败：" + e.getLocalizedMessage());
        }
    }

    /**
     * 发送信息提示
     *
     * @param index    index
     * @param allCount allCount
     * @param name     name
     * @return String
     */
    @NonNull
    private String getSenderToast(int index, int allCount, String name) {
        return index + "/" + allCount + " . " + name + " 发送短信";
    }

    private String getPinYinName(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        return getFirstLetter(text);
    }

    /**
     * 取第一个汉字的第一个字符
     *
     * @return String
     */
    public static String getFirstLetter(String ChineseLanguage) {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 输出拼音全部大写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        try {
            String str = String.valueOf(cl_chars[0]);
            if (str.matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                hanyupinyin = PinyinHelper.toHanyuPinyinStringArray(
                        cl_chars[0], defaultFormat)[0].substring(0, 1);
            } else if (str.matches("[0-9]+")) {// 如果字符是数字,取数字
                hanyupinyin += cl_chars[0];
            } else if (str.matches("[a-zA-Z]+")) {// 如果字符是字母,取字母

                hanyupinyin += cl_chars[0];
            }
        } catch (BadHanyuPinyinOutputFormatCombination ignored) {
        }
        return hanyupinyin;
    }
}
