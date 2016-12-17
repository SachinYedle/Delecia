package com.delecia.retrofit.services;

import com.delecia.retrofit.responses.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by admin1 on 17/12/16.
 */

public interface UserDataService {
    @FormUrlEncoded
    @POST("delecia/public/login")
    Call<LoginResponse> login(@Field("phone") String phone,
                              @Field("password") String password);
}
