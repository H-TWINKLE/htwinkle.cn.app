package cn.htwinkle.app.view.app;

import android.webkit.HttpAuthHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.just.agentweb.AgentWebView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.htwinkle.app.R;
import cn.htwinkle.app.constants.Constants;
import cn.htwinkle.app.view.base.BaseActivity;
import cn.hutool.core.util.StrUtil;

@ContentView(R.layout.activity_web_view)
public class CommWebViewActivity extends BaseActivity {

    @ViewInject(R.id.comm_web_view_sv)
    private AgentWebView comm_web_view_sv;

    @Override
    public void initData() {
        setToolBarTitle("详情");

        String title = getIntentTitle();
        if (StrUtil.isNotEmpty(title)) {
            setToolBarTitle(title);
        }
        loadAnim(R.anim.anim_slide_in_bottom, R.anim.no_anim);
    }

    @Override
    public void initView() {
        initWebView();

        String intentHome = getIntentHome();
        comm_web_view_sv.loadUrl(intentHome);
    }

    /**
     * 初始化浏览器
     */
    private void initWebView() {

        comm_web_view_sv.getSettings().setSupportZoom(true);
        comm_web_view_sv.getSettings().setBuiltInZoomControls(true);
        comm_web_view_sv.getSettings().setDisplayZoomControls(true);

        comm_web_view_sv.getSettings().setLoadWithOverviewMode(true);
        comm_web_view_sv.getSettings().setUseWideViewPort(true);
        comm_web_view_sv.getSettings().setJavaScriptEnabled(true);
        comm_web_view_sv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        comm_web_view_sv.getSettings().setDomStorageEnabled(true);
        comm_web_view_sv.getSettings().setDatabaseEnabled(true);

        comm_web_view_sv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                comm_web_view_sv.loadUrl(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                String intentAuth = getIntentAuth();
                if (StrUtil.isNotEmpty(intentAuth)) {
                    String[] split = intentAuth.split(":");
                    handler.proceed(split[0], split[1]);
                    return;
                }
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        loadAnim(R.anim.no_anim, R.anim.anim_slide_out_bottom);
    }

    private String getIntentAuth() {
        return getIntent().getStringExtra(Constants.WEB_VIEW_SRS_AUTH);
    }

    private String getIntentHome() {
        return getIntent().getStringExtra(Constants.WEB_VIEW_HOME);
    }

    private String getIntentTitle() {
        return getIntent().getStringExtra(Constants.WEB_VIEW_TITLE);
    }
}
