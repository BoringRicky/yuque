package vip.liteng.net;


import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author LiTeng
 * @date 2019-05-14
 */
public interface WanService {

    @GET("wxarticle/chapters/json")
    Call<Response> listWxArticle();
}
