package cn.htwinkle.app.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.xutils.x;

import cn.htwinkle.app.R;
import cn.htwinkle.app.entity.AppInfo;
import cn.htwinkle.app.kit.StrKit;

public class AppInfoAdapter extends BaseQuickAdapter<AppInfo, BaseViewHolder> {
    public AppInfoAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, AppInfo appInfo) {
        holder.setText(R.id.item_app_info_name_tv, StrKit.safetyText(appInfo.getName()));
        holder.setText(R.id.item_app_info_description_tv, StrKit.safetyText(appInfo.getDescription()));

        // 设置显示信息
        if (!TextUtils.isEmpty(appInfo.getImgUrl())) {
            x.image().bind(holder.getView(R.id.item_app_info_img_siv), appInfo.getImgUrl());
        } else if (appInfo.getResourcesId() != 0) {
            holder.setImageResource(R.id.item_app_info_img_siv, appInfo.getResourcesId());
        } else {
            holder.setImageResource(R.id.item_app_info_img_siv, R.drawable.base_empty);
        }
    }
}
