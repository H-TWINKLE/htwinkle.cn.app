package cn.htwinkle.app.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import cn.htwinkle.app.R;
import cn.htwinkle.app.entity.OnlineStream;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;

public class OnlineStreamAdapter extends BaseQuickAdapter<OnlineStream.StreamsDTO, BaseViewHolder> {

    private static final String TAG = "OnlineStreamAdapter";

    private final Activity activity;

    public OnlineStreamAdapter(int layoutResId, Activity activity) {
        super(layoutResId);
        this.activity = activity;
    }

    @Override
    protected void convert(@NonNull final BaseViewHolder holder, final OnlineStream.StreamsDTO streamsDTO) {
        String name = streamsDTO.getName();
        if (StrUtil.isNotEmpty(name)) {
            String[] split = name.split("_");
            holder.setText(R.id.item_shared_live_index_tv, String.valueOf(getItemPosition(streamsDTO) + 1));
            holder.setText(R.id.item_shared_live_name_tv, split[0]);
            holder.setText(R.id.item_shared_live_device_tv, split.length > 1 ? split[1] : "");
            holder.setText(R.id.item_sms_shared_live_device_name_tv, DateTime.of(streamsDTO.getLiveMs()).toString(DatePattern.NORM_DATETIME_PATTERN));
        }
    }
}
