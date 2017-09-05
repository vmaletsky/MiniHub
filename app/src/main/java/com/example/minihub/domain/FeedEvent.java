package com.example.minihub.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by v.maletskiy on 7/18/2017.
 */

public class FeedEvent {

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("actor")
    public User actor;

    @SerializedName("id")
    public long id;

    @SerializedName("type")
    public String type;

    @SerializedName("repo")
    public Repository repo;

    public Payload payload;
}
