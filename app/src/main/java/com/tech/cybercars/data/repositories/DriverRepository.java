package com.tech.cybercars.data.repositories;

import com.tech.cybercars.data.remote.retrofit.RetrofitRequest;
import com.tech.cybercars.data.remote.user.UserServiceRetrofit;
import com.tech.cybercars.data.repositories.base.BaseRepository;

public class DriverRepository extends BaseRepository {
    private final UserServiceRetrofit user_service;
    private static DriverRepository user_repository;

    public static DriverRepository GetInstance(){
        if(user_repository == null){
            user_repository = new DriverRepository();
        }
        return user_repository;
    }
    public DriverRepository() {
        user_service = RetrofitRequest.getInstance().create(UserServiceRetrofit.class);
    }
}
