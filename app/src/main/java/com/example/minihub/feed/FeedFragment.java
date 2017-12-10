package com.example.minihub.feed;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.minihub.R;
import com.example.minihub.Utilities;
import com.example.minihub.network.FeedAsyncTask;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.minihub.network.FeedAsyncTask.ERROR_INCORRECT_DATA;
import static com.example.minihub.network.FeedAsyncTask.ERROR_NO_NETWORK;

public class FeedFragment extends MvpFragment<FeedView, FeedPresenter>
        implements FeedView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
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
        return new FeedPresenter(getActivity(), mFeedAdapter, getActivity().getLoaderManager());
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
        mFeedAdapter.mOnLastElementReachedListener = new FeedAdapter.OnLastElementReached() {
            @Override
            public void onLastElementReached() {
                presenter.loadData(false, mPage + 1);
                mPage += 1;
            }
        };
        mFeedList.setAdapter(mFeedAdapter);
        mRefreshFeed.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        presenter.onActivityCreated(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.loadData(true, 0);
        mScrollListener.reset(0, true);
    }

    @Override
    public String getAccessToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = sp.getString(getString(R.string.access_token_pref_id), null);
        return token;
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        mRefreshFeed.setRefreshing(isRefreshing);
    }

    @Override
    public void setErrorMessage(@FeedAsyncTask.Error int errorCode) {
        Snackbar.make(mFeedList, Utilities.getErrorMessage(errorCode), BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setAction(R.string.retry_action, this)
                .show();
    }

    public void onRefresh() {
        mScrollListener.reset(0, true);
        presenter.loadData(true, 1);
    }

    @Override
    public void onClick(View v) {
        mScrollListener.reset(0, true);
        presenter.loadData(true, 1);
    }
}
