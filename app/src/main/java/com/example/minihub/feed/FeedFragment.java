package com.example.minihub.feed;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.minihub.R;
import com.example.minihub.data.EventsContract;
import com.example.minihub.data.RepoContract;
import com.example.minihub.data.UsersContract;
import com.example.minihub.domain.FeedEvent;
import com.example.minihub.network.FeedAsyncTask;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class FeedFragment extends MvpFragment<FeedView, FeedPresenter>
        implements FeedView, LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {
    String TAG = getClass().getSimpleName();
    @BindView(R.id.feed_list)
    public RecyclerView mFeedList;
    public FeedAdapter mFeedAdapter;

    EndlessRecyclerOnScrollListener mScrollListener;

    private int mPage;

    @BindView(R.id.refresh_feed)
    public SwipeRefreshLayout mRefreshFeed;


    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public FeedPresenter createPresenter() {
        return new FeedPresenter(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, view);
        mPage = 1;
        mFeedAdapter = new FeedAdapter(getActivity());
        Log.v(TAG, String.valueOf(mFeedAdapter.getItemCount()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mFeedList.setLayoutManager(layoutManager);
        mScrollListener = new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                Log.v(TAG, "onLoadMore");
                presenter.loadData(false, mPage + 1);
                mPage += 1;
            }
        };
        mFeedList.addOnScrollListener(mScrollListener);
        mFeedList.setAdapter(mFeedAdapter);
        Timber.plant(new Timber.Tree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                Log.v(tag, message);
            }
        });
        mRefreshFeed.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        mScrollListener.reset(0, true);
        presenter.loadData(true, 1);
        getLoaderManager().restartLoader(0, null, this);

    }


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



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = EventsContract.EventColumns.COLUMN_CREATED_AT + " DESC";

        Uri eventsUri = EventsContract.EventColumns.CONTENT_URI;
        CursorLoader loader = new CursorLoader(getActivity(),
                eventsUri,
                EVENT_COLUMNS,
                null,
                null,
                sortOrder);
        return loader;
    }

    @Override
    public String getAccessToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = sp.getString(getString(R.string.access_token_pref_id), null);
        return token;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFeedAdapter.swapCursor(data);
        Log.v(TAG, "onLoadFinished: " + String.valueOf(data.getCount()));
        mRefreshFeed.setRefreshing(false);
        if (data.moveToFirst()) {
            mFeedAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFeedAdapter.swapCursor(null);
    }

    public void onRefresh() {
        mScrollListener.reset(0, true);
        presenter.loadData(true, 1);
    }
}
