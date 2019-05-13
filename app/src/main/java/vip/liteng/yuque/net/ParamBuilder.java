package vip.liteng.yuque.net;

import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import vip.liteng.yuque.Constants;
import vip.liteng.yuque.net.sign.AbstractSign;
import vip.liteng.yuque.net.sign.HmacSha1;

/**
 * @author LiTeng
 * @date 2019-05-13
 */
public class ParamBuilder {

    public static String sortSignParams(Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> sortedMapData = new TreeMap<>(new MapKeyComparator());
        sortedMapData.putAll(params);
        for (Map.Entry<String, Object> entry : sortedMapData.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString()));
            builder.append("&");
        }

        String paramStr = builder.toString();
        paramStr = paramStr.substring(0, paramStr.length() - 1);

        return paramStr;
    }

    public static String authorizeSign() {
        Map<String, Object> params = new HashMap<>(5);
        params.put("client_id", Constants.CLIENT_ID);
        params.put("scope", "group,repo,topic,doc,artboard");
        params.put("code", AbstractSign.generateCode());
        params.put("timestamp", System.currentTimeMillis() + "");
        params.put("response_type", "code");

        return HmacSha1.signSha1(params);
    }

    private static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }
}
