package com.tech.cybercars.data.remote.retrofit;

import com.tech.cybercars.constant.URL;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {
    private static Retrofit retrofit_client;
    public static Retrofit getInstance(){
        if(retrofit_client == null){
            Interceptor interceptor = chain -> {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                builder.addHeader("Accept", "application/json");
                return chain.proceed(builder.build());
            };

            OkHttpClient.Builder ok_client_builder = new OkHttpClient.Builder().addInterceptor(interceptor);

            retrofit_client = new Retrofit.Builder()
                    .baseUrl(URL.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(ok_client_builder.build())
                    .build();
            return retrofit_client;
        }
        return retrofit_client;
    }
}
