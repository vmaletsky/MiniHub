package com.example.minihub.feed;

import com.example.minihub.domain.FeedEvent;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

/**
 * Created by v.maletskiy on 8/21/2017.
 */

public interface FeedView extends MvpView {

    void sync();

    String getAccessToken();
}
