package vip.liteng.yuque;

import android.app.Application;

import vip.liteng.log.Logger;

/**
 * @author LiTeng
 * @date 2019-05-10
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.register();
    }
}
