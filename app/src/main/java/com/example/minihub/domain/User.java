package com.example.minihub.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by v.maletskiy on 8/17/2017.
 */

public class User {

    @SerializedName("login")
    public String login;

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("avatar_url")
    public String avatarUrl;

    @SerializedName("public_repos")
    public int publicRepos;

    @SerializedName("followers")
    public int followers;

    @SerializedName("following")
    public int following;

    @SerializedName("email")
    public String email;

    @SerializedName("location")
    public String location;
}
