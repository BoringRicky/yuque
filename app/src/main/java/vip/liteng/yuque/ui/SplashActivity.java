package vip.liteng.yuque.ui;

import android.os.Bundle;

import vip.liteng.yuque.R;
import vip.liteng.yuque.base.BaseActivity;
import vip.liteng.yuque.net.Url;
import vip.liteng.yuque.net.WebViewActivity;
import vip.liteng.yuque.utils.IntentUtil;

/**
 * 欢迎页
 *
 * @author RickyLee
 */
public class SplashActivity extends BaseActivity {

    public static final int DELAY = 1500;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void created() {
        mLifecycleHandler.postDelayed(() -> {
            Bundle args = new Bundle();
            args.putString(WebViewActivity.KEY_URL, Url.OAUTH2_NON_WEB);
//            args.putString(WebViewActivity.KEY_URL, Url.OAUTH2);
            IntentUtil.openActivity(this, WebViewActivity.class, args);
            finish();
        }, DELAY);

    }
}
