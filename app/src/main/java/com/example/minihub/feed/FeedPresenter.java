package com.example.minihub.feed;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.example.minihub.data.EventsContract;
import com.example.minihub.data.RepoContract;
import com.example.minihub.data.UsersContract;
import com.example.minihub.domain.FeedEvent;
import com.example.minihub.domain.Repository;
import com.example.minihub.domain.User;
import com.example.minihub.network.FeedAsyncTask;
import com.example.minihub.widget.CollectionAppWidgetProvider;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;


public class FeedPresenter extends MvpBasePresenter<FeedView> implements LoaderManager.LoaderCallbacks<Cursor>  {
    private static final int FEED_LOADER = 200;
    private String TAG = getClass().getSimpleName();

    private FeedAsyncTask mFeedAsyncTask;

    private FeedAdapter mFeedAdapter;

    private Context mContext;

    private LoaderManager mLoaderManager;

    public String EVENT_COLUMNS[] =  {
            EventsContract.EventColumns.TABLE_NAME + "." + EventsContract.EventColumns.COLUMN_EVENT_ID + " AS _id",
            EventsContract.EventColumns.TABLE_NAME + "." + EventsContract.EventColumns.COLUMN_TYPE,
            EventsContract.EventColumns.TABLE_NAME + "." + EventsContract.EventColumns.COLUMN_USER_ID,
            EventsContract.EventColumns.TABLE_NAME + "." + EventsContract.EventColumns.COLUMN_CREATED_AT,
            EventsContract.EventColumns.TABLE_NAME + "." +EventsContract.EventColumns.COLUMN_REPO_ID,
            EventsContract.EventColumns.TABLE_NAME + "." + EventsContract.EventColumns.COLUMN_PAYLOAD_ACTION,
            EventsContract.EventColumns.TABLE_NAME + "." + EventsContract.EventColumns.COLUMN_PAYLOAD_REF_TAG,
            EventsContract.EventColumns.TABLE_NAME + "." + EventsContract.EventColumns.COLUMN_PAYLOAD_REF,
            EventsContract.EventColumns.TABLE_NAME + "." + EventsContract.EventColumns.COLUMN_PAYLOAD_SIZE,
            EventsContract.EventColumns.TABLE_NAME + "." + EventsContract.EventColumns.COLUMN_PAYLOAD_MERGED,
            UsersContract.UserColumns.TABLE_NAME + "." + UsersContract.UserColumns.COLUMN_NAME,
            UsersContract.UserColumns.TABLE_NAME + "." + UsersContract.UserColumns.COLUMN_AVATAR_URL,
            UsersContract.UserColumns.TABLE_NAME + "." + UsersContract.UserColumns.COLUMN_LOGIN,
            RepoContract.RepoColumns.TABLE_NAME + "." + RepoContract.RepoColumns.COLUMN_NAME
    };

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = EventsContract.EventColumns.COLUMN_CREATED_AT + " DESC";

        Uri eventsUri = EventsContract.EventColumns.CONTENT_URI;
        CursorLoader loader = new CursorLoader(mContext,
                eventsUri,
                EVENT_COLUMNS,
                null,
                null,
                sortOrder);
        return loader;
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
            mFeedAdapter.swapCursor(null);
    }


    public static final int COL_EVENT_ID = 0;
    public static final int COL_EVENT_TYPE = 1;
    public static final int COL_USER_ID = 2;
    public static final int COL_CREATED_AT = 3;
    public static final int COL_REPO_ID = 4;
    public static final int COL_ACTION = 5;
    public static final int COL_REF_TAG = 6;
    public static final int COL_REF = 7;
    public static final int COL_SIZE = 8;
    public static final int COL_MERGED = 9;
    public static final int COL_USER_NAME = 10;
    public static final int COL_AVATAR_URL = 11;
    public static final int COL_USER_LOGIN = 12;
    public static final int COL_REPO_NAME = 13;

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mLoaderManager.initLoader(FEED_LOADER, null, this);
    }


    FeedPresenter(Context context, FeedAdapter feedAdapter, LoaderManager loaderManager) {
        mContext = context;
        mFeedAdapter = feedAdapter;
        mLoaderManager = loaderManager;
    }


    public void loadData(final boolean isRefreshing, int page) {
        mFeedAsyncTask = new FeedAsyncTask(mContext, isRefreshing, page);
        mFeedAsyncTask.mListener = new FeedAsyncTask.OnLoad() {
            @Override
            public void onLoaded(List<FeedEvent> events) {
                cacheEvents(events, isRefreshing);
                CollectionAppWidgetProvider.sendRefreshBroadcast(mContext);
            }
        };
        mFeedAsyncTask.mErrorListener = new FeedAsyncTask.OnError() {
            @Override
            public void onError(int errorCode) {
                getView().setErrorMessage(errorCode);
                getView().setRefreshing(false);
            }
        };
        mFeedAsyncTask.execute();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == FEED_LOADER) {
            mFeedAdapter.swapCursor(data);
            Log.v(TAG, "onLoadFinished: " + String.valueOf(data.getCount()));
            if (getView() != null) {
                getView().setRefreshing(false);
            }
            if (data.moveToFirst()) {
                mFeedAdapter.notifyDataSetChanged();
            }
        }
    }

    private void cacheEvents(List<FeedEvent> events, boolean mIsRefreshing) {
        Vector<ContentValues> eventValuesVector = new Vector<ContentValues>(events.size());
        HashMap<Integer, Repository> repos = new HashMap<>();
        HashMap<Integer, User> users = new HashMap<>();
        for (FeedEvent event : events) {
            Log.v(TAG, " Event id " + event.id + " Repo id : " + event.repo.id + " user id : " + event.actor.id);
            users.put(event.actor.id, event.actor);
            repos.put(event.repo.id, event.repo);
            ContentValues eventValues = new ContentValues();
            eventValues.put(EventsContract.EventColumns.COLUMN_EVENT_ID, event.id);
            eventValues.put(EventsContract.EventColumns.COLUMN_REPO_ID, event.repo.id);
            eventValues.put(EventsContract.EventColumns.COLUMN_TYPE, event.type);
            eventValues.put(EventsContract.EventColumns.COLUMN_USER_ID, event.actor.id);
            eventValues.put(EventsContract.EventColumns.COLUMN_CREATED_AT, event.createdAt);
            eventValues.put(EventsContract.EventColumns.COLUMN_PAYLOAD_ACTION, event.payload.action);
            eventValues.put(EventsContract.EventColumns.COLUMN_PAYLOAD_REF_TAG, event.payload.ref_tag);
            eventValues.put(EventsContract.EventColumns.COLUMN_PAYLOAD_SIZE, event.payload.size);
            eventValues.put(EventsContract.EventColumns.COLUMN_PAYLOAD_REF, event.payload.ref);
            eventValues.put(EventsContract.EventColumns.COLUMN_PAYLOAD_MERGED, (event.payload.merged)?1:0);
            eventValuesVector.add(eventValues);
        }

        Vector<ContentValues> userValuesVector = new Vector<>(users.size());
        for (User user : users.values()) {
            ContentValues userValues = new ContentValues();
            userValues.put(UsersContract.UserColumns.COLUMN_USER_ID, user.id);
            userValues.put(UsersContract.UserColumns.COLUMN_EMAIL, user.email);
            userValues.put(UsersContract.UserColumns.COLUMN_LOGIN, user.login);
            userValues.put(UsersContract.UserColumns.COLUMN_NAME, user.name);
            userValues.put(UsersContract.UserColumns.COLUMN_EMAIL, user.email);
            userValues.put(UsersContract.UserColumns.COLUMN_AVATAR_URL, user.avatarUrl);
            userValues.put(UsersContract.UserColumns.COLUMN_BIO, user.bio);
            userValues.put(UsersContract.UserColumns.COLUMN_FOLLOWERS, user.followers);
            userValues.put(UsersContract.UserColumns.COLUMN_FOLLOWING, user.following);
            userValues.put(UsersContract.UserColumns.COLUMN_LOCATION, user.location);
            userValues.put(UsersContract.UserColumns.COLUMN_PUBLIC_REPOS, user.publicRepos);
            userValuesVector.add(userValues);
        }

        Vector<ContentValues> reposValuesVector = new Vector<>(repos.size());
        for (Repository repo : repos.values()) {
            ContentValues repoValues = new ContentValues();
            repoValues.put(RepoContract.RepoColumns.COLUMN_REPO_ID, repo.id);
            repoValues.put(RepoContract.RepoColumns.COLUMN_NAME, repo.name);
            repoValues.put(RepoContract.RepoColumns.COLUMN_LANGUAGE, repo.language);
            repoValues.put(RepoContract.RepoColumns.COLUMN_WATCHERS_COUNT, repo.watchersCount);
            repoValues.put(RepoContract.RepoColumns.COLUMN_STARGAZERS_COUNT, repo.stargazersCount);
            repoValues.put(RepoContract.RepoColumns.COLUMN_FORKS_COUNT, repo.forksCount);
            repoValues.put(RepoContract.RepoColumns.COLUMN_TOPICS,  implode(",", repo.topics));
            reposValuesVector.add(repoValues);
        }

        if (eventValuesVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[eventValuesVector.size()];
            eventValuesVector.toArray(cvArray);

            // delete old data
            if (mIsRefreshing) {
                mContext.getContentResolver().delete(EventsContract.EventColumns.CONTENT_URI, null, null);
            }
            mContext.getContentResolver().bulkInsert(EventsContract.EventColumns.CONTENT_URI, cvArray);
        }

        if (userValuesVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[userValuesVector.size()];
            userValuesVector.toArray(cvArray);

            // delete old data
            if (mIsRefreshing) {
                mContext.getContentResolver().delete(UsersContract.UserColumns.CONTENT_URI, null, null);
            }

            mContext.getContentResolver().bulkInsert(UsersContract.UserColumns.CONTENT_URI, cvArray);
        }
        if (reposValuesVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[reposValuesVector.size()];
            reposValuesVector.toArray(cvArray);

            // delete old data
         /*   if (mIsRefreshing) {
                mContext.getContentResolver().delete(RepoContract.RepoColumns.CONTENT_URI, null, null);
            }*/
            mContext.getContentResolver().bulkInsert(RepoContract.RepoColumns.CONTENT_URI, cvArray);
        }

        Log.d(TAG, "Sync complete. " + eventValuesVector.size() + " events inserted");
        Log.d(TAG, userValuesVector.size() + " users inserted");
        Log.d(TAG, reposValuesVector.size() + " repos inserted");
    }

    public static String implode(String separator, String... data) {
        if (data == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length - 1; i++) {
            //data.length - 1 => to not add separator at the end
            if (!data[i].matches(" *")) {//empty string are ""; " "; "  "; and so on
                sb.append(data[i]);
                sb.append(separator);
            }
        }
        sb.append(data[data.length - 1].trim());
        return sb.toString();
    }

}
