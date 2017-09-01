package com.example.minihub.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by v.maletskiy on 8/29/2017.
 */
@ContentProvider(authority = GithubProvider.AUTHORITY, database = GithubDB.class)
public class GithubProvider {

    public static final String AUTHORITY = "com.example.minihub.data";

    @TableEndpoint(table = GithubDB.USERS)  public static class Users {
        @ContentUri(path="users", type="")
        public static final Uri USERS = Uri.parse("content://" + AUTHORITY + "/users");
    }

}
