package com.qinlong275.android.picu.common;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.qinlong275.android.picu.data.http.ApiService;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingleTons {

    private static Gson gson;
    private static Application mApplication;    //在Application中已经初始化
    private static OkHttpClient mOkhttpClient;
    private static Retrofit mRetrofit;
    private static ApiService mApiService;


    public static Gson getGson() {

        if (gson == null) {
            gson = new GsonBuilder()

                    .setDateFormat("yyyy-MM-dd hh:mm:ss")
                    .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                        @Override
                        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                            if (src == src.longValue())
                                return new JsonPrimitive(src.longValue());

                            return new JsonPrimitive(src);
                        }
                    })
                    .create();
        }
        return gson;
    }

    public static void setmApplication(Application mApplication) {
        SingleTons.mApplication = mApplication;
    }

    public static Application getmApplication() {
        return mApplication;
    }

    public static OkHttpClient getmOkhttpClient() {

        if (mOkhttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            // 如果使用到HTTPS，我们需要创建SSLSocketFactory，并设置到client
//        SSLSocketFactory sslSocketFactory = null;

            mOkhttpClient= builder
                    //添加公共参数
                    .addInterceptor(new CommonParamsInterceptor(getmApplication(), getGson()))

                    // 连接超时时间设置
                    .connectTimeout(10, TimeUnit.SECONDS)
                    // 读取超时时间设置
                    .readTimeout(10, TimeUnit.SECONDS)

                    .build();
        }
        return mOkhttpClient;
    }

    public static Retrofit getmRetrofit(){
        if (mRetrofit==null){
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(ApiService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getmOkhttpClient());
            mRetrofit= builder.build();
        }

        return mRetrofit;
    }

    public static ApiService getApiService(){
        if (mApiService==null){
            mApiService= getmRetrofit().create(ApiService.class);
        }
        return mApiService;
    }


}
