package com.example.minihub.domain;

import com.google.gson.annotations.SerializedName;

public class Repository {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("url")
    public String url;

    @SerializedName("stargazers_url")
    public String stargazersUrl;

    @SerializedName("stargazers_count")
    public int stargazersCount;

    @SerializedName("watchers_count")
    public int watchersCount;

    @SerializedName("topics")
    public String[] topics;

    @SerializedName("forks_count")
    public int forksCount;

    @SerializedName("subscribers_count")
    public int subscribersCount;

    @SerializedName("language")
    public String language;

    @SerializedName("fork")
    public boolean isFork;
}
