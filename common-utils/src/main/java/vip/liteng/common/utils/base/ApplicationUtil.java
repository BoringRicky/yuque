package vip.liteng.common.utils.base;

import android.app.Application;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 一个可以设置、获取全局 Application 的工具类。
 * Context 在 Android 中非常常用，但是其实很多时候我们不需要传递 Activity 类型的 Context ，
 * 使用 ApplicationUtils 可以缓解我们到处传递 Context 的困境。 默认会从 ActivityThread 获取 Application）
 * <p>
 *
 * @author AlanCheen
 * @date 2019/4/23
 * @see <a href="https://github.com/AlanCheen/Pandora/blob/master/pandora-basic/src/main/java/me/yifeiyuan/pandora/ApplicationUtils.java">程序亦非猿的Pandora</a>
 */
public class ApplicationUtil {

    private static final String ACTIVITY_THREAD = "android.app.ActivityThread";
    private static final String INITIAL_APPLICATION = "mInitialApplication";
    private static final String CURRENT_ACTIVITY_THREAD = "currentActivityThread";

    private static Application sApplication;

    static {
        try {
            Class<?> activityThread = Class.forName(ACTIVITY_THREAD);
            Method m_currentActivityThread = activityThread.getDeclaredMethod(
                    CURRENT_ACTIVITY_THREAD);
            Field f_mInitialApplication = activityThread.getDeclaredField(INITIAL_APPLICATION);
            f_mInitialApplication.setAccessible(true);
            Object current = m_currentActivityThread.invoke(null);
            Object app = f_mInitialApplication.get(current);
            sApplication = (Application) app;
        } catch (Exception e) {
            //            e.printStackTrace();
        }
    }

    public static Application getApplication() {
        return sApplication;
    }

    public static void setApplication(Application application) {
        sApplication = application;
    }

    private ApplicationUtil() {
    }
}
