package vip.liteng.common.utils.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author liteng
 */
public class DeviceInfo {

    private static final String TAG = "DeviceInfo";

    public static class BuildInfo {

        public static String getOSVersion() {
            return Build.VERSION.RELEASE;
        }

        public static String getBoard() {
            return Build.BOARD;
        }

        public static String getManufacturer() {
            return Build.MANUFACTURER;
        }

        public static String getDevice() {
            return Build.DEVICE;
        }

        public static String getBootLoader() {
            return Build.BOOTLOADER;
        }

        public static String getType() {
            return Build.TYPE;
        }

        /**
         * 当前设设备的运行的SDK的版本号
         *
         * @return SDK 版本号
         */
        public static int getSdkVersion() {
            return Build.VERSION.SDK_INT;
        }

        public static String getModel() {
            return Build.MODEL;
        }

        public static String getBrand() {
            return Build.BRAND;
        }

        public static String getRadioVersion() {
            return Build.getRadioVersion();
        }

        public static String getDisplay() {
            return Build.DISPLAY;
        }

        public static String getFingerPrint() {
            return Build.FINGERPRINT;
        }

        public static String getCpuABI() {
            return Build.CPU_ABI;
        }

        public static String getCpuABI2() {
            return Build.CPU_ABI2;
        }

        public static String getHardware() {
            return Build.HARDWARE;
        }

        public static String getHost() {
            return Build.HOST;
        }

        public static String getProduct() {
            return Build.PRODUCT;
        }

        public static String getTags() {
            return Build.TAGS;
        }

        public static String getUser() {
            return Build.USER;
        }

        public static String getId() {
            return Build.ID;
        }

        public static String[] getSupportedABIS() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return Build.SUPPORTED_ABIS;
            }
            return null;
        }

        public static Map<String, String> allBuildInfos() {
            Map<String, String> allBuildInfos = new HashMap<>(65);

            Field[] fields = Build.class.getDeclaredFields();
            try {
                for (Field field : fields) {
                    field.setAccessible(true);

                    Object value = field.get(null);

                    if (value instanceof String[]) {

                        StringBuilder valueBuilder = new StringBuilder("[");
                        String[] values = (String[]) value;
                        int length = values.length;
                        int lastIndex = length - 1;
                        for (String v : values) {
                            valueBuilder.append(v);
                            if (v != values[lastIndex]) {
                                valueBuilder.append(" , ");
                            }
                        }

                        valueBuilder.append("]");
                        value = valueBuilder;
                    }
                    allBuildInfos.put(field.getName(), value.toString());
                    Log.d(TAG, field.getName() + " : " + field.get(null));
                }
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect device info", e);
            }

            return allBuildInfos;
        }

    }

    /**
     * 获取android当前可用内存大小
     */
    public static String getAvailMemory(Context context) {
        ActivityManager.MemoryInfo memoryInfo = getMemoryInfo(context);
        return Formatter.formatFileSize(context, memoryInfo.availMem);
    }

    /**
     * 获得系统总内存
     */
    public static String getTotalMemory(Context context) {
        ActivityManager.MemoryInfo memoryInfo = getMemoryInfo(context);
        return Formatter.formatFileSize(context, memoryInfo.totalMem);
    }

    @NonNull
    private static ActivityManager.MemoryInfo getMemoryInfo(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    public interface NetType {
        int NONE = 0;
        int WIFI = 0x001;
        int MOBILE = 0x002;
        int OTHER = 0x003;
    }

    public static String getDefaultLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getConnectionInfo().getMacAddress();
    }

    public static boolean isNetOk(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Network network = manager.getActiveNetwork();
            NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(network);
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        } else {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isConnected();
            }
        }

        return false;
    }

    /**
     * 获取当前的网络状态
     *
     * @return {@link NetType#NONE}:没有网络 ; {@link NetType#WIFI}:WIFI ; {@link NetType#MOBILE}:Mobile ; {@link NetType#OTHER}:Other
     * @see NetType
     */
    public static int getNetType(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getNetTypeByNetworkCapabilities(context);
        } else {
            return getNetTypeByNetworkInfo(context);
        }
    }

    /**
     * 获取当前的网络状态
     * ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     *
     * @return 0:没有网络
     * 1:WIFI
     * 2:Mobile
     * 4:Other
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static int getNetTypeByNetworkCapabilities(Context context) {
        if (!isNetOk(context)) {
            return NetType.NONE;
        }

        ConnectivityManager manager = getConnectivityManager(context);
        Network network = manager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(network);
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return NetType.WIFI;
        }

        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return NetType.MOBILE;
        }

        return NetType.OTHER;
    }

    /**
     * 获取当前的网络状态
     * ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     *
     * @return 0:没有网络
     * 1:WIFI
     * 2:Mobile
     * 4:Other
     */
    private static int getNetTypeByNetworkInfo(Context context) {
        if (!isNetOk(context)) {
            return NetType.NONE;
        }

        ConnectivityManager manager = getConnectivityManager(context);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            return NetType.MOBILE;
        }
        return NetType.OTHER;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    public static String getImei(Context context) {
        String imei = null;
        try {
            TelephonyManager tm = getTelephonyManager(context);
            if (needRequestPermission()) {
                if (readPhoneGranted(context)) {
                    imei = tm.getImei();
                }
            } else {
                imei = tm.getImei();
            }
        } catch (Exception e) {
            Log.e("getImei", e.getMessage());
        }

        return imei;
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        String deviceId = null;
        try {
            TelephonyManager tm = getTelephonyManager(context);
            if (needRequestPermission()) {
                if (readPhoneGranted(context)) {
                    deviceId = tm.getDeviceId();
                }
            } else {
                deviceId = tm.getDeviceId();
            }
        } catch (Exception e) {
        }
        return deviceId;
    }

    @SuppressLint("MissingPermission")
    public static String getSimSerialNumber(Context context) {
        String simSerialNumber = null;
        try {
            TelephonyManager tm = getTelephonyManager(context);
            if (needRequestPermission()) {
                if (readPhoneGranted(context)) {
                    simSerialNumber = tm.getSimSerialNumber();
                }
            } else {
                simSerialNumber = tm.getSimSerialNumber();
            }
        } catch (Exception e) {
        }
        return simSerialNumber;
    }

    public static boolean needRequestPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    private static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    private static boolean readPhoneGranted(Context context) {
        return ActivityCompat.checkSelfPermission(
                context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

}
