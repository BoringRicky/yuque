package vip.liteng.yuque.net;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import vip.liteng.common.utils.base.ObjectsHelper;
import vip.liteng.log.Logger;
import vip.liteng.yuque.R;
import vip.liteng.yuque.base.BaseActivity;

/**
 * @author RickyLee
 */
public class WebViewActivity extends BaseActivity {

    private static final String TAG = "WebViewActivity";

    public static final String KEY_URL = "load_url";
    public static final int MAX_PROGRESS = 100;

    @BindView(R.id.wv_web_view) WebView mWebView;

    private Intent mIntent;
    private String mUrl;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void created() {
        Logger.tag(TAG);

        initUrl();

        setupWebView();

        load();

        addClients();
    }

    private void addClients() {
        mWebView.setWebChromeClient(new YuqueWebChromeClient());
        mWebView.setWebViewClient(new YuqueWebViewClient());
    }

    private void load() {
        mWebView.loadUrl(mUrl);
    }

    private void setupWebView() {
        WebSettings webSettings = mWebView.getSettings();
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //允许js代码
        webSettings.setJavaScriptEnabled(true);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);

        //禁用放缩
        webSettings.setDisplayZoomControls(false);
    }

    private void initUrl() {
        mIntent = getIntent();
        if (ObjectsHelper.isNull(mIntent)) {
            return;
        }

        mUrl = mIntent.getStringExtra(KEY_URL);
        if (TextUtils.isEmpty(mUrl)) {
            return;
        }
    }

    private class YuqueWebChromeClient extends WebChromeClient {
    }

    private class YuqueWebViewClient extends WebViewClient {
    }
}
