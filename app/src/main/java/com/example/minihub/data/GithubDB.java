package com.example.minihub.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by v.maletskiy on 9/1/2017.
 */

@Database(version = GithubDB.VERSION)
public final class GithubDB {

    public static final int VERSION = 1;

    @Table(EventColumns.class) public static final String EVENTS = "events";

    @Table(UserColumns.class) public static final String USERS = "users";

    @Table(RepoColumns.class) public static final String REPOS = "repos";
}
