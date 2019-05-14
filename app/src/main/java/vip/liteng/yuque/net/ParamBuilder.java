package vip.liteng.yuque.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import vip.liteng.yuque.Constants;
import vip.liteng.yuque.net.sign.HmacSha1;

/**
 * @author LiTeng
 * @date 2019-05-13
 */
public class ParamBuilder {

    public static final int CODE_LEN = 40;
    public static final char[] DIGITS_LOWER = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static String sortSignParams(Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        Map<String, String> sortedMapData = new TreeMap<>(new MapKeyComparator());
        sortedMapData.putAll(params);
        try {
            for (Map.Entry<String, String> entry : sortedMapData.entrySet()) {
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(URLEncoder.encode(entry.getValue(), Constants.UTF_8));
                builder.append("&");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String paramStr = builder.toString();
        paramStr = paramStr.substring(0, paramStr.length() - 1);

        return paramStr;
    }

    public static String authorizeSign() {
        Map<String, String> params = new HashMap<>(5);
        params.put("client_id", Constants.CLIENT_ID);
        params.put("scope", "group,repo,topic,doc,artboard");
        params.put("code", generateCode());
        params.put("timestamp", System.currentTimeMillis() + "");
        params.put("response_type", "code");

        return HmacSha1.signSha1(params);
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

    private static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }
}
