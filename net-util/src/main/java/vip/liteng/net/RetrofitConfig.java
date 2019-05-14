package vip.liteng.net;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author LiTeng
 * @date 2019-05-14
 */
public class RetrofitConfig {

    private static final String TAG = "RetrofitConfig";

    public static final String API_URL = "https://wanandroid.com";

    public static void setup() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(
                GsonConverterFactory.create()).build();

        WanService wanService = retrofit.create(WanService.class);
        Call<Response> listWxArticleCall = wanService.listWxArticle();
        try {
            Response response = listWxArticleCall.execute().body();

            Log.e(TAG,response.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
