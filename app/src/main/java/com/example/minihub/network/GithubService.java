package com.example.minihub.network;

import com.example.minihub.domain.FeedEvent;
import com.example.minihub.domain.Repository;
import com.example.minihub.domain.User;

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

    @GET("/users/vmaletsky/received_events") // TODO: make username parameter
    Call<FeedEvent[]> getUserEvents();

    @GET("/user/repos")
    Call<Repository[]> getUserRepos();

    @GET("/user/subscriptions")
    Call<Repository[]> getWatchedRepos();
}