package cn.htwinkle.app.wrapper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebClientWrapper extends WebViewClient {

    private Activity activity;

    public WebClientWrapper(Activity activity) {
        this.activity = activity;
    }

    private static final String TAG = WebClientWrapper.class.toString();


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        WebView.HitTestResult hitTestResult = view.getHitTestResult();
        Log.i(TAG, "shouldOverrideUrlLoading: " + url);
        // view.loadUrl(url);
        openBrowser(url);
        return true;
    }

    private void openBrowser(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        activity.startActivity(intent);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.d(TAG, "onPageFinished: 浏览器加载页面完成：url: " + url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        //这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
        // view.loadUrl("file:///android_asset/error.html?realUrl=" + HttpConstant.DY_LAW_WEB_URL_NO_SPLIT);
    }
}
