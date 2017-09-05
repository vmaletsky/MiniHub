package com.example.minihub.feed;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.minihub.R;
import com.example.minihub.data.EventsContract;
import com.example.minihub.data.RepoContract;
import com.example.minihub.data.UsersContract;
import com.example.minihub.domain.FeedEvent;
import com.example.minihub.sync.GithubSyncAdapter;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.minihub.network.ServiceGenerator.createService;



public class FeedFragment extends MvpFragment<FeedView, FeedPresenter> implements FeedView, LoaderManager.LoaderCallbacks<Cursor> {
    String TAG = getClass().getSimpleName();

    @BindView(R.id.feed_list)
    public ListView mFeedList;
    public FeedAdapter mFeedAdapter;

    private int mPosition = ListView.INVALID_POSITION;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public FeedPresenter createPresenter() {
        return new FeedPresenter();
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
        Timber.plant(new Timber.Tree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                Log.v(tag, message);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        presenter.getEvents();
        super.onResume();
    }

    @Override
    public String getAccessToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = sp.getString(getString(R.string.access_token_pref_id), null);
        return token;
    }

    public String EVENT_COLUMNS[] =  {
            EventsContract.EventColumns.TABLE_NAME + "." + EventsContract.EventColumns.COLUMN_EVENT_ID,
            EventsContract.EventColumns.COLUMN_TYPE,
            EventsContract.EventColumns.COLUMN_USER_ID,
            EventsContract.EventColumns.COLUMN_CREATED_AT,
            EventsContract.EventColumns.COLUMN_REPO_ID,
            UsersContract.UserColumns.COLUMN_NAME,
            UsersContract.UserColumns.COLUMN_AVATAR_URL,
            UsersContract.UserColumns.COLUMN_LOGIN,
            RepoContract.RepoColumns.COLUMN_NAME
    };

    static final int COL_EVENT_ID = 0;
    static final int COL_EVENT_TYPE = 1;
    static final int COL_USER_ID = 2;
    static final int COL_CREATED_AT = 3;
    static final int COL_REPO_ID = 4;
    static final int COL_USER_NAME = 5;
    static final int COL_AVATAR_URL = 6;
    static final int COL_USER_LOGIN = 7;
    static final int COL_REPO_NAME = 8;



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = EventsContract.EventColumns.COLUMN_CREATED_AT + " ASC";

        Uri eventsUri = EventsContract.EventColumns.CONTENT_URI;

        return new CursorLoader(getActivity(),
                eventsUri,
                EVENT_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFeedAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mFeedList.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFeedAdapter.swapCursor(null);
    }

    public void sync() {
        GithubSyncAdapter.syncImmediately(getActivity());
    }
}
