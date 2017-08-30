package com.example.minihub.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by v.maletskiy on 8/29/2017.
 */

public class GithubDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "minihub.db";

    public static final int DATABASE_VERSION = 1;

    GithubDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_REPOS_TABLE = "CREATE TABLE " + RepoContract.TABLE_NAME
                + " (" +
                RepoContract.RepoEntry.COLUMN_REPO_ID + " INTEGER PRIMARY KEY, " +
                RepoContract.RepoEntry.COLUMN_NAME + " TEXT" +
                " );";
        db.execSQL(SQL_CREATE_REPOS_TABLE);


        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " + UserContract.TABLE_NAME
                + " (" +
                UserContract.UserEntry.COLUMN_USER_ID + " INTEGER PRIMARY KEY, " +
                UserContract.UserEntry.COLUMN_LOGIN + " TEXT, " +
                UserContract.UserEntry.COLUMN_AVATAR_URL + " TEXT, " +
                UserContract.UserEntry.COLUMN_NAME + " TEXT" +
                " );";
        db.execSQL(SQL_CREATE_USERS_TABLE);

        final String SQL_CREATE_EVENTS_TABLE = "CREATE TABLE " + EventContract.TABLE_NAME
                + " (" +
                EventE
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
