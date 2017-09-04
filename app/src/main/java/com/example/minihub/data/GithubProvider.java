package com.example.minihub.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (mUriMatcher.match(uri)) {
            case EVENT:
                retCursor = queryBuilder.query(mDBHelper.getReadableDatabase(), projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case USER:
                retCursor = mDBHelper.getReadableDatabase().query(
                        UsersContract.UserColumns.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case REPO:
                retCursor = mDBHelper.getReadableDatabase().query(
                        RepoContract.RepoColumns.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
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
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case EVENT: {
                long _id = db.insert(EventsContract.EventColumns.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = EventsContract.EventColumns.buildEventsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case USER: {
                long _id = db.insert(UsersContract.UserColumns.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = UsersContract.UserColumns.buildUsersUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REPO: {
                long _id = db.insert(RepoContract.RepoColumns.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = RepoContract.RepoColumns.buildReposUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int rowsDeleted;
        if ( null == selection ) selection = "1";
        switch (match) {
            case EVENT: {
                rowsDeleted = db.delete(EventsContract.EventColumns.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case USER: {
                rowsDeleted = db.delete(UsersContract.UserColumns.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case REPO: {
                rowsDeleted = db.delete(RepoContract.RepoColumns.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case EVENT: {
                rowsUpdated = db.update(EventsContract.EventColumns.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            }
            case USER: {
                rowsUpdated = db.update(UsersContract.UserColumns.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            }
            case REPO: {
                rowsUpdated = db.update(RepoContract.RepoColumns.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
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
