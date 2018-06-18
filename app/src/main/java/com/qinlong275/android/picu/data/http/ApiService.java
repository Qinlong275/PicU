package com.qinlong275.android.picu.data.http;

import com.qinlong275.android.picu.bean.BaseBean;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import io.reactivex.Observable;

//与服务端交互的接口
public interface ApiService {


    public static final String BASE_URL = "http://112.124.22.238:8081/course_api/cniaoplay/";



//
//    @GET("featured")
//    public Call<PageBean<AppInfo>> getApps(@Query("p") String jsonParam);



    @GET("featured2")
    public Observable<BaseBean<String>> getApps(@Query("p") String jsonParam);


    @GET("index")
    public Observable<BaseBean<String>> index();



     @GET("toplist")
    public Observable<BaseBean<String>>topList(@Query("page") int page);

     @GET("game")
    public Observable<BaseBean<String>> games(@Query("page") int page);


    @POST("login")
    Observable<BaseBean<String>> login(@Body String param);


    @GET("category")
    Observable<BaseBean<String>> getCategories();


    @GET("category/featured/{categoryid}")
    Observable<BaseBean<String>> getFeaturedAppsByCategory(@Path("categoryid") int categoryid, @Query("page") int page);

    @GET("category/toplist/{categoryid}")
    Observable<BaseBean<String>> getTopListAppsByCategory(@Path("categoryid") int categoryid, @Query("page") int page);

    @GET("category/newlist/{categoryid}")
    Observable<BaseBean<String>> getNewListAppsByCategory(@Path("categoryid") int categoryid, @Query("page") int page);

    @GET("app/{id}")
    Observable<BaseBean<String>> getAppDetail(@Path("id") int id);
}
