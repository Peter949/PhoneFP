package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface API
{
    @POST("auth/login")
    Call<param> poster(@Body param par);
}
