package com.example.minihub.feed;

import com.example.minihub.user_info.User;
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
    public int id;
}
