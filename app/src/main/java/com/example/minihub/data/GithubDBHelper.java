package com.example.minihub.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daryna on 03.09.17.
 */

public class GithubDBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "minihub.db";

    public static final int DATABASE_VERSION = 7;

    public GithubDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " + UsersContract.UserColumns.TABLE_NAME + " (" +
                UsersContract.UserColumns.COLUMN_USER_ID + " INTEGER PRIMARY KEY, " +
                UsersContract.UserColumns.COLUMN_LOGIN + " TEXT NOT NULL, " +
                UsersContract.UserColumns.COLUMN_NAME + " TEXT, " +
                UsersContract.UserColumns.COLUMN_AVATAR_URL + " TEXT, " +
                UsersContract.UserColumns.COLUMN_PUBLIC_REPOS + " TEXT, " +
                UsersContract.UserColumns.COLUMN_EMAIL + " TEXT, " +
                UsersContract.UserColumns.COLUMN_FOLLOWERS + " INTEGER, " +
                UsersContract.UserColumns.COLUMN_FOLLOWING + " INTEGER, " +
                UsersContract.UserColumns.COLUMN_BIO + " TEXT, " +
                UsersContract.UserColumns.COLUMN_LOCATION + " TEXT " +
                " );";

        final String SQL_CREATE_REPOS_TABLE = "CREATE TABLE " + RepoContract.RepoColumns.TABLE_NAME + " (" +
                RepoContract.RepoColumns.COLUMN_REPO_ID + " INTEGER PRIMARY KEY, " +
                RepoContract.RepoColumns.COLUMN_NAME + " TEXT NOT NULL, " +
                RepoContract.RepoColumns.COLUMN_USER_ID + " INTEGER, " +
                RepoContract.RepoColumns.COLUMN_LANGUAGE + " TEXT, " +
                RepoContract.RepoColumns.COLUMN_TOPICS + " TEXT, " +
                RepoContract.RepoColumns.COLUMN_STARGAZERS_COUNT + " INTEGER, " +
                RepoContract.RepoColumns.COLUMN_FORKS_COUNT + " INTEGER, " +
                RepoContract.RepoColumns.COLUMN_WATCHERS_COUNT + " INTEGER, " +
                RepoContract.RepoColumns.COLUMN_STARGAZERS_URL + " TEXT, " +
                " FOREIGN KEY (" + RepoContract.RepoColumns.COLUMN_USER_ID + " ) REFERENCES " +
                UsersContract.UserColumns.TABLE_NAME + " (" + UsersContract.UserColumns.COLUMN_USER_ID + " ) " +
                " );";

        final String SQL_CREATE_EVENTS_TABLE = "CREATE TABLE " + EventsContract.EventColumns.TABLE_NAME + " (" +
                EventsContract.EventColumns.COLUMN_EVENT_ID + " INTEGER PRIMARY KEY, " +
                EventsContract.EventColumns.COLUMN_REPO_ID + " INTEGER, " +
                EventsContract.EventColumns.COLUMN_USER_ID + " INTEGER, " +
                EventsContract.EventColumns.COLUMN_TYPE + " TEXT NOT NULL, " +
                EventsContract.EventColumns.COLUMN_CREATED_AT + " TEXT NOT NULL," +
                " FOREIGN KEY (" + EventsContract.EventColumns.COLUMN_REPO_ID + " ) REFERENCES " +
                RepoContract.RepoColumns.TABLE_NAME + " (" + RepoContract.RepoColumns.COLUMN_REPO_ID + " ), " +
                " FOREIGN KEY (" + EventsContract.EventColumns.COLUMN_USER_ID + " ) REFERENCES " +
                UsersContract.UserColumns.TABLE_NAME + " (" + UsersContract.UserColumns.COLUMN_USER_ID + " ) " +

                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_EVENTS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REPOS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EventsContract.EventColumns.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UsersContract.UserColumns.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RepoContract.RepoColumns.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
