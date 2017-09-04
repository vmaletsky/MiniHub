package com.example.minihub.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by v.maletskiy on 8/29/2017.
 */

public class GithubProvider extends ContentProvider{
    public static final String CONTENT_AUTHORITY = "com.example.minihub.data";

    private GithubDBHelper mDBHelper;

    private static final UriMatcher mUriMatcher = buildUriMatcher();

    static final int EVENT = 100;
    static final int REPO = 200;
    static final int USER = 300;

    private static final SQLiteQueryBuilder queryBuilder;

    static {
        queryBuilder = new SQLiteQueryBuilder();

        // Join Events table with repo and user table
        queryBuilder.setTables(
                EventsContract.EventColumns.TABLE_NAME + " INNER JOIN " +
                        RepoContract.RepoColumns.TABLE_NAME  +
                        " ON " + EventsContract.EventColumns.TABLE_NAME + "." +
                        EventsContract.EventColumns.COLUMN_REPO_ID +
                        " = " + RepoContract.RepoColumns.TABLE_NAME +
                        "." + RepoContract.RepoColumns.COLUMN_REPO_ID +
                        " INNER JOIN " + UsersContract.UserColumns.TABLE_NAME +
                        " ON " + UsersContract.UserColumns.TABLE_NAME +
                        "." + UsersContract.UserColumns.COLUMN_USER_ID +
                        " = " + EventsContract.EventColumns.TABLE_NAME +
                        "." + EventsContract.EventColumns.COLUMN_USER_ID);
    }


    @Override
    public boolean onCreate() {
        mDBHelper = new GithubDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case USER :
                return UsersContract.UserColumns.CONTENT_TYPE;
            case REPO:
                return RepoContract.RepoColumns.CONTENT_TYPE;
            case EVENT:
                return EventsContract.EventColumns.CONTENT_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, UsersContract.PATH_USERS, USER);
        matcher.addURI(authority, RepoContract.PATH_REPOS, REPO);
        matcher.addURI(authority, EventsContract.PATH_EVENTS, EVENT);
        return matcher;
    }

}
