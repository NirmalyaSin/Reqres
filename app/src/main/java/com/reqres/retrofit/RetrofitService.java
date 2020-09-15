package com.reqres.retrofit;

import com.reqres.screens.login.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {

        @FormUrlEncoded
        @POST("login")
        Call<LoginResponse> userLogin(@Field("email") String email,
                                      @Field("password") String password);

    }