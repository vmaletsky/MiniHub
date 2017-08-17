package com.example.minihub;

import com.example.minihub.user_info.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by v.maletskiy on 8/17/2017.
 */

public interface GithubService {
    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/user")
    Call<User> getAuthenticatedUser();
}
