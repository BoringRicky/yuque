package vip.liteng.yuque.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import vip.liteng.log.Logger;
import vip.liteng.yuque.Constants;
import vip.liteng.yuque.net.sign.HmacSha1;

/**
 * @author LiTeng
 * @date 2019-05-13
 */
class ParamBuilder {

    private static final int CODE_LEN = 40;
    private static final char[] DIGITS_LOWER = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    static String generateOAuthUrl() {
        StringBuilder builder = new StringBuilder(Url.YU_QUE_API_OAUTH2);
        builder.append("?");
        builder.append(sortParams2String(authorizeParams(), false));

        String url = builder.toString();
        url = url.substring(0, url.length() - 1);

        Logger.e(url);

        return url;
    }

    private static Map<String, String> authorizeParams() {
        Map<String, String> params = new HashMap<>(5);
        params.put("client_id", Constants.CLIENT_ID);
        params.put("code", generateCode());
        params.put("response_type", "code");
        params.put("scope", "group,repo,topic,doc,artboard");
        params.put("timestamp", System.currentTimeMillis() + "");

        String sortedParams = sortParams2String(params, true);
        params.put("sign", HmacSha1.signSha1(sortedParams));

        return params;
    }

    private static String sortParams2String(Map<String, String> params, boolean encode) {
        StringBuilder builder = new StringBuilder();
        Map<String, String> sortedMapData = new TreeMap<>(new MapKeyComparator());
        sortedMapData.putAll(params);

        try {
            for (Map.Entry<String, String> entry : sortedMapData.entrySet()) {
                builder.append(entry.getKey());
                builder.append("=");
                String urlEncode = URLEncoder.encode(entry.getValue(), Constants.UTF_8);
                builder.append(encode ? urlEncode : entry.getValue());
                builder.append("&");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String paramStr = builder.toString();
        paramStr = paramStr.substring(0, paramStr.length() - 1);

        return paramStr;
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
