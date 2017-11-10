package com.example.minihub.feed;

import android.content.Context;

import com.example.minihub.domain.FeedEvent;
import com.example.minihub.network.FeedAsyncTask;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;


public class FeedPresenter extends MvpBasePresenter<FeedView> {
    private String TAG = getClass().getSimpleName();

    FeedAsyncTask mFeedAsyncTask;

    Context mContext;

    FeedPresenter(Context context) {
        mContext = context;
    }


    public void loadData(boolean isRefreshing, int page) {
        mFeedAsyncTask = new FeedAsyncTask(mContext, isRefreshing, page);
        mFeedAsyncTask.execute();
    }


}
