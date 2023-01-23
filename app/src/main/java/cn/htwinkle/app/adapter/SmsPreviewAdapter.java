package cn.htwinkle.app.adapter;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import cn.htwinkle.app.R;
import cn.htwinkle.app.entity.SmsPreview;

public class SmsPreviewAdapter extends BaseQuickAdapter<SmsPreview, BaseViewHolder> {

    private static final String TAG = "SmsPreviewAdapter";

    private final Activity activity;

    public SmsPreviewAdapter(int layoutResId, Activity activity) {
        super(layoutResId);
        this.activity = activity;
    }

    @Override
    protected void convert(@NonNull final BaseViewHolder holder, final SmsPreview smsPreview) {

        holder.setText(R.id.item_sms_preview_index_tv, getItemPosition(smsPreview) + 1 + "");
        holder.setText(R.id.item_sms_preview_text_tv, smsPreview.getSendText());
        holder.setText(R.id.item_sms_preview_tel_tv, smsPreview.getSmsPerson().getTelPhone());

        ImageView imageView = holder.getView(R.id.item_sms_preview_status_iv);
        TextView telTv = holder.getView(R.id.item_sms_preview_tel_tv);

        if (smsPreview.getStatus() == SmsPreview.SUCCESS) {
            telTv.setTextColor(activity.getResources().getColor(R.color.ok));
            imageView.setImageResource(R.drawable.round_check_circle_outline_light_green_a700_36dp);
        } else if (smsPreview.getStatus() == SmsPreview.FAILURE) {
            imageView.setImageResource(R.drawable.round_close_red_500_36dp);
        } else if (smsPreview.getStatus() == SmsPreview.ERROR) {
            imageView.setImageResource(R.drawable.round_error_deep_orange_800_36dp);
        } else {
            imageView.setImageBitmap(null);
            telTv.setTextColor(activity.getResources().getColor(R.color.black_description));
        }

    }
}
