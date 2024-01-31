package com.tech.cybercars.data.remote.base;

import retrofit2.Response;

public interface CallServerStatus<T> {
    void CallSuccess(Response<T> response);
    void CallFail(Throwable error);
}
