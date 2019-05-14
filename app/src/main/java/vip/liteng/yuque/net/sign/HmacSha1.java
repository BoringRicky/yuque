package vip.liteng.yuque.net.sign;

import android.text.TextUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import vip.liteng.yuque.Constants;

/**
 * @author LiTeng
 * @date 2019-05-10
 */
public class HmacSha1 extends AbstractSign {

    public static String signSha1(String paramString) {
        AbstractSign sign = new HmacSha1();
        return sign.sign(paramString);
    }

    @Override
    public String getSignType() {
        return HMAC_SHA1;
    }

    @Override
    public String sign(String paramString) {
        String sha1 = null;
        if (!TextUtils.isEmpty(paramString)) {
            byte[] key = Constants.CLIENT_SECRET.getBytes();
            byte[] data = paramString.getBytes();
            String encode = encode(key, data);
            sha1 = base64(encode.getBytes());
        }
        return sha1;
    }

    private String encode(byte[] key, byte[] data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, getSignType());
            Mac mac = Mac.getInstance(getSignType());
            mac.init(secretKeySpec);

            byte[] bytes = mac.doFinal(data);
            return byte2hex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String temp;
        for (int n = 0; b != null && n < b.length; n++) {
            temp = Integer.toHexString(b[n] & 0XFF);
            if (temp.length() == 1) {
                hs.append('0');
            }
            hs.append(temp);
        }
        return hs.toString();
    }
}

