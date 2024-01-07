package com.tech.cybercars.data.remote.api;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitResponse<R> {

    public Callback<R> GetResponse(ResSuccessCallback<R> success_callback, ResFailCallback fail_callback){
        return new Callback<R>() {
            @Override
            public void onResponse(@NonNull Call<R> call, @NonNull Response<R> response) {
                success_callback.OnSuccess(response);
            }
            @Override
            public void onFailure(@NonNull Call<R> call, @NonNull Throwable t) {
                fail_callback.OnFail(t);
            }
        };
    }
}
