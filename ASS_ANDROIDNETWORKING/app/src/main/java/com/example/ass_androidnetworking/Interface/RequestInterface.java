package com.example.ass_androidnetworking.Interface;

import com.example.ass_androidnetworking.DTO.ServerRequest;
import com.example.ass_androidnetworking.DTO.ServerResponse;
import com.example.ass_androidnetworking.DTO.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RequestInterface {

    @POST("register")
    Call<ServerResponse> register(@Body ServerRequest serverRequest);

    @POST("login")
    Call<ServerResponse> login(@Body ServerRequest serverRequest);

    @PUT("changePassword/{id}")
    Call<User> changePassword(@Path("id") String id, @Body User user);
}
