package com.example.minihub.network;

import com.example.minihub.domain.FeedEvent;
import com.example.minihub.domain.Repository;
import com.example.minihub.domain.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface GithubService {
    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/user")
    Call<User> getAuthenticatedUser();

    @GET("/users/vmaletsky/received_events") // TODO: make username parameter
    Call<List<FeedEvent>> getUserEvents(@Query("page") int page, @Query("per_page") int perPage);

    @GET("/user/repos")
    Call<Repository[]> getUserRepos();

    @GET("/user/subscriptions")
    Call<Repository[]> getWatchedRepos();
}
