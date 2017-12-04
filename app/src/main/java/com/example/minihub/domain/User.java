package com.example.minihub.domain;

import com.google.gson.annotations.SerializedName;


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

    @SerializedName("followers_url")
    public String followersUrl;

    @SerializedName("following_url")
    public String followingUrl;

    @SerializedName("organizations_url")
    public String organizationsUrl;

    @SerializedName("email")
    public String email;

    @SerializedName("location")
    public String location;

    @SerializedName("bio")
    public String bio;


}
