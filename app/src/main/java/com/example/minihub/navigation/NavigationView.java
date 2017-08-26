package com.example.minihub.navigation;

import com.example.minihub.BasePresenter;
import com.example.minihub.BaseView;
import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by volod on 8/23/2017.
 */

public interface NavigationView extends MvpView {
    void openLoginActivity();
    void showUserInfo();
    void showRepositoriesList();
    void showFeed();
}
