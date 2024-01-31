package com.tech.cybercars.data.remote.api;

import retrofit2.Response;

public interface ResSuccessCallback<R> {
    public void OnSuccess(Response<R> response);
}
