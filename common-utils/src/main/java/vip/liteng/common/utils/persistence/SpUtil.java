package vip.liteng.common.utils.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.Map;
import java.util.Set;

import vip.liteng.common.utils.base.ApplicationUtil;

/**
 * SharedPreferences 工具类，不支持多进程
 * 提供自定义 SharedPreferences 文件的名称;init(Context);init(Context,String)
 * <p>
 * 同时提供 putCommit() 和 putApply() 两种方式存储数据
 * <p>
 * putCommit() 使用的是SharedPreferences.Editor.commit() 提交数据，该操作是同步，会返回该操作的结果
 * <p>
 * putApply() 使用的是SharedPreferences.Editor.apply() 提交数据，该操作是异步;它会立即在内存中修改你的提交，然后开启一个异步的提交去修改磁盘上数据，但是它只管执行，如果遇到也不会返回任何错误信息。
 * 如果其他的Editor对SharedPreferences有个commit提交，只能等到当前的apply()操作完成之后才会执行。
 * 由于 SharedPreferences 在当前进程中只会存在一个实例，如果不关心返回值，完全可以使用apply()替换掉commit().不用担心其它组件的生命周期的切换会对apply()有影响，系统确定会在组件切换生命周期前完全执行完apply()
 *
 * @author RickyLi
 */
public class SpUtil {

    private static SpUtil sUtil;
    private static Context sContext = ApplicationUtil.getApplication();
    private static String sCurrentSpName;
    private static SharedPreferences sPreferences;
    private static SharedPreferences.Editor sEditor;

    private SpUtil(Context context) {
        this(context, null);
    }

    private SpUtil(Context context, String spName) {
        sContext = context.getApplicationContext();

        // 如果不指定 SharedPreferences 的名字，则以 "包名__preferences" 方式存储
        if (TextUtils.isEmpty(spName)) {
            sPreferences = PreferenceManager.getDefaultSharedPreferences(sContext);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sCurrentSpName = PreferenceManager.getDefaultSharedPreferencesName(sContext);
            } else {
                sCurrentSpName = sContext.getPackageName() + "_preferences";
            }
        } else {
            sPreferences = sContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
            sCurrentSpName = spName;
        }

        sEditor = sPreferences.edit();
    }

    /**
     * 将SpUtil 注册到 app中，这是使用该工具的第一步。
     *
     * @param context 上下文对象
     */
    public static void register(Context context) {
        sContext = context.getApplicationContext();
    }

    /**
     * 如果不指定 SharedPreferences 的名字，则以 包名__preferences 方式存储
     *
     * @return SpUtil 的引用
     */
    public static SpUtil init() {
        checkNull();

        if (sUtil == null) {
            sUtil = new SpUtil(sContext);
        }

        return sUtil;
    }

    public static SpUtil init(String spName) {
        checkNull();

        if (sUtil == null) {
            sUtil = new SpUtil(sContext, spName);
        } else if (!TextUtils.equals(sCurrentSpName, spName)) {
            sUtil = new SpUtil(sContext, spName);
        }

        return sUtil;
    }

    public static String getCurrentSpName() {
        return sCurrentSpName;
    }

    public void clearApply() {
        sEditor.clear().apply();
    }

    public boolean clearCommit() {
        return sEditor.clear().commit();
    }

    public void removeApply(String key) {
        sEditor.remove(key).apply();
    }

    public boolean removeCommit(String key) {
        return sEditor.remove(key).commit();
    }

    public Map<String, ?> getAll() {
        return sPreferences.getAll();
    }

    public String get(String key, String defaultValue) {
        return sPreferences.getString(key, defaultValue);
    }

    public int get(String key, int defaultValue) {
        return sPreferences.getInt(key, defaultValue);
    }

    public boolean get(String key, boolean defaultValue) {
        return sPreferences.getBoolean(key, defaultValue);
    }

    public float get(String key, float defaultValue) {
        return sPreferences.getFloat(key, defaultValue);
    }

    public long get(String key, long defaultValue) {
        return sPreferences.getLong(key, defaultValue);
    }

    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return sPreferences.getStringSet(key, defaultValue);
    }

    public SpUtil putApply(String key, String value) {
        sEditor.putString(key, value);
        sEditor.apply();
        return sUtil;
    }

    public SpUtil putApply(String key, int value) {
        sEditor.putInt(key, value);
        sEditor.apply();
        return sUtil;
    }

    public SpUtil putApply(String key, boolean value) {
        sEditor.putBoolean(key, value);
        sEditor.apply();
        return sUtil;
    }

    public SpUtil putApply(String key, float value) {
        sEditor.putFloat(key, value);
        sEditor.apply();
        return sUtil;
    }

    public SpUtil putApply(String key, long value) {
        sEditor.putLong(key, value);
        sEditor.apply();
        return sUtil;
    }

    public SpUtil putApply(String key, Set<String> value) {
        sEditor.putStringSet(key, value);
        sEditor.apply();
        return sUtil;
    }

    public boolean putCommit(String key, String value) {
        sEditor.putString(key, value);
        return sEditor.commit();
    }

    public boolean putCommit(String key, int value) {
        sEditor.putInt(key, value);
        return sEditor.commit();
    }

    public boolean putCommit(String key, boolean value) {
        sEditor.putBoolean(key, value);
        return sEditor.commit();
    }

    public boolean putCommit(String key, float value) {
        sEditor.putFloat(key, value);
        return sEditor.commit();
    }

    public boolean putCommit(String key, long value) {
        sEditor.putLong(key, value);
        return sEditor.commit();
    }

    public boolean putCommit(String key, Set<String> value) {
        sEditor.putStringSet(key, value);
        return sEditor.commit();
    }

    private static void checkNull() {
        if (sContext == null) {
            throw new NullPointerException(
                    "请先在 Application 里的 onCreate() 调用 SpUtil.register(Context) , 对 SpUtil 进行注册");
        }
    }

}
