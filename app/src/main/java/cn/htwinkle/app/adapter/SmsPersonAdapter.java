package cn.htwinkle.app.adapter;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import cn.htwinkle.app.R;
import cn.htwinkle.app.entity.SmsPerson;
import cn.htwinkle.app.kit.StrKit;

public class SmsPersonAdapter extends BaseQuickAdapter<SmsPerson, BaseViewHolder> {

    private static final String TAG = "SmsPersonAdapter";

    private final Activity activity;

    public SmsPersonAdapter(int layoutResId, Activity activity) {
        super(layoutResId);
        this.activity = activity;
    }

    @Override
    protected void convert(@NonNull final BaseViewHolder holder, final SmsPerson smsPerson) {

        holder.setText(R.id.item_sms_person_index_tv, getItemPosition(smsPerson) + 1 + "");
        holder.setText(R.id.item_sms_person_tel_tv, StrKit.safetyText(smsPerson.getTelPhone()));

        CheckBox enableCb = holder.getView(R.id.item_sms_person_cb_enable);
        enableCb.setOnCheckedChangeListener(null);
        enableCb.setChecked(smsPerson.isChecked());
        enableCb.setTag(smsPerson);
        enableCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            smsPerson.setChecked(isChecked);
            saveInfo(smsPerson);
        });

        CheckBox backUpCb = holder.getView(R.id.item_sms_person_cb_back_up);
        backUpCb.setOnCheckedChangeListener(null);
        backUpCb.setChecked(smsPerson.isBackUp());
        backUpCb.setTag(smsPerson);
        backUpCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            smsPerson.setBackUp(isChecked);
            saveInfo(smsPerson);
        });

        TextView nameTv = holder.getView(R.id.item_sms_person_name_tv);
        nameTv.setText(StrKit.safetyText(smsPerson.getName()));

        TextView sendNameTv = holder.getView(R.id.item_sms_person_send_name_tv);
        sendNameTv.setText(StrKit.safetyText(smsPerson.getSendName()));
        sendNameTv.setOnClickListener(view -> {
            changeSendName(smsPerson, view);
        });
    }

    private void changeSendName(SmsPerson smsPerson, View view) {
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.base_dialog_edit, null);
        EditText sendNameEt = dialogView.findViewById(R.id.base_dialog_text_et);
        sendNameEt.setText(smsPerson.getSendName());
        sendNameEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        sendNameEt.setSelection(smsPerson.getSendName().length());
        sendNameEt.postDelayed(() -> {
            sendNameEt.requestFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(sendNameEt, InputMethodManager.SHOW_IMPLICIT);
        }, 100);

        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .setTitle("请输入发送名字")
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    activity.runOnUiThread(() -> {
                        smsPerson.setSendName(sendNameEt.getText().toString());
                        saveInfo(smsPerson);
                        notifyDataSetChanged();
                    });
                    dialogInterface.cancel();
                })
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.cancel())
                .create();

        alertDialog.show();
    }

    private void saveInfo(SmsPerson smsPerson) {
        if (!TextUtils.isEmpty(smsPerson.getSendName())) {
            smsPerson.saveSelfSafety();
        }

        if (!smsPerson.isChecked() && !smsPerson.isBackUp()) {
            smsPerson.deleteSelf();
        }

        // todo 备份用户信息到云端
    }
}
