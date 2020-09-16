package com.reqres.retrofit;

import com.reqres.screens.home.adapter.UserListAdapter;
import com.reqres.screens.home.model.UserListResponse;
import com.reqres.screens.login.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {

        @FormUrlEncoded
        @POST("login")
        Call<LoginResponse> userLogin(@Field("email") String email,
                                      @Field("password") String password);

        @GET("users")
        Call<UserListResponse> getUserList(@Query("page") int page);

    }
