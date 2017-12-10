package com.example.minihub.auth;

import com.example.minihub.domain.AccessToken;
import com.example.minihub.domain.User;
import com.hannesdorfmann.mosby3.mvp.MvpView;


public interface LoginView extends MvpView {
    void saveAuthToken(AccessToken token);

    void openNavigationActivity();

    String getAccessToken();
}
