package vip.liteng.yuque.base;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import vip.liteng.common.utils.LifecycleHandler;
import vip.liteng.log.Logger;

/**
 * @author LiTeng
 * @date 2019-05-10
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected String mTag = getClass().getName();

    protected LifecycleHandler mLifecycleHandler;

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        mLifecycleHandler = new LifecycleHandler(this);

        ButterKnife.bind(this);

        Logger.tag(mTag);
        Logger.i("onCreate");

        created();
    }

    /**
     * 用来给子类的获取 Activity 的 contentView
     *
     * @return view id
     */
    protected abstract int getContentViewId();

    /**
     * onCreate 调用完毕
     */
    protected abstract void created();

    @CallSuper
    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.i("onRestart");
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        Logger.i("onStart");
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        Logger.i("onResume");
    }

    @Override
    @CallSuper
    protected void onPause() {
        super.onPause();
        Logger.i("onPause");
    }

    @CallSuper
    @Override
    protected void onStop() {
        super.onStop();
        Logger.i("onStop");
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        Logger.i("onDestroy");
    }

}
