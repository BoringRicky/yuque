package vip.liteng.log;

import timber.log.Timber;

/**
 * @author LiTeng
 * @date 2019-05-10
 */
public class Logger {

    private static final boolean DEBUG = true;

    private static String sTag;

    private Logger() {}

    public static void register() {
        Timber.plant(new Timber.DebugTree());
    }

    public static void tag(String tag) {
        sTag = tag;
    }

    public static void v(String message, Object... objects) {
        if (!DEBUG) {
            return;
        }
        Timber.tag(sTag).v(message, objects);
    }

    public static void i(String message, Object... objects) {
        if (!DEBUG) {
            return;
        }
        Timber.tag(sTag).i(message, objects);
    }

    public static void d(String message, Object... objects) {
        if (!DEBUG) {
            return;
        }
        Timber.tag(sTag).d(message, objects);
    }

    public static void w(String message, Object... objects) {
        if (!DEBUG) {
            return;
        }
        Timber.tag(sTag).w(message, objects);
    }

    public static void e(String message, Object... objects) {
        if (!DEBUG) {
            return;
        }
        Timber.tag(sTag).e(message, objects);
    }

    public static void e(Throwable throwable) {
        if (!DEBUG) {
            return;
        }
        Timber.tag(sTag).e(throwable);
    }

    public static void e(Throwable throwable, String message, Object... objects) {
        if (!DEBUG) {
            return;
        }
        Timber.tag(sTag).e(throwable, message, objects);
    }

}
