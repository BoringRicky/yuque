package vip.liteng.yuque.net.sign;

import android.util.Base64;

import java.util.Map;

/**
 * @author LiTeng
 * @date 2019-05-10
 */
public abstract class AbstractSign {

    public static final String HMAC_SHA1 = "HmacSHA1";

    /**
     * 签名类型
     *
     * @return type
     */
    protected abstract String getSignType();

    /**
     * 签名过程
     *
     * @param params 需要签名的参数
     * @return 签名后的字符串
     */
    protected abstract String sign(Map<String, String> params);

    protected String base64(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

}
