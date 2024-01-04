package com.tech.cybercars.data.remote.api;

public interface ResponseCallback<T> {
    void onResponse(T response);
    void onFailure(Throwable throwable);
}
