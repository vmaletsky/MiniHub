package com.example.minihub.feed;

import com.example.minihub.BasePresenter;
import com.example.minihub.BaseView;
import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by v.maletskiy on 8/21/2017.
 */

public interface FeedView extends MvpView {

    void showEvents();

    String getAccessToken();
}
