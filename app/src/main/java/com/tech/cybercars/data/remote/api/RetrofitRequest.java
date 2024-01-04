package com.tech.cybercars.data.remote.api;

import com.tech.cybercars.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {
    private static Retrofit retrofit_client;
    public static Retrofit getInstance(){
        if(retrofit_client == null){
            retrofit_client = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit_client;
        }
        return retrofit_client;
    }
}
