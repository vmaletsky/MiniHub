package com.example.minihub.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by v.maletskiy on 8/31/2017.
 */

public class Organization {
    @SerializedName("login")
    public String login;

    @SerializedName("id")
    public int id;

    @SerializedName("description")
    public String description;

    @SerializedName("avatar_url")
    public String avatarUrl;
}
