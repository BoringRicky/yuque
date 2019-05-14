package vip.liteng.yuque.net;

import java.net.URLEncoder;

import vip.liteng.yuque.Constants;

/**
 * @author LiTeng
 * @date 2019-05-10
 */
public class Url {

    public static final String YU_QUE_API_OAUTH2 = "https://www.yuque.com/oauth2/authorize";
    public static final String YU_QUE_API_V2 = "https://www.yuque.com/api/v2/";
    public static final String REDIRECT_URI = "https://www.baidu.com";

    public static final String OAUTH2 = YU_QUE_API_OAUTH2
            + "?client_id=" + Constants.CLIENT_ID
            + "&scope=group,repo,topic,doc,artboard"
            + "&redirect_uri="+ REDIRECT_URI
            + "&state="+Constants.STATE
            + "&response_type=code";



    public static final String OAUTH2_NON_WEB = YU_QUE_API_OAUTH2
            + "?client_id=" + Constants.CLIENT_ID
            + "&code=" + ParamBuilder.generateCode()
            + "&response_type=code"
            + "&scope="+ URLEncoder.encode("group,repo,topic,doc,artboard")
            + "&timestamp=" + System.currentTimeMillis()
            + "&sign=" + ParamBuilder.authorizeSign()
            ;



}
