package vip.liteng.yuque.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author LiTeng
 * @date 2019-05-13
 */
public class IntentUtil {

    public static void openActivity(Context context, Class<?> activityClass) {
        openActivity(context, activityClass, null);
    }

    public static void openActivity(Context context, Class<?> activityClass, Bundle bundle) {
        Intent intent = new Intent(context, activityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
