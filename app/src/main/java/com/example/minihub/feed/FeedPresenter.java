package com.example.minihub.feed;

import com.example.minihub.domain.FeedEvent;
import com.example.minihub.sync.GithubSyncAdapter;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;


/**
 * Created by v.maletskiy on 8/21/2017.
 */

public class FeedPresenter extends MvpBasePresenter<FeedView> {
    private String TAG = getClass().getSimpleName();



    public void getEvents() {
        getView().sync();
        getView().refreshLayout();
    }

    @Override
    public void detachView(boolean retainInstance) {

    }
}
