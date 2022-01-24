package cn.htwinkle.app.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import cn.htwinkle.app.R;
import cn.htwinkle.app.entity.SmsPerson;
import cn.htwinkle.app.kit.StrKit;

public class SmsPersonAdapter extends BaseQuickAdapter<SmsPerson, BaseViewHolder> {
    public SmsPersonAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, SmsPerson smsPerson) {

        holder.setText(R.id.item_sms_person_index_tv, getItemPosition(smsPerson) + 1 + "");

        CheckBox checkBox = holder.getView(R.id.item_sms_person_cb);
        checkBox.setOnCheckedChangeListener(null);

        checkBox.setChecked(smsPerson.isChecked());
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            smsPerson.setChecked(isChecked);
            if (isChecked && !TextUtils.isEmpty(smsPerson.getName())) {
                smsPerson.saveSelfSafety();
            } else {
                smsPerson.deleteSelf();
            }
        });

        EditText nameEt = holder.getView(R.id.item_sms_person_name_et);
        nameEt.setText(StrKit.safetyText(smsPerson.getName()));
        nameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                smsPerson.setName(s.toString());
            }
        });


        EditText telEt = holder.getView(R.id.item_sms_person_tel_et);
        telEt.setText(StrKit.safetyText(smsPerson.getTelPhone()));
    }
}
