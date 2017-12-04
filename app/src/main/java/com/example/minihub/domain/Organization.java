package com.example.minihub.domain;

import com.google.gson.annotations.SerializedName;


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
