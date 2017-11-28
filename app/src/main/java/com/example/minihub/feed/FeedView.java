package com.example.minihub.feed;

import com.example.minihub.domain.FeedEvent;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

public interface FeedView extends MvpView {

    String getAccessToken();

    void setRefreshing(boolean isRefreshing);
}
