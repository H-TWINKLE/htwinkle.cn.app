package cn.htwinkle.app.adapter;

import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.xutils.x;

import java.io.InputStream;

import cn.htwinkle.app.R;
import cn.htwinkle.app.entity.AppInfo;
import cn.htwinkle.app.kit.CommKit;
import cn.htwinkle.app.kit.StrKit;
import cn.hutool.http.HttpConfig;
import cn.hutool.http.HttpRequest;

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
            x.task().run(() -> getPic(holder, appInfo));

        } else if (appInfo.getResourcesId() != 0) {
            holder.setImageResource(R.id.item_app_info_img_siv, appInfo.getResourcesId());
        } else {
            holder.setImageResource(R.id.item_app_info_img_siv, R.drawable.base_empty);
        }
    }

    private static void getPic(@NonNull BaseViewHolder holder, AppInfo appInfo) {
        String imgUrl = appInfo.getImgUrl() + "&t=" + System.currentTimeMillis() + "&proxy=false";
        String realUrl = getRealUrl(imgUrl);

        InputStream stream = HttpRequest.get(realUrl)
                .header("Referer", "https://desk.zol.com.cn/")
                .execute()
                .bodyStream();

        ImageView view = holder.getView(R.id.item_app_info_img_siv);
        x.task().post(() -> view.setImageBitmap(BitmapFactory.decodeStream(stream)));
    }

    private static String getRealUrl(String imgUrl) {
        HttpRequest httpRequest = HttpRequest.get(imgUrl);
        httpRequest.setConfig(new HttpConfig().setMaxRedirectCount(10));
        CommKit.safety(() -> {
            httpRequest.execute();
        }, true);
        return httpRequest.getUrl();
    }
}
