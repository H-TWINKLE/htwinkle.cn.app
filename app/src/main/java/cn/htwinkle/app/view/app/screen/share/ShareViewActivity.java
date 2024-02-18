package cn.htwinkle.app.view.app.screen.share;

import android.graphics.Point;
import android.view.Display;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.just.agentweb.AgentWebView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.htwinkle.app.R;
import cn.htwinkle.app.constants.Constants;
import cn.htwinkle.app.entity.OnlineStream;
import cn.htwinkle.app.view.base.BaseActivity;
import cn.htwinkle.app.wrapper.VlcVideoLibraryWrapper;
import cn.hutool.core.util.StrUtil;

@ContentView(R.layout.activity_share_view)
public class ShareViewActivity extends BaseActivity {

    private VlcVideoLibraryWrapper wrapper = null;

    @ViewInject(R.id.share_view_sv)
    private AgentWebView share_view_sv;

    @ViewInject(R.id.share_view_cl)
    private CoordinatorLayout share_view_cl;

    @Override
    public void initData() {
        setToolBarTitle("详情");

        OnlineStream.StreamsDTO shareView = getContent(Constants.SHARE_VIEW);
        if (shareView != null) {
            setToolBarTitle(shareView.getName());
        }
        loadAnim(R.anim.anim_slide_in_bottom, R.anim.no_anim);
    }

    @Override
    public void initView() {
        OnlineStream.StreamsDTO shareView = getContent(Constants.SHARE_VIEW);

        String tcUrl = shareView.getTcUrl();
        String substring = tcUrl.substring(0, tcUrl.lastIndexOf("/"));
        String endPoint = substring + shareView.getUrl();

        initWebView();
        share_view_sv.loadUrl(StrUtil.format("http://htwinkle.cn:8080/players/srs_player.html?vhost=__defaultVhost__&app=live&stream={}.flv&server=htwinkle.cn&port=8080&autostart=true&schema=http", shareView.getName()));
    }

    /**
     * 初始化浏览器
     */
    private void initWebView() {

        share_view_sv.getSettings().setSupportZoom(true);
        share_view_sv.getSettings().setBuiltInZoomControls(true);
        share_view_sv.getSettings().setDisplayZoomControls(true);

        share_view_sv.getSettings().setLoadWithOverviewMode(true);
        share_view_sv.getSettings().setUseWideViewPort(true);
        share_view_sv.getSettings().setJavaScriptEnabled(true);
        share_view_sv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        share_view_sv.getSettings().setDomStorageEnabled(true);
        share_view_sv.getSettings().setDatabaseEnabled(true);
        share_view_sv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                share_view_sv.loadUrl(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }

    @NonNull
    private Point getPoint() {
        Display display = getWindowManager().getDefaultDisplay();
        // 方法一(推荐使用)使用Point来保存屏幕宽、高两个数据
        Point outSize = new Point();
        // 通过Display对象获取屏幕宽、高数据并保存到Point对象中
        display.getSize(outSize);
        return outSize;
    }

    @Override
    public void finish() {
        super.finish();
        loadAnim(R.anim.no_anim, R.anim.anim_slide_out_bottom);
    }

    private OnlineStream.StreamsDTO getContent(String key) {
        return (OnlineStream.StreamsDTO) getIntent().getSerializableExtra(key);
    }
}
