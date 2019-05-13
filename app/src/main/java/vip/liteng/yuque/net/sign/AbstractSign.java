package vip.liteng.yuque.net.sign;

import android.util.Base64;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author LiTeng
 * @date 2019-05-10
 */
public abstract class AbstractSign {

    public static final char[] DIGITS_LOWER = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static final String HMAC_SHA1 = "HmacSHA1";

    public static final int CODE_LEN = 40;

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
    protected abstract String sign(Map<String, Object> params);

    protected String base64(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static String generateCode() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        int len = uuid.length();

        if (len > CODE_LEN) {
            uuid = uuid.substring(0, CODE_LEN);
        }

        if (len < CODE_LEN) {
            uuid += randomString(CODE_LEN - len, DIGITS_LOWER);
        }

        return uuid;
    }

    private static String randomString(int len, char[] digits) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(digits.length);
            builder.append(digits[index]);
        }

        return builder.toString();
    }

}
