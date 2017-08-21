package com.example.minihub;

import com.example.minihub.feed.FeedEvent;
import com.example.minihub.data.Repository;
import com.example.minihub.data.User;

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
