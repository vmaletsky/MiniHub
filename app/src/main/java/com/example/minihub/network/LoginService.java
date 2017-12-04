package com.example.minihub.network;

import com.example.minihub.domain.AccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface LoginService {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("login/oauth/access_token")
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code
    );
}