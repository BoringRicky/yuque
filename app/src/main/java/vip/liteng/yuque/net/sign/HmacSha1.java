package vip.liteng.yuque.net.sign;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import vip.liteng.log.Logger;
import vip.liteng.yuque.Constants;
import vip.liteng.yuque.net.ParamBuilder;

/**
 * @author LiTeng
 * @date 2019-05-10
 */
public class HmacSha1 extends AbstractSign {

    public static String signSha1(Map<String, Object> params) {
        AbstractSign sign = new HmacSha1();
        return sign.sign(params);
    }

    @Override
    public String getSignType() {
        return HMAC_SHA1;
    }

    @Override
    public String sign(Map<String, Object> params) {
        String sha1 = null;
        //Sort data
        if (params != null) {
            try {

                String sortedParams = ParamBuilder.sortSignParams(params);

                Logger.e("排序后的Sign参数 = " + sortedParams);

                byte[] key = Constants.CLIENT_SECRET.getBytes(Constants.UTF8);
                byte[] data = sortedParams.getBytes(Constants.UTF8);
                String encode = encode(key, data);

                Logger.e("Sha1 后 = " + encode);

                sha1 = base64(encode.getBytes(Constants.UTF8));

                Logger.e("Base64 后 = " + sha1);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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

    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString();
    }
}

