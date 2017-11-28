package com.example.minihub.feed;

import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.content.CursorLoader;
import android.content.Loader;
import android.util.Log;

import com.example.minihub.data.EventsContract;
import com.example.minihub.data.RepoContract;
import com.example.minihub.data.UsersContract;
import com.example.minihub.domain.FeedEvent;
import com.example.minihub.network.FeedAsyncTask;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;


public class FeedPresenter extends MvpBasePresenter<FeedView> implements LoaderManager.LoaderCallbacks<Cursor>  {
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


    static final int COL_EVENT_ID = 0;
    static final int COL_EVENT_TYPE = 1;
    static final int COL_USER_ID = 2;
    static final int COL_CREATED_AT = 3;
    static final int COL_REPO_ID = 4;
    static final int COL_ACTION = 5;
    static final int COL_REF_TAG = 6;
    static final int COL_REF = 7;
    static final int COL_SIZE = 8;
    static final int COL_MERGED = 9;
    static final int COL_USER_NAME = 10;
    static final int COL_AVATAR_URL = 11;
    static final int COL_USER_LOGIN = 12;
    static final int COL_REPO_NAME = 13;

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mLoaderManager.initLoader(0, null, this);
    }


    FeedPresenter(Context context, FeedAdapter feedAdapter, LoaderManager loaderManager) {
        mContext = context;
        mFeedAdapter = feedAdapter;
        mLoaderManager = loaderManager;
    }


    public void loadData(boolean isRefreshing, int page) {
        mFeedAsyncTask = new FeedAsyncTask(mContext, isRefreshing, page);
        mFeedAsyncTask.mListener = new FeedAsyncTask.OnLoad() {
            @Override
            public void onLoaded() {

            }
        };
        mFeedAsyncTask.execute();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFeedAdapter.swapCursor(data);
        Log.v(TAG, "onLoadFinished: " + String.valueOf(data.getCount()));
        getView().setRefreshing(false);
        if (data.moveToFirst()) {
            mFeedAdapter.notifyDataSetChanged();
        }
    }


}
