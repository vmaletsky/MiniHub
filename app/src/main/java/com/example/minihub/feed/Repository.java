package com.example.minihub.feed;

import com.google.gson.annotations.SerializedName;

/**
 * Created by v.maletskiy on 8/18/2017.
 */

public class Repository {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("url")
    public String url;
}