package vip.liteng.common.utils;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import vip.liteng.common.utils.base.DeviceInfo;

/**
 * @author liteng
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";

    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    private CrashHandler() {
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private static class InstanceHolder {
        private static CrashHandler INSTANCE = new CrashHandler();
    }

    public static CrashHandler getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e(TAG, assembleLog(e));
        // 根据具体需求写入文件或是上传云端
    }


    private String assembleLog(Throwable throwable) {
        String cause = getCause(throwable);

        StringBuilder builder = new StringBuilder();
        builder.append("[============================================================================================]");
        builder.append("\n");
        builder.append("\n *************");
        String time = getDateFormat().format(new Date(System.currentTimeMillis()));
        builder.append(time).append(" 遇到崩溃，设备信息如下：*************").append("\n");
        Map<String, String> allBuildInfos = DeviceInfo.BuildInfo.allBuildInfos();
        for (Map.Entry<String, String> entry : allBuildInfos.entrySet()) {
            builder.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }

        builder.append("*************崩溃信息如下：*************").append("\n");
        builder.append(cause);
        builder.append("\n");
        builder.append("[============================================================================================]");
        builder.append("\n\n\n");
        return builder.toString();
    }


    private String getCause(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        printWriter.close();
        return writer.toString();
    }


    private SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }


}
