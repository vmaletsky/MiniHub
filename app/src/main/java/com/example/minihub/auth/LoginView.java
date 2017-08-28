package com.example.minihub.auth;

import com.example.minihub.BasePresenter;
import com.example.minihub.BaseView;
import com.example.minihub.data.AccessToken;
import com.example.minihub.data.User;
import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by v.maletskiy on 8/21/2017.
 */

public interface LoginView extends MvpView {
    void saveAuthToken(AccessToken token);

    void openNavigationActivity();

    void saveUserInfo(User user);

    String getAccessToken();
}
