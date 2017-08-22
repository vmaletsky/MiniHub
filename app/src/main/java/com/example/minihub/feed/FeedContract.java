package com.example.minihub.feed;

import com.example.minihub.BasePresenter;
import com.example.minihub.BaseView;

/**
 * Created by v.maletskiy on 8/21/2017.
 */

public interface FeedContract {
    interface View extends BaseView<Presenter> {
        void showEvents();
    }

    interface Presenter extends BasePresenter {
        void getUserEvents();

    }
}
