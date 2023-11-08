package cn.htwinkle.app.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import cn.htwinkle.app.entity.ShareLive;

public class SharedLiveAdapter extends BaseQuickAdapter<ShareLive, BaseViewHolder> {
    public SharedLiveAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ShareLive shareLive) {

    }
}
