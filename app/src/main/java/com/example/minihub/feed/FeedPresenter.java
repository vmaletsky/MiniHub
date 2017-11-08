package com.example.minihub.feed;

import com.example.minihub.domain.FeedEvent;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;


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
